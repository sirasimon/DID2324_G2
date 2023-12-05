#include "Arduino.h"
#include "CustomTimer.h"

CustomTimer::CustomTimer(float duration) {
  this.duration = duration;
  timestamp = millis();
}

CustomTimer::checkAndUpdate() {
  if (millis >= timestamp) {
    timestamp = millis();
    return true;
  }
  return false;
}
