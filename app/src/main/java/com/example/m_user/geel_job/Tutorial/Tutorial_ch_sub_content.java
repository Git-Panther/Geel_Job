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
import com.example.m_user.geel_job.Tutorial.Storage.TutorialEventStoring;

/**
 * Created by M-user on 2017-06-21.
 * 1장 1번 내용입니다.
 * debugged by KYG on OCT.8 / 2017
 */

public class Tutorial_ch_sub_content extends CustomFontActivity { // 튜토리얼 소단원 내용 보여주는 클래스. 커스텀 폰트 적용.

    TutorialEventStoring tes = TutorialEventStoring.getInstance(); // 상태 저장용. 주로 TutorialStatus 용도로 쓰임.

    private int min_page; // 최소 페이지 정보 저장용.
    private int max_page; // 최대 페이지 정보 저장용.
    private int cur_page; // 현재 페이지 정보 저장용. 페이지가 변화함에 따라 같이 변함. 시작 값이 소단원에서의 시작이냐 리플레이로 재시작이냐에 따라 다름.

    private int min_layout; // 해당 소단원의 첫 페이지의 이미지 값을 저장할 변수.
    private int start_layout; // 시작 페이지에 표시할 이미지를 저장할 변수. 일반 시작이냐 재시작이냐에 따라 다름.

    private LinearLayout layout; // 레이아웃 객체를 불러와 설정하기 위함.
    private TextView cur_page_text; // 현재 페이지 번호 표시하는 텍스트뷰. cur_page의 값에 따라 변화함.
    private TextView max_page_text; // 최대 페이지 표시하는 텍스트뷰. 객체 정보를 가져오기 위해 쓰임.
    private SeekBar seekBar; // 시크바 객체.

    // 이하는 사운드 객체 사용
    private SoundPool sound_pool;
    private int SoundID;
    private SoundSetting sos;

    private ImageView prevButton; // 이전 버튼. 여기에 두는 이유는 setPage()에서 1페이지에서 이전 페이지를 감출 때 쓰기 때문에 미리 쓰려면 미리 선언해줘야 한다. 사용에는 문제 없음.
    private int prev_visible = View.INVISIBLE; // 1페이지부터 시작이므로 뒤로 가는 페이지 비활성화

    private ImageView nextButton; // 다음 버튼
    private ImageView exitButton; // 나가기 버튼

    public void setDefaultSetting() // onCreate() 호출과 동시에 기본적인 세팅을 하기 위한 메소드. 여기에 초기값들을 설정하는 요소들이 모여있다. 객체보단 변수 값 설정에 중심을 두었다.
    {
        min_page = tes.getStatus().getSelectedChapterInfo().getStart_page(); // 최소 페이지 설정
        max_page = tes.getStatus().getSelectedChapterInfo().getMax_page(); // 최대 페이지 설정
        cur_page = tes.getStatus().getSelectedChapterInfo().getCur_page(); // 시작 페이지 설정

        min_layout = tes.getStatus().getSelectedChapterInfo().getStart_layout(); // 해당 소단원의 첫 페이지의 이미지를 설정. 페이지 넘기는 기준으로 쓰인다.
        start_layout = tes.getStatus().getSelectedChapterInfo().getCur_layout(); // 시작할 때의 페이지의 배경 이미지를 설정.

        layout = (LinearLayout) findViewById(R.id.tutorial_content_layout); // 레이아웃 정보 불러옴

        cur_page_text = (TextView) findViewById(R.id.current_page_text); // 페이지 수 보여주는 텍스트뷰
        max_page_text = (TextView) findViewById(R.id.max_page_text); // 최대 페이지 아이디값 가져오기

        layout.setBackgroundResource(start_layout); // 초기 화면 이미지 변수를 패러미터로 주어 배경 설정.
        cur_page_text.setText(String.valueOf(cur_page)); // 현재 페이지 텍스트 시각적으로 수정
        max_page_text.setText(String.valueOf(max_page)); // 최대 페이지 텍스트 시각적으로 수정

        sos = SoundSetting.getInstance(); // 효과음 설정
        sound_pool = new SoundPool( 10, AudioManager.STREAM_MUSIC, 0 );
        SoundID = sound_pool.load( Tutorial_ch_sub_content.this, R.raw.button, 1 ); // R.raw.sound에서 "sound"는 사운드 파일명
    }

    public void setButtonSetting() // 버튼 세팅 메소드
    {
        prevButton = (ImageView) findViewById(R.id.btnPrev); // 이전 버튼
        nextButton = (ImageView) findViewById(R.id.btnNext); // 다음 버튼
        exitButton = (ImageView) findViewById(R.id.button_game_exit); // 나가기 버튼

        if(cur_page == min_page) // 시작 페이지와 최소 페이지(=1)가 같으면 안 보이게 가려준다.
            prevButton.setVisibility(prev_visible); // 1페이지 시작이므로 invisible. 만일 리플레이로 시작 페이지 달라지면 이 이벤트는 발생하지 않음.

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 이전 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                if(cur_page > min_page){ // 1이 아니면 페이지 값 수정
                    setPage(--cur_page); // 페이지 설정 메소드. --로 먼저 감소시킨 후에 실행시킴.
                }

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 다음 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                if(cur_page >= max_page){ // 마지막 페이지에서 넘길 경우. 이것부터 검사해야 막 페이지 되자마자 안 넘어감.

                    sos.setBgmContinuous(true); // OX를 시작하므로 배경음 유지
                    Intent intent = new Intent(getApplicationContext(),Tutorial_OX_main.class); // ox문제 출력
                    startActivity(intent);
                    finish(); // 설명 페이지 종료
                }
                else
                {
                    setPage(++cur_page); // 페이지 설정 메소드. ++로 먼저 증가시킨 후에 실행시킴.
                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 나가기 버튼 누를 시
                onBackPressed();
            }
        });
    }

    public void setPage(int pageNum){ // 실제로 페이지 넘기는 역할 메소드
        if(pageNum < min_page) // 최소 페이지보다 작아지면 문제가 되므로 검사*
        {
            pageNum = min_page; // 최소 페이지보다 작아졌으므로 최소 페이지로 재설정*
        }
        else if(pageNum > max_page) // 최대 페이지보다 커지면 문제가 되므로 검사*
        {
            pageNum = max_page; // 최대 페이지보다 커졌으므로 최대 페이지로 재설정*
        }
        else // 범위 이내이므로 추가 작업 없음*
        {

        }

        if(pageNum == min_page && prev_visible == View.VISIBLE) // 1 페이지면 이전 버튼 비활성화*
        {
            prev_visible = View.INVISIBLE;
            prevButton.setVisibility(prev_visible);
        }

        if(pageNum > min_page && prev_visible == View.INVISIBLE) // 1 페이지보다 크면 이전 버튼 활성화*
        {
            prev_visible = View.VISIBLE;
            prevButton.setVisibility(prev_visible);
        }

        layout.setBackgroundResource(min_layout + pageNum - 1); // 시작 레이아웃 번호 + 페이지 번호 -1 을 해줘야 자기 페이지가 됨*
        seekBar.setProgress(pageNum - 1); // 시크바 위치 설정. 시크바는 0 ~ max_page - 1까지 이동한다.
        cur_page_text.setText(String.valueOf(pageNum)); // 화면상에 현재 페이지의 값을 출력
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_ch_sub_content); // 사용 xml. 공용 페이지 xml을 사용한다.

        setDefaultSetting(); // 기본 세팅 시작.
        setButtonSetting(); // 버튼 세팅 시작. 버튼 이벤트 세팅한 메소드들 여기에다 모아둠.

        seekBar = (SeekBar) findViewById(R.id.bgSeekBar); // 시크바 객체 정보 저장
        seekBar.setProgress(cur_page - 1); // 시크바 위치 초기화. 0부터 시작하므로 1을 빼준다. 시작 위치는 시작이냐 재시작이냐에 따라 달라짐.
        seekBar.setMax(max_page - 1); // 시크바 최대값 설정. 0부터 시작하므로 최대 페이지 - 1로 설정

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // 시크바의 위치가 변했을 때 발생하는 이벤트
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { // 위치가 바뀔경우
                cur_page = progress + 1; // 위치가 바뀌때 마다 페이지번호 수정. progress는 0 ~ max_page - 1이므로 1을 더해줘야 원래 페이지의 값이 나온다.

                if(cur_page < min_page) // 최소 페이지보다 작아지면 문제가 되므로 검사*
                {
                    cur_page = min_page; // 최소 페이지보다 작아졌으므로 최소 페이지로 재설정*
                }
                else if(cur_page > max_page) // 최대 페이지보다 커지면 문제가 되므로 검사*
                {
                    cur_page = max_page; // 최대 페이지보다 커졌으므로 최대 페이지로 재설정*
                }
                else // 범위 이내이므로 다른 조치 취하지 않음*
                {

                }

                if(cur_page >= min_page && cur_page <= max_page) // 범위 내이면 페이지 바꿈. 스레드 문제로 위 if 검사 안 할 때 대비용*
                {
                    setPage(cur_page); // 화면 바꾸는 메소드
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { // 이동을 위해 시크바를 선택했을 때 나타나는 이벤트

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { // 시크바 이동을 멈췄을 때. 즉, 손에서 뗐을 때(?)

            }
        });
    }

    public void onBackPressed() { // 취소 버튼 누를 시
        sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); //효과음 재생
        LayoutInflater dialog = LayoutInflater.from(Tutorial_ch_sub_content.this); // 종료하시겠습니까? 안내창임
        final View dialogLayout = dialog.inflate(R.layout.dialog, null);
        final Dialog myDialog = new Dialog(Tutorial_ch_sub_content.this);

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
        sound_pool.release();
    }
}
