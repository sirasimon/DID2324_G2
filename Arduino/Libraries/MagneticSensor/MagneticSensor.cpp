#include "Arduino.h"
#include "MagneticSensor.h"

MagneticSensor::MagneticSensor(int pin, int sensitivity) {
  _pin = pin;
  _sensitivity = sensitivity;
}

MagneticSensor::MagneticSensor() {
  _pin = A0;
  _sensitivity = 100;
}

void MagneticSensor::init() {
 //Moved in locker
}

bool MagneticSensor::check() {
  long value = read();
  long deltaSensor = abs(value - calibrationValue);
  if (deltaSensor > _sensitivity) {
    lastCheckValue = value;
    lastCheckDelta = deltaSensor;
    return true;
  }
  return false;
}

long MagneticSensor::read() {
  for (int i = 0; i < 10; i++) {
    _read(1);
  }
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

long MagneticSensor::readTimeAverage(long time) {
  for (int i = 0; i < 1000; i++) {
    _read(1);
  }
  long measure = 0;
  long n = 0;
  long startTime = millis();
  while (millis() - startTime < time) {
    measure += read();
    n++;
  }
  measure /= n;
  return measure;
}