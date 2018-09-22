package com.example.m_user.geel_job.GamePlay.Storage;

import com.example.m_user.geel_job.GamePlay.Game_Status;
import com.example.m_user.geel_job.R;

/**
 * Created by They on 2017-11-09.
 */

public class GameStorage { // 모든 게임들 정보 저장용. 오로지 하나뿐.

    private static GameStorage instance; // singletone

    private GameStorage()
    {
        setStorage();
    }

    public static GameStorage getInstance()
    {
        if(instance == null) {
            instance = new GameStorage();
            //instance.setStorage();
        }

        return instance;
    }

    private static BlankStorage[][] game_blank_data = new BlankStorage[3][6]; // 빈칸 채우기 1~3장
    // 빈칸 채우기는 정답 정보가 배열 인덱스로 지정됨. 즉, 버튼의 배열 인덱스와 맞춰야 함.
    // 예를 들어 정답 배열에 있는 첫 번째 인덱스[0]의 값이 3이면, button[3] 즉, 4번째 버튼이 들어가야 정답이다.

    private static BoolStorage[][] game_bool_data = new BoolStorage[3][6]; // 조건문 고개 1~3장
    // 조건문 고개는 정답 정보가 boolean 배열로 되어 있음.
    // 조건문 고개는 세 개의 문제가 한 번에 나오므로 끝날 때 클리어 등이 출력되어서는 안됨. 바로 스테이지 출력창으로 이동.

    private static OrderStorage[][] game_order_data = new OrderStorage[3][6]; // 순서 맞추기 1~3장
    // 순서 맞추기는 빈칸에 들어가 있는 이미지뷰의 id를 비교하여 정답을 채점함.
    // 그러므로 id 간 비교를 해야 하므로 각각의 빈칸의 리니어 안의 자식들을 꺼내서 비교할 필요 있음. 가장 작업 많이 걸릴듯.

    private static FixStorage[][] game_fix_data = new FixStorage[3][6]; // 틀린 부분 찾기 1~3장
    // 틀린 부분 찾기는 조건문 고개처럼 boolean 이지만, 3개로 고정된 것이랑 달리 여러 개임.
    // 눌렸으면 true, 안 눌렸으면 false로 정답 한 거 바꿔줌으로써 비교하면 될 듯. 참고로 무슨 버튼 == true, == false 방식으로 비교해야 함.

    // 이하는 복사본을 주는 기능. 원본을 주면 원본 내용이 훼손되기 때문.

    public BlankStorage[] getBlankStorageOnChapter(int ch)
    {
        BlankStorage[] reference =  game_blank_data[ch].clone();

        return reference;
    }

    public BoolStorage[] getBoolStorageOnChapter(int ch)
    {
        BoolStorage[] reference =  game_bool_data[ch].clone();

        return reference;
    }

    public OrderStorage[] getOrderStorageOnChapter(int ch)
    {
        OrderStorage[] reference =  game_order_data[ch].clone();

        return reference;
    }

    public FixStorage[] getFixStorageOnChapter(int ch)
    {
        FixStorage[] reference =  game_fix_data[ch].clone();

        return reference;
    }

    // 이하는 게임하기 기본 데이터 생성. 게임 실행시 1번만 호출됨.

    private static void setStorage()
    {
        // 게임하기 : 빈칸 채우기
        // 종류는 크게 일반 채우기, 문장 채우기, 코드, 특수형(1-3, 3-5)
        // 검사 결과(전체) 1장 : 완료, 2장 : 완료, 3장 :


        // OK
        game_blank_data[0][0] = new BlankStorage().setTitle(Game_Status.BLANK_CH1_Q1).setTypeResource(Game_Status.GAME_BLANK).setLayoutId(R.layout.game_blank_base)
                .setBlankAt(3).setBlankAt(5).setObjectResource(4, new int[6], R.drawable.blank_object_ch1_q1_01).setSubtitle(Game_Status.GAME_BLANK_BASIC)
                .setAnswerInfo(2, new int[]{0, 5}).setAnswerImage(R.drawable.blank_answer_ch1_q1_01);

        // OK
        game_blank_data[0][1] = new BlankStorage().setTitle(Game_Status.BLANK_CH1_Q2).setTypeResource(Game_Status.GAME_BLANK).setLayoutId(R.layout.game_blank_base)
                .setBlankAt(0).setBlankAt(4).setBlankAt(5).setObjectResource(3, new int[6], R.drawable.blank_object_ch1_q2_01).setSubtitle(Game_Status.GAME_BLANK_BASIC)
                .setAnswerInfo(3, new int[]{2, 1, 4}).setAnswerImage(R.drawable.blank_answer_ch1_q2_01);
        // .setInvisibleAt(int index).setEventLinear(int[] linearIndex)

        // OK
        game_blank_data[0][2] = new BlankStorage().setTitle(Game_Status.BLANK_CH1_Q3).setTypeResource(Game_Status.GAME_BLANK).setLayoutId(R.layout.game_blank_base_1_3)
                .setBlankAt(0).setBlankAt(1).setObjectResource(1, new int[3], R.drawable.blank_object_ch1_q3_01).setSubtitle(Game_Status.GAME_BLANK_ETC)
                .setAnswerInfo(2, new int[]{3, 2}).setAnswerImage(R.drawable.blank_answer_ch1_q3_01);
        // .setInvisibleAt(int index).setEventLinear(int[] linearIndex)
        // 빈칸 채우기 3번은 큰 이미지 1개로 특수하기에 주의할 것. 일반적인 것들과 빈칸 위치도 다르니 주의.

        // OK
        game_blank_data[0][3] = new BlankStorage().setTitle(Game_Status.BLANK_CH1_Q4).setTypeResource(Game_Status.GAME_BLANK).setLayoutId(R.layout.game_blank_base_sentence_1_4)
                .setBlankAt(0).setBlankAt(1).setBlankAt(2).setObjectResource(1, new int[4], R.drawable.blank_object_ch1_q4_01).setSubtitle(Game_Status.GAME_BLANK_SENTENCE)
                .setAnswerInfo(3, new int[]{5, 1, 4}).setAnswerImage(R.drawable.blank_answer_ch1_q4_01);
        // .setInvisibleAt(int index).setEventLinear(int[] linearIndex)

        // OK
        game_blank_data[0][4] = new BlankStorage().setTitle(Game_Status.BLANK_CH1_Q5).setTypeResource(Game_Status.GAME_BLANK).setLayoutId(R.layout.game_blank_base_sentence_1_5)
                .setBlankAt(0).setBlankAt(1).setObjectResource(1, new int[3], R.drawable.blank_object_ch1_q5_01).setSubtitle(Game_Status.GAME_BLANK_SENTENCE)
                .setAnswerInfo(2, new int[]{0, 4}).setAnswerImage(R.drawable.blank_answer_ch1_q5_01);
        // .setInvisibleAt(int index).setEventLinear(int[] linearIndex)

        // OK
        game_blank_data[0][5] = new BlankStorage().setTitle(Game_Status.BLANK_CH1_Q6).setTypeResource(Game_Status.GAME_BLANK).setLayoutId(R.layout.game_blank_base)
                .setBlankAt(1).setBlankAt(2).setBlankAt(3).setObjectResource(3, new int[6], R.drawable.blank_object_ch1_q6_01).setSubtitle(Game_Status.GAME_BLANK_BASIC)
                .setAnswerInfo(3, new int[]{5, 2, 3}).setAnswerImage(R.drawable.blank_answer_ch1_q6_01);
        // .setInvisibleAt(int index).setEventLinear(int[] linearIndex)


        // OK
        game_blank_data[1][0] = new BlankStorage().setTitle(Game_Status.BLANK_CH2_Q1).setTypeResource(Game_Status.GAME_BLANK).setLayoutId(R.layout.game_blank_base_sentence_2_1)
                .setBlankAt(0).setBlankAt(1).setObjectResource(1, new int[3], R.drawable.blank_object_ch2_q1_01).setSubtitle(Game_Status.GAME_BLANK_SENTENCE)
                .setAnswerInfo(2, new int[]{3, 1}).setAnswerImage(R.drawable.blank_answer_ch2_q1_01);
        // .setInvisibleAt(int index).setEventLinear(int[] linearIndex)

        // OK
        game_blank_data[1][1] = new BlankStorage().setTitle(Game_Status.BLANK_CH2_Q2).setTypeResource(Game_Status.GAME_BLANK).setLayoutId(R.layout.game_blank_base)
                .setBlankAt(3).setBlankAt(4).setBlankAt(5).setObjectResource(3, new int[6], R.drawable.blank_object_ch2_q2_01).setSubtitle(Game_Status.GAME_BLANK_BASIC)
                .setAnswerInfo(3, new int[]{5, 2, 4}).setAnswerImage(R.drawable.blank_answer_ch2_q2_01);
        // .setInvisibleAt(int index).setEventLinear(int[] linearIndex)

        // OK
        game_blank_data[1][2] = new BlankStorage().setTitle(Game_Status.BLANK_CH2_Q3).setTypeResource(Game_Status.GAME_BLANK).setLayoutId(R.layout.game_blank_base_code_2_3)
                .setBlankAt(0).setBlankAt(1).setObjectResource(1, new int[3], R.drawable.blank_object_ch2_q3_01).setSubtitle(Game_Status.GAME_BLANK_CODE)
                .setAnswerInfo(2, new int[]{0, 2}).setAnswerImage(R.drawable.blank_answer_ch2_q3_01);
        // .setInvisibleAt(int index).setEventLinear(int[] linearIndex)

        // OK
        game_blank_data[1][3] = new BlankStorage().setTitle(Game_Status.BLANK_CH2_Q4).setTypeResource(Game_Status.GAME_BLANK).setLayoutId(R.layout.game_blank_base_sentence_2_4)
                .setBlankAt(0).setBlankAt(1).setBlankAt(2).setObjectResource(1, new int[4], R.drawable.blank_object_ch2_q4_01).setSubtitle(Game_Status.GAME_BLANK_SENTENCE)
                .setAnswerInfo(3, new int[]{0, 1, 2}).setAnswerImage(R.drawable.blank_answer_ch2_q4_01);

        // OK
        game_blank_data[1][4] = new BlankStorage().setTitle(Game_Status.BLANK_CH2_Q5).setTypeResource(Game_Status.GAME_BLANK).setLayoutId(R.layout.game_blank_base_4)
                .setBlankAt(1).setBlankAt(2).setObjectResource(2, new int[4], R.drawable.blank_object_ch2_q5_01).setSubtitle(Game_Status.GAME_BLANK_BASIC)
                .setAnswerInfo(2, new int[]{1, 4}).setAnswerImage(R.drawable.blank_answer_ch2_q5_01)
                .setInvisibleAt(2).setInvisibleAt(5);
        // 2-5 빈칸 이미지를 흰색으로 가져오든, 비워서 가져오든, 가리든 알아서 하기.
        // 4개 전용 레이아웃을 만들어 id가 새지 않도록 함.

        // OK
        game_blank_data[1][5] = new BlankStorage().setTitle(Game_Status.BLANK_CH2_Q6).setTypeResource(Game_Status.GAME_BLANK).setLayoutId(R.layout.game_blank_base_sentence_2_6)
                .setBlankAt(0).setBlankAt(1).setObjectResource(1, new int[3], R.drawable.blank_object_ch2_q4_01).setSubtitle(Game_Status.GAME_BLANK_SENTENCE)
                .setAnswerInfo(2, new int[]{5, 2}).setAnswerImage(R.drawable.blank_answer_ch2_q6_01);



        game_blank_data[2][0] = new BlankStorage().setTitle(Game_Status.BLANK_CH3_Q1).setTypeResource(Game_Status.GAME_BLANK).setLayoutId(R.layout.game_blank_base_4)
                .setBlankAt(2).setBlankAt(3).setObjectResource(2, new int[4], R.drawable.blank_object_ch3_q1_01).setSubtitle(Game_Status.GAME_BLANK_BASIC)
                .setAnswerInfo(2, new int[]{0, 2}).setAnswerImage(R.drawable.blank_answer_ch3_q1_01)
                .setInvisibleAt(2).setInvisibleAt(5);

        // 3-1 빈칸 이미지를 흰색으로 가져오든, 비워서 가져오든, 가리든 알아서 하기.
        // 4개 전용 레이아웃을 만들어 id가 새지 않도록 함.

        game_blank_data[2][1] = new BlankStorage().setTitle(Game_Status.BLANK_CH3_Q2).setTypeResource(Game_Status.GAME_BLANK).setLayoutId(R.layout.game_blank_base_sentence_3_2)
                .setBlankAt(0).setBlankAt(1).setObjectResource(1, new int[3], R.drawable.blank_object_ch3_q2_01).setSubtitle(Game_Status.GAME_BLANK_SENTENCE)
                .setAnswerInfo(2, new int[]{0, 5}).setAnswerImage(R.drawable.blank_answer_ch3_q2_01);

        game_blank_data[2][2] = new BlankStorage().setTitle(Game_Status.BLANK_CH3_Q3).setTypeResource(Game_Status.GAME_BLANK).setLayoutId(R.layout.game_blank_base_code_3_3)
                .setBlankAt(0).setBlankAt(1).setBlankAt(2).setObjectResource(1, new int[4], R.drawable.blank_object_ch3_q3_01).setSubtitle(Game_Status.GAME_BLANK_CODE)
                .setAnswerInfo(3, new int[]{5, 4, 1}).setAnswerImage(R.drawable.blank_answer_ch3_q3_01);

        game_blank_data[2][3] = new BlankStorage().setTitle(Game_Status.BLANK_CH3_Q4).setTypeResource(Game_Status.GAME_BLANK).setLayoutId(R.layout.game_blank_base)
                .setBlankAt(0).setBlankAt(1).setBlankAt(5).setObjectResource(3, new int[6], R.drawable.blank_object_ch3_q4_01).setSubtitle(Game_Status.GAME_BLANK_BASIC)
                .setAnswerInfo(3, new int[]{4, 1, 2}).setAnswerImage(R.drawable.blank_answer_ch3_q4_01);

        game_blank_data[2][4] = new BlankStorage().setTitle(Game_Status.BLANK_CH3_Q5).setTypeResource(Game_Status.GAME_BLANK).setLayoutId(R.layout.game_blank_base_10)
                .setBlankAt(0).setBlankAt(1).setBlankAt(4).setBlankAt(7).setBlankAt(8).setObjectResource(5, new int[10], R.drawable.blank_object_ch3_q5_01).setSubtitle(Game_Status.GAME_BLANK_ETC)
                .setAnswerInfo(5, new int[]{5, 0, 1, 2, 4}).setAnswerImage(R.drawable.blank_answer_ch3_q5_01);
        // 3-5는 5행이므로 주의. 오브젝트 개수는 5개이므로 다른 것들과는 달리 3이 아닌 5까지 계산.
        // 현재 여기서 정답 채점에 문제가 생김.
        // 전용 xml 추가.

        game_blank_data[2][5] = new BlankStorage().setTitle(Game_Status.BLANK_CH3_Q6).setTypeResource(Game_Status.GAME_BLANK).setLayoutId(R.layout.game_blank_base_sentence_3_6)
                .setBlankAt(0).setBlankAt(1).setObjectResource(1, new int[3], R.drawable.blank_object_ch3_q6_01).setSubtitle(Game_Status.GAME_BLANK_SENTENCE)
                .setAnswerInfo(2, new int[]{0, 3}).setAnswerImage(R.drawable.blank_answer_ch3_q6_01);

        // 게임하기 : 조건문 고개

        // OK
        game_bool_data[0][0] = new BoolStorage().setTitle(Game_Status.BOOL_CH1_Q1_1).setTypeResource(Game_Status.GAME_BOOL)
                .setQuizTitle(R.drawable.bool_title_ch1_q1).setQuizImage(R.drawable.bool_object_ch1_q1_01).setAnswers(new boolean[]{true, false, false});

        // OK
        game_bool_data[0][1] = new BoolStorage().setTitle(Game_Status.BOOL_CH1_Q2_1).setTypeResource(Game_Status.GAME_BOOL)
                .setQuizTitle(R.drawable.bool_title_ch1_q2).setQuizImage(R.drawable.bool_object_ch1_q2_01).setAnswers(new boolean[]{true, true, true});

        // OK
        game_bool_data[0][2] = new BoolStorage().setTitle(Game_Status.BOOL_CH1_Q3_1).setTypeResource(Game_Status.GAME_BOOL)
                .setQuizTitle(R.drawable.bool_title_ch1_q3).setQuizImage(R.drawable.bool_object_ch1_q3_01).setAnswers(new boolean[]{false, true, false});

        // OK
        game_bool_data[0][3] = new BoolStorage().setTitle(Game_Status.BOOL_CH1_Q4_1).setTypeResource(Game_Status.GAME_BOOL)
                .setQuizTitle(R.drawable.bool_title_ch1_q4).setQuizImage(R.drawable.bool_object_ch1_q4_01).setAnswers(new boolean[]{true, true, false});

        // OK
        game_bool_data[0][4] = new BoolStorage().setTitle(Game_Status.BOOL_CH1_Q5_1).setTypeResource(Game_Status.GAME_BOOL)
                .setQuizTitle(R.drawable.bool_title_ch1_q5).setQuizImage(R.drawable.bool_object_ch1_q5_01).setAnswers(new boolean[]{true, true, true});

        // OK
        game_bool_data[0][5] = new BoolStorage().setTitle(Game_Status.BOOL_CH1_Q6_1).setTypeResource(Game_Status.GAME_BOOL)
                .setQuizTitle(R.drawable.bool_title_ch1_q6).setQuizImage(R.drawable.bool_object_ch1_q6_01).setAnswers(new boolean[]{true, false, true});



        //
        game_bool_data[1][0] = new BoolStorage().setTitle(Game_Status.BOOL_CH2_Q1_1).setTypeResource(Game_Status.GAME_BOOL)
                .setQuizTitle(R.drawable.bool_title_ch2_q1).setQuizImage(R.drawable.bool_object_ch2_q1_01).setAnswers(new boolean[]{false, true, true});

        game_bool_data[1][1] = new BoolStorage().setTitle(Game_Status.BOOL_CH2_Q2_1).setTypeResource(Game_Status.GAME_BOOL)
                .setQuizTitle(R.drawable.bool_title_ch2_q2).setQuizImage(R.drawable.bool_object_ch2_q2_01).setAnswers(new boolean[]{false, true, false});

        // OK
        game_bool_data[1][2] = new BoolStorage().setTitle(Game_Status.BOOL_CH2_Q3_1).setTypeResource(Game_Status.GAME_BOOL)
                .setQuizTitle(R.drawable.bool_title_ch2_q3).setQuizImage(R.drawable.bool_object_ch2_q3_01).setAnswers(new boolean[]{true, false, true});

        // OK
        game_bool_data[1][3] = new BoolStorage().setTitle(Game_Status.BOOL_CH2_Q4_1).setTypeResource(Game_Status.GAME_BOOL)
                .setQuizTitle(R.drawable.bool_title_ch2_q4).setQuizImage(R.drawable.bool_object_ch2_q4_01).setAnswers(new boolean[]{true, true, false});

        // OK
        game_bool_data[1][4] = new BoolStorage().setTitle(Game_Status.BOOL_CH2_Q5_1).setTypeResource(Game_Status.GAME_BOOL)
                .setQuizTitle(R.drawable.bool_title_ch2_q5).setQuizImage(R.drawable.bool_object_ch2_q5_01).setAnswers(new boolean[]{false, true, false});

        // OK
        game_bool_data[1][5] = new BoolStorage().setTitle(Game_Status.BOOL_CH2_Q6_1).setTypeResource(Game_Status.GAME_BOOL)
                .setQuizTitle(R.drawable.bool_title_ch2_q6).setQuizImage(R.drawable.bool_object_ch2_q6_01).setAnswers(new boolean[]{false, false, false});

        game_bool_data[2][0] = new BoolStorage().setTitle(Game_Status.BOOL_CH3_Q1_1).setTypeResource(Game_Status.GAME_BOOL)
                .setQuizTitle(R.drawable.bool_title_ch3_q1).setQuizImage(R.drawable.bool_object_ch3_q1_01).setAnswers(new boolean[]{true, true, false});

        game_bool_data[2][1] = new BoolStorage().setTitle(Game_Status.BOOL_CH3_Q2_1).setTypeResource(Game_Status.GAME_BOOL)
                .setQuizTitle(R.drawable.bool_title_ch3_2).setQuizImage(R.drawable.bool_object_ch3_q2_01).setAnswers(new boolean[]{false, true, true});

        game_bool_data[2][2] = new BoolStorage().setTitle(Game_Status.BOOL_CH3_Q3_1).setTypeResource(Game_Status.GAME_BOOL)
                .setQuizTitle(R.drawable.bool_title_ch3_q3).setQuizImage(R.drawable.bool_object_ch3_q3_01).setAnswers(new boolean[]{false, true, false});

        game_bool_data[2][3] = new BoolStorage().setTitle(Game_Status.BOOL_CH3_Q4_1).setTypeResource(Game_Status.GAME_BOOL)
                .setQuizTitle(R.drawable.bool_title_ch3_q4).setQuizImage(R.drawable.bool_object_ch3_q4_01).setAnswers(new boolean[]{true, true, false});

        game_bool_data[2][4] = new BoolStorage().setTitle(Game_Status.BOOL_CH3_Q5_1).setTypeResource(Game_Status.GAME_BOOL)
                .setQuizTitle(R.drawable.bool_title_ch3_q5).setQuizImage(R.drawable.bool_object_ch3_q5_01).setAnswers(new boolean[]{true, true, true});

        game_bool_data[2][5] = new BoolStorage().setTitle(Game_Status.BOOL_CH3_Q6_1).setTypeResource(Game_Status.GAME_BOOL)
                .setQuizTitle(R.drawable.bool_title_ch3_q6).setQuizImage(R.drawable.bool_object_ch3_q6_01).setAnswers(new boolean[]{false, false, false});

        // 순서 맞추기
        // 만일 이미지 맞추기가 안되면 이미지뷰의 id를 설정할 것.

        // OK
        game_order_data[0][0] = new OrderStorage().setTitle(Game_Status.ORDER_CH1_Q1).setTypeResource(Game_Status.GAME_ORDER).setOrderTitle(R.drawable.order_title_ch1_q1).setLayoutId(R.layout.game_order_base_10)
                .setOrderAmount(9).setOrderPieceAt(0, R.drawable.order_object_ch1_q1_01).setOrderPieceAt(2, R.drawable.order_object_ch1_q1_02)
                .setOrderPieceAt(3, R.drawable.order_object_ch1_q1_03).setOrderPieceAt(5, R.drawable.order_object_ch1_q1_04).setOrderPieceAt(7, R.drawable.order_object_ch1_q1_05)
                .setAnswerInfo(9, new int[]{R.id.order_object_01, R.id.order_answer_03, R.id.order_object_03, R.id.order_object_04
                        , R.id.order_answer_01, R.id.order_object_06, R.id.order_answer_04, R.id.order_object_08, R.id.order_answer_02})
                .setAnswerImage(R.drawable.order_answer_ch1_q1_01).setBlank()
                .setStartingAnswer();

        // OK
        game_order_data[0][1] = new OrderStorage().setTitle(Game_Status.ORDER_CH1_Q2).setTypeResource(Game_Status.GAME_ORDER).setOrderTitle(R.drawable.order_title_ch1_q2).setLayoutId(R.layout.game_order_base_8)
                .setOrderAmount(7).setOrderPieceAt(0, R.drawable.order_object_ch1_q2_01).setOrderPieceAt(3, R.drawable.order_object_ch1_q2_02)
                .setOrderPieceAt(4, R.drawable.order_object_ch1_q2_03)
                .setAnswerInfo(7, new int[]{R.id.order_answer_02, R.id.order_object_01, R.id.order_answer_04, R.id.order_object_04
                        , R.id.order_answer_03, R.id.order_object_05, R.id.order_answer_01})
                .setAnswerImage(R.drawable.order_answer_ch1_q2_01).setBlank().setStartingAnswer();

        // OK
        game_order_data[0][2] = new OrderStorage().setTitle(Game_Status.ORDER_CH1_Q3).setTypeResource(Game_Status.GAME_ORDER).setOrderTitle(R.drawable.order_title_ch1_q3).setLayoutId(R.layout.game_order_base_8)
                .setOrderAmount(8).setOrderPieceAt(0, R.drawable.order_object_ch1_q3_01).setOrderPieceAt(1, R.drawable.order_object_ch1_q3_02)
                .setOrderPieceAt(4, R.drawable.order_object_ch1_q3_03).setOrderPieceAt(7, R.drawable.order_object_ch1_q3_04)
                .setAnswerInfo(8, new int[]{R.id.order_object_08, R.id.order_answer_02, R.id.order_answer_03, R.id.order_answer_04
                        , R.id.order_answer_01, R.id.order_object_02, R.id.order_object_01, R.id.order_object_05})
                .setAnswerImage(R.drawable.order_answer_ch1_q3_01).setBlank().setStartingAnswer();

        // OK
        game_order_data[0][3] = new OrderStorage().setTitle(Game_Status.ORDER_CH1_Q4).setTypeResource(Game_Status.GAME_ORDER).setOrderTitle(R.drawable.order_title_ch1_q4).setLayoutId(R.layout.game_order_base_10)
                .setOrderAmount(10).setOrderPieceAt(0, R.drawable.order_object_ch1_q4_01).setOrderPieceAt(1, R.drawable.order_object_ch1_q4_02)
                .setOrderPieceAt(4, R.drawable.order_object_ch1_q4_03).setOrderPieceAt(6, R.drawable.order_object_ch1_q4_04)
                .setOrderPieceAt(8, R.drawable.order_object_ch1_q4_05).setOrderPieceAt(9, R.drawable.order_object_ch1_q4_06)
                .setAnswerInfo(10, new int[]{R.id.order_object_01, R.id.order_object_02, R.id.order_answer_04, R.id.order_answer_01, R.id.order_object_05
                        , R.id.order_answer_02, R.id.order_object_07, R.id.order_answer_03, R.id.order_object_09, R.id.order_object_10})
                .setAnswerImage(R.drawable.order_answer_ch1_q4_01).setBlank().setStartingAnswer();

        // OK
        game_order_data[0][4] = new OrderStorage().setTitle(Game_Status.ORDER_CH1_Q5).setTypeResource(Game_Status.GAME_ORDER).setOrderTitle(R.drawable.order_title_ch1_q5).setLayoutId(R.layout.game_order_base_5)
                .setOrderAmount(5).setOrderPieceAt(3, R.drawable.order_object_ch1_q5_01)
                .setAnswerInfo(5, new int[]{R.id.order_answer_01, R.id.order_answer_04, R.id.order_answer_03, R.id.order_answer_02, R.id.order_object_04
                })
                .setAnswerImage(R.drawable.order_answer_ch1_q5_01).setBlank().setStartingAnswer();

        // OK
        game_order_data[0][5] = new OrderStorage().setTitle(Game_Status.ORDER_CH1_Q6).setTypeResource(Game_Status.GAME_ORDER).setOrderTitle(R.drawable.order_title_ch1_q6).setLayoutId(R.layout.game_order_base_8)
                .setOrderAmount(8).setOrderPieceAt(0, R.drawable.order_object_ch1_q6_01).setOrderPieceAt(2, R.drawable.order_object_ch1_q6_02)
                .setOrderPieceAt(5, R.drawable.order_object_ch1_q6_03).setOrderPieceAt(7, R.drawable.order_object_ch1_q6_04)
                .setAnswerInfo(8, new int[]{R.id.order_object_01, R.id.order_answer_01, R.id.order_object_03, R.id.order_answer_03
                        , R.id.order_answer_02, R.id.order_object_06, R.id.order_answer_04, R.id.order_object_08})
                .setAnswerImage(R.drawable.order_answer_ch1_q6_01).setBlank().setStartingAnswer();

        // OK
        game_order_data[1][0] = new OrderStorage().setTitle(Game_Status.ORDER_CH2_Q1).setTypeResource(Game_Status.GAME_ORDER).setOrderTitle(R.drawable.order_title_ch2_q1).setLayoutId(R.layout.game_order_base_6)
                .setOrderAmount(6).setOrderPieceAt(0, R.drawable.order_object_ch2_q1_01).setOrderPieceAt(1, R.drawable.order_object_ch2_q1_02)
                .setAnswerInfo(6, new int[]{R.id.order_object_01, R.id.order_object_02, R.id.order_answer_03
                        , R.id.order_answer_04, R.id.order_answer_01, R.id.order_answer_02})
                .setAnswerImage(R.drawable.order_answer_ch2_q1_01).setBlank().setStartingAnswer();

        //
        game_order_data[1][1] = new OrderStorage().setTitle(Game_Status.ORDER_CH2_Q2).setTypeResource(Game_Status.GAME_ORDER).setOrderTitle(R.drawable.order_title_ch2_q2).setLayoutId(R.layout.game_order_base_6)
                .setOrderAmount(6).setOrderPieceAt(1, R.drawable.order_object_ch2_q2_01).setOrderPieceAt(5, R.drawable.order_object_ch2_q2_02)
                .setAnswerInfo(6, new int[]{R.id.order_answer_01, R.id.order_object_02, R.id.order_answer_04
                        , R.id.order_answer_02, R.id.order_object_06, R.id.order_answer_03})
                .setAnswerImage(R.drawable.order_answer_ch2_q2_01).setBlank().setStartingAnswer();

        // OK
        game_order_data[1][2] = new OrderStorage().setTitle(Game_Status.ORDER_CH2_Q3).setTypeResource(Game_Status.GAME_ORDER).setOrderTitle(R.drawable.order_title_ch2_q3).setLayoutId(R.layout.game_order_base_5)
                .setOrderAmount(3)
                .setAnswerInfo(3, new int[]{R.id.order_answer_01, R.id.order_answer_02, R.id.order_answer_03})
                .setAnswerImage(R.drawable.order_answer_ch2_q3_01).setBlank().setStartingAnswer();

        // OK
        game_order_data[1][3] = new OrderStorage().setTitle(Game_Status.ORDER_CH2_Q4).setTypeResource(Game_Status.GAME_ORDER).setOrderTitle(R.drawable.order_title_ch2_q4).setLayoutId(R.layout.game_order_base_5)
                .setOrderAmount(5).setOrderPieceAt(1, R.drawable.order_object_ch2_q4_01).setOrderPieceAt(4, R.drawable.order_object_ch2_q4_02)
                .setAnswerInfo(5, new int[]{R.id.order_object_02, R.id.order_answer_04, R.id.order_answer_02
                        , R.id.order_object_05, R.id.order_answer_01})
                .setAnswerImage(R.drawable.order_answer_ch2_q4_01).setBlank().setStartingAnswer();

        //
        game_order_data[1][4] = new OrderStorage().setTitle(Game_Status.ORDER_CH2_Q5).setTypeResource(Game_Status.GAME_ORDER).setOrderTitle(R.drawable.order_title_ch2_q5).setLayoutId(R.layout.game_order_base_5)
                .setOrderAmount(5).setOrderPieceAt(0, R.drawable.order_object_ch2_q5_01)
                .setAnswerInfo(5, new int[]{R.id.order_answer_04, R.id.order_answer_02, R.id.order_object_01, R.id.order_answer_01, R.id.order_answer_03})
                .setAnswerImage(R.drawable.order_answer_ch2_q5_01).setBlank().setStartingAnswer();

        // OK
        game_order_data[1][5] = new OrderStorage().setTitle(Game_Status.ORDER_CH2_Q4).setTypeResource(Game_Status.GAME_ORDER).setOrderTitle(R.drawable.order_title_ch2_q6).setLayoutId(R.layout.game_order_base_5)
                .setOrderAmount(4)
                .setAnswerInfo(4, new int[]{R.id.order_answer_03, R.id.order_answer_02, R.id.order_answer_01, R.id.order_answer_04})
                .setAnswerImage(R.drawable.order_answer_ch2_q6_01).setBlank().setStartingAnswer();

        // OK
        game_order_data[2][0] = new OrderStorage().setTitle(Game_Status.ORDER_CH3_Q1).setTypeResource(Game_Status.GAME_ORDER).setOrderTitle(R.drawable.order_title_ch3_q1).setLayoutId(R.layout.game_order_base_10)
                .setOrderAmount(9).setOrderPieceAt(0, R.drawable.order_object_ch3_q1_01).setOrderPieceAt(1, R.drawable.order_object_ch3_q1_02).setOrderPieceAt(2, R.drawable.order_object_ch3_q1_03)
                .setOrderPieceAt(4, R.drawable.order_object_ch3_q1_04).setOrderPieceAt(6, R.drawable.order_object_ch3_q1_05).setOrderPieceAt(8, R.drawable.order_object_ch3_q1_06)
                .setAnswerInfo(9, new int[]{R.id.order_object_01, R.id.order_object_03, R.id.order_object_02, R.id.order_answer_01, R.id.order_answer_02
                        , R.id.order_object_09, R.id.order_answer_04, R.id.order_object_07, R.id.order_object_05})
                .setAnswerImage(R.drawable.order_answer_ch3_q1_01).setBlank().setStartingAnswer();

        //
        game_order_data[2][1] = new OrderStorage().setTitle(Game_Status.ORDER_CH3_Q2).setTypeResource(Game_Status.GAME_ORDER).setOrderTitle(R.drawable.order_title_ch3_q2).setLayoutId(R.layout.game_order_base_8)
                .setOrderAmount(8).setOrderPieceAt(0, R.drawable.order_object_ch3_q2_01).setOrderPieceAt(3, R.drawable.order_object_ch3_q2_02)
                .setOrderPieceAt(4, R.drawable.order_object_ch3_q2_03).setOrderPieceAt(7, R.drawable.order_object_ch3_q2_04)
                .setAnswerInfo(8, new int[]{R.id.order_object_01, R.id.order_answer_04, R.id.order_object_04, R.id.order_answer_03
                        , R.id.order_object_05, R.id.order_answer_02, R.id.order_object_08, R.id.order_answer_01})
                .setAnswerImage(R.drawable.order_answer_ch3_q2_01).setBlank().setStartingAnswer();

        //
        game_order_data[2][2] = new OrderStorage().setTitle(Game_Status.ORDER_CH3_Q3).setTypeResource(Game_Status.GAME_ORDER).setOrderTitle(R.drawable.order_title_ch3_q3).setLayoutId(R.layout.game_order_base_8)
                .setOrderAmount(8).setOrderPieceAt(0, R.drawable.order_object_ch3_q3_01).setOrderPieceAt(1, R.drawable.order_object_ch3_q3_02)
                .setOrderPieceAt(2, R.drawable.order_object_ch3_q3_03).setOrderPieceAt(3, R.drawable.order_object_ch3_q3_04).setOrderPieceAt(7, R.drawable.order_object_ch3_q3_05)
                .setAnswerInfo(8, new int[]{R.id.order_answer_03, R.id.order_object_01, R.id.order_object_02, R.id.order_object_04
                        , R.id.order_answer_01, R.id.order_answer_04, R.id.order_object_03, R.id.order_object_08})
                .setAnswerImage(R.drawable.order_answer_ch3_q3_01).setBlank().setStartingAnswer();

        // OK
        game_order_data[2][3] = new OrderStorage().setTitle(Game_Status.ORDER_CH3_Q4).setTypeResource(Game_Status.GAME_ORDER).setOrderTitle(R.drawable.order_title_ch3_q4).setLayoutId(R.layout.game_order_base_8)
                .setOrderAmount(8).setOrderPieceAt(0, R.drawable.order_object_ch3_q4_01).setOrderPieceAt(1, R.drawable.order_object_ch3_q4_02)
                .setOrderPieceAt(2, R.drawable.order_object_ch3_q4_03).setOrderPieceAt(6, R.drawable.order_object_ch3_q4_04).setOrderPieceAt(7, R.drawable.order_object_ch3_q4_05)
                .setAnswerInfo(8, new int[]{R.id.order_object_03, R.id.order_object_02, R.id.order_object_01, R.id.order_answer_01
                        , R.id.order_answer_04, R.id.order_answer_02, R.id.order_object_08, R.id.order_object_07})
                .setAnswerImage(R.drawable.order_answer_ch3_q4_01).setBlank().setStartingAnswer();

        game_order_data[2][4] = new OrderStorage().setTitle(Game_Status.ORDER_CH3_Q5).setTypeResource(Game_Status.GAME_ORDER).setOrderTitle(R.drawable.order_title_ch3_q5).setLayoutId(R.layout.game_order_base_10)
                .setOrderAmount(9).setOrderPieceAt(0, R.drawable.order_object_ch3_q5_01).setOrderPieceAt(1, R.drawable.order_object_ch3_q5_02)
                .setOrderPieceAt(2, R.drawable.order_object_ch3_q5_03).setOrderPieceAt(7, R.drawable.order_object_ch3_q5_04).setOrderPieceAt(8, R.drawable.order_object_ch3_q5_05)
                .setAnswerInfo(9, new int[]{R.id.order_object_01, R.id.order_object_09, R.id.order_object_02, R.id.order_answer_04
                        , R.id.order_answer_03, R.id.order_answer_02, R.id.order_answer_01, R.id.order_object_03, R.id.order_object_08})
                .setAnswerImage(R.drawable.order_answer_ch3_q5_01).setBlank().setStartingAnswer();

        game_order_data[2][5] = new OrderStorage().setTitle(Game_Status.ORDER_CH3_Q6).setTypeResource(Game_Status.GAME_ORDER).setOrderTitle(R.drawable.order_title_ch3_q6).setLayoutId(R.layout.game_order_base_6)
                .setOrderAmount(6).setOrderPieceAt(0, R.drawable.order_object_ch3_q6_01).setOrderPieceAt(1, R.drawable.order_object_ch3_q6_02).setOrderPieceAt(5, R.drawable.order_object_ch3_q6_03)
                .setAnswerInfo(6, new int[]{R.id.order_object_01, R.id.order_answer_02, R.id.order_answer_03
                        , R.id.order_object_02, R.id.order_answer_04, R.id.order_object_06})
                .setAnswerImage(R.drawable.order_answer_ch3_q6_01).setBlank().setStartingAnswer();

        // 틀린 부분 찾기
        // 만일 전체 이미지 합본 쓸거면 선택할 부분의 이미지 리소스 대신 id를 써서 리스너 부여하는 용도로.

        // OK
        game_fix_data[0][0] = new FixStorage().setTitle(Game_Status.FIX_CH1_Q1).setTypeResource(Game_Status.GAME_FIX).setFixTitle(R.drawable.fix_title_sentence).setSubtitle(Game_Status.GAME_FIX_SENTENCE)
                .setLayoutId(R.layout.game_fix_base_sentence_1_1).setFixBackground(R.drawable.fix_bg_ch1_q1).setClickableObject(6)//.setClickableImages(R.drawable.fix_object_ch1_q1_01)
                .setAnswers(new boolean[]{false, true, true, false, true, false});

        // OK
        game_fix_data[0][1] = new FixStorage().setTitle(Game_Status.FIX_CH1_Q2).setTypeResource(Game_Status.GAME_FIX).setFixTitle(R.drawable.fix_title_sentence).setSubtitle(Game_Status.GAME_FIX_SENTENCE)
                .setLayoutId(R.layout.game_fix_base_sentence_1_2).setFixBackground(R.drawable.fix_bg_ch1_q2).setClickableObject(6)//.setClickableImages(R.drawable.fix_object_ch1_q2_01)
                .setAnswers(new boolean[]{true, false, true, true, false, false});

        // OK
        game_fix_data[0][2] = new FixStorage().setTitle(Game_Status.FIX_CH1_Q3).setTypeResource(Game_Status.GAME_FIX).setFixTitle(R.drawable.fix_title_sentence).setSubtitle(Game_Status.GAME_FIX_SENTENCE)
                .setLayoutId(R.layout.game_fix_base_sentence_1_3).setFixBackground(R.drawable.fix_bg_ch1_q3).setClickableObject(6)//.setClickableImages(R.drawable.fix_object_ch1_q3_01)
                .setAnswers(new boolean[]{false, false, true, true, false, true});

        // OK
        game_fix_data[0][3] = new FixStorage().setTitle(Game_Status.FIX_CH1_Q4).setTypeResource(Game_Status.GAME_FIX).setFixTitle(R.drawable.fix_title_algorithm).setSubtitle(Game_Status.GAME_FIX_ALGORITHM)
                .setLayoutId(R.layout.game_fix_base_algorithm_1_4).setFixBackground(R.drawable.fix_bg_ch1_q4).setClickableObject(9)//.setClickableImages(R.drawable.fix_object_ch1_q4_01)
                .setAnswers(new boolean[]{true, false, false, false, true, false, false, false, true});

        // OK
        game_fix_data[0][4] = new FixStorage().setTitle(Game_Status.FIX_CH1_Q5).setTypeResource(Game_Status.GAME_FIX).setFixTitle(R.drawable.fix_title_sentence).setSubtitle(Game_Status.GAME_FIX_SENTENCE)
                .setLayoutId(R.layout.game_fix_base_sentence_1_5).setFixBackground(R.drawable.fix_bg_ch1_q5).setClickableObject(5)//.setClickableImages(R.drawable.fix_object_ch1_q5_01)
                .setAnswers(new boolean[]{false, false, true, true, true});

        // OK
        game_fix_data[0][5] = new FixStorage().setTitle(Game_Status.FIX_CH1_Q6).setTypeResource(Game_Status.GAME_FIX).setFixTitle(R.drawable.fix_title_sentence).setSubtitle(Game_Status.GAME_FIX_ORDER)
                .setLayoutId(R.layout.game_fix_base_order_1_6).setFixBackground(R.drawable.fix_bg_ch1_q6).setClickableObject(8)//.setClickableImages(R.drawable.fix_object_ch1_q6_01)
                .setAnswers(new boolean[]{false, false, false, true, false, true, false, true});



        // OK
        game_fix_data[1][0] = new FixStorage().setTitle(Game_Status.FIX_CH2_Q1).setTypeResource(Game_Status.GAME_FIX).setFixTitle(R.drawable.fix_title_sentence).setSubtitle(Game_Status.GAME_FIX_SENTENCE)
                .setLayoutId(R.layout.game_fix_base_sentence_2_1).setFixBackground(R.drawable.fix_bg_ch2_q1).setClickableObject(8)//.setClickableImages(R.drawable.fix_object_ch2_q1_01)
                .setAnswers(new boolean[]{true, false, true, false, true, false, true, false});

        // OK
        game_fix_data[1][1] = new FixStorage().setTitle(Game_Status.FIX_CH2_Q2).setTypeResource(Game_Status.GAME_FIX).setFixTitle(R.drawable.fix_title_sentence).setSubtitle(Game_Status.GAME_FIX_SENTENCE)
                .setLayoutId(R.layout.game_fix_base_sentence_2_2).setFixBackground(R.drawable.fix_bg_ch2_q2).setClickableObject(5)//.setClickableImages(R.drawable.fix_object_ch2_q2_01)
                .setAnswers(new boolean[]{false, true, true, false, true});

        //
        game_fix_data[1][2] = new FixStorage().setTitle(Game_Status.FIX_CH2_Q3).setTypeResource(Game_Status.GAME_FIX).setFixTitle(R.drawable.fix_title_sentence).setSubtitle(Game_Status.GAME_FIX_SENTENCE)
                .setLayoutId(R.layout.game_fix_base_sentence_2_3).setFixBackground(R.drawable.fix_bg_ch2_q3).setClickableObject(5)//.setClickableImages(R.drawable.fix_object_ch2_q3_01)
                .setAnswers(new boolean[]{true, true, false, false, true});

        // OK
        game_fix_data[1][3] = new FixStorage().setTitle(Game_Status.FIX_CH2_Q4).setTypeResource(Game_Status.GAME_FIX).setFixTitle(R.drawable.fix_title_algorithm).setSubtitle(Game_Status.GAME_FIX_ALGORITHM)
                .setLayoutId(R.layout.game_fix_base_algorithm_2_4).setFixBackground(R.drawable.fix_bg_ch2_q4).setClickableObject(12)//.setClickableImages(R.drawable.fix_object_ch2_q4_01)
                .setAnswers(new boolean[]{false, true, false, false, true, true, false, false, false, false, false, false});

        // OK
        game_fix_data[1][4] = new FixStorage().setTitle(Game_Status.FIX_CH2_Q5).setTypeResource(Game_Status.GAME_FIX).setFixTitle(R.drawable.fix_title_code).setSubtitle(Game_Status.GAME_FIX_CODE)
                .setLayoutId(R.layout.game_fix_base_code_2_5).setClickableObject(11).setFixBackground(R.drawable.fix_bg_ch2_q5)
                .setDialog(R.drawable.fix_result_ch2_q5)
                .setAnswers(new boolean[]{true, false, false, false, true, false, false, false, true, true, false});

        // OK
        game_fix_data[1][5] = new FixStorage().setTitle(Game_Status.FIX_CH2_Q6).setTypeResource(Game_Status.GAME_FIX).setFixTitle(R.drawable.fix_title_sentence).setSubtitle(Game_Status.GAME_FIX_SENTENCE)
                .setLayoutId(R.layout.game_fix_base_sentence_2_6).setFixBackground(R.drawable.fix_bg_ch2_q6).setClickableObject(5)//.setClickableImages(R.drawable.fix_object_ch2_q3_01)
                .setAnswers(new boolean[]{true, false, false, false, true});

        //
        game_fix_data[2][0] = new FixStorage().setTitle(Game_Status.FIX_CH3_Q1).setTypeResource(Game_Status.GAME_FIX).setFixTitle(R.drawable.fix_title_code).setSubtitle(Game_Status.GAME_FIX_CODE)
                .setLayoutId(R.layout.game_fix_base_code_3_1).setClickableObject(10).setFixBackground(R.drawable.fix_bg_ch3_q1)
                .setDialog(R.drawable.fix_result_ch3_q1)
                .setAnswers(new boolean[]{true, false, true, true, false, false, false, true, true, true});

        //
        game_fix_data[2][1] = new FixStorage().setTitle(Game_Status.FIX_CH3_Q2).setTypeResource(Game_Status.GAME_FIX).setFixTitle(R.drawable.fix_title_code).setSubtitle(Game_Status.GAME_FIX_CODE)
                .setLayoutId(R.layout.game_fix_base_code_3_2).setClickableObject(11).setFixBackground(R.drawable.fix_bg_ch3_q2)
                .setDialog(R.drawable.fix_result_ch3_q2)
                .setAnswers(new boolean[]{false, false, false, true, true, false, true, true, true, false, false});

        //
        game_fix_data[2][2] = new FixStorage().setTitle(Game_Status.FIX_CH3_Q3).setTypeResource(Game_Status.GAME_FIX).setFixTitle(R.drawable.fix_title_code).setSubtitle(Game_Status.GAME_FIX_CODE)
                .setLayoutId(R.layout.game_fix_base_code_3_3).setClickableObject(12).setFixBackground(R.drawable.fix_bg_ch3_q3)
                .setDialog(R.drawable.fix_result_ch3_q3)
                .setAnswers(new boolean[]{true, false, false, true, true, false, true, true, true, true, false, false});

        //
        game_fix_data[2][3] = new FixStorage().setTitle(Game_Status.FIX_CH3_Q4).setTypeResource(Game_Status.GAME_FIX).setFixTitle(R.drawable.fix_title_code).setSubtitle(Game_Status.GAME_FIX_CODE)
                .setLayoutId(R.layout.game_fix_base_code_3_4).setClickableObject(13).setFixBackground(R.drawable.fix_bg_ch3_q4)
                .setDialog(R.drawable.fix_result_ch3_q4)
                .setAnswers(new boolean[]{true, false, false, true, true, false, true, false, true, true, true, false, false});

        game_fix_data[2][4] = new FixStorage().setTitle(Game_Status.FIX_CH3_Q5).setTypeResource(Game_Status.GAME_FIX).setFixTitle(R.drawable.fix_title_code).setSubtitle(Game_Status.GAME_FIX_CODE)
                .setLayoutId(R.layout.game_fix_base_code_3_5).setClickableObject(10).setFixBackground(R.drawable.fix_bg_ch3_q5)
                .setDialog(R.drawable.fix_result_ch3_q5)
                .setAnswers(new boolean[]{true, false, true, false, true, true, true, true, true, true});

        // OK
        game_fix_data[2][5] = new FixStorage().setTitle(Game_Status.FIX_CH3_Q6).setTypeResource(Game_Status.GAME_FIX).setFixTitle(R.drawable.fix_title_code).setSubtitle(Game_Status.GAME_FIX_CODE)
                .setLayoutId(R.layout.game_fix_base_code_3_6).setClickableObject(12).setFixBackground(R.drawable.fix_bg_ch3_q6)
                .setDialog(R.drawable.fix_result_ch3_q6)
                .setAnswers(new boolean[]{false, false, false, false, false, true, false, true, true, true, false, true});

    }
}
