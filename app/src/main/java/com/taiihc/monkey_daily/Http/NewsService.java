package com.taiihc.monkey_daily.Http;

import com.taiihc.monkey_daily.Beans.BeforeBean;
import com.taiihc.monkey_daily.Beans.NewBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


public interface NewsService {
    @GET("latest")
    Observable<NewBean> getLatestList();

    @GET("before/{date}")
    Observable<BeforeBean> getBeforeList(@Path("date")String date);



}
