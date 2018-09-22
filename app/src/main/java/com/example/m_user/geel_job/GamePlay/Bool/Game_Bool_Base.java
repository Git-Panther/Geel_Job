package com.example.m_user.geel_job.GamePlay.Bool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m_user.geel_job.GamePlay.GameDynamicStatus;
import com.example.m_user.geel_job.GamePlay.GameResult;
import com.example.m_user.geel_job.GamePlay.Game_Status;
import com.example.m_user.geel_job.GamePlay.Storage.BoolStorage;
import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.Option.SoundSetting;
import com.example.m_user.geel_job.R;

/**
 * Created by They on 2017-11-09.
 */

public class Game_Bool_Base extends CustomFontActivity {

    GameDynamicStatus status = GameDynamicStatus.getInstance(); // 동적 게임 데이터 가져오기

    int time_count = status.getTime(); // 시간 정보 가져오기

    boolean dialog_shown = false;

    int count = 0; // 현재 조건문 고개 넘은 수. 시작할 때는 한 번도 넘지 않았으므로 0.

    TextView text_time; // 시간초 표시하는 텍스트뷰
    TextView text_score; // 점수 표시하는 텍스트뷰

    ImageView subtitle; // 부제 설정하기 위한 이미지뷰
    ImageView sentence; // 설명란 설정하기 위한 이미지뷰
    ImageView round; // 현재 진행중인 라운드 표시를 위한 이미지뷰

    ImageView exitButton; // 나가기 버튼

    private CountDownTimer countDownTimer; // 타이머 함수

    SoundSetting sos;
    SoundPool sound_pool;
    int SoundID;

    BoolStorage storage = (BoolStorage)status.getChoice().getSelectedGamesAt(status.getStage());
    // 지금 진행중인 조건문 고개 문제 정보 가져오기

    int bool_title = storage.getQuizTitle(); // 조건문 고개의 부제 가져오기
    int[] bool_sentence = storage.getQuizImage(); // 조건문 고개의 이미지를 가지고 있는 배열 가져오기

    Toast mainToast; // 토스트 띄우기

    public void setDefaultSetting() // 시작할 때의 설정.
    {
        status.getChecker().setAnswers(status.getChoice().getIntentStyleAt(status.getStage()), 0);

        text_time = (TextView) findViewById(R.id.game_time);
        text_score = (TextView) findViewById(R.id.game_score);
        subtitle = (ImageView)findViewById(R.id.bool_subtitle);
        sentence = (ImageView)findViewById(R.id.bool_sentence_background);
        round = (ImageView)findViewById(R.id.bool_round);

        exitButton = (ImageView) findViewById(R.id.button_game_exit); // 나가기 버튼.

        text_score.setText(String.valueOf(status.getGameScore()).toString()); // 점수 출력
        text_time.setText(String.valueOf(status.getTime()).toString()); // 초기 시간 출력

        pageSet(count); // 화면 설정
        subtitle.setImageResource(bool_title); // 부제 설정
        round.setImageResource(R.drawable.round_bool_01); // 초기에는 고개 1.
    }

    public void pageSet(int count){ // 화면을 표시하는 메소드
        sentence.setImageResource(bool_sentence[count]); // count가 조건문 고개 넘은 횟수이므로 0~2까지 대응.

        round.setImageResource(R.drawable.round_bool_01 + count); // count는 0~2이므로 더해주면 됨.
        text_score.setText(String.valueOf(status.getGameScore()).toString()); // 점수 갱신
    }

    public void checkBoolAnswer(boolean bool) // 조건문 고개 정답 검사.
    {
        CharSequence message; // 토스트 메시지.

        boolean answerResult = status.getChecker().checkBool(storage, count); // 정답 결과 저장.

        if(status.resultGameScore(answerResult)) // 정답 체크 : 정답이면 정답 부를 거고 아니면 오답 부름. 점수도 이 과정에서 합산.
        {
            message = "정답! ";
        }
        else
        {
            message = "땡! ";
        }

        switch (count)
        {
            case 0:
                message = message + "두 고개 남았습니다.";
                break;
            case 1:
                message = message + "한 고개 남았습니다.";
                break;
            case 2:
                message = message + "고개를 모두 넘었습니다.";
                break;
            default:
                message = "이런, 길잡이에 오류가 생겼네요.";
                break;
        }

        if(mainToast != null)
        {
            mainToast.cancel();
        }

        mainToast = Toast.makeText(Game_Bool_Base.this, message, Toast.LENGTH_SHORT);

        if(answerResult) // 정답이면 녹색, 틀렸으면 적색.
        {
            mainToast.getView().setBackgroundResource(R.drawable.toast_game_sucess);
        }
        else
        {
            mainToast.getView().setBackgroundResource(R.drawable.toast_game_fail);
        }

        final LinearLayout toastLayout = (LinearLayout) mainToast.getView();
        final TextView toastTV = (TextView) toastLayout.getChildAt(0);

        toastTV.setTypeface(Typeface.createFromAsset( getAssets(), "fonts/mn.ttf"));
        toastTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

        mainToast.show();

        count++; // 진행을 1회 하였으므로 넘은 횟수 1 증가.

        if(count == 3) // 3회 넘었을 때
        {
            sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
            status.setTime(time_count);
            countDownTimer.cancel(); // 시간 제거
            status.nextStage(Game_Bool_Base.this); // 다음 문제로
        }
        else // 아직 3회 넘지 않았을 때
        {
            pageSet(count);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_bool_base);

        setDefaultSetting();

        sos = SoundSetting.getInstance(); // 효과음 3셋
        sound_pool = new SoundPool( 10, AudioManager.STREAM_MUSIC, 0 );
        SoundID = sound_pool.load( Game_Bool_Base.this, R.raw.button, 1 ); // R.raw.sound에서 "sound"는 사운드 파일명

        ImageView button_true = (ImageView) findViewById(R.id.button_true);
        final ImageView button_false = (ImageView) findViewById(R.id.button_false);

        button_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 위에 있는 버튼 누를 경우
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                status.getChecker().setAnswersOfBoolAt(count, true); // 정답 설정.
                checkBoolAnswer(true);
            }
        });

        button_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 위에 있는 버튼 누를 경우
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                status.getChecker().setAnswersOfBoolAt(count, false); // 정답 설정.
                checkBoolAnswer(false);
                /*
                CharSequence message; // 토스트 메시지.

                if(status.resultGameScore(status.getChecker().checkBool(storage, count))) // 정답 체크 : 정답이면 정답 부를 거고 아니면 오답 부름. 점수도 이 과정에서 합산.
                {
                    message = "정답! ";
                }
                else
                {
                    message = "땡! ";
                }

                switch (count)
                {
                    case 0:
                        message = message + "두 고개 남았습니다.";
                        break;
                    case 1:
                        message = message + "한 고개 남았습니다.";
                        break;
                    case 2:
                        message = message + "고개를 모두 넘었습니다.";
                        break;
                    default:
                        message = "이런, 길잡이에 오류가 생겼네요.";
                        break;
                }

                Toast.makeText(Game_Bool_Base.this, message, Toast.LENGTH_SHORT).show();
                count++; // 진행을 1회 하였으므로 넘은 횟수 1 증가.

                if(count == 3) // 3회 넘었을 때
                {
                    sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
                    status.setTime(time_count); // 액티비티 종료 직전에 시간 넘겨주기
                    countDownTimer.cancel(); // 시간 제거
                    status.nextStage(Game_Bool_Base.this);
                }
                else // 아직 3회 넘지 않았을 때
                {
                    pageSet(count);
                }
                */
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void countDownTimer(){ // 시간 설정하는 메소드

        countDownTimer = new CountDownTimer((time_count + 2) * 1000, Game_Status.TIME_ONE_SECOND) { // 1000가 1초 즉 300초에서 1초씩으로 설정. 카운트가 2초 남았을 때 안 지나가므로 보정치 추가.
            public void onTick(long millisUntilFinished) { // 타이머 시작되면
                text_time.setText(String.valueOf(time_count - 1)); // 시간화면 표시
                time_count --; // 1초씩 뺌. 그 후에 적용.
            }
            public void onFinish() { // 시간이 끝날 경우
                sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
                status.setStatus(Game_Status.GAME_TIMEOVER); // 시간이 끝났으므로 타임오버 상태 저장.
                status.setTime(time_count); // 0을 줘도 되지만 확실하게 하기 위해 time_count를 줌.
                Intent intent = new Intent(getApplicationContext(), GameResult.class); // 클리어 체크할려고 보냄
                startActivity(intent);
                finish();
            }
        };

    }

    @Override
    public void onBackPressed() { // 취소버튼 눌렸을 경우
        sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); //효과음 재생
        LayoutInflater dialog = LayoutInflater.from(Game_Bool_Base.this); // 종료하시겠습니까? 안내창임
        final View dialogLayout = dialog.inflate(R.layout.dialog, null);
        final Dialog myDialog = new Dialog(Game_Bool_Base.this){
            @Override
            public void dismiss() {
                if(dialog_shown)
                {
                    dialog_shown = false;
                    time_count++; // 시간 보정
                    countDownTimer.start(); // 재시작
                }

                super.dismiss();
            }

            @Override
            public void cancel() {
                if(dialog_shown)
                {
                    dialog_shown = false;
                    time_count++; // 시간 보정
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
    protected void onStart() {
        sos.setBgmContinuous(false); // 시작할 때 false로 해줘야 이후에 일시정지 했을 때 bgm이 멈춘다.
        countDownTimer(); // 시작과 동시에 틀어줌
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
                time_count++; // 시간 보정
                countDownTimer.start(); // 타이머 시작
            }
        }

        super.onResume();
    }

    @Override
    public void onDestroy() { // 현 엑티비티가 종료될 경우
        super.onDestroy();
        try{
            countDownTimer.cancel();
        } catch (Exception e) {}
        countDownTimer=null;
        sound_pool.release();
    }
}
