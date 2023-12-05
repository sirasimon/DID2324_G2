#include "Arduino.h"
#include "CustomTimer.h"

CustomTimer::CustomTimer(unsigned long duration) {
  _duration = duration;
  _timestamp = millis() + _duration;
}

CustomTimer::CustomTimer() {
  CustomTimer(1000);
}

bool CustomTimer::checkAndUpdate() {
  if (millis() >= _timestamp) {
    _timestamp = millis() + _duration;
    return true;  
  }
  return false;
}
