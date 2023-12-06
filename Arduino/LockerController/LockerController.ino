//------------------------------------------------------------------------------------------------IMPORTS
#include <Servo.h>
#include <MagneticSensor.h>
#include <CustomTimer.h>
#include <CustomServo.h>
#include <Locker.h>

Servo servo1;
Servo servo2;
MagneticSensor sensor1(A0, 100);
MagneticSensor sensor2(A1, 100);
CustomServo customServo1(servo1, 9, 10000, 1);
CustomServo customServo2(servo2, 10, 5000, 1);
Locker locker1(customServo1, sensor1, 'A');
Locker locker2(customServo2, sensor2, 'B');
int charCounter = 0;
int locker;

void setup() {
  Serial.begin(9600);
  Serial.println("START");

  sensor1.init();
  sensor2.init();
  customServo1.init();
  customServo2.init();
  locker1.init();
  locker2.init();
}

void loop() {
  input_manager();
  locker1.update();
  locker2.update();
}

void input_manager() {
  while (Serial.available()) {
    Serial.println(charCounter);
    char c = Serial.read();
    if (charCounter == 0) {
      if (c == locker1._id)
        locker = 1;
      else if (c == locker2._id)
        locker = 2;
    }
    else if (c != '\n') {
      if (locker == 1)
      {
        Serial.println("sent input to locker1 " + String(c));
        locker1.send_input(c);
      }
      }
      else if (locker == 2)
        locker2.send_input(c);
  charCounter++;
  if (c == '\n')
    charCounter = 0;
  }
}