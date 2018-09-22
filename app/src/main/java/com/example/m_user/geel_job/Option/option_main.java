package com.example.m_user.geel_job.Option;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.m_user.geel_job.GamePlay.Event.GameEventStatus;
import com.example.m_user.geel_job.GamePlay.Event.Game_event_content;
import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.R;
import com.example.m_user.geel_job.Tutorial.Storage.TutorialEventStoring;
import com.example.m_user.geel_job.Tutorial.Tutorial_event_content;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by M-user on 2017-06-20.
 * 환경설정입니다.
 */

public class option_main extends CustomFontActivity {

    // 뒤로가기 버튼 만들어야 함.*

    TutorialEventStoring tes = TutorialEventStoring.getInstance(); // 상태 저장용*
    GameEventStatus status = GameEventStatus.getInstance(); // 게임하기 관련 상태 저장용.

    private final static int MAX_VOLUME = 100; // 시그바 최대 크기

    SoundPool sound_pool;
    int SoundID;
    SoundSetting sos; // 효과음

    ImageView tutorialButton; // 튜토리얼 안내
    ImageView gameButton; // 게임하기 안내
    ImageView exitButton; // 나가기 버튼

    float volumeBgm; // 배경음 소리값 저장용
    float volumeEft; // 효과음 소리값 저장용

    static SeekBar bgSeekBar;
    static SeekBar effSeekBar;

    public void saveSoundData(String path) // 배경음 또는 효과음 설정 원본. 받는 문자열에 따라 달라짐.
    {
        String readStr = ""; // 파일 시스템 사용할려고 만듬

        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Geel_job"; // 파일 경로 지정
        String text; // 저장할 최종 데이터.

        File file = new File(sdPath) ; // 디렉터리 경로 저장

        file.mkdirs(); // 디렉토리 생성

        sdPath += path; // 파일 경로 지정
        file = new File(sdPath); // 파일 경로 저장

        FileWriter fw = null;
        try {
            file.createNewFile(); // 파일 생성
        } catch (IOException ie)
        {
            ie.printStackTrace();
        }

        try{
            BufferedReader br = new BufferedReader(new FileReader(file)); // 파일 일기 쓸려고 만듬

            String str = null;
            while(((str = br.readLine()) != null)){ // 끝까지 읽기
                readStr = str;
            }
            br.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        if(readStr == ""){ // 아무 값도 없으면
            Log.d("읽어들인 값이 존재하지 않습니다.", "경로는 "+sdPath);
        }
        else // 값이 있단 얘기이므로 한 번 비운다.
        {
            Log.d("이미 값이 존재합니다.", "현재 값을 지웁니다.");
            try { // 최종적으로 파일 쓰기
                // open file.
                text = "";

                fw = new FileWriter(file) ;

                // write file.

                fw.write(text) ; // 쓰기
                // fw.close(); // 닫기

            } catch (Exception e) {
                e.printStackTrace() ;
            }
        }

        try { // 최종적으로 파일 쓰기
            // open file.
            fw = new FileWriter(file) ;

            switch (path) // 배경음이냐 효과음이냐에 따라 달라짐.
            {
                case "/Background_Sound.gji":
                    //text = "" + (int)(sos.getVolumeBGM()*100); // 배경음 불러오기
                    text = "" + sos.getVolumeBGM(); // 배경음 불러오기
                    Log.d("배경음 저장", text);
                    fw.write(text) ; // 쓰기
                    fw.close(); // 닫기
                    break;
                case "/Effect_Sound.gji":
                    //text = "" + (int)(sos.getVolumeEFT()*100); // 효과음 불러오기
                    text = "" + sos.getVolumeEFT(); // 효과음 불러오기
                    Log.d("효과음 저장", text);
                    fw.write(text) ; // 쓰기
                    fw.close(); // 닫기
                    break;
                default:
                    text = "";
            }

            // write file.

        } catch (Exception e) {
            e.printStackTrace() ;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_main);

        tutorialButton = (ImageView) findViewById(R.id.tutorial_replay_button);
        gameButton = (ImageView) findViewById(R.id.game_replay_button);
        exitButton = (ImageView) findViewById(R.id.button_game_exit);

        //final MediaPlayer mp = Title_main.getMediaPlayer(); // 타이틀에서 배경음 정보 받기

        sos = SoundSetting.getInstance(); // 효과음 셋팅
        sound_pool = new SoundPool( 10, AudioManager.STREAM_MUSIC, 0 );
        SoundID = sound_pool.load( this, R.raw.button, 1 ); // R.raw.sound에서 "sound"는 사운드 파일명

        bgSeekBar = (SeekBar) findViewById(R.id.bgSeekBar); // 배경음 시그바
        //bgSeekBar.setMax(99); // 시그바 최대값 설정
        bgSeekBar.setProgress(sos.getProgressBGM()); // 시그바 위치 설정 저장된거 불러오기
        bgSeekBar.incrementProgressBy(1);
        bgSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /*
                int result;
                if(progress == 0)
                {
                    result = progress; // 무음으로 만들기 위해.
                }
                else
                {
                    result = progress + 1; // 실제 볼륨 크기는 1 ~ 100이므로 1 보정치 추가
                }
                */

                // volumeBgm = (float) (1 - (Math.log(MAX_VOLUME - progress) / Math.log(MAX_VOLUME))); // 소리값 셋딩 소리값 범위 0~1 시그바 범위 0~100 변환작업
                // volumeBgm = (float) (Math.log(MAX_VOLUME - progress) / Math.log(MAX_VOLUME)); // 소리값 셋딩 소리값 범위 0~1 시그바 범위 0~100 변환작업
                volumeBgm = (float) progress / (float) MAX_VOLUME; // 소리값 셋딩 소리값 범위 0~1 시그바 범위 0~100 변환작업
                sos.setVolumeBGM(volumeBgm); // 소리값 저장
                sos.setProgressBGM(progress); // 시그바 위치 저장
                //mp.setVolume(sos.getVolumeBGM(), sos.getVolumeBGM()); // 배경음 소리 조절
                sos.getBgm().setVolume(sos.getVolumeBGM(), sos.getVolumeBGM()); // 배경음 소리 조절


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

               // saveSoundData("/Background_Sound.gji");
            }
        });

        effSeekBar = (SeekBar) findViewById(R.id.effSeekBar); // 효과음 시그바
        //effSeekBar.setMax(99); // 시그바 최대값 설정
        effSeekBar.setProgress(sos.getProgressEFT()); // 시그바 위치 설정
        effSeekBar.incrementProgressBy(1);
        effSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { // 실행되는 분
                /*
                int result;
                if(progress == 0)
                {
                    result = progress; // 무음으로 만들기 위해.
                }
                else
                {
                    result = progress + 1; // 실제 볼륨 크기는 1 ~ 100이므로 1 보정치 추가
                }
                */
                //volumeEft = (float) (1 - (Math.log(MAX_VOLUME - progress) / Math.log(MAX_VOLUME))); // 소리값 설정
                //volumeEft = (float) (Math.log(MAX_VOLUME - result) / Math.log(MAX_VOLUME)); // 소리값 설정
                volumeEft = (float) progress / (float) MAX_VOLUME; // 소리값 설정
                sos.setVolumeEFT(volumeEft); // 소리값 저장
                sos.setProgressEFT(progress); // 시그바 위치 저장


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
               // saveSoundData("/Effect_Sound.gji");

            }
        });

        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 튜토리얼 다시보기*

                sos.setBgmContinuous(true); // 다시보기를 시작하므로 배경음 유지
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                tes.setTutorialEvent(true); // 환경설정에서 보기 때문에 이벤트 값을 true로 바꿔주어 환경설정 창으로 돌아올 수 있게 한다.*
                tes.setPageNum(1); // 이벤트를 시작하므로 1페이지로 초기화. *
                Intent intent = new Intent(getApplicationContext(), Tutorial_event_content.class);
                startActivity(intent);
            }
        });

        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 게임하기 다시보기*

                sos.setBgmContinuous(true); // 다시보기를 시작하므로 배경음 유지
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
                status.setGameEvent(true); // 환경설정에서 보기 때문에 이벤트 값을 true로 바꿔주어 환경설정 창으로 돌아올 수 있게 한다.*
                status.resetPage(); // 페이지 리셋
                Intent intent = new Intent(getApplicationContext(), Game_event_content.class);
                startActivity(intent);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 나가기
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
    public void finish() {
        // 끝나기 직전에 저장
        saveSoundData("/Background_Sound.gji");
        saveSoundData("/Effect_Sound.gji");

        super.finish();
    }

    @Override
    protected void onPause() {
        // 일시정지 상태에서 종료될 수 있으니 저장.
        saveSoundData("/Background_Sound.gji");
        saveSoundData("/Effect_Sound.gji");

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

    public void onBackPressed() { // 취소 버튼 누를 시 액티비티 종료
//        super.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      onBackPressed();
        sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
        finish();

    }

    @Override
    protected void onDestroy() { // 엑티비티 종료시
        super.onDestroy();
        sound_pool.release(); // file.close()랑 같은 거임
    }
}