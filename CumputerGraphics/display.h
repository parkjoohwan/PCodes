//화면 디스플레이
#include <math.h>
#include "drawrobot.h"
#define PI   (3.14159265358979323846)
//디스플레이 함수
void MyDisplay() {

	if(robotRun == true){
		robotRange += robotSpeed;
		if(robotRange > 360)
			robotRange -= 360;
		dRadian = (PI/180.0) * robotRange;
		sinR = (float)sin(dRadian);
		sinR2 = (float)sin(2*dRadian);
		cosR =  (float)cos(dRadian);

		dRadian = (PI/180.0) * (robotRange - 180);
		d_cosR = (float)cos(dRadian);
		d_sinR = (float)sin(dRadian);
	}
	if(robotpunch == true){
		dz+=0.2;
		if( dz > 4 )
			dz = 0;
	}
	if(robotview){
		vy+=5+tspd;
		if( vy == 360  )
			vy = 0;
	}

	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();

	glOrtho (-1 * WidthFactor, 1 * WidthFactor, -1 * HeightFactor, 1 * HeightFactor, -4, 4);

	glMatrixMode (GL_MODELVIEW);
    glLoadIdentity();
	

	glViewport(0, windowY/2, windowX/2, windowY/2);        // 1 뷰포트
		if(viewSelected == 1){
			setColor(1.0, 0.0, 0.0, 1.0);
			glBegin(GL_LINES);
			glVertex3f(-0.99, -0.99, 0.0);  // 해당 뷰포트 마우스로 클릭되었음을 나타내는 사각형
			glVertex3f(0.99, -0.99, 0.0);  
			glVertex3f(0.99, -0.99, 0.0);  
			glVertex3f(0.99, 0.99, 0.0);  
			glVertex3f(0.99, 0.99, 0.0);  
			glVertex3f(-0.99, 0.99, 0.0); 
			glVertex3f(-0.99, 0.99, 0.0);  
			glVertex3f(-0.99, -0.99, 0.0);  
			glEnd();
		}
		glPushMatrix();
			gluLookAt(0.0, 0.5, 1.0,  0.0, 0.5, 0.0,  0.0, 1.0, 0.0 );

			glPushMatrix();

				glTranslated (0, 0.8, 0);
				glScalef (0.8f, 0.8f, 0.8f);
				glRotatef (rotateY1, 1, 0, 0);
				glRotatef (rotateX1, 0, 1, 0); 

				drawRobot();                  // 로봇 출력
			glPopMatrix();

			
			drawCoord (1);
			drawGridPlane();

		glPopMatrix();


	glViewport(windowX/2, windowY/2, windowX/2, windowY/2);        // 2 뷰포트
		if(viewSelected == 2){
			setColor(1.0, 0.0, 0.0, 1.0);
			glBegin(GL_LINES);
			glVertex3f(-0.99, -0.99, 0.0);  // 해당 뷰포트 마우스로 클릭되었음을 나타내는 사각형
			glVertex3f(0.99, -0.99, 0.0);  
			glVertex3f(0.99, -0.99, 0.0);  
			glVertex3f(0.99, 0.99, 0.0);  
			glVertex3f(0.99, 0.99, 0.0);  
			glVertex3f(-0.99, 0.99, 0.0); 
			glVertex3f(-0.99, 0.99, 0.0);  
			glVertex3f(-0.99, -0.99, 0.0);  
			glEnd();
		}
		glPushMatrix();
			gluLookAt(1.0, 0.5, 0.0,  0.0, 0.5, 0.0,  0.0, 1.0, 0.0 );

			glPushMatrix();

				glTranslated (0, 0.8, 0);
				glScalef (0.8f, 0.8f, 0.8f);
				glRotatef (rotateY2, 1, 0, 0);
				glRotatef (rotateX2, 0, 1, 0); 

				drawRobot();                  // 로봇 출력
			glPopMatrix();

			
			drawCoord (1);
			drawGridPlane();

		glPopMatrix();


	glViewport(0, 0, windowX/2, windowY/2);        // 3 뷰포트
		if(viewSelected == 3){
			setColor(1.0, 0.0, 0.0, 1.0);
			glBegin(GL_LINES);
			glVertex3f(-0.99, -0.99, 0.0);  // 해당 뷰포트 마우스로 클릭되었음을 나타내는 사각형
			glVertex3f(0.99, -0.99, 0.0);  
			glVertex3f(0.99, -0.99, 0.0);  
			glVertex3f(0.99, 0.99, 0.0);  
			glVertex3f(0.99, 0.99, 0.0);  
			glVertex3f(-0.99, 0.99, 0.0); 
			glVertex3f(-0.99, 0.99, 0.0);  
			glVertex3f(-0.99, -0.99, 0.0);  
			glEnd();
		}
		glPushMatrix();
			gluLookAt(0.0, 1.0, 0.0,  0.0, 0.0, 0.0,  0.0, 0.0, -1.0 );

			glPushMatrix();

				glTranslated (0, 0.8, 0);
				glScalef (0.8f, 0.8f, 0.8f);
				glRotatef (rotateY3, 1, 0, 0);
				glRotatef (rotateX3, 0, 1, 0); 

				drawRobot();                  // 로봇 출력
			glPopMatrix();

			
			drawCoord (1);
			drawGridPlane();

		glPopMatrix();		
	



	glViewport(windowX/2, 0, windowX/2, windowY/2);        // 4 뷰포트
		if(viewSelected == 4){
			setColor(1.0, 0.0, 0.0, 1.0);
			glBegin(GL_LINES);
			glVertex3f(-0.99, -0.99, 0.0);  // 해당 뷰포트 마우스로 클릭되었음을 나타내는 사각형
			glVertex3f(0.99, -0.99, 0.0);  
			glVertex3f(0.99, -0.99, 0.0);  
			glVertex3f(0.99, 0.99, 0.0);  
			glVertex3f(0.99, 0.99, 0.0);  
			glVertex3f(-0.99, 0.99, 0.0); 
			glVertex3f(-0.99, 0.99, 0.0);  
			glVertex3f(-0.99, -0.99, 0.0);  
			glEnd();
		}
		
		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
			glLoadIdentity();
			
			glFrustum(-0.1 * WidthFactor, 0.1 * WidthFactor, -0.1 * HeightFactor, 0.1 * HeightFactor, p_near, 20);

			glMatrixMode (GL_MODELVIEW);

			glPushMatrix();

				gluLookAt(camEyeX, camEyeY, camEyeZ, camAtX, camAtY, camAtZ, camUpX, camUpY, camUpZ);

				glScalef (1.0f / scaleRate, 1.0f / scaleRate, 1.0f / scaleRate);
				glPushMatrix();

					glTranslated (0, 0.8, 0);
					glScalef (0.8f / p_scale, 0.8f / p_scale, 0.8f / p_scale);

					drawRobot();                  // 로봇 출력
				glPopMatrix();
			
				drawCoord (1);
				drawGridPlane();

			glPopMatrix();
			glMatrixMode(GL_PROJECTION);
		glPopMatrix();

	glutSwapBuffers();
	glFlush();
}
