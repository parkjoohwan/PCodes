// 카메라/타이머
float camEyeX;  // 카메라 설정 값 변수
float camEyeY;
float camEyeZ;
float camAtX;
float camAtY;
float camAtZ;
float camUpX;
float camUpY;
float camUpZ;


// 타이머 함수
void MyTimer(int Value){
	if(robotRun){                       // 달리는 모션이면 타이머 함수 적용
		glutPostRedisplay();
		glutTimerFunc(40, MyTimer, 1);
	}
	if(robotpunch){						// 주먹을 날리는 모션이면 타이머 함수 적용
		glutPostRedisplay();
		glutTimerFunc(40, MyTimer, 1);
	}
	if(robotview){						// 주먹을 날리는 모션이면 타이머 함수 적용
		glutPostRedisplay();
		glutTimerFunc(40, MyTimer, 1);
	}
}
// 카메라/시점 설정 함수
void setCamera(float EyeX, float EyeY, float EyeZ, float AtX, float AtY, float AtZ, float UpX, float UpY, float UpZ){
	camEyeX = EyeX;
	camEyeY = EyeY;
	camEyeZ = EyeZ;
	camAtX = AtX;
	camAtY = AtY;
	camAtZ = AtZ;
	camUpX = UpX;
	camUpY = UpY;
	camUpZ = UpZ;
}
