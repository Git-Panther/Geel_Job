package com.example.m_user.geel_job.Tutorial.Storage;

/**
 * Created by They on 2017-10-23.
 */

public class Quiz {// 퀴즈 저장용
    private int quizName; // 퀴즈명 저장
    private boolean buttonO; // 0 정답 여부 저장
    private boolean buttonX; // X 정답 여부 저장
    private int hint; // 힌트 이미지 저장

    private int quiz_layout; // 퀴즈에 쓸 이미지 저장

    private int failed_layout; // 오답시 나오는 첫 페이지에 쓸 이미지 저장

    public Quiz setQuizName(int quizName)
    {
        this.quizName = quizName;
        return this;
    }

    public int getQuizName()
    {
        return quizName;
    }

    public Quiz setQuizButton(boolean buttonO, boolean buttonX) // 퀴즈 정답 설정. true면 정답, 아니면 오답
    {
        this.buttonO = buttonO;
        this.buttonX = buttonX;

        return this;
    }

    public boolean getQuizButtonO()
    {
        return buttonO;
    }

    public boolean getQuizButtonX()
    {
        return buttonX;
    }

    public Quiz setHint(int image)
    {
        hint = image;
        return this;
    }

    public int getHint() { return hint; }

    public Quiz setQuiz_layout(int image)
    {
        quiz_layout = image;
        return this;
    }

    public int getQuiz_layout()
    {
        return quiz_layout;
    }

    public Quiz setFailed_layout(int image)
    {
        failed_layout = image;
        return this;
    }

    public int getFailed_layout()
    {
        return failed_layout;
    }
}
