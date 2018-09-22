package com.example.m_user.geel_job.GamePlay.Ranking;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m_user.geel_job.GamePlay.GameDynamicStatus;
import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.Option.SoundSetting;
import com.example.m_user.geel_job.R;
import com.example.m_user.geel_job.Title.Title_main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by M-user on 2017-09-08.
 * 마지막에 이름 입력하는 곳입니다.
 */

public class Game_Last_input extends CustomFontActivity {

    GameDynamicStatus status = GameDynamicStatus.getInstance(); // 게임 스테이터스 불러오기

    String grade1_name = ""; // 1등 이름
    int grade1_score = 0; // 1등 점수
    String grade2_name = ""; // 2등 이름
    int grade2_score = 0; // 2등 점수
    String grade3_name = ""; // 3등 이름
    int grade3_score = 0; // 3등 점수
    String grade4_name = ""; // 4등 이름
    int grade4_score = 0; // 4등 점수
    String grade5_name = ""; // 5등 이름
    int grade5_score = 0; // 5등 점수

    FileWriter fw_score = null; // 쓰기작업 할려고 만듬 점수 쓰기용
    FileWriter fw_name = null; // 이름 쓰기용

    String write_score; // 쓰기메소드 사용할때 1등,2등 등.. 구분용
    String write_name; // 쓰기메소드 사용할때 1등,2등 등.. 구분용

    SoundPool sound_pool; // 효과음 3셋
    int SoundID;
    SoundSetting sos;

    Toast mainToast; // 토스트 표시를 위해 만듬.

    public void readFile(int num){ // 파일 생성 및 읽어서 변수에 저장하는 역할를 하는 메소드
        String sdPathScore = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Geel_job";
        String sdPathName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Geel_job";
        File file_score = new File(sdPathScore) ;
        File file_name = new File(sdPathName) ;

        file_score.mkdirs();
        sdPathScore += "/grade"+num+"_score.gji";
        file_score = new File(sdPathScore);

        file_name.mkdirs();
        sdPathName += "/grade"+num+"_name.gji";
        file_name = new File(sdPathName);

        try {
            file_score.createNewFile(); // 파일 생성
            file_name.createNewFile();
        } catch (IOException ie) {
            ie.printStackTrace();
        } // 5등 집 만들기

        try{
            BufferedReader br_score = new BufferedReader(new FileReader(file_score));
            BufferedReader br_name = new BufferedReader(new FileReader(file_name));

            String str_score = null;
            String str_name = null;
            while(((str_score = br_score.readLine()) != null)){ // 읽기
                if(num == 5){
                    grade5_score = Integer.parseInt(str_score);
                }
                else if(num == 4){
                    grade4_score = Integer.parseInt(str_score);
                }
                else if(num == 3){
                    grade3_score = Integer.parseInt(str_score);
                }
                else if(num == 2){
                    grade2_score = Integer.parseInt(str_score);
                }
                else if(num == 1){
                    grade1_score = Integer.parseInt(str_score);
                }

            }
            while(((str_name = br_name.readLine()) != null)){ // 읽기
                if(num == 5){
                    grade5_name = str_name;
                }
                else if(num == 4){
                    grade4_name = str_name;
                }
                else if(num == 3){
                    grade3_name = str_name;
                }
                else if(num == 2){
                    grade2_name = str_name;
                }
                else if(num == 1){
                    grade1_name = str_name;
                }
            }
            br_score.close();
            br_name.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFile(int num){
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Geel_job/grade"+num+"_score.gji";
        File file_score = new File(sdPath);
        String sdPath1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Geel_job/grade"+num+"_name.gji";
        File file_name = new File(sdPath1);
        try {
            // open file.
            fw_score = new FileWriter(file_score) ;
            fw_name = new FileWriter(file_name) ;

            if(num == 5){
                write_score = String.valueOf(grade5_score).toString();
                write_name = grade5_name;
            }
            else if(num == 4){
                write_score = String.valueOf(grade4_score).toString();
                write_name = grade4_name;
            }
            else if(num == 3){
                write_score = String.valueOf(grade3_score).toString();
                write_name = grade3_name;
            }
            else if(num == 2){
                write_score = String.valueOf(grade2_score).toString();
                write_name = grade2_name;
            }
            else if(num == 1){
                write_score = String.valueOf(grade1_score).toString();
                write_name = grade1_name;
            }

            // write file.
            fw_score.write(write_score) ; // 쓰기
            fw_score.close();

            fw_name.write(write_name) ; // 쓰기
            fw_name.close();

        } catch (Exception e) {
            e.printStackTrace() ;
        } // 5등 집으로 보냄
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_last_input); // 사용 xml

        ImageView submit = (ImageView) findViewById(R.id.button_ranking_submit);
        final EditText edit = (EditText) findViewById(R.id.edit_text); // 아이디값 설정

        sos = SoundSetting.getInstance(); // 효과음 3셋
        sound_pool = new SoundPool( 10, AudioManager.STREAM_MUSIC, 0 );
        SoundID = sound_pool.load( Game_Last_input.this, R.raw.button, 1 ); // R.raw.sound에서 "sound"는 사운드 파일명


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 서브밋 버튼이 눌렸을 경우
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                if(edit.getText().toString().equals("")){ // 내용이 없음면

                    if(mainToast != null)
                        mainToast.cancel();

                    mainToast = Toast.makeText(Game_Last_input.this, "이름을 입력하지 않았습니다!", Toast.LENGTH_SHORT);

                    mainToast.getView().setBackgroundResource(R.drawable.toast_ranking);

                    final LinearLayout toastLayout = (LinearLayout) mainToast.getView();
                    final TextView toastTV = (TextView) toastLayout.getChildAt(0);

                    toastTV.setTypeface(Typeface.createFromAsset( getAssets(), "fonts/mn.ttf"));
                    toastTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

                    mainToast.show();
                }
                else { // 내용이 있으면
                    status.setUser_name(edit.getText().toString()); // 이름 저장

                    readFile(5);
                    readFile(4);
                    readFile(3);
                    readFile(2);
                    readFile(1);

                    if(status.getGameScore() >= grade5_score || grade5_name == ""){ // 토너먼트
                        grade5_score = status.getGameScore();
                        grade5_name = status.getUser_name();
                        if(grade5_score >= grade4_score || grade4_name == ""){ // 5등이 4등보다 점수가 높거나 , 4등이 비어있을 경우
                            int grade4_temp = 0;
                            String grade4_temp_name;

                            grade4_temp = grade4_score;
                            grade4_temp_name = grade4_name; // 패배자

                            grade4_score = grade5_score;
                            grade4_name = grade5_name; // 승리자

                            grade5_score = grade4_temp;
                            grade5_name = grade4_temp_name; // 불쌍
                            if(grade4_score >= grade3_score || grade3_name == ""){
                                int grade3_temp = 0;
                                String grade3_temp_name;

                                grade3_temp = grade3_score;
                                grade3_temp_name = grade3_name; // 패배자

                                grade3_score = grade4_score;
                                grade3_name = grade4_name; // 승리자

                                grade4_score = grade3_temp;
                                grade4_name = grade3_temp_name; // 불쌍
                                if(grade3_score >= grade2_score || grade2_name == ""){
                                    int grade2_temp = 0;
                                    String grade2_temp_name;

                                    grade2_temp = grade2_score;
                                    grade2_temp_name = grade2_name; // 패배자

                                    grade2_score = grade3_score;
                                    grade2_name = grade3_name; // 승리자

                                    grade3_score = grade2_temp;
                                    grade3_name = grade2_temp_name; // 불쌍
                                    if(grade2_score >= grade1_score || grade1_name == ""){
                                        int grade1_temp = 0;
                                        String grade1_temp_name;

                                        grade1_temp = grade1_score;
                                        grade1_temp_name = grade1_name; // 패배자

                                        grade1_score = grade2_score;
                                        grade1_name = grade2_name; // 승리자

                                        grade2_score = grade1_temp;
                                        grade2_name = grade1_temp_name; // 불쌍
                                    }
                                }
                            }
                        }
                    }

                    writeFile(5);
                    writeFile(4);
                    writeFile(3);
                    writeFile(2);
                    writeFile(1);

                    sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
                    Intent intent = new Intent(getApplicationContext(), Game_Last_output.class); // 랭킹 출력화면으로 보냄
                    startActivity(intent);
                    finish();
                }
            }
        });

        ImageView exitButton = (ImageView)findViewById(R.id.button_game_exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
    public void onBackPressed() { // 취소버튼 눌렸을 경우
        sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); //효과음 재생
        LayoutInflater dialog = LayoutInflater.from(Game_Last_input.this); // 종료하시겠습니까? 안내창임
        final View dialogLayout = dialog.inflate(R.layout.dialog, null);
        final Dialog myDialog = new Dialog(Game_Last_input.this);

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
}
