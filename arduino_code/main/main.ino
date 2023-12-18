#include <SoftwareSerial.h>

SoftwareSerial MyBlue(2, 3);  // RX | TX

#define DRIVE_SPEED 200
#define TURN_SPEED 200

// Pins für Gleichstrommotor 1

#define GSM1 10
#define in1 9
#define in2 8

// Pins für Gleichstrommotor 2

#define GSM2 5
#define in3 7
#define in4 6

// Variable und Funktionsdeklaration
char cmd = '\0';
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
  // Konfiguration des Serielen Monitors für Debugging
  Serial.begin(9600);
  Serial.println("Ready to connect");

  // Konfiguration der Digital Ports für die Gleichstrommotoren
  pinMode(GSM1, OUTPUT);
  pinMode(GSM2, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);

  Serial.println("Ready to connect");
}

void loop() {
  // wenn Text per Bluetooth gesendet wird, lese diese und füge diese zusammen
  if (Serial.available()) {
    cmd = Serial.read();
  }

  // Befehl verarbeiten
  if (cmd == 'W') {
    DriveForward();
  } else if (cmd == 'w') {
    StopDriving();
  } else if(cmd == 'S') {
    DriveBackwards();
  } else if (cmd == 's') {
    StopDriving();
  } else if(cmd == 'A') {
    TurnLeft();
  } else if (cmd == 'a') {
    StopDriving();
  } else if(cmd == 'D') {
    TurnRight();
  } else if (cmd == 'd') {
    StopDriving();
  }
  // Befehl zurücksetzten
  cmd = '\0';
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
