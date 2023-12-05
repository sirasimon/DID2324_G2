#include "Arduino.h"
#include "CustomTimer.h"

MagneticSensor::MagneticSensor(int pin, int sensitivity) {
  this.pin = pin;
  this.sensitivity = sensitivity;
}

MagneticSensor::init() {
  calibrationValue = read(calibrationReadings);
}

MagneticSensor::check() {
  long deltaSensor = abs(read() - calibrationValue);
  if (deltaSensor > sensitivity) {
    return true;
  }
  return false;
}

MagneticSensor::read() {
  return read(readings);
}

MagneticSensor::read(int readings) {
  long measure = 0;
  for (int i = 0; i < readings; i++) {
    measure += analogRead(pin);
  }
  measure /= readings;
  return measure;
}