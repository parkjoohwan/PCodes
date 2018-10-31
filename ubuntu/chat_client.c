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

#define MAXLINE 1000
#define NAME_LEN 20

char *EXIT_STRING = "exit";
int tcp_connect(int af, char *servip, unsigned short port);	// 소켓 생성 및 서버 연결, 생성된 소켓 리턴
void errquit(char *mesg) { perror(mesg); exit(1); }

int main( int argc, char* argv[]){
	char bufall[MAXLINE + NAME_LEN], *bufmsg;	// 이름 + 메세지를 위한 버퍼, bufmsg는 bufall에서 메시지부분의 포인터
	int maxfdp1, s, namelen;					// 최대 소켓 디스크립터, 소켓, 이름의 길이
	fd_set read_fds;							

	if(argc != 4){
		printf(" usage : %s sever_ip port name \n", argv[0]);
		exit(0);
	}

	sprintf(bufall, "[%s] : ", argv[3]);	//bufall의 앞 부분에 이름을 저장
	namelen = strlen(bufall);
	bufmsg = bufall + namelen;				// 메세지 시작부분 지정
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
			}
		}
	}
}


int tcp_connect ( int af, char *servip, unsigned short port){
	struct sockaddr_in servaddr;
	int s;
	// 소켓생성
	if( ( s = socket(af, SOCK_STREAM, 0 ) ) < 0 )
		return -1;
	// 채팅 서버의 소켓 주소 구조체 seraddr 초기화
	bzero((char *)&servaddr, sizeof(servaddr));
	servaddr.sin_family = af;
	inet_pton(AF_INET, servip, &servaddr.sin_addr);
	servaddr.sin_port = htons(port);

	//연결 요청
	if(connect(s, (struct sockaddr *)&servaddr, sizeof(servaddr)) < 0)
		return -1;
	return s;
}
