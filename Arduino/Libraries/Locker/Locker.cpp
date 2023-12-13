#include "Arduino.h"
#include "Servo.h"
#include "CustomServo.h"
#include "MagneticSensor.h"
#include "CustomTimer.h"
#include "Locker.h"

Locker::Locker(CustomServo& servo, MagneticSensor& sensor, char id): _servo(servo), _sensor(sensor) {
  _id = id;
} 
#pragma region Open State
void Locker::_open_enter() {
  Serial.println("Locker " + String(_id) + " state has changed to OPEN");
  _servo.sweep(180);
}
void Locker::_open_update() {
  if (_input_sent && _com == '1') {
    _change_state(closed);
  }
}
void Locker::_open_exit() {

}
#pragma endregion 
#pragma region Closed State

void Locker::_closed_enter() {
  Serial.println("Locker " + String(_id) + " state has changed to CLOSED");
  _servo.sweep(0);
}
void Locker::_closed_update() {
  if (_input_sent && _com == '1') {
    _change_state(open);
  }
}
void Locker::_closed_exit() {

}
#pragma endregion
#pragma region Other

void Locker::init() {
  _change_state(open);
}
void Locker::send_input(char com) {
   _com = com;
   _input_sent = true;
}
void Locker::update() {
  _servo.update();
  switch (_currentState) {
    case open:
      _open_update();
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
    case open: 
      _open_exit();
      break;
    case closed:
      _closed_exit();
      break;
    default:
      Serial.println("Locker " + String(_id) + " has enterd an INVALID state!");
  }
  _currentState = nextState;
  switch (_currentState){
    case open: 
      _open_enter(); 
      break;
    case closed:
      _closed_enter();
      break;
  }
}
#pragma endregion