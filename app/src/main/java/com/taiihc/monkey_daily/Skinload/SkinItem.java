package com.taiihc.monkey_daily.Skinload;

import android.view.View;

import com.taiihc.monkey_daily.Attr.SkinAttr;

import java.util.ArrayList;
import java.util.List;

public class SkinItem {

    public View view;
    public List<SkinAttr> attrs;

    public SkinItem() {
        attrs = new ArrayList<>();
    }

    public void apply() {
        if (attrs==null || attrs.size()==0) {
            return;
        }
        for (SkinAttr at : attrs) {
            at.apply(view);
        }
    }

    public void clean() {
        if (attrs==null || attrs.size()==0) {
            return;
        }
        for (SkinAttr at : attrs) {
            at = null;
        }
    }

    @Override
    public String toString() {
        return "SkinItem [view=" + view.getClass().getSimpleName() + ", attrs=" + attrs + "]";
    }
}