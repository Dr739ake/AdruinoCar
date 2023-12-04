#include <SoftwareSerial.h>

SoftwareSerial MyBlue(2, 3);  // RX | TX

char flag = 0;

#define TIRE_1 7
#define TIRE_2 8

String cmd = "none";

void setup() {
  Serial.begin(9600);
  Serial.println("Ready to connect");

  pinMode(TIRE_1, OUTPUT);
  pinMode(TIRE_2, OUTPUT);
  Serial.println("Ready to connect");

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
    digitalWrite(TIRE_1, HIGH);
  } else if (cmd == "-up") {
    Serial.println("-up");
    digitalWrite(TIRE_1, LOW);
  } else if(cmd == "+down") {
    Serial.println("+down");
    digitalWrite(TIRE_2, HIGH);
  } else if(cmd == "-down") {
    Serial.println("-down");
    digitalWrite(TIRE_2, LOW);
  }

  cmd = "";
}