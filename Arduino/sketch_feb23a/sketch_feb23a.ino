
#include <Servo.h>
#include <MagneticSensor.h>

MagneticSensor magneticSensor(A1, 100, 20);

void setup() {
  Serial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  Serial.println(magneticSensor.read());

  delay(100);
}
