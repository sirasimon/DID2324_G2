#ifndef CustomTimer_h
#define CustomTimer_h
#include "Arduino.h"

class CustomTimer {
  public:
    CustomTimer(unsigned long duration);
    CustomTimer();
    bool checkAndUpdate();

  private:
    unsigned long _duration;
    unsigned long _timestamp; 
};

#endif
  
