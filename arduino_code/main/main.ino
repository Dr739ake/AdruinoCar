#include <SoftwareSerial.h>

SoftwareSerial MyBlue(2, 3);  // RX | TX

#define DRIVE_SPEED 200
#define TURN_SPEED 100

char flag = 0;

// Gleichstrommotor 1

#define GSM1 10
#define in1 9
#define in2 8

// Gleichstrommotor 2

#define GSM2 5
#define in3 7
#define in4 6

String cmd = "";

void setup() {
  Serial.begin(9600);
  Serial.println("Ready to connect");

  pinMode(GSM1, OUTPUT);    
  pinMode(GSM2, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);

  Serial.println("Ready to connect");
}

void DriveForward() {
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);
  analogWrite(GSM1, DRIVE_SPEED);

  digitalWrite(in3, LOW);
  digitalWrite(in4, HIGH);
  analogWrite(GSM2, DRIVE_SPEED);
}

void StopDriving() {
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);
  analogWrite(GSM1, DRIVE_SPEED);

  digitalWrite(in3, LOW);
  digitalWrite(in4, LOW);
  analogWrite(GSM2, DRIVE_SPEED);
}

void DriveBackwards() {
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  analogWrite(GSM1, DRIVE_SPEED);

  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
  analogWrite(GSM2, DRIVE_SPEED);
}

void TurnRight() {
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);
  analogWrite(GSM1, TURN_SPEED);

  digitalWrite(in3, LOW);
  digitalWrite(in4, HIGH);
  analogWrite(GSM2, TURN_SPEED);
}

void TurnLeft() {
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);
  analogWrite(GSM1, TURN_SPEED);

  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
  analogWrite(GSM2, TURN_SPEED);
}

void loop() {
  if (MyBlue.available()) {
    flag = MyBlue.read();
    Serial.print(flag);
    if (flag != ';') {
      Serial.print(".");
      cmd += flag;
      return;
    }
  }

// Steuerung der Motoren

  // DriveForward();
  // delay(1000);
  // StopDriving();
  // DriveBackwards();
  // delay(1000);
  // StopDriving();
  // TurnLeft();
  // delay(1000);
  // StopDriving();
  // TurnRight();
  // delay(1000);
  // StopDriving();


  if (cmd == "+up") {
    Serial.println(cmd);
    Serial.println("+up");
    DriveForward();
  } else if (cmd == "-up") {
    Serial.println(cmd);
    Serial.println("-up");
    StopDriving();
  } else if(cmd == "+down") {
    Serial.println(cmd);
    Serial.println("+down");
    DriveBackwards();
  } else if(cmd == "-down") {
    Serial.println(cmd);
    Serial.println("-down");
    StopDriving();
  }


  cmd = "";
}