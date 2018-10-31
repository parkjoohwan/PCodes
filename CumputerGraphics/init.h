// 기본 초기화
#include <gl/glut.h>
#include <gl/gl.h>
#include <gl/glu.h>

//Mesh *Robot;
Mesh *Head;        // 로봇의 몸 부위 객체
Mesh *Body;
Mesh *RightArm;
Mesh *RightHand;
Mesh *LeftArm;
Mesh *LeftHand;
Mesh *RightLeg;
Mesh *RightFoot;
Mesh *LeftLeg;
Mesh *LeftFoot;

float scale;         // ase 파일을 불러올 때 그 mesh 객체의 크기를 뷰포트 내에 알맞게 조정하기 위한 변수
float scaleRate;     // 프로그램에서 z, x 키를 통해 줌인/줌아웃을 하기 위한 변수

float rotateX1;   // 1번 뷰포트의 Y축 기준 회전 각도 변수
float rotateY1;   // 1번 뷰포트의 X축 기준 회전 각도 변수
float rotateX2;
float rotateY2;
float rotateX3;
float rotateY3;
float rotateX4;
float rotateY4;

float p_near;   // 원근투상의 near 값
float p_scale;  // 원근투상의 확대, 축소를 위한 변수

bool robotRun;            // 로봇의 달리는 애니메이션 유무
bool robotpunch;		  // 로봇 펀치 유무
bool robotview;			  // 로봇 턴 뷰 유무
float robotWeight;        // 로봇의 무게
char robotWeightStr[3];   // 로봇의 무게를 출력하기 위한 문자열 
double robotSpeed;        // 로봇의 달리는 속도
char robotSpeedStr[3];    // 로봇의 속도를 출력하기 위한 문자열 
double robotRange;        // 로봇이 달리는 정도를 구하기 위한 각도
double dRadian;           // 위 각도의 변수로 sin, cos 값을 구하기 위한 라디안 변수
float sinR;               // sin, cos 값으로 로봇의 팔,다리, 몸, 머리의 각 회전 각도를 조절
float sinR2;
float cosR;
float d_cosR;
float d_sinR;
float dz;				// 로봇 펀치 
float vy;				// 로봇 턴 뷰
float tspd;				// 턴 뷰 스피드
GLfloat mat_diffuse[6];
GLfloat mat_ambient[6];

// ase 파일 읽기
void meshLoad(){      

	Head = new Mesh();
	Head->readAse("S_Head.ASE");

	Body = new Mesh();
	Body->readAse("S_Body.ASE");

	RightArm = new Mesh();
	RightArm->readAse("S_RightArm.ASE");

	RightHand = new Mesh();
	RightHand->readAse("S_RightHand.ASE");

	LeftArm = new Mesh();
	LeftArm->readAse("S_LeftArm.ASE");

	LeftHand = new Mesh();
	LeftHand->readAse("S_LeftHand.ASE");

	RightLeg = new Mesh();
	RightLeg->readAse("S_RightLeg.ASE");

	RightFoot = new Mesh();
	RightFoot->readAse("S_RightFoot.ASE");

	LeftLeg = new Mesh();
	LeftLeg->readAse("S_LeftLeg.ASE");

	LeftFoot = new Mesh();
	LeftFoot->readAse("S_LeftFoot.ASE");

}
// mesh 의 회전방향, 이동 위치 좌표를 초기화
void meshInit() {      
	robotWeight = 10;
	robotSpeed = 20;
	robotRun = false;
	robotpunch = false;
	robotview = false;
	robotRange = 0;
	dRadian = 0;
	sinR = 0;
	cosR = 0;
	sinR2 = 0;
	d_cosR = 0;
	d_sinR = 0;
	dz = 0;
	vy = 0;
	tspd = 0;

	sprintf(robotWeightStr, "%.f", robotWeight - 5);
	sprintf(robotWeightStr, "%.f", (float)robotSpeed - 15);

	scaleRate = 1;
	rotateX1 = 0.0f;
	rotateY1 = 0.0f;
	rotateX2 = 0.0f;
	rotateY2 = 0.0f;
	rotateX3 = 0.0f;
	rotateY3 = 0.0f;
	rotateX4 = 0.0f;
	rotateY4 = 0.0f;
	p_near = 0.2;
	p_scale = 1;

	if( Body->max > 1 )
		scale = (float)floor((double)(Body->max+0.5)) * 2;
	else
		scale = 1;
}
// 그리는 색 설정
void setColor(float r, float g, float b, float a){

	mat_diffuse[0] = r;
	mat_diffuse[1] = g;
	mat_diffuse[2] = b;
	mat_diffuse[3] = a;

	mat_ambient[0] = r;
	mat_ambient[1] = g;
	mat_ambient[2] = b;
	mat_ambient[3] = a;

	glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, mat_diffuse);
	glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, mat_ambient);
}
// 광원 초기화
void InitLight(){
	    // mesh 의 재질 색, 광원 값
	GLfloat mat_specular[ ] = {0.5, 1.0, 0.5, 1.0};
	GLfloat mat_shininess[ ] = {70.0};
	    // 조명의 색, 광원 값
	GLfloat light_specular[ ] = {0.8, 0.5, 0.8, 1.0};
	GLfloat light_diffuse[ ] = {0.7, 0.7, 0.7, 1.0};
	GLfloat light_ambient[ ] = {0.4, 0.4, 0.4, 1.0};
	GLfloat light_position[ ] = {-1.5 / scaleRate, 0.5 / scaleRate, 0.5, 0.0};
	glShadeModel(GL_SMOOTH);
	glEnable(GL_LIGHTING);  // 조명 사용 설정
	glEnable(GL_LIGHT0);
	glEnable(GL_NORMALIZE); // 정규화
        // mesh 의 재질 색, 광원  및  조명의 색, 광원 설정
	glLightfv(GL_LIGHT0, GL_POSITION, light_position);
	glLightfv(GL_LIGHT0, GL_DIFFUSE, light_diffuse);
	glLightfv(GL_LIGHT0, GL_SPECULAR, light_specular);
	glLightfv(GL_LIGHT0, GL_AMBIENT, light_ambient);

	setColor(0.5, 0.5, 0.5, 1.0);
	glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, mat_specular);
	glMaterialfv(GL_FRONT_AND_BACK, GL_SHININESS, mat_shininess);
}