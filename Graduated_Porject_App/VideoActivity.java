package com.example.aict.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AICT on 2018-03-05.
 */

public class VideoActivity extends AppCompatActivity implements ListViewBtnAdapter.ListBtnClickListener {
    int num = 10;   // 운동항목 개수
    ListViewBtnItem[] item = new ListViewBtnItem[num];  // 항목 만큼의 아이템 클래스의 배열 선언
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        ListView listview;
        ListViewBtnAdapter adapter;
        ArrayList<ListViewBtnItem> items = new ArrayList<ListViewBtnItem>();

        loadItemsFromDB(items); // 리스트 데이터 받아옴

        // 어뎁터 선언
        adapter = new ListViewBtnAdapter(this, R.layout.listview, items, this);

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);

//        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView parent, View v, int position, long id) {
//                // TODO : item click
//            }
//        }) ;

    }
    @Override
    public void onListBtnClick(int position) {
//        Toast.makeText(this, Integer.toString(position+1) , Toast.LENGTH_SHORT).show() ;
        if(position == 0){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=wEvl5akpaG4"));
            startActivity(intent);
        }
        else if(position == 1){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=H3cUcazg7ZY"));
            startActivity(intent);
        }
        else if(position == 2){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=PQqrhpaV_tU"));
            startActivity(intent);
        }
        else if(position == 3){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=xnNKx_M47HY"));
            startActivity(intent);
        }
        else if(position == 4){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=AmsRfTT-OwU"));
            startActivity(intent);
        }
        else if(position == 5){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=Ov98-jhZJf0"));
            startActivity(intent);
        }
        else if(position == 6){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=W-cdrskZHpU"));
            startActivity(intent);
        }
        else if(position == 7){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=XDWhmdaiHz0"));
            startActivity(intent);
        }
        else if(position == 8){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=20tfmqaEUlQ"));
            startActivity(intent);
        }
        else if(position == 9){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=amMMqG3KZ8w"));
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
            item[i].setbtnText("동영상");
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
