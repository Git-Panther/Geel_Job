package com.example.m_user.geel_job.Tutorial.Storage;

import android.app.Activity;

import com.example.m_user.geel_job.Interface.CustomFontActivity;
import com.example.m_user.geel_job.Tutorial.Tutorial_ch_num;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by They on 2017-10-23.
 */

public class ChapterInfo extends CustomFontActivity { // 각 장별로 시작 페이지와 끝 페이지 등의 정보 저장. 번호 붙은 건 생성과 동시에 세팅해줘야 하는 것들
        // private ChapterInfo() {}

    /*
        public static ChapterInfo newChapterInfo() // 새 인스턴스 만들고 바로 이어서 설정 가능하도록 하기 위한 메소드(빌더 패턴)
        {
            ChapterInfo info = new ChapterInfo();
            return info;
        }
     */

        private int ch_number; // 1. 장 번호

        public ChapterInfo setCh_number(int num) {
            ch_number = num;
            return this;
        }

        public int getCh_number() {
            return ch_number;
        }

        private int ch_sub_number; // 2. 소단원 번호

        public ChapterInfo setCh_sub_number(int num) {
            ch_sub_number = num;
            return this;
        }

        public int getCh_sub_number() {
            return ch_sub_number;
        }

        private int start_page = Tutorial_ch_num.MIN_PAGE; // 일반적인 시작 페이지

        public int getStart_page() {
            return start_page;
        }

        private int max_page; // 3. 일반적인 끝 페이지

        public ChapterInfo setMax_page(int num) {
            max_page = num;
            return this;
        }

        public int getMax_page() {
            return max_page;
        }

        private int restart_page; // 4. 문제 틀리고 재시작하는 페이지

        public ChapterInfo setRestart_page(int num) {
            restart_page = num;
            return this;
        }

        public int getRestart_page() {
            return restart_page;
        }

        private int cur_page; // 시작 페이지 결정. 일반 시작이냐 재시작이냐에 따라 다름. 발생은 소단원 선택 또는 리플레이 버튼

        public ChapterInfo setCur_page(boolean event) { // 시작할 페이지 쪽수 저장. 참은 오답으로 인한 재시작, 거짓은 소단원 선택으로 인한 재시작
            if(event)
            {
                cur_page = restart_page;
            }
            else
            {
                cur_page = start_page;
            }

            return this;
        }

        public int getCur_page() {
            return cur_page;
        }

        private int start_layout; // 5. 시작하는 이미지

        public ChapterInfo setStart_layout(int image) {
            start_layout = image;
            return this;
        }

        public int getStart_layout() {
            return start_layout;
        }

        private int restart_layout; // 6. 재시작 이미지

        public ChapterInfo setRestart_layout(int image) {
            restart_layout = image;
            return this;
        }

        public int getRestart_layout() {
            return restart_layout;
        }

        private int cur_layout; // 시작 이미지 결정. 일반 시작이냐 재시작에 따라 다름. 발생은 소단원 버튼 또는 리플레이 버튼

        public ChapterInfo setCur_layout(boolean event) { // true면 재시작, 아니면 처음 시작
            if(event)
            {
                cur_layout = restart_layout;
            }
            else
            {
                cur_layout = start_layout;
            }

            return this;
        }

        public int getCur_layout()
        {
            return cur_layout;
        }

    private ArrayList<Quiz> quizlist = new ArrayList<Quiz>(); // 7. OX 퀴즈 저장용

    public ChapterInfo setQuizlist(Quiz quiz) {
        quizlist.add(quiz);

        return this;
    }

    /*
    public ArrayList<Quiz> getQuizlist() {
        return quizlist;
    }
    */

    private Random randomQuiz = new Random(); // 퀴즈 랜덤화를 위한 랜덤 클래스
    int quizNum; // 세 퀴즈 중 하나 무작위 선택
    Quiz selectedQuiz; // 선택된 OX퀴즈 클래스 저장

    public ChapterInfo selectTheQuiz() { // 퀴즈 랜덤하여 선택. 소단원 선택과 동시에 발생
        quizNum = randomQuiz.nextInt(quizlist.size()); // 무작위 퀴즈 번호 추출
        selectedQuiz = quizlist.get(quizNum); // 선택된 퀴즈 정보 저장
        // quizlist.clear(); // 퀴즈 선택 이후 초기화
        return this; // 자기 자신 객체 반환하여 getSelectedQuiz() 등 빌더 패턴 가능하도록 함.
    }

    public Quiz getSelectedQuiz()
    {
        return selectedQuiz;
    }

    private int failed_startPage = Tutorial_ch_num.MIN_PAGE; // 실패했을 때 시작 페이지. 하지만 사실상 1 고정.

    public int getFailed_startPage()
    {
        return failed_startPage;
    }

    private int failed_endPage; // 8. 실패시의 최종 페이지

    public ChapterInfo setFailed_endPage(int page)
    {
        failed_endPage = page;
        return this;
    }

    public int getFailed_endPage()
    {
        return failed_endPage;
    }

    private int replay_layout; // 9. 실패한 이후에 재생되는 리플레이 처음 페이지

    public ChapterInfo setReplay_layout(int image)
    {
        replay_layout = image;
        return this;
    }

    public int getReplay_layout()
    {
        return replay_layout;
    }

    private int clear_startPage = Tutorial_ch_num.MIN_PAGE; // 맞추면 시작되는 첫 페이지.

    public int getClear_startPage()
    {
        return clear_startPage;
    }

    private int clear_endPage; // 10. 맞춘 후 끝 페이지

    public ChapterInfo setClear_endPage(int page)
    {
        clear_endPage = page;
        return this;
    }

    public int getClear_endPage()
    {
        return clear_endPage;
    }

    private int clear_layout; // 11. 맞추면 시작되는 이미지

    public ChapterInfo setClear_layout(int image)
    {
        clear_layout = image;
        return this;
    }

    public int getClear_layout()
    {
        return clear_layout;
    }

    private int clear_endLayout; // 12. 정답 맞췄을 때의 이벤트 끝나고 엔딩 보여줄 때 그 시작하는 이미지

    public ChapterInfo setClear_endLayout(int image)
    {
        clear_endLayout = image;
        return this;
    }

    public int getClear_endLayout()
    {
        return clear_endLayout;
    }

    public void reset() // 리셋
    {
        setCur_page(false).setCur_layout(false);
        quizNum = -1;
        selectedQuiz = null;
    }
}
