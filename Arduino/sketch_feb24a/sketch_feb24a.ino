#include <ESP8266WiFi.h>

void setup() {
  Serial.begin(115200);
  delay(1000);
  pinMode(15, INPUT);

  Serial.println("---------");
}

void loop() {

  Serial.println(String(digitalRead(15)));

}