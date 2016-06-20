package com.taiihc.monkey_daily;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.taiihc.monkey_daily.Adapter.ThemeRecAdapter;
import com.taiihc.monkey_daily.Beans.ThemeBean;
import com.taiihc.monkey_daily.Skinload.ILoaderListener;
import com.taiihc.monkey_daily.Skinload.SkinBaseActivity;
import com.taiihc.monkey_daily.Skinload.SkinManager;
import com.taiihc.monkey_daily.Utils.FileUtils;
import com.taiihc.monkey_daily.Utils.PreferencesUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 更换主题的Activity
 */
public class ThemeSetActivity extends SkinBaseActivity {
    @BindView(R.id.theme_rec)
    public RecyclerView recyclerView;
    @BindView(R.id.theme_toolbar)
    public Toolbar toolbar;
    private ThemeRecAdapter adapter;

    private static String SKIN_NAME_GREEN = "skin_green.skin";
    private static String SKIN_NAME_RED = "skin_red.skin";
    private static String SKIN_NAME_YELLOW = "skin_yellow.skin";
    private static String SKIN_NAME_BLUE = "skin_blue.skin";
    private static String defualtskin = SKIN_NAME_BLUE;
    private static String themecolor = "胖次蓝";
    private static String THEME_COLOR = "theme_color";
    private static String SKIN_DIR =  Environment.getExternalStorageDirectory().getAbsolutePath();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        ButterKnife.bind(this);
        toolbar.setTitle("我的主题");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final List<ThemeBean> list = new ArrayList<>();
        list.add(new ThemeBean("胖次蓝"));
        list.add(new ThemeBean("咸蛋黄"));
        list.add(new ThemeBean("烈焰红"));
        list.add(new ThemeBean("早苗绿"));
        for(int i = 0;i<4;i++){
            if(list.get(i).getColorname()== PreferencesUtils.getString(this,THEME_COLOR,themecolor)){
                list.get(i).setUsed(true);
            }
        }
        adapter = new ThemeRecAdapter(list,this);
        adapter.setButtonClickListener(new ThemeRecAdapter.OnButtonClickListener() {
            @Override
            public void onClick(String colorname) {
                if("胖次蓝".equals(colorname)){
                    SkinManager.getInstance().restoreDefaultTheme();
                }else {
                    PreferencesUtils.putString(ThemeSetActivity.this,THEME_COLOR,colorname);
                    chageTheme(colorname);
                }
                for(ThemeBean bean:list){
                    if(bean.getColorname().equals(colorname)){
                        bean.setUsed(true);
                    }else {
                        bean.setUsed(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);

    }

    private void chageTheme(String colorname){
        String skinname = parseColorName(colorname);
        if(skinname==null){
            skinname = defualtskin;
        }
        String skinFullName = SKIN_DIR + File.separator + skinname;
        FileUtils.moveRawToDir(this, skinname, skinFullName);
        File skin = new File(skinFullName);
        if (!skin.exists()) {
            Toast.makeText(this, "请检查" + skinFullName + "是否存在", Toast.LENGTH_SHORT).show();
            return;
        }
        SkinManager.getInstance().load(skin.getAbsolutePath(),
                new ILoaderListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailed() {

                    }
                });

    }



    private String parseColorName(String name){
        if("胖次蓝".equals(name)){
            return null;
        }else if("咸蛋黄".equals(name)){
            return SKIN_NAME_YELLOW;
        }else if("烈焰红".equals(name)){
            return SKIN_NAME_RED;
        }else if("早苗绿".equals(name)){
            return SKIN_NAME_GREEN;
        }else {
            return  null;
        }
    }
}
