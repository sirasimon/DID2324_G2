#ifndef CustomServo_h
#define CustomServo_h
#include "Arduino.h"
#include "Servo.h"
#include "MagneticSensor.h"
#include "CustomTimer.h"
class CustomServo {
  public:
    CustomServo(Servo& servo, int pin, int sweepSpeed, int sweepResolution);
    CustomServo();
    void init();
    void sweep(int angle);
    void update();
    int _currentAngle;
  private:
    Servo& _servo;
    CustomTimer _sweepTimer;
    int _pin;
    int _sweepSpeed;
    int _sweepResolution;
    int _sweepDir;
    int _targetAngle;
    bool _sweeping;
};

#endif