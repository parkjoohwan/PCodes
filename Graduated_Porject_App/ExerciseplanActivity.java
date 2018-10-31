package com.example.aict.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by AICT on 2018-03-05.
 */

public class ExerciseplanActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    public static int SUNDAY        = 1;
    public static int MONDAY        = 2;
    public static int TUESDAY       = 3;
    public static int WEDNSESDAY    = 4;
    public static int THURSDAY      = 5;
    public static int FRIDAY        = 6;
    public static int SATURDAY      = 7;

    private TextView mTvCalendarTitle;
    private GridView mGvCalendar;

    private ArrayList<DayInfo> mDayList;
    private CalendarAdapter mCalendarAdapter;

    Calendar mLastMonthCalendar;
    Calendar mThisMonthCalendar;
    Calendar mNextMonthCalendar;

    int width;  // 그리드 뷰 넓이
    int height; // 그리드 뷰 높이

    DayInfo day[] = new DayInfo[42]; // day의 개수만큼 선언

    double colorpersent;  // 완료한 리스트의 개수의 퍼센트

    int pos; // 클릭한 그리드 뷰 위치
    String color = "#FFFFFF";

    // 색깔 주는 배열
    String gridcolor[][][] = new String[100][12][42];
    // 다시 클릭했을 때 선택 항목들 표시되게 하는것
    int choosevaluse[][][][] = new int[100][12][42][100];;
    int first_year = 2000; // 기준시작 년도

    String user_id;

    private DBHelper helper;
    private SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        Button bLastMonth = (Button)findViewById(R.id.gv_calendar_activity_b_last);
        Button bNextMonth = (Button)findViewById(R.id.gv_calendar_activity_b_next);

        mTvCalendarTitle = (TextView)findViewById(R.id.gv_calendar_activity_tv_title);
        mGvCalendar = (GridView)findViewById(R.id.gv_calendar_activity_gv_calendar);

        // SQLiteOpenHelper 클래스의 subclass인 DBHelper 클래스 객체 생성
        helper = new DBHelper(this);
        // DBHelper 객체를 이용하여 DB 생성
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException e) {
            db = helper.getReadableDatabase();
        }

        Intent getintent = getIntent();
        user_id = getintent.getStringExtra("user_id");

        // 처음 시작할 때 그리드 뷰 바탕색 흰색으로 지정
        for(int k=0; k<100 ; k++) {
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 42; j++) {
                    gridcolor[k][i][j] = "#FFFFFF";
                }
            }
        }

        for(int u=0 ; u<100 ; u++){
            for(int k=0; k<12 ; k++) {
                for (int i = 0; i < 42; i++) {
                    for (int j = 0; j < 100; j++) {
                        choosevaluse[u][k][i][j] = 1;
                    }
                }
            }
        }


        for(int i = 0 ; i<1000 ; i++){
            Cursor cursor;
            cursor = db.rawQuery("SELECT year, month, day, color FROM calendar WHERE id='"+user_id+"' AND _id= "+i+";",null);
            if(cursor.getCount() != 0){
                int year;
                int month;
                int day;
                String color;
                while(cursor.moveToNext()){
                    year = cursor.getInt(0);
                    month = cursor.getInt(1);
                    day = cursor.getInt(2);
                    color = cursor.getString(3);
                    gridcolor[year][month][day] = color;
                }
            }
            cursor.close();

            Cursor cursor1;
            cursor1 = db.rawQuery("SELECT year, month, day, pos, value FROM completestate WHERE id='"+user_id+"' AND _id= "+i+";",null);
            if(cursor1.getCount() != 0){
                int year;
                int month;
                int day;
                int pos;
                int value;
                while(cursor1.moveToNext()){
                    year = cursor1.getInt(0);
                    month = cursor1.getInt(1);
                    day = cursor1.getInt(2);
                    pos = cursor1.getInt(3);
                    value = cursor1.getInt(4);
                    choosevaluse[year][month][day][pos] = value;
                }
            }
            cursor1.close();
        }


        // 처음 액티비티가 실행될 때 gridview의 크기를 가져오지 못하는 문제가 있어 adapter에 숫자로 일단 넣어놓음
        // 뷰의 높이와 넓이를 가져오는 두번째 방법
//        // ViewTree의 뷰가 그려질 때마다
//        mGvCalendar.getViewTreeObserver()
//                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//
//                        //뷰의 생성된 후 크기와 위치 구하기
//                        width = mGvCalendar.getWidth();
//                        height = mGvCalendar.getHeight();
//
//
//                        //리스너 해제
//                        mGvCalendar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    }
//                });




        bLastMonth.setOnClickListener(this);
        bNextMonth.setOnClickListener(this);
        mGvCalendar.setOnItemClickListener(this);

        mDayList = new ArrayList<DayInfo>();


    }
        // 뷰의 높이와 넓이를 가져오는 첫번째 방법
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus){
//        mGvCalendar = (GridView)findViewById(R.id.gv_calendar_activity_gv_calendar);
//        width = mGvCalendar.getWidth();
//        height = mGvCalendar.getHeight();
//
//    }


    @Override
    protected void onResume()
    {
        super.onResume();

        // 이번달 의 캘린더 인스턴스를 생성한다.
        mThisMonthCalendar = Calendar.getInstance();
        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        getCalendar(mThisMonthCalendar);
    }

    /**
     * 달력을 셋팅한다.
     *
     * @param calendar 달력에 보여지는 이번달의 Calendar 객체
     */
    private void getCalendar(Calendar calendar)
    {
        int lastMonthStartDay;
        int dayOfMonth;
        int thisMonthLastDay;

        mDayList.clear();

        // 이번달 시작일의 요일을 구한다. 시작일이 일요일인 경우 인덱스를 1(일요일)에서 8(다음주 일요일)로 바꾼다.)
        dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, -1);
        Log.e("지난달 마지막일", calendar.get(Calendar.DAY_OF_MONTH)+"");

        // 지난달의 마지막 일자를 구한다.
        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, 1);
        Log.e("이번달 시작일", calendar.get(Calendar.DAY_OF_MONTH)+"");

        if(dayOfMonth == SUNDAY)
        {
            dayOfMonth += 7;
        }

        lastMonthStartDay -= (dayOfMonth-1)-1;


        // 캘린더 타이틀(년월 표시)을 세팅한다.
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");


        Log.e("DayOfMOnth", dayOfMonth+"");

        // 전 달의 일수 나타내는거
        for(int i=0; i<dayOfMonth-1; i++)
        {
            int date = lastMonthStartDay+i;
            day[i] = new DayInfo();
            day[i].setDay(Integer.toString(date));
            day[i].setInMonth(false);
            day[i].setcolor("#FFFFFF");

            mDayList.add(day[i]);
        }
        // 이번달 일 수 나타냄
        for(int i=1; i <= thisMonthLastDay; i++)
        {
            day[dayOfMonth-1+i-1] = new DayInfo();
            day[dayOfMonth-1+i-1].setDay(Integer.toString(i));
            day[dayOfMonth-1+i-1].setInMonth(true);
            day[dayOfMonth-1+i-1].setcolor("#FFFFFF");

            mDayList.add(day[dayOfMonth-1+i-1]);
        }
        // 다음달 일 수 나타냄
        for(int i=1; i<42-(thisMonthLastDay+dayOfMonth-1)+1; i++)
        {
            day[dayOfMonth-1+thisMonthLastDay+i-1] = new DayInfo();
            day[dayOfMonth-1+thisMonthLastDay+i-1].setDay(Integer.toString(i));
            day[dayOfMonth-1+thisMonthLastDay+i-1].setInMonth(false);
            day[dayOfMonth-1+thisMonthLastDay+i-1].setcolor("#FFFFFF");
            mDayList.add(day[dayOfMonth-1+thisMonthLastDay+i-1]);
        }

        // 컬러 셋팅
        for(int i=0 ; i<42 ; i++){
            day[i].setcolor(gridcolor[mThisMonthCalendar.get(Calendar.YEAR)-first_year][mThisMonthCalendar.get(Calendar.MONTH)][i]);
        }

        initCalendarAdapter();
    }

    /**
     * 지난달의 Calendar 객체를 반환합니다.
     *
     * @param calendar
     * @return LastMonthCalendar
     */
    private Calendar getLastMonth(Calendar calendar)
    {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, -1);
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;
    }

    /**
     * 다음달의 Calendar 객체를 반환합니다.
     *
     * @param calendar
     * @return NextMonthCalendar
     */
    private Calendar getNextMonth(Calendar calendar)
    {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, +1);
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "년 "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1) + "월");
        return calendar;
    }
    // 달력 클릭시
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long arg3)
    {
        pos = position;
        // 오늘 날짜 구하기
        String getTime; // 오늘 날짜
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        getTime = sdf.format(date);
        final int num_get = Integer.parseInt(getTime);  // 정수형으로 바꿔서 저장

        // 달력의 클릭한 날짜 구하기
        day[position] = mDayList.get(position);
        String Today_month;
        // 월 : month+1은 시스템에서 달이 -1되서 해줌, 0이 안붙어서 예외처리
                if((mThisMonthCalendar.get(Calendar.MONTH) + 1) == 1 || (mThisMonthCalendar.get(Calendar.MONTH) + 1) == 2 ||
                        (mThisMonthCalendar.get(Calendar.MONTH) + 1) == 3 || (mThisMonthCalendar.get(Calendar.MONTH) + 1) == 4 ||
                        (mThisMonthCalendar.get(Calendar.MONTH) + 1) == 5 || (mThisMonthCalendar.get(Calendar.MONTH) + 1) == 6 ||
                        (mThisMonthCalendar.get(Calendar.MONTH) + 1) == 7 || (mThisMonthCalendar.get(Calendar.MONTH) + 1) == 8 ||
                        (mThisMonthCalendar.get(Calendar.MONTH) + 1) == 9){
                    Today_month = "0" + Integer.toString(mThisMonthCalendar.get(Calendar.MONTH) + 1);
                }
                else{
                    Today_month =  Integer.toString(mThisMonthCalendar.get(Calendar.MONTH) + 1);
                }
        // 클릭한 날짜
        String Today = Integer.toString(mThisMonthCalendar.get(Calendar.YEAR))+Today_month+day[position].getDay();

//        // 오늘의 날짜와 클릭한 달력의 날짜가 같으면 다음 액티비티 실행
//        if(Integer.parseInt(Today) == Integer.parseInt(getTime)){
//            Intent intent = new Intent(this, ExerciseplanActivity_second.class);
//            intent.putExtra("buttonchoose",choosevaluse[mThisMonthCalendar.get(Calendar.YEAR)-first_year][mThisMonthCalendar.get(Calendar.MONTH)][pos]);
//            startActivityForResult(intent, 1);
//        }
//        else {
//            Toast.makeText(this, "오늘 날짜를 클릭해주세요." ,Toast.LENGTH_LONG).show();
//        }
        Intent intent = new Intent(this, ExerciseplanActivity_second.class);
        intent.putExtra("buttonchoose",choosevaluse[mThisMonthCalendar.get(Calendar.YEAR)-first_year][mThisMonthCalendar.get(Calendar.MONTH)][pos]);
        intent.putExtra("user_id", user_id);
        startActivityForResult(intent, 1);
    }
    // ExerciseplanActivity_second에서 받아온 값 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null){
            colorpersent = data.getDoubleExtra("colorpersent",0);   // 퍼센트 값 받아옴
            choosevaluse[mThisMonthCalendar.get(Calendar.YEAR)-first_year][mThisMonthCalendar.get(Calendar.MONTH)][pos] = data.getIntArrayExtra("choose"); // 어떤 버튼이 눌렀는지 가져옴

//            if(colorpersent < 0.25){
//                color = "#FF0000";  // 빨강
//            }
//            else if(0.25 <= colorpersent && colorpersent < 0.5){
//                color = "#FF8224";  // 주황
//            }
//            else if(0.5 <= colorpersent && colorpersent < 0.75){
//                color = "#FFF612";  // 노랑
//            }
//            else if(0.75 <= colorpersent && colorpersent <= 1){
//                color = "#1DDB16";  // 초록
//            }
            if(colorpersent < 0.25){
                gridcolor[mThisMonthCalendar.get(Calendar.YEAR)-first_year][mThisMonthCalendar.get(Calendar.MONTH)][pos] = "#FF0000"; // 빨강
            }
            else if(0.25 <= colorpersent && colorpersent < 0.5){
                gridcolor[mThisMonthCalendar.get(Calendar.YEAR)-first_year][mThisMonthCalendar.get(Calendar.MONTH)][pos] = "#FF8224"; // 주황
            }
            else if(0.5 <= colorpersent && colorpersent < 0.75){
                gridcolor[mThisMonthCalendar.get(Calendar.YEAR)-first_year][mThisMonthCalendar.get(Calendar.MONTH)][pos] = "#FFF612";  // 노랑
            }
            else if(0.75 <= colorpersent && colorpersent <= 1){
                gridcolor[mThisMonthCalendar.get(Calendar.YEAR)-first_year][mThisMonthCalendar.get(Calendar.MONTH)][pos] = "#1DDB16";  // 초록
            }
            int year = mThisMonthCalendar.get(Calendar.YEAR)-first_year;
            int month = mThisMonthCalendar.get(Calendar.MONTH);
            int day = pos;
            String color = gridcolor[mThisMonthCalendar.get(Calendar.YEAR)-first_year][mThisMonthCalendar.get(Calendar.MONTH)][pos];

            db.execSQL("INSERT INTO calendar VALUES (null, '"+user_id+"', '"+year+"', '"+month+"', '"+day+"', '"+color+"')");
            for(int i=0 ; i< 7 ; i++){
                db.execSQL("INSERT INTO completestate VALUES (null, '"+user_id+"', '"+year+"', '"+month+"', '"+day+"', '"+i+"', '"+choosevaluse[year][month][day][i]+"')");
            }

        }else if(resultCode == RESULT_CANCELED){

        }
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.gv_calendar_activity_b_last:
                mThisMonthCalendar = getLastMonth(mThisMonthCalendar);
                getCalendar(mThisMonthCalendar);
                break;
            case R.id.gv_calendar_activity_b_next:
                mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
                getCalendar(mThisMonthCalendar);
                break;
        }
    }

    private void initCalendarAdapter()
    {   // 넓이와 높이를 보냄
        mCalendarAdapter = new CalendarAdapter(this, R.layout.day, mDayList, width, height);
        mGvCalendar.setAdapter(mCalendarAdapter);
    }

}






