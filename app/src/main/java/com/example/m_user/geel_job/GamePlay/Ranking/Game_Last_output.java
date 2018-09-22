package com.example.m_user.geel_job.GamePlay.Ranking;

import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.m_user.geel_job.GamePlay.GameDynamicStatus;
import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.Option.SoundSetting;
import com.example.m_user.geel_job.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by M-user on 2017-09-08.
 * 랭킹출력하는 화면입니다.
 */

public class Game_Last_output extends CustomFontActivity {

    GameDynamicStatus status = GameDynamicStatus.getInstance();

    LinkedList<String> grade_name = new LinkedList<>(); // 플레이어 이름을 문자열로 저장
    LinkedList<Integer> grade_score = new LinkedList<>(); // 플레이어 점수를 정수로 저장

    LinkedList<TextView> rank_number_view = new LinkedList<>(); // 랭킹 넘버들
    LinkedList<TextView> grade_score_view = new LinkedList<>(); // 각 플레이어 점수들
    LinkedList<TextView> grade_name_view = new LinkedList<>(); // 각 플레이어 이름들

    SoundPool sound_pool;
    int SoundID;
    SoundSetting sos; // 효과음 재생

    public  void readFile(int num){ // 파일 생성 및 읽어서 변수에 저장하는 역할를 하는 메소드
        String sdPathOfScore = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Geel_job";
        String sdPathOfName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Geel_job";
        File file_score = new File(sdPathOfScore) ; // 경로 등록
        File file_name = new File(sdPathOfName) ; // 경로 등록

        file_score.mkdirs();
        sdPathOfScore += "/grade"+(num + 1)+"_score.gji"; // 0 ~ 4이므로 1 ~ 5로 보정
        file_score = new File(sdPathOfScore);

        file_name.mkdirs();
        sdPathOfName += "/grade"+(num + 1)+"_name.gji"; // 0 ~ 4이므로 1 ~ 5로 보정
        file_name = new File(sdPathOfName);

        try {
            file_score.createNewFile(); // 파일 생성
            file_name.createNewFile();
        } catch (IOException ie) {
            ie.printStackTrace();
        } // 5등 집 만들기

        try{
            BufferedReader br_score = new BufferedReader(new FileReader(file_score));
            BufferedReader br_name = new BufferedReader(new FileReader(file_name));

            String str_score = null; // 스코어 불러오기
            String str_name = null; // 이름 불러오기

            while(((str_score = br_score.readLine()) != null)){ // 읽기
                grade_score.set(num, Integer.parseInt(str_score));
            }

            while(((str_name = br_name.readLine()) != null)){ // 읽기
                grade_name.set(num, str_name);
            }

            try // 이름이 있냐 없냐 검사. 있으면 다 출력하지만 없으면 안 한다.
            {
                if(grade_name.get(num) == "")
                {
                    Log.d("비어있는 값", (num + 1)+"등의 항목을 가립니다.");
                    rank_number_view.get(num).setVisibility(View.INVISIBLE);
                    grade_name_view.get(num).setVisibility(View.INVISIBLE);
                    grade_score_view.get(num).setVisibility(View.INVISIBLE);
                }
                else
                {
                    Log.d("존재하는 값", (num + 1)+"등의 항목을 갱신합니다.");
                    grade_score_view.get(num).setText(String.valueOf(grade_score.get(num)).toString()); // 점수 갱신
                    grade_name_view.get(num).setText(String.valueOf(grade_name.get(num)).toString()); // 이름 갱신
                }
            }
            catch (NullPointerException e)
            {
                e.printStackTrace();
            }

            br_score.close();
            br_name.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_last_output); // 사용 xml

        sos = SoundSetting.getInstance(); // 효과을 3셋
        sound_pool = new SoundPool( 10, AudioManager.STREAM_MUSIC, 0 );
        SoundID = sound_pool.load( Game_Last_output.this, R.raw.button, 1 ); // R.raw.sound에서 "sound"는 사운드 파일명

        TextView textOfStage = (TextView) findViewById(R.id.stage); // 맨위에 스테이지 표시
        TextView textOfScore = (TextView) findViewById(R.id.score); // 점수 표시
        TextView textOfTime = (TextView) findViewById(R.id.timer_text); // 시간 표시

        // 이하는 스코어 id 불러오기. 값마다 떨어져있는 위치가 달라서 개별적으로 설정함.

        grade_score_view.add((TextView)findViewById(R.id.grade1_score));
        grade_score_view.add((TextView)findViewById(R.id.grade2_score));
        grade_score_view.add((TextView)findViewById(R.id.grade3_score));
        grade_score_view.add((TextView)findViewById(R.id.grade4_score));
        grade_score_view.add((TextView)findViewById(R.id.grade5_score));

        // 이하는 이름 id 불러오기. 값마다 떨어져있는 위치가 달라서 개별적으로 설정함.

        grade_name_view.add((TextView) findViewById(R.id.grade1_name));
        grade_name_view.add((TextView) findViewById(R.id.grade2_name));
        grade_name_view.add((TextView) findViewById(R.id.grade3_name));
        grade_name_view.add((TextView) findViewById(R.id.grade4_name));
        grade_name_view.add((TextView) findViewById(R.id.grade5_name));

        rank_number_view = new LinkedList<>();

        // 이하는 숫자 id 불러오기. 값마다 떨어져있는 위치가 달라서 개별적으로 설정함.

        rank_number_view.add((TextView) findViewById(R.id.rank_01));
        rank_number_view.add((TextView) findViewById(R.id.rank_02));
        rank_number_view.add((TextView) findViewById(R.id.rank_03));
        rank_number_view.add((TextView) findViewById(R.id.rank_04));
        rank_number_view.add((TextView) findViewById(R.id.rank_05));

        String stageString;
        String scoreString;
        String timeString;

        if(status.getStage() == -1) // 랭킹 보기 선택으로 보면
        {
            stageString = "NONE";
            scoreString = "NONE";
            timeString = "NONE";

            textOfStage.setVisibility(View.INVISIBLE);
            textOfScore.setVisibility(View.INVISIBLE);
            textOfTime.setVisibility(View.INVISIBLE);

            findViewById(R.id.score_title).setVisibility(View.INVISIBLE);
            findViewById(R.id.time_title).setVisibility(View.INVISIBLE);
            findViewById(R.id.stage_title).setVisibility(View.INVISIBLE);
        }
        else
        {
            stageString = String.valueOf(status.getStage()).toString();
            scoreString = String.valueOf(status.getGameScore()).toString();
            timeString = String.valueOf(status.getTime()).toString(); // 맨위에 세개 출력할려고 만듬.
        }

        textOfStage.setText(stageString);
        textOfScore.setText(scoreString);
        textOfTime.setText(timeString); // 맨위 세개 출력하는 역할

        for(int x = 0; x < 5; x++) // 0 ~ 4까지
        {
            grade_score.add(0);
            grade_name.add("");
            readFile(x);
        }

        ImageView exitButton = (ImageView)findViewById(R.id.button_game_exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // status.resetGame();
    }

    @Override
    public void onBackPressed() { // 취소버튼 눌렸을 경우
        sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); //효과음 재생
        LayoutInflater dialog = LayoutInflater.from(Game_Last_output.this); // 종료하시겠습니까? 안내창임
        final View dialogLayout = dialog.inflate(R.layout.dialog, null);
        final Dialog myDialog = new Dialog(Game_Last_output.this);

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
        sound_pool.release();
    }
}
