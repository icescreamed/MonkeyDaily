package com.taiihc.monkey_daily;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.taiihc.monkey_daily.Adapter.CollectionAdapter;
import com.taiihc.monkey_daily.Beans.NewContent;
import com.taiihc.monkey_daily.Db.DbService;
import com.taiihc.monkey_daily.Skinload.SkinBaseActivity;
import com.taiihc.monkey_daily.Utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tai on 2016/6/19.
 */
public class CollectionActivity extends SkinBaseActivity {
    @BindView(R.id.collection_rec)
    public RecyclerView recyclerView;
    @BindView(R.id.collection_toolbar)
    public Toolbar toolbar;
    private CollectionAdapter adapter;
    private List<NewContent>newContents;
    private RecyclerView.LayoutManager manager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        toolbar.setTitle("我的收藏");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if(parent.getChildCount() != 0)
                    outRect.bottom = 20;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        newContents = new ArrayList<>();
        adapter =new CollectionAdapter(CollectionActivity.this,newContents);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter.setOnitemClickListener(new CollectionAdapter.ItemClickListener() {
            @Override
            public void onClick(NewContent content) {
                Intent intent = new Intent(CollectionActivity.this,CctentActivity.class);
                intent.putExtra(CctentActivity.INTENT_CONTENT,content);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DbService.getAllData(this, new DbService.QueryListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(List<String> contents) {
               if(!ListUtils.isEmpty(contents)){
                   Gson gson = new Gson();
                   if(newContents==null){
                       newContents= new ArrayList<>();
                   }
                   int len = contents.size();
                   for(int i=0;i<len;i++ ){
                       newContents.add(gson.fromJson(contents.get(i),NewContent.class));
                   }
                   adapter.setDataList(newContents);
                   adapter.notifyDataSetChanged();
               }


            }
        });
    }
}
