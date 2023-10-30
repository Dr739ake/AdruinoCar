#include <SoftwareSerial.h>
SoftwareSerial MyBlue(2, 3);  // RX | TX

char flag = 0;
int LED_UP = 8;
int LED_DOWN = 9;
int LED_LEFT = 10;
int LED_RIGHT = 11;

String cmd = "none";

void setup() {
  Serial.begin(9600);
  MyBlue.begin(9600);
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
  } else if (cmd == "-up") {
    Serial.println("-up");
    digitalWrite(LED_UP, LOW);
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