#include "Arduino.h"
#include "Servo.h"
#include "CustomServo.h"
#include "MagneticSensor.h"
#include "CustomTimer.h"

Locker::Locker(CustomServo servo, MagneticSensor sensor, char id) {
  servo.init();
  sensor.init();
  _id = id;
}

void input(char com) {
  _com = com;
}

void open_enter() {
  servo.sweep(180);
}

void open_update() {
  if (MagneticSensor.check()) {
    changeState(closed);
  }
}

void open_exit() {

}

void closed_enter() {
  myservo.write(0);
}

void close_loop
enum states {
  invalid,
  open,
  closed
}