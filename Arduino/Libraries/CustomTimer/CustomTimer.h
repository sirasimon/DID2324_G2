#ifndef CustomTimer_h
#define CustomTimer_h
#include "Arduino.h"
class CustomTimer {
  public:
    CustomTimer(float duration);
    bool checkAndUpdate();
  private:
    float duration;
    float timestamp; 
}
#endif
  
