package com.example.m_user.geel_job.GamePlay.Storage;

import android.view.View;

import com.example.m_user.geel_job.R;

/**
 * Created by They on 2017-11-09.
 */

public class BoolStorage extends Storage{
    private int title;
    // 1. 미니게임의 이름(코드 번호)

    public BoolStorage setTitle(int name)
    {
        title = name;
        return this;
    }

    public int getTitle()
    {
        return title;
    }

    private int typeResource;
    // 2. 해당 종목의 종류에 따라 타이틀 이미지 id가 다르다.

    public BoolStorage setTypeResource(int type)
    {
        typeResource = type;
        return this;
    }

    public int getTypeResource()
    {
        return typeResource;
    }

    private int[] quizImage = new int[3];
    // 3. 문제로 표시할 이미지의 배열. 1~3번 순(0~2)

    public BoolStorage setQuizImage(int firstImage)
    {
        for(int i = 0; i < quizImage.length; i++)
            quizImage[i] = firstImage + i;

        return this;
    }

    public int[] getQuizImage()
    {
        return quizImage;
    }

    private boolean answers[] = new boolean[3];
    // 4. 각 고개별로 정답이 무엇인가 저장하는 변수. 순서대로 저장.

    public BoolStorage setAnswers(boolean[] b)
    {
        for(int i = 0; i < answers.length; i++)
            answers[i] = b[i];

        return this;
    }

    public boolean[] getAnswers()
    {
        return answers;
    }

    private int quizTitle;
    // 5. 조건문 고개 메인 타이틀에 해당하는 이미지 리소스

    public BoolStorage setQuizTitle(int image)
    {
        quizTitle = image;
        return this;
    }

    public int getQuizTitle()
    {
        return quizTitle;
    }
}
