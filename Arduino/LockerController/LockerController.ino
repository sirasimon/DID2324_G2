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

Servo lockServoA;
Servo bumpServoA;
Servo lockServoB;
Servo bumpServoB;
MagneticSensor doorSensorA(A0, 35);
MagneticSensor doorSensorB(A1, 100);
CustomServo lockCustomServoA(lockServoA, 11, 200, 1);
CustomServo bumpCustomServoA(bumpServoA, 10, 200, 1);
CustomServo lockCustomServoB(lockServoB, 6, 200, 1);
CustomServo bumpCustomServoB(bumpServoB, 9, 200, 1);
Locker lockerA(lockCustomServoA, bumpCustomServoA, doorSensorA, "LT02_1");
Locker lockerB(lockCustomServoB, bumpCustomServoB, doorSensorB, "LT02_2");
int charCounter = 0;
int locker;
bool espReady = false;

void i2cReceiveCallback(int size) {
  while (Wire.available() > 0) {
    char data = Wire.read();
    if (data == 'a') {
      lockerA.send_input('o');
    }
    else if (data == 'b') {
      lockerB.send_input('o');
    }
    else if (data == 'r') {
      espReady = true;
    }
  }
}
void i2cRequestCallback() {
  if (lockerA.isClosed()) {
    Wire.write('a');
  }
  else {
    Wire.write('_');
  }
  if (lockerB.isClosed()) {
    Wire.write('b');
  }
  else {
    Wire.write('_');
  }
}
void setup() {
  Serial.begin(115200);
  delay(1000);
  Serial.println(" ");
  Serial.println("=======================================================================");
  Serial.println("STARTING UNO");
  lockCustomServoA.init();
  bumpCustomServoA.init();
  lockCustomServoB.init();
  bumpCustomServoB.init();
  lockerA.init();
  lockerB.init();
  Wire.begin(8);
  Wire.onReceive(i2cReceiveCallback);
  Wire.onRequest(i2cRequestCallback);
  delay(1000);
  Serial.println("WAITING FOR ESP");
  while (!espReady) {
    Serial.print(".");
    delay(300);
  }
  Serial.print("\n");
  Serial.println("Esp ready");
  Serial.println("UNO STARTED");
}   

void loop() {
  lockerA.update();
  lockerB.update();
  //simReceiveEvent();
}

/*
void simReceiveEvent() {
  char data = Serial.read();
  if (data == 'a') {
    lockerA.send_input('c');
  }
  else if (data == 'b') {
    lockerB.send_input('c');
  }
}
*/

/*
void simReceiveEvent() {
  while (Serial.available() > 0) {
    char c = Serial.read();
    if (charCounter == 0) {
      if (c == lockerA._id)
        locker = 1;
      else if (c == lockerB._id)
        locker = 2;
    }
    else if (c != '\n') {
      if (locker == 1)
      {
        Serial.println("Received input for locker A " + String(c));
        lockerA.send_input(c);
      }
      else if (locker == 2) {
        Serial.println("Received input for locker B " + String(c));
      }
    }
  charCounter++;
  if (c == '\n')
    charCounter = 0;
  }
}
void i2cReceiveCallback(int size) {
  while (Wire.available() > 0) {
    char c = Wire.read();
    if (charCounter == 0) {
      if (c == lockerA._id)
        locker = 1;
      else if (c == lockerB._id)
        locker = 2;
    }
    else if (c != '\n') {
      if (locker == 1)
      {
        Serial.println("Received input for locker A " + String(c));
        lockerA.send_input(c);
      }
      else if (locker == 2) {
        Serial.println("Received input for locker B " + String(c));
      }
    }
  charCounter++;
  if (c == '\n')
    charCounter = 0;
  }
}
*/
