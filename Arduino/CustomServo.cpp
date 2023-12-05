#include "Arduino.h"
#include "CustomServo.h"
#include <CustomTimer.h>

CustomServo::CustomServo(Servo servo, int pin, int sweepSpeed, int sweepResolution)) {
  this.servo = servo;
  this.pin = pin;
  this.sweepSpeed = sweepSpeed;
  this.sweepResolution = sweepResolution;
}

CustomServo::init() {
  servo.attach(pin);
  servo.write(0);
  currentAngle = 0;
}

CustomServo::sweep(int angle) {
  int deltaAngle = currentAngle - angle;
  int deltaAngle = Abs(currentAngle - angle);
  sweepTimer = new CustomTimer((Abs(deltaAngle) / sweepSpeed) * sweepResolution);
  sweeping = true;
  sweepDir = deltaAngle > 0? 1 : -1;
  targetAngle = angle;
}

CustomServo::update() {
  if (sweeping && sweepTimer.checkAndUpdate) {
    currentAngle += sweepDir * sweepResolution;
    if (sweepDir > 0 && currentAngle >= targetAngle || sweepDir < 0 && currentAngle <= targetAngle) {
      currentAngle = targetAngle;
      sweeping = false;
    }
  }
}
