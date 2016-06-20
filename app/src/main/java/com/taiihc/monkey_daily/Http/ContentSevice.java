package com.taiihc.monkey_daily.Http;

import com.taiihc.monkey_daily.Beans.NewContent;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


public interface ContentSevice {
    @GET("{id}")
    Observable<NewContent> getContent(@Path("id") int id);



}
