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
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.Option.SoundSetting;
import com.example.m_user.geel_job.R;
import com.example.m_user.geel_job.Tutorial.List.Tutorial_List;
import com.example.m_user.geel_job.Tutorial.Storage.TutorialEventStoring;

/**
 * Created by M-user on 2017-08-30.
 * 튜토리얼 처음 실행하면 나오는 화면입니다.
 */

public class Tutorial_event_content extends CustomFontActivity {

    // 우선은 앞으로만 갈 수 있게 막음*

    TutorialEventStoring tes = TutorialEventStoring.getInstance(); // 상태 저장할려고 만듬

    SoundPool sound_pool;
    int SoundID;
    SoundSetting sos; // 효과음 3셋

    int min_page = Tutorial_ch_num.MIN_PAGE;
    int start_page = R.drawable.tutorial_event01; // 첫 시작 페이지*
    int max_page = Tutorial_ch_num.TUTORIAL_EVENT_MAX_PAGE; // 총 페이지*
    int breakpoint_ch = Tutorial_ch_num.TUTORIAL_EVENT_BREAKPOINT_CH; // 브레이크포인트 : 장 선택 창으로*
    int breakpoint_ch_sub = Tutorial_ch_num.TUTORIAL_EVENT_BREAKPOINT_CH_SUB; // 브레이크포인트 : 소단원 선택 창으로*

    LinearLayout layout; // 레이아웃 이름 불러와서 저장할 변수
    ImageView prevButton; // 이전 버튼 불러올 변수
    ImageView nextButton; // 다음 버튼 불러올 변수
    ImageView exitButton; // 나가기 버튼 불러올 변수
    ImageView chButton; // 장 버튼 불러올 변수
    ImageView subButton; // 소단원 버튼 불러올 변수
    TextView curText; // 현재 페이지 불러올 변수
    TextView maxText; // 최대 페이지 불러올 변수


    public void pageSet(int pageNum){ // 실제로 페이지 넘기는 역할 메소드
        if(pageNum >= min_page && pageNum <= max_page) // 범위 이내이면 실행
        {
            detailSet(pageNum);
            layout.setBackgroundResource(start_page + pageNum - 1); // 실제 페이지 - 1을 해야 맞음.*
            curText.setText(String.valueOf(pageNum));
        }
    }

    public void detailSet(int page) // 세밀한 세팅을 위한 메소드
    {
        if(page > min_page) // 최소 페이지보다 크면 뒤로가기 활성화
            prevButton.setVisibility(View.VISIBLE);

        if(page != breakpoint_ch || page != breakpoint_ch_sub) // 버튼 보이게 해야 하는 경우는 보이게 함. 이게 먼저 처리되어야 나중에 가릴 거 다 가림.
        {
            chButton.setClickable(false);
            subButton.setClickable(false);
            nextButton.setVisibility(View.VISIBLE);
            chButton.setVisibility(View.INVISIBLE);
            subButton.setVisibility(View.INVISIBLE);
        }

        if(page == min_page) // 1페이지면 뒤로가기 가리기
            prevButton.setVisibility(View.INVISIBLE);

        if(page >= breakpoint_ch - 2 && page <= breakpoint_ch) { // 5~7이면 장 버튼 보이기
            chButton.setVisibility(View.VISIBLE);
            if (page == breakpoint_ch) // 7페이지면 장 선택 활성화
            {
                nextButton.setVisibility(View.INVISIBLE);
                chButton.setClickable(true);
            }
        }
        if(page >= breakpoint_ch_sub - 1 && page <= breakpoint_ch_sub) { // 8~9이면 소단원 버튼 보이기
            subButton.setVisibility(View.VISIBLE);
            if (page == breakpoint_ch_sub) // 9페이지면 소단원 선택 활성화
            {
                nextButton.setVisibility(View.INVISIBLE);
                subButton.setClickable(true);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_event_content); // 사용 xml

        layout = (LinearLayout) findViewById(R.id.tutorial_content_layout); // 레이아웃 아이디값 가져옴

        prevButton = (ImageView) findViewById(R.id.btnPrev); // 이전 버튼
        nextButton = (ImageView) findViewById(R.id.btnNext); // 다음 버튼
        exitButton = (ImageView) findViewById(R.id.button_game_exit); // 나가기 버튼

        chButton = (ImageView) findViewById(R.id.ch_button); // 이벤트 버튼1 : 챕터 선택
        subButton = (ImageView) findViewById(R.id.ch_sub_button); // 이벤트 버튼2 : 소단원 선택

        curText = (TextView) findViewById(R.id.current_page_text); // 현재 페이지 표시
        maxText = (TextView) findViewById(R.id.max_page_text); // 최대 페이지 표시

        layout.setBackgroundResource(start_page); // 초기 페이지 설정

        prevButton.setVisibility(View.INVISIBLE); // 시작할 때 뒤로가기 버튼 비활성화

        chButton.setClickable(false);
        chButton.setVisibility(View.INVISIBLE); // 시작할 때 챕터 선택 버튼 비활성화

        subButton.setClickable(false);
        subButton.setVisibility(View.INVISIBLE); // 시작할 때 소단원 선택 버튼 비활성화

        SeekBar seek = (SeekBar) findViewById(R.id.bgSeekBar); // 시크바 가리기 위한 용도
        seek.setVisibility(View.INVISIBLE); // 시크바 가리기

        curText.setText(String.valueOf(min_page)); // 시작할 때 1 페이지로 맞추기 위함
        maxText.setText(String.valueOf(max_page)); // 최종 페이지 표시

        sos = SoundSetting.getInstance(); // 효과음 3셋
        sound_pool = new SoundPool( 10, AudioManager.STREAM_MUSIC, 0 );
        SoundID = sound_pool.load( Tutorial_event_content.this, R.raw.button, 1 ); // R.raw.sound에서 "sound"는 사운드 파일명

        chButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 이전 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                tes.setPlusPageNum(1); // 페이지수 변경 , 페이지수 += 1랑 같음
                pageSet(tes.getPageNum()); // 화면 바꾸는 메소드
            }
        });

        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 이전 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                tes.setPlusPageNum(1); // 페이지수 변경 , 페이지수 += 1랑 같음
                pageSet(tes.getPageNum()); // 화면 바꾸는 메소드
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 이전 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                if(tes.getPageNum() > min_page) // 최소값보다 높으면
                {
                    tes.setMinusPageNum(1); // 페이지수 -= 1랑 같음
                    pageSet(tes.getPageNum()); // 화면 바끼는 메소드
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 다음 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                if(tes.getPageNum() >= max_page) { // 마지막 페이지일 경우
                    Intent intent = new Intent(getApplicationContext(), Tutorial_List.class);
                    if(tes.getTutorialEvent()) // 이벤트를 환경설정에서 접하면*
                    {
                        tes.setTutorialEvent(false); // 이벤트가 끝났기 때문에 false로 되돌려 나중에 문제가 생기지 않게 함.*
                    }
                    else // 이벤트를 튜토리얼에서 접하면*
                    {
                        sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
                        startActivity(intent);
                    }

                    tes.setPageNum(Tutorial_ch_num.MIN_PAGE); // 이벤트가 끝났으므로 페이지 1로 초기화*
                    finish();
                }
                else if(tes.getPageNum() < max_page){ // 마지막 페이지가 아닐경우
                    tes.setPlusPageNum(1); // 페이지수 변경 , 페이지수 += 1랑 같음
                    pageSet(tes.getPageNum()); // 화면 바꾸는 메소드
                }
                else
                {

                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 취소 버튼 누를 시
                onBackPressed();
            }
        });
    }

    public void onBackPressed() { // 취소 버튼 누를 시
//        super.onBackPressed();
        sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); //효과음 재생
        LayoutInflater dialog = LayoutInflater.from(Tutorial_event_content.this); // 종료하시겠습니까? 안내창임
        final View dialogLayout = dialog.inflate(R.layout.dialog, null);
        final Dialog myDialog = new Dialog(Tutorial_event_content.this);

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
                tes.setPageNum(Tutorial_ch_num.MIN_PAGE); // 이벤트가 끝났으므로 페이지 1로 초기화*
                myDialog.dismiss();

                if(tes.getTutorialEvent()) // 이벤트를 환경설정에서 접하면*
                {
                    tes.setTutorialEvent(false); // 이벤트가 끝났기 때문에 false로 되돌려 나중에 문제가 생기지 않게 함.*
                }
                else // 이벤트를 튜토리얼에서 접하면*
                {
                    Intent intent = new Intent(getApplicationContext(), Tutorial_List.class);
                    sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
                    startActivity(intent);
                }

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
    protected void onDestroy() { // 현 엑티비티 종료 시
        super.onDestroy();
        sound_pool.release(); // file.close() 같음
    }
}
