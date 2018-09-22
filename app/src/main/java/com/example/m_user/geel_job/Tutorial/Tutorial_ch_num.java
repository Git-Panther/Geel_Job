package com.example.m_user.geel_job.Tutorial;

/**
 * Created by KYG on 2017-10-06.
 * How to Use : Define or rewrite useful static final integers and use them from this interface.
 */

public interface Tutorial_ch_num {

    int CH_MAX = 10; // 현재 장-소단원 개수

    // 이하는 각 장 int
    int CH1 = 100;
    int CH2 = 200;
    int CH3 = 300;

    // 이하는 각 장 소단원 int
    int CH1_1 = 110;
    int CH1_2 = 120;
    int CH1_3 = 130;
    int CH2_1 = 210;
    int CH3_1 = 310;
    int CH3_2 = 320;
    int CH3_3 = 330;
    int CH3_4 = 340;
    int CH3_5 = 350;
    int CH3_6 = 360;

    // 이하는 각 장 최대 페이지
    int MIN_PAGE = 1; // 최소 페이지. 어차피 1이지만 가독성 있게 하기 위해 만듬

    int TUTORIAL_EVENT_MAX_PAGE = 24;
    int TUTORIAL_EVENT_BREAKPOINT_CH = 7;
    int TUTORIAL_EVENT_BREAKPOINT_CH_SUB = 9;

    int CH1_1_MAX_PAGE = 26;
    int CH1_1_END_MAX_PAGE = 3;

    int CH1_2_MAX_PAGE = 22;
    int CH1_2_END_MAX_PAGE = 3;

    int CH1_3_MAX_PAGE = 35;
    int CH1_3_END_MAX_PAGE = 8;

    int CH2_1_MAX_PAGE = 35;
    int CH2_1_END_MAX_PAGE = 3;

    int CH3_1_MAX_PAGE = 28;
    int CH3_1_END_MAX_PAGE = 3;

    int CH3_2_MAX_PAGE = 17;
    int CH3_2_END_MAX_PAGE = 3;

    int CH3_3_MAX_PAGE = 47;
    int CH3_3_END_MAX_PAGE = 3;

    int CH3_4_MAX_PAGE = 17;
    int CH3_4_END_MAX_PAGE = 3;

    int CH3_5_MAX_PAGE = 17;
    int CH3_5_END_MAX_PAGE = 3;

    int CH3_6_MAX_PAGE = 28;
    int CH3_6_END_MAX_PAGE = 5;

    // 이하는 각 장 소단원 ox 문제 int
    int CH1_1_OX1 = 111;
    int CH1_1_OX2 = 112;
    int CH1_1_OX3 = 113;

    int CH1_2_OX1 = 121;
    int CH1_2_OX2 = 122;
    int CH1_2_OX3 = 123;

    int CH1_3_OX1 = 131;
    int CH1_3_OX2 = 132;
    int CH1_3_OX3 = 133;

    int CH2_1_OX1 = 211;
    int CH2_1_OX2 = 212;
    int CH2_1_OX3 = 213;

    int CH3_1_OX1 = 311;
    int CH3_1_OX2 = 312;
    int CH3_1_OX3 = 313;

    int CH3_2_OX1 = 321;
    int CH3_2_OX2 = 322;
    int CH3_2_OX3 = 323;

    int CH3_3_OX1 = 331;
    int CH3_3_OX2 = 332;
    int CH3_3_OX3 = 333;

    int CH3_4_OX1 = 341;
    int CH3_4_OX2 = 342;
    int CH3_4_OX3 = 343;

    int CH3_5_OX1 = 351;
    int CH3_5_OX2 = 352;
    int CH3_5_OX3 = 353;

    int CH3_6_OX1 = 361;
    int CH3_6_OX2 = 362;
    int CH3_6_OX3 = 363;

    int QUIZ_RESULT = 2; // OX 퀴즈 결과 페이지
    int CH1_SUB1_QUIZ_FAILED_MAX_PAGE = 4; // 1-1장 한정 OX 오답시 최대 페이지 수
    int QUIZ_FAILED_MAX_PAGE = 3; // OX 오답시 최대 페이지 수

    // 이하는 재시작 페이지

    int CH1_1_RE = 2;
    int CH1_2_RE = 3;
    int CH1_3_RE = 1;
    int CH2_1_RE = 3;
    int CH3_1_RE = 5                                                                                                                                                                                                                                                                                      ;
    int CH3_2_RE = 5;
    int CH3_3_RE = 4;
    int CH3_4_RE = 4;
    int CH3_5_RE = 3;
    int CH3_6_RE = 4;
}
