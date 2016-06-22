package com.taiihc.monkey_daily.Http;


import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface DownLoadService {
    @GET("{url}")
    Observable<ResponseBody> downLoadImage(@Path("url") String url);
}
