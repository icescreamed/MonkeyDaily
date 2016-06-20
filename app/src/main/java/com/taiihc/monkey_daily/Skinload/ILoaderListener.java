package com.taiihc.monkey_daily.Skinload;

/**
 * 加载皮肤时需要回调的接口
 */
public interface ILoaderListener {
    void onStart();

    void onSuccess();

    void onFailed();
}
