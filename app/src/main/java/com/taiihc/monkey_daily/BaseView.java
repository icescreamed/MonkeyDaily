package com.taiihc.monkey_daily;

/**
 * MVP的View需要实现的基本接口
 */
public interface BaseView<T>{

    void setPresenter(T presenter);
}
