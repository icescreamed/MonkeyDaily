package com.taiihc.monkey_daily.Attr;


import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.taiihc.monkey_daily.Skinload.SkinManager;

public class FloatButtonAttr extends SkinAttr{

    @Override
    public void apply(View view) {
        if (view instanceof FloatingActionButton) {
           FloatingActionButton fab = (FloatingActionButton) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
                int color = SkinManager.getInstance().getColor(attrValueRefId);
                fab.setBackgroundTintList(ColorStateList.valueOf(color));
            }
        }

    }
}
