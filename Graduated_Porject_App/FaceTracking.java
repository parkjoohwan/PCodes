package com.example.aict.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import processing.android.CompatUtils;
import processing.android.PFragment;
import processing.core.PApplet;

import static com.example.aict.myapplication.Sketch.center;

public class FaceTracking extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private PApplet sketch;
    boolean isPermitted_camera = false;   // 위치 허가 유무 판별
    final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE = 1; // 구별자
    BluetoothAdapter mBTAdapter;
    BluetoothManager mBTManager;
    static final int REQUEST_ENABLE_BT = 1;         // 블루투스 사용 허용을 누를시
    static final int REQUEST_ENABLE_DISCOVER = 2;  // 블루투스 사용 거절을 누를시

//    int count;
//    int number;
    private TextToSpeech tts;
//    int count;
    int exercise_count;

    String filedir[] = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Log.d("자세비교얼굴인식", "호출");
//        count = intent.getIntExtra("count_third", -1);
//        number = intent.getIntExtra("all_third",0);
//        count++;
        exercise_count = intent.getIntExtra("num", 0);
//        count = 1;
//        count = intent.getIntExtra("count", 1);
        tts = new TextToSpeech(this, this);
        tts.setOnUtteranceProgressListener(progressListener);

        FrameLayout frame = new FrameLayout(this);
        frame.setId(CompatUtils.getUniqueViewId());
        setContentView(frame, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //sketch = new Sketch(this, count);
        sketch = new Sketch(this);
        PFragment fragment = new PFragment(sketch);
        fragment.setView(frame, this);

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        Intent intent_put = new Intent(FaceTracking.this, Camera_capture.class);
        //intent_put.putExtra("count", count );
        intent_put.putExtra("exercise_count", exercise_count);

        startActivity(intent_put);
    }


    @Override
    public void onNewIntent(Intent intent){
        if(sketch != null){
            sketch.onNewIntent(intent);
//            int center = intent.getIntExtra("value", 0);
//            Toast.makeText(this, Integer.toString(center), Toast.LENGTH_SHORT).show();
//            if(center == 0){
//                sketch.onNewIntent(intent);
//            }
//            else if(center == -1){
//                finish();
//            }
        }
    }

    //음성 재생 상태에 대한 callback을 받을 수 있는 추상 클래스
    private UtteranceProgressListener progressListener = new UtteranceProgressListener() {
        @Override
        public void onStart(String utteranceId) { // 음성이 재생되었을 때

        }

        @Override
        public void onDone(String utteranceId) { // 제공된 텍스트를 모두 음성으로 재생한 경우

        }

        @Override
        public void onError(String utteranceId) { // ERROR!

        }
    };
    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.KOREA);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
//                if(count == 1 || count == 2){
//                    speakOut();
//                }

                speakOut();


            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }
    private void speakOut() {
        tts.speak("자세촬영 전 카메라 조정을 실시합니다. 원할한 카메라 조정을 위해 카메라를 따라 천천히 준비자세를 취해주세요. " , TextToSpeech.QUEUE_FLUSH, null,"id1");

//        else if(count == 2){
//            tts.speak("두 번째 자세촬영 전 카메라 조정을 실시합니다. 준비자세를 취하고 카메라를 바라봐주세요. " , TextToSpeech.QUEUE_FLUSH, null,"id2");
//        }

    }

}

