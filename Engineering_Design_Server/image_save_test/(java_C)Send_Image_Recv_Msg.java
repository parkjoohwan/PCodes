package test;

import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;



public class Testimagefile {
    public static void main(String[] args){
        String filename = "C:\\Users\\AICT\\Desktop\\새 폴더\\test\\src\\test3.jpg";

        try {
            uploadingImg(filename);    //JLabel에 설정된 이미지를 서버에 업로드 한다

        } catch (Exception e) {
                e.printStackTrace();
        }
    }

//JLabel에 포함된 ImageIcon데이터를 BufferedImage로 변환하여 서버로 전송한다.
    private static void uploadingImg(String name){
        Socket socket=null;
        DataOutputStream dataOutput = null;
        DataInputStream dataInput = null;
		    BufferedOutputStream bufferedOutput = null;
		    BufferedInputStream bufferedInput = null;
        File img = new File(name);
        byte[] pushmsg;
        byte[] pullmsg=new byte[1024];
        int fsize;
        String push;

        try {
            socket=new Socket("218.150.181.230",9001);
            dataOutput = new DataOutputStream(socket.getOutputStream()); //output 스크림 생성
            fsize = (int)img.length(); 		// 파일 사이즈를 int 변수에 저장
            push = String.valueOf(fsize); 	// 파일 사이즈를 String으로 변환
            pushmsg = push.getBytes("UTF-8");	// String 파일 사이즈를 byte형식으로 변환
            dataOutput.write(pushmsg);  		// 수신측에 전송파일 사이즈 전달
            dataOutput.flush();
            System.out.println("파일 사이즈 전송 완료");

            byte[] bytes;//이미지 파일을 저장할 바이트 배열 선언
            dataInput = new DataInputStream(new FileInputStream(name));
            bufferedOutput = new BufferedOutputStream(dataOutput);

            bytes = new byte[fsize];	// 파일 크기만큼 배열 사이즈 조정
            dataInput.readFully(bytes);	// 바이트 크기만큼 읽어옴
            bufferedOutput.write(bytes, 0, fsize);	// 바이트에 있는걸 파일 크기만큼 전송
//            bufferedOutput.flush();
            System.out.println("파일 업로드 완료 ");

            System.out.println("메세지 대기 ");
            bufferedInput = new BufferedInputStream(socket.getInputStream());
            bufferedInput.read(pullmsg);	// 파이썬에서 바이트 형식밖에 못보내므로 위에서 선언한 바이트 배열에 읽어옴
            String pull = new String(pullmsg,"UTF-8");	// 읽어온 바이트 배열을 utf-8형식으로 디코딩
            System.out.println("받은 메세지 : "+ pull); 		// 해당 메세지 확인


            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
           } finally {
            try {
             if (bufferedOutput != null)
            	 bufferedOutput.close();
             if (bufferedOutput != null)
            	 bufferedInput.close();
             if (dataInput != null)
            	 dataInput.close();
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
