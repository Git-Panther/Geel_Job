package com.example.m_user.geel_job.GamePlay;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.m_user.geel_job.GamePlay.Ranking.Game_Last_input;
import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.Option.SoundSetting;
import com.example.m_user.geel_job.R;

/**
 * Created by They on 2017-11-14.
 */

public class GameResult extends CustomFontActivity {

    GameDynamicStatus status = GameDynamicStatus.getInstance(); // 상태 불러오기

    SoundPool sound_pool; // 효과음 3셋
    int SoundID;
    SoundSetting sos;

    private CountDownTimer countDownTimer; // 전역변수로 쓸려고 여기에 넣음

    int time_count = 1; // 시간 카운트.

    boolean dialog_shown = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stage_result); // 사용 xml

        LinearLayout bg = (LinearLayout) findViewById(R.id.stage_layout); // 리니어 가져와서 배경 설정
        ViewGroup nextObject = (ViewGroup) findViewById(R.id.stage_layout); // 화면에 리스너 이벤트 부여
        ImageView result = (ImageView) findViewById(R.id.result_object);

        sos = SoundSetting.getInstance(); // 효과음 3셋
        sound_pool = new SoundPool( 10, AudioManager.STREAM_MUSIC, 0 );
        SoundID = sound_pool.load( GameResult.this, R.raw.button, 1 ); // R.raw.sound에서 "sound"는 사운드 파일명

        switch (status.getStatus()) // 결과 표시하는 이미지 달라지도록 조정.
        {
            case Game_Status.GAME_CLEAR:
                result.setImageResource(R.drawable.result_clear);
                break;
            case Game_Status.GAME_FAIL:
                result.setImageResource(R.drawable.result_fail);
                break;
            case Game_Status.GAME_TIMEOVER:
                result.setImageResource(R.drawable.result_timeover);
                break;
            default: // 오류가 발생하면
                status.resetGame();
                finish();
                break;
        }

        countDownTimer();

        /*
        nextObject.setOnClickListener(new View.OnClickListener() { // 화면을 누르면 이동하도록 변경.
            @Override
            public void onClick(View v) {
                if(status.getStatus() == Game_Status.GAME_TIMEOVER) // 시간 초과
                {
                    Intent intent = new Intent(getApplicationContext(), Game_Last_input.class);
                    startActivity(intent);
                    finish();
                }
                else if (status.getStatus() == Game_Status.GAME_END) // 제 시간 안에 게임 끝
                {
                    status.setResultScore();
                    Intent intent = new Intent(getApplicationContext(), Game_Last_input.class);
                    startActivity(intent);
                    finish();
                }
                else // 성공이나 실패
                    status.nextStage(GameResult.this);
            }
        });
        */
    }

    @Override
    public void onBackPressed() { // 취소버튼 눌렸을 경우
        sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); //효과음 재생
        LayoutInflater dialog = LayoutInflater.from(GameResult.this); // 종료하시겠습니까? 안내창임
        final View dialogLayout = dialog.inflate(R.layout.dialog, null);
        final Dialog myDialog = new Dialog(GameResult.this){
            @Override
            public void dismiss() {
                if(dialog_shown) {
                    dialog_shown = false;
                    countDownTimer.start(); // 재시작
                }

                super.dismiss();
            }

            @Override
            public void cancel() {
                if(dialog_shown) {
                    dialog_shown = false;
                    countDownTimer.start(); // 재시작
                }
                super.cancel();
            }
        };

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setContentView(dialogLayout);
        myDialog.show();

        countDownTimer.cancel(); // 일시정지
        dialog_shown = true;

        ImageView btn_ok = (ImageView)dialogLayout.findViewById(R.id.btn_ok);
        ImageView btn_cancel = (ImageView)dialogLayout.findViewById(R.id.btn_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            { // 확인을 눌렀을 경우 밑에는 다 초기화 작업
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f );
                status.resetGame();
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

    public void countDownTimer(){ // 시간 설정하는 메소드

        countDownTimer = new CountDownTimer(time_count * Game_Status.TIME_ONE_SECOND, Game_Status.TIME_ONE_SECOND) { // 1000가 1초 즉 300초에서 1초씩으로 설정
            public void onTick(long millisUntilFinished) { // 타이머 시작되면
                time_count--;
            }
            public void onFinish() { // 시간이 끝날 경우
                this.cancel();
                sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
                if(status.getStatus() == Game_Status.GAME_TIMEOVER) // 시간 초과
                {
                    Intent intent = new Intent(getApplicationContext(), Game_Last_input.class);
                    startActivity(intent);
                    finish();
                }
                else if (status.getStatus() == Game_Status.GAME_END) // 제 시간 안에 게임 끝
                {
                    status.setResultScore();
                    Intent intent = new Intent(getApplicationContext(), Game_Last_input.class);
                    startActivity(intent);
                    finish();
                }
                else // 성공이나 실패
                    status.nextStage(GameResult.this);
            }
        };
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
        else // 배경음을 일시정지 해야한다면. 즉, 화면을 내렸을 때.
        {
            sos.getBgm().pause(); // 배경음 일시정지.
            countDownTimer.cancel();

            /*
            try { // 지나가는 시간도 일시정지.
                myThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            */
        }

        super.onPause();
    }

    @Override
    protected void onResume() {

        if(sos.getBgmContinuous()) // 배경음이 유지 중이라면
        {

        }
        else // 배경음이 멈춘 상태라면. 지나가는 시간도 다시 활성화.
        {
            sos.getBgm().start(); // 배경음 다시 재생.
            //updateThread(); // 스레드 업데이트 재시작

            if(dialog_shown)
            {

            }
            else
            {
                countDownTimer.start(); // 타이머 시작
            }
        }

        super.onResume();
    }

    @Override
    protected void onDestroy() { // 현 엑티비티가 종료될 경우
        super.onDestroy();
        try{
            countDownTimer.cancel();
        } catch (Exception e) {}
        countDownTimer=null;
        sound_pool.release();
    }
}
