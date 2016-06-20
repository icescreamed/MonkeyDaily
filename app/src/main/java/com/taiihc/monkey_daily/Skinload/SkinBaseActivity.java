package com.taiihc.monkey_daily.Skinload;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;

/**
 * 实现动态换肤必须继承的Activity
 * 由于AppCompatActivity没有实现LayoutInflaterFactory 所以要持有一个LayoutInflaterFactory的引用
 */
public class SkinBaseActivity extends AppCompatActivity implements ISkinUpdate, IDynamicNewView {
    private boolean isResponseOnSkinChanging = true;
    //自定义的LayoutInflaterFactory
    private SkinInflaterFactory mSkinInflaterFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mSkinInflaterFactory = new SkinInflaterFactory();
        LayoutInflaterCompat.setFactory(getLayoutInflater(), mSkinInflaterFactory);
        super.onCreate(savedInstanceState);
        changeStatusColor();

    }

    public void changeStatusColor() {
        //如果当前的Android系统版本大于4.4则更改状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int color = SkinManager.getInstance().getColorPrimaryDark();
            StatusBarBackground statusBarBackground = new StatusBarBackground(
                    this, color);
            if (color != -1)
                statusBarBackground.setStatusBarbackColor();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SkinManager.getInstance().attach(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().detach(this);
        mSkinInflaterFactory.clean();
    }

    @Override
    public void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
    }


    final protected void enableResponseOnSkinChanging(boolean enable) {
        isResponseOnSkinChanging = enable;
    }
    @Override
    public void onThemeUpdate() {
        if (!isResponseOnSkinChanging) {
            return;
        }
        mSkinInflaterFactory.applySkin();
        changeStatusColor();
    }
}
