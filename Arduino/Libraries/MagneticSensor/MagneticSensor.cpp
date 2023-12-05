#include "Arduino.h"
#include "MagneticSensor.h"

MagneticSensor::MagneticSensor(int pin, int sensitivity) {
  _pin = pin;
  _sensitivity = sensitivity;
}

void MagneticSensor::init() {
  calibrationValue = _read(calibrationReadings);
}

bool MagneticSensor::check() {
  long deltaSensor = abs(read() - calibrationValue);
  if (deltaSensor > _sensitivity) {
    return true;
  }
  return false;
}

long MagneticSensor::read() {
  return _read(readings);
}

long MagneticSensor::_read(int readings) {
  long measure = 0;
  for (int i = 0; i < readings; i++) {
    measure += analogRead(_pin);
  }
  measure /= readings;
  return measure;
}