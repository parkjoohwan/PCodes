package com.example.aict.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Random;


public class LoginActivity extends AppCompatActivity {

   /* private final static int PERMISSIONS_REQUEST_CODE = 100;
    private AppCompatActivity mActivity;*/
   boolean isPermitted = false;
    final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

    BluetoothAdapter mBTAdapter;
    BluetoothManager mBTManager;
    static final int REQUEST_ENABLE_BT = 1;
    static final int REQUEST_ENABLE_DISCOVER = 2;
    static final int PICK_FROM_GALLERY = 3;  // 구별자 - 이미지불러오기
    Uri uri;

    private DBHelper helper;
    private SQLiteDatabase db;

    EditText edit_id;
    EditText edit_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Bluetooth Adapter 참조 객체 얻는 두 가지 방법 ========================//
        // 1. BluetoothManager 통해서
        mBTManager = (BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
        mBTAdapter = mBTManager.getAdapter(); // project 생성시 Minimum SDK 설정에서 API level 18 이상으로 선택해야
        // 2. BluetoothAdapter 클래스의 static method, getDefaultAdapter() 통해서
        //mBTAdapter = BluetoothAdapter.getDefaultAdapter();


        // BT adapter 확인 ===============================//
        // 장치가 블루투스를 지원하지 않는 경우 null 반환
        if(mBTAdapter == null) {
            // 블루투스 지원하지 않기 때문에 블루투스를 이용할 수 없음
            // alert 메세지를 표시하고 사용자 확인 후 종료하도록 함
            // AlertDialog.Builder 이용, set method에 대한 chaining call 가능
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your device does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        } else {
            // 블루투스 이용 가능
            // 스캔하고, 연결하고 등 작업을 할 수 있음

            // 필요한 경우, 블루트스 활성화 ========================================//
            // 블루투스를 지원하지만 현재 비활성화 상태이면, 활성화 상태로 변경해야 함
            // 이는 사용자의 동의를 구하는 다이얼로그와 비슷한 activity 화면이 표시되어 사용자가 활성화 하게 됨
            if(!mBTAdapter.isEnabled()) {
                // 비활성화 상태
                Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
            } else {
                // 활성화 상태
                // 스캔을 하거나 연결을 할 수 있음
            }
        }
        requestRuntimePermission();

        Button serverbutton = (Button)findViewById(R.id.button11);
        serverbutton.setVisibility(View.GONE);

        edit_id = (EditText)findViewById(R.id.editText6);
        edit_pw = (EditText)findViewById(R.id.editText7);


        // SQLiteOpenHelper 클래스의 subclass인 DBHelper 클래스 객체 생성
        helper = new DBHelper(this);
        // DBHelper 객체를 이용하여 DB 생성
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException e) {
            db = helper.getReadableDatabase();
        }
    }
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        // 요청 코드에 따라 처리할 루틴을 구분해줌
        switch(requestCode) {
            case REQUEST_ENABLE_BT:
                if(responseCode == RESULT_OK) {
                    // 사용자가 활성화 상태로 변경하는 것을 허용하였음
                } else if(responseCode == RESULT_CANCELED) {
                    // 사용자가 활성화 상태로 변경하는 것을 허용하지 않음
                    // 블루투스를 사용할 수 없으므로 애플리케이션 종료
                    finish();
                }
                break;
            case REQUEST_ENABLE_DISCOVER:
                if(responseCode == RESULT_CANCELED) {
                    // 사용자가 DISCOVERABLE 허용하지 않음 (다이얼로그 화면에서 거부를 선택한 경우)
                    Toast.makeText(this, "사용자가 discoverable을 허용하지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            case PICK_FROM_GALLERY :
                uri = data.getData();
                try {
                    new Thread(){
                    public void run() {
                        String name = getPath(uri);
                        Log.d("경로 : ", name);
                        uploadingImg(name);    //이미지를 서버에 업로드 + 결과 메세지 받기
                     }
                 }.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }
    }
    // 이미지 경로 가져오기
    public String getPath(Uri uri) {


        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String picturePath = cursor.getString(columnIndex); // returns null
        cursor.close();
        return picturePath;
    }
    private void requestRuntimePermission() {
        //*******************************************************************
        // Runtime permission check
        //*******************************************************************
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)||
                    ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)||
                    ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.CAMERA)||
                    ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            }
        } else {
            // ACCESS_FINE_LOCATION 권한이 있는 것
            isPermitted = true;
        }
        //*********************************************************************
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isPermitted = true;

                } else {
                    finish();
                }
                return;
            }
        }
    }

    public void onClickLogin(View view){
        Cursor cursor;
        cursor = db.rawQuery("SELECT pw, id FROM users WHERE id='"+edit_id.getText().toString()+"';",null);
        if(cursor.getCount() == 0){
            Toast.makeText(this, "아이디와 패스워드를 확인해주세요.", Toast.LENGTH_SHORT).show();
        }
        else{
            String db_pw="";
            String db_id = "";
            while(cursor.moveToNext()){
                db_pw = cursor.getString(0);
                db_id = cursor.getString(1);
            }
            if(db_pw.equals(edit_pw.getText().toString())){
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("user_id",db_id);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        }

    }
    public void onClickSignup(View view){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void onClicktest(View view){

        Intent intent = new Intent(Intent.ACTION_PICK);     //갤러리 호출을 위한 intent
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);  //타입 설정
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_FROM_GALLERY);  //갤러리 데이터 값을 가져오는 실행

    }
    //JLabel에 포함된 ImageIcon데이터를 BufferedImage로 변환하여 서버로 전송한다.
    private static void uploadingImg(String name) {
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
        int fsize;
        String push;

        try {
            socket = new Socket("192.168.1.154", 9001);
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