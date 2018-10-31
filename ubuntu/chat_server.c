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

char *EXIT_STRING = "exit";	//Ŭ���̾�Ʈ ���� ���ڿ�
char *START_STRING = "Connected to chat_server \n";	//Ŭ���̾�Ʈ ȯ�� �޼���

int maxfdp1;				//�ִ� ���� ��ȣ
int num_chat = 0;			//ä�� ������ ��
int clisock_list[MAX_SOCK];	//ä�� ������ ���� ��ȣ ����Ʈ
int listen_sock;			//������ ���� ����


void logwrite(char* str);	//�α� �ۼ� �Լ�
void addClient(int s, struct sockaddr_in *newcliaddr);	//���ο� ������ ó��
int getmax();											//�ִ� ���Ϲ�ȣ ã��
void removeClient(int s);								//ä�� Ż�� ó���Լ�
int tcp_listen(int host, int port, int backlog);		//���� ���� �� Listen
void errquit(char *mesg){ perror(mesg); exit(1);}		//���� ����

int main( int argc, char* argv[] ){

	struct sockaddr_in cliaddr;
	char buf[MAXLINE+1];
	char t[25];				//time ����� ����
	char id[20] = "";		//id ����� ����
	char msg[MAXLINE+1];	//�α׿� ������ �޼���
	int i, j, k, nbyte, accp_sock, addrlen = sizeof(struct sockaddr_in);
	char *idf;				//id ���п� ������
	fd_set read_fds;

	if(argc != 2){
		printf("usage : %s port\n", argv[0]);
		exit(0);
	}

	// ���� ���� �� Listen
	listen_sock = tcp_listen(INADDR_ANY, atoi(argv[1]), 5);
	while(1){

	FD_ZERO(&read_fds);
	FD_SET(listen_sock, &read_fds);
	for( i = 0 ; i < num_chat ; i++ ){
		FD_SET(clisock_list[i], &read_fds);
	}
	maxfdp1 = getmax() +1;	//�ִ� ���Ϲ�ȣ ���
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

	//Ŭ���̾�Ʈ�� ���� �޽����� ��� Ŭ���̾�Ʈ���� ����
	for( i = 0 ; i < num_chat ; i++ ){
		if(FD_ISSET(clisock_list[i], &read_fds)){
			nbyte = recv(clisock_list[i], buf, MAXLINE, 0);
			// nbyte�� ������ Ŭ���̾�Ʈ ����
			if(nbyte <= 0){
				removeClient(i);	
				continue;
			}
			buf[nbyte] = 0;
			// EXIT_STRING�� ������ Ŭ���̾�Ʈ ����
			if(strstr(buf, EXIT_STRING) != NULL ){
				removeClient(i);
				continue;
			}
			// ��� ä�� �����ڿ��� �޼��� �߼� 
			for( j = 0 ; j < num_chat ; j ++)	
				send(clisock_list[j], buf, nbyte, 0);
			// �α� ����� [�ð�] [���̵�] [�޼���] [�޼�������]
			buf[strlen(buf)-1]='\0';	// �о�� �޼����� ���๮�ڸ� \0���� ����
			printf("%s\n", buf);		// �ش� ���� ������ print
			time_t current_time;		// �ð� ����� ���� ���� ����
			time(&current_time);		// time ����
			strcpy(t, ctime(&current_time));	// ���ڿ� ���·� ����
			t[24]='\0';							// ������ ���๮�ڸ� \0���� ����
			idf = strchr(buf,']');				// id ������ ���� �ּ� ���
			for( k = 0 ; k < idf-buf-1 ; k ++ )	// idf-buf�ϸ� ']'������ �ּҰ� ���°���� ����, �׷� �װͿ��� -1���ϸ� ���̵��� ������ ����
				id[k] = buf[1+k];
			// msg ���ڿ��� [�ð�] [���̵�] [�޼���] [�޼�������] ���·� ���
			sprintf(msg, "[%s] [ user_id : %s ] [ user_message %s ] [ user_message_num : %d ]",t, id, strstr(buf,":"), strchr(buf,'\0')-strchr(buf,':')-2);
			// �α� ���
			logwrite(msg);
			}
		}
	}

	return 0;
}
// �α� ��� �Լ�
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
// ���ο� ä�� ������ ó��
void addClient(int s, struct sockaddr_in *newcliaddr){
	char buf[20];
	char t[25];
	char msg[MAXLINE+1];

	inet_ntop(AF_INET, &newcliaddr->sin_addr, buf, sizeof(buf));
	printf("new client : %s\n", buf);
	clisock_list[num_chat] = s;
	num_chat++;
	// [�ð�] [�� ����] [���� ������] ���·� �αױ��
	time_t current_time;
	time(&current_time);
	strcpy(t, ctime(&current_time));
	t[24]='\0';
	sprintf(msg, "[%s] [ total_user : %d ] [ user_ip : %s ]",t, num_chat, buf);
	logwrite(msg);

	
}
// ä�� Ż�� ó��
void removeClient(int s){
	char msg[MAXLINE+1];
	char t[25];

	close(clisock_list[s]);
	if(s != num_chat-1)
		clisock_list[s] = clisock_list[num_chat-1];
	num_chat--;

	printf("chat user exit. now user num = %d\n", num_chat);
	// [�ð�] [�� ����] [�޼���] ���·� �αױ��
	time_t current_time;
	time(&current_time);
	strcpy(t, ctime(&current_time));
	t[24]='\0';
	sprintf(msg, "[%s] [ total_user : %d ] [ 1 user leave ]",t, num_chat);
}
// �ִ� ���� ��ȣ ã��
int getmax(){
	int max = listen_sock;
	int i;
	for( i = 0 ; i < num_chat ; i++ )
		if(clisock_list[i] > max)
			max = clisock_list[i];
	return max;
}

// listen ���� ���� �� listen
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
	// servaddr ����ü�� ���� ����
	bzero((char *) &servaddr, sizeof(servaddr));
	servaddr.sin_family = AF_INET;
	servaddr.sin_addr.s_addr = htonl(host);
	servaddr.sin_port = htons(port);
	if(bind(sd, (struct sockaddr *)&servaddr, sizeof(servaddr)) < 0 ){
		perror("bind fail"); exit(1);
	}
	// ------[ ä�� ���� ���� �ð� ] [ ��� ��Ʈ ]------ ���·� �α� ���
	time_t current_time;
	time(&current_time);
	strcpy(t, ctime(&current_time));
	t[24]='\0';
	sprintf(msg, "------[Chat server ON At : %s ] [ Use Port : %d ]------",t, port);
	logwrite(msg);

	// Ŭ���̾�Ʈ�κ��� ���� ��û�� ��ٸ�
	listen(sd, backlog);
	return sd;
}
