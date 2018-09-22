package com.example.m_user.geel_job.GamePlay;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.example.m_user.geel_job.GamePlay.Ranking.Game_Last_input;

/**
 * Created by They on 2017-11-09.
 */

public class GameDynamicStatus { // 게임하기 진행상황을 실시간으로 적용.

    private static GameDynamicStatus instance; // singletone

    private GameDynamicStatus() {}

    public static GameDynamicStatus getInstance()
    {
        if(instance == null)
            instance = new GameDynamicStatus();

        return instance;
    }

    private GameChecker checker = GameChecker.getInstance();
    // 1. 게임 정답인지 오답인지 체크해주는 클래스. Single Tone.

    public GameChecker getChecker() // 정답 체크를 쉽게 반환하기 위해 만듬.
    {
        return checker;
    }

    private int gameScore = 0;
    // 2. 현재 진행중은 1회의 게임에 대한 점수.

    public boolean resultGameScore(boolean check) // 채점 여부에 따라 점수 합산.
    {
        int score; // 더하거나 뺄 점수.

        if(check) // 정답 맞추면
        {
            score = Game_Status.GAME_CLEAR_BONUS;
        }
        else // 틀렸으면
        {
            setFailCount(); // 오답 카운트 증가.
            score = Game_Status.GAME_FAIL_PENALTY;
        }

        switch (choice.getIntentStyleAt(stage)) // 점수 분배를 위한 보정
        {
            case Game_Status.GAME_BOOL:
                score = score / Game_Status.GAME_SCORE_DIVIDING_PERCENT; // 3분의 1로 줄임.
                break;
            case Game_Status.GAME_FIX:
                if(score < 0) // 감점일 경우
                    score = score / Game_Status.GAME_SCORE_DIVIDING_PERCENT; // 3분의 1로 줄임.
                break;
            case Game_Status.GAME_BLANK:
                break;
            case Game_Status.GAME_ORDER:
                break;
            default: // 오류났을 때
                Log.d("점수 계산 실패", "어느 타입의 미니게임인지 알 수 없습니다.");
                score = -1000000;
                break;
        }

        setGameScore(score); // 점수 반영
        return check; // check가 이미 boolean
    }

    private void setGameScore(int score) // 점수를 더하거나 빼는 기능.
    {
        gameScore += score;
        if(gameScore < 0) // 점수가 0점보다 낮아지면 0점으로 초기화하기 위한 장치.
            gameScore = 0;
    }

    public void setResultScore() // 제한 시간 이내로 전부 깨면 시간 가산점 추가.
    {
        int bonus = Game_Status.GAME_TIME_BONUS - failCount;
        if(bonus < 0) // 다 깼을 때 보너스 계수가 - 이면
            bonus = 0;

        setGameScore((time)* bonus);

    }

    public int getGameScore() // 현재 점수를 반환하여 표시하는데 쓴다.
    {
        return gameScore;
    }

    private int time = Game_Status.GAME_TIME;
    // 3. 게임할 수 있는 시간. 초 단위로 int에 넣음.

    public void setTime(int time) // 최종 시간 저장.
    {
        this.time = time;
    }

    public int getTime() // 현재 시간을 반환하여 시간을 표시.
    {
        return time;
    }

    private int stage = -1;
    // 4. 현재 진행중인 스테이지 번호. 주의할 점은 배열의 인덱스에 맞췄기 때문에 실제로는 스테이지 넘버 - 1의 값이 저장됨.

    public int getStage()
    {
        return stage;
    }

    public void nextStage(Activity activity) // 종료할 activity를 받아 페이지를 넘김.
    {
        stage++;
        if(stage == Game_Status.GAME_MAX_STAGE) // 10 라운드를 넘겼으면. 즉, 0 ~ 9인데 9보다 큰 10이 되었을 때. 이게 호출되었다는 건 결국 제 시간 이내로 끝냈다는 얘기니 가산치를 주어야 함.
        {
            status = Game_Status.GAME_END; // 게임을 끝냈으므로.
            setResultScore(); // 가산치 추가.
            activity.startActivity(new Intent(activity.getApplicationContext(), Game_Last_input.class));
        }
        else
        {
            Intent intent = new Intent(activity.getApplicationContext(),GameStagePreview.class);
            activity.startActivity(intent); // 이어서 시작.
        }
        if(stage != 0) // 게임하기 선택 페이지 안 닫히게 설정.
            activity.finish(); // 기존 액티비티 종료.
    }

    private int failCount = 0;
    // 5. 문제 틀린 횟수

    public void setFailCount()
    {
        failCount++;
    }

    private GameChoice choice = GameChoice.getInstance();
    // 6. 싱글톤 게임 선택 객체 불러오기

    public GameChoice getChoice() // 랜덤 게임 객체 불러오기
    {
        return choice;
    }

    private int selectedChapter;
    // 7. 선택된 장 저장용.

    public void setSelectedChapter(int ch)
    {
        selectedChapter = ch;
    }

    private String user_name;
    // 8. 랭킹 등록시 유저의 이름을 받을 변수.

    public void setUser_name(String name)
    {
        user_name = name;
    }

    public String getUser_name()
    {
        return user_name;
    }

    private int status = 0; // 현재 상태를 의미. 성공, 실패, 시간 초과, 완전 클리어 중 하나.

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getStatus()
    {
        return status;
    }

    public void startGame() // 게임 시작할 때의 세팅
    {
        choice.startRandomGame(selectedChapter);
    }

    public void setResult(Activity activity) // CLEAR, FAIL, TIMEOVER 중 하나를 출력
    {
        Intent intent;

        switch (status)
        {
            case Game_Status.GAME_CLEAR:
                checker.resetAnswer();
                intent = new Intent(activity.getApplicationContext(), GameResult.class);
                activity.startActivity(intent);
                activity.finish();
                break;
            case Game_Status.GAME_FAIL:
                checker.resetAnswer();
                intent = new Intent(activity.getApplicationContext(), GameResult.class);
                activity.startActivity(intent);
                activity.finish();
                break;
            case Game_Status.GAME_TIMEOVER:
                checker.resetAnswer();
                intent = new Intent(activity.getApplicationContext(), GameResult.class);
                activity.startActivity(intent);
                activity.finish();
                break;
            case Game_Status.GAME_END:
                checker.resetAnswer();
                intent = new Intent(activity.getApplicationContext(), GameResult.class);
                activity.startActivity(intent);
                activity.finish();
                break;
            default:
                break;
        }
    }

    public void resetGame() // 게임하기 1회가 끝나면 모든 정보 초기화. 랭킹 창 끝나기 직전에 실행. 다이얼로그로 강종해도 적용.
    {
        gameScore = 0;
        stage = -1;
        failCount = 0;
        time = Game_Status.GAME_TIME;
        selectedChapter = 0;
        status = 0;

        choice.resetChoice();
        checker.resetAnswer();

        user_name = "";
    }
}
