#ifndef MagneticSensor_h
#define MagneticSensor_h
#include "Arduino.h"
class MagneticSensor {
  public:
    static const int calibrationReadings = 100;
    static const int readings = 10;
    long calibrationValue;
    MagneticSensor(int pin, int sensitivity);
    init();
    read();
    check();
    
  private:
    int pin;
    int sensitivity;
    read(int readings);
}
#endif
