package com.example.aict.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.aict.myapplication.ComparePosture.exercisename;

public class ComparePosture_explaintwo extends AppCompatActivity {
    int exercisenum;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compareposture_explaintwo);

        Intent getintent = getIntent();
        exercisenum = getintent.getIntExtra("num",0);

        TextView textView = (TextView)findViewById(R.id.textView30);
        ImageView imageView = (ImageView)findViewById(R.id.imageView6);
        ImageView imageView2 = (ImageView)findViewById(R.id.imageView7);
        imageView.setImageResource(0);
        imageView2.setImageResource(0);

        textView.setText("해당 운동은 구분 동작 2개로 이루어져 있습니다."  );
        if(exercisename == 1){
            imageView.setImageResource(R.drawable.trainer_pushup1);
            imageView2.setImageResource(R.drawable.trainer_pushup2);
        }else if(exercisename == 5 ){
            imageView.setImageResource(R.drawable.trainer_lengh1);
            imageView2.setImageResource(R.drawable.trainer_lengh2);
        }else if(exercisename == 7){
            imageView.setImageResource(R.drawable.trainer_legraise1);
            imageView2.setImageResource(R.drawable.trainer_legraise2);
        }else if(exercisename == 9){
            imageView.setImageResource(R.drawable.trainer_mountain1);
            imageView2.setImageResource(R.drawable.trainer_mountain2);
        }else if(exercisename == 12){
            imageView.setImageResource(R.drawable.trainer_bench1);
            imageView2.setImageResource(R.drawable.trainer_bench2);
        }else if(exercisename == 15){
            imageView.setImageResource(R.drawable.trainer_briddog1);
            imageView2.setImageResource(R.drawable.trainer_briddog2);
        }

    }
    public void onClick(View view){
        Intent intent = new Intent(this, ComparePosture_second.class);
        intent.putExtra("num", exercisenum);
        startActivity(intent);
        finish();
    }
}