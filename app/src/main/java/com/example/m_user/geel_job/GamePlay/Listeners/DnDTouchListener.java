package com.example.m_user.geel_job.GamePlay.Listeners;

import android.content.ClipData;
import android.content.ClipDescription;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by They on 2017-11-18.
 */

public class DnDTouchListener implements View.OnTouchListener {
    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                // 태그 생성
                ClipData.Item item = new ClipData.Item(
                        (CharSequence) v.getTag());

                String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
                ClipData data = new ClipData(v.getTag().toString(),
                        mimeTypes, item);
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        v);

                v.startDrag(data, // data to be dragged
                        shadowBuilder, // drag shadow
                        v, // 드래그 드랍할  View
                        0 // 필요없는 플래그
                );

                v.setVisibility(View.INVISIBLE);
                break;
        }

        return true;
    }
}
