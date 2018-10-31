package com.example.aict.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ExerciseplanActivity_second extends AppCompatActivity implements ListViewBtnAdapter.ListBtnClickListener {
    int num = 7;   // 운동항목 개수
    ListViewBtnItem[] item = new ListViewBtnItem[num];  // 항목 만큼의 아이템 클래스의 배열 선언
    ListViewBtnAdapter adapter;
    int count[] = new int[num]; // 각 버튼의 완료와 취소를 구분하기 위한 int 배열
    String complete_color = "#0BC904";
    String cancle_color = "#FF0000";
    int complete_count = 0;

    private DBHelper helper;
    private SQLiteDatabase db;

    String user_id;

    String db_age = "";
    String db_sex = "";
    String db_weight = "";
    String db_exercise = "";

    int age = 0;
    int weight =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_second);

        Intent intent = getIntent();
        count = intent.getExtras().getIntArray("buttonchoose");
        user_id = intent.getStringExtra("user_id");

        ListView listview;
        ArrayList<ListViewBtnItem> items = new ArrayList<ListViewBtnItem>();

        // SQLiteOpenHelper 클래스의 subclass인 DBHelper 클래스 객체 생성
        helper = new DBHelper(this);
        // DBHelper 객체를 이용하여 DB 생성
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException e) {
            db = helper.getReadableDatabase();
        }

        Cursor cursor;
        cursor = db.rawQuery("SELECT age, sex, weight, exercise FROM users WHERE id='"+user_id+"';",null);
        if(cursor.getCount() == 0){
            Toast.makeText(this, "사용자 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                db_age = cursor.getString(0);
                db_sex = cursor.getString(1);
                db_weight = cursor.getString(2);
                db_exercise = cursor.getString(3);

                age = Integer.parseInt(db_age);
                weight = Integer.parseInt(db_weight);

            }
        }


        loadItemsFromDB(items); // 리스트 데이터 받아옴

        // 어뎁터 선언
        adapter = new ListViewBtnAdapter(this, R.layout.listview, items, this);

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview_plan);
        listview.setAdapter(adapter);

        for(int i=0 ; i<num ; i++){
            if(count[i] % 2 == 0){
                item[i].setbtnText("완료");
                item[i].setbtnColor(complete_color);
                complete_count++;  // 전체 선택된 개수
            }
            else {
                item[i].setbtnText("미완료");
                item[i].setbtnColor(cancle_color);

            }
            count[i]++;  // 버튼 클릭시 +1 되서 완료 미완료 구분
        }



    }
    // 버튼 클릭시 하는 거
    @Override
    public void onListBtnClick(int position) {

        for(int i=0 ; i<num ; i++){
            // 클릭된 버튼과 반복문의 i가 같을 때만
            if(position == i){
                if(count[position] % 2 == 0){
                    item[i].setbtnText("완료");
                    item[i].setbtnColor(complete_color);
                    complete_count++;  // 전체 선택된 개수
                }
                else {
                    item[i].setbtnText("미완료");
                    item[i].setbtnColor(cancle_color);
                    complete_count--; // 전체 선택된 개수
                }
                count[position]++;  // 버튼 클릭시 +1 되서 완료 미완료 구분
                adapter.notifyDataSetChanged(); //어뎁터 갱신
            }
        }
    }

    // 완료 버튼을 눌렀을 때
    public void onClick(View view){
        double persent = (double)complete_count/(double)num;    // 페센트 값 계산
        for(int i=0 ; i<num ; i++){
            count[i]++;
            Log.d("값 : ", Integer.toString(count[i]));
        }

        Intent intent = new Intent();
        intent.putExtra("colorpersent", persent);
        intent.putExtra("choose", count);
        intent.putExtra("exercisenum", num);




        this.setResult(RESULT_OK, intent);      // ExerciseplanActivity로 퍼센트 값 보냄
        finish();   // 액티비티 종료
    }

    public boolean loadItemsFromDB(ArrayList<ListViewBtnItem> list) {

        for(int i=0 ; i<num ; i++){
            item[i] = new ListViewBtnItem();
        }

        list.clear();

        if (list == null) {
            list = new ArrayList<ListViewBtnItem>() ;
        }

        setitem();

        for(int i=0 ; i<num ; i++){
            list.add(item[i]);
        }

        return true ;
    }

    public void setitem(){
        if(age >= 17 && age <= 30){
            if(db_sex.equals("남")){
                if(weight >= 40 && weight <= 70){
                    if(db_exercise.equals("상관없음")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.pushup)) ;
                        item[1].setText("팔굽혀펴기 20회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[2].setText("플랭크 1분 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 30회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[4].setText("스쿼트 20회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[5].setText("런지 20회 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }else if(db_exercise.equals("상체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.pushup)) ;
                        item[1].setText("팔굽혀펴기 20회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[2].setText("플랭크 1분 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[3].setText("레그레이즈 20회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("벤치딥스 20회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[5].setText("사이드 힙 레이즈 1분 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }else if(db_exercise.equals("하체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[1].setText("스쿼트 20회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[2].setText("런지 20회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.brigh)) ;
                        item[3].setText("브릿지 1분 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("마운틴클라이머 30회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 1분 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }else if(db_exercise.equals("복근")){
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 1분 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 20회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 30회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[4].setText("사이드 힙 레이즈 1분 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 1분 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }
                }else if(weight >= 71 && weight <= 90){
                    if(db_exercise.equals("상관없음")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.pushup)) ;
                        item[1].setText("팔굽혀펴기 13회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[2].setText("플랭크 40초 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 20회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[4].setText("스쿼트 13회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[5].setText("런지 13회 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }else if(db_exercise.equals("상체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.pushup)) ;
                        item[1].setText("팔굽혀펴기 13회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[2].setText("플랭크 40초 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[3].setText("레그레이즈 13회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("벤치딥스 13회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[5].setText("사이드 힙 레이즈 40초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }else if(db_exercise.equals("하체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[1].setText("스쿼트 13회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[2].setText("런지 13회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.brigh)) ;
                        item[3].setText("브릿지 40초 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("마운틴클라이머 20회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 40초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }else if(db_exercise.equals("복근")){
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 40초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 13회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 20회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[4].setText("사이드 힙 레이즈 40초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 40초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }
                }else if(weight >= 91 && weight <= 110){
                    if(db_exercise.equals("상관없음")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.pushup)) ;
                        item[1].setText("팔굽혀펴기 7회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[2].setText("플랭크 30초 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 10회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[4].setText("스쿼트 7회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[5].setText("런지 7회 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }else if(db_exercise.equals("상체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.pushup)) ;
                        item[1].setText("팔굽혀펴기 7회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[2].setText("플랭크 30초 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[3].setText("레그레이즈 7회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("벤치딥스 7회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[5].setText("사이드 힙 레이즈 30초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }else if(db_exercise.equals("하체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[1].setText("스쿼트 7회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[2].setText("런지 7회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.brigh)) ;
                        item[3].setText("브릿지 30초 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("마운틴클라이머 10회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 30초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }else if(db_exercise.equals("복근")){
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 30초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 7회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 7회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[4].setText("사이드 힙 레이즈 30초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 30초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }
                }
            }else if(db_sex.equals("여")){
                if(weight >= 40 && weight <= 70){
                    if(db_exercise.equals("상관없음")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 1분 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 20회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 30회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[4].setText("스쿼트 20회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[5].setText("런지 20회 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }else if(db_exercise.equals("상체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 1분 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[2].setText("마운틴 클라이머 30회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[3].setText("레그레이즈 20회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[4].setText("버드독 1분 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[5].setText("사이드 힙 레이즈 1분 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }else if(db_exercise.equals("하체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[1].setText("스쿼트 20회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[2].setText("런지 20회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.brigh)) ;
                        item[3].setText("브릿지 1분 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("마운틴클라이머 30회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 1분 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }else if(db_exercise.equals("복근")){
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 1분 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 20회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 30회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[4].setText("사이드 힙 레이즈 1분 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 1분 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }
                }else if(weight >= 71 && weight <= 90){
                    if(db_exercise.equals("상관없음")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 40초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 13회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 20회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[4].setText("스쿼트 13회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[5].setText("런지 13회 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }else if(db_exercise.equals("상체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 40초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[2].setText("마운틴 클라이머 20회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[3].setText("레그레이즈 13회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[4].setText("버드독 40초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[5].setText("사이드 힙 레이즈 40초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }else if(db_exercise.equals("하체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[1].setText("스쿼트 13회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[2].setText("런지 13회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.brigh)) ;
                        item[3].setText("브릿지 40초 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("마운틴클라이머 20회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 40초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }else if(db_exercise.equals("복근")){
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 40초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 13회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 20회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[4].setText("사이드 힙 레이즈 40초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 40초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }
                }else if(weight >= 91 && weight <= 110){
                    if(db_exercise.equals("상관없음")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 30초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 7회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 10회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[4].setText("스쿼트 7회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[5].setText("런지 7회 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }else if(db_exercise.equals("상체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 30초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[2].setText("마운틴 클라이머 10회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[3].setText("레그레이즈 7회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[4].setText("버드독 30초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[5].setText("사이드 힙 레이즈 30초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }else if(db_exercise.equals("하체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[1].setText("스쿼트 7회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[2].setText("런지 7회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.brigh)) ;
                        item[3].setText("브릿지 30초 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("마운틴클라이머 10회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 30초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }else if(db_exercise.equals("복근")){
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 30초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 7회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 10회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[4].setText("사이드 힙 레이즈 30초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 30초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }
                }
            }
        }else if(age >= 31 && age <= 50){
            if(db_sex.equals("남")){
                if(weight >= 40 && weight <= 70){
                    if(db_exercise.equals("상관없음")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.pushup)) ;
                        item[1].setText("팔굽혀펴기 15회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[2].setText("플랭크 40초 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 20회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[4].setText("스쿼트 15회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[5].setText("런지 15회 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }else if(db_exercise.equals("상체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.pushup)) ;
                        item[1].setText("팔굽혀펴기 15회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[2].setText("플랭크 40초 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[3].setText("레그레이즈 15회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("벤치딥스 15회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[5].setText("사이드 힙 레이즈 40초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }else if(db_exercise.equals("하체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[1].setText("스쿼트 15회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[2].setText("런지 15회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.brigh)) ;
                        item[3].setText("브릿지 40초 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("마운틴클라이머 20회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 40초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }else if(db_exercise.equals("복근")){
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 40초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 15회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 20회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[4].setText("사이드 힙 레이즈 40초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 40초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }
                }else if(weight >= 71 && weight <= 90){
                    if(db_exercise.equals("상관없음")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.pushup)) ;
                        item[1].setText("팔굽혀펴기 10회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[2].setText("플랭크 30초 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 16회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[4].setText("스쿼트 10회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[5].setText("런지 10회 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }else if(db_exercise.equals("상체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.pushup)) ;
                        item[1].setText("팔굽혀펴기 10회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[2].setText("플랭크 30초 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[3].setText("레그레이즈 10회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("벤치딥스 10회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[5].setText("사이드 힙 레이즈 30초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }else if(db_exercise.equals("하체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[1].setText("스쿼트 10회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[2].setText("런지 10회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.brigh)) ;
                        item[3].setText("브릿지 30초 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("마운틴클라이머 16회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 30초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }else if(db_exercise.equals("복근")){
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 30초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 10회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 16회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[4].setText("사이드 힙 레이즈 30초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 30초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }
                }else if(weight >= 91 && weight <= 110){
                    if(db_exercise.equals("상관없음")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.pushup)) ;
                        item[1].setText("팔굽혀펴기 5회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[2].setText("플랭크 20초 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 10회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[4].setText("스쿼트 5회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[5].setText("런지 5회 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }else if(db_exercise.equals("상체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.pushup)) ;
                        item[1].setText("팔굽혀펴기 5회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[2].setText("플랭크 20초 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[3].setText("레그레이즈 5회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("벤치딥스 5회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[5].setText("사이드 힙 레이즈 20초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }else if(db_exercise.equals("하체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[1].setText("스쿼트 5회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[2].setText("런지 5회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.brigh)) ;
                        item[3].setText("브릿지 20초 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("마운틴클라이머 10회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 20초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }else if(db_exercise.equals("복근")){
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 20초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 5회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 10회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[4].setText("사이드 힙 레이즈 20초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 20초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }
                }
            }else if(db_sex.equals("여")){
                if(weight >= 40 && weight <= 70){
                    if(db_exercise.equals("상관없음")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 40초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 15회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 20회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[4].setText("스쿼트 15회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[5].setText("런지 15회 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }else if(db_exercise.equals("상체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 40초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[2].setText("마운틴 클라이머 20회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[3].setText("레그레이즈 15회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[4].setText("버드독 40초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[5].setText("사이드 힙 레이즈 40초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }else if(db_exercise.equals("하체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[1].setText("스쿼트 15회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[2].setText("런지 15회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.brigh)) ;
                        item[3].setText("브릿지 40초 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("마운틴클라이머 20회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 40초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }else if(db_exercise.equals("복근")){
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 40초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 15회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 20회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[4].setText("사이드 힙 레이즈 40초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 40초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }
                }else if(weight >= 71 && weight <= 90){
                    if(db_exercise.equals("상관없음")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 30초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 10회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 16회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[4].setText("스쿼트 10회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[5].setText("런지 10회 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }else if(db_exercise.equals("상체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 30초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[2].setText("마운틴 클라이머 16회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[3].setText("레그레이즈 10회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[4].setText("버드독 30초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[5].setText("사이드 힙 레이즈 30초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }else if(db_exercise.equals("하체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[1].setText("스쿼트 10회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[2].setText("런지 10회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.brigh)) ;
                        item[3].setText("브릿지 30초 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("마운틴클라이머 16회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 30초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }else if(db_exercise.equals("복근")){
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 30초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 10회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 16회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[4].setText("사이드 힙 레이즈 30초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 30초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }
                }else if(weight >= 91 && weight <= 110){
                    if(db_exercise.equals("상관없음")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 20초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 5회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 10회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[4].setText("스쿼트 5회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[5].setText("런지 5회 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }else if(db_exercise.equals("상체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 20초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[2].setText("마운틴 클라이머 10회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[3].setText("레그레이즈 5회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[4].setText("버드독 20초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[5].setText("사이드 힙 레이즈 20초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }else if(db_exercise.equals("하체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[1].setText("스쿼트 5회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[2].setText("런지 5회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.brigh)) ;
                        item[3].setText("브릿지 20초 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("마운틴클라이머 10회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 20초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }else if(db_exercise.equals("복근")){
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 20초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 5회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 10회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[4].setText("사이드 힙 레이즈 20초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 20초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }
                }
            }
        }else if(age >= 51 && age <= 60){
            if(db_sex.equals("남")){
                if(weight >= 40 && weight <= 70){
                    if(db_exercise.equals("상관없음")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.pushup)) ;
                        item[1].setText("팔굽혀펴기 10회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[2].setText("플랭크 30초 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 16회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[4].setText("스쿼트 10회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[5].setText("런지 10회 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }else if(db_exercise.equals("상체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.pushup)) ;
                        item[1].setText("팔굽혀펴기 10회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[2].setText("플랭크 30초 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[3].setText("레그레이즈 10회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("벤치딥스 10회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[5].setText("사이드 힙 레이즈 30초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }else if(db_exercise.equals("하체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[1].setText("스쿼트 10회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[2].setText("런지 10회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.brigh)) ;
                        item[3].setText("브릿지 30초 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("마운틴클라이머 16회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 30초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }else if(db_exercise.equals("복근")){
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 30초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 10회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 16회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[4].setText("사이드 힙 레이즈 30초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 30초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }
                }else if(weight >= 71 && weight <= 90){
                    if(db_exercise.equals("상관없음")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.pushup)) ;
                        item[1].setText("팔굽혀펴기 7회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[2].setText("플랭크 25초 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 12회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[4].setText("스쿼트 7회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[5].setText("런지 7회 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }else if(db_exercise.equals("상체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.pushup)) ;
                        item[1].setText("팔굽혀펴기 7회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[2].setText("플랭크 25초 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[3].setText("레그레이즈 7회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("벤치딥스 7회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[5].setText("사이드 힙 레이즈 25초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }else if(db_exercise.equals("하체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[1].setText("스쿼트 7회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[2].setText("런지 7회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.brigh)) ;
                        item[3].setText("브릿지 25초 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("마운틴클라이머 12회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 25초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }else if(db_exercise.equals("복근")){
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 25초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 7회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 12회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[4].setText("사이드 힙 레이즈 25초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 25초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }
                }else if(weight >= 91 && weight <= 110){
                    if(db_exercise.equals("상관없음")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.pushup)) ;
                        item[1].setText("팔굽혀펴기 5회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[2].setText("플랭크 20초 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 8회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[4].setText("스쿼트 5회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[5].setText("런지 5회 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }else if(db_exercise.equals("상체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.pushup)) ;
                        item[1].setText("팔굽혀펴기 5회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[2].setText("플랭크 20초 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[3].setText("레그레이즈 5회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("벤치딥스 5회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[5].setText("사이드 힙 레이즈 20초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }else if(db_exercise.equals("하체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[1].setText("스쿼트 5회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[2].setText("런지 5회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.brigh)) ;
                        item[3].setText("브릿지 20초 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("마운틴클라이머 8회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 20초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }else if(db_exercise.equals("복근")){
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 20초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 5회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 8회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[4].setText("사이드 힙 레이즈 20초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 20초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }
                }
            }else if(db_sex.equals("여")){
                if(weight >= 40 && weight <= 70){
                    if(db_exercise.equals("상관없음")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 30초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 10회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 16회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[4].setText("스쿼트 10회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[5].setText("런지 10회 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }else if(db_exercise.equals("상체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 30초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[2].setText("마운틴 클라이머 16회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[3].setText("레그레이즈 10회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[4].setText("버드독 30초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[5].setText("사이드 힙 레이즈 30초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }else if(db_exercise.equals("하체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[1].setText("스쿼트 10회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[2].setText("런지 10회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.brigh)) ;
                        item[3].setText("브릿지 30초 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("마운틴클라이머 16회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 30초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }else if(db_exercise.equals("복근")){
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 20분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 30초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 10회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 16회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[4].setText("사이드 힙 레이즈 30초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 30초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 20분") ;
                    }
                }else if(weight >= 71 && weight <= 90){
                    if(db_exercise.equals("상관없음")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 25초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 7회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 12회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[4].setText("스쿼트 7회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[5].setText("런지 7회 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }else if(db_exercise.equals("상체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 25초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[2].setText("마운틴 클라이머 12회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[3].setText("레그레이즈 7회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[4].setText("버드독 25초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[5].setText("사이드 힙 레이즈 25초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }else if(db_exercise.equals("하체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 25분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[1].setText("스쿼트 7회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[2].setText("런지 7회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.brigh)) ;
                        item[3].setText("브릿지 25초 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("마운틴클라이머 12회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 25초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }else if(db_exercise.equals("복근")){
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 30분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 25초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 7회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 12회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[4].setText("사이드 힙 레이즈 25초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 25초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 30분") ;
                    }
                }else if(weight >= 91 && weight <= 110){
                    if(db_exercise.equals("상관없음")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 20초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 5회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 8회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[4].setText("스쿼트 5회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[5].setText("런지 5회 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }else if(db_exercise.equals("상체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 20초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[2].setText("마운틴 클라이머 8회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[3].setText("레그레이즈 5회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[4].setText("버드독 20초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[5].setText("사이드 힙 레이즈 20초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }else if(db_exercise.equals("하체")) {
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
                        item[1].setText("스쿼트 5회 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
                        item[2].setText("런지 5회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.brigh)) ;
                        item[3].setText("브릿지 20초 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
                        item[4].setText("마운틴클라이머 8회 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 20초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }else if(db_exercise.equals("복근")){
                        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[0].setText("런닝 40분") ;
                        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
                        item[1].setText("플랭크 20초 3세트");
                        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
                        item[2].setText("레그레이즈 5회 3세트");
                        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
                        item[3].setText("마운틴 클라이머 8회 3세트");
                        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
                        item[4].setText("사이드 힙 레이즈 20초 3세트");
                        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
                        item[5].setText("버드독 20초 3세트");
                        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.running)) ;
                        item[6].setText("런닝 40분") ;
                    }
                }
            }
        }
    }
}
