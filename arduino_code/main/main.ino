#include <SoftwareSerial.h>

SoftwareSerial MyBlue(2, 3);  // RX | TX

#define DRIVE_SPEED 200
#define TURN_SPEED 200

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

bool movin_forward    = false;
bool movin_backwards  = false;
bool turnin_left      = false;
bool turnin_right     = false;

void StopDriving();
void DriveForward();
void DriveBackwards();
void TurnRight();
void TurnLeft();

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

void loop() {
  if (Serial.available()) {
    flag = Serial.read();
    if (flag != ';') {
      cmd += flag;
      return;
    }
  }

  if (cmd == "W") {
    DriveForward();
  } else if (cmd == "w") {
    StopDriving();
  } else if(cmd == "S") {
    DriveBackwards();
  } else if (cmd == "s") {
    StopDriving();
  } else if(cmd == "A") {
    TurnLeft();
  } else if (cmd == "a") {
    StopDriving();
  } else if(cmd == "D") {
    TurnRight();
  } else if (cmd == "d") {
    StopDriving();
  }
  cmd = "";
}


void DriveForward() {
  if(!movin_forward) {
    digitalWrite(in1, LOW);
    digitalWrite(in2, HIGH);
    analogWrite(GSM1, DRIVE_SPEED);

    digitalWrite(in3, LOW);
    digitalWrite(in4, HIGH);
    analogWrite(GSM2, DRIVE_SPEED);
    movin_forward = true;
  }
}

void StopDriving() {
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);
  analogWrite(GSM1, DRIVE_SPEED);

  digitalWrite(in3, LOW);
  digitalWrite(in4, LOW);
  analogWrite(GSM2, DRIVE_SPEED);

  movin_forward    = false;
  movin_backwards  = false;
  turnin_left      = false;
  turnin_right     = false;
}

void DriveBackwards() {
  if(!movin_backwards) {
    digitalWrite(in1, HIGH);
    digitalWrite(in2, LOW);
    analogWrite(GSM1, DRIVE_SPEED);

    digitalWrite(in3, HIGH);
    digitalWrite(in4, LOW);
    analogWrite(GSM2, DRIVE_SPEED);

    movin_backwards = true;
  }
}

void TurnRight() {
  if(!turnin_right) {
    digitalWrite(in1, HIGH);
    digitalWrite(in2, LOW);
    analogWrite(GSM1, TURN_SPEED);

    digitalWrite(in3, LOW);
    digitalWrite(in4, HIGH);
    analogWrite(GSM2, TURN_SPEED);
    turnin_right = true;
  }
}

void TurnLeft() {
  if(!turnin_left) {
    digitalWrite(in1, LOW);
    digitalWrite(in2, HIGH);
    analogWrite(GSM1, TURN_SPEED);

    digitalWrite(in3, HIGH);
    digitalWrite(in4, LOW);
    analogWrite(GSM2, TURN_SPEED);
  turnin_left = true;
  }
}
