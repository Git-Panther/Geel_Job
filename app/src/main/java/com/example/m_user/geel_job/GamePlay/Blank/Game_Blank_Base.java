package com.example.m_user.geel_job.GamePlay.Blank;

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
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.m_user.geel_job.GamePlay.GameDynamicStatus;
import com.example.m_user.geel_job.GamePlay.GameResult;
import com.example.m_user.geel_job.GamePlay.Game_Status;
import com.example.m_user.geel_job.GamePlay.Listeners.DnDTouchListener;
import com.example.m_user.geel_job.GamePlay.Listeners.ETCOnDragListener;
import com.example.m_user.geel_job.GamePlay.Storage.BlankStorage;
import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.Option.SoundSetting;
import com.example.m_user.geel_job.R;

/**
 * Created by They on 2017-11-09.
 */

public class Game_Blank_Base extends CustomFontActivity {

    GameDynamicStatus status = GameDynamicStatus.getInstance(); // 상태 불러오기

    BlankStorage storage = (BlankStorage) status.getChoice().getSelectedGamesAt(status.getStage());
    // 현재 진행중인 빈칸 채우기 정보 가져오기

    boolean dialog_shown = false;

    int storage_type = storage.getTypeResource();
    // 현재 진행중인 빈칸이 넷 중 무엇인가 가져오기

    int using_layout = storage.getLayoutId();
    // 사용할 레이아웃 id. 빈칸 채우기는 사용할 레이아웃이 각각 다르다.

    TextView text_time; // 시간초 표시하는 텍스트뷰
    TextView text_score; // 점수 표시하는 텍스트뷰

    View extraView[];
    // 스케일 조정해주는 뷰.
    ViewGroup scalableLayout[];
    // scalableLayout 개수는 4개로 고정이므로 고정.

    SoundSetting sos;
    SoundPool sound_pool;
    int SoundID;

    ImageView button_submit; // 실행 버튼
    ImageView exitButton; // 나가기 버튼

    private CountDownTimer countDownTimer; // 전역변수로 쓸려고 여기에 넣음
    int time_count = status.getTime(); // 초기 시간 가져오기

    ImageView[] answer_edge = new ImageView[6]; // 정답의 빈칸 테두리

    LinearLayout[] answer_place = new LinearLayout[6]; // 정답 리니어

    ImageView[] clickable_answers = new ImageView[6]; // 정답 이미지뷰 참조

    // 정답 빈칸은 어느 것이든 고정 6개이므로 이렇게 처리함.

    // 이하는 빈칸 채우기 - 기본 베이스 스타일에 쓰이는 형식

    ImageView staticObject[];
    // 사용 여부와 관계 없이 총 표시되는 문제 객체 개수. 4개는 4개, 6개는 6개, 10개는 10개.

    ImageView usingObject[] = new ImageView[storage.getObjectAmount()];
    // 사용할 문제 제시용 오브젝트.

    ImageView staticBlankEdge[];
    // 사용 여부와 관계 없이 총 표시되는 빈칸 테두리. 4개는 4개, 6개는 6개, 10개는 10개.

    ImageView usingBlankEdge[] = new ImageView[storage.getAnswerAmount()];
    // 사용할 빈칸 테두리.

    LinearLayout[] staticBlank;
    // 사용 여부와 관계 없이 모든 빈칸들을 가져옴. 4개는 4개, 6개는 6개, 10개는 10개.

    LinearLayout[] usingBlank = new LinearLayout[storage.getAnswerAmount()]; // 사용 중인 빈칸 참조

    // 이하는 문장형을 포함한 빈칸 채우기

    ImageView sentence; // 풀 문제의 자세한 정보를 담은 이미지 id

    // 이하는 기능들을 분류

    public void setPage() // 자세한 초기 화면 설정.
    {
        status.getChecker().setAnswers(storage_type, storage.getAnswerAmount()); // 정답 배열을 먼저 생성해줘야 참조가 가능.

        switch (storage.getLayoutId()) // 빈칸 문제냐 아니면 문장 문제냐에 따라 id 가져오기가 달라짐. 쓰는 레이아웃의 id에 따라 달라짐.
        {
            case R.layout.game_blank_base: // 6개 최대 빈칸
                setBlankDefault(6);
                break;
            case R.layout.game_blank_base_4: // 4개 최대 빈칸
                setBlankDefault(4);
                break;
            case R.layout.game_blank_base_10: // 10개 최대 빈칸
                setBlankDefault(10);
                break;
            default: // 그밖의 배경 레이아웃 하나만 쓰고 나머지 두 세개 빈칸 채우는 경우
                setBlankSentence();
                break;
        }
    }

    public void setBlankDefault(int n) // 문장형이 아닌 것들에게만 적용
    {
        // n은 빈칸 스타일(위의 함수 참조할 것)

        staticObject = new ImageView[n]; // 사용 여부와 관계 없이 xml에 존재하는 모든 빈칸 리니어 내부의 이미지.
        staticBlankEdge = new ImageView[n]; // 사용 여부와 관계 없이 xml에 존재하는 모든 빈칸 틀.
        staticBlank = new LinearLayout[n];  // 사용 여부와 관계 없이 xml에 존재하는 모든 빈칸 리니어.

        for(int i = 0; i < n; i ++)
        {
            staticObject[i] = (ImageView) findViewById(R.id.blank_object01 + i);
            staticBlankEdge[i] = (ImageView) findViewById(R.id.blank_base01 + i);
            staticBlank[i] = (LinearLayout) findViewById(R.id.blank_place01 + i);
        }

        int j = 0; // 사용할 빈칸의 인덱스
        int k = 0; // 표시할 이미지의 인덱스

        for(int i = 0; i < storage.getObjectResource().length; i++)
        {
            //if( storage.getBlanks()[i])
            if(storage.getObjectResourceAt(i) == R.drawable.blank_blank) // 빈칸이 맞으면 빈칸 그대로 아니면 빼버리기. storage.getBlanks()[i]로도 가능.
            {
                usingBlank[j] = staticBlank[i];
                usingBlankEdge[j] = staticBlankEdge[i];

                usingBlank[j].removeAllViews(); // 사용할 빈칸 안에 있는 이미지뷰 전부 제거.

                j++; // j 인덱스 증가
            }
            else // storage.getObjectResourceAt(i) == 오브젝트 이미지
            {
                usingObject[k] = staticObject[i]; // 더미 데이터. 이제부턴 빈칸 틀을 이미지로 바꿈.
                staticBlankEdge[i].setImageResource(storage.getObjectResourceAt(i));

                // 빈칸을 못 쓰게 막기
                staticBlank[i].removeAllViews();
                staticBlank[i].setVisibility(View.INVISIBLE);

                k++;
            }
        }
    }

    public void setBlankSentence() // 문장형, 코드형, 기타 등등 배경 하나만 쓰는 경우는 이 세팅을 따름
    {
        staticObject = new ImageView[storage.getObjectResource().length]; // 빈칸 + 배경 수. 리스너 이벤트 하나로 다 쓰려면 규격 통일해야 해서 만들어줘야 함.
        staticBlankEdge = new ImageView[storage.getAnswerAmount()]; // 바로 위와 일치.
        staticBlank = new LinearLayout[storage.getAnswerAmount()]; // 바로 위와 일치.

        // 각 문제별로 다른 xml을 쓰기에 굳이 배경을 설정해줄 필요는 없음.

        sentence = (ImageView) findViewById(R.id.blank_sentence_background); // 배경 이미지 가져오기. 안 쓰지만은.

        for(int i = 0; i < storage.getAnswerAmount(); i++) // 정답 관련하여 설정
        {
            staticBlankEdge[i] = (ImageView) findViewById(R.id.blank_base01 + i); // 빈칸 테두리 id 가져옴. 사실 이거갖고 안 쓰는데 계륵.
            usingBlankEdge[i] = staticBlankEdge[i]; // 고정 수치와 사용 수치가 같음.

            staticBlank[i] = (LinearLayout) findViewById(R.id.blank_place01 + i); // 빈칸 설정
            usingBlank[i] = staticBlank[i]; // 사용 빈칸과 최대 빈칸이 일치.
        }

        for(int i = 0; i < storage.getObjectResource().length; i++)
        {
            if(i == storage.getObjectResource().length - 1) // 마지막에만 객체 추가. 배경을 설정한다는 의미를 제외하고는 의미 없고, 문장형은 각각의 배경을 쓰므로 무관.
            {
                staticObject[i] = sentence;
                usingObject[0] = sentence;
            }
            else
            {
                staticObject[i] = null;
            }
        }
    }

    public void setDefaultSetting() // 시작과 동시에 실행되는 기본 세팅
    {
        status.getChecker().setAnswers(status.getChoice().getIntentStyleAt(status.getStage()), storage.getAnswerAmount()); // 정답 초기화

        text_time = (TextView) findViewById(R.id.game_time); // 시간
        text_score = (TextView) findViewById(R.id.game_score); // 점수
        exitButton = (ImageView) findViewById(R.id.button_game_exit); // 나가기 버튼.
        button_submit = (ImageView) findViewById(R.id.button_game_submit); // 실행 버튼

        extraView = new View[]{ findViewById(R.id.extra_view01), findViewById(R.id.extra_view02), findViewById(R.id.extra_view03)};
        scalableLayout =  new ViewGroup[]
            {(ViewGroup)findViewById(R.id.scale01), (ViewGroup)findViewById(R.id.scale02), (ViewGroup)findViewById(R.id.scale03), (ViewGroup)findViewById(R.id.scale04)};

        // 드래그앤드롭 현상 해결
        for(int i = 0; i < 3; i ++)
        {
            extraView[i].setOnDragListener(ETCOnDragListener.getInstance());
        }

        for(int i = 0; i < 4; i ++)
        {
            scalableLayout[i].setOnDragListener(ETCOnDragListener.getInstance());
        }

        // id 등록 및 드래그앤 드롭 설정

        for(int i = 0; i < 6; i ++)
        {
            answer_edge[i] = (ImageView)findViewById(R.id.blank_answer_base01 + i);
            answer_place[i] = (LinearLayout) findViewById(R.id.blank_answer_place01 + i);
            clickable_answers[i] = (ImageView)findViewById(R.id.blank_answer01 + i);

            clickable_answers[i].setImageResource(storage.getAnswerImage()[i]);
            answer_place[i].setOnDragListener(new DnDBlankListener());
            answer_place[i].setTag((new StringBuilder("Answer_Place").append(i))); // 태그 없으면 그림자가 null을 참조하므로 있어야 함.

            clickable_answers[i].setTag((new StringBuilder("Answer").append(i))); // 태그 없으면 그림자가 null을 참조하므로 있어야 함.
            //clickable_answers[i].setOnLongClickListener(new DnDLongClickListener());
            clickable_answers[i].setOnTouchListener(new DnDTouchListener());
        }

        text_score.setText(String.valueOf(status.getGameScore()).toString()); // 초기 점수 출력
        text_time.setText(String.valueOf(status.getTime()).toString()); // 초기 시간 출력

        setPage(); // 문제 창을 설정. 복잡하므로 따로 놓음.

        button_submit.setOnClickListener(new View.OnClickListener() { // 확인 이벤트를 따로 만들어줘야 하는 이유 : 시간 반영.
                @Override
                public void onClick(View v) {
                    sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); //효과음 재생

                    if(status.resultGameScore(status.getChecker().checkAnswer(storage.getTypeResource(), storage))) // 정답 체크. 정답 체크와 동시에 점수 반영.
                    {
                        status.setStatus(Game_Status.GAME_CLEAR);
                    }
                    else // 틀렸으면
                    {
                        status.setStatus(Game_Status.GAME_FAIL);
                    }

                    sos.setBgmContinuous(true); // 다른 액티비티로 넘어가므로 배경음 유지.
                    countDownTimer.cancel(); // 시간 제거
                    status.setTime(time_count);
                    status.setResult(Game_Blank_Base.this);
                }
            }); // 확인 이벤트

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        for(int i = 0; i < usingBlank.length; i++) // 모든 객체가 설정된 후에야 DND 이벤트를 추가할 수 있다.
        {
            // usingBlank.get(i).setOnDragListener(new DnDBlankListener()); // 사용 중인 빈칸에는 드래그앤드롭 이벤트 추가

            usingBlank[i].setOnDragListener(new DnDBlankListener());
            usingBlank[i].setTag((new StringBuilder("Using Blank").append(i))); // 로그 확인용
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(using_layout); // 사용할 레이아웃 xml

        setDefaultSetting(); // 기본 세팅 시작

        sos = SoundSetting.getInstance(); // 효과음 3셋
        sound_pool = new SoundPool( 10, AudioManager.STREAM_MUSIC, 0 );
        SoundID = sound_pool.load( Game_Blank_Base.this, R.raw.button, 1 ); // R.raw.sound에서 "sound"는 사운드 파일명
    }

    @Override
    public void onBackPressed() { // 취소버튼 눌렸을 경우
        sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); //효과음 재생
        LayoutInflater dialog = LayoutInflater.from(Game_Blank_Base.this); // 종료하시겠습니까? 안내창임
        final View dialogLayout = dialog.inflate(R.layout.dialog, null);
        final Dialog myDialog = new Dialog(Game_Blank_Base.this){
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

    // 이하는 빈칸 채우기의 리스너 이벤트

    class DnDBlankListener extends Activity implements View.OnDragListener { // 빈칸 채우기 전용 드래그앤드롭 이벤트

        LinearLayout containView; // 현재 접근된 빈칸
        ImageView beforeImage; // 옮기려는 빈칸 자리에 현재 존재하는 정답 항목의 이미지뷰(변경 전)
        ImageView view; // 옮기고 있는 정답
        ViewGroup viewgroup; // 옮기고 있는 정답의 LinearLayout 가져오기
        ImageView afterImage; // 가장 최근에 빈칸 안으로 들어온 정답 항목의 이미지뷰(삽입, 변경 후)
        ImageView extraAfterImage; // 두 빈칸에서 각 항목들이 교체되었을 때, view로 들어온 beforeImage를 가져오기 위한 변수.
        boolean swapCount = true; // containView(옮기려는 빈칸) 안에 이미 다른 항목이 있으면 false, 비어있는 경우는 true. 밑의 for문으로 false가 될 수 있으니 기본값은 true.
        boolean checkCount = false; // 하나의 빈칸만 정답을 검사할 경우 false, 두 빈칸을 동시에 검사할 경우 true. 비어있던 빈칸에 항목을 넣는 경우도 하나만 바꾸기에 기본값은 false. swapTag()에 의해 결정.

        public boolean swapTag(int index) // 현재 빈칸에 무슨 정답이 들어있는지 검사하는 메소드. 들어가 있으면 들어간 것을 원위치로 바꾸고 옮기고 있는 걸 빈칸에 새로 넣는다.
        {
            for(int i = 0; i < answer_place.length; i++)
            {
                if(viewgroup.getTag() == answer_place[i].getTag()) // 옮기고 있는 정답이 원래 부모 리니어(answer_place[])로부터 왔다면. 즉 빈칸에 갱신되는 이미지가 하나뿐일 경우.
                {
                    Log.d("옮기는 이미지가 원래 자신의 곳에서 왔다.", "문제 빈칸에는 "+view.getTag()+"가 들어가고 "+beforeImage.getTag()+"는 원래 자리로 돌아간다.");
                    containView.removeView(beforeImage); // 빈칸 비우기
                    viewgroup.removeView(view); // 옮기고 있는 정답을 포함하고 있던 LinearLayout에서 정답 삭제

                    containView.addView(view); // 빈칸에 정답 추가.
                    answer_place[index].addView(beforeImage);

                    return false; // 실제 항목 교환은 1회밖에 안 하므로 다음 처리를 패스하고자 반환 처리. 갱신 이미지가 하나이므로 false.
                }
            }

            Log.d("문제 빈칸에 있는 둘을 교환", "교환 시작");
            // add
            containView.removeView(beforeImage); // 빈칸 비우기
            viewgroup.removeView(view); // 옮기고 있는 정답을 포함하고 있던 LinearLayout에서 정답 삭제

            containView.addView(view); // 빈칸에 정답 추가.
            if(containView != viewgroup) { // containView와 viewgroup이 서로 다른 것이면 옮기는 게 정상적으로 실행 가능하므로 실행. 하지만 일치한다면 두 번 추가하는 꼴이 되므로 추가하지 않는다.
                Log.d("둘은 서로 다른 빈칸", "둘을 서로 맞바꿉니다. "+beforeImage.getTag() + "는 "+viewgroup.getTag()+" 안으로, "+view.getTag()+"는 "+containView.getTag()+" 안으로 이동합니다.");
                viewgroup.addView(beforeImage); // 옮기고 있는 항목을 포함하고 있던 빈칸에는 view가 들어갈 빈칸에 있던 beforeImage를 추가.
            }
            else
            {
                Log.d("둘은 서로 같은 빈칸", "containView와 viewgroup이 같아 두 번 추가는 허용하지 않습니다.");
            }

            return true; // 갱신 이미지가 두 개가 되므로 true 반환.
        }


        public void checkAnswer(boolean b) // 고른 정답을 설정하는 메소드. false면 하나만. 아니면 두개를.
        {
            Log.d("정답을 반영합니다", "정답 반영 시작");
            afterImage = (ImageView) containView.getChildAt(0); // 현재 들어간 정답 항목 불러오기. beforeImage와는 달리 처리가 진행된 이후에 초기화해주므로 둘의 값은 다를 수 밖에 없다.
            if(b) { // 넣어햐 할 대상이 둘이면 extraAfterImage 설정.
                Log.d("이미 빈칸에 있던 둘을 정답 반영", "afterImage : "+afterImage+", extraAfterImage : "+extraAfterImage);
                extraAfterImage = (ImageView) viewgroup.getChildAt(0); // 두 번째 검사 대상의 빈칸에 들어간 항목 가져오기
            }
            else
            {
                Log.d("하나의 정답만 반영", "afterImage : "+afterImage.getTag());
            }

            int index = 0; // 비교할 번호 정하기. 0 ~ 정답 개수 - 1(빈칸 순서) 중 하나가 됨.
            int extraIndex = 0; // 정답 검사할 빈칸이 두개일 경우, 두 번째 빈칸 검사를 위한 인덱스

            for (int i = 0; i < storage.getAnswerAmount(); i++) { // 첫 번째 빈칸 배열의 번호 0~정답 개수-1 중 하나 추출하여 index에 저장.
                if (containView.getTag() == usingBlank[i].getTag()) {
                    Log.d("containView 보기", "containView는 "+ containView.getTag()+", index(반영할 정답 위치)의 값은 "+i);
                    index = i;
                    if(!b) // 정답 검사할 빈칸이 하나면
                    {
                        break;
                    }
                }

                if(b) { // 반영할 대상이 둘이면
                    if (viewgroup.getTag() == usingBlank[i].getTag()) // 두 번째 빈칸 배결의 번호 0-2중 하나 추출하여 저장. 해당사항 없으면 값은 변화하지 않음.
                    {
                        Log.d("viewgroup 보기", "viewgroup은 " + viewgroup.getTag() + ", extraIndex(반영할 정답 위치)의 값은 " + i);
                        extraIndex = i;
                        // 검사할 빈칸이 두 개면 index의 값도 설정해줘야 하기에 extraIndex의 값이 이미 설정되었다 하더라도 break; 를 쓸 수 없다.
                    }
                }
            }

            int insertingAnswer; // 삽입할 정답. 즉, 정답 설정시에 넣을 숫자.
            int extraInsertingAnswer; // 삽입할 정답이 두 개이면 하나 더 추가. 두 번째 것에 추가.

            for(int i = 0; i < clickable_answers.length; i++)
            {
                if (afterImage.getTag() == clickable_answers[i].getTag()) {
                    Log.d("afterImage 보기", "afterImage는 "+ afterImage.getTag()+", insertingAnswer(반영할 정답 값)의 값은 "+i);
                    insertingAnswer = i;
                    status.getChecker().setAnswersOfBlankAt(index, insertingAnswer);
                    if(!b) // 넣어야 할 값이 하나라면
                    {
                        break;
                    }
                }

                if(b) { // 넣어야 할 값이 둘이라면
                    if (extraAfterImage.getTag() == clickable_answers[i].getTag()) // 두 번째 빈칸 배결의 번호 0-2중 하나 추출하여 저장. 해당사항 없으면 값은 변화하지 않음.
                    {
                        Log.d("extraAfterImage 보기", "extraAfterImage는 "+ extraAfterImage.getTag()+", extraInsertingAnswer(반영할 정답 값)의 값은 "+i);
                        extraInsertingAnswer = i;
                        status.getChecker().setAnswersOfBlankAt(extraIndex, extraInsertingAnswer);
                        // 검사할 빈칸이 두 개면 index의 값도 설정해줘야 하기에 extraIndex의 값이 이미 설정되었다 하더라도 break; 를 쓸 수 없다.
                    }
                }
            }

        }


        @Override
        public boolean onDrag(View v, DragEvent event) {

            view = (ImageView) event.getLocalState(); // 옮기고 있는 정답.

            // 이벤트 시작
            switch (event.getAction()) {

                // 이미지를 드래그 시작될때
                case DragEvent.ACTION_DRAG_STARTED:
                    //Log.d("DragClickListener", "ACTION_DRAG_STARTED");
                    break;

                // 드래그한 이미지를 옮길려는 지역으로 들어왔을때
                case DragEvent.ACTION_DRAG_ENTERED:
                   // Log.d("DragClickListener", "ACTION_DRAG_ENTERED");
                    // 이미지가 들어왔다는 것을 알려주기 위해 배경이미지 변경
                    break;

                // 드래그한 이미지가 영역을 빠져 나갈때
                case DragEvent.ACTION_DRAG_EXITED:
                   // Log.d("DragClickListener", "ACTION_DRAG_EXITED");
                    break;

                // 이미지를 드래그해서 드랍시켰을때
                case DragEvent.ACTION_DROP:
                   // Log.d("DragClickListener", "ACTION_DROP");

                    viewgroup = (ViewGroup) view.getParent(); // 옮기고 있는 정답의 LinearLayout 가져오기

                    if(v instanceof LinearLayout) // 접근된 빈칸이 LinearLayout일 경우
                        containView = (LinearLayout) v; // 현재 접근된 빈칸

                    boolean switcher = false; // 이하 for 문에서 하나라도 걸러졌나 안 걸러졌냐 판단하는 놈

                    for(int i = 0; i < storage.getAnswerAmount(); i++)
                    {
                        if(containView.getTag() == usingBlank[i].getTag())
                        { // 빈칸 항목으로 존재하는 LinearLayout인가 검사
                            beforeImage = (ImageView) containView.getChildAt(0); // 이전에 그 자리에 있었던 정답 항목의 이미지뷰

                            //for(int j = 0; i < answer_place.size(); j++) { // 빈칸에 이미 항목이 들어가 있는지 1 ~ 6번째 버튼 다 검사
                            for(int x = 0; x < clickable_answers.length; x++) {
                                if ( (beforeImage != null) && (containView.getChildCount() == 1) && (beforeImage.getId() == clickable_answers[x].getId())) { // 1은 이미 자식이 있다는 것을 의미.
                                //if ( (beforeImage != null) && (beforeImage.getTag() == clickable_answers[i].getTag()) ) {
                                    Log.d("빈칸에 이미 항목이 존재합니다.", "둘을 교체합니다.");
                                    swapCount = false; // 이미 빈칸에 항목이 존재하므로 밑의 if문으로 교체할 필요가 없어져 false 설정
                                    checkCount = swapTag(x); // 빈칸에 들어왔으면 빈칸 안에 어느 이미지뷰 클래스가 들어가 있는지 검사하여 교체할지 말지 결정. 동시에 정답 체크 기준 설정.
                                    break; // 한 번 검사 끝났으므로 반복문 탈출. 버튼 옮길 때 swapTag()는 한 번만.
                                }
                            }

                            if(swapCount) // 위 for문으로 swapTag(i)가 발생하지 않았을 때(= 넣을 빈칸에 아무것도 없으면)
                            {
                                Log.d("비어있는 곳으로 들어갑니다.", "빈칸으로 이동");
                                viewgroup.removeView(view); // 옮기고 있는 정답을 포함하고 있던 LinearLayout에서 정답 삭제
                                containView.addView(view); // 빈칸에 정답 추가.
                            }

                            checkAnswer(checkCount); // 옮긴 이후 정답 확인

                            switcher = true;
                            break; // 하나라도 걸러냈기에 나간다.
                        }
                    }

                    if(!switcher) // 위 for 문에서 하나라도 안 걸러졌으면
                    {
                        if (view.getVisibility() == View.INVISIBLE) // 만일 옮기고 있던 정답이 보여지지 않는 상태이면
                            view.setVisibility(View.VISIBLE); // 원상태로 돌려서 그 자리에 보이던 상태로 되돌림.
                    }

                    // DROP 이벤트 종료 직전에 boolean 변수를 초기화하여 다음 옮기는 이벤트에서 사고가 나지 않게 한다.
                    swapCount = true;
                    checkCount = false;

                    Log.d("결과 공개", "containView(접근한 빈칸)의 자식 수 : "+containView.getChildCount() + ", viewGroup(옮기고 있던 객체의 부모)의 자식 수 : "+viewgroup.getChildCount());

                    break;

                case DragEvent.ACTION_DRAG_ENDED: // 드랍이 끝났을 때. 가끔씩 제대로 드랍하지도 않고 끝나는 경우 있기에 보여주게끔은 해야 한다.
                   // Log.d("DragClickListener", "ACTION_DRAG_ENDED");
                    if (view.getVisibility() == View.INVISIBLE) // 만일 옮기고 있던 정답이 보여지지 않는 상태이면
                        view.setVisibility(View.VISIBLE); // 원상태로 돌려서 그 자리에 보이던 상태로 되돌림.

                    break;

                default:
                    break;
            }
            return true;
        }
    }
}
