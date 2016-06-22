package com.taiihc.monkey_daily.Http;

import android.os.Environment;
import android.util.Log;

import com.taiihc.monkey_daily.Beans.BeforeBean;
import com.taiihc.monkey_daily.Beans.NewBean;
import com.taiihc.monkey_daily.Beans.NewContent;
import com.taiihc.monkey_daily.Beans.WelfareBean;
import com.taiihc.monkey_daily.Constants.Apis;
import com.taiihc.monkey_daily.Utils.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class HttpService {
    private static volatile HttpService singleHttpService;
    private Retrofit gankhRetrofit;
    private Retrofit zhihuRectrofit;
    private Retrofit downLoadRectrofit;
    private NewsService newsService;
    private WelfareService welfareService;
    private ContentSevice contentSevice;
    private DownLoadService downLoadService;
    private HttpService(){
        gankhRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Apis.Urls.GanHuoBaseUrl)
                .build();
        zhihuRectrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Apis.Urls.ZhuHuBaseUrl)
                .build();
    }

    public static HttpService getHttpService(){
        if(null == singleHttpService){
            synchronized (HttpService.class){
                if(null == singleHttpService){
                    singleHttpService = new HttpService();
                    return singleHttpService;
                }
            }
        }
        return  singleHttpService;
    }
    public void downLoadImage(String url,Subscriber<Boolean> subscriber){
           String img = url.substring(url.lastIndexOf("/")+1);
           String base = url.substring(0,url.lastIndexOf("/")+1);
           Log.e("downlaodimg base",base);
           Log.e("downloading img",img);
           downLoadRectrofit = new Retrofit.Builder()
                   .baseUrl(base)
                   .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                   .build();


            downLoadService = downLoadRectrofit.create(DownLoadService.class);

        downLoadService.downLoadImage(img)
                .subscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, Boolean>() {

                    @Override
                    public Boolean call(ResponseBody responseBody) {
                        return saveImage(responseBody);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    private Boolean saveImage(ResponseBody body){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String time = df.format(new Date());
        String filename = path+File.separator+time+".jpg";
        Boolean success = false;
        try {
            File file = FileUtils.writeFile(body.byteStream(),filename,true);
            success = FileUtils.fileExists(file.getAbsolutePath());
            return success;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }







    public void getNewsContent(Subscriber<NewContent> subscriber,int id){
        if(contentSevice==null){
            contentSevice = zhihuRectrofit.create(ContentSevice.class);
        }

        contentSevice.getContent(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }



    public void getBeforeStroylist(Subscriber<BeforeBean> subscriber,String date){
        if(newsService==null){
            newsService = zhihuRectrofit.create(NewsService.class);
        }
        newsService.getBeforeList(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void getLatestStroylist(Subscriber<NewBean> subscriber){
        if(newsService==null){
            newsService = zhihuRectrofit.create(NewsService.class);
        }

        newsService
                .getLatestList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }




    public void getWelfarelist(Subscriber<List<WelfareBean.WelfareEntry>> subscriber, int count, int page){
        if(welfareService==null){
             welfareService = gankhRetrofit.create(WelfareService.class);
        }
        welfareService.getWelfareList(count,page)
                .map(new Func1<WelfareBean, List<WelfareBean.WelfareEntry>>() {
                    @Override
                    public List<WelfareBean.WelfareEntry> call(WelfareBean welfareBean) {
                        return welfareBean.getResults();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }





    private OkHttpClient getokHttpClient(){
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .cookieJar(new CookieJar() {
                    private final HashMap<HttpUrl,List<Cookie>>cookiestore = new HashMap<>();
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                               cookiestore.put(HttpUrl.parse(url.host()),cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        return cookiestore.get(HttpUrl.parse(url.host()));
                    }
                }).build();
    }
}
