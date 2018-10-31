#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <strings.h>
#include <fcntl.h>
#include <sys/socket.h>
#include <sys/file.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <time.h>

#define MAXLINE 511
#define MAX_SOCK 1024

char *EXIT_STRING = "exit";	//클라이언트 종료 문자열
char *START_STRING = "Connected to chat_server \n";	//클라이언트 환영 메세지

int maxfdp1;				//최대 소켓 번호
int num_chat = 0;			//채팅 참가자 수
int clisock_list[MAX_SOCK];	//채팅 참가자 소켓 번호 리스트
int listen_sock;			//서버의 리슨 소켓


void logwrite(char* str);	//로그 작성 함수
void addClient(int s, struct sockaddr_in *newcliaddr);	//새로운 참가자 처리
int getmax();											//최대 소켓번호 찾기
void removeClient(int s);								//채팅 탈퇴 처리함수
int tcp_listen(int host, int port, int backlog);		//소켓 생성 및 Listen
void errquit(char *mesg){ perror(mesg); exit(1);}		//에러 종료

int main( int argc, char* argv[] ){

	struct sockaddr_in cliaddr;
	char buf[MAXLINE+1];
	char t[25];				//time 저장용 변수
	char id[20] = "";		//id 저장용 변수
	char msg[MAXLINE+1];	//로그에 저장할 메세지
	int i, j, k, nbyte, accp_sock, addrlen = sizeof(struct sockaddr_in);
	char *idf;				//id 구분용 포인터
	fd_set read_fds;

	if(argc != 2){
		printf("usage : %s port\n", argv[0]);
		exit(0);
	}

	// 소켓 생성 및 Listen
	listen_sock = tcp_listen(INADDR_ANY, atoi(argv[1]), 5);
	while(1){

	FD_ZERO(&read_fds);
	FD_SET(listen_sock, &read_fds);
	for( i = 0 ; i < num_chat ; i++ ){
		FD_SET(clisock_list[i], &read_fds);
	}
	maxfdp1 = getmax() +1;	//최대 소켓번호 계산
	puts("wait for client");
	if( select(maxfdp1, &read_fds, NULL, NULL, NULL) <0)
		errquit("select fail");

	if(FD_ISSET(listen_sock, &read_fds)){
		accp_sock = accept(listen_sock, (struct sockaddr *)&cliaddr, &addrlen);

		if(accp_sock == -1)
			errquit("accept fail");
		addClient(accp_sock, &cliaddr);
		send(accp_sock, START_STRING, strlen(START_STRING), 0 );
		printf("%d user add \n", num_chat);
	}

	//클라이언트가 보낸 메시지를 모든 클라이언트에게 전송
	for( i = 0 ; i < num_chat ; i++ ){
		if(FD_ISSET(clisock_list[i], &read_fds)){
			nbyte = recv(clisock_list[i], buf, MAXLINE, 0);
			// nbyte가 없으면 클라이언트 종료
			if(nbyte <= 0){
				removeClient(i);	
				continue;
			}
			buf[nbyte] = 0;
			// EXIT_STRING이 들어오면 클라이언트 종료
			if(strstr(buf, EXIT_STRING) != NULL ){
				removeClient(i);
				continue;
			}
			// 모든 채팅 참가자에게 메세지 발송 
			for( j = 0 ; j < num_chat ; j ++)	
				send(clisock_list[j], buf, nbyte, 0);
			// 로그 남기기 [시간] [아이디] [메세지] [메세지길이]
			buf[strlen(buf)-1]='\0';	// 읽어온 메세지의 개행문자를 \0으로 변경
			printf("%s\n", buf);		// 해당 내용 서버에 print
			time_t current_time;		// 시간 기록을 위한 변수 선언
			time(&current_time);		// time 저장
			strcpy(t, ctime(&current_time));	// 문자열 형태로 저장
			t[24]='\0';							// 마지막 개행문자를 \0으로 변경
			idf = strchr(buf,']');				// id 구분을 위한 주소 기록
			for( k = 0 ; k < idf-buf-1 ; k ++ )	// idf-buf하면 ']'문자의 주소가 몇번째인지 나옴, 그럼 그것에서 -1을하면 아이디의 마지막 문자
				id[k] = buf[1+k];
			// msg 문자열에 [시간] [아이디] [메세지] [메세지길이] 형태로 기록
			sprintf(msg, "[%s] [ user_id : %s ] [ user_message %s ] [ user_message_num : %d ]",t, id, strstr(buf,":"), strchr(buf,'\0')-strchr(buf,':')-2);
			// 로그 기록
			logwrite(msg);
			}
		}
	}

	return 0;
}
// 로그 기록 함수
void logwrite(char* str)
{
    FILE *log;
    char fname[20];

    sprintf(fname,"%s.log","chat");
    log = fopen(fname,"ab");

    fprintf(log,"%s\r\n", str);

    fclose(log);
    return;
}
// 새로운 채팅 참가자 처리
void addClient(int s, struct sockaddr_in *newcliaddr){
	char buf[20];
	char t[25];
	char msg[MAXLINE+1];

	inet_ntop(AF_INET, &newcliaddr->sin_addr, buf, sizeof(buf));
	printf("new client : %s\n", buf);
	clisock_list[num_chat] = s;
	num_chat++;
	// [시간] [총 유저] [유저 아이피] 형태로 로그기록
	time_t current_time;
	time(&current_time);
	strcpy(t, ctime(&current_time));
	t[24]='\0';
	sprintf(msg, "[%s] [ total_user : %d ] [ user_ip : %s ]",t, num_chat, buf);
	logwrite(msg);

	
}
// 채팅 탈퇴 처리
void removeClient(int s){
	char msg[MAXLINE+1];
	char t[25];

	close(clisock_list[s]);
	if(s != num_chat-1)
		clisock_list[s] = clisock_list[num_chat-1];
	num_chat--;

	printf("chat user exit. now user num = %d\n", num_chat);
	// [시간] [총 유저] [메세지] 형태로 로그기록
	time_t current_time;
	time(&current_time);
	strcpy(t, ctime(&current_time));
	t[24]='\0';
	sprintf(msg, "[%s] [ total_user : %d ] [ 1 user leave ]",t, num_chat);
}
// 최대 소켓 번호 찾기
int getmax(){
	int max = listen_sock;
	int i;
	for( i = 0 ; i < num_chat ; i++ )
		if(clisock_list[i] > max)
			max = clisock_list[i];
	return max;
}

// listen 소켓 생성 및 listen
int tcp_listen(int host, int port, int backlog){
	int sd;
	char msg[MAXLINE+1];
	char t[25];
	struct sockaddr_in servaddr;
	
	sd = socket ( AF_INET, SOCK_STREAM, 0 );
	if( sd == -1 ){
		perror("socket fail");
		exit(1);
	}
	// servaddr 구조체의 내용 세팅
	bzero((char *) &servaddr, sizeof(servaddr));
	servaddr.sin_family = AF_INET;
	servaddr.sin_addr.s_addr = htonl(host);
	servaddr.sin_port = htons(port);
	if(bind(sd, (struct sockaddr *)&servaddr, sizeof(servaddr)) < 0 ){
		perror("bind fail"); exit(1);
	}
	// ------[ 채팅 서버 실행 시간 ] [ 사용 포트 ]------ 형태로 로그 기록
	time_t current_time;
	time(&current_time);
	strcpy(t, ctime(&current_time));
	t[24]='\0';
	sprintf(msg, "------[Chat server ON At : %s ] [ Use Port : %d ]------",t, port);
	logwrite(msg);

	// 클라이언트로부터 연결 요청을 기다림
	listen(sd, backlog);
	return sd;
}
