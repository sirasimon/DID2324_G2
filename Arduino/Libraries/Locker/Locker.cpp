#include "Arduino.h"
#include "Servo.h"
#include "CustomServo.h"
#include "MagneticSensor.h"
#include "CustomTimer.h"
#include "Locker.h"

Locker::Locker(CustomServo& servo, CustomServo& bumpServo, MagneticSensor& sensor, String id): _servo(servo), _bumpServo(bumpServo), _sensor(sensor) {
  _id = id;
}

#pragma region OpenUnlock State
void Locker::_openUnlock_enter() {
  Serial.println("Locker " + String(_id) + " STATE has changed to OPEN UNLOCK");
  _servo.sweep(0);
}
void Locker::_openUnlock_update() {
  if (_servo._currentAngle == 0) {
    _change_state(openBump);
  }
}
void Locker::_openUnlock_exit() { }
#pragma endregion 

#pragma region OpenBump State
void Locker::_openBump_enter() { 
  _bumpServo.sweep(180);
}
void Locker::_openBump_update() {
  if (_bumpServo._currentAngle == 180) {
    _change_state(open);
  }
}
void Locker::_openBump_exit() { }
#pragma endregion

#pragma region Open State
void Locker::_open_enter() {
  Serial.println("Locker " + String(_id) + " STATE has changed to OPEN");
  _bumpServo.sweep(0);
  _closeEnabled = false;
}
void Locker::_open_update() {
  if (!debugMode) {
    if (!_closeEnabled && (!_sensor.check())) {
      _closeEnabled = true;
    }
    else if (_closeEnabled && _sensor.check()) {
      _change_state(closed);
    }
  }
  else {
    if (_input_sent && _com == 'c') {
      _change_state(closed);
    }
  }
}
void Locker::_open_exit() { }
#pragma endregion 

#pragma region Closed State
void Locker::_closed_enter() {
  Serial.println("Locker " + String(_id) + " state has changed to CLOSED");
  _servo.sweep(90);
}
void Locker::_closed_update() {
  if (_input_sent && _com == 'o') {
    _change_state(openUnlock);
  }
}
void Locker::_closed_exit() { }
#pragma endregion

#pragma region Starting State
void Locker::_starting_enter() {
  Serial.println("Locker " + String(_id) + " STATE has changed to STARTING");
  _servo.sweep(90);
}
void Locker::_starting_update() {
  if (_servo._currentAngle == 90) {
    _change_state(closed);
  }
}
void Locker::_starting_exit() {
  delay(5000);
  _sensor.calibrationValue = _sensor.readTimeAverage(5000);
  Serial.println("Locker " + String(_id) + " magnetic calibration value: " + String(_sensor.calibrationValue));
}
#pragma endregion

#pragma region Other
void Locker::init() {
  _change_state(starting);
  _closeEnabled = true;
}
void Locker::send_input(char com) {
   _com = com;
   _input_sent = true;
}
void Locker::update() {
  _servo.update();
  _bumpServo.update();
  switch (_currentState) {
    case openUnlock:
      _openUnlock_update();
      break;
    case openBump:
      _openBump_update();
      break;
    case open:
      _open_update();
      break;
    case starting:
      _starting_update();
      break;
    case closed:
      _closed_update();  
      break; 
  }
  _input_sent = false;
}
void Locker::_change_state(state nextState) {
  switch (_currentState)
  {
    case openUnlock:
      _openUnlock_exit();
      break;
    case openBump:
      _openBump_exit();
      break;
    case open: 
      _open_exit(); 
      break;
    case starting:
      _starting_exit();
      break;
    case closed:
      _closed_exit();
      break;
    default:
      Serial.println("Locker " + String(_id) + " has enterd an INVALID state!");
  }
  _currentState = nextState;
  switch (_currentState) {
    case openUnlock:
      _openUnlock_enter();
      break;
    case openBump:
      _openBump_enter();
      break;
    case open: 
      _open_enter(); 
      break;
    case starting:
      _starting_enter();
      break;
    case closed:
      _closed_enter();
      break;
  }
}
bool Locker::isClosed() {
  return _currentState == closed;
}
#pragma endregion