package com.taiihc.monkey_daily.Http;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class PersistentCookiestore implements CookieStore {
    public static final String PREFER_COOKIE = "CookiesPreference";
    private Map<String,ConcurrentHashMap<String,HttpCookie>> cookies;
    private SharedPreferences cookiePref ;
    private static final String COOKIE_NAME_PREFIX = "cookie_";

    public  PersistentCookiestore(Context context){
        cookiePref = context.getSharedPreferences(PREFER_COOKIE,0);
        cookies = new HashMap<>();

        //取出已存储在SharePreference中的所有cookie
        Map<String,?> preMap = cookiePref.getAll();
        //抽取出所有键值对中 值以cookie_开头的值  这个键值对代表一个url和其对应所有的cookie名称
        for (Map.Entry<String,?> entry: preMap.entrySet()){
              if(entry.getKey()!=null &&entry.getValue()!=null
                      && ((String)entry.getValue()).startsWith(COOKIE_NAME_PREFIX)){
                  String[] cookiesname = TextUtils.split((String)entry.getValue(),",");
                  for(String name:cookiesname){
                      //通过cookie的名字在SharePreference中找到对应的键值
                      String encodecookie = cookiePref.getString(COOKIE_NAME_PREFIX+name,null);
                      if(encodecookie!=null){
                          //将键值转换为HttpCookie
                          HttpCookie decodecookie = decodeCookie(encodecookie);
                          if (decodecookie!=null){
                              if(!cookies.containsKey(entry.getKey())){
                                  //将上面url对应的所有cookie存入Map中 这应该是一个二维的Map
                                  cookies.put(entry.getKey(),new ConcurrentHashMap<String, HttpCookie>());
                              }
                              cookies.get(entry.getKey()).put(name,decodecookie);
                          }
                      }
                  }
              }

        }


    }

    private HttpCookie decodeCookie(String encodecookie) {
        return null;
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        if(!cookie.hasExpired()){
            if(!cookies.containsKey(cookie.getDomain())){
                cookies.put(cookie.getDomain(),new ConcurrentHashMap<String, HttpCookie>());
            }
            cookies.get(cookie.getDomain()).put(cookie.getName(),cookie);
        }else {
            if(cookies.containsKey(cookie.getDomain())){
                cookies.get(cookie.getDomain()).remove(cookie.getName());

            }
            return;
        }
        SharedPreferences.Editor editor = cookiePref.edit();
        editor.putString(cookie.getDomain(),TextUtils.join(",",cookies.get(cookie.getDomain()).keySet()));
        editor.putString(COOKIE_NAME_PREFIX+cookie.getName(),encodecookie(cookie));
        editor.commit();


    }

    private String encodecookie(HttpCookie decodecookie) {
        if (decodecookie == null)
            return null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(decodecookie);
        } catch (IOException e) {

            return null;
        }

        return byteArrayToHexString(os.toByteArray());
    }

    private String byteArrayToHexString(byte[] bytes) {
        return null;
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return null;
    }

    @Override
    public List<HttpCookie> getCookies() {
        return null;
    }

    @Override
    public List<URI> getURIs() {
        return null;
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return false;
    }

    @Override
    public boolean removeAll() {
        return false;
    }
}
