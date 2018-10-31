package com.example.aict.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by AICT on 2018-03-15.
 */

public class ComparePosture_second extends AppCompatActivity  {
    int number;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compareposture_second);

        number = getIntent().getExtras().getInt("num");
        Log.d("자세비교2", "호출");

        TextView textView_title = (TextView)findViewById(R.id.textView_title);
        TextView textView_info = (TextView)findViewById(R.id.textView_info);
        textView_title.setText("\n< 핸드폰 거치 안내 >");
        textView_info.setText("\n\n 아래의 사진과 같이 핸드폰을 꽂고\n 시작하기 버튼을 누른 뒤 \n 음성안내에 따라주세요.\n\n");
    }
    public void onClickPostureStart(View view){
        Intent intent = new Intent(this, FaceTracking.class);
        intent.putExtra("num", number);
        startActivity(intent);
        finish();
    }
}
