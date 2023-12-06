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

void setup() {
  Serial.begin(9600);
  Serial.println("START");
  sensor1.init();
  sensor2.init();
  customServo1.init();
  customServo2.init();
}

void loop() {
  locker1.update();
  locker2.update();
}
/*
//------------------------------------------------------------------------------------------------GLOBAL VALUES
//Constants

const int pinHall = A0;
const int magneticCalibrationReadings = 100;
const int magneticReadings = 10;
const long magneticSensitivity = 50;

const long onOpenCloseDelay = 2000;
const char openComChar = '1';
//Variables
Servo myservo;
auto timer = timer_create_default();
bool isFullyOpen;
int pos = 0;
long magneticCalibrationValue = 0;
LockerState lockerState = Invalid;

//------------------------------------------------------------------------------------------------SETUP
void setup() {
  //Com initilization
  Serial.begin(9600);
  //Pins initialization
  pinMode(pinHall, INPUT);
  //Servo initialization
  myservo.attach(9);
  myservo.write(180);
  //Magnetic sensor calibration - Value reading
  long measure = 0;
  for (int i = 0; i < magneticCalibrationReadings; i++) {
    measure += analogRead(pinHall);
  }
  measure /= magneticCalibrationReadings;
  magneticCalibrationValue = measure;
  Serial.println("Magnetic sensor calibration value is: " + String(magneticCalibrationValue));
  //State initialization
  changeState(Open);
  isFullyOpen = true;
}

//------------------------------------------------------------------------------------------------LOOP
void loop() {
  timer.tick();
  if (lockerState == Open)
    open_Loop();
  else if (lockerState == Closed)
    closed_Loop();
  else if (lockerState == Invalid)
    Serial.println("LOCKER HAS ENTERED AN INVALID STATE");
}

//------------------------------------------------------------------------------------------------OPEN STATE
void open_Enter() {
  Serial.println("State has changed to Open");
  timer.in(onOpenCloseDelay, []() -> void {
    isFullyOpen = true;
  });
  myservo.write(180);
}
void open_Loop() {
  if (isFullyOpen) {
    long deltaMagnetic = abs(readMagnetic() - magneticCalibrationValue);
    if (deltaMagnetic > magneticSensitivity) {
      changeState(Closed);
    }
  }
}
void open_Exit() {
  isFullyOpen = false;
}

//------------------------------------------------------------------------------------------------CLOSED STATE
void closed_Enter() {
  Serial.println("State has changed to Closed");
  myservo.write(0);
}
void closed_Loop() {
  if (Serial.available() > 0) {
    byte byteRead = Serial.read();
    if (char(byteRead) == openComChar) {
      changeState(Open);
    }
  }
}
void closed_Exit() {
}
//------------------------------------------------------------------------------------------------STATE CHANGE
void changeState(LockerState nextState) {
  if (lockerState == Open) {
    open_Exit();
  } else if (lockerState == Closed) {
    closed_Exit();
  }
  lockerState = nextState;
  if (lockerState == Open) {
    open_Enter();
  } else if (lockerState == Closed) {
    closed_Exit();
  }
}
//------------------------------------------------------------------------------------------------UTILITY
long readMagnetic() {
  long measure = 0;
  for (int i = 0; i < magneticReadings; i++) {
    measure += analogRead(pinHall);
  }
  measure /= magneticReadings;
  return measure;
}
*/