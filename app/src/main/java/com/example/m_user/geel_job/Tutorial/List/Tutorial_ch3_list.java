package com.example.m_user.geel_job.Tutorial.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.Option.SoundSetting;
import com.example.m_user.geel_job.R;
import com.example.m_user.geel_job.Tutorial.Storage.TutorialEventStoring;
import com.example.m_user.geel_job.Tutorial.Tutorial_ch_num;
import com.example.m_user.geel_job.Tutorial.Tutorial_ch_sub_content;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by M-user on 2017-06-20.
 * 3장 리스트 화면
 */

public class Tutorial_ch3_list extends CustomFontActivity {

    SoundPool sound_pool;
    int SoundID;
    SoundSetting sos; // 효과음 가족들

    TutorialEventStoring tes = TutorialEventStoring.getInstance(); // 상태 저장용*

    String path = ""; // 권한을 관리하는 파일의 경로
    CharSequence toast = ""; // 누른 장마다 토스트 메시지 출력값이 다르게 하기 위해 그걸 저장하는 변수.

    Toast mainToast; // 토스트 띄우기
    int ch_sub = 0; // 선택한 소단원 정보. 버튼 클릭시 값 초기화.
    Context context; // 각 하위 클래스가 자기 자신의 컨텍스트를 저장할 때 쓰는 변수.

    public void accessible() // 각 장별로 들어갈 권한이 있는지 검사하는 메소드.
    {
        context = Tutorial_ch3_list.this; // 각 하위 클래스가 자기 자신의 컨텍스트를 저장할 때 쓰는 변수.

        String readStr = ""; // 파일 시스템 사용할려고 만듬

        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Geel_job"; // 파일 경로 지정
        File file = new File(sdPath) ;

        file.mkdirs(); // 디렉토리 생성

        sdPath += path; // 파일 경로 지정
        file = new File(sdPath);
        //FileWriter fw = null;
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

                /*
                try {
                    // open file.
                    String text = "1";
                    fw = new FileWriter(file) ;

                    // write file.
                    fw.write(text) ; // 쓰기
                    fw.close();
                } catch (Exception e) {
                    e.printStackTrace() ;
                }
                Intent intent = new Intent(getApplicationContext(), Game_event_content.class); // 맨 처음일 경우 설명
                startActivity(intent);
                */

            if(mainToast != null)
                mainToast.cancel();

            mainToast = Toast.makeText(context, toast, Toast.LENGTH_SHORT);

            mainToast.getView().setBackgroundResource(R.drawable.toast_cannot_acess);

            final LinearLayout toastLayout = (LinearLayout) mainToast.getView();
            final TextView toastTV = (TextView) toastLayout.getChildAt(0);

            toastTV.setTypeface(Typeface.createFromAsset( getAssets(), "fonts/mn.ttf"));
            toastTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

            mainToast.show(); // 경고문
        }
        else
        {
            try
            { // 장 검사.

                Intent intent = new Intent(getApplicationContext(), Tutorial_ch_sub_content.class); // 소단원 출력; // 다음 장으로 넘어가기 위한 정보 저장

                /*
                switch (path) {
                    case "/CH1_1_CLEAR.gji":
                        ch_sub = Tutorial_ch_num.CH1_2;
                        Log.d("소단원 선택 성공!", "값은 "+ch_sub); // 장 찾기 성공
                        break;
                    case "/CH1_2_CLEAR.gji":
                        ch_sub = Tutorial_ch_num.CH1_3;
                        Log.d("소단원 선택 성공!", "값은 "+ch_sub); // 장 찾기 성공
                        break;
                    default: // 해당사항 없으면.
                        ch_sub = 0;
                        intent = null;
                        break;
                }
                */

                sos.setBgmContinuous(true); // 다른 액티비티를 시작하므로 배경음 유지
                tes.getStatus().setSelected_ch_sub_num(ch_sub).setSelectedChapterInfo() // m-n장 선택 정보 저장
                        .getSelectedChapterInfo().setCur_page(false).setCur_layout(false) // m-n장 진행 방식 결정. false이므로 1쪽부터 시작
                        .selectTheQuiz(); // OX 퀴즈 무작위 선택
                startActivity(intent);
                Log.d("소단원 선택 성공!", "값은 "+ch_sub); // 장 찾기 성공
            }
            catch (NullPointerException e)
            {
                e.printStackTrace();
                Log.d("소단원 선택 실패", "어느 장인지 알 수 없습니다."); // 장 찾기 실패.
                finish();
            }

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_ch3_list); // 사용 xml

        sos = SoundSetting.getInstance(); // 효과음 설정
        sound_pool = new SoundPool( 10, AudioManager.STREAM_MUSIC, 0 );
        SoundID = sound_pool.load( Tutorial_ch3_list.this, R.raw.button, 1 ); // R.raw.sound에서 "sound"는 사운드 파일명

        ImageView sub1 = (ImageView)findViewById(R.id.list_sub3_button1);
        ImageView sub2 = (ImageView)findViewById(R.id.list_sub3_button2);
        ImageView sub3 = (ImageView)findViewById(R.id.list_sub3_button3);
        ImageView sub4 = (ImageView)findViewById(R.id.list_sub3_button4);
        ImageView sub5 = (ImageView)findViewById(R.id.list_sub3_button5);
        ImageView sub6 = (ImageView)findViewById(R.id.list_sub3_button6);
        ImageView back_button = (ImageView)findViewById(R.id.list_back_button); // 나가기 버튼입니다. 위에 있는거 1번부터 5번까지 버튼

        sub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 3-1장 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f );
                sos.setBgmContinuous(true); // 3-1장을 시작하므로 배경음 유지
                tes.getStatus().setSelected_ch_sub_num(Tutorial_ch_num.CH3_1).setSelectedChapterInfo() // 3-1장 선택 정보 저장
                        .getSelectedChapterInfo().setCur_page(false).setCur_layout(false) // 3-1장 진행 방식 결정. false이므로 1쪽부터 시작
                        .selectTheQuiz(); // OX 퀴즈 무작위 선택
                Intent intent = new Intent(getApplicationContext(),Tutorial_ch_sub_content.class); // Intent 설정 : 공용 튜토리얼 설명
                startActivity(intent);
            }
        });
        sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// 3-2장 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f );
                path = "/CH3_1_CLEAR.gji";
                ch_sub = Tutorial_ch_num.CH3_2;
                toast = "3장 1단원을 완료하고 오세요!";
                accessible();
            }
        });
        sub3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// 3-3장 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f );
                path = "/CH3_2_CLEAR.gji";
                ch_sub = Tutorial_ch_num.CH3_3;
                toast = "3장 2단원을 완료하고 오세요!";
                accessible();
            }
        });
        sub4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// 3-4장 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f );
                path = "/CH3_3_CLEAR.gji";
                ch_sub = Tutorial_ch_num.CH3_4;
                toast = "3장 3단원을 완료하고 오세요!";
                accessible();
            }
        });

        sub5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// 5번 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f );
                path = "/CH3_4_CLEAR.gji";
                ch_sub = Tutorial_ch_num.CH3_5;
                toast = "3장 4단원을 완료하고 오세요!";
                accessible();
            }
        });

        sub6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// 6번 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f );
                path = "/CH3_5_CLEAR.gji";
                ch_sub = Tutorial_ch_num.CH3_6;
                toast = "3장 5단원을 완료하고 오세요!";
                accessible();
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 나가기 버튼 누를 시
                onBackPressed();
            }
        });


    }

    @Override
    protected void onStart() {
        sos.setBgmContinuous(false); // 시작할 때 false로 해줘야 이후에 일시정지 했을 때 bgm이 멈춘다.

        tes.setActSub(Tutorial_ch3_list.this); // 현재 진행중인 액티비티 저장*

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

        // 초기화
        tes.resetStatus();
        tes.resetAnswer();

        if(sos.getBgmContinuous()) // 배경음이 유지 중이라면
        {

        }
        else // 배경음이 멈춘 상태라면.
        {
            sos.getBgm().start(); // 배경음 다시 재생.
        }

        super.onResume();
    }

    public void onBackPressed() { // 취소 버튼 누를 시
//        super.onBackPressed();
        sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
        finish();
    }

    @Override
    protected void onDestroy() { // 현 엑티비티 종료 시
        super.onDestroy();
        sound_pool.release();
    }
}
