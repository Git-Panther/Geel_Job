package com.example.m_user.geel_job.Tutorial;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.m_user.geel_job.GamePlay.Game_List;
import com.example.m_user.geel_job.GamePlay.Event.Game_event_content;
import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.Option.SoundSetting;
import com.example.m_user.geel_job.R;
import com.example.m_user.geel_job.Tutorial.Storage.TutorialEventStoring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by M-user on 2017-09-13.
 * Remade by KYG on 2017-10-24
 */

public class Tutorial_After_OX extends CustomFontActivity { // OX 정오답에 따라 페이지 출력이 달라짐.

    TutorialEventStoring tes = TutorialEventStoring.getInstance(); // 상태 불러오기

    LinearLayout layout; // 클리어 여부에 따라 성공, 실패, 시간 끝 표시하기 위한

    int min_page; // 최소 페이지를 저장할 변수
    int max_page; // 최대 페이지를 저장할 변수
    int cur_page; // 현재 페이지 값을 저장할 변수

    int start_layout; // OX 퀴즈 이후의 시작 페이지의 배경 저장 변수
    int end_layout; // 정답&오답 이후의 시작 페이지(오답은 리플레이 권유) 배경 저장 변수

    TextView current_page_text; // 페이지수 표시하는 텍스트뷰
    TextView max_page_text; // 최대페이지 수를 표시하는 텍스트뷰

    ImageView action_button; // 1-3장의 게임하기 버튼, 오답일 경우에는 리플레이 버튼
    ImageView prevButton; // 이전 버튼. 1페이지 비활성화
    ImageView next_button; // 다음 버튼
    ImageView out_button; // 나가기 버튼

    ImageView clear_object; // 정답을 맞췄을 때의 이미지
    ImageView fail_object; // 정답을 틀렸을 때의 이미지.

    SeekBar seekBar; // 드래그로 페이지 넘길때 쓸려고 만듬

    // 이하는 효과음 변수
    SoundPool sound_pool;
    int SoundID;
    SoundSetting sos;

    int next_button_visiblity = View.VISIBLE; // 다음 버튼 on&off 설정 위한 변수. 이벤트에서 버튼 비활성화를 먼저 하므로 VISIBLE을 기본값으로.*
    int action_button_visiblity = View.INVISIBLE; // 이벤트 버튼 on&off 설정 위한 변수. 이벤트에서 버튼 활성화를 먼저 하므로 INVISIBLE을 기본값으로.*
    int prev_visiblity = View.INVISIBLE; // 1페이지부터 시작이므로 뒤로 가는 페이지 비활성화*

    public void setDefaultSetting() // 변수, 객체 초기화하는 작업들을 모아놓은 메소드
    {
        checkTheAnswer(); // 정답 고른 것에 따라 설정 다르게 해주는 메소드
        cur_page = min_page;

        current_page_text = (TextView) findViewById(R.id.tutorial_page_text);
        max_page_text = (TextView) findViewById(R.id.max_page_text); // 아이디값 가져오기
        max_page_text.setText(String.valueOf(max_page)); // 최대 페이지 번호 표시

        max_page_text.setText(String.valueOf(max_page));

        layout = (LinearLayout) findViewById(R.id.tutorial_ox_clear_layout); // 레이아웃 정보 이미지 바꿀 용도로 사용
        layout.setBackgroundResource(start_layout);

        sos = SoundSetting.getInstance(); // 효과음 세트 셋팅
        sound_pool = new SoundPool( 10, AudioManager.STREAM_MUSIC, 0 );
        SoundID = sound_pool.load( Tutorial_After_OX.this, R.raw.button, 1 ); // R.raw.sound에서 "sound"는 사운드 파일명
    }

    public void setButtonSetting() // 버튼 초기화 및 버튼 이벤트 추가하는 것들을 모아놓은 메소드
    {
        prevButton = (ImageView) findViewById(R.id.btnPrev); // 이전 버튼
        next_button = (ImageView) findViewById(R.id.btnNext); // 다음 버튼
        out_button = (ImageView) findViewById(R.id.button_game_exit); // 나가기 버튼

        prevButton.setVisibility(prev_visiblity); // 1페이지 시작이므로 invisible. 만일 리플레이로 시작 페이지 달라지면 조정해야 함.*

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 이전 버튼 누를 시
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); //효과음 재생
                if(cur_page > min_page){ // 최소 페이지보다 커야 페이지 줄일 수 있음
                    setPage(--cur_page); // 페이지 줄이면서 변경
                }
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 다음 버튼 누를 시
                if(cur_page >= max_page){ // 마지막 페이지일 경우
                    sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); //효과음 재생
                    finish();
                }
                else{ // 마지막페이지가 아니면 페이지수 변경
                    sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); //효과음 재생
                    setPage(++cur_page); // 화면 바꾸는 메소드
                }
            }
        });

        out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 나가기 버튼
                onBackPressed();
            }
        });

        action_button = (ImageView) findViewById(R.id.action_button); // 액션 버튼 설정. 1-3은 게임하기, 오답일 때에는 리플레이.*
        action_button.setVisibility(action_button_visiblity); // 액션 버튼 비활성화. 시작하자마자 쓰지는 않으므로.
        if(tes.getAnswer()) // 1-3장 게임하기 버튼과 리플레이 버튼 중 하나를 결정하기 위한 세팅. true면 정답, 아니면 오답
        {
            action_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sound_pool.play(SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f); // 효과음 재생

                    Intent intent;

                    String readStr = ""; // 파일 시스템 사용할려고 만듬
                    String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Geel_job"; // 파일 경로 지정
                    File file = new File(sdPath) ;

                    file.mkdirs(); // 디렉토리 생성

                    sdPath += "/Game_check.gji"; // 파일 경로 지정
                    file = new File(sdPath);
                    FileWriter fw = null;
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
                        intent = new Intent(getApplicationContext(), Game_event_content.class); // 게임하기 이벤트
                    }
                    else
                    {
                        intent = new Intent(getApplicationContext(), Game_List.class); // 게임하기 장 선택 페이지

                    }

                    // 이하는 액티비티 종료. 여기에 굳이 두는 이유는 게임하기 이벤트를 다 보지 않고 종료하면 튜토리얼 선택 창으로 돌려서 편하게 해주기 위함. 어차피 게임하기 할거면 이 이벤트 볼거니까.
                    tes.getActSub().finish(); // 게임하기로 넘어가므로 소단원 선택창 종료*
                    tes.getAct().finish(); // 게임하기로 넘어가므로 메인 장 선택창 종료*

                    sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
                    tes.getStatus().resetSelectedChapterInfo(); // 선택한 장의 소단원 초기화
                    startActivity(intent);
                    // 여기서는 선택한 장과 소단원을 종료하지 않는다. 정상적으로 이벤트를 마치고 게임하기로 갈 때까지 종료하지 않는다.

                    finish(); // 현재 창 종료*
                }
            });
        }
        else // 리플레이
        {
            action_button.setImageResource(R.drawable.button_action_replay);
            action_button.setOnClickListener(new View.OnClickListener() { // 리플레이*
                @Override
                public void onClick(View v) {

                    sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
                    sound_pool.play(SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f); // 효과음 재생
                    Intent intent = new Intent(getApplicationContext(), Tutorial_ch_sub_content.class); // 리플레이로 넘어갈 때에 닫은 소단원 열기 위해 공통 액티비티 intent 설정
                    tes.getStatus().getSelectedChapterInfo().setCur_page(true).setCur_layout(true).selectTheQuiz(); // 리플레이 시작 페이지, 리플레이 시작 배경, 그리고 풀어야 하는 OX 퀴즈 다시 세팅
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    public void writeAccess(String path) // 입장 권한을 부여하기 위한 파일 쓰기 메소드
    {
        String readStr = ""; // 파일 시스템 사용할려고 만듬

        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Geel_job"; // 파일 경로 지정
        File file = new File(sdPath) ;

        file.mkdirs(); // 디렉토리 생성

        sdPath += path; // 파일 경로 지정
        file = new File(sdPath);
        FileWriter fw = null;
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
        }
        else // else일 일은 없음.
        {

        }
    }

    public void checkTheAnswer() // OX퀴즈 페이지에서의 정답 고른 것에 따라 값 다르게 설정하는 메소드
    {
        if(tes.getAnswer()) { // 정답을 골랐으면 맞추었을 때의 이벤트 설정값으로. 정답을 맞추었으면 다음 장으로 갈 수 있는 권한도 생김.

            switch (tes.getStatus().getSelected_ch_sub_num()) // 진행중인 튜토리얼이 어디냐에 따라 달라짐.
            {
                case Tutorial_ch_num.CH1_1:
                    writeAccess("/CH1_1_CLEAR.gji");
                    break;
                case Tutorial_ch_num.CH1_2:
                    writeAccess("/CH1_2_CLEAR.gji");
                    break;
                case Tutorial_ch_num.CH1_3:
                    writeAccess("/CH1_3_CLEAR.gji");
                    writeAccess("/CH1_CLEAR.gji");
                    break;
                case Tutorial_ch_num.CH2_1:
                    writeAccess("/CH2_1_CLEAR.gji");
                    writeAccess("/CH2_CLEAR.gji");
                    break;
                case Tutorial_ch_num.CH3_1:
                    writeAccess("/CH3_1_CLEAR.gji");
                    break;
                case Tutorial_ch_num.CH3_2:
                    writeAccess("/CH3_2_CLEAR.gji");
                    break;
                case Tutorial_ch_num.CH3_3:
                    writeAccess("/CH3_3_CLEAR.gji");
                    break;
                case Tutorial_ch_num.CH3_4:
                    writeAccess("/CH3_4_CLEAR.gji");
                    break;
                case Tutorial_ch_num.CH3_5:
                    writeAccess("/CH3_5_CLEAR.gji");
                    break;
                case Tutorial_ch_num.CH3_6:
                    writeAccess("/CH3_6_CLEAR.gji");
                    writeAccess("/CH3_CLEAR.gji");
                    break;
                default:
                    Log.d("권한 부여 실패", "어느 장의 어느 소단원인지 알아내지 못했습니다.");
                    break;
            }

            min_page = tes.getStatus().getSelectedChapterInfo().getClear_startPage(); // min_page 초기화.
            max_page = tes.getStatus().getSelectedChapterInfo().getClear_endPage();// max_page 초기화.
            start_layout = tes.getStatus().getSelectedChapterInfo().getClear_layout(); // start_layout 초기화.
            end_layout = tes.getStatus().getSelectedChapterInfo().getClear_endLayout(); // end_layout 초기화.
        }
        else { // 틀렸으면 틀렸을 때의 이벤트 설정값으로

            min_page = tes.getStatus().getSelectedChapterInfo().getFailed_startPage(); // min_page 초기화.
            max_page = tes.getStatus().getSelectedChapterInfo().getFailed_endPage(); // max_page 초기화.
            start_layout = tes.getStatus().getSelectedChapterInfo().getSelectedQuiz().getFailed_layout(); // start_layout 초기화.
            end_layout = tes.getStatus().getSelectedChapterInfo().getReplay_layout(); // end_layout 초기화.
        }
    }

    public void setPage(int pageNum){
        if(pageNum > Tutorial_ch_num.QUIZ_RESULT && pageNum <= max_page) // 다음으로 넘겨서 페이지 값이 3 이상이 되었을 때
        {
            layout.setBackgroundResource(end_layout + pageNum - 3); // 레이아웃 번호 + 페이지 번호 -3 을 해줘야 자기 페이지가 됨*
            if(tes.getStatus().getSelectedChapterInfo().getCh_sub_number() == Tutorial_ch_num.CH1_3) // 1-3장은 마지막에 게임하기 버튼을 보여줌*
            {
                if(pageNum == max_page && next_button_visiblity == View.VISIBLE) // 마지막 페이지면 다음 페이지로 넘어가는 거 비활성화*
                {
                    next_button_visiblity = View.INVISIBLE;
                    next_button.setVisibility(next_button_visiblity);
                }

                if(pageNum == max_page && action_button_visiblity == View.INVISIBLE) // 마지막 페이지면 게임하기 버튼 생성*
                {
                    action_button_visiblity = View.VISIBLE;
                    action_button.setVisibility(action_button_visiblity);
                }
            }

            if(tes.getAnswer() == false) // 틀렸으면 마지막 페이지에서 리플레이 버튼 생성
            {
                if(pageNum == max_page && next_button_visiblity == View.VISIBLE) // 마지막 페이지면 다음 페이지로 넘어가는 거 비활성화*
                {
                    next_button_visiblity = View.INVISIBLE;
                    next_button.setVisibility(next_button_visiblity);
                }

                if(pageNum == max_page && action_button_visiblity == View.INVISIBLE) // 마지막 페이지면 리플레이 버튼 생성*
                {
                    action_button_visiblity = View.VISIBLE;
                    action_button.setVisibility(action_button_visiblity);
                }
            }
        }
        else if(pageNum >= min_page && pageNum <= Tutorial_ch_num.QUIZ_RESULT) // 1페이지에서 넘기면
        {
            layout.setBackgroundResource(start_layout + pageNum - 1); // 레이아웃 번호 + 페이지 번호 -1 을 해줘야 자기 페이지가 됨*
        }
        else // 범위 밖이면 변화 없음
        {

        }

        if(pageNum == min_page && prev_visiblity == View.VISIBLE) // 1 페이지면 이전 버튼 비활성화*
        {
            prev_visiblity = View.INVISIBLE;
            prevButton.setVisibility(prev_visiblity);
        }

        if(pageNum > min_page && prev_visiblity == View.INVISIBLE) // 1 페이지보다 크면 이전 버튼 활성화*
        {
            prev_visiblity = View.VISIBLE;
            prevButton.setVisibility(prev_visiblity);
        }

        seekBar.setProgress(pageNum - 1); // 시그바 위치 설정 시그바 범위가 0~99까지라서 -1로 함
        current_page_text.setText(String.valueOf(pageNum)); // 현재 페이지 번호 표시

        if(pageNum < max_page && next_button_visiblity == View.INVISIBLE) // 1-3장이나 오답시 마지막 페이지에서 사라지는 다음 페이지 버튼이 이전 페이지로 돌아왔을 때에는 다시 활성화되도록 변경
        {
            next_button_visiblity = View.VISIBLE;
            next_button.setVisibility(next_button_visiblity);
        }

        if(pageNum < max_page && action_button_visiblity == View.VISIBLE) // 1-3장 또는 틀렸을 때의 페이지의 끝부분이 아니면 게임하기 또는 리플레이 버튼 감추기
        {
            action_button_visiblity = View.INVISIBLE;
            action_button.setVisibility(action_button_visiblity);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_after_ox); // 사용 xml

        setDefaultSetting();
        setButtonSetting();

        seekBar = (SeekBar) findViewById(R.id.bgSeekBar); // 시크바 객체 불러오기
        seekBar.setProgress(cur_page - 1); // 시크바 초기 위치 설정. 현재 페이지에서 1을 뺀 값으로.
        seekBar.setMax(max_page - 1); // 시그바 최대값 설정 시그바 범위가 0~99까지라서 -1로함
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // 셋온클릭리스너 같은거
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { // 위치가 변화가 있을 경우
                cur_page = progress + 1; // 위치가 바뀌때 마다 페이지번호 수정. 1 더하는 것은 시크바가 0부터 시작하기 때문.

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
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onBackPressed() { // 취소버튼 눌렸을 경우
        sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); //효과음 재생
        LayoutInflater dialog = LayoutInflater.from(Tutorial_After_OX.this); // 종료하시겠습니까? 안내창임
        final View dialogLayout = dialog.inflate(R.layout.dialog, null);
        final Dialog myDialog = new Dialog(Tutorial_After_OX.this);

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
    protected void onDestroy() { // 현 엑티비티가 종료될 경우
        super.onDestroy();
        action_button.setVisibility(View.INVISIBLE);
    }
}
