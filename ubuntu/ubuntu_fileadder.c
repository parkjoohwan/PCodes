#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>
#include <string.h>

#define MAXCHAR 256
#define DEFAULTSIZE 512

int itoa(int value, char * str);

int main(int argc, char ** argv){

	int srcfd, dstfd;// 원본 파일 위한 변수, 목적파일 위한 변수
	int i, j, temp;
	int delsrc = 0;// 파일을 삭제 할지 
	int filecount;
	int invalid = 0;
	char * srcname;// 소스 파일의 이름 (–o)
	char * dstname;// 목적 파일의 이름 (-n)
	char * directory;// 디렉토리 경로 (-d)
	char * option[4] = {"-o","-n","-x","-d"};//파일의 옵션 판단
	char * optionvalue[4] = {0};/// 파일 옵션의 값
	char data[MAXCHAR+1];// 소스 파일에서 읽은 데이터 저장 변수
	char name[MAXCHAR+1];//실제 파일을 만들 때 사용하는 변수
	char charcnt[4];// 생성 파일 번호 
	ssize_t nread;
	struct stat srcstat;

        // 옵션 개수 조건 판단 
	if(argc > 9){
		printf("too many predicates.\n");
		exit(0);
	}
	if(argc < 2){
		printf("need predicate.\n");
		exit(0);
	}

	for(i = 1; i < argc; i++){
		invalid = i;
		if(*argv[i] == '-'){//옵션 인자의 첫 번째가 – 인 경우만 검사
			for(j = 0; j < 4; j++){
				if(!strcmp(argv[i],option[j])){// 옵션 인자 값 비교
					optionvalue[j] = argv[i+1];//옵션 값 저장
					if(j == 2){
						optionvalue[j] = argv[i];//-x일 경우
					}
					invalid = 0;
					break;
				}
			}
			if(invalid > 0){//옵션 인자의 유효성 검사
				printf("invlied predicate : [%s]\n", argv[invalid]);
				exit(0);
			}
		}
	}

	//옵션 값 넣기
	if(optionvalue[0] != NULL){
		srcname = optionvalue[0];
		temp = strlen(srcname);
		srcname[temp-3] = '\0';
	}
	if(optionvalue[1] != NULL){
		dstname = optionvalue[1];
	}
	else{
		dstname = srcname;
	}
	if(optionvalue[2] != NULL){//-x인자가 들어 왔을 때 ( 기존 파일 삭제 )
		delsrc = 1;
	}
	if(optionvalue[3] != NULL){
		directory = optionvalue[3];
		strcat(directory, "/");
	}
	else{
		directory = "./";
	}

	// 프로그램에 대한 기본 정보 출력
	printf("delete source file   = %d\n" , delsrc);
	printf("source filename      = %s\n", srcname);
	printf("destination filename = %s\n", dstname);
	printf("directory            = %s\n", directory);

	if(opendir(directory) == NULL){
		mkdir(directory, 0777);
	}

	// 목적 파일 열기
	strcpy(name, directory);
	strcat(name, dstname);
	dstfd = open(name, O_WRONLY | O_CREAT, 0666);

	//원본 파일 열기
	for(i = 0; i < 1000 ; i++){
		itoa(i, charcnt);
		strcpy(name, srcname);
		strcat(name, charcnt);
		if((srcfd = open(name, O_RDONLY)) < 0){
			close(dstfd);
			exit(1);
		}
		// 파일 쓰기
		printf("%s", name);
		while((nread = read(srcfd, data, MAXCHAR)) > 0){
			write(dstfd, data, nread);
		}
		

		close(srcfd);

		if(delsrc == 1){
			unlink(name);
			printf("...deleted");
		}

		printf("...OK\n");
	}
	

	close(dstfd);
	return 0;
}
// 정수를 문자로 바꾸는 함수
int itoa(int value, char * str){
	
	*str = (value / 100) + 48;
	value = value % 100;
	*(str+1) = (value / 10) + 48;
	value = value % 10;
	*(str+2) = value + 48;
	*(str+3) = '\0';

	return 0;
}

