package com.example.m_user.geel_job.GamePlay;

import android.util.Log;

import com.example.m_user.geel_job.GamePlay.Storage.BlankStorage;
import com.example.m_user.geel_job.GamePlay.Storage.BoolStorage;
import com.example.m_user.geel_job.GamePlay.Storage.FixStorage;
import com.example.m_user.geel_job.GamePlay.Storage.GameStorage;
import com.example.m_user.geel_job.GamePlay.Storage.OrderStorage;
import com.example.m_user.geel_job.GamePlay.Storage.Storage;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by They on 2017-11-13.
 */

public class GameChoice { // 게임하기 진행하면서 랜덤으로 선택하게 해주는 클래스

    private GameChoice() {}

    private static GameChoice instance; // singletone

    public static GameChoice getInstance()
    {
        if(instance == null)
            instance = new GameChoice();

        return instance;
    }

    private Random random = new Random();
    // 1. 랜덤 객체.

    private Storage[] selectedGames = new Storage[Game_Status.GAME_MAX_STAGE];
    // 2. 선택된 게임들의 정보가 들어감.

    public Storage getSelectedGamesAt(int index)
    {
        return selectedGames[index];
    }

    private int[] intentStyle = new int[Game_Status.GAME_MAX_STAGE];
    // 3. intent가 무슨 클래스를 써야 하는지 골라주는 배열.

    public int getIntentStyleAt(int index) // 인덱스에 따라 인텐트 스타일(기호 상수)을 반환. index는 stage를 의미.
    {
        if(index < intentStyle.length) {
            Log.d("인덱스 정상", "범위 안이므로 게임 스타일을 정상적으로 반환합니다.");
            return intentStyle[index];
        }
        else
        {
            Log.d("인덱스 초과", "-1을 반환합니다.");
            return -1;
        }
    }

    private GameStorage storage = GameStorage.getInstance();
    // 4. 게임 저장소. 여기에 있는 걸 빌려다 씀.

    private LinkedList<BlankStorage> refBlanks = new LinkedList<>();
    // 5. 빈칸 채우기 참조.

    private LinkedList<BoolStorage> refBools = new LinkedList<>();
    // 6. 조건문 고개 참조.

    private LinkedList<OrderStorage> refOrders = new LinkedList<>();
    // 7. 순서 맞추기 참조.

    private LinkedList<FixStorage> refFixes = new LinkedList<>();
    // 8. 틀린 부분 찾기 참조.

    private int gameType = 1000; // 1 ~ 4 중에 뽑을 거라 보정치로 1000 부여.

    public void startRandomGame(int ch) // 각 장에 따른 게임 랜덤 선택.
    {
        setRandomData(ch);

        for (int i = 0; i < Game_Status.GAME_MAX_STAGE; i++)
            selectRandomGamesType(i);
    }

    private void setRandomData(int ch) // selectedGames와 gameIntent의 초기값을 설정할 기능. ch가 1 ~ 3 이므로 배열에 맞게 1 빼야 함. 그래야 0 ~ 2까지 나옴.
    {
        for(int i = 0; i < 6; i ++)
        {
            refBlanks.add(storage.getBlankStorageOnChapter(ch - 1)[i]);
            refBools.add(storage.getBoolStorageOnChapter(ch - 1)[i]);
            refOrders.add(storage.getOrderStorageOnChapter(ch - 1)[i]);
            refFixes.add(storage.getFixStorageOnChapter(ch - 1)[i]);
        }
    }

    private void selectRandomGamesType(int index) // 설정된 초기값을 바탕으로 무작위로 선택. 스타일부터 선택
    {
        int rand_style = (random.nextInt(4) + 1) * gameType; // 스타일 정하는 랜덤 변수. 보정치를 붙여줘야 1000 ~ 4000 이 나온다. 1 더하는 것도 0 ~ 3까지 나오기 때문.
        selectRandomGamesNumber(rand_style, index);
    }

    private void selectRandomGamesNumber(int style, int index) // 스타일이 선택되었으면 그 중 무작위 하나 추출하고 추출된 정보를 제외하여 다음 선택시에 또 뽑히지 않게 함.
    {
        int style_amount; // 현재 선택된 스타일의 게임 개수가 얼마나 남아있냐를 받는 변수.
        int rand_number;  // 0 ~ 남아있는 문제 개수. 즉 선택된 스타일의 문제를 선택.

        switch (style)
        {
            case Game_Status.GAME_BLANK:
                style_amount = refBlanks.size();
                break;
            case Game_Status.GAME_BOOL:
                style_amount = refBools.size();
                break;
            case Game_Status.GAME_ORDER:
                style_amount = refOrders.size();
                break;
            case Game_Status.GAME_FIX:
                style_amount = refFixes.size();
                break;
            default: // 아무것도 아니면
                style_amount = -100;
                break;
        }

        if(style_amount == 0) // 리스트에 아무것도 없으면 다시 골라야 함. 단, 다른 스타일로.
        {
            Log.d("자원 고갈", style + "스타일의 현재 장 문제가 전부 리스트에 포함되었으므로 스타일을 다시 정합니다.");

            int re_style = 0; // 기존 style에 리스트가 없으므로 새로운 스타일을 선정.

            while (true) // re_style의 기본값 또는 이미 고갈된 스타일로 지정되지 않을 때까지 선정.
            {
                re_style = (random.nextInt(4) + 1) * gameType;
                if(re_style != style) {
                    Log.d("스타일 재설정", "선택된 스타일 : "+re_style);
                    break;
                }
            }

            selectRandomGamesNumber(re_style, index); // 자기 자신을 다시 불러 선택함. 이번엔 다른 스타일로
        }
        else if(style_amount == -100) // 예외 대비용
        {
            return;
        }
        else // 리스트에 남아있기는 한다면
        {
            rand_number = random.nextInt(style_amount);
            insertRandomGame(style, rand_number, index);
        }
    }

    private void insertRandomGame(int style, int listIndex, int impIndex) // selectedGames의 값을 여기서 정해준다. 선택된 인덱스와 selectedGames가 쓸 인덱스를 준다.
    {
        switch (style)
        {
            case Game_Status.GAME_BLANK:
                selectedGames[impIndex] = refBlanks.get(listIndex); // 추가
                intentStyle[impIndex] = Game_Status.GAME_BLANK; // intent 스타일 추가.
                refBlanks.remove(listIndex); // 참조 리스트에서 제거
                break;
            case Game_Status.GAME_BOOL:
                selectedGames[impIndex] = refBools.get(listIndex);
                intentStyle[impIndex] = Game_Status.GAME_BOOL;
                refBools.remove(listIndex); // 참조 리스트에서 제거
                break;
            case Game_Status.GAME_ORDER:
                selectedGames[impIndex] = refOrders.get(listIndex);
                intentStyle[impIndex] = Game_Status.GAME_ORDER;
                refOrders.remove(listIndex); // 참조 리스트에서 제거
                break;
            case Game_Status.GAME_FIX:
                selectedGames[impIndex] = refFixes.get(listIndex);
                intentStyle[impIndex] = Game_Status.GAME_FIX;
                refFixes.remove(listIndex); // 참조 리스트에서 제거
                break;
            default: // 아무것도 아니면
                break;
        }
    }

    public void resetChoice() // 랜덤 선택된 정보 초기화.
    {
        for (int i = 0; i < Game_Status.GAME_MAX_STAGE; i++)
        {
            selectedGames[i] = null;
            intentStyle[i] = 0;
        }

        refBlanks.clear();
        refBools.clear();
        refOrders.clear();
        refFixes.clear();
    }
}
