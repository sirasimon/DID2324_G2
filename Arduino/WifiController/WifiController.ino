//#include <ESP8266WiFi.h>
//#include <Firebase_ESP_Client.h>
#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <qrcode.h>

#define ARDUINO_ADDRESS 8
#define OLED_ADRRESS 9
#define OLED_WIDTH 128
#define OLED_HEIGHT 64

const int sclPin = 5;
const int sdaPin = 4;
const byte pinInput = 15;

String read = "null";
Adafruit_SSD1306 display(OLED_WIDTH, OLED_HEIGHT, &Wire, -1);
bool valPIR;

void setup() {
  Serial.begin(115200);
  Serial.println("STARTING ESP");
  delay(3000);
  Serial.println("STARTED ESP");
 // Wire.begin(sdaPin, sclPin);
  //QR AND OLED TEST
  display.begin(SSD1306_SWITCHCAPVCC, 0x3C);
  display.display();
  delay(3000);
  display.clearDisplay();
  QRCode testCode = GenerateQR();
  DisplayQR(testCode);

/*
  //PIR
  pinMode(pinInput, INPUT);
  */

}


void loop() {


  valPIR = digitalRead(pinInput);
  Serial.println(valPIR);
  
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
}

QRCode GenerateQR() {
  QRCode qrcode;
  uint8_t qrcodeBytes[qrcode_getBufferSize(3)];
  qrcode_initText(&qrcode, qrcodeBytes, 1, ECC_HIGH, "12"); //Value of the qr

  return qrcode;
}


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



