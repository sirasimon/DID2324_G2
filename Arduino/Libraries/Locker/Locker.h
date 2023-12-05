#ifndef Locker_h
#define Locker_h
#include "Arduino.h"
class Locker {
  public:
    Locker;
    bool checkAndUpdate();
    enum state;
  private:
    float duration;
    float timestamp; 
}
#endif
  
