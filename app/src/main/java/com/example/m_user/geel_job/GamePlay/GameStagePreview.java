package com.example.m_user.geel_job.GamePlay;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.m_user.geel_job.GamePlay.Blank.Game_Blank_Base;
import com.example.m_user.geel_job.GamePlay.Bool.Game_Bool_Base;
import com.example.m_user.geel_job.GamePlay.Fix.Game_Fix_Base;
import com.example.m_user.geel_job.GamePlay.Order.Game_Order_Base;
import com.example.m_user.geel_job.GamePlay.Ranking.Game_Last_input;

import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.Option.SoundSetting;
import com.example.m_user.geel_job.R;

/**
 * Created by They on 2017-11-14.
 */

public class GameStagePreview extends CustomFontActivity {

    int time_count = 1; // 시간 카운트.
    GameDynamicStatus status = GameDynamicStatus.getInstance(); // 게임 상태 불러오기

    boolean dialog_shown = false;

    SoundSetting sos; // 효과음 3셋
    SoundPool sound_pool;
    int SoundID; // R.raw.sound에서 "sound"는 사운드 파일명

    private CountDownTimer countDownTimer; // 전역변수로 쓸려고 여기에 넣음

    @Override
    public void onBackPressed() { // 취소버튼 누를 경우
        sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); //효과음 재생
        final LayoutInflater dialog = LayoutInflater.from(GameStagePreview.this); // 종료하시겠습니까? 안내창임
        final View dialogLayout = dialog.inflate(R.layout.dialog, null);
        final Dialog myDialog = new Dialog(GameStagePreview.this){
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

    @Override
    protected void onDestroy() { // 현 엑티비티가 종료될 경우
        super.onDestroy();
        try{
            countDownTimer.cancel();
        } catch (Exception e) {}
        countDownTimer=null;
        sound_pool.release();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stage_preview); // 사용 xml

        sos = SoundSetting.getInstance(); // 효과음 3셋
        sound_pool = new SoundPool( 10, AudioManager.STREAM_MUSIC, 0 );
        SoundID = sound_pool.load( GameStagePreview.this, R.raw.button, 1 ); // R.raw.sound에서 "sound"는 사운드 파일명

        LinearLayout bg = (LinearLayout) findViewById(R.id.stage_layout); // 스테이지에 따라 배경이 바뀐다.

        bg.setBackgroundResource(R.drawable.stage01 + status.getStage()); // 배경 설정.

        status.getChecker().resetAnswer(); // 정답 초기화.

        countDownTimer();
    }

    /*
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            updateThread();
        }
    };

    Thread myThread = new Thread(new Runnable() {
        public void run() {
            while (true) {
                try {
                    handler.sendMessage(handler.obtainMessage());
                    Thread.sleep(1000); // 1초씩
                } catch (Throwable t) {

                }
            }
        }
    });
    */

    public void countDownTimer(){ // 시간 설정하는 메소드

        countDownTimer = new CountDownTimer(time_count * Game_Status.TIME_ONE_SECOND, Game_Status.TIME_ONE_SECOND) { // 1000가 1초 즉 300초에서 1초씩으로 설정
            public void onTick(long millisUntilFinished) { // 타이머 시작되면
                time_count--;
            }
            public void onFinish() { // 시간이 끝날 경우
                this.cancel();
                Intent intent; // 이어서 할 스타일의 Intent
                if (status.getStatus() == Game_Status.GAME_TIMEOVER || status.getStatus() == Game_Status.GAME_END) // 게임 시간이 만료되었거나 게임 스테이지를 전부 마쳤다면 끝내기
                {
                    intent = new Intent(getApplicationContext(), Game_Last_input.class);
                } else { // 전달된 상황이 성공이나 실패면
                    switch (status.getChoice().getIntentStyleAt(status.getStage())) {
                        case Game_Status.GAME_BLANK:
                            intent = new Intent(getApplicationContext(), Game_Blank_Base.class);
                            break;
                        case Game_Status.GAME_BOOL:
                            intent = new Intent(getApplicationContext(), Game_Bool_Base.class);
                            break;
                        case Game_Status.GAME_ORDER:
                            intent = new Intent(getApplicationContext(), Game_Order_Base.class);
                            break;
                        case Game_Status.GAME_FIX:
                            intent = new Intent(getApplicationContext(), Game_Fix_Base.class);
                            break;
                        default: // 아무것도 아니면 종료
                            intent = new Intent(getApplicationContext(), Game_Last_input.class);
                            break;
                    }
                }


                sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
                startActivity(intent);
                finish();
            }
        };
    }

    @Override
    protected void onStart() { // 엑티비티가 실행되면
        sos.setBgmContinuous(false); // 시작할 때 false로 해줘야 이후에 일시정지 했을 때 bgm이 멈춘다.

        //myThread.start(); // 스레드 시작
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

    /*
    private void updateThread() {
        synchronized (myThread) {
            time_count++; // 스레드가 1초씩 실행함 그래서 1초마다 +1함
            if (time_count == 2) { // 2초가 되었을 경우
                Intent intent; // 이어서 할 스타일의 Intent
                if (status.getStatus() == Game_Status.GAME_TIMEOVER || status.getStatus() == Game_Status.GAME_END) // 게임 시간이 만료되었거나 게임 스테이지를 전부 마쳤다면 끝내기
                {
                    intent = new Intent(getApplicationContext(), Game_Last_input.class);
                } else { // 전달된 상황이 성공이나 실패면
                    switch (status.getChoice().getIntentStyleAt(status.getStage())) {
                        case Game_Status.GAME_BLANK:
                            intent = new Intent(getApplicationContext(), Game_Blank_Base.class);
                            break;
                        case Game_Status.GAME_BOOL:
                            intent = new Intent(getApplicationContext(), Game_Bool_Base.class);
                            break;
                        case Game_Status.GAME_ORDER:
                            intent = new Intent(getApplicationContext(), Game_Order_Base.class);
                            break;
                        case Game_Status.GAME_FIX:
                            intent = new Intent(getApplicationContext(), Game_Fix_Base.class);
                            break;
                        default: // 아무것도 아니면 종료
                            intent = new Intent(getApplicationContext(), Game_Last_input.class);
                            break;
                    }
                }

                sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
                startActivity(intent);
                finish();
            }
        }
    }
    */
}
