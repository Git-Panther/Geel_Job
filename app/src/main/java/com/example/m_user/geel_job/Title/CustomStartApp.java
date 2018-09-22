package com.example.m_user.geel_job.Title;

import android.app.Application;
import android.graphics.Typeface;
import android.util.Log;

import com.tsengvn.typekit.Typekit;

/**
 * Created by They on 2017-11-19.
 */

public class CustomStartApp extends Application { // 폰트 변경용 초반 세팅. build.gradle에 compile 추가하고 AndroidManifest.xml에 android:name 추가함.
    @Override
    public void onCreate()
    {
        super.onCreate();

        try {
            Typekit.getInstance()
                    .addNormal(Typekit.createFromAsset(this, "fonts/bradley_hand_itc_tt_bold.ttf"))
                    .add("NON_KOREAN", Typekit.createFromAsset(this, "fonts/bradley_hand_itc_tt.ttf"))
                    .add("KOREAN", Typekit.createFromAsset(this, "fonts/mn.ttf"))
                    .add("KOREANBOLD", Typekit.createFromAsset(this, "fonts/mg.ttf"))
                    .add("HUNMIN", Typekit.createFromAsset(this, "fonts/mh.ttf"))
                    .add("HANGEUL", Typekit.createFromAsset(this, "fonts/KoPubDotumBold.ttf"));
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
            Log.d("폰트 불러오기 실패", "폰트 기본값으로");
            Typeface.defaultFromStyle(0);
        }
    }
}
