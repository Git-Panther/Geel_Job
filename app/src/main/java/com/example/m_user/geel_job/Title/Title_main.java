package com.example.m_user.geel_job.Title;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.m_user.geel_job.GamePlay.Event.Game_event_content;
import com.example.m_user.geel_job.GamePlay.Game_List;
import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.Option.SoundSetting;
import com.example.m_user.geel_job.Option.option_main;
import com.example.m_user.geel_job.R;
import com.example.m_user.geel_job.Tutorial.Storage.TutorialEventStoring;
import com.example.m_user.geel_job.Tutorial.List.Tutorial_List;
import com.example.m_user.geel_job.Tutorial.Tutorial_event_content;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by M-user on 2017-06-05.
 *메인 화면 입니다.
 */

public class Title_main extends CustomFontActivity {

    private static MediaPlayer mp; // 배경음 쓸때 필요한거
    int mpcount = 1; // 중복 재생 방지용

    SoundPool sound_pool;
    int SoundID; // 효과음 사용할려면 2개 필요함
    SoundSetting sos; // 효과음 소리 상태 저장할려고 만듬

    static final String TAG = "TedPark"; // 권한 묻는 건데 나도 자세히 모름

    Toast mainToast; // 토스트 띄우기. 토스트 중복 방지를 위한 것임.

    TutorialEventStoring tes = TutorialEventStoring.getInstance(); // 실행할 때 한번만 스테이터스 기본 정보(장의 소단원 정보)를 초기화하려고 씀

    public class tutorial_button implements View.OnClickListener{ // 튜토리얼 버튼 누를 시
        @Override
        public void onClick(View v) {

            sos.setBgmContinuous(true); // 튜토리얼을 시작하므로 배경음 유지
            sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
//            Intent intent = new Intent(getApplicationContext(),Tutorial_List.class); // 튜토리얼 리스트 자바파일
//            startActivity(intent);
            String readStr = ""; // 파일 시스템 사용할려고 만듬

            String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Geel_job"; // 파일 경로 지정
            File file = new File(sdPath) ;

            file.mkdirs(); // 디렉토리 생성

            sdPath += "/Tutorial_check.gji"; // 파일 경로 지정
            file = new File(sdPath);
            FileWriter fw = null;
            try {
                file.createNewFile(); // 파일 생성
            } catch (IOException ie) {

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
                Intent intent = new Intent(getApplicationContext(), Tutorial_event_content.class); // 맨 처음일 경우 설명
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(getApplicationContext(), Tutorial_List.class); // 기존이면 기존리스트 보여줌
                startActivity(intent);
            }
        }
    }

    public class gameplay_button implements View.OnClickListener{ // 게임하기 버튼 누를 시
        @Override
        public void onClick(View v) {
            sound_pool.play(SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f); // 효과음 재생

            String readStr = ""; // 파일 시스템 사용할려고 만듬. 따로 선언해야 위의 튜토리얼 접근 권한과 겹치지 않음.

            String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Geel_job"; // 파일 경로 지정
            File file = new File(sdPath) ; // 파일 경로 갱신

            file.mkdirs(); // 디렉토리 생성. 있으면 그대로 유지된다.

            sdPath += "/CH1_CLEAR.gji"; // 파일 경로 지정
            file = new File(sdPath); // 파일 경로 갱신

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

            if(readStr == ""){ // 1장을 클리어하지 않았다면

                if(mainToast != null) // 이미 토스트가 존재하면
                    mainToast.cancel();

                // 이하는 토스트 텍스트, 모양, 폰트, 크기 조정
                mainToast = Toast.makeText(Title_main.this, "1장을 완료하고 오세요!", Toast.LENGTH_SHORT); // 토스트 객체 생성

                mainToast.getView().setBackgroundResource(R.drawable.toast_cannot_acess); // 토스트 모양 결정

                final LinearLayout toastLayout = (LinearLayout) mainToast.getView(); // 토스트의 뷰 가져오기(텍스트뷰 가져오기 위함)
                final TextView toastTV = (TextView) toastLayout.getChildAt(0); // 토스트의 텍스트뷰 가져오기(메시지)

                // toastTV.setTypeface(Typeface.createFromAsset( getAssets(), "fonts/KoPubDotumBold.ttf")); // 토스트의 텍스트 변경
                toastTV.setTypeface(Typeface.createFromAsset( getAssets(), "fonts/mn.ttf")); // 토스트의 텍스트 변경
                toastTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

                mainToast.show();
            }
            else // 1장을 클리어했으면 이벤트를 이미 보았냐 혹은 처음 보는가에 따라 달라짐.
            {
                // 이하는 이벤트 추가

                FileWriter fw = null;
                Intent intent;

                readStr = ""; // 파일 시스템 사용할려고 만듬
                sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Geel_job"; // 파일 경로 지정
                file = new File(sdPath) ;

                file.mkdirs(); // 디렉토리 생성

                sdPath += "/Game_check.gji"; // 파일 경로 지정
                file = new File(sdPath);
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

                if(readStr == ""){ // 이벤트를 한 번도 본 적이 없다면
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
                else // 이벤트를 이미 봤다면
                {
                    intent = new Intent(getApplicationContext(), Game_List.class); // 게임하기 장 선택 페이지
                }

                sos.setBgmContinuous(true); // 게임하기를 시작하므로 배경음 유지
                startActivity(intent);
            }
        }
    }

    public class option_button implements View.OnClickListener{ // 설정 버튼 누를 시
        @Override
        public void onClick(View v) {

            sos.setBgmContinuous(true); // 환경설정을 시작하므로 배경음 유지
            sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); // 효과음 재생
            Intent intent = new Intent(getApplicationContext(),option_main.class); // 메인 옵션 화면 자바파일
            startActivity(intent);
        }
    }

    public class close_button implements View.OnClickListener{ // 종료 버튼 누를 시
        @Override
        public void onClick(View v) {
            onBackPressed(); // 맨밑에 있음
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) { // 메인 메소드
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title); // 레이아웃 폴더 타이틀.xml

        tes.getStatus().setChapters(); // 각 장 정보 초기화

        sos = SoundSetting.getInstance(); // 효과음 상태 저장용 객체 생성
        sound_pool = new SoundPool( 10, AudioManager.STREAM_MUSIC, 0 ); // 효과음 설정
        SoundID = sound_pool.load( this, R.raw.button, 1 ); // R.raw.sound에서 "sound"는 사운드 파일명

        sos.setBgmContinuous(false); // 시작할 때 false 값을 주어 일시정지 했을 때 멈추도록 한다.

        if (Build.VERSION.SDK_INT >= 23) { // 권한 묻는 거 안드로이드 버전이 api 23이상이면 onRequestPermissionsResult()여기로
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
            }else{
                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(Title_main.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }else{
            Log.d(TAG, "External Storage Permission is Grant ");
        }


        ImageView btnNewActivity1 = (ImageView)findViewById(R.id.t_button1); // 튜토리얼
        ImageView btnNewActivity2 = (ImageView)findViewById(R.id.t_button2); // 게임하기
        ImageView btnNewActivity3 = (ImageView)findViewById(R.id.t_button3); // 옵션
        ImageView btnNewActivity4 = (ImageView)findViewById(R.id.t_button4); // 종료

        btnNewActivity1.setOnClickListener(new tutorial_button()); // 튜토리얼 버튼 설정
        btnNewActivity2.setOnClickListener(new gameplay_button()); // 게임하기 버튼 설정
        btnNewActivity3.setOnClickListener(new option_button()); // 옵션 버튼 설정
        btnNewActivity4.setOnClickListener(new close_button()); // 종료 버튼 설정


    }

    public void onBackPressed() { // 취소 버튼 누를 시
//        super.onBackPressed();
        sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); //효과음 재생
        LayoutInflater dialog = LayoutInflater.from(this); // 종료하시겠습니까? 안내창임
        final View dialogLayout = dialog.inflate(R.layout.dialog, null);
        final Dialog myDialog = new Dialog(this);

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setContentView(dialogLayout);
        myDialog.show();

        ImageView btn_ok = (ImageView)dialogLayout.findViewById(R.id.btn_ok);
        ImageView btn_cancel = (ImageView)dialogLayout.findViewById(R.id.btn_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f );
                myDialog.dismiss(); // 다이얼로그 닫기
                finish();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f );
                myDialog.cancel();
            }
        });
    }

    @Override
    protected void onStart() { // 어플 시작시

        super.onStart();
        if(mpcount != 0) { // 음성 중복 재생 방지
            mpcount = 0;
            mp = MediaPlayer.create(this, R.raw.blues_blast); // 배경음 경로 설정
            sos.setBgm(mp); // 배경음 정보 저장.

            sos.setStartingSound(); // 기본 사운드 결정. 수치만 정해주므로 밑의 setVolume이 있어야 함.

            sos.getBgm().start(); // 배경음 재생
            sos.getBgm().setLooping(true); // 음악 반복 재생
            sos.getBgm().setVolume(sos.getVolumeBGM(), sos.getVolumeBGM()); //소리 크기 설정

            sos.setBgmContinuous(false); // 초기값 설정은 false. 그래야 일시정지 했을 때에 배경음 멈춤.
        }

    }

    @Override
    protected void onPause() { // 일시정지했을 때 배경음 정지. 즉, 배경음이 길잡이 창을 일시적으로 내렸을 때 더 재생되지 못하도록 멈춤.

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
    protected void onRestart() { // 앱 재시작할 때
        super.onRestart();
    }

    @Override
    protected void onResume() { // 앱을 다시 실행시켰을 때

        // 초기화
        tes.resetAct();

        if(sos.getBgmContinuous()) // 배경음이 유지 중이라면
        {

        }
        else // 배경음이 멈춘 상태라면.
        {
            sos.getBgm().start(); // 배경음 다시 재생.
        }

        Log.d("배경음 값", ""+ sos.getVolumeBGM());
        Log.d("효과음 값", ""+sos.getVolumeEFT());
        Log.d("배경음 시크값", ""+ sos.getProgressBGM());
        Log.d("효과음 시크값", ""+sos.getProgressEFT());

        super.onResume();
    }

    @Override
    protected void onDestroy() { // 현재 엑티비티 종료시
        super.onDestroy();
        sound_pool.release(); // 효과음 file.close()랑 같은거임
        mp.release(); // 배경음 위와 동일
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { // 권한 요청 창
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= 23) {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){ // 확인 버튼 누르시 권한 적용
                Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                //resume tasks needing this permission
            }
            else { // 취소 누를 시
                finish(); // 종료
            }
        }
    }

    public static MediaPlayer getMediaPlayer(){
        return mp; // option_main에 보낼려고 만듬
    }
}
