package com.example.m_user.geel_job.Tutorial.Storage;

import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.R;
import com.example.m_user.geel_job.Tutorial.Tutorial_ch_num;

/**
 * Created by They on 2017-10-22.
 * 기본 틀은 그대로 놔두고 나중에 장-소단원 정보 추가될 때마다 아래 setChapters()에서 객체 하나 추가해주면 됩니다. 그 전에 CH_MAX 값을 추가할 수만큼 올리고.ㄴ
 */

public class TutorialStatus extends CustomFontActivity { // 튜토리얼 장 선택, 퀴즈 선택 저장용
    private static TutorialStatus instance = null; // Singletone

    public static TutorialStatus getInstance(){
        if(instance == null){
            instance = new TutorialStatus();
        }
        return instance;
    }

    private int selected_ch_num = -1; // 현재 선택한 장 저장

    public TutorialStatus setSelected_ch_num(int selected_ch_num) {
        this.selected_ch_num = selected_ch_num;
        return this;
    }

    public int getSelected_ch_num() {
        return selected_ch_num;
    }

    private int selected_ch_sub_num = -1; // 현재 선택한 소단원 저장

    public TutorialStatus setSelected_ch_sub_num(int selected_ch_sub_num) {
        this.selected_ch_sub_num = selected_ch_sub_num;
        return this;
    }

    public int getSelected_ch_sub_num() {
        return selected_ch_sub_num;
    }

    private ChapterInfo[] chapters = new ChapterInfo[Tutorial_ch_num.CH_MAX]; // 1-1 ~ 3-6장 세세한 정보 모음.

    public ChapterInfo[] getChapters()
    {
        return chapters;
    }

    private ChapterInfo selectedChapterInfo = null; // 선택된 장의 정보

    public TutorialStatus setSelectedChapterInfo() // 현재 진행중인 장 정보를 검색 후 저장
    {
        for(int i=0; i<chapters.length; i++)
        {
            if(selected_ch_sub_num == chapters[i].getCh_sub_number()) // 만일 현재 선택한 장이 리스트 중 하나와 일치하면 저장
            {
                selectedChapterInfo = chapters[i];
            }
        }

        return this;
    }

    public ChapterInfo getSelectedChapterInfo()
    {
        return selectedChapterInfo;
    }

    public void reset() // 선택된 장 정보 초기화
    {
        selected_ch_num = -1;
        selected_ch_sub_num = -1;

    }

    public void resetSelectedChapterInfo() // 선택된 장 정보 초기화
    {
        selectedChapterInfo.reset();

        selectedChapterInfo = null;
    }

    public TutorialStatus setChapters() {
        // 이하는 각 장별로 정보 저장 작업. 주의 : 함부로 수정 말 것!

        chapters[0] = new ChapterInfo()
                .setCh_number(Tutorial_ch_num.CH1).setCh_sub_number(Tutorial_ch_num.CH1_1) // 장, 소단원 정보 저장
                .setStart_layout(R.drawable.ch1_sub1_page01).setMax_page(Tutorial_ch_num.CH1_1_MAX_PAGE) // 첫 페이지를 담당하는 레이아웃, 최대 페이지 정보 저장
                .setRestart_page(Tutorial_ch_num.CH1_1_RE).setRestart_layout(R.drawable.ch1_sub1_page01 + Tutorial_ch_num.CH1_1_RE - 1) // 리플레이로 재시작할 때 시작 페이지 및 이미지 레이아웃 정보 저장
                .setFailed_endPage(Tutorial_ch_num.CH1_SUB1_QUIZ_FAILED_MAX_PAGE).setReplay_layout(R.drawable.ch1_sub1_replay01) // OX퀴즈 틀린 이후 틀렸을 때의 최대 페이지와 리플레이 이미지 레이아웃 정보 저장
                .setClear_endPage(Tutorial_ch_num.CH1_1_END_MAX_PAGE).setClear_layout(R.drawable.ch1_sub1_ox_clear01).setClear_endLayout(R.drawable.ch1_sub1_end01) // 맞춘 후의 최대 페이지, 클리어 및 종료 레이아웃 정보 저장
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH1_1_OX1).setQuizButton(false, true).setHint(R.drawable.ch1_sub1_ox_hint1)
                        .setQuiz_layout(R.drawable.ch1_sub1_ox1).setFailed_layout(R.drawable.ch1_sub1_ox1_fail01)) // 1번 문제. 퀴즈명, 퀴즈 정답, 힌트, 배경, 틀렸을 때의 시작 배경 정보 저장
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH1_1_OX2).setQuizButton(false, true).setHint(R.drawable.ch1_sub1_ox_hint2)
                        .setQuiz_layout(R.drawable.ch1_sub1_ox2).setFailed_layout(R.drawable.ch1_sub1_ox2_fail01)) // 2번 문제. 위와 동일.
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH1_1_OX3).setQuizButton(true, false).setHint(R.drawable.ch1_sub1_ox_hint3)
                        .setQuiz_layout(R.drawable.ch1_sub1_ox3).setFailed_layout(R.drawable.ch1_sub1_ox3_fail01)); // 3번 문제

        chapters[1] = new ChapterInfo()
                .setCh_number(Tutorial_ch_num.CH1).setCh_sub_number(Tutorial_ch_num.CH1_2)
                .setStart_layout(R.drawable.ch1_sub2_page01).setMax_page(Tutorial_ch_num.CH1_2_MAX_PAGE)
                .setRestart_page(Tutorial_ch_num.CH1_2_RE).setRestart_layout(R.drawable.ch1_sub2_page01 + Tutorial_ch_num.CH1_2_RE - 1)
                .setFailed_endPage(Tutorial_ch_num.QUIZ_FAILED_MAX_PAGE).setReplay_layout(R.drawable.ch1_sub2_replay01)
                .setClear_endPage(Tutorial_ch_num.CH1_2_END_MAX_PAGE).setClear_layout(R.drawable.ch1_sub2_ox_clear01).setClear_endLayout(R.drawable.ch1_sub2_end01)
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH1_2_OX1).setQuizButton(true, false).setHint(R.drawable.ch1_sub2_ox_hint1)
                        .setQuiz_layout(R.drawable.ch1_sub2_ox1).setFailed_layout(R.drawable.ch1_sub2_ox1_fail01))
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH1_2_OX2).setQuizButton(true, false).setHint(R.drawable.ch1_sub2_ox_hint2)
                        .setQuiz_layout(R.drawable.ch1_sub2_ox2).setFailed_layout(R.drawable.ch1_sub2_ox2_fail01))
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH1_2_OX3).setQuizButton(false, true).setHint(R.drawable.ch1_sub2_ox_hint3)
                        .setQuiz_layout(R.drawable.ch1_sub2_ox3).setFailed_layout(R.drawable.ch1_sub2_ox3_fail01));

        chapters[2] = new ChapterInfo()
                .setCh_number(Tutorial_ch_num.CH1).setCh_sub_number(Tutorial_ch_num.CH1_3)
                .setStart_layout(R.drawable.ch1_sub3_page01).setMax_page(Tutorial_ch_num.CH1_3_MAX_PAGE)
                .setRestart_page(Tutorial_ch_num.CH1_3_RE).setRestart_layout(R.drawable.ch1_sub3_page01 + Tutorial_ch_num.CH1_3_RE - 1)
                .setFailed_endPage(Tutorial_ch_num.QUIZ_FAILED_MAX_PAGE).setReplay_layout(R.drawable.ch1_sub3_replay01)
                .setClear_endPage(Tutorial_ch_num.CH1_3_END_MAX_PAGE).setClear_layout(R.drawable.ch1_sub3_ox_clear01).setClear_endLayout(R.drawable.ch1_sub3_end01)
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH1_3_OX1).setQuizButton(false, true).setHint(R.drawable.ch1_sub3_ox_hint1)
                        .setQuiz_layout(R.drawable.ch1_sub3_ox1).setFailed_layout(R.drawable.ch1_sub3_ox1_fail01))
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH1_3_OX2).setQuizButton(true, false).setHint(R.drawable.ch1_sub3_ox_hint2)
                        .setQuiz_layout(R.drawable.ch1_sub3_ox2).setFailed_layout(R.drawable.ch1_sub3_ox2_fail01))
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH1_3_OX3).setQuizButton(true, false).setHint(R.drawable.ch1_sub3_ox_hint3)
                        .setQuiz_layout(R.drawable.ch1_sub3_ox3).setFailed_layout(R.drawable.ch1_sub3_ox3_fail01));

        chapters[3] = new ChapterInfo()
                .setCh_number(Tutorial_ch_num.CH2).setCh_sub_number(Tutorial_ch_num.CH2_1)
                .setStart_layout(R.drawable.ch2_sub1_page01).setMax_page(Tutorial_ch_num.CH2_1_MAX_PAGE)
                .setRestart_page(Tutorial_ch_num.CH2_1_RE).setRestart_layout(R.drawable.ch2_sub1_page01 + Tutorial_ch_num.CH2_1_RE - 1)
                .setFailed_endPage(Tutorial_ch_num.QUIZ_FAILED_MAX_PAGE).setReplay_layout(R.drawable.ch2_sub1_replay01)
                .setClear_endPage(Tutorial_ch_num.CH2_1_END_MAX_PAGE).setClear_layout(R.drawable.ch2_sub1_ox_clear01).setClear_endLayout(R.drawable.ch2_sub1_end01)
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH2_1_OX1).setQuizButton(false, true).setHint(R.drawable.ch2_sub1_ox_hint1)
                        .setQuiz_layout(R.drawable.ch2_sub1_ox1).setFailed_layout(R.drawable.ch2_sub1_ox1_fail01))
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH2_1_OX2).setQuizButton(true, false).setHint(R.drawable.ch2_sub1_ox_hint2)
                        .setQuiz_layout(R.drawable.ch2_sub1_ox2).setFailed_layout(R.drawable.ch2_sub1_ox2_fail01))
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH2_1_OX3).setQuizButton(true, false).setHint(R.drawable.ch2_sub1_ox_hint3)
                        .setQuiz_layout(R.drawable.ch2_sub1_ox3).setFailed_layout(R.drawable.ch2_sub1_ox3_fail01));

        chapters[4] = new ChapterInfo()
                .setCh_number(Tutorial_ch_num.CH3).setCh_sub_number(Tutorial_ch_num.CH3_1)
                .setStart_layout(R.drawable.ch3_sub1_page01).setMax_page(Tutorial_ch_num.CH3_1_MAX_PAGE)
                .setRestart_page(Tutorial_ch_num.CH3_1_RE).setRestart_layout(R.drawable.ch3_sub1_page01 + Tutorial_ch_num.CH3_1_RE - 1)
                .setFailed_endPage(Tutorial_ch_num.QUIZ_FAILED_MAX_PAGE).setReplay_layout(R.drawable.ch3_sub1_replay01)
                .setClear_endPage(Tutorial_ch_num.CH3_1_END_MAX_PAGE).setClear_layout(R.drawable.ch3_sub1_ox_clear01).setClear_endLayout(R.drawable.ch3_sub1_end01)
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH3_1_OX1).setQuizButton(true, false).setHint(R.drawable.ch3_sub1_ox_hint1)
                        .setQuiz_layout(R.drawable.ch3_sub1_ox1).setFailed_layout(R.drawable.ch3_sub1_ox1_fail01))
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH3_1_OX2).setQuizButton(true, false).setHint(R.drawable.ch3_sub1_ox_hint2)
                        .setQuiz_layout(R.drawable.ch3_sub1_ox2).setFailed_layout(R.drawable.ch3_sub1_ox2_fail01))
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH3_1_OX3).setQuizButton(false, true).setHint(R.drawable.ch3_sub1_ox_hint3)
                        .setQuiz_layout(R.drawable.ch3_sub1_ox3).setFailed_layout(R.drawable.ch3_sub1_ox3_fail01));

        chapters[5] = new ChapterInfo()
                .setCh_number(Tutorial_ch_num.CH3).setCh_sub_number(Tutorial_ch_num.CH3_2)
                .setStart_layout(R.drawable.ch3_sub2_page01).setMax_page(Tutorial_ch_num.CH3_2_MAX_PAGE)
                .setRestart_page(Tutorial_ch_num.CH3_2_RE).setRestart_layout(R.drawable.ch3_sub2_page01 + Tutorial_ch_num.CH3_2_RE - 1)
                .setFailed_endPage(Tutorial_ch_num.QUIZ_FAILED_MAX_PAGE).setReplay_layout(R.drawable.ch3_sub2_replay01)
                .setClear_endPage(Tutorial_ch_num.CH3_2_END_MAX_PAGE).setClear_layout(R.drawable.ch3_sub2_ox_clear01).setClear_endLayout(R.drawable.ch3_sub2_end01)
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH3_2_OX1).setQuizButton(false, true).setHint(R.drawable.ch3_sub2_ox_hint1)
                        .setQuiz_layout(R.drawable.ch3_sub2_ox1).setFailed_layout(R.drawable.ch3_sub2_ox1_fail01))
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH3_2_OX2).setQuizButton(false, true).setHint(R.drawable.ch3_sub2_ox_hint2)
                        .setQuiz_layout(R.drawable.ch3_sub2_ox2).setFailed_layout(R.drawable.ch3_sub2_ox2_fail01))
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH3_2_OX3).setQuizButton(true, false).setHint(R.drawable.ch3_sub2_ox_hint3)
                        .setQuiz_layout(R.drawable.ch3_sub2_ox3).setFailed_layout(R.drawable.ch3_sub2_ox3_fail01));

        chapters[6] = new ChapterInfo()
                .setCh_number(Tutorial_ch_num.CH3).setCh_sub_number(Tutorial_ch_num.CH3_3)
                .setStart_layout(R.drawable.ch3_sub3_page01).setMax_page(Tutorial_ch_num.CH3_3_MAX_PAGE)
                .setRestart_page(Tutorial_ch_num.CH3_3_RE).setRestart_layout(R.drawable.ch3_sub3_page01 + Tutorial_ch_num.CH3_3_RE - 1)
                .setFailed_endPage(Tutorial_ch_num.QUIZ_FAILED_MAX_PAGE).setReplay_layout(R.drawable.ch3_sub3_replay01)
                .setClear_endPage(Tutorial_ch_num.CH3_3_END_MAX_PAGE).setClear_layout(R.drawable.ch3_sub3_ox_clear01).setClear_endLayout(R.drawable.ch3_sub3_end01)
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH3_3_OX1).setQuizButton(true, false).setHint(R.drawable.ch3_sub3_ox_hint1)
                        .setQuiz_layout(R.drawable.ch3_sub3_ox1).setFailed_layout(R.drawable.ch3_sub3_ox1_fail01))
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH3_3_OX2).setQuizButton(true, false).setHint(R.drawable.ch3_sub3_ox_hint2)
                        .setQuiz_layout(R.drawable.ch3_sub3_ox2).setFailed_layout(R.drawable.ch3_sub3_ox2_fail01))
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH3_3_OX3).setQuizButton(false, true).setHint(R.drawable.ch3_sub3_ox_hint3)
                        .setQuiz_layout(R.drawable.ch3_sub3_ox3).setFailed_layout(R.drawable.ch3_sub3_ox3_fail01));

        chapters[7] = new ChapterInfo()
                .setCh_number(Tutorial_ch_num.CH3).setCh_sub_number(Tutorial_ch_num.CH3_4)
                .setStart_layout(R.drawable.ch3_sub4_page01).setMax_page(Tutorial_ch_num.CH3_4_MAX_PAGE)
                .setRestart_page(Tutorial_ch_num.CH3_4_RE).setRestart_layout(R.drawable.ch3_sub4_page01 + Tutorial_ch_num.CH3_4_RE - 1)
                .setFailed_endPage(Tutorial_ch_num.QUIZ_FAILED_MAX_PAGE).setReplay_layout(R.drawable.ch3_sub4_replay01)
                .setClear_endPage(Tutorial_ch_num.CH3_4_END_MAX_PAGE).setClear_layout(R.drawable.ch3_sub4_ox_clear01).setClear_endLayout(R.drawable.ch3_sub4_end01)
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH3_4_OX1).setQuizButton(false, true).setHint(R.drawable.ch3_sub4_ox_hint1)
                        .setQuiz_layout(R.drawable.ch3_sub4_ox1).setFailed_layout(R.drawable.ch3_sub4_ox1_fail01))
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH3_4_OX2).setQuizButton(false, true).setHint(R.drawable.ch3_sub4_ox_hint2)
                        .setQuiz_layout(R.drawable.ch3_sub4_ox2).setFailed_layout(R.drawable.ch3_sub4_ox2_fail01))
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH3_4_OX3).setQuizButton(false, true).setHint(R.drawable.ch3_sub4_ox_hint3)
                        .setQuiz_layout(R.drawable.ch3_sub4_ox3).setFailed_layout(R.drawable.ch3_sub4_ox3_fail01));

        chapters[8] = new ChapterInfo()
                .setCh_number(Tutorial_ch_num.CH3).setCh_sub_number(Tutorial_ch_num.CH3_5)
                .setStart_layout(R.drawable.ch3_sub5_page01).setMax_page(Tutorial_ch_num.CH3_5_MAX_PAGE)
                .setRestart_page(Tutorial_ch_num.CH3_5_RE).setRestart_layout(R.drawable.ch3_sub5_page01 + Tutorial_ch_num.CH3_5_RE - 1)
                .setFailed_endPage(Tutorial_ch_num.QUIZ_FAILED_MAX_PAGE).setReplay_layout(R.drawable.ch3_sub5_replay01)
                .setClear_endPage(Tutorial_ch_num.CH3_5_END_MAX_PAGE).setClear_layout(R.drawable.ch3_sub5_ox_clear01).setClear_endLayout(R.drawable.ch3_sub5_end01)
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH3_5_OX1).setQuizButton(true, false).setHint(R.drawable.ch3_sub5_ox_hint1)
                        .setQuiz_layout(R.drawable.ch3_sub5_ox1).setFailed_layout(R.drawable.ch3_sub5_ox1_fail01))
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH3_5_OX2).setQuizButton(true, false).setHint(R.drawable.ch3_sub5_ox_hint2)
                        .setQuiz_layout(R.drawable.ch3_sub5_ox2).setFailed_layout(R.drawable.ch3_sub5_ox2_fail01))
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH3_5_OX3).setQuizButton(true, false).setHint(R.drawable.ch3_sub5_ox_hint3)
                        .setQuiz_layout(R.drawable.ch3_sub5_ox3).setFailed_layout(R.drawable.ch3_sub5_ox3_fail01));

        chapters[9] = new ChapterInfo()
                .setCh_number(Tutorial_ch_num.CH3).setCh_sub_number(Tutorial_ch_num.CH3_6)
                .setStart_layout(R.drawable.ch3_sub6_page01).setMax_page(Tutorial_ch_num.CH3_6_MAX_PAGE)
                .setRestart_page(Tutorial_ch_num.CH3_6_RE).setRestart_layout(R.drawable.ch3_sub6_page01 + Tutorial_ch_num.CH3_6_RE - 1)
                .setFailed_endPage(Tutorial_ch_num.QUIZ_FAILED_MAX_PAGE).setReplay_layout(R.drawable.ch3_sub6_replay01)
                .setClear_endPage(Tutorial_ch_num.CH3_6_END_MAX_PAGE).setClear_layout(R.drawable.ch3_sub6_ox_clear01).setClear_endLayout(R.drawable.ch3_sub6_end01)
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH3_6_OX1).setQuizButton(false, true).setHint(R.drawable.ch3_sub6_ox_hint1)
                        .setQuiz_layout(R.drawable.ch3_sub6_ox1).setFailed_layout(R.drawable.ch3_sub6_ox1_fail01))
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH3_6_OX2).setQuizButton(false, true).setHint(R.drawable.ch3_sub6_ox_hint2)
                        .setQuiz_layout(R.drawable.ch3_sub6_ox2).setFailed_layout(R.drawable.ch3_sub6_ox2_fail01))
                .setQuizlist(new Quiz().setQuizName(Tutorial_ch_num.CH3_6_OX3).setQuizButton(false, true).setHint(R.drawable.ch3_sub6_ox_hint3)
                        .setQuiz_layout(R.drawable.ch3_sub6_ox3).setFailed_layout(R.drawable.ch3_sub6_ox3_fail01));
        // 정보 저장 끝. 여기서 장 번호는 장이 늘어날 때마다 + 100, 소단원은 늘어날 때마다 + 10, 나머지는 +로 찾기엔 애매.

        return this;
    }
}
