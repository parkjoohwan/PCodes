package com.example.aict.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by AICT on 2018-03-05.
 */

public class UserinfoActivity extends AppCompatActivity {
    String user_id;

    String name = "";
    String age= "";
    String sex= "";
    String weight= "";
    String exercise= "";

    private DBHelper helper;
    private SQLiteDatabase db;
    EditText edit_username;

    final String spinner_age[] = new String[60-16];
    final String spinner_sex[] = {"남", "여"};
    final String spinner_weight[] = new String[110-39];
    final String spinner_exercise[] = {"상관없음", "상체", "하체", "복근"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        final Spinner s_age = (Spinner)findViewById(R.id.spinner5);
        final Spinner s_sex = (Spinner)findViewById(R.id.spinner6);
        final Spinner s_weight = (Spinner)findViewById(R.id.spinner7);
        final Spinner s_exercise = (Spinner)findViewById(R.id.spinner8);
        edit_username = (EditText)findViewById(R.id.editText);

        for(int i = 17 ; i<=60; i++){
            spinner_age[i-17] = Integer.toString(i);
        }
        for(int i=40 ; i<=110 ; i++){
            spinner_weight[i-40] = Integer.toString(i);
        }

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");

        // SQLiteOpenHelper 클래스의 subclass인 DBHelper 클래스 객체 생성
        helper = new DBHelper(this);
        // DBHelper 객체를 이용하여 DB 생성
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException e) {
            db = helper.getReadableDatabase();
        }

        Cursor cursor;
        cursor = db.rawQuery("SELECT name, age, sex, weight, exercise FROM users WHERE id='"+user_id+"';",null);
        if(cursor.getCount() == 0){
            Toast.makeText(this, "유저 정보를 읽어오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            String db_name = "";
            String db_age="";
            String db_sex="";
            String db_weight = "";
            String db_exercise = "";
            while(cursor.moveToNext()){
                db_name = cursor.getString(0);
                db_age = cursor.getString(1);
                db_sex = cursor.getString(2);
                db_weight = cursor.getString(3);
                db_exercise = cursor.getString(4);
            }

            edit_username.setText(db_name);
            name = db_name;

            s_age.setSelection(Integer.parseInt(db_age) - 17);
            age = db_age;

            if(db_sex.equals("남")){
                s_sex.setSelection(0);
                sex = db_sex;
            } else if(db_sex.equals("여")){
                s_sex.setSelection(1);
                sex = db_sex;
            }

            s_weight.setSelection(Integer.parseInt(db_weight) - 40);
            weight = db_weight;

            if(db_exercise.equals("상관없음")){
                s_exercise.setSelection(0);
                exercise = db_exercise;
            }else if(db_exercise.equals("상체")){
                s_exercise.setSelection(1);
                exercise = db_exercise;
            }else if(db_exercise.equals("하체")){
                s_exercise.setSelection(2);
                exercise = db_exercise;
            }else if(db_exercise.equals("복근")){
                s_exercise.setSelection(3);
                exercise = db_exercise;
            }
        }

        s_age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                age = spinner_age[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        s_sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                sex = spinner_sex[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        s_weight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                weight = spinner_weight[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        s_exercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                exercise = spinner_exercise[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void onClick_fix(View view){
        db.execSQL("UPDATE users SET name = '"+edit_username.getText().toString()+"'");
        db.execSQL("UPDATE users SET age = '"+age+"'");
        db.execSQL("UPDATE users SET sex = '"+sex+"'");
        db.execSQL("UPDATE users SET weight = '"+weight+"'");
        db.execSQL("UPDATE users SET exercise = '"+exercise+"'");
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }
    public void onClick_cancle(View view){
        finish();
    }
}
