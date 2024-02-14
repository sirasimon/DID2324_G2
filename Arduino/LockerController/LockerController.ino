//------------------------------------------------------------------------------------------------IMPORTS
/*
COMMANDS FOR LOCKERS
 - "LOCKERID" + com
 - Coms:
  -  0: open  

  Example: A0 when locker closed = open locker with id A
*/
#include <Servo.h>
#include <Wire.h>
#include <MagneticSensor.h>
#include <CustomTimer.h> 
#include <CustomServo.h>
#include <Locker.h>

Servo doorServoA;
Servo bumpServoA;
MagneticSensor doorSensorA(A0, 30);
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
  Wire.begin(8);
  Wire.onReceive(receiveEvent);
  Wire.onRequest(requestEvent);
  delay(2000);
}   

void loop() {
  lockerA.update();
  //Serial.println("Sensor reading: " + String(doorSensorA.read()));
}
void receiveEvent(int size) {
  while (Wire.available() > 0) {
    char c = Wire.read();
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
void requestEvent() {
  if (lockerA.isClosed()) {
    Wire.write('c');
  }
  else {
    Wire.write('o');
  }
}
