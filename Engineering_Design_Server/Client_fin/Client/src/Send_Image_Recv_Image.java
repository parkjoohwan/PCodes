import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;



public class Send_Image_Recv_Image {
    public static void main(String[] args){
        String filename = "C:\\Users\\AICT\\Desktop\\Client_fin\\Client\\src\\test1.jpg";

        try {
            uploadingImg(filename);    //�̹����� ������ ���ε� + ��� �޼��� �ޱ�

        } catch (Exception e) {
                e.printStackTrace();
        }
    }

//JLabel�� ���Ե� ImageIcon�����͸� BufferedImage�� ��ȯ�Ͽ� ������ �����Ѵ�.
    private static void uploadingImg(String name){
        Socket socket=null;
        DataOutputStream dataOutput = null;
        DataInputStream dataInput = null;
		    BufferedOutputStream bufferedOutput = null;
		    BufferedInputStream bufferedInput = null;
		    FileOutputStream fileOutput = null;
        File img = new File(name);
        byte[] pushmsg;
        byte[] pullmsg=new byte[1024];
        int fsize;
        String push;

        try {
            socket=new Socket("218.150.181.230",9001);
            dataOutput = new DataOutputStream(socket.getOutputStream()); //output ��ũ�� ����
            fsize = (int)img.length(); 		// ���� ����� int ������ ����
            push = String.valueOf(fsize); 	// ���� ����� String���� ��ȯ
            pushmsg = push.getBytes("UTF-8");	// String ���� ����� byte�������� ��ȯ
            dataOutput.write(pushmsg);  		// �������� �������� ������ ����
            dataOutput.flush();
            System.out.println("���� ������ ���� �Ϸ�");

            byte[] bytes;//�̹��� ������ ������ ����Ʈ �迭 ����
            dataInput = new DataInputStream(new FileInputStream(name));
            bufferedOutput = new BufferedOutputStream(dataOutput);

            bytes = new byte[fsize];	// ���� ũ�⸸ŭ �迭 ������ ����
            dataInput.readFully(bytes);	// ����Ʈ ũ�⸸ŭ �о��
            bufferedOutput.write(bytes, 0, fsize);	// ����Ʈ�� �ִ°� ���� ũ�⸸ŭ ����
//            bufferedOutput.flush();
            System.out.println("���� ���ε� �Ϸ� ");

            System.out.println("���� ��� ");
            bufferedInput = new BufferedInputStream(socket.getInputStream());
            bufferedInput.read(pullmsg);	// ���̽㿡�� ����Ʈ ���Ĺۿ� �������Ƿ� ������ ������ ����Ʈ �迭�� �о��
            String pull = new String(pullmsg,"UTF-8");
            pull = pull.replaceAll("[^0-9]", ""); // ���� ���ڿ����� ���ڸ� ����
            int rsize = Integer.parseInt(pull);
            System.out.println("���� ���� ũ�� : " + rsize/1024 + "kb");

            byte[] recv = new byte[rsize];
            String rname = "C:\\Users\\AICT\\Desktop\\Client_fin\\Client\\src\\recv.jpg";
            fileOutput = new FileOutputStream(rname, false);
//            bufferedInput.read(recv, 0, rsize);
            int i = 0;                     //�迭 �ε��� �ʱ�ȭ
            recv = new byte[rsize]; //100MB���� ���ų� �����Ƿ� totalSize�� �迭ũ�� �ٽ� ����
            while (i < rsize) {
             recv[i] = (byte) bufferedInput.read();
             i++;      //�迭�ε��� �̵�
            }//while��
            fileOutput.write(recv);
            fileOutput.flush();
            System.out.println("���� �Ϸ�");

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