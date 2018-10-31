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
	int i, j
	int filesize = DEFAULTSIZE; // 파일의 크기 초기엔 디폴트 값
	int filecount;// 절단 파일의 개수 카운트
	int invalid = 0; // 주어진 옵션 판단
	char * srcname;// 소스 파일의 이름 (–o)
	char * dstname;// 목적 파일의 이름 (-n)
	char * directory;// 디렉토리 경로 (-d)
	char * option[4] = {"-o","-n","-s","-d"};//파일의 옵션 판단
	char * optionvalue[4] = {0};// 파일 옵션의 값
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

	for(i = 1; i < argc; i+=2){
		invalid = i;
		for(j = 0; j < 4; j++){
			if(!strcmp(argv[i],option[j])){// 옵션 인자 값 비교
				optionvalue[j] = argv[i+1];// 옵션 값 채워넣기
				invalid = 0;
				break;
			}
		}
		if(invalid > 0){// 옵션이 유효한지 판단
			printf("invlied predicate : [%s]\n", argv[invalid]);
			exit(0);
		}
	}

	// 옵션 값 넣기
	if(optionvalue[0] != NULL){
		srcname = optionvalue[0];
	}
	if(optionvalue[1] != NULL){
		dstname = optionvalue[1];
	}
	else{
		dstname = srcname;
	}
	if(optionvalue[2] != NULL){
		filesize = atoi(optionvalue[2]);
	}
	if(optionvalue[3] != NULL){
		directory = optionvalue[3];
		strcat(directory, "/");
	}
	else{
		directory = "./";
	}

	// 프로그램에 대한 기본 정보 출력
	printf("file size            = %dkb\n", filesize);
	printf("source filename      = %s\n", srcname);
	printf("destination filename = %s\n", dstname);
	printf("directory            = %s\n", directory);

	// 원본 파일 개방
	if((srcfd = open(srcname, O_RDONLY)) < 0){
		printf("file open error!\n");
		exit(1);
	}

	// 파일로부터 정보 얻어오기
	if(fstat(srcfd, &srcstat) == -1){
		printf("fstat error!\n");
		close(srcfd);
		exit(1);
	}



	//원본 파일의 크기가 디폴트 값보다 작으면 메시지 출력 후 종료하기
	if(srcstat.st_size < filesize*1024){
		printf("file size is small than %d Kb\n", filesize);
	}

	// 생성 될 절단 파일 개수 구하기
	filecount = (srcstat.st_size / (filesize*1024)) + 1;
	printf("%d / %d \n", srcstat.st_size, filesize*1024);

	// 생성 될 절단 파일의 개수가 1000개를 넘어가면 메시지 출력 후 종료
	if(filecount > 999){
		printf("too many file create.(more then 999)\n");
		printf("input size more than %d\n", filesize);
		close(srcfd);
		exit(1);
	}
	
	if(opendir(directory) == NULL){
		mkdir(directory, 0777);
	}

	// 파일 생성하기
	for(i = 0 ; i < filecount ; i++){
		strcpy(name, directory);
		strcat(name, dstname);
		itoa(i, charcnt);
		strcat(name, charcnt);

		dstfd = open(name, O_WRONLY | O_CREAT, srcstat.st_mode);

		printf("%s", name);
		for(j = 0; j < filesize*1024/MAXCHAR ; j++){
			if((nread = read(srcfd, data, MAXCHAR)) <= 0)
				break;
			write(dstfd, data, nread);
		}
		printf("...OK\n");	
	
		close(dstfd);

	}
	
	close(srcfd);
	return 0;
}

int itoa(int value, char * str){
	
	*str = (value / 100) + 48;
	value = value % 100;
	*(str+1) = (value / 10) + 48;
	value = value % 10;
	*(str+2) = value + 48;
	*(str+3) = '\0';

	return 0;
}

