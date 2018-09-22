package com.example.m_user.geel_job.GamePlay;

/**
 * Created by KYG on 2017-10-06.
 * How to Use : Define or rewrite useful static final integers and use them from this interface.
 */

public interface Game_Status { // 여기에 게임하기와 관련된 상수 저장
    int MIN_PAGE = 1; // 최소 페이지. 어차피 1이지만 가독성 있게 하기 위해 만듬

    int GAME_EVENT_MAX_PAGE = 23; // 게임하기 이벤트 총 페이지
    int GAME_EVENT_CHOICE_BREAKPOINT = 5; // 3~5 페이지는 이미지 뷰 넣어서 클릭 가능하게 해야 함.
    int GAME_EVENT_CHOICE_SHOW = 3;
    int GAME_EVENT_MINIGAME_SHOW = 8; // 8~17 까지는 미니게임 이미지 뷰 써야 함.
    int GAME_EVENT_MINIGAME_BREAKPOINT = 16; // 미니게임 하는 구간
    int GAME_EVENT_MINIGAME_CHECK = 17; // 미니게임 채점 구간
    int GAME_EVENT_AFTERQUIZ = 18; // 퀴즈 정상적으로 넘어온 후부터의 구간
    int GAME_EVENT_FOCUS_02 = 14; // 게임하기 이벤트로 포커스 이미지 띄우는 구간 2(1은 5페이지와 동일)
    int GAME_EVENT_FOCUS_03 = 15; // 게임하기 이벤트로 포커스 이미지 띄우는 구간 3

    int[] GAME_EVENT_ANSWER = { 0, 2, 5 }; // 게임하기 이벤트로 하는 빈칸 채우기의 정답 버튼 위치. 가로 순.
    int GAME_EVENT_QUEST_AMOUNT = 3; // 풀어야 할 빈칸 개수
    int GAME_EVENT_ANSWER_AMOUNT = 6; // 정답으로 제시되는 항목 개수

    // 이하는 게임하기 정보 관련

    int GAME_BLANK = 1000;

    int GAME_BLANK_BASIC = 1001;
    int GAME_BLANK_SENTENCE = 1002;
    int GAME_BLANK_CODE = 1003;
    int GAME_BLANK_ETC = 1004;

    int GAME_BOOL = 2000;

    int GAME_ORDER = 3000;

    int GAME_FIX = 4000;

    int GAME_FIX_SENTENCE = 4001;
    int GAME_FIX_ALGORITHM = 4002;
    int GAME_FIX_ORDER = 4003;
    int GAME_FIX_CODE = 4004;
    int GAME_FIX_ETC = 4005;

    // 각 미니게임의 이름

    int BLANK_CH1_Q1 = 1110;
    int BLANK_CH1_Q2 = 1120;
    int BLANK_CH1_Q3 = 1130;
    int BLANK_CH1_Q4 = 1140;
    int BLANK_CH1_Q5 = 1150;
    int BLANK_CH1_Q6 = 1160;

    int BLANK_CH2_Q1 = 1210;
    int BLANK_CH2_Q2 = 1220;
    int BLANK_CH2_Q3 = 1230;
    int BLANK_CH2_Q4 = 1240;
    int BLANK_CH2_Q5 = 1250;
    int BLANK_CH2_Q6 = 1260;

    int BLANK_CH3_Q1 = 1310;
    int BLANK_CH3_Q2 = 1320;
    int BLANK_CH3_Q3 = 1330;
    int BLANK_CH3_Q4 = 1340;
    int BLANK_CH3_Q5 = 1350;
    int BLANK_CH3_Q6 = 1360;

    int BOOL_CH1_Q1_1 = 2111;
    int BOOL_CH1_Q1_2 = 2112;
    int BOOL_CH1_Q1_3 = 2113;

    int BOOL_CH1_Q2_1 = 2121;
    int BOOL_CH1_Q2_2 = 2122;
    int BOOL_CH1_Q2_3 = 2123;

    int BOOL_CH1_Q3_1 = 2131;
    int BOOL_CH1_Q3_2 = 2132;
    int BOOL_CH1_Q3_3 = 2133;

    int BOOL_CH1_Q4_1 = 2141;
    int BOOL_CH1_Q4_2 = 2142;
    int BOOL_CH1_Q4_3 = 2143;

    int BOOL_CH1_Q5_1 = 2151;
    int BOOL_CH1_Q5_2 = 2152;
    int BOOL_CH1_Q5_3 = 2153;

    int BOOL_CH1_Q6_1 = 2161;
    int BOOL_CH1_Q6_2 = 2162;
    int BOOL_CH1_Q6_3 = 2163;

    int BOOL_CH2_Q1_1 = 2211;
    int BOOL_CH2_Q1_2 = 2212;
    int BOOL_CH2_Q1_3 = 2213;

    int BOOL_CH2_Q2_1 = 2221;
    int BOOL_CH2_Q2_2 = 2222;
    int BOOL_CH2_Q2_3 = 2223;

    int BOOL_CH2_Q3_1 = 2231;
    int BOOL_CH2_Q3_2 = 2232;
    int BOOL_CH2_Q3_3 = 2233;

    int BOOL_CH2_Q4_1 = 2241;
    int BOOL_CH2_Q4_2 = 2242;
    int BOOL_CH2_Q4_3 = 2243;

    int BOOL_CH2_Q5_1 = 2251;
    int BOOL_CH2_Q5_2 = 2252;
    int BOOL_CH2_Q5_3 = 2253;

    int BOOL_CH2_Q6_1 = 2261;
    int BOOL_CH2_Q6_2 = 2262;
    int BOOL_CH2_Q6_3 = 2263;

    int BOOL_CH3_Q1_1 = 2311;
    int BOOL_CH3_Q1_2 = 2312;
    int BOOL_CH3_Q1_3 = 2313;

    int BOOL_CH3_Q2_1 = 2321;
    int BOOL_CH3_Q2_2 = 2322;
    int BOOL_CH3_Q2_3 = 2323;

    int BOOL_CH3_Q3_1 = 2331;
    int BOOL_CH3_Q3_2 = 2332;
    int BOOL_CH3_Q3_3 = 2333;

    int BOOL_CH3_Q4_1 = 2341;
    int BOOL_CH3_Q4_2 = 2342;
    int BOOL_CH3_Q4_3 = 2343;

    int BOOL_CH3_Q5_1 = 2351;
    int BOOL_CH3_Q5_2 = 2352;
    int BOOL_CH3_Q5_3 = 2353;

    int BOOL_CH3_Q6_1 = 2361;
    int BOOL_CH3_Q6_2 = 2362;
    int BOOL_CH3_Q6_3 = 2363;

    int ORDER_CH1_Q1 = 3110;
    int ORDER_CH1_Q2 = 3120;
    int ORDER_CH1_Q3 = 3130;
    int ORDER_CH1_Q4 = 3140;
    int ORDER_CH1_Q5 = 3150;
    int ORDER_CH1_Q6 = 3160;

    int ORDER_CH2_Q1 = 3210;
    int ORDER_CH2_Q2 = 3220;
    int ORDER_CH2_Q3 = 3230;
    int ORDER_CH2_Q4 = 3240;
    int ORDER_CH2_Q5 = 3250;
    int ORDER_CH2_Q6 = 3260;

    int ORDER_CH3_Q1 = 3310;
    int ORDER_CH3_Q2 = 3320;
    int ORDER_CH3_Q3 = 3330;
    int ORDER_CH3_Q4 = 3340;
    int ORDER_CH3_Q5 = 3350;
    int ORDER_CH3_Q6 = 3360;

    int FIX_CH1_Q1 = 4110;
    int FIX_CH1_Q2 = 4120;
    int FIX_CH1_Q3 = 4130;
    int FIX_CH1_Q4 = 4140;
    int FIX_CH1_Q5 = 4150;
    int FIX_CH1_Q6 = 4160;

    int FIX_CH2_Q1 = 4210;
    int FIX_CH2_Q2 = 4220;
    int FIX_CH2_Q3 = 4230;
    int FIX_CH2_Q4 = 4240;
    int FIX_CH2_Q5 = 4250;
    int FIX_CH2_Q6 = 4260;

    int FIX_CH3_Q1 = 4310;
    int FIX_CH3_Q2 = 4320;
    int FIX_CH3_Q3 = 4330;
    int FIX_CH3_Q4 = 4340;
    int FIX_CH3_Q5 = 4350;
    int FIX_CH3_Q6 = 4360;

    // 이하는 게임하기 상태.

    int GAME_CLEAR = 10000;
    int GAME_FAIL = 20000;
    int GAME_TIMEOVER = 30000;
    int GAME_END = 1000000; // 게임을 10 스테이지까지 깨면 보내는 상태. 물론 제한 시간 이내로.

    // 이하는 게임하기 점수 분배.

    int GAME_CLEAR_BONUS = 300;
    int GAME_CLEAR_SMALL_BONUS = 100;
    int GAME_FAIL_PENALTY = -150;
    int GAME_FAIL_SMALL_PENALTY = -50; // 조건문 고개, 틀린 부분 찾기에서 1회 틀릴 때마다 감점되는 점수
    int GAME_TIME_BONUS = 10; // 게임하기를 완전히 클리어하고 시간도 남으면 가중치로 부여되는 시간 보너스
    int GAME_SCORE_DIVIDING_PERCENT = 3;

    // 이하는 기타 인터페이스

    int GAME_TIME = 300;
    int GAME_MAX_STAGE = 10;

    int GAME_CH1 = 1;
    int GAME_CH2 = 2;
    int GAME_CH3 = 3;

    int TIME_ONE_SECOND = 1000;

}
