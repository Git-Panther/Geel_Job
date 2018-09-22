package com.example.m_user.geel_job.GamePlay.Storage;

import android.view.View;

import com.example.m_user.geel_job.R;

/**
 * Created by They on 2017-11-09.
 */

public class OrderStorage extends Storage{

    private int title;
    // 1. 미니게임의 이름(코드 번호)

    public OrderStorage setTitle(int name)
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

    public OrderStorage setTypeResource(int type)
    {
        typeResource = type;
        return this;
    }

    public int getTypeResource()
    {
        return typeResource;
    }

    private int orderPiece[] = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // = {0, 0, 0};
    // 3. 순서 맞추기에서 사용해야 할 순서 빈칸에서 어느 빈칸에 항목이 들어가 있냐를 표시하는 배열.
    // 최대 10개이므로 10개로 잡음. 0을 걸러내기 위해 0으로 초기화. 문제 창에 있는 것만 해당. drawable id를 저장. 0인 부분은 리니어에서 제외.
    // 주의 : 세로 순으로 셈을 주의.
    // 참고로 이미지 순서는 빈칸 쪽의 세로열 순 ~ 정답 쪽의 세로열 순으로 갱신. 따라서 이미지 순서와 실제 순서는 불일치할 수 있음.

    private int orderAmount;
    // 4. 보여주는 순서 개수를 저장할 변수. 5~10.

    public OrderStorage setOrderAmount(int amount)
    {
        orderAmount = amount;
        return this;
    }

    public int getOrderAmount()
    {
        return orderAmount;
    }

    public OrderStorage setOrderPieceAt(int index, int image) // 순서 자리에 0이 아닌 이미지 값을 줌으로써 검사할 때 해당 이미지 값으로 해당 인덱스 순서의 이미지를 정함.
    {
        if(index < orderAmount && index >= 0)
        {
            orderPiece[index] = image;
        }

        return this;
    }

    public int[] getOrderPiece()
    {
        return orderPiece;
    }

    public int getOrderPieceAt(int index)
    {
        return orderPiece[index];
    }

    /*

    public OrderStorage setDesign(ImageView[] images, ImageView[] blanks, LinearLayout[] zone) 이미지 세팅 임시용
    {
        for(int i = 0; i < orderPiece.length; i++)
        {
            if(orderPiece[i] == 0)
            {
                images[i].getParent().removeAllView();
            }
            else
            {
                images.setImageResource(orderPiece[i]);
            }

            if(i > orderAmount - 1)
            {
                blanks[i].setVisiblity(View.INVISIBLE);
                zone[i].setOnDragListener(ETCOnDragListener.getInstance());
             }
             else
             {
                zone[i].setOnDragListener(DnDOrderListener.getInstance());
             }
        }
    }
     */

    private boolean blanks[] = new boolean[]{false, false, false, false, false, false, false, false, false, false};
    // 5. 채워야 할 빈칸을 좌표별로 지정. 빈칸이면 true, 아니면 false. 1열부터 센다. 빈칸으로 안 쓰고 가릴 거면 false.
    // 순서 맞추기에서는 채워야 할 빈칸이면 보이면서 동시에 DND 이벤트 추가. 아니면 가림과 동시에 ETC 이벤트 추가.
    // 채워야 할 빈칸 개수를 넘어선 빈칸들은 그 안의 객체를 지우면서도 동시에 빈칸 가리고 ETC 이벤트 추가.
    // 추가사항 : 10개 빈칸 레이아웃 쓸 경우, 6개 쓸때는 3, 4, 8, 9번째 인덱스의 빈칸 객체는 사용 불가. 8개일 경우도. -> 취소

    public OrderStorage setBlank() // blank[i]를 false로 만들어 해당 빈칸을 시작과 동시에 지우고 ETC 이벤트 추가.
            // 변경 사항 : 이제부터 0 ~ 빈칸 개수 - 1로 함. 그만큼 xml을 새로 개설했으니 안심할 것(빈칸 채우기 새로 개설한 것에서 착안)
    {
        for(int i = 0; i < orderAmount; i++)
        {
            blanks[i] = true;
        }

        return this;
    }

    public boolean[] getBlanks()
    {
        return blanks;
    }

    private int answerInfo[]; // = new int[]{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    // 6. 정답의 정보를 가지고 있는 배열. 이미지뷰의 id(메인 클래스에서든 여기에서든 가져오는 순서는 일치할 것). 역시 세로부터 센다.
    // 주의할 점 : answerInfo[]의 인덱스는 그 문제의 빈칸 인덱스(역시 세로순)와 같으므로, 그 빈칸에 맞는 정답 번호를 순서대로 해줘야 한다.
    // 순서를 막 정해서 넣으면 꼬이니 주의.
    private int answerAmount;
    // 7. 정답 개수 저장용.

    public OrderStorage setAnswerInfo(int amount, int indexInfo[])
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

    public OrderStorage setLayoutId(int id)
    {
        layoutId = id;
        return this;
    }

    public int getLayoutId()
    {
        return layoutId;
    }

    private int answerImage[] = new int[4];
    // 9. 정답 선택 항목으로 쓸 이미지 리소스를 저장할 배열.

    public OrderStorage setAnswerImage(int firstImage)
    {
        for(int i = 0; i < answerImage.length; i++)
            answerImage[i] = firstImage + i; // 첫 이미지 이후로 순서대로 이름이 정해졌다면, i만큼 더해서 저장 가능. 세로 순으로 저장함에 주의.

        return this;
    }

    public int[] getAnswerImage()
    {
        return answerImage;
    }

    /*

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

    */

    private int subtitle;
    // 12. 정확히 무슨 종류인가 지정하는 변수.

    public OrderStorage setSubtitle(int sub)
    {
        subtitle = sub;
        return this;
    }

    public int getSubtitle()
    {
        return subtitle;
    }

    private int orderTitle;
    // 13. 순서 맞추기의 서브 타이틀. 이미지 id 저장

    public OrderStorage setOrderTitle(int image)
    {
        orderTitle = image;
        return this;
    }

    public int getOrderTitle()
    {
        return orderTitle;
    }

    private int[] realAnswer;
    // 정답 순대로 들어가야 할 이미지.

    public OrderStorage setRealAnswer(int[] images)
    {
        realAnswer = images;

        return this;
    }

    public int[] getRealAnswer()
    {
        return realAnswer;
    }

    private int[] startingAnswer; // 14. 순서 맞추기 시작할 때 초기 고른 정답을 설정. 이는 시작할 때부터 올바른 위치에 있는 빈칸들을 위한 준비.

    public OrderStorage setStartingAnswer() // 순서의 객체 id 중 정답 위치에 있는 이미지뷰들은 정답 처리하고 시작하게끔 하기 위함.
    {
        startingAnswer = new int[orderAmount]; // 순서 개수만큼 생성.

        for(int x = 0; x < startingAnswer.length; x++)
        {
            startingAnswer[x] = R.id.order_object_01 + x; // x는 0부터 시작하므로 1 ~ 순서 개수까지 id 저장. 참고로 정답으로 지정한 것 이외의 것이 들어오면 오답 처리이므로 order_object들을 넣어도 문제 없음.
        }

        return this;
    }

    public int[] getStartingAnswer()
    {
        return  startingAnswer;
    }
}
