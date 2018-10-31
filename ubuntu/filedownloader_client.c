#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <strings.h>
#include <fcntl.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <sys/time.h>
#include <unistd.h>
#include <arpa/inet.h>

#define MAXLINE 511
#define NAME_LEN 20
#define BUFSIZE 30

char *EXIT_STRING = "exit";	// exit 처리용 문자열
char *DOWNLOAD = "get";		// get(download) 처리용 문자열
char *UPLOAD = "put";		// put(upload) 처리용 문자열
int tcp_connect(int af, char *servip, unsigned short port);
char* ltrim(char *s);		// 공백제거를 위한 함수
void errquit(char *mesg) { perror(mesg); exit(1); }


int main( int argc, char* argv[]){
	char bufall[MAXLINE + NAME_LEN], *bufmsg;	// 문자열 변수
	char buf[BUFSIZE];	
	int maxfdp1, s, namelen;// 소켓 처리를 위한 변수	
	int len, fd;			// 파일 처리를 위한 변수
	fd_set read_fds;		
	char fname[30];			// 파일 이름 처리를 위한 변수
	char *tf;				// 파일 이름 처리를 위한 변수2

	// 사용 방법 : 파일명 (ip) (port)
	if(argc != 3){	
		printf(" usage : %s sever_ip port\n", argv[0]);
		exit(0);
	}
	// 중간대체에서 썻던 형식과 같으나 id 대신 ip를 기본 정보로 이용
	sprintf(bufall, "[%s] : ", argv[1]);	
	namelen = strlen(bufall);
	bufmsg = bufall + namelen;				
	s = tcp_connect (AF_INET, argv[1], atoi(argv[2]));
	if( s == -1 )
		errquit("tcp_connect fail");

	puts("connect to server");
	maxfdp1 = s + 1;
	FD_ZERO(&read_fds);

	while(1){
		FD_SET(0, &read_fds);
		FD_SET(s, &read_fds);
		if(select(maxfdp1, &read_fds, NULL, NULL, NULL) < 0 )
			errquit("select fail");
		if(FD_ISSET(s, &read_fds)){
			int nbyte;
			if((nbyte = recv(s, bufmsg, MAXLINE, 0 )) > 0 ){
				bufmsg[nbyte] = 0;
				printf("%s \n", bufmsg);
			}
		}
		if( FD_ISSET(0, &read_fds)){
			if(fgets(bufmsg, MAXLINE,stdin)) {
				if(send(s, bufall, namelen + strlen(bufmsg), 0)< 0)
					puts("Error: Write error on socket.");
				if(strstr(bufmsg, EXIT_STRING) != NULL ){
					puts("Good bye.");
					close(s);
					exit(0);
				}
				// 원래 exit 메세지를 처리하던 방식을 활용하여 get 메세지를 처리함
				if(strstr(bufmsg, DOWNLOAD) != NULL ){
					puts("DOWNLOADING....");
					// bugmsg가 get (파일명)인것을 이용, ' '(공백)이후의 메세지만 자름
					tf = strrchr(bufmsg,' ');
					// 해당 메세지에서 공백을 제거한 후 fname(파일명)을 저장
					sprintf(fname, "%s", ltrim(tf));
					fname[strlen(fname)-1] = '\0';	// 파일명의 마지막 문자열(\n)을 NULL로 바꿈

					// 다운로드하는 파일의 이름을 클라이언틑에게 명시함
					printf("다운로드 하는 파일 : %s\n", fname);
					// 수신 한 데이터를 저장 할 파일 오픈
					fd = open(fname, O_RDWR|O_CREAT|O_TRUNC, 0777);  
					if(fd == -1)  
						errquit("File open error");  

					// 데이터를 전송받아 파일에 저장
					while( (len=read(s, buf, BUFSIZE )) != 0 )  
					{  
						write(fd, buf, len);   
					}  
					//이후 소켓을 닫고 프로그램 종료
					close(s);
					exit(0);
				}
				// put도 get과 마찬가지 방식으로 처리함
				if(strstr(bufmsg, UPLOAD) != NULL ){
					puts("UPLOADING....");

					tf = strrchr(bufmsg,' ');
					sprintf(fname, "%s", ltrim(tf));
					fname[strlen(fname)-1] = '\0';

					printf("업로드 하려는 파일명 : %s\n", fname);
					sleep(1);	// 1초 대기
					// 서버에게 전송해줄 파일 오픈
					fd = open( fname, O_RDONLY );   
					if(fd == -1)  
						errquit("File open error");  

					while( (len=read(fd, buf, BUFSIZE)) != 0 )  
					{  
						send(s, buf, len,0);  
					}  
					// 업로드가 끝났다는 것을 클라이언트측에 표시함
					printf("\n------------업로드 완료-----------\n");

					// 이후 소켓을 닫고 프로그램 종료
					close(s);
					exit(0);
				}
			}
		}
	}
}

// 문자열 내에 공백을 제거하기 위한 함수
char* ltrim(char *s) 
{
	char* begin;
	begin = s;

	while (*begin != '\0')
	{
		if (isspace(*begin))
			begin++;
		else 
		{
			s = begin;
			break;
		}
	}

	return s;
}

int tcp_connect ( int af, char *servip, unsigned short port){
	struct sockaddr_in servaddr;
	int s;
	if( ( s = socket(af, SOCK_STREAM, 0 ) ) < 0 )
		return -1;
	bzero((char *)&servaddr, sizeof(servaddr));
	servaddr.sin_family = af;
	inet_pton(AF_INET, servip, &servaddr.sin_addr);
	servaddr.sin_port = htons(port);
	if(connect(s, (struct sockaddr *)&servaddr, sizeof(servaddr)) < 0)
		return -1;
	return s;
}

