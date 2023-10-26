#include <SoftwareSerial.h>
SoftwareSerial MyBlue(2, 3);  // RX | TX

char flag = 0;
int LED = 8;
String cmd;

void setup() {
  Serial.begin(9600);
  MyBlue.begin(9600);
  pinMode(LED, OUTPUT);
  Serial.println("Ready to connect\nDefault password is 1234 or 000");
}

void loop() {
  if (MyBlue.available()) {
    flag = MyBlue.read();
    if (flag != ';') {
      cmd += flag;
      return;
    }
    Serial.println(cmd);
  }

  if (cmd == "forward") {
    digitalWrite(LED, HIGH);
  } else {
    digitalWrite(LED, LOW);
  }

}