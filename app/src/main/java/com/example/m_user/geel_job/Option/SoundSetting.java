package com.example.m_user.geel_job.Option;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

import com.example.m_user.geel_job.Interface.CustomFontActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by M-user on 2017-09-11.
 * 여기는 소리값 상태를 소멸 안시키게 할려고 만듬
 * 값 저장소입니다.
 */

public class SoundSetting extends CustomFontActivity { // 배경음과 효과음 데이터 저장용. 동적으로 계속 불러와 쓴다.


    private static SoundSetting instance; // 싱글톤
    static float volumeBGM; // 배경음 소리 크기
    static float volumeEFT; // 효과음 소리 크기
    static int progressBGM; // 배경음 프로그래스 위치 값
    static int progressEFT;  // 효과음 프로그래스 위치 값

    private static boolean bgmContinuous; // 배경음 재생을 계속할 것인가 말 것인가를 결정.

    private static MediaPlayer bgm; // 배경음 정보

    private static String readStr = ""; // 파일 읽어오는데 공백일 경우를 검사

    public void setBgm(MediaPlayer mp)
    {
        bgm = mp;
    }

    public MediaPlayer getBgm()
    {
        return bgm;
    }

    private static void setSound(String path) // 배경음 또는 효과음 설정 원본. 받는 문자열에 따라 달라짐.
    {
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Geel_job"; // 파일 경로 지정
        File file = new File(sdPath) ; // 디렉터리 생성을 위한 경로 전달

        //if(!file.exists()) // 디렉터리가 없으면
        file.mkdirs(); // 디렉토리 생성

        sdPath += path; // 파일 경로 지정
        file = new File(sdPath); // 음량 값을 저장할 파일 생성을 위한 경로 전달

        FileWriter fw = null; // 파일 쓰기 객체. 이후에 정확한 값이 결정됨.

        try {
            file.createNewFile(); // 파일 생성
        } catch (IOException ie) {
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
            try {

                // 아무 값도 없으므로 기본값으로 돌린다.

                String inputText = ""; // 파일에 쓸 값

                switch (path) // 배경음이냐 효과음이냐에 따라 달라짐.
                {
                    case "/Background_Sound.gji":
                        volumeBGM = 1;
                        progressBGM = 100;
                        inputText = String.valueOf(volumeBGM);
                        break;
                    case "/Effect_Sound.gji":
                        volumeEFT = 1;
                        progressEFT = 100;
                        inputText = String.valueOf(volumeEFT);
                        break;
                }

                fw = new FileWriter(file) ;

                // write file.
                fw.write(inputText) ; // 쓰기
                fw.close();

            } catch (Exception e) {
                e.printStackTrace() ;
            }
        }
        else // 값이 있단 얘기이므로 불러와서 세팅한다.
        {
            switch (path) // 배경음이냐 효과음이냐에 따라 달라짐.
            {
                case "/Background_Sound.gji":
                    //volumeBGM = Float.parseFloat(readStr) / 100F;
                    //progressBGM = (int)(volumeBGM * 100); // 100배한 값이 자기 값.
                    //progressBGM = (int) (Float.parseFloat(readStr));
                    volumeBGM = Float.parseFloat(readStr);
                    progressBGM = (int)(volumeBGM * 100); // 실제 프로그레스는 0 ~100
                    break;
                case "/Effect_Sound.gji":
                    //volumeEFT = Float.parseFloat(readStr) / 100F;
                    // progressEFT = (int)(volumeEFT * 100); // 100배한 값이 자기 값.
                    //progressEFT = (int) (Float.parseFloat(readStr));
                    volumeEFT = Float.parseFloat(readStr);
                    progressEFT = (int)(volumeEFT * 100); // 실제 프로그레스는 0 ~100
                    break;
            }
        }
    }

    public static void setStartingSound() // 시작할 때의 배경음과 효과음 초기 설정. 파일에서 불러와 읽는다.
    {
        setSound("/Background_Sound.gji"); // 배경음
        setSound("/Effect_Sound.gji"); // 효과음
    }

    public static void setVolumeBGM(float vol){
        volumeBGM = vol;
    } // 셋팅하기
    public static void setVolumeEFT(float vol){
        volumeEFT = vol;
    }
    public static void setProgressBGM(int Pro){
        progressBGM = Pro;
    }
    public static void setProgressEFT(int Pro){
        progressEFT = Pro;
    }

    public static float getVolumeBGM(){
        return volumeBGM;
    } // 불러오기
    public static float getVolumeEFT(){
        return volumeEFT;
    }
    public static int getProgressBGM(){
        return progressBGM;
    }
    public static int getProgressEFT(){
        return progressEFT;
    }

    public static void setBgmContinuous(boolean b)
    {
        bgmContinuous = b;
    }

    public static boolean getBgmContinuous()
    {
        return bgmContinuous;
    }

    public static SoundSetting getInstance(){ // 싱글톤
        if(instance == null){
            instance = new SoundSetting();
        }
        return instance;
    }
}
