package com.example.m_user.geel_job.Tutorial.List;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.Option.SoundSetting;
import com.example.m_user.geel_job.R;
import com.example.m_user.geel_job.Tutorial.Storage.TutorialEventStoring;
import com.example.m_user.geel_job.Tutorial.Tutorial_ch_num;
import com.example.m_user.geel_job.Tutorial.Tutorial_ch_sub_content;

/**
 * Created by M-user on 2017-06-20.
 * 2장 리스트 화면입니다.
 */

public class Tutorial_ch2_list extends CustomFontActivity {

    SoundPool sound_pool;
    int SoundID;
    SoundSetting sos; // 효과음세트들

    TutorialEventStoring tes = TutorialEventStoring.getInstance(); // 상태 저장용*

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_ch2_list); // 사용 xml

        sos = SoundSetting.getInstance(); // 효과음 때문에 따라옴
        sound_pool = new SoundPool( 10, AudioManager.STREAM_MUSIC, 0 );
        SoundID = sound_pool.load( Tutorial_ch2_list.this, R.raw.button, 1 ); // R.raw.sound에서 "sound"는 사운드 파일명

        ImageView sub_button = (ImageView)findViewById(R.id.list_sub2_button1); // 2장 1번 버튼
        ImageView out_button = (ImageView)findViewById(R.id.list_back_button); // 나가기 버튼

        sub_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 1번 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생

                sos.setBgmContinuous(true); // 2-1장을 시작하므로 배경음 유지
                tes.getStatus().setSelected_ch_sub_num(Tutorial_ch_num.CH2_1).setSelectedChapterInfo() // 2-1장 선택 정보 저장
                        .getSelectedChapterInfo().setCur_page(false).setCur_layout(false) // 2-1장 진행 방식 결정. false이므로 1쪽부터 시작
                        .selectTheQuiz(); // OX 퀴즈 무작위 선택
                Intent intent = new Intent(getApplicationContext(),Tutorial_ch_sub_content.class); // Intent 설정 : 공용 튜토리얼 설명
                startActivity(intent);

                // Intent intent = new Intent(getApplicationContext(),Tutorial_ch2_sub1_content.class);

                /*
                tes.setChapterSub(Tutorial_ch_num.CH2_1);
                tes.setCh_sub_maxPage(Tutorial_ch_num.CH2_1_MAX_PAGE);
                tes.setCh_sub_layout(R.drawable.ch2_sub1_page01);
                */

                /*
                tes.getList().add("ch2_sub1_ox1"); // 0번 주소의 저장
                tes.getList().add("ch2_sub1_ox2");
                tes.getList().add("ch2_sub1_ox3"); // 랜덤으로 섞을 리스트(배열같은 존재)
                */
            }
        });

        out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 나가기 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        sos.setBgmContinuous(false); // 시작할 때 false로 해줘야 이후에 일시정지 했을 때 bgm이 멈춘다.

        tes.setActSub(Tutorial_ch2_list.this); // 현재 진행중인 액티비티 저장*

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
        tes.resetAnswer();
        tes.resetStatus();

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
        sound_pool.release(); // file.close() 같음거
    }
}
