package com.example.aict.myapplication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import ketai.net.bluetooth.*;
import ketai.ui.*;
import ketai.net.*;
import ketai.camera.*;
import ketai.cv.facedetector.*;
import processing.core.PApplet;

import android.os.Environment;
import android.bluetooth.BluetoothAdapter;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static com.example.aict.myapplication.ComparePosture.exercisename;


//__End of imports__//

public class Sketch extends PApplet {

    BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    KetaiBluetooth bt;

    KetaiSimpleFace[] faces;
    String directory;

    KetaiCamera cam;

    int posX,posY,i,info;
    int pev_posX,pev_posY,pev_sec;
    String cninfo = "";
    boolean send_flag=true;

    static int center = 0;
    Context context_this;

    int center_count = 0;

    public static double x,y;

    public static int getSw(){
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getSh(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public Sketch(Context context){
        context_this = context;
    }
    //**To start BT when app is launched**//
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bt = new KetaiBluetooth(this);


    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        bt.onActivityResult(requestCode, resultCode, data);
        //data.putExtra("value", center);
    }
//__BT launched__//

    //** To get data from blue tooth**/
    void onBluetoothDataEvent(String who, byte[] data)
    {
        info = (data[0] & 0xFF) ;
    }
//__data received_//

    //**To get connection status**//
    String getBluetoothInformation()
    {
        String btInfo = "Connected to :";

        ArrayList<String> devices = bt.getConnectedDeviceNames();
        for (String device: devices)
        {
            btInfo+= device+"\n";
        }

        return btInfo;
    }
//--connection status received_//


    //**To select bluetooth device if needed**// (not required for our program
    void onKetaiListSelection(KetaiList klist)
    {
        String selection = klist.getSelection();
        bt.connectToDeviceByName(selection);
        //dispose of list for now
        klist = null;
    }
//__End of selection__//


    public void setup() {
        bt.start(); //start listening for BT connections
        bt.getPairedDeviceNames();
        bt.connectToDeviceByName("HC-06"); //Connect to our HC-06 bluetooth module

        delay(5000);
        byte[] data = {'6'}; bt.broadcast(data); println("FIRST");
        stroke(0, 255, 0);
        strokeWeight(8);
        noFill();
        textSize(38);
//        rectMode(CENTER);
        orientation(LANDSCAPE);
        imageMode(CENTER);
        x=getSw()/2;
        y=getSh()/2;
        cam = new KetaiCamera(this, (int)x, (int)y, 20);
        directory = new String(Environment.getExternalStorageDirectory().getAbsolutePath());

        cam.setCameraID(0);
        cam.setSaveDirectory(directory);
        cam.setPhotoSize((int)x,(int)y);
        cam.autoSettings();


    }

    public void draw() {
        if (cam != null)
        {
            if (!cam.isStarted())
                cam.start();



            image(cam, width/2, height/2, width, height); //display CAM on phone screen
            cam.loadPixels();

            faces = KetaiFaceDetector.findFaces(cam, 1);    //detect faces

            if(faces.length!=0) //if face found
            {
                println("Faces found: " + faces.length); //notify the face detection on console
                pev_posX=posX;
                posX = (int)(map(faces[0].location.x,20,(int)x-20,1,100));
                pev_posY=posY;
                posY = (int)(map(faces[0].location.y,20,(int)y-20,101,201));
                Log.d("FaceLocaation : ", Float.toString(faces[0].location.x)+", "+Float.toString(faces[0].location.y));
                Log.d("PosX, Y : ", posX+", "+ posY );

                if(posX<=100 && posY>=101)
                {
                    fill(0x2C1BF5);
                    textAlign(LEFT);
                    text("X position(1-100): " + posX,(width/12),height/12);
                    text("Y position(101-201): " + posY,(width/12),height/8);
                }
            }


            for ( i=0; i < faces.length; i++) // Box the faces and print its location
            {
                noFill();
                Log.d("width", Integer.toString(width));
                Log.d("widthphone", Integer.toString((int)x));
                rect((faces[i].location.x)*width/((int)x) + (((faces[i].distance)*width/(int)(x)) - ((float)(3*faces[i].distance)*width/(int)(x)))/2,
                        (faces[i].location.y)*height/((int)y) + (((faces[i].distance)*width/(int)(x)) - ((float)(4*faces[i].distance)*width/(int)(x)))/2,
                        (float)(2*faces[i].distance)*width/(int)(x),
                        (3*faces[i].distance)*height/((int)y));

            }

            // 오른쪽 중앙에서 운동하는 것 : 팔굽혀펴기, 플랭크, 마운틴, 버드독
            rect(835*width/(int)x,288*height/(int)y, 130*width/(int)x, 68*height/(int)y);
            // 서서 하는 운동 : 스쿼트, 런지
            rect(580*width/(int)x,82*height/(int)y, 130*width/(int)x, 68*height/(int)y);
            // 왼쪽 중앙에서 하는 운동 : 레그레이즈, 브릿지, 사이드 힙
            rect(325*width/(int)x,288*height/(int)y, 130*width/(int)x, 68*height/(int)y);
            // 벤치딥스
            rect(455*width/(int)x,152*height/(int)y, 130*width/(int)x, 68*height/(int)y);
        }

        textfun();

        if (send_flag==true && faces.length!=0 && pev_sec!=second()) //we can send data
        {
            // 오른쪽 중앙에서 운동하는 것 : 팔굽혀펴기, 플랭크, 마운틴, 버드독
            if(exercisename == 1 || exercisename == 4 || exercisename == 9 || exercisename == 15){
                pev_sec=second();
                Log.v("tat",Integer.toString(posX)+ "\t" + Integer.toString(posY));
                if (pev_posX==posX)
                {
                    if (posX<65)
                    {byte[] data = {'1'}; bt.broadcast(data); println("LEFT");}

                    if (posX>75)
                    {byte[] data = {'2'}; bt.broadcast(data); println("RIGHT");}
                }
                delay(500);
                if (pev_posY==posY)
                {
                    if (posY<140)
                    {byte[] data = {'3'}; bt.broadcast(data); println("UP");}

                    if (posY>150)
                    {byte[] data = {'4'}; bt.broadcast(data); println("DOWN");}
                }

                if (posX>=65 && posX<=75 && posY>=140 &&posY<=150)
                {   byte[] data = {'5'}; bt.broadcast(data); println("No Change");
                    center_count++;
                    if(center_count == 4){
                        ((Activity)context_this).finish();
                    }

                }

                send_flag=false;
            }
            // 서서 하는 운동 : 스쿼트, 런지
            else if(exercisename == 3 || exercisename == 5){
                pev_sec=second();
                Log.v("tat",Integer.toString(posX)+ "\t" + Integer.toString(posY));
                if (pev_posX==posX)
                {
                    if (posX<45)
                    {byte[] data = {'1'}; bt.broadcast(data); println("LEFT");}

                    if (posX>55)
                    {byte[] data = {'2'}; bt.broadcast(data); println("RIGHT");}
                }
                delay(500);
                if (pev_posY==posY)
                {
                    if (posY<110)
                    {byte[] data = {'3'}; bt.broadcast(data); println("UP");}

                    if (posY>120)
                    {byte[] data = {'4'}; bt.broadcast(data); println("DOWN");}
                }

                if (posX>=45 && posX<=55 && posY>=110 &&posY<=120)
                {   byte[] data = {'5'}; bt.broadcast(data); println("No Change");
                    center_count++;
                    if(center_count == 4){
                        ((Activity)context_this).finish();
                    }
                }

                send_flag=false;
            }
            // 왼쪽 중앙에서 하는 운동 : 레그레이즈, 브릿지, 사이드 힙
            else if(exercisename == 7 || exercisename == 11 || exercisename == 14){
                pev_sec=second();
                Log.v("tat",Integer.toString(posX)+ "\t" + Integer.toString(posY));
                if (pev_posX==posX)
                {
                    if (posX<25)
                    {byte[] data = {'1'}; bt.broadcast(data); println("LEFT");}

                    if (posX>35)
                    {byte[] data = {'2'}; bt.broadcast(data); println("RIGHT");}
                }
                delay(500);
                if (pev_posY==posY)
                {
                    if (posY<140)
                    {byte[] data = {'3'}; bt.broadcast(data); println("UP");}

                    if (posY>150)
                    {byte[] data = {'4'}; bt.broadcast(data); println("DOWN");}
                }

                if (posX>=25 && posX<=35 && posY>=140 &&posY<=150)
                {   byte[] data = {'5'}; bt.broadcast(data); println("No Change");
                    center_count++;
                    if(center_count == 4){
                        ((Activity)context_this).finish();
                    }
                }

                send_flag=false;
            }
            // 가운데 좀 위에 : 벤치딥스
            else if(exercisename == 12){
                pev_sec=second();
                Log.v("tat",Integer.toString(posX)+ "\t" + Integer.toString(posY));
                if (pev_posX==posX)
                {
                    if (posX<35)
                    {byte[] data = {'1'}; bt.broadcast(data); println("LEFT");}

                    if (posX>45)
                    {byte[] data = {'2'}; bt.broadcast(data); println("RIGHT");}
                }
                delay(500);
                if (pev_posY==posY)
                {
                    if (posY<120)
                    {byte[] data = {'3'}; bt.broadcast(data); println("UP");}

                    if (posY>130)
                    {byte[] data = {'4'}; bt.broadcast(data); println("DOWN");}
                }

                if (posX>35 && posX<45 && posY>120 &&posY<130)
                {   byte[] data = {'5'}; bt.broadcast(data); println("No Change");
                    center_count++;
                    if(center_count == 4){
                        ((Activity)context_this).finish();
                    }
                }

                send_flag=false;
            }
        }

        if (pev_sec!=second())
            send_flag=true;
    }

    public void onCameraPreviewEvent(){
        cam.read();
    }

    void onSavePhotoEvent( String filename) {
        cam.addToMediaLibrary(filename);
        textAlign(CENTER);
    }
    /*
        public void mousePressed(){
            cam.savePhoto("CD_selfie.jpg");
            text("Photo captured",width/2,height/2);
            delay(2000);
        }
        */
    void textfun()
    {
        textSize(30);
        textAlign(CENTER);
        fill(255);
        cninfo = getBluetoothInformation();    //get connection information status
//        text(cninfo,width/2,height-height/1.03);
        noFill();
    }

//    private void speakOut() {
//        tts_first.speak("자세촬영 전 카메라 조정을 실시합니다. 원할한 카메라 조정을 위해 카메라를 따라 천천히 준비자세를 취해주세요. " , TextToSpeech.QUEUE_FLUSH, null,"id1");
//    }
}