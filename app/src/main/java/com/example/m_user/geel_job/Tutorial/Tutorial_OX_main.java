package com.example.m_user.geel_job.Tutorial;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.Option.SoundSetting;
import com.example.m_user.geel_job.R;
import com.example.m_user.geel_job.Tutorial.Storage.TutorialEventStoring;

/**
 * Created by M-user on 2017-09-13.
 * OX퀴즈를 하는 곳입니다.
 * 클리어 번호 성공:1 실패:2 시간끝:3으로 설정 TutorialEventStoring에 저장
 */

public class Tutorial_OX_main extends CustomFontActivity {
    TutorialEventStoring tes = TutorialEventStoring.getInstance(); // 상태 불러오기

    LinearLayout layout;  // 배경 설정을 위해 레이아웃 객체 가져올 변수
    boolean buttonO; // O버튼 정답, 오답 여부 저장
    boolean buttonX; // X버튼 정답, 오답 여부 저장
    int hint; // 힌트 이미지 저장 변수

    // 이하는 사운드 객체 사용
    SoundPool sound_pool;
    int SoundID;
    SoundSetting sos;

    public void setDefaultSetting() // onCreate() 호출과 동시에 기본적인 세팅을 하기 위한 메소드. 여기에 초기값들을 설정하는 요소들이 모여있다. 객체보단 변수 값 설정에 중심을 두었다.
    {
        layout = (LinearLayout) findViewById(R.id.turorial_ox_layout); // 레이아웃 아이디값 저장
        setPage(); // OX 페이지 설정.

        sos = SoundSetting.getInstance();
        sound_pool = new SoundPool( 10, AudioManager.STREAM_MUSIC, 0 );
        SoundID = sound_pool.load( Tutorial_OX_main.this, R.raw.button, 1 ); // R.raw.sound에서 "sound"는 사운드 파일명
    }

    public void setPage(){ // OX 페이지 설정 메소드
        layout.setBackgroundResource(tes.getStatus().getSelectedChapterInfo().getSelectedQuiz().getQuiz_layout()); // 배경 이미지 설정
        buttonO = tes.getStatus().getSelectedChapterInfo().getSelectedQuiz().getQuizButtonO(); // O 버튼 정오답 여부 설정
        buttonX = tes.getStatus().getSelectedChapterInfo().getSelectedQuiz().getQuizButtonX(); // X 버튼 정오답 여부 설정
        hint = tes.getStatus().getSelectedChapterInfo().getSelectedQuiz().getHint();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_ox_main); // 사용 xml

        setDefaultSetting(); // 기본 세팅 시작

        final ImageView trueButton = (ImageView) findViewById(R.id.ox_button_true);
        final ImageView falseButton = (ImageView) findViewById(R.id.ox_button_false);
        final ImageView hintButton = (ImageView) findViewById(R.id.ox_button_hint);

        ImageView exitButton = (ImageView) findViewById(R.id.button_game_exit); // 나가기 버튼
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 나가기 버튼 누를 시
                onBackPressed();
            }
        });

        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // O버튼 누를 경우
                sos.setBgmContinuous(true); // 채점 결과창으로 이동하므로 배경음 유지
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                // tes.setClear_check(leftNum); // 클리어 상태 설정 성공:1 실패:2 시간끝:3
                tes.setAnswer(buttonO);
                Intent intent = new Intent(getApplicationContext(),Tutorial_After_OX.class);
                startActivity(intent);
                finish();

            }
        });

        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // X버튼 누를 경우
                sos.setBgmContinuous(true); // 채점 결과창으로 이동하므로 배경음 유지
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                // tes.setClear_check(rightNum); // 클리어 상태 설정 성공:1 실패:2 시간끝:3
                tes.setAnswer(buttonX);
                Intent intent = new Intent(getApplicationContext(),Tutorial_After_OX.class);
                startActivity(intent);
                finish();

            }
        });

        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 힌트 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                hintButton.setImageResource(hint); // 이미지 변경
                hintButton.setClickable(false); // 한 번만 누를 수 있음.*
            }
        });
    }

    public void onBackPressed() { // 취소 버튼 누를 시
//        super.onBackPressed();
        sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); //효과음 재생
        LayoutInflater dialog = LayoutInflater.from(Tutorial_OX_main.this); // 종료하시겠습니까? 안내창임
        final View dialogLayout = dialog.inflate(R.layout.dialog, null);
        final Dialog myDialog = new Dialog(Tutorial_OX_main.this);

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setContentView(dialogLayout);
        myDialog.show();

        ImageView btn_ok = (ImageView)dialogLayout.findViewById(R.id.btn_ok);
        ImageView btn_cancel = (ImageView)dialogLayout.findViewById(R.id.btn_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            { // 확인을 눌렀을 경우 밑에는 다 초기화 작업
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f );
                tes.getStatus().resetSelectedChapterInfo();
                myDialog.dismiss();
                finish();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            { // 취소 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f );
                myDialog.cancel();
            }
        });
    }

    @Override
    protected void onStart() {
        sos.setBgmContinuous(false); // 시작할 때 false로 해줘야 이후에 일시정지 했을 때 bgm이 멈춘다.
        tes.resetAnswer(); // 선택한 정보 초기화
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

    @Override
    protected void onDestroy() { // 현 엑티비티 종료시
        super.onDestroy();
        sound_pool.release();
    }
}
