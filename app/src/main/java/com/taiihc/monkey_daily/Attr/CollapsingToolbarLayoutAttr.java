package com.taiihc.monkey_daily.Attr;

import android.support.design.widget.CollapsingToolbarLayout;
import android.util.Log;
import android.view.View;

import com.taiihc.monkey_daily.Skinload.SkinManager;


public class CollapsingToolbarLayoutAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof CollapsingToolbarLayout) {
            CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
                int color = SkinManager.getInstance().getColor(attrValueRefId);
                ctl.setContentScrimColor(color);
                ctl.setBackgroundColor(color);
            } else if (RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)) {
                Log.i("CollapsingToolbarAttr", "apply drawable");
            }
        }
    }
}
