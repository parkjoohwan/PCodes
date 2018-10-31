// x,y,zÃà / ¹Ù´Ú / x,y,z ±Û¾¾ ±×¸®±â

void drawGridPlane()
{
	int		i;
	double	x, z;

	glLineWidth(1);

    glPushMatrix();
		glBegin (GL_LINES);
			x = -1.0;
			z = -1.0;
			for( i=0 ; i<=10 ; i++ ) {
				setColor(i/20.f, 0, 0, 0.2);
				glVertex3f (x, 0, z);
				glVertex3f (x, 0, z+2);

				setColor(0, 0, i/20.f, 0.2);
				glVertex3f (z, 0, x);
				glVertex3f (z+2, 0, x);
				x += 0.2;
			}
		glEnd();
    glPopMatrix();
}
void drawString (const char *s)
{
    unsigned int i;

    for (i= 0; i < strlen (s); i++)
	glutBitmapCharacter (GLUT_BITMAP_HELVETICA_12, s[i]);
}
void drawCoord(int lineWidth)
{
	glLineWidth(lineWidth);

    glPushMatrix();
		glScaled (0.9, 0.9, 0.9);
		glBegin (GL_LINES);
			setColor(1.0f, 0, 0, 0);
			glVertex3f (0.0f, 0, 0);
			glVertex3f (1.0f, 0, 0);	// RED   ==> +x axis
			
			setColor(0, 1.0f, 0, 0);
			glVertex3f (0, 0.0f, 0);
			glVertex3f (0, 1.0f, 0);	// GREEN ==> +y axis
			
			setColor(0, 0, 1.0f, 0);
			glVertex3f (0, 0, 0.0f);
			glVertex3f (0, 0, 1.0f);	// BLUE  ==> +z axis
		glEnd();

		glLineWidth(1);

		setColor(1.0f, 0, 0, 0);
		glRasterPos3f (1.0f, 0, 0);
		drawString ("x");

		setColor(0, 1.0f, 0, 0);
		glRasterPos3f (0, 1.0f, 0);
		drawString ("y");

		setColor(0, 1.0f, 0, 0);
		glRasterPos3f (0, 0, 1.0f);
		drawString ("z");

    glPopMatrix();
}