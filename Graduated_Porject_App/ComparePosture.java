package com.example.aict.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by AICT on 2018-03-05.
 */

public class ComparePosture extends AppCompatActivity implements ListViewBtnAdapter.ListBtnClickListener {
    int num = 10;   // 운동항목 개수
    ListViewBtnItem[] item = new ListViewBtnItem[num];  // 항목 만큼의 아이템 클래스의 배열 선언
    static int exercisename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compareposture);

        Log.d("자세비교1", "호출");

        ListView listview;
        ListViewBtnAdapter adapter;
        ArrayList<ListViewBtnItem> items = new ArrayList<ListViewBtnItem>();

        loadItemsFromDB(items); // 리스트 데이터 받아옴

        // 어뎁터 선언
        adapter = new ListViewBtnAdapter(this, R.layout.listview, items, this);

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview2);
        listview.setAdapter(adapter);
    }
    @Override
    public void onListBtnClick(int position) {
        int one = 1;
        int two = 2;
        int three = 3;
        String putname = "num";
        // 팔굽혀펴기
        if(position == 0){
            exercisename = 1;
            Intent intent = new Intent(this, ComparePosture_explaintwo.class);
            intent.putExtra(putname, two);
            startActivity(intent);
        }
        // 스쿼트
        else if(position == 1){
            exercisename = 3;
            Intent intent = new Intent(this, ComparePosture_explain.class);
            intent.putExtra(putname, one);
            startActivity(intent);
        }
        // 플랭크
        else if(position == 2){
            exercisename = 4;
            Intent intent = new Intent(this, ComparePosture_explain.class);
            intent.putExtra(putname, one);
            startActivity(intent);
        }
        // 런지
        else if(position == 3){
            exercisename = 5;
            Intent intent = new Intent(this, ComparePosture_explaintwo.class);
            intent.putExtra(putname, two);
            startActivity(intent);
        }
        // 레그레이즈
        else if(position == 4){
            exercisename = 7;
            Intent intent = new Intent(this, ComparePosture_explaintwo.class);
            intent.putExtra(putname, two);
            startActivity(intent);
        }
        // 마운틴 클라이머
        else if(position == 5){
            exercisename = 9;
            Intent intent = new Intent(this, ComparePosture_explaintwo.class);
            intent.putExtra(putname, two);
            startActivity(intent);
        }
        // 브릿지
        else if(position == 6){
            exercisename = 11;
            Intent intent = new Intent(this, ComparePosture_explain.class);
            intent.putExtra(putname, one);
            startActivity(intent);
        }
        // 벤치딥스
        else if(position == 7){
            exercisename = 12;
            Intent intent = new Intent(this, ComparePosture_explaintwo.class);
            intent.putExtra(putname, two);
            startActivity(intent);
        }
        // 사이드 힙 레이즈
        else if(position == 8){
            exercisename = 14;
            Intent intent = new Intent(this, ComparePosture_explain.class);
            intent.putExtra(putname, one);
            startActivity(intent);
        }
        // 버드독
        else if(position == 9){
            exercisename = 15;
            Intent intent = new Intent(this, ComparePosture_explaintwo.class);
            intent.putExtra(putname, two);
            startActivity(intent);
        }

    }


    public boolean loadItemsFromDB(ArrayList<ListViewBtnItem> list) {
        for(int i=0 ; i<num ; i++){
            item[i] = new ListViewBtnItem();
        }

        list.clear();

        if (list == null) {
            list = new ArrayList<ListViewBtnItem>() ;
        }

        // 아이템 생성.
        item[0].setIcon(ContextCompat.getDrawable(this, R.drawable.pushup)) ;
        item[0].setText("팔굽혀 펴기") ;

        item[1].setIcon(ContextCompat.getDrawable(this, R.drawable.squat)) ;
        item[1].setText("스쿼트") ;

        item[2].setIcon(ContextCompat.getDrawable(this, R.drawable.plank)) ;
        item[2].setText("플랭크") ;

        item[3].setIcon(ContextCompat.getDrawable(this, R.drawable.lungy)) ;
        item[3].setText("런지") ;

        item[4].setIcon(ContextCompat.getDrawable(this, R.drawable.legraise)) ;
        item[4].setText("레그레이즈") ;

        item[5].setIcon(ContextCompat.getDrawable(this, R.drawable.mountain)) ;
        item[5].setText("마운틴 클라이머") ;

        item[6].setIcon(ContextCompat.getDrawable(this, R.drawable.brigh)) ;
        item[6].setText("브릿지") ;

        item[7].setIcon(ContextCompat.getDrawable(this, R.drawable.deeps)) ;
        item[7].setText("벤치 딥스") ;

        item[8].setIcon(ContextCompat.getDrawable(this, R.drawable.sidehipraise)) ;
        item[8].setText("사이드 힙 레이즈") ;

        item[9].setIcon(ContextCompat.getDrawable(this, R.drawable.briddog)) ;
        item[9].setText("버드 독") ;

        for(int i=0 ; i<num ; i++){
            item[i].setbtnText("자세 비교");
        }
        for(int i=0 ; i<num ; i++){
            item[i].setbtnColor("#000000");
        }
        for(int i=0 ; i<num ; i++){
            list.add(item[i]);
        }

        return true ;
    }
}
