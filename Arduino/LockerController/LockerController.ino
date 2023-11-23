//------------------------------------------------------------------------------------------------IMPORTS
#include <Servo.h>
#include <arduino-timer.h>
#include <LockerStates.h>

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
  }
  else if (lockerState == Closed) {
    closed_Exit();
  }
  lockerState = nextState;
  if (lockerState == Open) {
    open_Enter();
  }
  else if (lockerState == Closed) {
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
