package com.example.aict.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.aict.myapplication.ComparePosture.exercisename;

public class ComparePosture_explain extends AppCompatActivity {
    int exercisenum;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_compareposture_explain);

        Intent getintent = getIntent();
        exercisenum = getintent.getIntExtra("num",0);

        TextView textView = (TextView)findViewById(R.id.textView28);
        ImageView imageView = (ImageView)findViewById(R.id.imageView5);
        imageView.setImageResource(0);

        textView.setText("해당 운동은 구분 동작 1개로 이루어져 있습니다."  );
        if(exercisename == 3){
            imageView.setImageResource(R.drawable.trainer_squat);
        }else if(exercisename == 4){
            imageView.setImageResource(R.drawable.trainer_plank);
        }else if(exercisename == 11){
            imageView.setImageResource(R.drawable.trainer_brigh);
        }else if(exercisename == 14){
            imageView.setImageResource(R.drawable.trainer_side);
        }

    }
    public void onClick(View view){
        Intent intent = new Intent(this, ComparePosture_second.class);
        intent.putExtra("num", exercisenum);
        startActivity(intent);
        finish();
    }
}
