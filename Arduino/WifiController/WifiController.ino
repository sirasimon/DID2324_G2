#include <Wire.h>
#include <ESP8266WiFi.h>
#include <Firebase_ESP_Client.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SH110X.h>
#include <splash.h>
#include <qrcode.h>
#include <CustomTimer.h> 

//CONSTANTS
#define ARDUINO_ADDRESS 8
#define OLED_ADRRESS 9
#define OLED_WIDTH 128
#define OLED_HEIGHT 64
#define WIFI_SSID "Mattia"
#define WIFI_PASSWORD "sicurezza"
#define DATABASE_URL "did23-shopdrop-default-rtdb.firebaseio.com"
#define L1_OPENING_DATA_PATH "/lockers/LTO_01/compartments/1/isOpen"
#define L2_OPENING_DATA_PATH "/lockers/LTO_01/compartments/2/isOpen"
#define OTP_DATA_PATH "/lockers/LTO_01/otp"
#define L1_ALARM_PATH "/lockers/LTO_01/compartments/1/alarmOn"
#define L2_ALARM_PATH "/lockers/LTO_01/compartments/2/alarmOn"
 
const char l1OpenCloseCom = 'a';
const char l2OpenCloseCom = 'b';
const char espStartedCom = 'r';
const int sclPin = 5;
const int sdaPin = 4;
const byte pirInput = 15;
//GLOBAL VARIABLES
Adafruit_SH1106G display(OLED_WIDTH, OLED_HEIGHT, &Wire, -1);
String read = "null";
bool valPIR;
bool isOpenChangedL1;
bool isOpenChangedL2;
bool isOpenL1;
bool isOpenL2;
bool isReallyOpenL1;
bool isReallyOpenL2;
bool leftOpen;
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;
FirebaseData l1Stream;
FirebaseData l2Stream;

bool isOledOn;
CustomTimer oledTimerToCode(30000);
CustomTimer oledTimerToTurnOff(40000);
CustomTimer alarmTimer1(120000);
CustomTimer alarmTimer2(150000);
CustomTimer tooLateTimer(180000);
CustomTimer buzzerTimer1(1000);
CustomTimer buzzerTimer2(400);

String currentCode;
int currentCodeNumber;
QRCode currentQr;
bool showingCode;
bool showingInstructions;
bool l1AlarmOn;
bool l2AlarmOn;
const int buzzerPin = 12;
const int note = 1046;

//SETUP
void setup() {
  //Serial setup
  Serial.begin(115200);
  delay(5000);
  Serial.println(" ");
  Serial.println("=======================================================================");
  Serial.println("ESP STARTING");

  //Wifi connection setup
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(300);
  }
  Serial.print("\n");
  Serial.println("Connection with server succesful");

  //I2C Master setup
  Wire.begin(sdaPin, sclPin);
  Serial.println("I2C started");
  delay(1000);

  //Oled setup
  display.begin(0x3C, true);
  display.display();
  delay(3000);
  sleepDisplay(&display);

  //Buzzer setup
  pinMode(buzzerPin, OUTPUT);
  
  //Firebase connection setup
  config.database_url = DATABASE_URL;
  config.signer.test_mode = true;
  Firebase.reconnectNetwork(true);
  fbdo.setBSSLBufferSize(512 /* Rx buffer size in bytes from 512 - 16384 */, 512 /* Tx buffer size in bytes from 512 - 16384 */);
  Firebase.begin(&config, &auth);
  if (!Firebase.RTDB.beginStream(&l1Stream, F(L1_OPENING_DATA_PATH)))
  {
    Serial.printf("Stream L1 start error: %s\n\n", l1Stream.errorReason().c_str());
    tone(buzzerPin, 500, 5000);
  }
  Firebase.RTDB.setStreamCallback(&l1Stream, l1StreamCallback, l1StreamTimeoutCallback);
  if (!Firebase.RTDB.beginStream(&l2Stream, F(L2_OPENING_DATA_PATH))) {
    Serial.printf("Stream L2 start error: %s\n\n", l2Stream.errorReason().c_str());
    tone(buzzerPin, 500, 5000);
  }
  Firebase.RTDB.setStreamCallback(&l2Stream, l2StreamCallback, l2StreamTimeoutCallback);
  Firebase.RTDB.setBool(&fbdo, L1_OPENING_DATA_PATH, false);
  Firebase.RTDB.setBool(&fbdo, L2_OPENING_DATA_PATH, false);
  Serial.println("Connection with database succesful");

  //Pir setup
  pinMode(pirInput, INPUT);

  Serial.println("ESP STARTED");
  Serial.print("\n");

  tone(buzzerPin, note, 500);
}

//LOOP
void loop() {
  //Opening com to UNO
  if (isOpenChangedL1 && isOpenL1) {
    //Serial.println("Sending open command to L1");
    Wire.beginTransmission(ARDUINO_ADDRESS);
    Wire.write(l1OpenCloseCom);
    Wire.endTransmission();
  }

  if (isOpenChangedL2 && isOpenL2) {
    //Serial.println("Sending open command to L2");
    Wire.beginTransmission(ARDUINO_ADDRESS);
    Wire.write(l2OpenCloseCom);
    Wire.endTransmission();
  }

  //Locker opening status from UNO
  Wire.requestFrom(8, 2);
  while (Wire.available() > 0) {
    char data1 = char(Wire.read());
    if (data1 == l1OpenCloseCom && isReallyOpenL1) {
      Serial.println("L1 has been closed");
      Serial.printf("Set bool... %s\n", Firebase.RTDB.setBool(&fbdo, L1_OPENING_DATA_PATH, false) ? "ok" : fbdo.errorReason().c_str());     
      Serial.printf("Set bool... %s\n", Firebase.RTDB.setBool(&fbdo, L2_ALARM_PATH, false) ? "ok" : fbdo.errorReason().c_str());   
      isOpenL1 = false;
      isReallyOpenL1 = false;
      leftOpen = false;
    }
    else if (data1 != l1OpenCloseCom) {
      isReallyOpenL1 = true;
    }
    char data2 = char(Wire.read());
    if (data2 == l2OpenCloseCom && isReallyOpenL2) {
      Serial.println("L2 has been closed");
      Serial.printf("Set bool... %s\n", Firebase.RTDB.setBool(&fbdo, L2_OPENING_DATA_PATH, false) ? "ok" : fbdo.errorReason().c_str()); 
      Serial.printf("Set bool... %s\n", Firebase.RTDB.setBool(&fbdo, L2_ALARM_PATH, false) ? "ok" : fbdo.errorReason().c_str());       
      isOpenL2 = false;
      isReallyOpenL2 = false;
      leftOpen = false;
    }
    else if (data2 != l2OpenCloseCom) {
      isReallyOpenL2 = true;
    }
  }

  //QR generation and display
  if (digitalRead(pirInput)) {
    InfraredTriggered();
  }

  if (isOledOn) {
      OledManagement();
  }

  if (isOpenL1 || isOpenL2) {
    AlarmManagement();
  }

  isOpenChangedL1 = false;
  isOpenChangedL2 = false;
}

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
  if (!l1Stream.httpConnected()) {
    tone(buzzerPin, 500, 5000);
    Serial.printf("An error has occured for stream L1 - error code: %d, reason: %s\n\n", l1Stream.httpCode(), l1Stream.errorReason().c_str());
  }
}
void l2StreamTimeoutCallback(bool timeout) {
  if (timeout)
    Serial.println("Stream is timed out... resuming");
  if (!l2Stream.httpConnected()) {
    tone(buzzerPin, 500, 5000);
    Serial.printf("An error has occured for stream L2 - error code: %d, reason: %s\n\n", l2Stream.httpCode(), l2Stream.errorReason().c_str());
  }
}

void DisplayTextCenter(const char* text, int x, int y) {
  Serial.println("Showing text centered");
  display.clearDisplay();
  display.setCursor(32 ,32);
  display.setTextSize(2);
  display.setTextColor(SH110X_WHITE);
  display.print(text);
  display.display();
}

void InfraredTriggered() {
  if (isOpenL1 || isOpenL2) 
    return;
  if (!isOledOn) {
    wakeDisplay(&display);
    Serial.println("Oled is now on");
    currentQr = GenerateCode();
    //DisplayQR(currentQr);
    //oledTimerToCode.Update();
    isOledOn = true;
  }
  oledTimerToTurnOff.Update();
}

//QR CODE GENERATION
QRCode GenerateCode() {
  QRCode qrcode;
  uint8_t qrcodeBytes[qrcode_getBufferSize(3)];
  currentCodeNumber = random(1000, 9999);
  currentCode = String(currentCodeNumber);
  String codeString = "LTO_01" + currentCode;
  Serial.println(codeString);
  qrcode_initText(&qrcode, qrcodeBytes, 2, ECC_LOW, codeString.c_str()); //Value of the qr
  Serial.printf("Set int... %s\n", Firebase.RTDB.setString(&fbdo, OTP_DATA_PATH, String(currentCodeNumber)) ? "ok" : fbdo.errorReason().c_str());  
  display.clearDisplay();
  int scale = min(OLED_WIDTH / qrcode.size, OLED_HEIGHT / qrcode.size);
  int shiftX = (OLED_WIDTH - qrcode.size * scale) / 2;
  int shiftY = (OLED_HEIGHT - qrcode.size * scale) / 2;
  display.fillRect(0, 0, OLED_WIDTH, OLED_HEIGHT, SH110X_WHITE);
  for (uint8_t y = 0; y < qrcode.size; y++) {
    for (uint8_t x = 0; x < qrcode.size; x++) {
      if (qrcode_getModule(&qrcode, x, y)) {
        display.fillRect(shiftX + x*scale, shiftY + y*scale, scale, scale, SH110X_BLACK);
      } 
    }
  }
  display.display();
  return qrcode;
}

void OledManagement() {
  if (!isOledOn)
    return; 
  if ((isOpenL1 || isOpenL2) && !showingInstructions) {
    String text = "Aperto";
    DisplayTextCenter(text.c_str(), OLED_WIDTH / 2, OLED_HEIGHT / 2);
    showingInstructions = true;
  }
  /*
  if (oledTimerToCode.check() && !showingCode && !showingInstructions) {
    DisplayTextCenter(currentCode.c_str(), OLED_WIDTH / 2, OLED_HEIGHT / 2);
    showingCode = true;
  }
  */
  if ((oledTimerToTurnOff.check() && !isOpenL1 && !isOpenL2) || (!isOpenL1 && !isOpenL2 && showingInstructions)) {
    sleepDisplay(&display);
    isOledOn = false;
    showingCode = false;
    showingInstructions = false;
  }
}

void AlarmManagement() { 
  if (isOpenChangedL1 || isOpenChangedL2) {
    alarmTimer1.Update();
    alarmTimer2.Update();
    tooLateTimer.Update();
  }
  if (alarmTimer1.check() && !alarmTimer2.check()) {
    playAlarm1();
  }
  else if (alarmTimer2.check() && !tooLateTimer.check()) {
    playAlarm2();
  }
  else if (tooLateTimer.check() && !leftOpen) {
    if (isOpenL1) {
      Serial.printf("Set bool... %s\n", Firebase.RTDB.setBool(&fbdo, L1_ALARM_PATH, true) ? "ok" : fbdo.errorReason().c_str());
    }
    else if (isOpenL2) {
      Serial.printf("Set bool... %s\n", Firebase.RTDB.setBool(&fbdo, L2_ALARM_PATH, true) ? "ok" : fbdo.errorReason().c_str());
    }
    leftOpen = true;
  }
}

void sleepDisplay(Adafruit_SH1106G* display) {
  display->oled_command(SH110X_DISPLAYOFF);
}

void wakeDisplay(Adafruit_SH1106G* display) {
  display->clearDisplay();
  display->display();
  display->oled_command(SH110X_DISPLAYON);
}

void playAlarm1() {
  if (buzzerTimer1.checkAndUpdate()) {
    tone(buzzerPin, note, 500);
    Serial.println("PLAYING BIP 1");
  }
}
void playAlarm2() {
  if (buzzerTimer2.checkAndUpdate()) {
    tone(buzzerPin, note, 200);
    Serial.println("playinb bip 2");
  }
}