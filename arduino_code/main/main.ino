#include <SoftwareSerial.h> 
SoftwareSerial MyBlue(2, 3); // RX | TX 

char flag = 0; 
int LED = 8; 

void setup() 
{   
 Serial.begin(9600); 
 MyBlue.begin(9600); 
 pinMode(LED, OUTPUT); 
 Serial.println("Ready to connect\nDefault password is 1234 or 000"); 
} 

void loop() 
{ 
 if (MyBlue.available()) 
  {
    flag = MyBlue.read();
    Serial.println("Flag is: ");
    Serial.println(flag);
  }
 if (flag == '1') 
 { 
   digitalWrite(LED, HIGH); 
   //Serial.println("LED On"); 
 } 
 else if (flag == '0') 
 { 
   digitalWrite(LED, LOW); 
   //Serial.println("LED Off"); 
 } 
}