#include <SoftwareSerial.h>
SoftwareSerial blue(10,11);

#define led1 3
#define led2 5
String comando;
int intencidad;
void setup() {
  // put your setup code here, to run once:
  pinMode(led1,OUTPUT);
  pinMode(led2,OUTPUT);

  Serial.begin(9600);
  blue.begin(9600);

}

void loop() {
  // put your main code here, to run repeatedly:
  comando = "";

  if(blue.available()){
    while(blue.available()){
      char caracter = blue.read();

      comando += caracter;
      delay(10);
    }
    if(comando.indexOf("ON") >= 0){
      analogWrite(led1,255);
      analogWrite(led2,255);
    }
  
    if(comando.indexOf("Off") >= 0){
      analogWrite(led1,0);
      analogWrite(led2,0);
    }
    if(comando.toInt()> 0 && comando.toInt() < 255){
      analogWrite(led1,comando.toInt());
      Serial.println(comando.toInt());
    }
  }
}
