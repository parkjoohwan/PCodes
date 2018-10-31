package com.example.aict.myapplication;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by AICT on 2018-03-29.
 */

public class SignupActivity extends AppCompatActivity {

    String name = "";
    String age= "";
    String sex= "";
    String id= "";
    String pw= "";
    String pw_two= "";
    String weight= "";
    String exercise= "";

    final String spinner_age[] = new String[60-16];
    final String spinner_sex[] = {"남", "여"};
    final String spinner_weight[] = new String[110-39];
    final String spinner_exercise[] = {"상관없음", "상체", "하체", "복근"};

    EditText edit_name;
    EditText edit_id;
    EditText edit_pw;
    EditText edit_pw_two;

    Boolean id_idx = false;
    Boolean pw_idx = false;

    private DBHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final Spinner s_age = (Spinner)findViewById(R.id.spinner);
        final Spinner s_sex = (Spinner)findViewById(R.id.spinner2);
        final Spinner s_weight = (Spinner)findViewById(R.id.spinner3);
        final Spinner s_exercise = (Spinner)findViewById(R.id.spinner4);

        edit_name = (EditText)findViewById(R.id.editText10);
        edit_id = (EditText)findViewById(R.id.editText8);
        edit_pw = (EditText)findViewById(R.id.editText9);
        edit_pw_two = (EditText)findViewById(R.id.editText11);

        // SQLiteOpenHelper 클래스의 subclass인 DBHelper 클래스 객체 생성
        helper = new DBHelper(this);
        // DBHelper 객체를 이용하여 DB 생성
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException e) {
            db = helper.getReadableDatabase();
        }

        for(int i = 17 ; i<=60; i++){
            spinner_age[i-17] = Integer.toString(i);
        }
        for(int i=40 ; i<=110 ; i++){
            spinner_weight[i-40] = Integer.toString(i);
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
    public void onClick_id(View view){
        id = edit_id.getText().toString();
        Cursor cursor;
        cursor = db.rawQuery("SELECT id FROM users WHERE id='"+id+"';",null);
        if(cursor.getCount() == 0){
            Toast.makeText(this, "해당 아이디 사용이 가능합니다.", Toast.LENGTH_SHORT).show();
            id_idx = true;
        }
        else{
            String db_id="";
            while(cursor.moveToNext()){
                db_id = cursor.getString(0);
            }
            if(db_id.equals(id)){
                Toast.makeText(this, "해당 아이디가 이미 있습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void onClick_pw(View view){
        pw = edit_pw.getText().toString();
        pw_two = edit_pw_two.getText().toString();
        if(pw.equals(pw_two)){
            Toast.makeText(this, "비밀번호 확인이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            pw_idx = true;
        }
        else{
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    public void onClickSignupComplete(View view){
        name = edit_name.getText().toString();


        if(id_idx == false){
            Toast.makeText(this, "아이디 중복검사를 완료해주세요.", Toast.LENGTH_SHORT).show();
        }
        if(pw_idx == false){
            Toast.makeText(this, "비밀번호 확인을 완료해주세요.", Toast.LENGTH_SHORT).show();
        }
        if(name.equals("")){
            Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else{
            if(id_idx == true){
                if(pw_idx == true){
                    db.execSQL("INSERT INTO users VALUES (null, '"+name+"', '"+age+"', '"+sex+"', '"+id+"', '"+pw+"', '"+weight+"', '"+exercise+"' )");
                    Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }


        Log.d("이름 : ", name);
        Log.d("나이 : ", age);
        Log.d("성별 : ", sex);
        Log.d("id : ", id);
        Log.d("pw : ", pw);
        Log.d("pw_two : ", pw_two);
        Log.d("weight : ", weight);
        Log.d("exercise : ", exercise);
    }
    public void onClick_cancle(View view){
        finish();
    }
}