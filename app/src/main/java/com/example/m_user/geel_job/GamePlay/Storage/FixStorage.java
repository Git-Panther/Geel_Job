package com.example.m_user.geel_job.GamePlay.Storage;

import android.view.View;

/**
 * Created by They on 2017-11-09.
 */

public class FixStorage extends Storage{

    /* 틀린 부분 찾기의 정보 저장용 클래스.
        틀린 부분 찾기는 매 xml마다 형태를 달리 해줘야 하는 문장형, 알고리즘형이 주를 이루기 때문에 정형화된 경우는 코드밖에 없음.
        문장형 : 3줄 ~ 7줄로 매우 다양.
        알고리즘형 : 알고리즘 틀이 고정된다면 모를까 그럴 가능성 전무
        순서형 : 순서별로 레이아웃을 나눠주면 될 듯.
        코드형 : 줄마다 스케일레이블 적용하면 조절 가능할 듯. 최대 13줄을 기준으로 xml 생성.
        1장은 알고리즘 1, 순서 1을 제외하고 전부 문장형
        2장은 알고리즘 1, 코드 1, 순서 1(문장으로 대체 가능), 나머지 문장
        3장은 전부 코드
     */

    private int title;
    // 1. 미니게임의 이름(코드 번호)

    public FixStorage setTitle(int name)
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

    public FixStorage setTypeResource(int type)
    {
        typeResource = type;
        return this;
    }

    public int getTypeResource()
    {
        return typeResource;
    }

    private int subtitle;
    // 3. 정확히 무슨 종류인가 지정하는 변수. 문장형, 알고리즘형, 순서형, 코드형 이렇게 네 가지.

    public FixStorage setSubtitle(int sub)
    {
        subtitle = sub;
        return this;
    }

    public int getSubtitle()
    {
        return subtitle;
    }

    private int fixTitle;
    // 4. 틀린 부분 찾기의 부제. 이미지 id 저장

    public FixStorage setFixTitle(int image)
    {
        fixTitle = image;
        return this;
    }

    public int getFixTitle()
    {
        return fixTitle;
    }

    private int fixBackground;
    // 5. 틀린 부분 찾기 중 배경 텍스트 이미지를 사용하는 경우 여기에 저장.

    public FixStorage setFixBackground(int image)
    {
        fixBackground = image;
        return this;
    }

    public int getFixBackground()
    {
        return fixBackground;
    }

    private int clickableObject;
    // 6. 클릭 가능한 선택지 개수 저장용.

    public FixStorage setClickableObject(int amount)
    {
        clickableObject = amount;
        return this;
    }

    public int getClickableObject()
    {
        return clickableObject;
    }

    private boolean answers[];
    // 7. 정답 저장용. 클릭 가능한 선택지 개수에 따라 배열 크기도 달라진다.

    public FixStorage setAnswers(boolean[] b)
    {
        if(b.length == clickableObject)
            answers = b;

        return this;
    }

    public boolean[] getAnswers()
    {
        return answers;
    }

    private int[] clickableImages;
    // 8. 클릭 가능한 이미지들에게 이미지 리소스를 부여하기 위한 배열.

    public FixStorage setClickableImages(int firstImage) {
        clickableImages = new int[clickableObject];

        for (int i = 0; i < clickableImages.length; i++)
            clickableImages[i] = firstImage + i;

        return this;
    }

    private int layoutId;
    // 9. 배경으로 사용할 레이아웃 id를 저장할 변수. 이미지 말고 xml.

    public FixStorage setLayoutId(int id)
    {
        layoutId = id;
        return this;
    }

    public int getLayoutId()
    {
        return layoutId;
    }

    private int resultDialog;
    // 10. 코드 한정으로 실행 결과가 있으면 그 다이얼로그의 이미지를 지정해준다.

    public FixStorage setDialog(int image)
    {
        resultDialog = image;
        return this;
    }

    public int getDialog()
    {
        return resultDialog;
    }
}
