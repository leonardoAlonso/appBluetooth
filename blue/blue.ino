#include <SoftwareSerial.h>
SoftwareSerial blue(10,11);

#define led1 3
#define led2 5
String comando;

void motores(int m1, int m2){
    //m1 y m2 son las velocidades del primer
   // y segundo motor, entre -255 y 255
 
   //Declarar los pines para el motor 1
   pinMode(7, OUTPUT);
   pinMode(8, OUTPUT);
   pinMode(5, OUTPUT);
    
   //Declarar los pines para el motor 2
   pinMode(2, OUTPUT);
   pinMode(4, OUTPUT);
   pinMode(6, OUTPUT);
    
   //Invertir el giro del motor 1 si es necesario
   if(m1 < 0){
      digitalWrite(8, HIGH);
      digitalWrite(7, LOW);
      m1 = -m1;
   }
   else{
      digitalWrite(8, LOW);
      digitalWrite(7,HIGH);
   }
    
 
   //Invertir el giro del motor 2 si es necesario
   if(m2 < 0){
      digitalWrite(4, HIGH);
      digitalWrite(2, LOW);
      m2 = -m2;
   }
   else{
      digitalWrite(4, LOW);
      digitalWrite(2,HIGH);
   }
 
   //Generar pulso PWM
   analogWrite(5, m1);
   analogWrite(6, m2);
}

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
    if(comando == "Arriba"){
      motores(250,250);
    }
    else if(comando == "Izquierda"){
      motores(-250, 250);
    }
    else if(comando == "Off"){
      motores(0,0);
    }
    else if(comando == "Abajo") {
      motores(-250,-250);
    }
    else if(comando == "Derecha"){
      motores(250,-250);
    }
  }
}
