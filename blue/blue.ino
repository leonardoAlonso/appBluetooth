#include <SoftwareSerial.h>
SoftwareSerial blue(10,11);

#define led1 3
#define led2 4
#define led3 5 
String comando;
void setup() {
  // put your setup code here, to run once:
  pinMode(led1,OUTPUT);
  pinMode(led2,OUTPUT);
  pinMode(led3,OUTPUT);

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
    if(comando.indexOf("led1") >= 0){
      digitalWrite(led1,!digitalRead(led1));
      //Serial.prontln("Ligar/Desligar Led1")  
    }
  
    if(comando.indexOf("led2") >= 0){
      digitalWrite(led2,!digitalRead(led2));
      //Serial.prontln("Ligar/Desligar Led2")  
    }
  
    if(comando.indexOf("led3") >= 0){
      digitalWrite(led3,!digitalRead(led3));
      //Serial.prontln("Ligar/Desligar Led2")  
    }
  
    if(digitalRead(led1)){
       blue.println("l1on");  
    }else{
       blue.println("l1off");  
    }
  
    if(digitalRead(led2)){
       blue.println("l2on");  
    }else{
       blue.println("l2off");  
    }
  
    if(digitalRead(led3)){
       blue.println("l3on");  
    }else{
       blue.println("l3off");  
    }  
  }
}
