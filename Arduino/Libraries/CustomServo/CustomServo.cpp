#include "Arduino.h"
#include "Servo.h"
#include "MagneticSensor.h"
#include "CustomTimer.h"
#include "CustomServo.h"

CustomServo::CustomServo(Servo& servo, int pin, int sweepSpeed, int sweepResolution) {
  _servo = servo;
  _pin = pin;
  _sweepSpeed = sweepSpeed;
  _sweepResolution = sweepResolution;
}

CustomServo::CustomServo() {
}
void CustomServo::init() {
  _servo.attach(_pin);
  _servo.write(0);
  _currentAngle = 0;
}

void CustomServo::sweep(int angle) {
  _sweeping = true;
  int deltaAngle = angle - _currentAngle;
  _sweepTimer = CustomTimer(_sweepSpeed / (abs(deltaAngle)) * _sweepResolution);
  _sweepDir = deltaAngle > 0? 1 : -1;
  _targetAngle = angle;
}

void CustomServo::update() {
  if (_sweeping && _sweepTimer.checkAndUpdate()) {
    _currentAngle += _sweepDir * _sweepResolution;
    if (_sweepDir == 1 && _currentAngle >= _targetAngle || _sweepDir == -1 && _currentAngle <= _targetAngle) {
      _currentAngle = _targetAngle;
      _sweeping = false;
    }
    _servo.write(_currentAngle);
  }
}
