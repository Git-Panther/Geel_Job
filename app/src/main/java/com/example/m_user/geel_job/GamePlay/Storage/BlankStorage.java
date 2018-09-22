package com.example.m_user.geel_job.GamePlay.Storage;

import android.view.View;
import android.widget.ImageView;

import com.example.m_user.geel_job.GamePlay.GameChecker;
import com.example.m_user.geel_job.R;

/**
 * Created by They on 2017-11-09.
 */

public class BlankStorage extends Storage{ // 빈칸 채우기 정보 저장 클래스. 1문제마다 저장.

    private int title;
    // 1. 미니게임의 이름(코드 번호)

    public BlankStorage setTitle(int name)
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

    public BlankStorage setTypeResource(int type)
    {
        typeResource = type;
        return this;
    }

    public int getTypeResource()
    {
        return typeResource;
    }

    private int objectResource[]; // = {0, 0, 0};
    // 3. 오브젝트 이미지 개수. 이미지 id가 들어간다. 세로 순이다. 빈칸 채우기 블록 베이스는 빈칸까지 설정해줌.

    private int objectAmount;
    // 4. 보여주는 오브젝트 개수 저장할 변수

    public BlankStorage setObjectResource(int amount, int array[], int firstImage)
    {
        objectAmount = amount;
        objectResource = array;
        int j = 0; // 이미지 리소스 개수 범위 내에서 인덱스 지정

        for(int i = 0; i < objectResource.length; i++) {
            if(blanks[i])
            {
                objectResource[i] = R.drawable.blank_blank;
            }
            else {
                objectResource[i] = firstImage + j; // 오브젝트 이미지 이름이 순차적이면 i만큼 더하여 추가 가능.
                j++; // 오브젝트를 추가했으므로 다음으로 넘기기 위해 증가.
            }
        }

        return this;
    }

    public int getObjectAmount()
    {
        return objectAmount;
    }

    public int getObjectResourceAt(int index)
    {
        return objectResource[index];
    }

    public int[] getObjectResource()
    {
        return objectResource;
    }

    private boolean blanks[] = {false, false, false, false, false, false, false, false, false, false};
    // 5. 채워야 할 빈칸을 좌표별로 지정. 빈칸이면 true, 아니면 false. 이 값을 통해 이미지를 빈칸으로 할 것이냐, 아니면 그대로 할 것이냐 결정. 세로 순. 즉 1열부터 행 하나씩 센다.
    // 3-5로 인해 10개로 늘림. 하지만 동작은 정상적일 것.

    public BlankStorage setBlankAt(int index)
    {
        blanks[index] = true;
        return this;
    }

    public boolean[] getBlanks()
    {
        return blanks;
    }

    private int answerInfo[];// = {0, 0, 0};
    // 6. 정답의 정보를 가지고 있는 배열. 이미지 id 혹은 xml의 id, 또는 정답 배열의 순서. 역시 세로부터 센다.
    // 주의할 점 : answerInfo[]의 인덱스는 그 문제의 빈칸 인덱스(역시 세로순)와 같으므로, 그 빈칸에 맞는 정답 번호를 순서대로 해줘야 한다.
    // 순서를 막 정해서 넣으면 꼬이니 주의.
    private int answerAmount;
    // 7. 정답 개수 저장용.

    public BlankStorage setAnswerInfo(int amount, int indexInfo[])
    {
        answerAmount = amount;
        /*
        for(int i = 0; i < amount; i++)
            answerInfo[i] = indexInfo[i];
            */
        answerInfo = indexInfo;

        return this;
    }

    public int[] getAnswerInfo()
    {
        return answerInfo;
    }

    public int getAnswerAmount()
    {
        return answerAmount;
    }

    private int layoutId;
    // 8. 배경으로 사용할 레이아웃 id를 저장할 변수. 이미지 말고 xml.

    public BlankStorage setLayoutId(int id)
    {
        layoutId = id;
        return this;
    }

    public int getLayoutId()
    {
        return layoutId;
    }

    private int answerImage[] = new int[6];
    // 9. 정답 선택 항목으로 쓸 이미지 리소스를 저장할 배열.

    public BlankStorage setAnswerImage(int firstImage)
    {
        for(int i = 0; i < answerImage.length; i++)
            answerImage[i] = firstImage + i; // 첫 이미지 이후로 순서대로 이름이 정해졌다면, i만큼 더해서 저장 가능. 세로 순으로 저장함에 주의.

        return this;
    }

    public int[] getAnswerImage()
    {
        return answerImage;
    }

    private int visiblity[] = {View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE};

    // 10. 문제로 제시되는 항목 중에서 보여줘야 하냐 말아야 하냐를 저장.

    public BlankStorage setInvisibleAt(int index) // 설정하는 값은 가리게 된다. 세로 순으로 저장.
    {
        visiblity[index] = View.INVISIBLE;
        return this;
    }

    public int[] getVisiblity()
    {
        return visiblity;
    }

    private int eventLinear[]; // = new int[3];
    // 11. 드래그앤드롭 이벤트를 부여해야 할 리니어(빈칸)의 번호를 저장하는 배열.  세로순으로 지정.

    public BlankStorage setEventLinear(int[] linearIndex)
    {
        eventLinear = linearIndex;
        return this;
    }

    public int[] getEventLinear()
    {
        return eventLinear;
    }

    private int subtitle;
    // 12. 정확히 무슨 종류인가 지정하는 변수.

    public BlankStorage setSubtitle(int sub)
    {
        subtitle = sub;
        return this;
    }

    public int getSubtitle()
    {
        return subtitle;
    }
}
