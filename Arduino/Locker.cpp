#include "Arduino.h"
#include "CustomTimer.h"

Locker(Servo servo) {
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
