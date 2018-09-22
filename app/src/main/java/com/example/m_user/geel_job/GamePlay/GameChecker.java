package com.example.m_user.geel_job.GamePlay;

import android.util.Log;

import com.example.m_user.geel_job.GamePlay.Storage.BlankStorage;
import com.example.m_user.geel_job.GamePlay.Storage.BoolStorage;
import com.example.m_user.geel_job.GamePlay.Storage.FixStorage;
import com.example.m_user.geel_job.GamePlay.Storage.OrderStorage;
import com.example.m_user.geel_job.GamePlay.Storage.Storage;

/**
 * Created by They on 2017-11-09.
 */

public class GameChecker { // 정답 채점하는 클래스. 모든 유형에 대해 전부 대응.

    private static GameChecker instance; // singletone

    private GameChecker() {}

    public static GameChecker getInstance()
    {
        if (instance == null)
            instance = new GameChecker();

        return instance;
    }

    private int[] answersOfBlank; // 선택한 정답 배열 : 빈칸 채우기
    private boolean[] answersOfBool; // 선택한 정답 배열 : 조건문 고개
    private int[] answersOfOrder; // 선택한 정답 배열 : 순서 맞추기
    private boolean[] answersOfFix; // 선택한 정답 배열 : 틀린 부분 찾기

    public void setAnswers(int style, int amount) // 정답 초기 설정. 스테이지 페이지 넘긴 직후 해당 클래스 시작하자마자 호출할 것. 종류에 따라 다른 답을 사용. amount가 0이면 조건문 고개.
    {
        switch (style)
        {
            case Game_Status.GAME_BLANK:
                setAnswersOfBlank(amount);
                break;
            case Game_Status.GAME_BOOL:
                setAnswersOfBool();
                break;
            case Game_Status.GAME_ORDER:
                setAnswersOfOrder(amount);
                break;
            case Game_Status.GAME_FIX:
                setAnswersOfFix(amount);
                break;
            default: // 오류났을 때
                break;
        }
    }

    public void setAnswersOfBlank(int amount) // 정답 초기 설정 : 빈칸 채우기
    {
        answersOfBlank = new int[amount];
        for (int i = 0; i < answersOfBlank.length; i++) // 정답 여부를 -1로 초기화. 정답 항목의 배열 인덱스를 저장하기 때문.
        {
            answersOfBlank[i] = -1;
        }
    }

    public void setAnswersOfBlankAt(int index, int place) // 정답 반영 : 빈칸 채우기
    {
        answersOfBlank[index] = place; // 빈칸 인덱스와 같은 인덱스 위치에 정답 항목 배열의 인덱스 저장. 예 : answers[0](1) == answersOfBlank[0](2) ? button index : index
    }

    public int[] getAnswersOfBlank()
    {
        return answersOfBlank;
    }

    public void setAnswersOfBool() // 정답 초기 설정 : 조건문 고개
    {
        answersOfBool = new boolean[3];
        for (int i = 0; i < answersOfBool.length; i++) // 정답 여부를 false로 초기화. 여기서 true, false는 조건문 고개에서 O, X를 택했냐를 의미.
        {
            answersOfBool[i] = false;
        }
    }

    public void setAnswersOfBoolAt(int index, boolean answer) // 정답 반영 : 조건문 고개
    {
        if(index < answersOfBool.length) {
            answersOfBool[index] = answer; // O, X를 눌렀냐를 저장. 예 : answers[0](true) == answersOfBool[0](false) ? true : false
            Log.d("반영 성공", "인덱스 범위 안입니다.");
        }
        else
            Log.d("인덱스 에러", "인덱스가 정답 최대 길이보다 커졌습니다. 조건문 고개의 정답 개수는 3개입니다.");
    }

    public void setAnswersOfOrder(int amount) // 정답 초기 설정 : 빈칸 채우기
    {
        answersOfOrder = new int[amount];
        for (int i = 0; i < answersOfOrder.length; i++) // 정답 여부를 0으로 초기화. 무슨 이미지뷰(id로)인지 검사함.
        {
            answersOfOrder[i] = 0;
        }
    }

    public void setAnswersOfOrderAt(int index, int id) // 정답 반영 : 순서 맞추기
    {
        answersOfOrder[index] = id; // 빈칸 인덱스와 같은 인덱스 위치에 정답 항목 배열의 인덱스 저장. 예 : answers[0](R.id.~~) == answersOfOrder[0](R.id.~~) ? ViewParent.getChildAt(0) : R.id.~~~
    }

    public void setStartingAnswerOfOrder(OrderStorage storage) // 정답 초기 설정 : 순서 맞추기
    {
        for(int x = 0; x < storage.getOrderAmount(); x++)
        {
            answersOfOrder[x] = storage.getStartingAnswer()[x];
        }
    }

    public int[] getAnswersOfOrder()
    {
        return answersOfOrder;
    }

    public void setAnswersOfFix(int amount) // 정답 초기 설정 : 틀린 부분 찾기
    {
        answersOfFix = new boolean[amount];
        for (int i = 0; i < answersOfFix.length; i++) // 정답 여부를 false로 초기화. 여기서 true, false는 단순히 클릭 여부를 의미.
        {
            answersOfFix[i] = false;
        }
    }

    public void setAnswersOfFixAt(int index, boolean click) // 정답 반영 : 조건문 고개
    {
        answersOfFix[index] = click; // 눌린 상태인지 아닌지를 저장. 예 : answers[0](false) == answersOfFix[0](true) ? 안 눌림 : 눌림
    }

    public boolean getAnswersOfFixAt(int index)
    {
        return answersOfFix[index]; // 눌린 상태면 true, 안 눌렀으면 false.
    }

    public boolean[] getAnswersOfFix()
    {
        return answersOfFix;
    }

    public boolean checkAnswer(int style, Storage storage) // 문제 스타일과 참조 중인 저장소를 인자로 보냄. 저장소는 정답 참고를 위한 것.
    {
        boolean answerState; // 정답 맞췄으면 true, 아니면 false

        switch (style) // 점수 분배를 위한 보정
        {
            case Game_Status.GAME_BLANK:
                answerState = checkBlank((BlankStorage)storage);
                break;
            case Game_Status.GAME_BOOL:
                answerState = false;
                break;
            case Game_Status.GAME_ORDER:
                answerState = checkOrder((OrderStorage)storage);
                break;
            case Game_Status.GAME_FIX:
                answerState = checkFix((FixStorage)storage);
                break;
            default: // 오류났을 때
                answerState = false;
        }

        return answerState;
    }

    public boolean checkBlank(BlankStorage storage) // 빈칸 채우기 채점.
    {
        for(int i = 0; i < answersOfBlank.length; i++)
        {
            if(storage.getAnswerInfo()[i] != answersOfBlank[i]) // 만일 하나라도 다르면 바로 오답 처리.
            {
                return false;
            }
        }
        return true;
    }

    public boolean checkBool(BoolStorage storage, int subStage) // 조건문 고개 채점. 빈칸 채우기만의 리스너를 통해 호출할 것. 추가로 결과 이벤트 말고 스테이지 이벤트로 바로 넘어갈 것.
    {
        if(subStage < storage.getAnswers().length && subStage < answersOfBool.length)
        {
            Log.d("채점 시작", "인덱스 범위 안입니다. 정상적으로 채점합니다.");
            if (storage.getAnswers()[subStage] != answersOfBool[subStage]) // 고른 정답과 다르면 false 처리.
            {
                return false;
            } else
                return true;
        }
        else
        {
            Log.d("채점 불가", "인덱스 범위 밖입니다. 오답 처리하겠습니다.");
            return false;
        }
    }

    public boolean checkOrder(OrderStorage storage)
    {
        for(int i = 0; i < answersOfOrder.length; i++)
        {
            if(storage.getAnswerInfo()[i] != answersOfOrder[i]) // 만일 하나라도 다르면 바로 오답 처리.
            {
                return false;
            }
        }
        return true;
    }

    public boolean checkFix(FixStorage storage) // 틀린 부분 찾기. 조건문 고개와 동일하게 독립적인 리스너 이벤트 만들 것. 그걸로 카운트하는 거 따져야 함.
    {
        for(int i = 0; i < answersOfFix.length; i++)
        {
            if(storage.getAnswers()[i] != answersOfFix[i]) // 만일 하나라도 다르면 바로 오답 처리.
            {
                return false;
            }
        }
        return true;
    }

    public void resetAnswer() // 게임 1회 선택할 때마다 고른 정답 초기화.
    {
        answersOfBlank = null;
        answersOfBool = null;
        answersOfOrder = null;
        answersOfFix = null;
    }
}
