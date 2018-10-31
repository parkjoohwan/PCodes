package com.example.aict.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Locale;

import static com.example.aict.myapplication.ComparePosture.exercisename;


/**
 * Created by AICT on 2018-03-15.
 */

public class ComparePosture_third extends AppCompatActivity implements TextToSpeech.OnInitListener {

    /** Called when the activity is first created. */
    int number; // 구분동작의 개수
    private TextToSpeech tts_first; // 처음 말할거
    TextToSpeech tts;
    TextToSpeech tts_count;

    int count;  // 진행중인 구분동작 번호

    Intent intent_next;

    String filedir[] = new String[3];   // Camera_capture에서 찍은 사진들의 경로
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compareposture_third);

        Log.d("자세비교3", "호출");
        Intent intent = getIntent();
        number = intent.getExtras().getInt("num");

        tts_first = new TextToSpeech(this, this);

        tts = new TextToSpeech(this, this);

        tts_count = new TextToSpeech(this, this);
        tts_count.setOnUtteranceProgressListener(progressListener);



        Intent intent_get = getIntent();
        count = intent_get.getExtras().getInt("count_camera" ,-1);
        number = intent_get.getExtras().getInt("all_camera", number);
        if(count > 1){
            filedir = intent_get.getStringArrayExtra("array");
        }
        if(count > 0){
            filedir[count-1] = intent_get.getExtras().getString("dir");
        }
        if(count == number){
            intent_next= new Intent(ComparePosture_third.this, ComparePosture_communication.class);
            intent_next.putExtra("number",number);
            intent_next.putExtra("capture",filedir);
        }



        speakOut();

    }
    //음성 재생 상태에 대한 callback을 받을 수 있는 추상 클래스
    private UtteranceProgressListener progressListener = new UtteranceProgressListener() {
        @Override
        public void onStart(String utteranceId) { // 음성이 재생되었을 때

        }

        @Override
        public void onDone(String utteranceId) { // 제공된 텍스트를 모두 음성으로 재생한 경우
            if(count == -1){
                Intent intent = new Intent(ComparePosture_third.this, FaceTracking.class);
                intent.putExtra("count_third", count);
                intent.putExtra("all_third", number);
                finish();
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(ComparePosture_third.this, Camera_capture.class);
                intent.putExtra("count_third", count);
                intent.putExtra("all_third", number);
                if(count > 0 ){
                    intent.putExtra("zxc",filedir);
                }
                finish();
                startActivity(intent);
            }
        }

        @Override
        public void onError(String utteranceId) { // ERROR!

        }
    };
    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts_first != null) {
            tts_first.stop();
            tts_first.shutdown();
        }
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        if (tts_count != null) {
            tts_count.stop();
            tts_count.shutdown();
        }
        super.onDestroy();
    }
    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts_first.setLanguage(Locale.KOREA);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    private void speakOut() {

        if(count == -1){
            if(exercisename == 1){
                tts_count.speak("카메라 조정을 실시합니다. 카메라 조정이 끝날 때까지 제자리에 앉아서 기달려주세요." , TextToSpeech.QUEUE_FLUSH, null,"id1");
            }else if(exercisename == 3){
                tts_count.speak("카메라 조정을 실시합니다. 카메라 조정이 끝날 때까지 제자리에 서서 기달려주세요." , TextToSpeech.QUEUE_FLUSH, null,"id1");
            }else if(exercisename == 4){
                tts_count.speak("카메라 조정을 실시합니다. 카메라 조정이 끝날 때까지 제자리에 앉아서 기달려주세요." , TextToSpeech.QUEUE_FLUSH, null,"id1");
            }else if(exercisename == 5 ){
                tts_count.speak("카메라 조정을 실시합니다. 카메라 조정이 끝날 때까지 제자리에 서서 기달려주세요." , TextToSpeech.QUEUE_FLUSH, null,"id1");
            }else if(exercisename == 7){
                tts_count.speak("카메라 조정을 실시합니다. 카메라 조정이 끝날 때까지 제자리에 앉아서 기달려주세요." , TextToSpeech.QUEUE_FLUSH, null,"id1");
            }else if(exercisename == 9){
                tts_count.speak("카메라 조정을 실시합니다. 카메라 조정이 끝날 때까지 제자리에 앉아서 기달려주세요." , TextToSpeech.QUEUE_FLUSH, null,"id1");
            }else if(exercisename == 11){
                tts_count.speak("카메라 조정을 실시합니다. 카메라 조정이 끝날 때까지 제자리에 앉아서 기달려주세요." , TextToSpeech.QUEUE_FLUSH, null,"id1");
            }else if(exercisename == 12 ){
                tts_count.speak("카메라 조정을 실시합니다. 카메라 조정이 끝날 때까지 제자리에 앉아서 기달려주세요." , TextToSpeech.QUEUE_FLUSH, null,"id1");
            }else if(exercisename == 14){
                tts_count.speak("카메라 조정을 실시합니다. 카메라 조정이 끝날 때까지 제자리에 앉아서 기달려주세요." , TextToSpeech.QUEUE_FLUSH, null,"id1");
            }else if(exercisename == 15){
                tts_count.speak("카메라 조정을 실시합니다. 카메라 조정이 끝날 때까지 제자리에 앉아서 기달려주세요." , TextToSpeech.QUEUE_FLUSH, null,"id1");
            }
            intent_next = null;
        }
        // 첫 번쨰 구분 동작
        else if(count == 0 ){
            // 정해진 구분동작이 끝나면 다음 activity 실행
            if( count == number){
                startActivity(intent_next);
                finish();
            }
            else{
                tts_first.speak("카메라 조정이 완료되었습니다. 이제 자세 비교를 시작하겠습니다  해당 운동은 " + Integer.toString(number) +"개의 구분 동작으로 구성되어있습니다   " , TextToSpeech.QUEUE_FLUSH, null,"id1");
                tts.speak(Integer.toString(count+1) + "번 구분 동작을 취해주세요  5초 뒤에 촬영이 시작됩니다" , TextToSpeech.QUEUE_FLUSH, null,"id2");
                tts_count.setSpeechRate(0.2f);
                tts_count.speak("5 4 3 2 1" , TextToSpeech.QUEUE_FLUSH, null,"id3");
                intent_next = null;
            }
        // 두 번째 구분동작
        }
        else if( count == 1){
            if( count == number){
                startActivity(intent_next);
                finish();
            }
            else{
                tts.speak(Integer.toString(count+1) + "번 구분 동작을 취해주세요  5초 뒤에 촬영이 시작됩니다" , TextToSpeech.QUEUE_FLUSH, null,"id2");
                tts_count.setSpeechRate(0.2f);
                tts_count.speak("5 4 3 2 1" , TextToSpeech.QUEUE_FLUSH, null,"id3");
                intent_next = null;
            }
        // 세 번째 구분 동작
        }
        else if( count == 2){
            if( count == number){
                startActivity(intent_next);
                finish();
            }
            else{
                tts.speak(Integer.toString(count+1) + "번 구분 동작을 취해주세요  5초 뒤에 촬영이 시작됩니다" , TextToSpeech.QUEUE_FLUSH, null,"id2");
                tts_count.setSpeechRate(0.2f);
                tts_count.speak("5 4 3 2 1" , TextToSpeech.QUEUE_FLUSH, null,"id3");
                intent_next = null;
            }
        // 네 번째 구분 동작
        }
        else if( count == 3){
            if( count == number){
                startActivity(intent_next);
                finish();
            }
            else{
                tts.speak(Integer.toString(count+1) + "번 구분 동작을 취해주세요  5초 뒤에 촬영이 시작됩니다" , TextToSpeech.QUEUE_FLUSH, null,"id2");
                tts_count.setSpeechRate(0.2f);
                tts_count.speak("5 4 3 2 1" , TextToSpeech.QUEUE_FLUSH, null,"id3");
                intent_next = null;
            }

        }
    }
}
