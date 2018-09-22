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
import com.example.m_user.geel_job.Tutorial.Storage.TutorialEventStoring;

/**
 * Created by M-user on 2017-08-30.
 * 튜토리얼 처음 실행하면 나오는 화면입니다.
 */

public class Game_event_AfterGame extends CustomFontActivity {

    // 우선은 앞으로만 갈 수 있게 막음*

    GameEventStatus status = GameEventStatus.getInstance(); // 게임하기 이벤트 상태 불러오기
    TutorialEventStoring tes = TutorialEventStoring.getInstance(); // 튜토리얼 정보 불러옴. 왜냐하면

    SoundPool sound_pool;
    int SoundID;
    SoundSetting sos; // 효과음 3셋

    int check_page = Game_Status.GAME_EVENT_MINIGAME_CHECK; // 17페이지가 최소이자 검사 페이지.
    int check_layout; // 미니게임을 맞췄느냐 틀렸느냐에 따라 시작 레이아웃이 달라짐.
    int max_page = status.getMaxPage(); // 총 페이지*
    int breakpoint_After = Game_Status.GAME_EVENT_AFTERQUIZ; // 퀴즈가 끝난 이후의 브레이크포인트
    int after_layout = R.drawable.game_event_after_practice01; // 퀴즈가 끝난 이후의 첫 페이지. 공식은 after_layout(0) + 현재 페이지(18 or more) - 18

    LinearLayout layout; // 레이아웃 이름 불러와서 저장할 변수
    ImageView prevButton; // 이전 버튼 불러올 변수
    ImageView nextButton; // 다음 버튼 불러올 변수
    ImageView exitButton; // 나가기 버튼 불러올 변수
    ImageView replayButton; // 다시 시도하는 버튼 불러올 변수
    TextView curText; // 현재 페이지 불러올 변수
    TextView maxText; // 최대 페이지 불러올 변수
	TextView iterator; // 페이지 구분자 저장할 변수
    ImageView[] chButton = new ImageView[3]; // 게임하기 장 버튼 이미지

    public void setDefaultSetting() // 시작과 동시에 세팅해야 할 기본적이고 아주 중요한 요소들을 담은 곳.
    {
        // 이하는 버튼 세팅

        layout = (LinearLayout) findViewById(R.id.tutorial_content_layout); // 레이아웃 아이디값 가져옴

        prevButton = (ImageView) findViewById(R.id.btnPrev); // 이전 버튼
        nextButton = (ImageView) findViewById(R.id.btnNext); // 다음 버튼
        exitButton = (ImageView) findViewById(R.id.button_game_exit); // 나가기 버튼
        replayButton = (ImageView) findViewById(R.id.button_replay); // 다시 시작 버튼

        curText = (TextView) findViewById(R.id.current_page_text); // 현재 페이지 id 가져오기
        maxText = (TextView) findViewById(R.id.max_page_text); // 최대 페이지 id 가져오기

        for(int i = 0; i < chButton.length; i++) // 게임하기 장 선택 버튼 id 불러오기
        {
            chButton[i] = (ImageView) findViewById(R.id.button_game_ch1 + i); // i는 0 ~ 2까지 계산하므로 그대로 더해서 가져올 수 있음.
        }

        if(status.getCheck()) // 문제를 맞췄다면
        {
            replayButton.setVisibility(View.INVISIBLE); // 맞췄으므로 다시 시작할 필요가 없음
            check_layout = R.drawable.game_event_clear; // 레이아웃은 맞춘 버전으로

            layout.setBackgroundResource(check_layout); // 초기 페이지의 레이아웃 설정

            curText.setText(String.valueOf(status.getPage())); // 시작할 때 현재 페이지로 맞추기 위함
            maxText.setText(String.valueOf(max_page)); // 최대 페이지 표시
        }
        else // 틀렸을 경우
        {
            curText.setVisibility(View.INVISIBLE); // 틀리면 가리기
			maxText.setVisibility(View.INVISIBLE); // 틀리면 가리기
			iterator = (TextView)findViewById(R.id.page_iterator); // 구분자도 가리기
			iterator.setVisibility(View.INVISIBLE);

			prevButton.setVisibility(View.INVISIBLE); // 앞뒤 가는 버튼도 막기
            nextButton.setVisibility(View.INVISIBLE);

			check_layout = R.drawable.game_event_fail; // 레이아웃은 틀린 버전으로
			
			layout.setBackgroundResource(check_layout); // 초기 페이지의 레이아웃 설정

            replayButton.setVisibility(View.VISIBLE); // 리플레이 버튼 보이게 하기

			replayButton.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{ // 리플레이 버튼 누를시
					sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생

                    sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
					status.setPage(1, false); // 페이지 감소
					Intent intent = new Intent(getApplicationContext(), Game_event_Game_Play.class);
					startActivity(intent);
					finish();
				}
			});
		}
    }

    public void pageSet(int pageNum){ // 실제로 페이지 넘기는 역할 메소드
        if(pageNum >= check_page && pageNum <= max_page) // 범위 이내이면 실행
        {
            detailSet(pageNum);
            curText.setText(String.valueOf(pageNum));
        }
    }

    public void detailSet(int page) // 세밀한 세팅을 위한 메소드
    {
        if(page > check_page) // 체크 페이지보다 크면 이후 페이지로 배경 변경
        {
            layout.setBackgroundResource(after_layout + page - 18); // 실제 페이지 - 18을 해야 맞음.*
        }
        else // 체크 페이지이면 처리
        {
            layout.setBackgroundResource(check_layout); // 체크하는 배경으로
        }

        if(page == max_page) // 마지막 페이지면 버튼 이미지 보이기
        {
            toggleView(View.VISIBLE);
        }
        else // 마지막 페이지가 아니면 가리기
        {
            toggleView(View.INVISIBLE);
        }
    }

    public void toggleView(int visible) // 뷰 보이게 할 것인가 말 것인가 on/off. 여기서는 장 선택 버튼이 마지막에 보이느냐 마냐만 결정.
    {
        for(int i = 0; i < chButton.length; i++)
        {
            chButton[i].setVisibility(visible);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_event_content); // 사용 xml

        setDefaultSetting(); // 기본 세팅 시작

        sos = SoundSetting.getInstance(); // 효과음 3셋
        sound_pool = new SoundPool( 10, AudioManager.STREAM_MUSIC, 0 );
        SoundID = sound_pool.load( Game_event_AfterGame.this, R.raw.button, 1 ); // R.raw.sound에서 "sound"는 사운드 파일명

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 이전 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                if(status.getPage() > check_page) // 최소값보다 높으면
                {
                    pageSet(status.setPage(1, false).getPage()); // 화면 바끼는 메소드
                }
                else if(status.getPage() == check_page) // 검사 페이지이면 미니게임 페이지로
                {
                    sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
                    status.setPage(1, false); // 페이지 감소
					Intent intent = new Intent(getApplicationContext(), Game_event_Game_Play.class);
					startActivity(intent);
					finish();
                }
				else
				{	

				}
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 다음 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                if(status.getPage() >= max_page) { // 마지막 페이지일 경우
                    Intent intent = new Intent(getApplicationContext(), Game_List.class);
                    if(status.getGameEvent()) // 이벤트를 환경설정에서 접하면*
                    {
                        status.setGameEvent(false); // 이벤트가 끝났기 때문에 false로 되돌려 나중에 문제가 생기지 않게 함.*
                    }
                    else // 이벤트를 1-3장에서 접하면 게임하기 페이지로 이동
                    {
                        sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
                        startActivity(intent);
                    }

                    status.resetPage(); // 이벤트가 끝났으므로 페이지 1로 초기화*
                    finish();
                }
                else if(status.getPage() < max_page){ // 마지막 페이지가 아닐경우
                     pageSet(status.setPage(1, true).getPage()); // 화면 바끼는 메소드
                }
                else // 해당사항 없음
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
        LayoutInflater dialog = LayoutInflater.from(Game_event_AfterGame.this); // 종료하시겠습니까? 안내창임
        final View dialogLayout = dialog.inflate(R.layout.dialog, null);
        final Dialog myDialog = new Dialog(Game_event_AfterGame.this);

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
