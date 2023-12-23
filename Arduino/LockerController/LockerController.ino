//------------------------------------------------------------------------------------------------IMPORTS
#include <Servo.h>
#include <MagneticSensor.h>
#include <CustomTimer.h> 
#include <CustomServo.h>
#include <Locker.h>

Servo doorServoA;
Servo bumpServoA;
MagneticSensor doorSensorA(A0, 15);
CustomServo doorCustomServoA(doorServoA, 11, 200, 1);
CustomServo bumpCustomServoA(bumpServoA, 10, 400, 1);
Locker lockerA(doorCustomServoA, bumpCustomServoA, doorSensorA, 'A');
int charCounter = 0;
int locker;

void setup() {
  Serial.begin(9600);
  Serial.println("START");

  doorSensorA.init();
  //Serial.println("Sensor A calibration " + String(doorSensorA.calibrationValue));
  doorCustomServoA.init();
  bumpCustomServoA.init();
  lockerA.init();
  delay(2000);
}   

void loop() {
  input_manager();
  lockerA.update();
  //Serial.println("Sensor reading: " + String(doorSensorA.read()));
}

void input_manager() {
  while (Serial.available()) {
    Serial.println(charCounter);
    char c = Serial.read();
    if (charCounter == 0) {
      if (c == lockerA._id)
        locker = 1;
    }
    else if (c != '\n') {
      if (locker == 1)
      {
        Serial.println("sent input to locker1 " + String(c));
        lockerA.send_input(c);
      }
    }
  charCounter++;
  if (c == '\n')
    charCounter = 0;
  }
}