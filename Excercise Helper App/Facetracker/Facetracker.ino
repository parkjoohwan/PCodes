#include <Servo.h> //서보모터 라이브러리 사용
//수직, 수평으로 움직이는 서보모터를 구별하기 위한 변수
char verticalSignal=0, horizonSignal=1; 
Servo servoV, servoH; //서보모터 객체 생성 
char serialChar=0; 
//서보모터의 핀번호를 설정하고, 시리얼통신을 초기화하며 모터의 초기각도를 지정한다. 
void setup(){ 
  servoV.attach(9); 
  servoH.attach(10); 
  servoV.write(90); 
  servoH.write(90); 
  Serial.begin(9600); 
  } 
  
void loop(){
  while(Serial.available() <=0); //시리얼통신을 통해 데이터를 받을때까지 대기한다
  serialChar = Serial.read(); //데이터를 받았을 경우
  
  if(serialChar == verticalSignal){  //수직모터를 움직이라는 신호를 받았을 경우
    delay(200);
    while(Serial.available() <=0); //각도값을 받을 때까지 대기
    servoV.write(Serial.read()); //각도값을 받았을 경우 모터를 전송받은 값만큼 움직인다
  }
  else if(serialChar == horizonSignal){ //수평모터를 움직이라는 신호를 받았을 경우
    delay(200);
    while(Serial.available() <= 0);  //각도값을 받을 때까지 대기
    servoH.write(Serial.read());  //각도값을 받았을 경우 모터를 전송받은 값만큼 움직인다
  }
}
