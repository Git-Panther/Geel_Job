package com.example.m_user.geel_job.GamePlay.Listeners;

import android.app.Activity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.m_user.geel_job.GamePlay.Event.GameEventStatus;
import com.example.m_user.geel_job.GamePlay.Game_Status;

import static com.example.m_user.geel_job.GamePlay.Event.Game_event_Game_Play.answer;
import static com.example.m_user.geel_job.GamePlay.Event.Game_event_Game_Play.answer_place;
import static com.example.m_user.geel_job.GamePlay.Event.Game_event_Game_Play.quest_place;

/**
 * Created by They on 2017-11-04.
 */

public class DnDGameEventListener extends Activity implements View.OnDragListener {

    private static DnDGameEventListener instance; // Singletone

    private DnDGameEventListener() {}

    public static DnDGameEventListener getInstance()
    {
        if(instance == null)
            instance = new DnDGameEventListener();

        return instance;
    }

    GameEventStatus status = GameEventStatus.getInstance(); // 게임 스테이터스 가져와서 정답 수정

    LinearLayout containView; // 현재 접근된 빈칸
    ImageView beforeImage; // 옮기려는 빈칸 자리에 현재 존재하는 정답 항목의 이미지뷰(변경 전)
    ImageView view; // 옮기고 있는 정답
    ViewGroup viewgroup; // 옮기고 있는 정답의 LinearLayout 가져오기
    ImageView afterImage; // 가장 최근에 빈칸 안으로 들어온 정답 항목의 이미지뷰(삽입, 변경 후)
    ImageView extraAfterImage; // 두 빈칸에서 각 항목들이 교체되었을 때, view로 들어온 beforeImage를 가져오기 위한 변수.
    boolean swapCount = true; // containView(옮기려는 빈칸) 안에 이미 다른 항목이 있으면 false, 비어있는 경우는 true. 밑의 for문으로 false가 될 수 있으니 기본값은 true.
    boolean checkCount = false; // 하나의 빈칸만 정답을 검사할 경우 false, 두 빈칸을 동시에 검사할 경우 true. 비어있던 빈칸에 항목을 넣는 경우도 하나만 바꾸기에 기본값은 false. swapTag()에 의해 결정.

    public boolean swapTag(int index) // 현재 빈칸에 무슨 정답이 들어있는지 검사하는 메소드. 들어가 있으면 들어간 것을 원위치로 바꾸고 옮기고 있는 걸 빈칸에 새로 넣는다.
    {
        for(int i = 0; i < Game_Status.GAME_EVENT_ANSWER_AMOUNT; i++)
        {
            if(viewgroup == answer_place[i]) // 옮기고 있는 정답이 원래 부모 리니어(answer_place[])로부터 왔다면. 즉 빈칸에 갱신되는 이미지가 하나뿐일 경우.
            {
                containView.removeView(beforeImage); // 빈칸 비우기
                viewgroup.removeView(view); // 옮기고 있는 정답을 포함하고 있던 LinearLayout에서 정답 삭제

                containView.addView(view); // 빈칸에 정답 추가.
                answer_place[index].addView(beforeImage); // 이전 정답이 있던 데로 제자리로 되돌린다.

                return false; // 실제 항목 교환은 1회밖에 안 하므로 다음 처리를 패스하고자 반환 처리. 갱신 이미지가 하나이므로 false.
            }
        }

        // 이하는 위 if문이 단 한 번도 발생하지 않았을 때( = 한 쪽은 빈칸에 있던 항목, 다른 한 쪽은 정답이 원래 있던 곳에서의 항목). 즉, 빈칸 갱신 이미지가 두 개일 경우

        containView.removeView(beforeImage); // 옮기고 있는 항목이 들어갈 빈칸(LinearLayout)에 존재했던 항목 삭제
        viewgroup.removeView(view); // 옮기고 있는 정답을 포함하고 있던 LinearLayout에서 정답 삭제

        containView.addView(view); // 빈칸에 정답 추가.
        viewgroup.addView(beforeImage); // 옮기고 있는 항목을 포함하고 있던 빈칸에는 view가 들어갈 빈칸에 있던 beforeImage를 추가.

        return true; // 갱신 이미지가 두 개가 되므로 true 반환.
    }

    public void checkAnswer(boolean b) // 정답&오답 처리 메소드
    {
        afterImage = (ImageView) containView.getChildAt(0); // 현재 들어간 정답 항목 불러오기. beforeImage와는 달리 처리가 진행된 이후에 초기화해주므로 둘의 값은 다를 수 밖에 없다.

        int index = 0; // 비교할 번호 정하기. 0 ~ 2(빈칸 순서) 중 하나가 됨.
        int extraIndex = 0; // 정답 검사할 빈칸이 두개일 경우, 두 번째 빈칸 검사를 위한 인덱스

        for (int i = 0; i < Game_Status.GAME_EVENT_QUEST_AMOUNT; i++) { // 첫 번째 빈칸 배열의 번호 0~2 중 하나 추출하여 index에 저장.
            if (containView == quest_place[i]) {
                index = i;
                if(!b) // 정답 검사할 빈칸이 하나면
                {
                    break;
                }
            }

            if(viewgroup == quest_place[i]) // 두 번째 빈칸 배결의 번호 0-2중 하나 추출하여 저장. 해당사항 없으면 값은 변화하지 않음.
            {
                extraIndex = i;
                // 검사할 빈칸이 두 개면 index의 값도 설정해줘야 하기에 extraIndex의 값이 이미 설정되었다 하더라도 break; 를 쓸 수 없다.
            }
        }

        if (afterImage != answer[Game_Status.GAME_EVENT_ANSWER[index]]) // 첫 번째 빈칸에 들어간 정답이 i번째 정답 항목이 아닌 경우 오답 처리
        {
            status.setAnswer(index, false); // 오답 처리
        } else // 정답인 경우
        {
            status.setAnswer(index, true); // 정답 처리
        }

        if(b) // 정답 비교 대상이 둘이면
        {
            extraAfterImage = (ImageView) viewgroup.getChildAt(0); // 두 번째 검사 대상의 빈칸에 들어간 항목 가져오기

            if (extraAfterImage != answer[Game_Status.GAME_EVENT_ANSWER[extraIndex]]) // 두 번째 빈칸에 들어간 정답이 i번째 정답 항목이 아닌 경우 오답 처리
            {
                status.setAnswer(extraIndex, false); // 오답 처리
                // return; // 정답 체크는 정답 항목을 옮길 때마다 한 번만 적용된다.
            } else // 정답인 경우
            {
                status.setAnswer(extraIndex, true); // 정답 처리
                // return; // 정답 체크는 정답 항목을 옮길 때마다 한 번만 적용된다.
            }
        }
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {

        view = (ImageView) event.getLocalState(); // 옮기고 있는 정답.

        // 이벤트 시작
        switch (event.getAction()) {

            // 이미지를 드래그 시작될때
            case DragEvent.ACTION_DRAG_STARTED:
                Log.d("DragClickListener", "ACTION_DRAG_STARTED");
                break;

            // 드래그한 이미지를 옮길려는 지역으로 들어왔을때
            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d("DragClickListener", "ACTION_DRAG_ENTERED");
                // 이미지가 들어왔다는 것을 알려주기 위해 배경이미지 변경
                break;

            // 드래그한 이미지가 영역을 빠져 나갈때
            case DragEvent.ACTION_DRAG_EXITED:
                Log.d("DragClickListener", "ACTION_DRAG_EXITED");
                break;

            // 이미지를 드래그해서 드랍시켰을때
            case DragEvent.ACTION_DROP:
                Log.d("DragClickListener", "ACTION_DROP");

                viewgroup = (ViewGroup) view.getParent(); // 옮기고 있는 정답의 LinearLayout 가져오기

                if(v instanceof LinearLayout) // 접근된 빈칸이 LinearLayout일 경우
                    containView = (LinearLayout) v; // 현재 접근된 빈칸

                if (containView == quest_place[0] || containView == quest_place[1] || containView == quest_place[2])
                { // 빈칸 항목으로 존재하는 LinearLayout인가 검사
                    beforeImage = (ImageView) containView.getChildAt(0); // 이전에 그 자리에 있었던 정답 항목의 이미지뷰

                    for(int i = 0; i < Game_Status.GAME_EVENT_ANSWER_AMOUNT; i++) { // 빈칸에 이미 항목이 들어가 있는지 1 ~ 6번째 버튼 다 검사
                        if (beforeImage == answer[i]) {
                            swapCount = false; // 이미 빈칸에 항목이 존재하므로 밑의 if문으로 교체할 필요가 없어져 false 설정
                            checkCount = swapTag(i); // 빈칸에 들어왔으면 빈칸 안에 어느 이미지뷰 클래스가 들어가 있는지 검사하여 교체할지 말지 결정. 동시에 정답 체크 기준 설정.
                            break; // 한 번 검사 끝났으므로 반복문 탈출. 버튼 옮길 때 swapTag()는 한 번만.
                        }
                    }

                    if(swapCount) // 위 for문으로 swapTag(i)가 발생하지 않았을 때(= 넣을 빈칸에 아무것도 없으면)
                    {
                        // ** 주의 : 스레드 문제로 viewgroup.removeView(view)가 무시되고 contaionView.addView(View)가 실행될 수 있음.
                        viewgroup.removeView(view); // 옮기고 있는 정답을 포함하고 있던 LinearLayout에서 정답 삭제
                        containView.addView(view); // 빈칸에 정답 추가.
                        // view.setVisibility(View.VISIBLE); // 추가한 정답 보이게 변경
                    }

                    checkAnswer(checkCount); // 옮긴 이후 정답 확인
                }
                else // 해당사항 없음
                {
                    if (view.getVisibility() == View.INVISIBLE) // 만일 옮기고 있던 정답이 보여지지 않는 상태이면
                        view.setVisibility(View.VISIBLE); // 원상태로 돌려서 그 자리에 보이던 상태로 되돌림.
                    break;
                }

                // DROP 이벤트 종료 직전에 boolean 변수를 초기화하여 다음 옮기는 이벤트에서 사고가 나지 않게 한다.
                swapCount = true;
                checkCount = false;

                break;

            case DragEvent.ACTION_DRAG_ENDED: // 드랍이 끝났을 때. 가끔씩 제대로 드랍하지도 않고 끝나는 경우 있기에 보여주게끔은 해야 한다.
                Log.d("DragClickListener", "ACTION_DRAG_ENDED");
                if (view.getVisibility() == View.INVISIBLE) // 만일 옮기고 있던 정답이 보여지지 않는 상태이면
                    view.setVisibility(View.VISIBLE); // 원상태로 돌려서 그 자리에 보이던 상태로 되돌림.

                break;

            default:
                break;
        }
        return true;
    }
}
