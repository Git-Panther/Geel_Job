package com.example.m_user.geel_job.GamePlay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m_user.geel_job.GamePlay.Ranking.Game_Last_output;
import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.Option.SoundSetting;
import com.example.m_user.geel_job.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by M-user on 2017-06-20.
 */

public class Game_List extends CustomFontActivity { // 게임하기 페이지. 1~3장까지 보여줌.

    SoundPool sound_pool;
    int SoundID;
    SoundSetting sos;

    GameDynamicStatus status = GameDynamicStatus.getInstance(); // 싱글톤 정보 불러오기

    int selectedChapter; // 선택한 챕터 저장.
    CharSequence toastString; // 토스트에 쓸 메시지 저장.
    Toast mainToast; // 토스트 열고 닫는거 자연스럽게 하기 위함.

    public void checkAccessible(String path) // 입장 권한 검사.
    {
        String readStr = ""; // 파일 시스템 사용할려고 만듬
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Geel_job"; // 파일 경로 지정
        File file = new File(sdPath) ;

        file.mkdirs(); // 디렉토리 생성

        FileWriter fw = null;

        sdPath += path; // 파일 경로 지정
        file = new File(sdPath);

        try {
            file.createNewFile(); // 파일 생성
        } catch (IOException ie) {
            ie.printStackTrace();
        }

        try{
            BufferedReader br = new BufferedReader(new FileReader(file)); // 파일 일기 쓸려고 만듬

            String str = null;
            while(((str = br.readLine()) != null)){ // 읽기
                readStr = str;
            }
            br.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        if(readStr == ""){ // 파일 내용 쓸려고 만듬

            if(mainToast != null)
                mainToast.cancel();


            mainToast = Toast.makeText(getApplicationContext(), toastString, Toast.LENGTH_SHORT); // 토스트 생성
            mainToast.getView().setBackgroundResource(R.drawable.toast_cannot_acess);

            final LinearLayout toastLayout = (LinearLayout) mainToast.getView();
            final TextView toastTV = (TextView) toastLayout.getChildAt(0);

            toastTV.setTypeface(Typeface.createFromAsset( getAssets(), "fonts/mn.ttf"));
            toastTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

            mainToast.show();
        }
        else
        {
            sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
            status.setSelectedChapter(selectedChapter); // 선택한 장 정보 스테이터스로 넘기기
            status.startGame();
            status.nextStage(this);
        }
    }

    class NextListener implements View.OnClickListener
    {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f );
            switch (v.getId()) // 어느 장을 선택했냐에 따라 정보 달라짐.
            {
                case R.id.game_main_list_button1:
                    selectedChapter = Game_Status.GAME_CH1;
                    toastString = "1장을 완료하고 오세요!";
                    checkAccessible("/CH1_CLEAR.gji");
                    break;
                case R.id.game_main_list_button2:
                    selectedChapter = Game_Status.GAME_CH2;
                    toastString = "2장을 완료하고 오세요!";
                    checkAccessible("/CH2_CLEAR.gji");
                    break;
                case R.id.game_main_list_button3:
                    selectedChapter = Game_Status.GAME_CH3;
                    toastString = "3장을 완료하고 오세요!";
                    checkAccessible("/CH3_CLEAR.gji");
                    break;
                default:
                    selectedChapter = 0;
                    break;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_list_main);

        sos = SoundSetting.getInstance();
        sound_pool = new SoundPool( 10, AudioManager.STREAM_MUSIC, 0 );
        SoundID = sound_pool.load( Game_List.this, R.raw.button, 1 ); // R.raw.sound에서 "sound"는 사운드 파일명

        ImageView ch1_button = (ImageView)findViewById(R.id.game_main_list_button1);
        ImageView ch2_button = (ImageView)findViewById(R.id.game_main_list_button2);
        ImageView ch3_button = (ImageView)findViewById(R.id.game_main_list_button3);
        ImageView out_button = (ImageView)findViewById(R.id.game_back_button);

        ImageView button_ranking = (ImageView) findViewById(R.id.button_ranking);

        button_ranking.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f );
                        sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
                        startActivity(new Intent(getApplicationContext(), Game_Last_output.class));
                        return true;
                    default:
                        break;
                }

                return false;
            }
        });

        ch1_button.setOnClickListener(new NextListener());
        ch2_button.setOnClickListener(new NextListener());
        ch3_button.setOnClickListener(new NextListener());
        out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f );
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        sos.setBgmContinuous(false); // 시작할 때 false로 해줘야 이후에 일시정지 했을 때 bgm이 멈춘다.
        super.onStart();
    }

    @Override
    protected void onPause() {

        if(sos.getBgmContinuous()) // 배경음을 유지해야 한다면
        {

        }
        else // 배경음을 일시정지 해야한다면.
        {
            sos.getBgm().pause(); // 배경음 일시정지.
        }

        super.onPause();
    }

    @Override
    protected void onResume() {

        if(sos.getBgmContinuous()) // 배경음이 유지 중이라면
        {

        }
        else // 배경음이 멈춘 상태라면.
        {
            sos.getBgm().start(); // 배경음 다시 재생.
        }

        super.onResume();
    }

    public void onBackPressed() { // 취소 버튼 누를 시 액티비티 종료.
//        super.onBackPressed();
        sos.setBgmContinuous(false); // 게임하기 타이틀 끝내기 직전에 false로 돌려 이후에 배경음이 일시정지될 수 있도록 한다.
        sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f );
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sound_pool.release();
    }
}
