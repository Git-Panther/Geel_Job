package com.example.m_user.geel_job.GamePlay.Event;

import com.example.m_user.geel_job.GamePlay.Game_Status;
import com.example.m_user.geel_job.R;

/**
 * Created by They on 2017-11-02.
 */

public class GameEventStatus { // 여기에 게임하기 이벤트 상태 저장

    private static GameEventStatus instance; // SingleTone

    private GameEventStatus()
    {

    }

    public static GameEventStatus getInstance()
    {
        if(instance == null)
            instance = new GameEventStatus();

        return instance;
    }

    private int currentPage = Game_Status.MIN_PAGE; // 게임하기 이벤트 현재 페이지 저장용.

    public GameEventStatus setPage(int page, boolean dimen) // true면 +, false면 -
    {
        if(dimen)
        {
            currentPage+=page;
        }
        else
        {
            currentPage-=page;
        }

        return this;
    }

    public int getPage()
    {
        return currentPage;
    }

    public GameEventStatus resetPage() // 게임 이벤트 페이지를 1페이지로 초기화
    {
        currentPage = Game_Status.MIN_PAGE;

        return this;
    }

    private int maxPage = Game_Status.GAME_EVENT_MAX_PAGE; // 게임하기 이벤트 최대 페이지

    public int getMaxPage()
    {
        return maxPage;
    }

    private int startLayout = R.drawable.game_event01; // 시작 레이아웃 저장

    public int getStartLayout()
    {
        return startLayout;
    }

    private boolean event_option = false; // 게임하기 버튼이나 1-3 이후에 볼 경우 false, 아닌 경우는 setTrue()로 바꾸고 시작. *

    public void setGameEvent(boolean event) // 환경설정에서 보면 보기만 하고 끝내게 하기 위함.
    {
        event_option = event;
    }

    public boolean getGameEvent()
    {
        return event_option;
    } // 이벤트 옵션 반환

    private boolean[] selectedAnswers = new boolean[3]; //{ false, false, false }; // 게임하기 이벤트에서 고른 정답이 옳은 지 안 옳은지 설정. 초기값은 false.

    public GameEventStatus resetAnswers() // 고른 답 초기화.
    {
        for(int i = 0; i < selectedAnswers.length; i++)
            selectedAnswers[i] = false;

        return this;
    }

    public GameEventStatus setAnswer(int index, boolean answer) // 정답 설정
    {
        if(index >= 0 && index <= 2)
            selectedAnswers[index] = answer;

        return this;
    }

    public boolean getAnswer(int index) // 선택된 정답 불러오기
    {
        if(index >= 0 && index <= 2)
            return selectedAnswers[index];

        return false;
    }

	private boolean check; // 이벤트에서 정답인가 오답인가 그 여부

	public GameEventStatus setCheck() // 최종적으로 정답인가 아닌가 설정하는 메소드. 오답이 하나라도 발견되면 오답 처리하고 끝.
	{
		for(int i = 0; i < selectedAnswers.length; i++)
		{
			if(!selectedAnswers[i])
			{
				check = false;
				return this;
			}
		}
		
		check = true;
		return this;
	}

	public boolean getCheck() // 정답/오답 여부 호출
	{
		return check;
	}
}