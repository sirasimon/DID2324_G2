#ifndef MagneticSensor_h
#define MagneticSensor_h
#include "Arduino.h"

class MagneticSensor {
  public:
    static const int calibrationReadings = 100;
    static const int readings = 10;
    long calibrationValue;
    MagneticSensor(int pin, int sensitivity);
    MagneticSensor();
    void init();
    long read();
    bool check();
    
  private:
    int _pin;
    int _sensitivity;
    long _read(int readings);
};

#endif
