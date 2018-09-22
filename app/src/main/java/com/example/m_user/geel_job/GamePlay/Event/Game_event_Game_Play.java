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
import com.example.m_user.geel_job.GamePlay.Listeners.DnDGameEventListener;
import com.example.m_user.geel_job.GamePlay.Listeners.DnDTouchListener;
import com.example.m_user.geel_job.GamePlay.Listeners.ETCOnDragListener;
import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.Option.SoundSetting;
import com.example.m_user.geel_job.R;

/**
 * Created by M-user on 2017-08-30.
 * 튜토리얼 처음 실행하면 나오는 화면입니다.
 */

public class Game_event_Game_Play extends CustomFontActivity {

    GameEventStatus status = GameEventStatus.getInstance(); // 상태 불러오기

    SoundPool sound_pool;
    int SoundID;
    SoundSetting sos; // 효과음 3셋

    // 이하는 기호 상수들.

    final int min_page = Game_Status.GAME_EVENT_CHOICE_BREAKPOINT + 1; // 최소 페이지 다르게 한 이유는 게임하기 이벤트의 두 번째 액티비티이므로. 장 선택 이후의 페이지 번호.
    final int start_layout = status.getStartLayout(); // 시작 페이지 결정. 페이지 이동 방향에 따라 다름.
    final int max_page = status.getMaxPage(); // 총 페이지*
    final int breakpoint_show_minigame = Game_Status.GAME_EVENT_MINIGAME_SHOW; // 브레이크포인트 : 미니게임 창을 보여줘야 하는 때
    final int breakpoint_minigame = Game_Status.GAME_EVENT_MINIGAME_BREAKPOINT; // 브레이크포인트 : 미니게임 플레이하는 때
    final int curPage = status.getPage(); // 현재 페이지 첫 불러오기
    final int curLayout = status.getStartLayout() + curPage - 1; // 현재 레이아웃 첫 불러오기
    final int pageFocus02 = Game_Status.GAME_EVENT_FOCUS_02; // 14 페이지 저장. 포커스 표시
    final int pageFocus03 = Game_Status.GAME_EVENT_FOCUS_03; // 15 페이지 저장. 포커스 표시

    public static final int quest_amount = Game_Status.GAME_EVENT_QUEST_AMOUNT; // 풀어야 할 빈칸 개수
    public static final int answer_amount = Game_Status.GAME_EVENT_ANSWER_AMOUNT; // 정답으로 제시되는 항목 개수

    // 이하는 객체들 가져올 변수

    LinearLayout layout; // 레이아웃 이름 불러와서 저장할 변수

    public static ImageView[] quest_blank = new ImageView[3]; // 빈칸 저장 배열

    public static ImageView[] quest_common = new ImageView[3]; // 문제로 제시되는 고정 오브젝트 배열

    public static ImageView[] answer = new ImageView[6]; // 정답으로 고를 수 있는 항목들의 배열

    ImageView checkButton; // 미니게임에서 검사를 위한 버튼

    ImageView prevButton; // 이전 버튼 불러올 변수
    ImageView nextButton; // 다음 버튼 불러올 변수
    ImageView exitButton; // 나가기 버튼 불러올 변수

    TextView curText; // 현재 페이지 불러올 변수
    TextView maxText; // 최대 페이지 불러올 변수

    ImageView focus02; // 이미지 포커스 불러올 변수 1
    ImageView focus03; // 이미지 포커스 불러올 변수 2

    public static LinearLayout quest_place[] = new LinearLayout[quest_amount]; // 빈칸을 가리키는 공간들의 배열
    public static LinearLayout answer_place[] = new LinearLayout[answer_amount]; // 선택 가능한 정답 항목들을 가리키는 배열

    public void toggleMinigame(int visible) // 미니게임을 보이게 하냐 마냐를 결정하는 메소드.
    {
        for(int i = 0; i < quest_amount; i++) // 문제 제시되는 것 관련하여 보여주거나 가리기
        {
            quest_common[i].setVisibility(visible);
            quest_blank[i].setVisibility(visible);
            quest_place[i].setVisibility(visible);
        }

        for(int i = 0; i < answer_amount; i++) // 정답 제시되는 것 관련하여 보여주거나 가리기
        {
            answer[i].setVisibility(visible);
            answer_place[i].setVisibility(visible);
        }
    }

    public void toggleClick(boolean bool) // 문제나 정답 선택을 가능하게 하거나 막기 체크 버튼 활성화도 담당. true면 가능하게 하나, false면 막는다.
    {
        for(int i = 0; i < quest_amount; i++) // 문제 제시되는 것 관련하여 클릭 설정
        {
            //quest_place[i].setLongClickable(bool);
            //quest_place[i].setClickable(bool);
        }

        for(int i = 0; i < answer_amount; i++) // 정답 제시되는 것 관련하여 클릭 설정
        {
            answer[i].setClickable(bool);
            answer[i].setLongClickable(bool);
        }

        if(bool) // 정답 체크 버튼 보여주거나 가리기. 참이면 보여주기
        {
            checkButton.setVisibility(View.VISIBLE);
        }
        else
        {
            checkButton.setVisibility(View.INVISIBLE);
        }
    }

    public void resetMinigame() // 미니게임 상태 초기화
    {
        status.resetAnswers(); // 정답 고른거 초기화.

        ImageView child; // Linear에 추가할 정답 항목을 저장

        for (int i = 0; i < quest_amount; i++) // 버튼 배치 초기화
        {
            quest_place[i].removeAllViews();
        }

        for (int i = 0; i < answer_amount; i++)
        {
            answer_place[i].removeAllViews();
            child = answer[i];
            answer_place[i].addView(child);
        }
    }

    public void pageSet(int pageNum){ // 실제로 페이지 넘기는 역할 메소드
        if(pageNum >= min_page && pageNum <= breakpoint_minigame) // 범위 이내이면 실행
        {
            detailSet(pageNum);
            layout.setBackgroundResource(start_layout + pageNum - 1); // 실제 페이지 - 1을 해야 맞음.*
            curText.setText(String.valueOf(pageNum));
        }
    }

    public void detailSet(int page) // 세밀한 세팅을 위한 메소드
    {
        if(page > min_page) // 최소 페이지보다 크면 뒤로가기 활성화
            prevButton.setVisibility(View.VISIBLE);

        if(page < breakpoint_show_minigame) // 미니게임 보여주는 페이지가 아니면 가림.
        {
            toggleMinigame(View.INVISIBLE);
        }

        if(page >= breakpoint_show_minigame && page <= breakpoint_minigame) { // 8~16이면 빈칸 채우기 보이기

            if(page >= 11 && page <= 12) // 11, 12 페이지이면 빈칸들 가리기
            {
                toggleMinigame(View.INVISIBLE);
            }
            else // 그렇지 않으면 보이게 하기
            {
                toggleMinigame(View.VISIBLE);
            }

            if(page == pageFocus02) // 14페이지이면 문제 창 포커스 보이게 하기
            {
                focus02.setVisibility(View.VISIBLE);
            }
            else // 아니면 가리기
            {
                focus02.setVisibility(View.INVISIBLE);
            }

            if(page == pageFocus03) // 15페이지이면 문제 창 포커스 보이게 하기
            {
                focus03.setVisibility(View.VISIBLE);
            }
            else // 아니면 가리기
            {
                focus03.setVisibility(View.INVISIBLE);
            }

            if (page == breakpoint_minigame) // 16페이지면 미니게임 활성화
            {
                toggleClick(true);
                nextButton.setVisibility(View.INVISIBLE);
            }
            else // 16 페이지가 아니면 클릭 막고 앞으로 가기 활성화
            {
                resetMinigame(); // 미니게임 초기화
                toggleClick(false);
                nextButton.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setDefaultSetting() // 기본 세팅 모아놓은 메소드
    {
        layout = (LinearLayout) findViewById(R.id.tutorial_content_layout); // 레이아웃 아이디값 가져옴

        status.resetAnswers(); // 정답 고른거 초기화. 미니게임 초기화하여 다시 맞추도록 하기

        for(int i = 0; i < quest_amount; i++) // 객체들 id 가져오기 : 문제 부분
        {
            quest_common[i] = (ImageView) findViewById(R.id.quest1+i); // 이름이 순서대로 되어있어 i 값을 더해주면 자동으로 그 id가 된다.
            quest_blank[i] = (ImageView) findViewById(R.id.blank1+i);
            quest_place[i] = (LinearLayout) findViewById(R.id.place_blank1+i);
            quest_place[i].setOnDragListener(DnDGameEventListener.getInstance()); // 빈칸의 공간이 되는 LinearLayout에 드래그앤드롭 이벤트 추가

        }

        for(int i = 0; i < answer_amount; i++) // 객체들 id 가져오기 : 정답 부분
        {
            answer_place[i] = (LinearLayout) findViewById(R.id.place_answer1+i);
            answer[i] = (ImageView) findViewById(R.id.answer1+i);
            answer[i].setTag((new StringBuilder("Answer").append(i))); // 태그 없으면 그림자가 null을 참조하므로 있어야 함.

            answer_place[i].setOnDragListener(DnDGameEventListener.getInstance()); // 빈칸의 공간이 되는 LinearLayout에 드래그앤드롭 이벤트 추가
            //answer[i].setOnLongClickListener(new DnDLongClickListener());
            answer[i].setOnTouchListener(new DnDTouchListener());
        }

        checkButton = (ImageView) findViewById(R.id.game_check_button); // 미니게임 체크 버튼

        prevButton = (ImageView) findViewById(R.id.btnPrev); // 이전 버튼
        nextButton = (ImageView) findViewById(R.id.btnNext); // 다음 버튼
        exitButton = (ImageView) findViewById(R.id.button_game_exit); // 나가기 버튼

        curText = (TextView) findViewById(R.id.current_page_text); // 현재 페이지 표시
        maxText = (TextView) findViewById(R.id.max_page_text); // 최대 페이지 표시

        focus02 = (ImageView) findViewById(R.id.focus02); // 문제 창 포커스
        focus03 = (ImageView) findViewById(R.id.focus03); // 정답 창 포커스

        focus02.setVisibility(View.INVISIBLE); // 포커스는 14, 15일때만 보이므로 가리기
        focus03.setVisibility(View.INVISIBLE); // 포커스는 14, 15일때만 보이므로 가리기

        layout.setBackgroundResource(curLayout); // 초기 페이지 설정. 1페이지 시작이 아니므로 초기 페이지 + 현재 페이지 - 1

        curText.setText(String.valueOf(curPage)); // 시작할 때 현재 페이지로 맞추기 위함
        maxText.setText(String.valueOf(max_page)); // 최대 페이지 표시

        if(curPage == breakpoint_minigame) // 미니게임 페이지에서 액티비티를 시작하면. 그러니까 이 if는 시작시에 버튼 세팅을 결정.
        {
            nextButton.setVisibility(View.INVISIBLE); // 앞으로 못 가게 하기 위함
        }
        else // 시작 페이지가 6페이지(이벤트 2 첫 페이지)면
        {
            toggleClick(false);
            toggleMinigame(View.INVISIBLE);
            checkButton.setVisibility(View.INVISIBLE);
        }

        // 이하는 드래그앤드롭에서 이상현상 방지를 위한 것.

        findViewById(R.id.view1).setOnDragListener(ETCOnDragListener.getInstance());
        findViewById(R.id.view2).setOnDragListener(ETCOnDragListener.getInstance());
        findViewById(R.id.view3).setOnDragListener(ETCOnDragListener.getInstance());

        findViewById(R.id.scale1).setOnDragListener(ETCOnDragListener.getInstance());
        findViewById(R.id.scale2).setOnDragListener(ETCOnDragListener.getInstance());
        findViewById(R.id.scale3).setOnDragListener(ETCOnDragListener.getInstance());
        findViewById(R.id.scale4).setOnDragListener(ETCOnDragListener.getInstance());
        findViewById(R.id.scale5).setOnDragListener(ETCOnDragListener.getInstance());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_event_gameplay); // 사용 xml

        setDefaultSetting(); // 기본 세팅

        sos = SoundSetting.getInstance(); // 효과음 3셋
        sound_pool = new SoundPool( 10, AudioManager.STREAM_MUSIC, 0 );
        SoundID = sound_pool.load( Game_event_Game_Play.this, R.raw.button, 1 ); // R.raw.sound에서 "sound"는 사운드 파일명

        // 이하는 정답 항목을 선택했을 때의 이벤트. 그리고 원래 블랭크인 곳을 다시 선택하여 취소하는 이벤트.

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생

                sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
				status.setCheck(); // 정답 체크
                status.setPage(1, true);
                Intent intent = new Intent(getApplicationContext(), Game_event_AfterGame.class); // 게임 체크 및 이후 액티비티로 이동
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
                    pageSet(status.setPage(1, false).getPage()); // 페이지수 -= 1랑 같음
                }
                else // 브레이크포인트인 6페이지에서 뒤로가기 버튼을 누르면
                {
                    sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
                    status.setPage(1, false); // 우선 페이지 1단계 역방향으로 넘김
                    Intent intent = new Intent(getApplicationContext(), Game_event_content.class); // 이전 액티비티로 되돌아감.
                    startActivity(intent);
                    finish();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 다음 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                if(status.getPage() < breakpoint_minigame)
                {
                    pageSet(status.setPage(1, true).getPage()); // 페이지수 += 1랑 같음
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
        sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f );

        LayoutInflater dialog = LayoutInflater.from(Game_event_Game_Play.this); // 종료하시겠습니까? 안내창임
        final View dialogLayout = dialog.inflate(R.layout.dialog, null);
        final Dialog myDialog = new Dialog(Game_event_Game_Play.this);

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
