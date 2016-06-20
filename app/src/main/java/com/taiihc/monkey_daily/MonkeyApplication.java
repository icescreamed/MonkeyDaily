package com.taiihc.monkey_daily;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.taiihc.monkey_daily.Skinload.SkinManager;

/**
 *
 */
public class MonkeyApplication extends Application {
    public static String IS_FIRSTTIME_OPEN ;
    private static SharedPreferences appSharePre;
    private static Boolean openFirstTime;


    @Override
    public void onCreate() {
        super.onCreate();
        initUil();
        appSharePre = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = appSharePre.edit();
        openFirstTime = appSharePre.getBoolean(IS_FIRSTTIME_OPEN,true);
        if(openFirstTime){
            editor.putBoolean(IS_FIRSTTIME_OPEN,false);
            editor.commit();
            openFirstTime=false;
        }
        initSkinLoader();
    }



    public static Boolean getOpenFirstTime(){
        return openFirstTime;
    }

    private void initSkinLoader() {
        SkinManager.getInstance().init(this);
        SkinManager.getInstance().load();
    }

    public static SharedPreferences getAppSharePre(){
        return appSharePre;
    }

    private void initUil(){
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(getApplicationContext());
        ImageLoader.getInstance().init(configuration);
    }


}
