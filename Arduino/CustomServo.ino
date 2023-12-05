#ifndef CustomServo_h
#define CustomServo_h
#include "Arduino.h"
#include <Servo.h>
class CustomServo {
  public:

  private:
    Servo servo;
    int pin;
    int currentAngle;
    int sweepSpeed;
    bool sweeping;
    CustomTimer sweepTimer;
    int deltaAngle;
    int sweepDir;
    int targetAngle;

}
#endif