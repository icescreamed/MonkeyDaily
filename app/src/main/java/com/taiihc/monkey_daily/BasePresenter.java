package com.taiihc.monkey_daily;

/**
 * MVP的Presenter需要实现的基本接口
 */
public interface BasePresenter {
    void start();
    void loadmoveData();
    void reflashData();
}
