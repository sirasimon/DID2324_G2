#include <Wire.h>
#include <ESP8266WiFi.h>
#include <Firebase_ESP_Client.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <qrcode.h>

//CONSTANTS
#define ARDUINO_ADDRESS 8
#define OLED_ADRRESS 9
#define OLED_WIDTH 128
#define OLED_HEIGHT 64
#define WIFI_SSID "Mattia"
#define WIFI_PASSWORD "sicurezza"
#define DATABASE_URL "did23-shopdrop-default-rtdb.firebaseio.com"
#define L1_OPENING_DATA_PATH "/lockers/LTO_02/compartments/1/isOpen"
#define L2_OPENING_DATA_PATH "/lockers/LTO_02/compartments/2/isOpen"
const char l1OpenCloseCom = 'a';
const char l2OpenCloseCom = 'b';
const char espStartedCom = 'r';
const int sclPin = 5;
const int sdaPin = 4;
const byte pirInput = 13;

//GLOBAL VARIABLES
Adafruit_SSD1306 display(OLED_WIDTH, OLED_HEIGHT, &Wire, -1);
String read = "null";
bool valPIR;
bool isOpenChangedL1;
bool isOpenChangedL2;
bool isOpenL1;
bool isOpenL2;
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;
FirebaseData l1Stream;
FirebaseData l2Stream;

//DATA INPUT CALLBACK
void l1StreamCallback(FirebaseStream data) {
  Serial.printf("Data L1 has changed! %s\n", data.boolData()? "true" : "false");
  isOpenChangedL1 = true;
  isOpenL1 = data.boolData();
}
void l2StreamCallback(FirebaseStream data) {
  Serial.printf("Data L2 has changed! %s\n", data.boolData()? "true" : "false");
  isOpenChangedL2 = true;
  isOpenL2 = data.boolData();
}
//DATA TIEMOUT CALLBACK
void l1StreamTimeoutCallback(bool timeout) {
  if (timeout)
    Serial.println("Stream L1 is timed out... resuming");
  if (!l1Stream.httpConnected())
    Serial.printf("An error has occured for stream L1 - error code: %d, reason: %s\n\n", l1Stream.httpCode(), l1Stream.errorReason().c_str());
}
void l2StreamTimeoutCallback(bool timeout) {
  if (timeout)
    Serial.println("Stream is timed out... resuming");
  if (!l2Stream.httpConnected())
    Serial.printf("An error has occured for stream L2 - error code: %d, reason: %s\n\n", l2Stream.httpCode(), l2Stream.errorReason().c_str());
}

//QR CODE GENERATION
QRCode GenerateQR() {
  QRCode qrcode;
  uint8_t qrcodeBytes[qrcode_getBufferSize(3)];
  qrcode_initText(&qrcode, qrcodeBytes, 1, ECC_HIGH, "12"); //Value of the qr
  return qrcode;
}

//QR CODE DISPLAY
void DisplayQR(QRCode qrcode) {
  int scale = min(48 / qrcode.size, 128 / qrcode.size);
  int shiftX = (OLED_WIDTH - qrcode.size * scale) / 2;
  int shiftY = (OLED_HEIGHT - qrcode.size * scale) / 2 + 6;
  for (uint8_t y = 0; y < qrcode.size; y++) {
    for (uint8_t x = 0; x < qrcode.size; x++) {
      if (qrcode_getModule(&qrcode, x, y)) {
        display.fillRect(shiftX + x*scale, shiftY + y*scale, scale, scale, WHITE);
      } 
    }
  }
  display.display();
}

//SETUP
void setup() {
  //Serial setup
  Serial.begin(115200);
  delay(1000);
  Serial.println(" ");
  Serial.println("=======================================================================");
  Serial.println("ESP STARTING");

  //I2C Master setup
  Wire.begin(sdaPin, sclPin);
  Serial.println("I2C started");

  //Wifi connection setup
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(300);
  }
  Serial.print("\n");
  Serial.println("Connection with server succesful");

  //Oled setup
  display.begin(SSD1306_SWITCHCAPVCC, 0x3C);
  display.display();
  delay(1000);
  display.clearDisplay();

  //Firebase connection setup
  config.database_url = DATABASE_URL;
  config.signer.test_mode = true;
  Firebase.reconnectNetwork(true);
  fbdo.setBSSLBufferSize(512 /* Rx buffer size in bytes from 512 - 16384 */, 512 /* Tx buffer size in bytes from 512 - 16384 */);
  Firebase.begin(&config, &auth);
  if (!Firebase.RTDB.beginStream(&l1Stream, F(L1_OPENING_DATA_PATH)))
    Serial.printf("Stream L1 start error: %s\n\n", l1Stream.errorReason().c_str());
  Firebase.RTDB.setStreamCallback(&l1Stream, l1StreamCallback, l1StreamTimeoutCallback);
  if (!Firebase.RTDB.beginStream(&l2Stream, F(L2_OPENING_DATA_PATH)))
    Serial.printf("Stream L2 start error: %s\n\n", l2Stream.errorReason().c_str());
  Firebase.RTDB.setStreamCallback(&l2Stream, l2StreamCallback, l2StreamTimeoutCallback);
  Firebase.RTDB.setBool(&fbdo, L1_OPENING_DATA_PATH, false);
  Firebase.RTDB.setBool(&fbdo, L2_OPENING_DATA_PATH, false);
  Serial.println("Connection with database succesful");

  //Starting confirm to UNO
  Wire.beginTransmission(ARDUINO_ADDRESS);
  Wire.write(espStartedCom);
  Wire.endTransmission();
  Serial.println("ESP STARTED");

  //Pir setup
  pinMode(pirInput, INPUT);

  Serial.print("\n");
}

//LOOP
void loop() {
  //Opening com to UNO
  if (isOpenChangedL1 && isOpenL1) {
    Serial.println("Sending open command to L1");
    Wire.beginTransmission(ARDUINO_ADDRESS);
    Wire.write(l1OpenCloseCom);
    Wire.endTransmission();
  }
  isOpenChangedL1 = false;
  if (isOpenChangedL2 && isOpenL2) {
    Serial.println("Sending open command to L2");
    Wire.beginTransmission(ARDUINO_ADDRESS);
    Wire.write(l2OpenCloseCom);
    Wire.endTransmission();
  }
  isOpenChangedL2 = false;

  //Locker opening status from UNO
  Wire.requestFrom(8, 2);
  bool store = false;
  while (Wire.available() > 0) {
    char data1 = char(Wire.read());
    if (data1 == l1OpenCloseCom && isOpenL1) {
      Serial.println("L1 has been closed");
      store = true;
      //Serial.printf("Set bool... %s\n", Firebase.RTDB.setBool(&fbdo, "/lockers/LTO_02/compartments/1/isOpen", false) ? "ok" : fbdo.errorReason().c_str());      
      isOpenL1 = false;
    }
    char data2 = char(Wire.read());
    if (data2 == l2OpenCloseCom && isOpenL2) {
      Firebase.RTDB.setBool(&fbdo, L2_OPENING_DATA_PATH, false);
      isOpenL2 = false;
      Serial.println("L2 has been closed");
    }
  }
  if (store) {
    Serial.printf("Set bool... %s\n", Firebase.RTDB.setBool(&fbdo, "/lockers/LTO_02/compartments/1/isOpen", false) ? "ok" : fbdo.errorReason().c_str());  
  }

  //QR generation and display
  if (digitalRead(pirInput)) {
    Serial.println(String(millis()));
  }
}
  /*
  //READ FROM LOCKER
  Wire.requestFrom(8, 1);
  while (Wire.available() > 0) {
    char c = Wire.read();
    if (c == 'c' || c == 'o') {
      Serial.println("Closed: " + String(c));
    }
  }
  //SEND TERMINAL INPUT
  while (Serial.available() > 0) 
  {
    char c = Serial.read();
    Serial.println("Sending: " + String(c));
    Wire.beginTransmission(ARDUINO_ADDRESS);
    Wire.write(c);
    Wire.endTransmission();
  }
  */