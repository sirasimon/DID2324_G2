#ifndef CustomTimer_h
#define CustomTimer_h
#include "Arduino.h"
class CustomTimer {
  public:
    CustomTimer(unsigned long duration);
    bool checkAndUpdate();
    unsigned long _duration;
    unsigned long _timestamp; 
  private:
};
#endif
  
