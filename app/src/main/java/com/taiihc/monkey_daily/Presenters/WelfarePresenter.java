package com.taiihc.monkey_daily.Presenters;

import com.taiihc.monkey_daily.Beans.WelfareBean;
import com.taiihc.monkey_daily.Contracts.WelfareContract;
import com.taiihc.monkey_daily.Http.HttpService;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;


public class WelfarePresenter implements WelfareContract.Presenter {
    private WelfareContract.View mView;
    private HttpService httpService;
    private   List<String> urls;
    private int defualCount = 20;
    private int defualPage = 1;
    private int increat =1;
    public WelfarePresenter(WelfareContract.View view){
        this.mView = view;
        urls = new ArrayList<>();
        httpService = HttpService.getHttpService();
        view.setPresenter(this);
    }


    @Override
    public void start() {
        httpService.getWelfarelist(new Subscriber<List<WelfareBean.WelfareEntry>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(List<WelfareBean.WelfareEntry> welfareEntries) {
                    for(WelfareBean.WelfareEntry entry : welfareEntries){
                        urls.add(entry.getUrl());
                    }
                    mView.setRecAdapterData(urls);
                }
            },defualCount,defualPage);



    }




    @Override
    public void loadmoveData() {
        final List<String> temp = new ArrayList<>();
        httpService.getWelfarelist(new Subscriber<List<WelfareBean.WelfareEntry>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<WelfareBean.WelfareEntry> welfareEntries) {
                for(WelfareBean.WelfareEntry entry : welfareEntries){
                    temp.add(entry.getUrl());
                }
                urls.addAll(temp);
                mView.setRecAdapterData(urls);
                mView.setProcesseEnd(true);
            }
        },defualCount,defualPage+increat);
        increat++;

    }

    @Override
    public void reflashData() {}

    @Override
    public void downLoadImage(String url) {
        httpService.downLoadImage(url, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                 mView.downLoadFinish(aBoolean);
            }
        });
    }
}
