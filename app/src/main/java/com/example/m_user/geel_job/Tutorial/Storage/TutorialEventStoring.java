package com.example.m_user.geel_job.Tutorial.Storage;

import android.app.Activity;

import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.Tutorial.Tutorial_ch_num;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by M-user on 2017-09-12.
 * 상태를 저장할려고 만듬
 * 엑티비티 종료시 변수 안에있는 값이 다 없어지기 때문에
 * 여기에 대신 변수값을 저장함
 */

public class TutorialEventStoring extends CustomFontActivity {
    private static TutorialEventStoring instance; // 싱글톤 쓸려고 만듬
    private static int clear_check = 0; // ox클리어 체크 번호 성공:1 실패:2 시간끝:3으로 설정
    private int pageNum = Tutorial_ch_num.MIN_PAGE; // 이벤트에 쓰이는 현재 페이지
    List list = new ArrayList(); // 배열 대신 ox랜덤으로 섞을려고 사용

    // 이하는 추가 변수

    private int cur_ch; // 현재 진행중인 메인 장을 저장하기 위한 변수*
    private int cur_ch_sub; // 서브 장 번호 저장 변수
    private int cur_quiz; // 퀴즈 번호 저장 변수
    private Activity cur_act; // 1-3장 게임하기 버튼 누르고 뒤로 가기 누르면 소단원 창 선택하는 곳으로 넘어가는 문제를 해결하기 위한 변수*
    private Activity cur_act_sub; // 1-3장 게임하기 버튼 누르고 뒤로 가기 누르면 소단원 창 선택하는 곳으로 넘어가는 문제를 해결하기 위한 변수*

    public void setChapter(int ch) // 장 설정. 장에 대한 정보를 저장하기 위해 씀.*
    {
        cur_ch = ch;
    }

    public int getChapter() // 장 반환. 검사에 쓰임.*
    {
        return cur_ch;
    }

    public void setChapterSub(int ch_sub) { cur_ch_sub = ch_sub; } // 소단원 값 설정할 때 쓰려고 한 거. 지금은 안 씀*
    public int getChapterSub() { return cur_ch_sub; }

    public void setQuiz(int quiz) { cur_quiz = quiz; } // 퀴즈 값을 정수로 설정할 때 쓰려고 한 거. 지금은 안 씀*
    public int getQuiz() { return cur_quiz; }

    public void setAct(Activity act)
    {
        cur_act = act;
    }
    public Activity getAct()
    {
        return cur_act;
    } // 튜토리얼 장 선택 페이지 닫기 위해 불러옴*

    public void setActSub(Activity act_sub)
    {
        cur_act_sub = act_sub;
    }
    public Activity getActSub()
    {
        return cur_act_sub;
    } // 튜토리얼 소단원 선택 페이지 닫기 위해 불러옴*

    public static void setClear_check(int check){
        clear_check = check;
    } // 고른 답 저장
    public int getClear_check(){
        return clear_check;
    } // 답 불러와서 정답과 비교하는데 씀

    public void setPageNum(int num){
        pageNum = num;
    }
    public int getPageNum(){
        return pageNum;
    }

    public void setPlusPageNum(int num){
        pageNum += num;
    }
    public void setMinusPageNum(int num){
        pageNum -= num;
    }

    public List getList(){
        return list;
    }

    public static TutorialEventStoring getInstance(){
        if(instance == null){
            instance = new TutorialEventStoring();
        }
        return instance;
    }

    private boolean event_option = false; // 튜토리얼을 통해 볼 경우 false, 아닌 경우는 setTrue()로 바꾸고 시작. *

    public void setTutorialEvent(boolean event) // 환경설정에서 보면 보기만 하고 끝내게 하기 위함.
    {
        event_option = event;
    }

    public boolean getTutorialEvent()
    {
        return event_option;
    } // 이벤트 옵션 반환

    private int ch_sub_maxPage; // 현재 소단원의 최대 페이지
    public void setCh_sub_maxPage(int sub_max)
    {
        ch_sub_maxPage = sub_max;
    }
    public int getCh_sub_maxPage()
    {
        return ch_sub_maxPage;
    }

    private int ch_sub_layout; // 현재 소단원의 시작 레이아웃

    public void setCh_sub_layout(int layout)
    {
        ch_sub_layout = layout;
    }
    public int getCh_sub_layout()
    {
        return ch_sub_layout;
    }

    private TutorialStatus status = TutorialStatus.getInstance(); // 싱글톤으로 하나만 쓰게 됨. 애당초 TutorialEventStoring 클래스가 싱글톤이라 무의미하겠지만.
    public TutorialStatus getStatus()
    {
        return status;
    }

    private boolean answer; // OX 퀴즈에서 선택한 정답 저장하는 변수

    public void setAnswer(boolean answer)
    {
        this.answer = answer;
    }

    public boolean getAnswer()
    {
        return answer;
    }

    public void resetActSub() // 선택한 소단원 액티비티 초기화
    {
        cur_act_sub = null;
    }

    public void resetAct() // 선택한 장 액티비티 초기화
    {
        cur_act = null;
    }

    public void resetAnswer() // 선택한 정답 초기화
    {
        answer = false;
    }

    public void resetStatus() // 선택을 모두 리셋. 튜토리얼 선택할 때마다 갱신되기 때문에 굳이 리셋할 필요는 없긴 한데...
    {
        status.reset();
    }
}
