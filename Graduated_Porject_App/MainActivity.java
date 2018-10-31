package com.example.aict.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String user_id;
    private long backKeyPressedTime = 0;

    private DBHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("메인", "호출");
        TextView textView_top = (TextView)findViewById(R.id.textView20);

        Intent getintent = getIntent();
        user_id = getintent.getStringExtra("user_id");

        // SQLiteOpenHelper 클래스의 subclass인 DBHelper 클래스 객체 생성
        helper = new DBHelper(this);
        // DBHelper 객체를 이용하여 DB 생성
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException e) {
            db = helper.getReadableDatabase();
        }

        Cursor cursor;
        cursor = db.rawQuery("SELECT name FROM users WHERE id='"+user_id+"';",null);
        if(cursor.getCount() == 0){
            Toast.makeText(this, "사용자 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
        else{
            String db_name = "";
            while(cursor.moveToNext()){
                db_name = cursor.getString(0);
            }
            textView_top.setText(db_name+"님 안녕하세요\n원하는 기능을 선택해주세요.");
        }



    }
    public void onClickPlan(View v) {
        Intent intent = new Intent(this, ExerciseplanActivity.class);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }
    public void onClickUser(View v) {
        Intent intent = new Intent(this, UserinfoActivity.class);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
        finish();
    }
    public void onClickVideo(View v) {
        Intent intent = new Intent(this, VideoActivity.class);
        startActivity(intent);
    }
    public void onClickPosture(View v) {
        Intent intent = new Intent(this, ComparePosture.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();

            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }


    }
}
