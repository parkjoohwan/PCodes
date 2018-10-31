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
#include <dirent.h>

#define MAXLINE 511
#define MAX_SOCK 1024
#define BUFSIZE 30

char *EXIT_STRING = "exit";	// exit 처리용 문자열
char *DOWNLOAD = "get";		// get(download) 처리용 문자열
char *UPLOAD = "put";		// put(upload) 처리용 문자열
char *START_STRING = "Connected to chat_server \n";	

int maxfdp1;				
int num_chat = 0;			
int clisock_list[MAX_SOCK];	
int listen_sock;			

char* ltrim(char *s);		// 공백 제거 함수
void logwrite(char* str);	
void addClient(int s, struct sockaddr_in *newcliaddr);	
int getmax();											
void removeClient(int s);								
int tcp_listen(int host, int port, int backlog);		
void errquit(char *mesg){ perror(mesg); exit(1);}	

int main( int argc, char* argv[] ){

	struct sockaddr_in cliaddr;
	char buf[MAXLINE+1];
	char t[25];				// 로그에 기록할 시간을 저장하기 위한 변수
	char msg[MAXLINE+1];	// 로그에 기록할 메세지를 저장하기 위한 변수
	char temp[MAXLINE+1];	// 서버에서 처리되는 문자열들을 저장하기위한 변수
	char up[BUFSIZE];		// 혹시모를 오류를 위해 업로드시에는 다른 변수를 사용
	int i, j, k, nbyte, accp_sock, addrlen = sizeof(struct sockaddr_in);
	int len, fd;			// 파일 처리를 위한 변수들
	fd_set read_fds;

	char *defalt = "./";	
	const char *name = "./";	// 서버측의 다운로드 가능한 파일을 확인하기 위한 기본 디렉토리
	struct dirent *d;
	DIR *dp;
	char *tf;
	char fname[10];
	// 실행 방법 : (파일명) (port)
	if(argc != 2){
		printf("usage : %s port\n", argv[0]);
		exit(0);
	}

	listen_sock = tcp_listen(INADDR_ANY, atoi(argv[1]), 5);
	while(1){

		FD_ZERO(&read_fds);
		FD_SET(listen_sock, &read_fds);
		for( i = 0 ; i < num_chat ; i++ ){
			FD_SET(clisock_list[i], &read_fds);
		}
		maxfdp1 = getmax() +1;	
		puts("wait for client");
		if( select(maxfdp1, &read_fds, NULL, NULL, NULL) <0)
			errquit("select fail");

		if(FD_ISSET(listen_sock, &read_fds)){
			accp_sock = accept(listen_sock, (struct sockaddr *)&cliaddr, &addrlen);

			if(accp_sock == -1)
				errquit("accept fail");
			addClient(accp_sock, &cliaddr);
			// 처음 클라이언트가 접속하면 다운로드 가능한 파일 목록들을 클라이언트에게 확인시켜줌
			send(accp_sock, START_STRING, strlen(START_STRING), 0 );
			sprintf(temp, "\n-----------------다운로드 가능 파일 목록-----------------\n\n");
			send(clisock_list[j], temp, strlen(temp), 0);

			// 시스템 프로그래밍때 배운 디렉토리 구조 확인 시스템콜을 이용함
			if((dp=opendir(name)) == NULL) exit(-1);
			while( d = readdir(dp)){
				if( d->d_ino != 0 ){
					sprintf(temp, "%s\n", d->d_name);
					send(clisock_list[j], temp, strlen(temp), 0);
				}
			}
			// 처음 클라이언트에게 알려주는 안내 메세지
			sprintf(temp, "\n----tip : 다운로드 시에는 get (파일명)을 입력해주세요----\n");
			send(clisock_list[j], temp, strlen(temp), 0);
			printf("%d user add \n", num_chat);
		}


		for( i = 0 ; i < num_chat ; i++ ){
			if(FD_ISSET(clisock_list[i], &read_fds)){
				nbyte = recv(clisock_list[i], buf, MAXLINE, 0);

				if(nbyte <= 0){
					removeClient(i);	
					continue;
				}
				buf[nbyte] = 0;

				if(strstr(buf, EXIT_STRING) != NULL ){
					removeClient(i);
					continue;
				}
				// exit 처리 방식과 비슷하게 메세지에 get이 있으면 실행되는 부분
				if(strstr(buf, DOWNLOAD) != NULL ){
					// 클라이언트에서 메세지가 [ip] : get (파일명) 형식으로 오기때문에, 마지막 공백을 찾아야함 strrchr 함수 이용
					tf = strrchr(buf,' ');
					sprintf(fname, "%s", ltrim(tf));	// (공백)(파일명)이기 때문에 공백을 제거해서 fname에 저장함
					fname[strlen(fname)-1] = '\0';		// fname의 마지막 개행문자를 NULL값으로 바꿔줌

					// 서버에 다운로드 받으려는 파일명을 명시해줌
					printf("다운로드 받으려는 파일명 : %s\n", fname);
					// 해당 시간을 기록하고 저장
					time_t current_time;		
					time(&current_time);		
					strcpy(t, ctime(&current_time));	
					t[24]='\0';							


					// 접속해오는 클라이언트에게 전송해줄 파일 오픈
					fd = open( fname, O_RDONLY );   
					if(fd == -1)  
						errquit("File open error");  

					// 클라이언트에 데이터 전송
					while( (len=read(fd, buf, BUFSIZE)) != 0 )  
					{  
						write(clisock_list[j], buf, len);  
					}  
					// 전송이 완료됨을 명시
					printf("\n------------전송 완료-----------\n");

					// 해당 클라이언트를 제거함
					removeClient(i);
					// 이후 로그에 시간과 다운로드 받은 파일명을 기록함
					sprintf(msg, "[%s] [ %s downloaded ]",t, fname);
					logwrite(msg);

					continue;

				}
				// get과 마찬가지의 방식으로 처리함
				if(strstr(buf, UPLOAD) != NULL ){
					tf = strrchr(buf,' ');
					sprintf(fname, "%s", ltrim(tf));	// (공백)(파일명)이기 때문에 공백을 제거해서 fname에 저장함
					fname[strlen(fname)-1] = '\0';		// fname의 마지막 개행문자를 NULL값으로 바꿔줌

					printf("업로드 될 파일명 : %s\n", fname);

					time_t current_time;		
					time(&current_time);		
					strcpy(t, ctime(&current_time));	
					t[24]='\0';							

					fd = open(fname, O_WRONLY|O_CREAT|O_TRUNC, 0777);  
					if(fd == -1)  
						errquit("File open error");  
					while( (len=recv(clisock_list[j] , up, BUFSIZE,0)) != 0 )  
					{  
						write(fd, up, len);   
					}  

					printf("파일이 업로드 됨 \n");

					close(fd);  
					sprintf(msg, "[%s] [ %s uploaded ]",t, fname);
					logwrite(msg);

					removeClient(i);
					continue;
				}


				for( j = 0 ; j < num_chat ; j ++)	
					send(clisock_list[j], buf, nbyte, 0);

				buf[strlen(buf)-1]='\0';	
				printf("%s\n", buf);		
				time_t current_time;		
				time(&current_time);		
				strcpy(t, ctime(&current_time));	
				t[24]='\0';							

				sprintf(msg, "[%s] [ user_message %s ]",t, strstr(buf,":"));
				logwrite(msg);
			}
		}
	}

	return 0;
}
// 문자열 내 공백 제거함수
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
// 로그 기록함수
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
// 밑의 함수들은 책에 있는 내용
void addClient(int s, struct sockaddr_in *newcliaddr){
	char buf[20];
	char t[25];
	char msg[MAXLINE+1];

	inet_ntop(AF_INET, &newcliaddr->sin_addr, buf, sizeof(buf));
	printf("new client : %s\n", buf);
	clisock_list[num_chat] = s;
	num_chat++;
	time_t current_time;
	time(&current_time);
	strcpy(t, ctime(&current_time));
	t[24]='\0';
	sprintf(msg, "[%s] [ total_user : %d ] [ user_ip : %s ]",t, num_chat, buf);
	logwrite(msg);


}


void removeClient(int s){
	char msg[MAXLINE+1];
	char t[25];

	close(clisock_list[s]);
	if(s != num_chat-1)
		clisock_list[s] = clisock_list[num_chat-1];
	num_chat--;

	printf("chat user exit. now user num = %d\n", num_chat);
	time_t current_time;
	time(&current_time);
	strcpy(t, ctime(&current_time));
	t[24]='\0';
	sprintf(msg, "[%s] [ total_user : %d ] [ 1 user leave ]",t, num_chat);
}


int getmax(){
	int max = listen_sock;
	int i;
	for( i = 0 ; i < num_chat ; i++ )
		if(clisock_list[i] > max)
			max = clisock_list[i];
	return max;
}


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
	bzero((char *) &servaddr, sizeof(servaddr));
	servaddr.sin_family = AF_INET;
	servaddr.sin_addr.s_addr = htonl(host);
	servaddr.sin_port = htons(port);
	if(bind(sd, (struct sockaddr *)&servaddr, sizeof(servaddr)) < 0 ){
		perror("bind fail"); exit(1);
	}
	time_t current_time;
	time(&current_time);
	strcpy(t, ctime(&current_time));
	t[24]='\0';
	sprintf(msg, "------[Chat server ON At : %s ] [ Use Port : %d ]------",t, port);
	logwrite(msg);

	listen(sd, backlog);
	return sd;
}
