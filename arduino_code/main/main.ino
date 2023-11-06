#include <SoftwareSerial.h>
#include <Stepper.h>

SoftwareSerial MyBlue(2, 3);  // RX | TX

#define STEPS 10000
#define MOTOR_RPM 2

Stepper stepper(STEPS, 8,9,10,11);

char flag = 0;

int LED_UP = 4;
int LED_DOWN = 5;
int LED_LEFT = 6;
int LED_RIGHT = 7;

String cmd = "none";

void setup() {
  Serial.begin(9600);
  MyBlue.begin(9600);
  Serial.println("Ready to connect");

  pinMode(LED_UP, OUTPUT);
  pinMode(LED_DOWN, OUTPUT);
  pinMode(LED_LEFT, OUTPUT);
  pinMode(LED_RIGHT, OUTPUT);
  Serial.println("Ready to connect");

  digitalWrite(LED_UP, HIGH);
  digitalWrite(LED_DOWN, HIGH);
  digitalWrite(LED_LEFT, HIGH);
  digitalWrite(LED_RIGHT, HIGH);
  delay(1000);
  digitalWrite(LED_UP, LOW);
  digitalWrite(LED_DOWN, LOW);
  digitalWrite(LED_LEFT, LOW);
  digitalWrite(LED_RIGHT, LOW);

}

void loop() {
  if (MyBlue.available()) {
    flag = MyBlue.read();
    Serial.print(flag);
    if (flag != ';') {
      cmd += flag;
      return;
    }
  }
  Serial.println(cmd);
  if (cmd == "+up") {
    Serial.println("+up");
    digitalWrite(LED_UP, HIGH);
    stepper.setSpeed(MOTOR_RPM);
    stepper.step(STEPS);
  } else if (cmd == "-up") {
    Serial.println("run in -up");
    digitalWrite(LED_UP, LOW);
    stepper.setSpeed(MOTOR_RPM);
    stepper.step(-STEPS);
  } else if (cmd == "+down") {
    Serial.println("+down");
    digitalWrite(LED_DOWN, HIGH);
  } else if (cmd == "-down") {
    Serial.println("-down");
    digitalWrite(LED_DOWN, LOW);
  } else if (cmd == "+left") {
    Serial.println("+left");
    digitalWrite(LED_LEFT, HIGH);
  } else if (cmd == "-left") {
    Serial.println("-left");
    digitalWrite(LED_LEFT, LOW);
  } else if (cmd == "+right") {
    Serial.println("+right");
    digitalWrite(LED_RIGHT, HIGH);
  } else if (cmd == "-right") {
    Serial.println("-right");
    digitalWrite(LED_RIGHT, LOW);
  }
  cmd = "";
}