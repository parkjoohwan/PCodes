#include <iostream>
#include <string.h>
#include "ase.h"
#include "event.h"
#include "display.h"
using namespace std;

// main
int main(int argc, char **argv)
{
	windowX = 800;
	windowY = 800;
	WidthFactor = 1;
	HeightFactor = 1;
	camEyeX = 2.0;
	camEyeY = 1.0;
	camEyeZ = 2.0;
	camAtX = 0.5;
	camAtY = 0.5;
	camAtZ = 0.5;
	camUpX = 0;
	camUpY = 1;
	camUpZ = 0;
	viewSelected = 4;
	viewClick = 0;

	meshLoad();
	meshInit();

	cout << "--- Control Key ---\n\nRun / Stop : E \nRun Faster : W\nRun Slower : S\nIncrease Weight : A\n";
	cout << "Loose Weight : D\nZoom in : Z\nZoom out : X\nReload : R\nQuit : Q\n";
	cout << "\n--- My Function ---\n\nPunch / Stop : P\nTurn View : V\nTurn Speed up : [\nTurn Speed down : ]";

	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE);
	glutInitWindowSize(windowX, windowY);
	glutInitWindowPosition(300, 100); 
	glutCreateWindow("2013136046 Opengl #6");
	glClearColor(1.0, 1.0, 1.0, 1.0);
	InitLight();       // 조명값 설정
	glEnable(GL_CULL_FACE);
  	glFrontFace(GL_CCW);
	glCullFace(GL_BACK);
	glEnable(GL_DEPTH_TEST);  // 숨겨진 면을 제거, 시점으로부터 mesh 의 각 픽셀의 깊이 값을 계산하여 출력
	glShadeModel(GL_SMOOTH); 

	GLint MySubMenuID = glutCreateMenu(MySubMenu);
	
	glutAddMenuEntry("Zoom in", 1);
	glutAddMenuEntry("Zoom out", 2);
	glutAddMenuEntry("Run", 3);
	glutAddMenuEntry("Stop", 4);
	glutAddMenuEntry("punch", 5);
	glutAddMenuEntry("Stop punch", 6);

	GLint MySubMenuID2 = glutCreateMenu(MySubMenu2);
	
	glutAddMenuEntry("Camera 1", 1);
	glutAddMenuEntry("Camera 2", 2);
	glutAddMenuEntry("Camera 3", 3);
	glutAddMenuEntry("Camera 4", 4);
	glutAddMenuEntry("default Camera", 5);
	
	GLint MySubMenuID3 = glutCreateMenu(MySubMenu3);
	
	glutAddMenuEntry("Backface Culling On", 1);
	glutAddMenuEntry("Backface Culling Off", 2);

	GLint MySubMenuID4 = glutCreateMenu(MySubMenu4);
	
	glutAddMenuEntry("Frontface Fill", 1);
	glutAddMenuEntry("Frontface Line", 2);
	glutAddMenuEntry("Frontface Point", 3);
	glutAddMenuEntry("Backface Fill", 4);
	glutAddMenuEntry("Backface Line", 5);
	glutAddMenuEntry("Backface Point", 6);

	GLint MyMainMenuID = glutCreateMenu(MyMainMenu);
	glutAddSubMenu("Motion", MySubMenuID);
	glutAddSubMenu("Camera", MySubMenuID2);
	glutAddSubMenu("BackfaceCulling", MySubMenuID3);
	glutAddSubMenu("PolygonMode", MySubMenuID4);
	glutAddMenuEntry("Reset", 1);
	glutAddMenuEntry("System Info", 2);
	glutAddMenuEntry("Exit", 0);

	glutAttachMenu(GLUT_RIGHT_BUTTON);
	glutKeyboardFunc(MyKeyboard);
	glutDisplayFunc(MyDisplay);
	glutReshapeFunc(MyReshape);
	glutMouseFunc(mouse);
	glutMotionFunc(motion);

	glOrtho (-1, 1, -1, 1, -4, 4);
	glutMainLoop();
  
	delete Head;
	delete Body;
	delete RightArm;
	delete RightHand;
	delete LeftArm;
	delete LeftHand;
	delete RightLeg;
	delete RightFoot;
	delete LeftLeg;
	delete LeftFoot;

	return 0;
}