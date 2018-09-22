package com.example.m_user.geel_job.Interface;

import android.app.Activity;
import android.content.Context;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by They on 2017-11-19.
 */

public class CustomFontActivity extends Activity { // 커스텀 폰트 상속용
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
