package com.taiihc.monkey_daily.Skinload;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.nostra13.universalimageloader.utils.L;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SkinManager implements ISkinLoader {

    private List<ISkinUpdate> mSkinObservers;
    private static volatile SkinManager mInstance;
    private Context context;
    private Resources mResources;
    private boolean isDefaultSkin = false;//当前的皮肤是否是默认的
    private String skinPackageName;//皮肤的包名
    private String skinPath;//皮肤路径

    private SkinManager() {
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
    }

    public int getColorPrimaryDark() {
        if (mResources != null) {
            int identify = mResources.getIdentifier("colorPrimaryDark", "color", skinPackageName);
            return mResources.getColor(identify);
        }
        return -1;
    }

    /**
     * 判断当前使用的皮肤是否来自外部
     *
     * @return
     */
    public boolean isExternalSkin() {
        return !isDefaultSkin && mResources != null;
    }

    /**
     * 得到当前的皮肤路径
     *
     * @return
     */
    public String getSkinPath() {
        return skinPath;
    }

    /**
     * 得到当前皮肤的包名
     *
     * @return
     */
    public String getSkinPackageName() {
        return skinPackageName;
    }

    public Resources getResources() {
        return mResources;
    }

    /**
     * 恢复到默认主题
     */
    public void restoreDefaultTheme() {
        SkinConfig.saveSkinPath(context, SkinConfig.DEFALT_SKIN);
        isDefaultSkin = true;
        mResources = context.getResources();
        skinPackageName = context.getPackageName();
        notifySkinUpdate();
    }

    public static SkinManager getInstance() {
        if (mInstance == null) {
            synchronized (SkinManager.class) {
                if (mInstance == null) {
                    mInstance = new SkinManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 绑定监听者
     * @param observer
     */
    @Override
    public void attach(ISkinUpdate observer) {
        if (mSkinObservers == null) {
            mSkinObservers = new ArrayList<>();
        }
        if (!mSkinObservers.contains(observer)) {
            mSkinObservers.add(observer);
        }
    }

    @Override
    public void detach(ISkinUpdate observer) {
        if (mSkinObservers == null) return;
        if (mSkinObservers.contains(observer)) {
            mSkinObservers.remove(observer);
        }
    }

    @Override
    public void notifySkinUpdate() {
        if (mSkinObservers == null) {
            return;
        }
        for (ISkinUpdate observer : mSkinObservers) {
            observer.onThemeUpdate();
        }
    }

    public void load() {
        String skin = SkinConfig.getCustomSkinPath(context);
        load(skin, null);
    }

    public void load(ILoaderListener callback) {
        String skin = SkinConfig.getCustomSkinPath(context);
        if (SkinConfig.isDefaultSkin(context)) {
            return;
        }
        load(skin, callback);
    }

    /**
     * callback是加载时的回调 skinPackagePath就是皮肤的路径
     * @param skinPackagePath
     * @param callback
     */
    public void load(String skinPackagePath, final ILoaderListener callback) {
        new AsyncTask<String, Void, Resources>() {
            protected void onPreExecute() {
                if (callback != null) {
                    callback.onStart();
                }
            }

            /**
             * 主要是读取皮肤包并保存到SharePreference
             * @param params
             * @return
             */
            @Override
            protected Resources doInBackground(String... params) {
                try {
                    if (params.length == 1) {
                        String skinPkgPath = params[0];

                        File file = new File(skinPkgPath);
                        if (file == null || !file.exists()) {
                            return null;
                        }

                        PackageManager mPm = context.getPackageManager();
                        PackageInfo mInfo = mPm.getPackageArchiveInfo(skinPkgPath, PackageManager.GET_ACTIVITIES);
                        skinPackageName = mInfo.packageName;

                        AssetManager assetManager = AssetManager.class.newInstance();
                        Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                        addAssetPath.invoke(assetManager, skinPkgPath);


                        Resources superRes = context.getResources();
                        Resources skinResource = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());

                        SkinConfig.saveSkinPath(context, skinPkgPath);

                        skinPath = skinPkgPath;
                        isDefaultSkin = false;
                        return skinResource;
                    }
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            protected void onPostExecute(Resources result) {
                mResources = result;
                if (mResources != null) {
                    if (callback != null)
                    {
                        callback.onSuccess();
                    }
                    notifySkinUpdate();
                } else {
                    isDefaultSkin = true;
                    if (callback != null)
                    {
                        callback.onFailed();
                    }
                }
            }

        }.execute(skinPackagePath);
    }

    public int getColor(int resId) {
        int originColor = context.getResources().getColor(resId);
        if (mResources == null || isDefaultSkin) {
            return originColor;
        }

        String resName = context.getResources().getResourceEntryName(resId);

        int trueResId = mResources.getIdentifier(resName, "color", skinPackageName);
        int trueColor = 0;

        try {
            trueColor = mResources.getColor(trueResId);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            trueColor = originColor;
        }

        return trueColor;
    }

    public Drawable getDrawable(int resId) {
        Drawable originDrawable = context.getResources().getDrawable(resId);
        if (mResources == null || isDefaultSkin) {
            return originDrawable;
        }
        String resName = context.getResources().getResourceEntryName(resId);

        int trueResId = mResources.getIdentifier(resName, "drawable", skinPackageName);

        Drawable trueDrawable = null;
        try {
            L.i("SkinManager getDrawable", "SDK_INT = " + android.os.Build.VERSION.SDK_INT);
            if (android.os.Build.VERSION.SDK_INT < 22) {
                trueDrawable = mResources.getDrawable(trueResId);
            } else {
                trueDrawable = mResources.getDrawable(trueResId, null);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            trueDrawable = originDrawable;
        }

        return trueDrawable;
    }



}
