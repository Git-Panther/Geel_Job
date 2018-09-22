package com.example.m_user.geel_job.Tutorial.List;

import android.content.Context;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.Option.SoundSetting;
import com.example.m_user.geel_job.Tutorial.Storage.TutorialEventStoring;
import com.example.m_user.geel_job.Tutorial.Tutorial_ch_sub_content;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by They on 2017-11-19.
 */

public class Tutorial_Ch_List extends CustomFontActivity { // 튜토리얼의 각 장별로 소단원 선택 페이지 부모 클래스. 지금은 쓰지 않음.
    SoundPool sound_pool;
    int SoundID;
    SoundSetting sos = SoundSetting.getInstance(); // 효과음

    TutorialEventStoring tes = TutorialEventStoring.getInstance(); // 상태 저장용*

    String path = ""; // 권한을 관리하는 파일의 경로
    CharSequence toast = ""; // 누른 장마다 토스트 메시지 출력값이 다르게 하기 위해 그걸 저장하는 변수.
    Context context = getApplicationContext(); // 각 하위 클래스가 자기 자신의 컨텍스트를 저장할 때 쓰는 변수.
    int ch_sub = 0; // 선택한 소단원 정보. 버튼 클릭시 값 초기화.

    LinkedList<Button> ch_sub_buttons = new LinkedList<>(); // 소단원 선택 버튼 id 가져와 저장. 보이지 않는 버튼.
    LinkedList<ImageView> ch_sub_Images = new LinkedList<>(); // 소단원 선택 버튼 이미지 표시 id 가져와 저장.
    ImageView exitButton; // 나가기 버튼
    ImageView title; // 각 장의 타이틀

    public void accessible() // 각 장별로 들어갈 권한이 있는지 검사하는 메소드.
    {
        String readStr = ""; // 파일 시스템 사용할려고 만듬

        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Geel_job"; // 파일 경로 지정
        File file = new File(sdPath) ;

        if(!file.exists()) // 디렉터리가 없으면
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

            Toast.makeText(context, toast, Toast.LENGTH_SHORT).show(); // 경고문
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
                tes.getStatus().setSelected_ch_sub_num(ch_sub).setSelectedChapterInfo() // 1-n장 선택 정보 저장
                        .getSelectedChapterInfo().setCur_page(false).setCur_layout(false) // 1-n장 진행 방식 결정. false이므로 1쪽부터 시작
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

    public void onBackPressed() { // 취소 버튼 누를 시
//        super.onBackPressed();
        sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f );
        finish();

    }

    @Override
    protected void onDestroy() { // 현 엑티비티 종료 시
        super.onDestroy();
        sound_pool.release(); // file.close() 같음
    }
}
