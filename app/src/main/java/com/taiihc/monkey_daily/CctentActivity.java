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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taiihc.monkey_daily.Beans.NewContent;
import com.taiihc.monkey_daily.Skinload.SkinBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CctentActivity extends SkinBaseActivity {
    public static final String INTENT_CONTENT = "intent_content";
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
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
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
        floatingActionButton.setVisibility(View.GONE);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheOnDisk(true).build();
        news_WebView.getSettings().setJavaScriptEnabled(true);
        news_WebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        news_WebView.getSettings().setDomStorageEnabled(true);
        news_WebView.getSettings().setDatabaseEnabled(true);
        news_WebView.getSettings().setAppCacheEnabled(true);
        content = getIntent().getParcelableExtra(INTENT_CONTENT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        imageLoader.displayImage(content.getImage(),news_ImageView,options);
        news_ToolbarLayout.setTitle(content.getTitle());
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/content.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + content.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        news_WebView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8",null);
    }
}
