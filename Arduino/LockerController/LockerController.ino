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
MagneticSensor doorSensorA(A0, 80, 20);
MagneticSensor doorSensorB(A1, 65, 20);
CustomServo lockCustomServoA(lockServoA, 11, 200, 1, 0);
CustomServo bumpCustomServoA(bumpServoA, 10, 200, 1, 0);
CustomServo lockCustomServoB(lockServoB, 5, 200, 1, 180);
CustomServo bumpCustomServoB(bumpServoB, 6, 200, 1, 180);
Locker lockerA(lockCustomServoA, bumpCustomServoA, doorSensorA, "LT02_1", true);
Locker lockerB(lockCustomServoB, bumpCustomServoB, doorSensorB, "LT02_2", false);
int charCounter = 0;
int locker;
bool espReady = false;

void i2cReceiveCallback(int size) {
  while (Wire.available() > 0) {
    char data = Wire.read();
    Serial.println("data received from esp");
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
  Serial.println(" ");
  Serial.println("=======================================================================");
  Serial.println("STARTING UNO");
  delay(20000);
  Wire.begin(8);
  Wire.onReceive(i2cReceiveCallback);
  Wire.onRequest(i2cRequestCallback);
  delay(1000);
  /*
  Serial.println("WAITING FOR ESP");
  while (!espReady) {
    Serial.print(".");
    delay(300);
  }
  */


  bumpCustomServoA.init();
  bumpCustomServoB.init();
  delay(3000);
  lockCustomServoB.init();
  lockCustomServoA.init();
  delay(3000);
  lockerA.init();
  lockerB.init();

  Serial.print("\n");
  Serial.println("Esp ready");
  Serial.println("UNO STARTED");
}   

void loop() {
  lockerA.update();
  lockerB.update();
}
