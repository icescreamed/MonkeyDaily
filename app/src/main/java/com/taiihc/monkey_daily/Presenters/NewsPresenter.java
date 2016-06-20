package com.taiihc.monkey_daily.Presenters;

import android.util.Log;

import com.taiihc.monkey_daily.Beans.BeforeBean;
import com.taiihc.monkey_daily.Beans.NewBean;
import com.taiihc.monkey_daily.Contracts.NewsContract;
import com.taiihc.monkey_daily.Http.HttpService;

import java.text.SimpleDateFormat;

import rx.Subscriber;


public class NewsPresenter implements NewsContract.Presenter {
    private NewsContract.View mView;
    private HttpService httpService;
    private int mdate = 0;
    private int today;
    public NewsPresenter(NewsContract.View view){
         mView = view;
        httpService = HttpService.getHttpService();
         mView.setPresenter(this);
    }
    @Override
    public void start() {
        mView.refreshDataProcess();
        httpService.getLatestStroylist(new Subscriber<NewBean>() {
             @Override
             public void onCompleted() {
                mView.refreshDataProcess();
             }

             @Override
             public void onError(Throwable e) {
                   Log.e("getLatestStroylist",e.toString());
             }

             @Override
             public void onNext(NewBean newBean) {
                  mView.setHeaderData(newBean.getTop_stories());
                  mView.setItemData(newBean.getStories());
                  today = Integer.parseInt(newBean.getDate());
                   mdate = today;
             }
         });
    }

    @Override
    public void loadmoveData() {
        mView.loadDataProccess();
        if(mdate==0){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String date = simpleDateFormat.format(new java.util.Date());
            mdate = Integer.parseInt(date);
        }
           mdate = reduce(mdate);
        httpService.getBeforeStroylist(new Subscriber<BeforeBean>() {
            @Override
            public void onCompleted() {
                mView.loadDataProccess();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BeforeBean beforeBean) {
                Log.e("onNext()",beforeBean.getDate());
                mView.loadMoreData(beforeBean.getStories());
            }
        },String.valueOf(mdate));
    }

    private int reduce(int date){
        int day = date%100;
        int month = date%10000-day;
        int year = date-month-day;
        int tempmonth = month/100;
        int tempyear = year/10000;
       if(day-1==0){
           if(tempyear%400==0 ||tempyear%4==0 ||tempmonth==3){
               day=29;
           }else if(tempmonth==3){
               day=28;
           }else if(tempmonth==1 || tempmonth==2 || tempmonth==4 || tempmonth==6 || tempmonth==8 ||
                   tempmonth==9 || tempmonth==11 ){
               day=31;
           }else {
               day=30;
           }
           if(tempmonth--<0){
               tempmonth=12;
               tempyear--;
           }
       }else {
           day--;
       }
        return tempyear*10000+tempmonth*100+day;
    }

    @Override
    public void reflashData() {
        mView.refreshDataProcess();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = simpleDateFormat.format(new java.util.Date());
        int day = Integer.parseInt(date);
        if(day == today){
            mView.refreshDatafailed();
        }else {
            httpService.getLatestStroylist(new Subscriber<NewBean>() {
                @Override
                public void onCompleted() {
                    mView.refreshDataProcess();
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(NewBean newBean) {
                    mView.setHeaderData(newBean.getTop_stories());
                    mView.setItemData(newBean.getStories());
                    today = Integer.parseInt(newBean.getDate());
                    mdate = today;
                }
            });
        }
    }




}
