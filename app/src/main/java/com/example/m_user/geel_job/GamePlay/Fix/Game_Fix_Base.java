package com.example.m_user.geel_job.GamePlay.Fix;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m_user.geel_job.GamePlay.GameDynamicStatus;
import com.example.m_user.geel_job.GamePlay.GameResult;
import com.example.m_user.geel_job.GamePlay.Game_Status;
import com.example.m_user.geel_job.GamePlay.Storage.FixStorage;
import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.Option.SoundSetting;
import com.example.m_user.geel_job.R;

/**
 * Created by They on 2017-11-09.
 */

public class Game_Fix_Base extends CustomFontActivity {

    GameDynamicStatus status = GameDynamicStatus.getInstance(); // 상태 불러오기

    boolean dialog_shown = false; // 종료 창을 띄워놓고 있냐를 따지는 변수.

    int chance = 3; // 남은 기회

    FixStorage storage = (FixStorage) status.getChoice().getSelectedGamesAt(status.getStage());
    // 현재 진행중인 틀린 부분 찾기 정보 가져오기

    Toast mainToast; // 토스트 띄우기

    int using_layout = storage.getLayoutId();
    // 사용할 레이아웃 id

    TextView text_time; // 시간초 표시하는 텍스트뷰
    TextView text_score; // 점수 표시하는 텍스트뷰
    TextView chance_count; // 기회 표시하는 텍스트뷰

    ImageView subtitle; // 부제
    ImageView gameResource; // 풀 문제의 자세한 정보를 담은 이미지 id

    ImageView button_submit; // 실행 버튼
    ImageView button_run; // 실행 예시 보여주는 버튼

    ImageView clickable[] = new ImageView[storage.getClickableObject()];
    boolean[] clickable_toggle = new boolean[storage.getClickableObject()];
    // 클릭 가능한 버튼들을 배열로 초기화. 클릭 가능한 수만큼 동적으로 생성하며, for 문과 i를 조합하여 초기화.
    // toggle은 클릭 여부.

    ImageView exitButton; // 나가기 버튼

    private CountDownTimer countDownTimer; // 전역변수로 쓸려고 여기에 넣음
    int time_count = status.getTime(); // 초기 시간 가져오기

    SoundSetting sos;
    SoundPool sound_pool;
    int SoundID;

    int color_common; // 안 눌렀을 때의 색상.

    class FixOnClickListener implements OnClickListener // 틀린부분찾기 전용 리스너
    {

        int index; // 자신의 좌표에 맞는 토글 값을 불러내기 위한 인덱스 저장.

        public FixOnClickListener(int index)
        {
            this.index = index;
        }
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); //효과음 재생
            if(clickable_toggle[index]) // 이미 선택한 것을 다시 선택하면
            {
                clickable_toggle[index] = false; // 선택 해제
                clickable[index].setImageResource(color_common); // 선택 해제 이미지로.
            }
            else // 선택하지 않은 걸 선택하면
            {
                clickable_toggle[index] = true; // 선택
                clickable[index].setImageResource(R.color.game_fix_clickable_clicked); // 선택한 이미지로.
            }

            status.getChecker().setAnswersOfFixAt(index, clickable_toggle[index]); // 선택 반영.
        }
    }

    public void setPage() // 자세한 초기 화면 설정.
    {
        if(storage.getSubtitle() == Game_Status.GAME_FIX_CODE)
        {
            color_common = R.color.game_fix_clickable_code;
        }
        else
        {
            color_common = R.color.game_fix_clickable_common;
        }

        text_score.setText(String.valueOf(status.getGameScore()).toString()); // 점수 출력
        text_time.setText(String.valueOf(status.getTime()).toString());
        subtitle.setImageResource(storage.getFixTitle()); // 부제 설정
        gameResource.setImageResource(storage.getFixBackground()); // 문제 배경 설정
        chance_count.setText(String.valueOf(chance)); // 기회 설정

        for(int i = 0; i < clickable.length; i++)
        {
            clickable_toggle[i] = false; // 선택 여부 초기화
            clickable[i] = (ImageView) findViewById(R.id.clickable_object_01 + i); // 첫 번째 clickable에 i를 더하면서 하면 다 가져올 수 있음.
            clickable[i].setOnClickListener(new FixOnClickListener(i)); // 각각의 클릭 가능한 객체에 클릭 리스너 추가.
        }

        if(storage.getSubtitle() == Game_Status.GAME_FIX_CODE) // 틀린 코드 찾기이면
        {
            button_run = (ImageView) findViewById(R.id.button_game_run);
            button_run.setOnClickListener(new OnClickListener(){

                /**
                 * Called when a view has been clicked.
                 *
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); //효과음 재생
                    FixDialog dialog = new FixDialog(Game_Fix_Base.this);
                    dialog.show();
                }
            });
        }
    }

    public void setDefaultSetting() // 시작과 동시에 실행되는 기본 세팅
    {
        status.getChecker().setAnswers(status.getChoice().getIntentStyleAt(status.getStage()), storage.getClickableObject()); // 정답 초기화

        text_time = (TextView) findViewById(R.id.game_time);
        text_score = (TextView) findViewById(R.id.game_score);
        subtitle = (ImageView)findViewById(R.id.fix_subtitle);
        gameResource = (ImageView)findViewById(R.id.fix_background);
        exitButton = (ImageView) findViewById(R.id.button_game_exit); // 나가기 버튼.
        button_submit = (ImageView) findViewById(R.id.button_game_submit); // 실행 버튼
        chance_count = (TextView) findViewById(R.id.count_challenge); // 남은 실행 횟수 설정

        setPage(); // 자세한 설정은 여기에. 특히 이미지 위주.
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(using_layout); // 사용할 레이아웃 xml

        setDefaultSetting(); // 기본 세팅 시작

        sos = SoundSetting.getInstance(); // 효과음 3셋
        sound_pool = new SoundPool( 10, AudioManager.STREAM_MUSIC, 0 );
        SoundID = sound_pool.load( Game_Fix_Base.this, R.raw.button, 1 ); // R.raw.sound에서 "sound"는 사운드 파일명

        button_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); //효과음 재생
                if(status.resultGameScore(status.getChecker().checkAnswer(storage.getTypeResource(), storage))) // 정답 체크 : 정답이면 정답 부를 거고 아니면 오답 부름. 점수도 이 과정에서 합산.
                {
                    status.setTime(time_count); // 시간 저장
                    status.setStatus(Game_Status.GAME_CLEAR);

                    sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
                    countDownTimer.cancel(); // 시간 제거
                    Intent intent = new Intent(getApplicationContext(), GameResult.class); // 결과창으로
                    startActivity(intent); // 성공했을 때 바로 넘어가기 위함.
                    finish();
                }
                else
                {
                    chance--; // 기회 감소
                    chance_count.setText(String.valueOf(chance).toString()); // 찬스 표시.
                    text_score.setText(String.valueOf(status.getGameScore()).toString()); // 점수 출력
                    if(chance == 0) // 기회를 전부 소진했으면
                    {
                        status.setTime(time_count); // 시간 저장
                        status.setStatus(Game_Status.GAME_FAIL);
                        countDownTimer.cancel(); // 시간 제거

                        sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
                        Intent intent = new Intent(getApplicationContext(), GameResult.class); // 결과창으로
                        startActivity(intent); // 실패하고 기회 다 소진되면 넘어감.
                        finish();
                    }
                    else // 아직 기회가 남았다면.
                    {
                        if(mainToast != null)
                        {
                            mainToast.cancel();
                        }

                        mainToast = Toast.makeText(Game_Fix_Base.this , "다시 한 번 잘 보세요. "+chance+"회 남았습니다." , Toast.LENGTH_SHORT);
                        mainToast.getView().setBackgroundResource(R.drawable.toast_game_fail);

                        final LinearLayout toastLayout = (LinearLayout) mainToast.getView();
                        final TextView toastTV = (TextView) toastLayout.getChildAt(0);

                        toastTV.setTypeface(Typeface.createFromAsset( getAssets(), "fonts/mn.ttf"));
                        toastTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

                        mainToast.show();
                    }
                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() { // 취소버튼 눌렸을 경우
        sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); //효과음 재생
        final LayoutInflater dialog = LayoutInflater.from(this); // 종료하시겠습니까? 안내창임
        final View dialogLayout = dialog.inflate(R.layout.dialog, null);
        final Dialog myDialog = new Dialog(this){
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
        dialog_shown = true; // 다이얼로그 켠 상태 유지

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

        countDownTimer = new CountDownTimer((time_count + 2) * Game_Status.TIME_ONE_SECOND, Game_Status.TIME_ONE_SECOND) { // 1000가 1초 즉 300초에서 1초씩으로 설정. 카운트가 2초 남았을 때 안 지나가므로 보정치 추가.
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

            if (dialog_shown)
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

    // 다이얼로그 클래스로 다이얼로그 호출

    class FixDialog extends Dialog {

        ImageView dialog_image; // 다이얼로그 이미지

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // 다이얼로그 외부 화면 흐리게 표현
            WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
            lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            lpWindow.dimAmount = 0.4f;
            getWindow().setAttributes(lpWindow);

            setContentView(R.layout.dialog_fix);

            dialog_image = (ImageView) findViewById(R.id.dialog_fix_run);
            dialog_image.setImageResource(storage.getDialog()); // 다이얼로그 이미지 설정

            LinearLayout dialog_bg = (LinearLayout) findViewById(R.id.dialog_fix_run_bg);
            dialog_bg.setOnClickListener(new View.OnClickListener() { // 새 클릭 이벤트
                @Override
                public void onClick(View v) {
                    sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f );
                    cancel(); // 다이얼로그 취소.
                }
            });

        }

        // 클릭버튼이 하나일때 생성자 함수로 클릭이벤트를 받는다.
        public FixDialog(Context context) {
            super(context, android.R.style.Theme_Translucent_NoTitleBar);

        }
    }
}
