#include <SoftwareSerial.h>

SoftwareSerial mySerial(10, 11); // RX, TX
void setup()
{

  Serial.begin(19200);
  while (!Serial)
  {
  }

  Serial.println("Goodnight moon!");

  mySerial.begin(38400);

}

void loop()
{
  if (mySerial.available())
  {
    Serial.write(mySerial.read());
  }
  if (Serial.available())
  {
    mySerial.write(Serial.read());
  }
}