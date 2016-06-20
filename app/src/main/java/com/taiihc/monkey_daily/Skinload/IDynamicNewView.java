package com.taiihc.monkey_daily.Skinload;

import android.view.View;

import java.util.List;

/**
 * 绑定view和它需要动态更新的属性
 */
public interface IDynamicNewView {
    void dynamicAddView(View view, List<DynamicAttr> pDAttrs);
}
