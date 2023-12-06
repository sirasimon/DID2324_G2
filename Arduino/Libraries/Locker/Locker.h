#ifndef Locker_h
#define Locker_h
#include "Arduino.h"
#include "Servo.h"
#include "CustomServo.h"
#include "MagneticSensor.h"
#include "CustomTimer.h"
class Locker {
  public:
    Locker(CustomServo servo, MagneticSensor sensor, char id);
    void send_input(char com);
    void update();
    void init();
    enum state {
      invalid,
      open,
      closed
    };
  private:
    CustomServo _servo;
    MagneticSensor _sensor;
    state _currentState;
    char _id;
    char _com;
    bool _input_sent;
    void _open_enter();
    void _open_update();
    void _open_exit();
    void _closed_enter();
    void _closed_update();
    void _closed_exit();
    void _change_state(state nextState);
};
#endif
  
