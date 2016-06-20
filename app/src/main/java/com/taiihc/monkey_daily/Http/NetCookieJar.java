package com.taiihc.monkey_daily.Http;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;


public class NetCookieJar implements CookieJar {
    private Map<String,List<String>> headers = Collections.emptyMap();
    private Map<String,List<String>> cookieHeaders ;
    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        return null;
    }
}
