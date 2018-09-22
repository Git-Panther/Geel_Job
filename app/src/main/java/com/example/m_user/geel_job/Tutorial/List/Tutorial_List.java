package com.example.m_user.geel_job.Tutorial.List;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/**
 * Created by M-user on 2017-06-05.
 * 1,2,3장 선택하는 화면입니다.
 */

public class Tutorial_List extends CustomFontActivity {
    // 튜토리얼의 장 리스트들을 보여주는 페이지.

    SoundPool sound_pool;
    int SoundID;
    SoundSetting sos; // 효과음 쓸려고

    TutorialEventStoring tes = TutorialEventStoring.getInstance(); // 상태 저장용. 여기서는 진행중인 장을 저장하기 위함.*
    String path = ""; // 권한을 관리하는 파일의 경로
    CharSequence toast = ""; // 누른 장마다 토스트 메시지 출력값이 다르게 하기 위해 그걸 저장하는 변수.
    Toast mainToast; // 토스트 띄우기

    public void accessible() // 각 장별로 들어갈 권한이 있는지 검사하는 메소드.
    {
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

            mainToast = Toast.makeText(Tutorial_List.this, toast, Toast.LENGTH_SHORT);

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

                int ch; // 선택한 장을 저장할 변수.
                Intent intent; // 다음 장으로 넘어가기 위한 정보 저장

                switch (path) {
                    case "/CH1_CLEAR.gji":
                        ch = Tutorial_ch_num.CH2;
                        intent = new Intent(getApplicationContext(), Tutorial_ch2_list.class); // 2장 리스트 화면 넘김
                        Log.d("장 선택 성공!", "값은 "+ch); // 장 찾기 성공.
                        break;
                    case "/CH2_CLEAR.gji":
                        ch = Tutorial_ch_num.CH3;
                        intent = new Intent(getApplicationContext(), Tutorial_ch3_list.class); // 2장 리스트 화면 넘김
                        Log.d("장 선택 성공!", "값은 "+ch); // 장 찾기 성공.
                        break;
                    default: // 해당사항 없으면.
                        ch = 0;
                        intent = null;
                        break;
                }

                sos.setBgmContinuous(true); // 2장을 시작하므로 배경음 유지
                tes.getStatus().setSelected_ch_num(ch); // n장 정보 선택 저장
                startActivity(intent);
            }
            catch (NullPointerException e)
            {
                e.printStackTrace();
                Log.d("장 선택 실패", "어느 장인지 알 수 없습니다."); // 장 찾기 실패.
                finish();
            }

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_list_main); //  res/layout에 있는 XML파일

        sos = SoundSetting.getInstance(); // 효과음 셋팅
        sound_pool = new SoundPool( 10, AudioManager.STREAM_MUSIC, 0 );
        SoundID = sound_pool.load( Tutorial_List.this, R.raw.button, 1 ); // R.raw.sound에서 "sound"는 사운드 파일명

        ImageView ch1_button = (ImageView)findViewById(R.id.img1); // 1장 버튼
        ImageView ch2_button = (ImageView)findViewById(R.id.img2); // 2장 버튼
        ImageView ch3_button = (ImageView)findViewById(R.id.img3); // 3장 버튼
        ImageView out_button = (ImageView)findViewById(R.id.img4); // 나가기 버튼

        ch1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 1장 버튼 누를 경우
                sos.setBgmContinuous(true); // 1장을 시작하므로 배경음 유지
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                tes.getStatus().setSelected_ch_num(Tutorial_ch_num.CH1); // 1장 선택 정보 저장
                Intent intent = new Intent(getApplicationContext(),Tutorial_ch1_list.class); // 1장 리스트 화면 넘김
                startActivity(intent);
            }
        });
        ch2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 2장 버튼 누를 경우
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f );
                path = "/CH1_CLEAR.gji"; // 1장 클리어 여부를 따져야 한다.
                toast = "1장을 완료하고 오세요!";
                accessible();
            }
        });
        ch3_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 3장 버튼 누를 경우
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f );
                path = "/CH2_CLEAR.gji"; // 1장 클리어 여부를 따져야 한다.
                toast = "2장을 완료하고 오세요!";
                accessible();
            }
        });
        out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 나가기 버튼 누를 경우
                onBackPressed();
            }
        });
    }

    public void onBackPressed() { // 취소 버튼 누를 시 튜토리얼 창 종료
//        super.onBackPressed();
        //sos.setBgmContinuous(false); // 튜토리얼 끝내기 직전에 false로 돌려 이후에 배경음이 일시정지될 수 있도록 한다.
        sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
        finish();

    }

    @Override
    protected void onStart() {
        sos.setBgmContinuous(false); // 시작할 때 false로 해줘야 이후에 일시정지 했을 때 bgm이 멈춘다.

        tes.setAct(Tutorial_List.this); // 현재 진행중인 액티비티 저장*

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
        tes.resetActSub();

        if(sos.getBgmContinuous()) // 배경음이 유지 중이라면
        {

        }
        else // 배경음이 멈춘 상태라면.
        {
            sos.getBgm().start(); // 배경음 다시 재생.
        }

        super.onResume();
    }

    @Override
    protected void onDestroy() { // 엑티비티 종료시
        super.onDestroy();
        sound_pool.release();
    }
}
