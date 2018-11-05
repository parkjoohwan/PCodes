package com.example.aict.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.*;
import java.net.Socket;

import static com.example.aict.myapplication.ComparePosture.exercisename;
import static com.example.aict.myapplication.ComparePosture_communication.result_msg;
import static com.example.aict.myapplication.ComparePosture_communication.result_msg_two;
import static com.example.aict.myapplication.ComparePosture_communication.storedir;

/**
 * Created by AICT on 2018-03-16.
 */

public class ComparePosture_fourth extends AppCompatActivity {
    int number;
    Bitmap myBitmap;
    File imgFile;
    int image_num = 0;
    ImageView user_image;
    ImageView trainer_image;
    TextView result_text;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compareposture_fourth);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //세로모드 고정
        Log.d("자세비교4", "호출");

        trainer_image = (ImageView)findViewById(R.id.imageView_trainer);
        user_image = (ImageView)findViewById(R.id.imageView_user);
        result_text = (TextView)findViewById(R.id.textView_coment);

        if(exercisename == 2){
            trainer_image.setImageResource(R.drawable.trainer_pushup1);
        }else if(exercisename == 3){
            trainer_image.setImageResource(R.drawable.trainer_squat);
        }else if(exercisename == 4){
            trainer_image.setImageResource(R.drawable.trainer_plank);
        }else if(exercisename == 6 ){
            trainer_image.setImageResource(R.drawable.trainer_lengh1);
        }else if(exercisename == 8){
            trainer_image.setImageResource(R.drawable.trainer_legraise1);
        }else if(exercisename == 10){
            trainer_image.setImageResource(R.drawable.trainer_mountain1);
        }else if(exercisename == 11){
            trainer_image.setImageResource(R.drawable.trainer_brigh);
        }else if(exercisename == 13){
            trainer_image.setImageResource(R.drawable.trainer_bench1);
        }else if(exercisename == 14){
            trainer_image.setImageResource(R.drawable.trainer_side);
        }else if(exercisename == 16){
            trainer_image.setImageResource(R.drawable.trainer_briddog1);
        }
        Intent intent = getIntent();
        number = intent.getIntExtra("exercise_num", 0);


        imgFile = new File(storedir[image_num]);

        if(imgFile.exists()){
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            user_image.setImageBitmap(myBitmap);

        }

        result_text.setText(result_msg);

    }
    public void onClick_left(View view){
        if(image_num == 0){
            Toast.makeText(this, "해당 사진이 첫 번째 사진입니다.", Toast.LENGTH_SHORT).show();
        }
        else{
            image_num = image_num - 1;
            imgFile = new File(storedir[image_num]);
            if(imgFile.exists()){
                myBitmap.recycle();
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                user_image.setImageBitmap(myBitmap);
                if(exercisename == 2){
                    trainer_image.setImageResource(0);
                    trainer_image.setImageResource(R.drawable.trainer_pushup1);
                }else if(exercisename == 3){
                    trainer_image.setImageResource(0);
                    trainer_image.setImageResource(R.drawable.trainer_squat);
                }else if(exercisename == 4){
                    trainer_image.setImageResource(0);
                    trainer_image.setImageResource(R.drawable.trainer_plank);
                }else if(exercisename == 6 ){
                    trainer_image.setImageResource(0);
                    trainer_image.setImageResource(R.drawable.trainer_lengh1);
                }else if(exercisename == 8){
                    trainer_image.setImageResource(0);
                    trainer_image.setImageResource(R.drawable.trainer_legraise1);
                }else if(exercisename == 10){
                    trainer_image.setImageResource(0);
                    trainer_image.setImageResource(R.drawable.trainer_mountain1);
                }else if(exercisename == 11){
                    trainer_image.setImageResource(0);
                    trainer_image.setImageResource(R.drawable.trainer_brigh);
                }else if(exercisename == 13){
                    trainer_image.setImageResource(0);
                    trainer_image.setImageResource(R.drawable.trainer_bench1);
                }else if(exercisename == 14){
                    trainer_image.setImageResource(0);
                    trainer_image.setImageResource(R.drawable.trainer_side);
                }else if(exercisename == 16){
                    trainer_image.setImageResource(0);
                    trainer_image.setImageResource(R.drawable.trainer_briddog1);
                }
                result_text.setText(result_msg);
            }
        }
    }
    public void onClick_right(View view){
        if(image_num == number-1){
            Toast.makeText(this, "해당 사진이 마지막 사진입니다.", Toast.LENGTH_SHORT).show();
        }
        else{
            image_num = image_num +1;
            imgFile = new File(storedir[image_num]);
            if(imgFile.exists()){
                myBitmap.recycle();
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                user_image.setImageBitmap(myBitmap);
                if(exercisename == 2){
                    trainer_image.setImageResource(0);
                    trainer_image.setImageResource(R.drawable.trainer_pushup2);
                }else if(exercisename == 3){
                    trainer_image.setImageResource(0);
                    trainer_image.setImageResource(R.drawable.trainer_squat);
                }else if(exercisename == 4){
                    trainer_image.setImageResource(0);
                    trainer_image.setImageResource(R.drawable.trainer_plank);
                }else if(exercisename == 6 ){
                    trainer_image.setImageResource(0);
                    trainer_image.setImageResource(R.drawable.trainer_lengh2);
                }else if(exercisename == 8){
                    trainer_image.setImageResource(0);
                    trainer_image.setImageResource(R.drawable.trainer_legraise2);
                }else if(exercisename == 10){
                    trainer_image.setImageResource(0);
                    trainer_image.setImageResource(R.drawable.trainer_mountain2);
                }else if(exercisename == 11){
                    trainer_image.setImageResource(0);
                    trainer_image.setImageResource(R.drawable.trainer_brigh);
                }else if(exercisename == 13){
                    trainer_image.setImageResource(0);
                    trainer_image.setImageResource(R.drawable.trainer_bench2);
                }else if(exercisename == 14){
                    trainer_image.setImageResource(0);
                    trainer_image.setImageResource(R.drawable.trainer_side);
                }else if(exercisename == 16){
                    trainer_image.setImageResource(0);
                    trainer_image.setImageResource(R.drawable.trainer_briddog2);
                }
                result_text.setText(result_msg_two);
            }
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        myBitmap.recycle();
    }
}


