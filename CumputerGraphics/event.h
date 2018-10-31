// 이벤트 처리
#include "init.h"
#include "drawbasic.h"
#include "setting.h"

int systemInt;  // 시스템 값 출력을 위한 변수
static float sx, sy;
static float tx = 0.0f;
static float ty = 0.0f;
int viewSelected;   // 마우스로 뷰포트 선택하기 위한 변수
int viewClick;
int windowX;
int windowY;
GLfloat WidthFactor;
GLfloat HeightFactor;

// mouse click event
void mouse(int button, int state, int x, int y)
{
	sx = x;
	sy = y;
	if(state == GLUT_DOWN){                 // 뷰포트를 클릭 시
		if( x < windowX/2 ){
			if( y > (windowY/2))
				viewSelected = 3;
			else
				viewSelected = 1;
		}
		else {
			if( y > (windowY/2))
				viewSelected = 4;
			else
				viewSelected = 2;
		}
	}
	glutPostRedisplay();

}
// mouse motion event
void motion(int x, int y)
{
	switch(viewSelected){                 // 선택된 뷰포트
	case 1:
		rotateX1 += (x-sx)/2;
		rotateY1 += (y-sy)/2;
		break;
	case 2:
		rotateX2 += (x-sx)/2;
		rotateY2 += (y-sy)/2;
		break;
	case 3:
		rotateX3 += (x-sx)/2;
		rotateY3 += (y-sy)/2;
		break;
	case 4:
		p_scale += (x-sx)/200;      // 마우스 좌우이동 시 로봇의 사이즈 조정
		p_near += (y-sy)/200;       // 마우스 상하이동 시 원근투상의 near 값 조정
		break;
	}
	sx = x;
	sy = y;
	if(robotRun == false)
		glutPostRedisplay(); // display();
}
// menu select event
void MySubMenu(int entryID){
	switch(entryID){
	case 1:                               // 줌인
		if(scaleRate > 0.5)
			scaleRate -= 0.25;
		glutPostRedisplay();
		break;
	case 2:                               // 줌아웃
		if(scaleRate < 2)
			scaleRate += 0.25;
		glutPostRedisplay();
		break;
	case 3:
		if(robotRun == false){
			robotRun = true;
			glutPostRedisplay();
			glutTimerFunc(40, MyTimer, 1);  // 타이머 시작
		}
		break;
	case 4:
		if(robotRun == true)
			robotRun = false;
		break;
	case 5:
		if(robotpunch == false){
			robotpunch = true;
			glutPostRedisplay();
			glutTimerFunc(40, MyTimer, 1);  // 타이머 시작
		}
		break;
	case 6:
		if(robotRun == false)
			meshInit();      // 기본 위치로 
		glutPostRedisplay();
		robotpunch = false;
		break;
	}
}
void MySubMenu2(int entryID){              // 카메라 선택
	switch(entryID){
	case 1:
		setCamera(0.0, 1.5, 2.0,  0.0, 0.0, -0.5, 0.0, 1.0, 0.0);
		glutPostRedisplay();
		break;
	case 2:
		setCamera(2.0, -0.5, 2.0,  0.5, 0.2, 0.5, 0.0, 1.0, 0.0);
		glutPostRedisplay();
		break;
	case 3:
		setCamera(-2.0, 0.5, -2.0,  -0.5, 0.2, -0.5, 0.0, 1.0, 0.0);
		glutPostRedisplay();
		break;
	case 4:
		setCamera(2.0, 2.0, 2.0,  0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
		glutPostRedisplay();
		break;
	case 5:
		setCamera(2.0, 1.0, 2.0,  0.5, 0.5, 0.5, 0.0, 1.0, 0.0);
		glutPostRedisplay();
		break;
	}
}
void MySubMenu3(int entryID){              // Backface culling 선택
	switch(entryID){
	case 1:
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glutPostRedisplay();
		break;
	case 2:
		glDisable(GL_CULL_FACE);
		glutPostRedisplay();
		break;
	}
}
void MySubMenu4(int entryID){              // 폴리곤 모드
	switch(entryID){
	case 1:
		glPolygonMode(GL_FRONT, GL_FILL);
		glutPostRedisplay();
		break;
	case 2:
		glPolygonMode(GL_FRONT, GL_LINE);
		glutPostRedisplay();
		break;
	case 3:
		glPolygonMode(GL_FRONT, GL_POINT);
		glutPostRedisplay();
		break;
	case 4:
		glPolygonMode(GL_BACK, GL_FILL);
		glutPostRedisplay();
		break;
	case 5:
		glPolygonMode(GL_BACK, GL_LINE);
		glutPostRedisplay();
		break;
	case 6:
		glPolygonMode(GL_BACK, GL_POINT);
		glutPostRedisplay();
		break;
	}
}
void MyMainMenu(int entryID){
	switch(entryID){
	case 0:
		exit(0);
		break;
	case 1:
		meshInit();      // 기본 위치로 
		glutPostRedisplay();
		break;
	case 2:
		glGetIntegerv(GL_MAX_CLIP_PLANES, &systemInt);
		cout << "--- System Info ---\nGL_MAX_CLIP_PLANES : " << systemInt << endl;
		glGetIntegerv(GL_DEPTH_BITS, &systemInt);
		cout << "GL_DEPTH_BITS : " << systemInt << endl;
		glGetIntegerv(GL_RED_BITS, &systemInt);
		cout << "GL_RED_BITS : " << systemInt << endl;
		glGetIntegerv(GL_GREEN_BITS, &systemInt);
		cout << "GL_GREEN_BITS : " << systemInt << endl;
		glGetIntegerv(GL_BLUE_BITS, &systemInt);
		cout << "GL_BLUE_BITS : " << systemInt << endl;
	}
}
// menu reshape event
void MyReshape(int NewWidth, int NewHeight) {
	WidthFactor = (GLfloat)NewWidth / (GLfloat)800;
	HeightFactor = (GLfloat)NewHeight / (GLfloat)800;

	windowX = NewWidth;
	windowY = NewHeight;
}
// menu keyboard event
void MyKeyboard(unsigned char KeyPressed, int X, int Y){
	switch(KeyPressed){
	case 'Q':
		exit(0);
		break;
	case 'q':
		exit(0);
		break;
	case 'W':
		if(robotSpeed < 35)
			robotSpeed++;
		break;
	case 'w':
		if(robotSpeed < 35)
			robotSpeed++;
		break;
	case 'S':
		if(robotSpeed > 16)
			robotSpeed--;
		break;
	case 's':
		if(robotSpeed > 16)
			robotSpeed--;
		break;
	case 'A':
		if(robotWeight < 20)
			robotWeight++;
		break;
	case 'a':
		if(robotWeight < 20)
			robotWeight++;
		break;
	case 'D':
		if(robotWeight > 6)
			robotWeight--;
		break;
	case 'd':
		if(robotWeight > 6)
			robotWeight--;
		break;
	case 'p':
		if(robotpunch == true){
			robotpunch = false;
			meshInit();      // 기본 초기화 
			glutPostRedisplay();
		}
		else {
			robotpunch = true;
			glutPostRedisplay();
			glutTimerFunc(40, MyTimer, 1);  // 타이머 시작
		} 
		break;
	case 'P':
		if(robotpunch == true){
			robotpunch = false;
			meshInit();      // 기본 초기화
			glutPostRedisplay();
		}
		else {
			robotpunch = true;
			glutPostRedisplay();
			glutTimerFunc(40, MyTimer, 1);  // 타이머 시작
		} 
		break;
	case 'v':
		if(robotview){
			robotview = false;
		}
		else {
			robotview = true;
			robotpunch = false;
			robotRun = false;
			glutPostRedisplay();
			glutTimerFunc(40, MyTimer, 1);  // 타이머 시작
		} 
		break;
	case 'V':
		if(robotview == true){
			robotview = false;
		}
		else {
			robotview = true;
			glutPostRedisplay();
			glutTimerFunc(40, MyTimer, 1);  // 타이머 시작
		} 
		break;
	case '[':
		if( tspd < 5 )
			tspd++;
		break;
	case ']':
		if(tspd > -4)
			tspd--;
		break;
	case 'E':
		if(robotRun == true){
			robotRun = false;
		}
		else {
			robotRun = true;
			glutPostRedisplay();
			glutTimerFunc(40, MyTimer, 1);  // 타이머 시작
		} 
		break;
	case 'e':
		if(robotRun == true){
			robotRun = false;
		}
		else {
			robotRun = true;
			glutPostRedisplay();
			glutTimerFunc(40, MyTimer, 1);  // 타이머 시작
		}
		break;
	case 'R':
		meshInit();      // 기본 위치로 
		glutPostRedisplay();
		break;
	case 'r':
		meshInit();      // 기본 위치로 
		glutPostRedisplay();
		break;
	case 'Z':
		if(scaleRate > 0.5)
			scaleRate -= 0.25;
		glutPostRedisplay();
		break;
	case 'z':
		if(scaleRate > 0.5)
			scaleRate -= 0.25;
		glutPostRedisplay();
		break;
	case 'X':
		if(scaleRate < 3)
			scaleRate += 0.25;
		glutPostRedisplay();
		break;
	case 'x':
		if(scaleRate < 3)
			scaleRate += 0.25;
		glutPostRedisplay();
		break;
	}
}
