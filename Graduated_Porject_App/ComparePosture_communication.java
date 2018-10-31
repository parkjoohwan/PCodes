package com.example.aict.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import static com.example.aict.myapplication.Camera_capture.filedir;
import static com.example.aict.myapplication.ComparePosture.exercisename;

public class ComparePosture_communication extends AppCompatActivity {
    int number = 0;

    static String result_msg;
    static String result_msg_two;
    static String storedir[] = new String[3];
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compareposture_communication);
        Log.d("자세비교 통신", "호출");

        Intent getintent = getIntent();
        number = getintent.getIntExtra("number", 0);
        Log.d("부분동작 개수_com",Integer.toString(number) );


    }
    protected void onStart(){
        super.onStart();
        try {
            new Thread(){
                public void run() {
                    for(int i =0 ; i<number ; i++){
                        uploadingImg(filedir[i], i);
                        if(exercisename == 1){
                            exercisename++;
                        }else if(exercisename == 5){
                            exercisename++;
                        }else if(exercisename == 7){
                            exercisename++;
                        }else if(exercisename == 9){
                            exercisename++;
                        }else if(exercisename == 12){
                            exercisename++;
                        }else if(exercisename == 15){
                            exercisename++;
                        }
                    }
                    Intent intent = new Intent(ComparePosture_communication.this, ComparePosture_fourth.class);
                    intent.putExtra("exercise_num", number);
                    startActivity(intent);
                    finish();
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //JLabel에 포함된 ImageIcon데이터를 BufferedImage로 변환하여 서버로 전송한다.
    private static void uploadingImg(String name, int number) {
        Log.d("경로 : ", name);
        File sdCard = Environment.getExternalStorageDirectory();
        Socket socket = null;
        DataOutputStream dataOutput = null;
        DataInputStream dataInput = null;
        BufferedOutputStream bufferedOutput = null;
        BufferedInputStream bufferedInput = null;
        FileOutputStream fileOutput = null;
        File img = new File(name);
        byte[] pushmsg;
        byte[] pullmsg = new byte[1024];
        byte[] pullmsg_result = new byte[1024]; // 결과 메세지
        int fsize;
        String push;
        String push_exercise;
        byte[] pushexecise;

        try {
            socket = new Socket("218.150.182.166", 9001);
            /*-------------------------------------------*/
            dataOutput = new DataOutputStream(socket.getOutputStream()); //output 스크림 생성
            push_exercise = String.valueOf(exercisename);
            pushexecise = push_exercise.getBytes("UTF-8");
            dataOutput.write(pushexecise);
            dataOutput.flush();
            /* -----------------------------------------*/
            dataOutput = new DataOutputStream(socket.getOutputStream()); //output 스크림 생성
            fsize = (int) img.length();        // 파일 사이즈를 int 변수에 저장
            push = String.valueOf(fsize);    // 파일 사이즈를 String으로 변환
            pushmsg = push.getBytes("UTF-8");    // String 파일 사이즈를 byte형식으로 변환
            dataOutput.write(pushmsg);        // 수신측에 전송파일 사이즈 전달
            dataOutput.flush();
            System.out.println("파일 사이즈 전송 완료");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e);    //sleep 메소드가 발생시키는 InterruptedException
            }
            byte[] bytes;//이미지 파일을 저장할 바이트 배열 선언
            dataInput = new DataInputStream(new FileInputStream(name));
            bufferedOutput = new BufferedOutputStream(dataOutput);

            bytes = new byte[fsize];    // 파일 크기만큼 배열 사이즈 조정
            dataInput.readFully(bytes);    // 바이트 크기만큼 읽어옴
            bufferedOutput.write(bytes, 0, fsize);    // 바이트에 있는걸 파일 크기만큼 전송
            System.out.println("파일 업로드 완료 ");

            System.out.println("응답 대기 ");
            /*--------------------------------------------*/
            bufferedInput = new BufferedInputStream(socket.getInputStream());
            bufferedInput.read(pullmsg_result);
            String pull_result = new String(pullmsg_result, "UTF-8");
            pull_result = pull_result.replaceAll("//d", ""); // 받은 문자열에서 숫자만 추출
            if(exercisename == 2 || exercisename == 6 || exercisename == 8 || exercisename == 10 || exercisename == 13 || exercisename == 16){
                result_msg_two = pull_result;
            }else{
                result_msg = pull_result;
            }
            System.out.println("받은 결과 메세지 : " + pull_result);
            Log.d("받은 결과 메세지", pull_result);

            /* --------------------------------------------- */
            bufferedInput = new BufferedInputStream(socket.getInputStream());
            bufferedInput.read(pullmsg);    // 파이썬에서 바이트 형식밖에 못보내므로 위에서 선언한 바이트 배열에 읽어옴
            String pull = new String(pullmsg, "UTF-8");
            pull = pull.replaceAll("[^0-9]", ""); // 받은 문자열에서 숫자만 추출
            int rsize = Integer.parseInt(pull);
            System.out.println("받을 파일 크기 : " + rsize / 1024 + "kb");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e);    //sleep 메소드가 발생시키는 InterruptedException
            }
            //byte[] recv = new byte[rsize];
            byte[] recv;
            // 갤러리에서 고른 파일 이름 - 주환이
            String rname = "/storage/emulated/0/exercise/"+"ACIT"+Long.toString(System.currentTimeMillis()) +".jpg";
            storedir[number] = rname;
            // 원래거
            //String rname = sdCard.getAbsolutePath() + "/camtest"+"ACIT"+Long.toString(System.currentTimeMillis()) +".jpg";
            fileOutput = new FileOutputStream(rname, false);
            int i = 0;                     //배열 인덱스 초기화
            recv = new byte[rsize]; //100MB보다 같거나 작으므로 totalSize로 배열크기 다시 생성
            while (i < rsize) {
                recv[i] = (byte) bufferedInput.read();
                i++;      //배열인덱스 이동
            }//while문
            fileOutput.write(recv);
            fileOutput.flush();
            System.out.println("수신 완료");

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedOutput != null)
                    bufferedOutput.close();
                if (bufferedInput != null)
                    bufferedInput.close();
                if (dataInput != null)
                    dataInput.close();
                if (fileOutput != null)
                    fileOutput.close();
                if (dataOutput != null)
                    dataOutput.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }//finally
    }
}
