package com.taiihc.monkey_daily.Http;

import com.taiihc.monkey_daily.Beans.WelfareBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface WelfareService {
    @GET("data/福利/{month}/{day}")
    Observable<WelfareBean> getWelfareList(@Path("month") int month, @Path("day") int day);
}
