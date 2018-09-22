package com.example.m_user.geel_job.GamePlay.Order;

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
import com.example.m_user.geel_job.GamePlay.Storage.OrderStorage;
import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.Option.SoundSetting;
import com.example.m_user.geel_job.R;

import java.util.ArrayList;

/**
 * Created by They on 2017-11-09.
 */

public class Game_Order_Base extends CustomFontActivity {
    GameDynamicStatus status = GameDynamicStatus.getInstance(); // 상태 불러오기

    OrderStorage storage = (OrderStorage) status.getChoice().getSelectedGamesAt(status.getStage());
    // 현재 진행중인 빈칸 채우기 정보 가져오기

    boolean dialog_shown = false; // 종료 창을 띄워놓고 있냐를 따지는 변수.

    int storage_type = storage.getTypeResource();
    // 현재 진행중인 빈칸이 넷 중 무엇인가 가져오기

    int using_layout = storage.getLayoutId();
    // 사용할 레이아웃 id. 빈칸 채우기는 사용할 레이아웃이 각각 다르다.

    TextView text_time; // 시간초 표시하는 텍스트뷰
    TextView text_score; // 점수 표시하는 텍스트뷰

    View extraView[];
    // 스케일 조정해주는 뷰. 3개 고정.
    ViewGroup scalableLayout[];
    // scalableLayout 개수는 4개로 고정이므로 고정.

    SoundSetting sos;
    SoundPool sound_pool;
    int SoundID;

    ImageView button_submit; // 실행 버튼
    ImageView exitButton; // 나가기 버튼

    private CountDownTimer countDownTimer; // 전역변수로 쓸려고 여기에 넣음
    int time_count = status.getTime(); // 초기 시간 가져오기

    //LinkedList<ImageView> answer_edge = new LinkedList<>();
    //LinkedList<ViewGroup> answer_place = new LinkedList<>();
    //LinkedList<ImageView> clickable_answers = new LinkedList<>();

    ImageView[] answer_edge = new ImageView[4]; // 정답의 빈칸 테두리. 4개 고정.
    LinearLayout[] answer_place = new LinearLayout[4]; // 정답 리니어. 4개 고정.
    ImageView[] clickable_answers = new ImageView[4]; // 정답 이미지뷰

    // 정답 빈칸은 어느 것이든 고정 4개이므로 이렇게 처리함.

    ImageView subtitle; // 순서 맞추기의 부제.

    //LinkedList<ImageView> orderObject = new LinkedList<>();
    ImageView orderObject[];
    // 사용 여부와 관계 없이 빈칸에 이미 들어가 있는 답. 5 ~ 10
    // 이 중에서 대응되는 값에 따라 사용할 값은 활성화되고 사용되지 않는 값은 비활성화됨.

    //LinkedList<ImageView> orderBlankEdge = new LinkedList<>();
    ImageView orderBlankEdge[];
    // 사용 여부와 관계 없이 총 표시되는 빈칸 테두리. 5 ~ 10. orderObject와 동일.

    //LinkedList<ImageView> orderNumber = new LinkedList<>();
    ImageView orderNumber[];
    // 사용 여부와 관계 없이 총 표시되는 넘버링. 5 ~ 10. 위와 동일.

    // LinkedList<ViewGroup> orderBlank = new LinkedList<>();
    LinearLayout orderBlank[];
    // 사용 여부와 관계 없이 모든 빈칸들을 가져옴. 5 ~ 10;
    // 이들에게는 전부 ETC 이벤트 추가.

    //LinkedList<ViewGroup> usingBlank = new LinkedList<>();
    LinearLayout usingBlank[] = new LinearLayout[storage.getOrderAmount()];
    /* 사용할 빈칸 개수. 사용할 개수는 storage를 통해 미리 가져올 수 있다. 어느 빈칸 문제든 이걸 사용함. */
    // 이번 경우는 0 ~ 사용 개수 -1 까지 가져오고 그것들에게 리스너 이벤트 부여.

    DnDOrderListener onlyOnThis = new DnDOrderListener(); // 내부 클래스는 싱글톤 사용이 불가능하므로 이렇게 하나 만듬. 어차피 하나 풀면 끝나니 의미 없다.

    ArrayList<ImageView> usingObject = new ArrayList<>(); // 사용중인 오브젝트들을 저장. 검사 용도로 쓰임.

    /*
    Iterator<ImageView> iterOfanswers = clickable_answers.iterator();
    Iterator<ImageView> iterOfobjects = orderObject.iterator();
    Iterator<ViewGroup> iterOfAP =  answer_place.iterator();
    Iterator<ViewGroup> iterOfUB = usingBlank.iterator();
    */

    // int[] chosenAnswer; // 선택한 정답 간단하게 비교하려고 가져옴.

    // 이하는 문장형을 포함한 빈칸 채우기

    public void setInvisible(int style) // 순서 맞추기가 무얼 가리고 무얼 보이냐를 결정.
    {
        for(int i = 0; i < style; i ++)
        {
            if(i < storage.getOrderAmount()) // 순서 범위 이내면
            {
                if(storage.getBlanks()[i]) // 빈칸 처리라면
               // if(orderBlankEdge.get(i).getVisibility() == View.VISIBLE) // 빈칸 처리라면
                //if(orderNumber.get(i).getVisibility() == View.VISIBLE) // 보인다면 등록
                {
                    // orderNumber[i].setImageResource(R.drawable.order_number_01 + i);
                }
            }
           else // 사용하지 않는 순서 제거. 7부터는 마지막만 제거해버리면 그만이지만 5 이하로는 두 개 제거해줘야 하므로 이리 함.
            {
                orderNumber[i].setVisibility(View.INVISIBLE);
                orderBlankEdge[i].setVisibility(View.INVISIBLE);
                orderObject[i].setVisibility(View.INVISIBLE);
                orderBlank[i].removeAllViews();
                orderBlank[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    public void setPage() // 자세한 초기 화면 설정.
    {
        status.getChecker().setAnswers(storage_type, storage.getAnswerAmount()); // 정답 배열을 먼저 생성해줘야 참조가 가능.
        // chosenAnswer = status.getChecker().getAnswersOfOrder(); // 정답 배열 생성 후 참조.

        switch (storage.getLayoutId()) // 빈칸 문제냐 아니면 문장 문제냐에 따라 id 가져오기가 달라짐. 쓰는 레이아웃의 id에 따라 달라짐.
        {
            case R.layout.game_order_base_5: // 최대 5개 오브젝트
                setOrderDefault(5);
                break;
            case R.layout.game_order_base_6: // 최대 6개 오브젝트
                setOrderDefault(6);
                break;
            case R.layout.game_order_base_8: // 최대 8개 오브젝트
                setOrderDefault(8);
                break;
            case R.layout.game_order_base_10: // 최대 10개 오브젝트
                setOrderDefault(10);
                break;
            default: // 오류가 났을 때
                //finish();
                break;
        }
    }

    public void setOrderDefault(int n) // 순서 맞추기 기본 세팅 중 더욱 세밀한 작업.
    {
        orderObject = new ImageView[n]; // 움직일 놈 중 이미 들어간 놈
        orderNumber = new ImageView[n]; // 넘버링
        orderBlankEdge = new ImageView[n]; // 빈칸 틀
        orderBlank = new LinearLayout[n]; // 빈칸 이벤트

        for(int i = 0; i < n; i ++)
        {
            orderNumber[i] = (ImageView) findViewById(R.id.order_number_01 + i);
            orderObject[i] = (ImageView) findViewById(R.id.order_object_01 + i);
            orderBlankEdge[i] = (ImageView) findViewById(R.id.order_base_01 + i);
            orderBlank[i] = (LinearLayout) findViewById(R.id.order_place_01 + i);

            if((orderNumber[i] == null))
                Log.d("null 값 참조", "현재 orderNumber가 "+i+"번째 항목이 null 값을 참조하고 있습니다.");

            if((orderObject[i] == null))
                Log.d("null 값 참조", "현재 orderObject가 "+i+"번째 항목이 null 값을 참조하고 있습니다.");

            if((orderBlankEdge[i] == null))
                Log.d("null 값 참조", "현재 orderBlankEdge가 "+i+"번째 항목이 null 값을 참조하고 있습니다.");

            if((orderBlank[i] == null))
                Log.d("null 값 참조", "현재 orderBlank가 "+i+"번째 항목이 null 값을 참조하고 있습니다.");

            orderBlank[i].setOnDragListener(ETCOnDragListener.getInstance()); // 그 이외의 움직임. 1차적으로는 모두에게 ETC를 부여.

            orderObject[i].setTag((new StringBuilder("Object ").append(i))); // 태그 없으면 그림자가 null을 참조하므로 있어야 함.
            // orderObject[i].setOnLongClickListener(new DnDLongClickListener()); // 모두가 움직일 수 있게 한다. 이 과정에서 떨어져나간 놈들은 뷰에서 사라지므로 만질 수도 없어서 움직이지도 못하므로 상관없다.
            orderObject[i].setOnTouchListener(new DnDTouchListener());
        }

        setInvisible(n); // 넘버링 작업

        int j = 0; // 사용할 빈칸의 인덱스(usingBlank)

        for(int i = 0; i < storage.getOrderAmount(); i++) // 순서 개수 범위 이내에서 처리. 이미 순서 밖의 것은 setInvisible(n)으로 처리했기 때문.
        {
            if(orderBlankEdge[i].getVisibility() == View.VISIBLE) // 순서를 채워야 하는 공간이라면
            //if(storage.getBlanks()[i]) // 빈칸 처리한 부분이라면
            //if(orderBlank.get(i).getVisibility() == View.VISIBLE) // 볼 수 있는 빈칸이면
            {
                usingBlank[j] = orderBlank[i];
                // usingBlank[j] = orderBlank[i]; // 범위 이내이므로 사용하는 것에 등록.

                if (storage.getOrderPieceAt(i) == 0) // 빈칸에 등록된 이미지가 없으면 그 안에 들어간 항목 빼버리기
                {
                    usingBlank[j].getChildAt(0).setVisibility(View.INVISIBLE);
                    usingBlank[j].removeAllViews();
                    //usingBlank[j].removeAllViews(); // 사용할 빈칸 안에 있는 이미지뷰 전부 제거.
                }
                else // 빈칸에 오브젝트가 들어가야 한다면.
                {
                    //orderObject.get(i).setImageResource(storage.getOrderPieceAt(i));
                    orderObject[i].setImageResource(storage.getOrderPieceAt(i));
                    orderObject[i].setTag((new StringBuilder("orderObject ").append(i))); // 태그 없으면 그림자가 null을 참조하므로 있어야 함.
                    Log.d("사용 중인 오브젝트 등록", "usingObject에 : "+orderObject[i].getTag()+"가 삽입되었습니다.");
                    usingObject.add(orderObject[i]); // i번째 오브젝트는 사용하기 때문에 사용 중인 오브젝트 리스트에 등록.
                }
                j++; // j 인덱스 증가
            }
        }

        Log.d("사용 중인 오브젝트 등록 완료", "usingObject의 총 개수 : "+usingObject.size());
    }

    public void setDefaultSetting() // 시작과 동시에 실행되는 기본 세팅
    {
        status.getChecker().setAnswers(status.getChoice().getIntentStyleAt(status.getStage()), storage.getAnswerAmount()); // 정답 배열 생성.

        text_time = (TextView) findViewById(R.id.game_time); // 시간
        text_score = (TextView) findViewById(R.id.game_score); // 점수
        exitButton = (ImageView) findViewById(R.id.button_game_exit); // 나가기 버튼.
        button_submit = (ImageView) findViewById(R.id.button_game_submit); // 실행 버튼
        subtitle = (ImageView) findViewById(R.id.order_subtitle); // 부제
        subtitle.setImageResource(storage.getOrderTitle());

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

        // 드래그앤 드롭 설정

        for(int i = 0; i < 4; i ++)
        {
            // 이하는 id 가져오기
            answer_edge[i] = (ImageView)findViewById(R.id.order_answer_base_01 + i);
            answer_place[i] = (LinearLayout) findViewById(R.id.order_answer_place_01 + i);
            clickable_answers[i] = (ImageView)findViewById(R.id.order_answer_01 + i);

            answer_place[i].setTag((new StringBuilder("answer_place ").append(i))); // 굳이 태그 붙이는 이유는 로그 쉽게 보기 위해. 로그 덕에 디버깅하는데 성공함.
            answer_place[i].setOnDragListener(onlyOnThis); // 모든 정답의 최초 부모 리니어에 DnD 이벤트 추가
            clickable_answers[i].setImageResource(storage.getAnswerImage()[i]); // 정답에 이미지 부여

            clickable_answers[i].setTag((new StringBuilder("clickable_answer ").append(i))); // 태그 없으면 그림자가 null을 참조하므로 있어야 함.
            //clickable_answers[i].setOnLongClickListener(new DnDLongClickListener()); // 모든 정답 항목에 드래그 가능한 이벤트 추가
            clickable_answers[i].setOnTouchListener(new DnDTouchListener());
        }

        text_score.setText(String.valueOf(status.getGameScore()).toString()); // 초기 점수 출력
        text_time.setText(String.valueOf(status.getTime()).toString()); // 초기 시간 출력

        setPage(); // 문제 창을 설정. 복잡하므로 따로 놓음.

        for(int i = 0; i < usingBlank.length; i++) // 모든 객체가 설정된 후에야 빈칸에 DND 이벤트를 추가할 수 있다.
        {
            usingBlank[i].setTag((new StringBuilder("usingBlank ").append(i))); // 태그 없으면 그림자가 null을 참조하므로 있어야 함.
            usingBlank[i].setOnDragListener(onlyOnThis); // 사용 중인 빈칸에는 드래그앤드롭 이벤트 추가
        }

        sos = SoundSetting.getInstance(); // 효과음 3셋
        sound_pool = new SoundPool( 10, AudioManager.STREAM_MUSIC, 0 );
        SoundID = sound_pool.load( Game_Order_Base.this, R.raw.button, 1 ); // R.raw.sound에서 "sound"는 사운드 파일명

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
                status.setResult(Game_Order_Base.this);
            }
        }); // 확인 이벤트
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        status.getChecker().setStartingAnswerOfOrder(storage); // 시작할 때의 정답 설정.
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(using_layout); // 사용할 레이아웃 xml

        setDefaultSetting(); // 기본 세팅 시작
    }

    @Override
    public void onBackPressed() { // 취소버튼 눌렸을 경우
        sound_pool.play( SoundID, sos.getVolumeEFT(), sos.getVolumeEFT(), 0, 0, 1f ); //효과음 재생
        LayoutInflater dialog = LayoutInflater.from(Game_Order_Base.this); // 종료하시겠습니까? 안내창임
        final View dialogLayout = dialog.inflate(R.layout.dialog, null);
        final Dialog myDialog = new Dialog(Game_Order_Base.this){
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

    // 이하는 리스너 이벤트

    class DnDOrderListener implements View.OnDragListener { // 순서 맞추기 전용 드래그앤드롭 이벤트

        LinearLayout containView; // 현재 접근된 빈칸
        ImageView beforeImage; // 옮기려는 빈칸 자리에 현재 존재하는 정답 항목의 이미지뷰(변경 전)
        ImageView view; // 옮기고 있는 정답
        ViewGroup viewgroup; // 옮기고 있는 정답의 LinearLayout 가져오기
        ImageView afterImage; // 가장 최근에 빈칸 안으로 들어온 정답 항목의 이미지뷰(삽입, 변경 후)
        ImageView extraAfterImage; // 두 빈칸에서 각 항목들이 교체되었을 때, view로 들어온 beforeImage를 가져오기 위한 변수.
        boolean swapCount = true; // containView(옮기려는 빈칸) 안에 이미 다른 항목이 있으면 false, 비어있는 경우는 true. 밑의 for문으로 false가 될 수 있으니 기본값은 true.
        boolean checkCount = false; // 하나의 빈칸만 정답을 검사할 경우 false, 두 빈칸을 동시에 검사할 경우 true. 비어있던 빈칸에 항목을 넣는 경우도 하나만 바꾸기에 기본값은 false. swapTag()에 의해 결정.
        // boolean twice = true; // 순서 맞추기에서 정답 설정이 두 번 일어나는 현상이 있는데, 그것을 막기 위한 boolean. true면 교체를 도와주고 false가 됨. false면 이미 교체했다는 의미니까 true가 되고 정답 체크를 끝내버림.

        // 이하 기호 상수들은 checkAnswer() 전용.

        final int RESULT_NO_CHANGE = -100; // 안 바뀜
        final int RESULT_CHANGED_ONE = -1000; // 하나 바뀜
        final int RESULT_CHANGED_TWO = -2000; // 둘 바뀜

        int result = RESULT_NO_CHANGE; // 정답이 얼마나 반영되었는가를 표시하는 기호 상수. 초기값은 하나도 안 바뀐 걸로.

        public int getInsertingAnswer() // 하나만 변경되었거나 첫 번째 것을 변경할 때 쓰는 기능
        {
            Log.d("첫 번째 정답을 반영합니다", "첫 번째 정답 반영 시작");

            int id = 0; // 아이디 값을 얻어 반환할 예정.
            for(int i = 0; i < clickable_answers.length; i++)
            {
                // 정답 중에서 afterImage 찾기
                if (afterImage.getId() == clickable_answers[i].getId())
                //if (view.getId() == clickable_answers[i].getId())
                {
                    id = clickable_answers[i].getId();
                    Log.d("정답과 매칭되는 id를 찾았습니다", "정답으로 제시된 항목 중에 있었습니다. id는 "+clickable_answers[i].getTag());

                    if(result == RESULT_CHANGED_TWO) // 두 번 다 바꿨다는 건 결국 다 바꿨다는 걸 의미.
                    {
                        return id;
                    }
                    else if(result == RESULT_CHANGED_ONE) // 이미 한 번 바뀌었다면
                    {
                        result = RESULT_CHANGED_TWO; // 두 번 바뀜. 두 번 바뀌었으므로 바로 반환.
                        return id;
                    }
                    else if(result == RESULT_NO_CHANGE) // 이전에 바꾼 적이 없다면
                    {
                        result = RESULT_CHANGED_ONE; // 하나 바뀜.
                    }
                    else // 해당사항 없음
                    {

                    }

                    return id; // 이미 찾았으므로 더 찾을 필요 없다.
                }
            }

            // 제시된 항목 중에서 afterImage 찾기
            //for(int i = 0; i < orderObject.length; i++)
            for(int i = 0; i < usingObject.size(); i++)
            {
                //if(orderObject[i].getVisibility() == View.VISIBLE) // 보이는 객체, 즉 안보이게끔 한 사전에 처리한 오브젝트들은 검사 대상에서 제외
                //{
                    if (afterImage.getId() == usingObject.get(i).getId()) // 오브젝트 중에서 일치하는 게 있다면
                    {
                    //if (afterImage.getId() == orderObject[i].getId()) { // 오브젝트 중에서 일치하는 게 있다면
                    //if (view.getId() == orderObject[i].getId()) { // 오브젝트 중에서 일치하는 게 있다면
                        //id = orderObject[i].getId();
                        id = usingObject.get(i).getId();
                        //Log.d("정답과 매칭되는 id를 찾았습니다", "시작할 때 빈칸에 이미 들어가 있던 항목 중에 있었습니다. id는 "+orderObject[i].getTag());
                        Log.d("정답과 매칭되는 id를 찾았습니다", "시작할 때 빈칸에 이미 들어가 있던 항목 중에 있었습니다. id는 "+usingObject.get(i).getTag());

                        if(result == RESULT_CHANGED_TWO) // 두 번 다 바꿨다는 건 결국 다 바꿨다는 걸 의미.
                        {
                            return id;
                        }
                        else if(result == RESULT_CHANGED_ONE) // 이미 한 번 바뀌었다면
                        {
                            result = RESULT_CHANGED_TWO; // 두 번 바뀜. 두 번 바뀌었으므로 바로 반환.
                            return id;
                        }
                        else if(result == RESULT_NO_CHANGE) // 이전에 바꾼 적이 없다면
                        {
                            result = RESULT_CHANGED_ONE; // 하나 바뀜.
                        }
                        else // 해당사항 없음
                        {

                        }

                        return id;
                    }
                //}
            }

            Log.d("찾기 실패", "정답 항목이나 시작할 때 빈칸 안에 들어간 객체 중에는 없습니다.");
            return -1; // 실패를 의미. 첫 번째 실패 때 이상현상 발생하므로 여기서 태클걸어주면 될 듯?
        }

        public int getExtraInsertingAnswer() // 둘 다 변경되었을 때, 두 번째 것을 찾는다.
        {
            Log.d("두 번째 정답을 반영합니다", "두 번째 정답 반영 시작");

            int id = 0; // 아이디 값을 얻어 반환할 예정.

            for(int i = 0; i < clickable_answers.length; i++) {

                // 정답 중에서 extraAfterImage 찾기
                if (extraAfterImage.getId() == clickable_answers[i].getId()) // 두 번째 빈칸 배결의 번호 0-2중 하나 추출하여 저장. 해당사항 없으면 값은 변화하지 않음.
                //if (beforeImage.getId() == clickable_answers[i].getId()) // 두 번째 빈칸 배결의 번호 0-2중 하나 추출하여 저장. 해당사항 없으면 값은 변화하지 않음.
                {
                    id = clickable_answers[i].getId();
                    Log.d("두 번째 정답과 매칭되는 id를 찾았습니다", "정답으로 제시된 항목 중에 있었습니다. id는 "+clickable_answers[i].getTag());

                    if (result == RESULT_CHANGED_TWO) // 두 번 다 바꿨다는 건 결국 다 바꿨다는 걸 의미.
                    {
                        return id;
                    }
                    else if (result == RESULT_CHANGED_ONE) // 이미 한 번 바뀌었다면
                    {
                        result = RESULT_CHANGED_TWO; // 두 번 바뀜. 두 번 바뀌었으므로 바로 반환.
                        return id;
                    }
                    else if (result == RESULT_NO_CHANGE) // 이전에 바꾼 적이 없다면
                    {
                        result = RESULT_CHANGED_ONE; // 하나 바뀜.
                    }
                    else // 해당사항 없음
                    {

                    }

                    return id; // 이미 찾았으므로 더 찾을 필요 없다.
                }
            }

            // 시작할 때 이미 들어가 있는 오브젝트들 중에서 찾기
            for(int i = 0; i < usingObject.size(); i++)
            //for(int i = 0; i < orderObject.length; i++)
            {
                //if(orderObject[i].getVisibility() == View.VISIBLE) // 보이는 객체, 즉 안보이게끔 한 사전에 처리한 오브젝트들은 검사 대상에서 제외
                //{
                    if (extraAfterImage.getId() == usingObject.get(i).getId()) { // 오브젝트 중에서 일치하는 게 있다면
                    //if (extraAfterImage.getId() == orderObject[i].getId()) { // 오브젝트 중에서 일치하는 게 있다면
                    //if (beforeImage.getId() == orderObject[i].getId()) { // 오브젝트 중에서 일치하는 게 있다면
                        id = usingObject.get(i).getId();
                        //id = orderObject[i].getId();

                        //Log.d("두 번째 정답과 매칭되는 id를 찾았습니다", "시작할 때 빈칸에 이미 들어가 있던 항목 중에 있었습니다. id는 "+orderObject[i].getTag());
                        Log.d("두 번째 정답과 매칭되는 id를 찾았습니다", "시작할 때 빈칸에 이미 들어가 있던 항목 중에 있었습니다. id는 "+usingObject.get(i).getTag());

                        if(result == RESULT_CHANGED_TWO) // 두 번 다 바꿨다는 건 결국 다 바꿨다는 걸 의미.
                        {
                            return id;
                        }
                        else if(result == RESULT_CHANGED_ONE) // 이미 한 번 바뀌었다면
                        {
                            result = RESULT_CHANGED_TWO; // 두 번 바뀜. 두 번 바뀌었으므로 바로 반환.
                            return id;
                        }
                        else if(result == RESULT_NO_CHANGE) // 이전에 바꾼 적이 없다면
                        {
                            result = RESULT_CHANGED_ONE; // 하나 바뀜.
                        }
                        else // 해당사항 없음
                        {

                        }

                        return id;
                    }
                //}
            }

            Log.d("찾기 실패", "정답 항목이나 시작할 때 빈칸 안에 들어간 객체 중에는 없습니다.");
            return -1; // 실패를 의미
        }

        public boolean swapTag()
        {
            for(int i = 0; i < answer_place.length; i++)
            {
                if(viewgroup.getTag() == answer_place[i].getTag()) // 옮기고 있는 정답이 원래 부모 리니어(answer_place[])로부터 왔다면. 즉 빈칸에 갱신되는 이미지가 하나뿐일 경우.
                {
                    Log.d("옮기는 이미지의 부모 리니어는 정답 칸.", containView.getTag()+"에는 "+view.getTag()+"가 들어가고 "+beforeImage.getTag()+"는 "+ viewgroup.getTag()+ "으로 돌아간다.");
                    containView.removeView(beforeImage); // 옮기고 있는 항목이 들어갈 빈칸(LinearLayout)에 존재했던 항목 삭제
                    viewgroup.removeView(view); // 옮기고 있는 정답을 포함하고 있던 LinearLayout에서 정답 삭제

                    containView.addView(view); // 빈칸에 정답 추가.
                    viewgroup.addView(beforeImage); // 옮기고 있는 항목을 포함하고 있던 빈칸에는 view가 들어갈 빈칸에 있던 beforeImage를 추가.

                    return false; // 실제 항목 교환은 1회밖에 안 하므로 다음 처리를 패스하고자 반환 처리. 갱신 이미지가 하나이므로 false.
                }
            }

            Log.d("문제 빈칸에 있는 둘을 교환", "교환 시작");
            // 순서 맞추기는 둘의 순서를 무조건 바꾸는 방향으로 구현해야 정답 항목에 없던 오브젝트도 정답 선택창으로 내려와서 재설정이 가능해짐.

            containView.removeView(beforeImage); // 옮기고 있는 항목이 들어갈 빈칸(LinearLayout)에 존재했던 항목 삭제
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
                //Log.d("이미 빈칸에 있던 둘을 정답 반영", "afterImage : "+afterImage+", extraAfterImage : "+extraAfterImage);
                Log.d("이미 빈칸에 있던 둘을 정답 반영", "afterImage : "+view.getTag()+", extraAfterImage : "+beforeImage.getTag());
                extraAfterImage = (ImageView) viewgroup.getChildAt(0); // 두 번째 검사 대상의 빈칸에 들어간 항목 가져오기
            }
            else
            {
                //Log.d("하나의 정답만 반영", "afterImage : "+afterImage.getId());
                Log.d("하나의 정답만 반영", "afterImage : "+view.getTag());
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

            //if(twice) { // 한 번도 안 바꿨다면 true, 한 번 이미 바꿨다면 false가 된다.
                Log.d("1회 변경", "변경을 시작합니다.");
                int insertingAnswer = getInsertingAnswer(); // 삽입할 정답. 즉, 정답 설정시에 넣을 숫자.
                if(insertingAnswer == -1)
                    Log.d("정답 반영 실패", "첫 번째 정답을 찾는 데 실패했습니다.");
                else
                {
                    status.getChecker().setAnswersOfOrderAt(index, insertingAnswer); // 첫 번째 대상 정답 반영
                    //Log.d("첫 번째 정답 반영 성공", "afterImage : " + afterImage.getId() + ", 삽입 위치 : " + index);
                    Log.d("첫 번째 정답 반영 성공", "afterImage : " + view.getTag() + ", 삽입 위치 : " + index);
                }

                int extraInsertingAnswer; // 삽입할 정답이 두 개이면 하나 더 추가. 두 번째 것에 추가.
                if (b) // 반영할 대상이 둘이면
                {
                    extraInsertingAnswer = getExtraInsertingAnswer();

                    if(extraInsertingAnswer == -1)
                        Log.d("정답 반영 실패", "두 번째 정답을 찾는 데 실패했습니다.");
                    else {
                        status.getChecker().setAnswersOfOrderAt(extraIndex, extraInsertingAnswer); // 두 번째 대상 정답 반영
                        //Log.d("두 번째 정답 반영 성공", "extraAfterImage : " + extraAfterImage.getId() + ", 삽입 위치 : " + extraIndex);
                        Log.d("두 번째 정답 반영 성공", "extraAfterImage : " + beforeImage.getTag() + ", 삽입 위치 : " + extraIndex);
                    }
                }

                Log.d("정답 반영 종료", "끝났습니다.");

              //  twice = false;
            //}
            //else
            //{
            //    Log.d("2회 변경", "변경을 거부되었습니다.");
            //    twice = true;
            //}
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

                            //정답 중에 옮김으로써 순서 빈칸에 들어간 경우를 검사.
                            for(int x = 0; x < clickable_answers.length; x++) {
                                if ( (beforeImage != null) && (containView.getChildCount() == 1) && (beforeImage.getId() == clickable_answers[x].getId())) { // 1은 이미 자식이 있다는 것을 의미.
                                    //if ( (beforeImage != null) && (beforeImage.getTag() == clickable_answers[i].getTag()) ) {
                                    Log.d("빈칸에 이미 항목이 존재합니다.", "빈칸 안에 들어있던, 원래 정답으로 제시되는 객체와 옮기고 있는 객체 둘의 자리를 맞바꿉니다.");
                                    swapCount = false; // 이미 빈칸에 항목이 존재하므로 밑의 if문으로 교체할 필요가 없어져 false 설정
                                    checkCount = swapTag(); // 빈칸에 들어왔으면 빈칸 안에 어느 이미지뷰 클래스가 들어가 있는지 검사하여 교체할지 말지 결정. 동시에 정답 체크 기준 설정.
                                    break; // 한 번 검사 끝났으므로 반복문 탈출. 버튼 옮길 때 swapTag()는 한 번만.
                                }
                            }

                            //원래 순서 맞추기 시작하자마자 순서에 들어간 오브젝트인 경우를 검사.

                            if(swapCount) { // 만일 앞전에 swapCount가 false가 되었다면 교체가 일어났다는 것이므로 굳이 또 검사할 필요가 없어진다.
                                for (int y = 0; y < usingObject.size(); y++) {
                                //for (int y = 0; y < orderObject.length; y++) {
                                    //if (orderObject[y].getVisibility() == View.VISIBLE) // 사용 중인 오브젝트일 경우. 사용 안하는 오브젝트는 맞는지 검사할 필요가 없다.
                                   // {
                                        if ((beforeImage != null) && (containView.getChildCount() == 1) && (beforeImage.getId() == usingObject.get(y).getId())) { // 1은 이미 자식이 있다는 것을 의미.
                                       // if ((beforeImage != null) && (containView.getChildCount() == 1) && (beforeImage.getId() == orderObject[y].getId())) { // 1은 이미 자식이 있다는 것을 의미.
                                            //if ( (beforeImage != null) && (beforeImage.getTag() == clickable_answers[i].getTag()) ) {
                                            Log.d("빈칸에 이미 항목이 존재합니다.", "시작할 때부터 순서 안에 들어있던 객체와 옮기고 있는 객체 둘의 자리를 맞바꿉니다.");
                                            swapCount = false; // 이미 빈칸에 항목이 존재하므로 밑의 if문으로 교체할 필요가 없어져 false 설정
                                            checkCount = swapTag(); // 빈칸에 들어왔으면 빈칸 안에 어느 이미지뷰 클래스가 들어가 있는지 검사하여 교체할지 말지 결정. 동시에 정답 체크 기준 설정.
                                            break; // 한 번 검사 끝났으므로 반복문 탈출. 버튼 옮길 때 swapTag()는 한 번만.
                                        }
                                    //}
                                }
                            }

                            if(swapCount) // 위 for문으로 swapTag(i)가 발생하지 않았을 때(= 넣을 빈칸에 아무것도 없으면)
                            {
                                Log.d("비어있는 곳으로 들어갑니다.", "빈칸으로 이동");
                                // ** 주의 : 스레드 문제로 viewgroup.removeView(view)가 무시되고 contaionView.addView(View)가 실행될 수 있음.
                                viewgroup.removeView(view); // 옮기고 있는 정답을 포함하고 있던 LinearLayout에서 정답 삭제

                                containView.addView(view); // 빈칸에 정답 추가.
                                // view.setVisibility(View.VISIBLE); // 추가한 정답 보이게 변경
                            }

                            checkAnswer(checkCount); // 옮긴 이후 정답 확인

                            switcher = true;
                            break; // 이미 걸러냈으므로 for 문을 나간다.
                        }
                    }

                    if(!switcher) // 위 for 문에서 하나라도 안 바꿨다면. 즉, 바꾸지 않았을 때
                    {
                        Log.d("변화 없음", "제자리로 돌아갑니다.");
                        if (view.getVisibility() == View.INVISIBLE) // 만일 옮기고 있던 정답이 보여지지 않는 상태이면
                            view.setVisibility(View.VISIBLE); // 원상태로 돌려서 그 자리에 보이던 상태로 되돌림.
                    }

                    // DROP 이벤트 종료 직전에 boolean 변수를 초기화하여 다음 옮기는 이벤트에서 사고가 나지 않게 한다. result도 마찬가지.

                    Log.d("초기화", "변수들이 말썽부리지 않게 초기화 당시의 기본값으로 변경하였습니다.");
                    swapCount = true;
                    checkCount = false;
                    result = RESULT_NO_CHANGE;

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

