package com.example.m_user.geel_job.GamePlay.Listeners;

import android.view.DragEvent;
import android.view.View;

/**
 * Created by They on 2017-11-10.
 */

public class ETCOnDragListener implements View.OnDragListener { // 옮기지 않을 공간에서 드래그앤드랍 이펙트가 이상하게 출력되지 않도록 여백 레이아웃에 부여하는 이벤트

    private ETCOnDragListener()
    {

    }

    private static ETCOnDragListener instance; // singletone

    public static ETCOnDragListener getInstance()
    {
        if(instance == null)
            instance = new ETCOnDragListener();

        return instance;
    }

    /**
     * Called when a drag event is dispatched to a view. This allows listeners
     * to get a chance to override base View behavior.
     *
     * @param v     The View that received the drag event.
     * @param event The {@link DragEvent} object for the drag event.
     * @return {@code true} if the drag event was handled successfully, or {@code false}
     * if the drag event was not handled. Note that {@code false} will trigger the View
     * to call its {@link #onDragEvent(DragEvent) onDragEvent()} handler.
     */
    @Override
    public boolean onDrag(View v, DragEvent event) {
        return true;
    }
}
