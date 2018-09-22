package com.example.m_user.geel_job.GamePlay.Event;

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
import android.widget.TextView;

import com.example.m_user.geel_job.GamePlay.Game_List;
import com.example.m_user.geel_job.GamePlay.Game_Status;
import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.Option.SoundSetting;
import com.example.m_user.geel_job.R;

/**
 * Created by M-user on 2017-08-30.
 * 튜토리얼 처음 실행하면 나오는 화면입니다.
 */

public class Game_event_content extends CustomFontActivity {

    // 우선은 앞으로만 갈 수 있게 막음*

    GameEventStatus status = GameEventStatus.getInstance(); // 상태 저장할려고 만듬

    SoundPool sound_pool;
    int SoundID;
    SoundSetting sos; // 효과음 3셋

    int min_page = Game_Status.MIN_PAGE;
    int start_layout = status.getStartLayout(); // 첫 시작 레이아웃
    int max_page = Game_Status.GAME_EVENT_MAX_PAGE; // 총 페이지*
    int breakpoint_show_select = Game_Status.GAME_EVENT_CHOICE_SHOW; // 브레이크포인트 : 장 이미지를 보여주는 때
    int breakpoint_select = Game_Status.GAME_EVENT_CHOICE_BREAKPOINT; // 브레이크포인트 : 장 선택
    int breakpoint_afterselect = Game_Status.GAME_EVENT_CHOICE_BREAKPOINT + 1; // 추가 브레이크 : 장 선택 이후
    int breakpoint_show_minigame = Game_Status.GAME_EVENT_MINIGAME_SHOW; // 브레이크포인트 : 미니게임 창을 보여줘야 하는 때
    int breakpoint_minigame = Game_Status.GAME_EVENT_MINIGAME_BREAKPOINT; // 브레이크포인트 : 미니게임 플레이하는 때
    int breakpoint_check = Game_Status.GAME_EVENT_MINIGAME_CHECK; // 브레이크포인트 : 미니게임 검사
    int breakpoint_after = Game_Status.GAME_EVENT_AFTERQUIZ;

    int curPage = status.getPage(); // 현재 페이지 첫 불러오기
    int curLayout = status.getStartLayout() + curPage - 1; // 현재 레이아웃 첫 불러오기

    LinearLayout layout; // 레이아웃 이름 불러와서 저장할 변수
    ImageView prevButton; // 이전 버튼 불러올 변수
    ImageView nextButton; // 다음 버튼 불러올 변수
    ImageView exitButton; // 나가기 버튼 불러올 변수
    ImageView[] chButton = new ImageView[3]; // 장 선택 버튼 불러올 변수
    TextView curText; // 현재 페이지 불러올 변수
    TextView maxText; // 최대 페이지 불러올 변수

    ImageView focus; // 강조 이미지 불러올 변수

    public void pageSet(int pageNum){ // 실제로 페이지 넘기는 역할 메소드
        if(pageNum >= min_page && pageNum <= breakpoint_select) // 범위(1~5) 이내이면 실행
        {
            detailSet(pageNum);
            layout.setBackgroundResource(start_layout + pageNum - 1); // 실제 페이지 - 1을 해야 맞음.*
            curText.setText(String.valueOf(pageNum));
        }
    }

    public void toggleView(int visible) // 장 선택 보여주냐 마냐 결정하는 메소드.
    {
        for(int i = 0; i < chButton.length; i++)
        {
            chButton[i].setVisibility(visible);
        }
    }

    public void detailSet(int page) // 세밀한 세팅을 위한 메소드
    {
        if(page > min_page) // 최소 페이지보다 크면 뒤로가기 활성화
            prevButton.setVisibility(View.VISIBLE);

        if(page < breakpoint_show_select) // 장 보여주는 페이지가 아니면 가릴거 가리기
        {
            nextButton.setVisibility(View.VISIBLE);
            toggleView(View.INVISIBLE);
        }

        if(page == min_page) // 1페이지면 뒤로가기 가리기
            prevButton.setVisibility(View.INVISIBLE);

        if(page >= breakpoint_show_select && page <= breakpoint_select) { // 3~5페이지이면 장 버튼 보이기
            toggleView(View.VISIBLE);
            chButton[0].setClickable(false);

            if (page == breakpoint_select) // 5페이지면 장 선택 활성화
            {
                nextButton.setVisibility(View.INVISIBLE);
                chButton[0].setClickable(true);
                focus.setVisibility(View.VISIBLE);
            }
            else // 5페이지가 아니면 앞으로 가는 버튼 활성화 및 포커스 이미지 비활성화
            {
                nextButton.setVisibility(View.VISIBLE);
                focus.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void setDefaultSetting() // 기본 세팅
    {
        layout = (LinearLayout) findViewById(R.id.tutorial_content_layout); // 레이아웃 아이디값 가져옴

        prevButton = (ImageView) findViewById(R.id.btnPrev); // 이전 버튼
        nextButton = (ImageView) findViewById(R.id.btnNext); // 다음 버튼
        exitButton = (ImageView) findViewById(R.id.button_game_exit); // 나가기 버튼

        for(int i = 0; i < chButton.length; i++)
        {
            chButton[i] = (ImageView) findViewById(R.id.button_game_ch1 + i); // 이벤트 버튼 : 장 선택(1장만)
        }

        curText = (TextView) findViewById(R.id.current_page_text); // 현재 페이지 표시
        maxText = (TextView) findViewById(R.id.max_page_text); // 최대 페이지 표시

        focus = (ImageView) findViewById(R.id.focus01); // 이미지 포커스 가져오기

        layout.setBackgroundResource(curLayout); // 초기 페이지 설정

        if(curPage == breakpoint_select) // 뒤로 넘어와서 다시 시작하는 경우(5페이지)
        {
            nextButton.setVisibility(View.INVISIBLE); // 5페이지에서는 장 선택 버튼으로만 넘어가야 하므로 다음 버튼 가림.
            focus.setVisibility(View.VISIBLE);
            toggleView(View.VISIBLE);
        }
        else if (curPage == min_page) // 1페이지에서부터 시작할 경우
        {
            toggleView(View.INVISIBLE); // 시작할 때 챕터 선택 버튼 비활성화

            prevButton.setVisibility(View.INVISIBLE); // 시작할 때 뒤로가기 버튼 비활성화
            chButton[0].setClickable(false); // 1장 선택 버튼 비활성화. 이후 5페이지 오면 이벤트에 의해 true로 설정됨.
            focus.setVisibility(View.INVISIBLE); // 포커스도 비활성화
        }
        else // 예외사항 : 아무 처리 없음
        {

        }

        curText.setText(String.valueOf(curPage)); // 시작할 때 그 페이지로 맞추기 위함
        maxText.setText(String.valueOf(max_page)); // 최종 페이지 표시

        chButton[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 장 선택 버튼 누르면
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                status.setPage(1, true);
                Intent intent = new Intent(getApplicationContext(), Game_event_Game_Play.class);
                sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
                startActivity(intent);
                finish();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 이전 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                if(status.getPage() > min_page) // 최소값보다 높으면
                {
                    pageSet(status.setPage(1, false).getPage()); // 화면 바끼는 메소드
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 다음 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                if(status.getPage() < breakpoint_select) // 최대 페이지보다 작으면
                { // 마지막 페이지가 아닐경우
                    pageSet(status.setPage(1, true).getPage()); // 화면 바꾸는 메소드
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_event_content); // 사용 xml

        setDefaultSetting(); // 기본 세팅.

        sos = SoundSetting.getInstance(); // 효과음 3셋
        sound_pool = new SoundPool( 10, AudioManager.STREAM_MUSIC, 0 );
        SoundID = sound_pool.load( Game_event_content.this, R.raw.button, 1 ); // R.raw.sound에서 "sound"는 사운드 파일명
    }

    public void onBackPressed() { // 취소 버튼 누를 시
//        super.onBackPressed();
        sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f );

        LayoutInflater dialog = LayoutInflater.from(Game_event_content.this); // 종료하시겠습니까? 안내창임
        final View dialogLayout = dialog.inflate(R.layout.dialog, null);
        final Dialog myDialog = new Dialog(Game_event_content.this);

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
                status.resetPage(); // 1페이지로 초기화.
                myDialog.dismiss();

                if(status.getGameEvent()) // 이벤트를 환경설정에서 접하면*
                {
                    status.setGameEvent(false); // 이벤트가 끝났기 때문에 false로 되돌려 나중에 문제가 생기지 않게 함.*
                }
                else // 이벤트를 1-3장에서 접하면 게임하기 페이지로 이동
                {
                    Intent intent = new Intent(getApplicationContext(), Game_List.class);
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
