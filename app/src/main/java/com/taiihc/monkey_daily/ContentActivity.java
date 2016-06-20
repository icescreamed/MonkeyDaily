package com.taiihc.monkey_daily;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taiihc.monkey_daily.Beans.NewContent;
import com.taiihc.monkey_daily.Db.DbService;
import com.taiihc.monkey_daily.Http.HttpService;
import com.taiihc.monkey_daily.Skinload.SkinBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * 显示新闻内容的Activity
 */
public class ContentActivity extends SkinBaseActivity {
    public static final String CONTENT_ID = "content_id";
    @BindView(R.id.news_toolbar)
    public Toolbar news_ToolBar;
    @BindView(R.id.news_web)
    public WebView news_WebView;
    @BindView(R.id.new_iv)
    public ImageView news_ImageView;
    @BindView(R.id.news_collapsinglayout)
    public CollapsingToolbarLayout news_ToolbarLayout;

    @BindView(R.id.content_fab)
    public FloatingActionButton floatingActionButton;
    private HttpService httpService;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private int id  ;
    private NewContent content;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ButterKnife.bind(this);
        setSupportActionBar(news_ToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        news_ToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   saveContent();
            }
        });
        id = getIntent().getIntExtra(CONTENT_ID,0);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheOnDisk(true).build();
        news_WebView.getSettings().setJavaScriptEnabled(true);
        news_WebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        news_WebView.getSettings().setDomStorageEnabled(true);
        news_WebView.getSettings().setDatabaseEnabled(true);
        news_WebView.getSettings().setAppCacheEnabled(true);

    }

    private void saveContent(){
        if(content!=null&& id!=0){
            Gson gson = new Gson();
            String jsonstring  = gson.toJson(content,NewContent.class);
            DbService.insertData(ContentActivity.this, id, jsonstring, new DbService.InsertListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(ContentActivity.this,"收藏成功",Toast.LENGTH_LONG).show();
                }
            });
        }
    }





    @Override
    protected void onStart() {
        super.onStart();
        if(httpService==null)
         httpService = HttpService.getHttpService();
        httpService.getNewsContent(new Subscriber<NewContent>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(NewContent newContent) {
                content = newContent;
                imageLoader.displayImage(newContent.getImage(),news_ImageView,options);
                news_ToolbarLayout.setTitle(newContent.getTitle());
                String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/content.css\" type=\"text/css\">";
                String html = "<html><head>" + css + "</head><body>" + newContent.getBody() + "</body></html>";
                html = html.replace("<div class=\"img-place-holder\">", "");
                news_WebView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8",null);
            }
        },id);



    }
}
