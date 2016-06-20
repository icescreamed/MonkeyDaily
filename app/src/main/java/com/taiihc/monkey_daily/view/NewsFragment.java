package com.taiihc.monkey_daily.view;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.taiihc.monkey_daily.Adapter.NewsRecAdapter;
import com.taiihc.monkey_daily.Beans.NewsEntry;
import com.taiihc.monkey_daily.Beans.TopNewsEntry;
import com.taiihc.monkey_daily.Contracts.NewsContract;
import com.taiihc.monkey_daily.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NewsFragment extends BaseFragment implements NewsContract.View {

    @BindView(R.id.news_rec)
    public RecyclerView recycleView;
    @BindView(R.id.news_refresh)
    public SwipeRefreshLayout refreshLayout;
    private NewsRecAdapter newsRecAdapter;
    private LinearLayoutManager layoutManager;
    private NewsContract.Presenter presenter;
    private boolean requeststart =true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_layout,container,false);
        ButterKnife.bind(this,view);
        layoutManager = new LinearLayoutManager(getContext());
        List<TopNewsEntry> temp1 = new ArrayList<>();
        List<NewsEntry> temp2 = new ArrayList<>();
        newsRecAdapter = new NewsRecAdapter(getContext(),temp1,temp2);
        recycleView.setLayoutManager(layoutManager);
        recycleView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if(parent.getChildCount() != 0)
                    outRect.bottom = 20;
            }
        });
        recycleView.setAdapter(newsRecAdapter);
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    int firstposition = layoutManager.findFirstCompletelyVisibleItemPosition();
                    int lastposition = layoutManager.findLastCompletelyVisibleItemPosition();
                    if(lastposition==layoutManager.getItemCount()-1){
                        presenter.loadmoveData();
                    }
                    if (firstposition!=0){
                        if(newsRecAdapter.isplaying()){
                            newsRecAdapter.headerplay(false);
                        }
                    }else {
                        if(!newsRecAdapter.isplaying()){
                            newsRecAdapter.headerplay(true);
                        }
                    }
                }
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                 presenter.reflashData();
            }
        });
        return view;
    }

    public static  NewsFragment getInstance(){
        return new NewsFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(requeststart) {
            presenter.start();
            requeststart = false;
        }else {
            newsRecAdapter.headerplay(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(newsRecAdapter.isplaying())
            newsRecAdapter.headerplay(false);
    }

    @Override
    public void setPresenter(NewsContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void setHeaderData(List<TopNewsEntry> entryList) {
        newsRecAdapter.setHeaderlist(entryList);
        newsRecAdapter.notifyDataSetChanged();
        newsRecAdapter.headerplay(true);
    }

    @Override
    public void setItemData(List<NewsEntry> entryList) {
        newsRecAdapter.setEntrygList(entryList);
        newsRecAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadMoreData(List<NewsEntry> entryList) {
        List<NewsEntry> list = newsRecAdapter.getEntryList();
        list.addAll(entryList);
        newsRecAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshData(List<NewsEntry> itemList, List<TopNewsEntry> headList) {
        List<NewsEntry> entryList = newsRecAdapter.getEntryList();
        newsRecAdapter.setHeaderlist(headList);
        itemList.addAll(entryList);
        newsRecAdapter.setEntrygList(itemList);
        newsRecAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadDataProccess() {
        if(newsRecAdapter.isloading()){
            newsRecAdapter.stopLoading();
            return;
        }
        newsRecAdapter.startLoading();
    }

    @Override
    public void refreshDataProcess() {
        if(refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }else {
            refreshLayout.setRefreshing(true);
        }


    }


    @Override
    public void loadDatafailed() {
        Toast.makeText(getContext(),"没有更多消息了哟",Toast.LENGTH_LONG).show();
        if(newsRecAdapter.isloading()){
            newsRecAdapter.stopLoading();
        }
    }

    @Override
    public void refreshDatafailed() {
        Toast.makeText(getContext(),"已经是最新内容了哟",Toast.LENGTH_LONG).show();
        if(refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
    }
}
