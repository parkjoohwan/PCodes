#include <SoftwareSerial.h> //시리얼 통신 라이브러리 호출

int Red_LED=3;
int Green_LED=5;
int Blue_LED=6;
int blueTx=10;   //Tx (보내는핀 설정)
int blueRx=9;   //Rx (받는핀 설정)
int red=0;
int blue=0;
int green=0;

SoftwareSerial mySerial(blueTx, blueRx);  //시리얼 통신을 위한 객체선언
String myString=""; //받는 문자열

void ledsetup() { //this sets the output pins
pinMode(Red_LED, INPUT);
pinMode(Green_LED, INPUT);
pinMode(Blue_LED, INPUT);
}

void setup() {
  Serial.begin(9600);   //시리얼모니터 
  mySerial.begin(9600); //블루투스 시리얼 개방
  ledsetup();
}

void setRGB(int i){
   red = i;
   blue = 255 - i;
   green = (int)(red + blue)/2; 
}

void resetRGB(){
  red = 0;
  green = 0;
  blue = 0;
}
void other(){
  int r = random(3);
  switch(r){
  case 0:
  for(int i = 0 ; i < 20 ; i ++)
  red += i;
  if(red > 255)
  red = 0;
  break;
  case 1:
  for(int i = 0 ; i < 20 ; i ++)
  blue += i;
  if(blue > 255)
  blue = 0;
  break;
  case 2:
  for(int i = 0 ; i < 20 ; i ++)
  green += i;
  if(green > 255)
  green = 0;
  break;
  }
}

void RainBow_Color()
{
  for( int i = 0 ; i < 255 ; i ++ ) 
  {
    analogWrite( Red_LED, i );
    analogWrite( Blue_LED, 255 - i );
    delay(10);
  }
   for( int i = 0 ; i < 255 ; i ++ ) 
  {
    analogWrite( Green_LED, i );
    analogWrite( Red_LED, 255 - i );
    delay(10);
  }
   for( int i = 0 ; i < 255 ; i ++ ) 
  {
    analogWrite( Blue_LED, i );
    analogWrite( Green_LED, 255 - i );
    delay(10);
  }
}

void loop() {
  while(mySerial.available())  //mySerial에 전송된 값이 있으면
  {
    char myChar = (char)mySerial.read();  //mySerial int 값을 char 형식으로 변환
    myString+=myChar;   //수신되는 문자를 myString에 모두 붙임 (1바이트씩 전송되는 것을 연결)
    delay(5);           //수신 문자열 끊김 방지
  }
 
  if(!myString.equals(""))  //myString 값이 있다면
  {
    Serial.println("input value: "+myString); //시리얼모니터에 myString값 출력
    
    if(myString == "onoff" ){//입력받은 값이 on이면
      int s;
      int i = random(255);//0~255까지의 랜덤 숫자 반환
      if(red == 0 && blue == 0 && green == 0 )
      s = 0;
      if(red > 0 || blue > 0 || green >0)
      s = 1;
      switch(s){
        case 0://OFF 상태라면 무작위 색깔로 ON
          setRGB(i);
          analogWrite( Red_LED, red );
          analogWrite( Green_LED, green );
          analogWrite( Blue_LED, blue );
          break;
        case 1://ON 상태라면 OFF
          resetRGB();
          digitalWrite(Red_LED, HIGH);//근데 왜 HIGH로 해야 꺼지지?
          digitalWrite(Green_LED, HIGH);
          digitalWrite(Blue_LED, HIGH);
          break;
      }
     }
     if(myString == "other" ){
       other();
       analogWrite( Red_LED, red );
       analogWrite( Green_LED, green );
       analogWrite( Blue_LED, blue );
      }
    if(myString == "sleep" ){
      while(!mySerial.available())  //mySerial에 전송된 값이 없다면
      {
        RainBow_Color();//무지개빛 계속반복
      }
    }
   
    myString="";  //myString 변수값 초기화
  }
  }


