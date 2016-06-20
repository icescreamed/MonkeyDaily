package com.taiihc.monkey_daily.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taiihc.monkey_daily.Adapter.WelfareRecAdapter;
import com.taiihc.monkey_daily.Contracts.WelfareContract;
import com.taiihc.monkey_daily.R;
import com.taiihc.monkey_daily.widgets.ExRecycleView;
import com.taiihc.monkey_daily.widgets.LoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WelfareFragment extends BaseFragment implements WelfareContract.View{
    private WelfareContract.Presenter mPresenter;
    private WelfareRecAdapter mAdapter ;
    @BindView(R.id.welfare_rec)
    public ExRecycleView welfare_rec;
    @BindView(R.id.welfare_reflash)
    public SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.welfare_layout,container,false);
        ButterKnife.bind(this,view);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
       // final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
       // layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        welfare_rec.setItemViewCacheSize(3);
        welfare_rec.setLayoutManager(layoutManager);
        mAdapter = new WelfareRecAdapter(getContext(),new ArrayList<String>());
        welfare_rec.setAdapter(mAdapter);
        welfare_rec.addLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoad() {
                mPresenter.loadmoveData();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
            }
        });
        return view;
    }


    public  static  WelfareFragment getInstance(){
        return new WelfareFragment();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();


    }

    private WelfareContract.Presenter checkPresenternull(WelfareContract.Presenter presenter){
        if(null == presenter){
            return null;
        }
        return presenter;
    }


    @Override
    public void setPresenter(WelfareContract.Presenter presenter) {
        mPresenter = checkPresenternull(presenter);
    }

    @Override
    public void setRecAdapterData(List<String> urls) {
        mAdapter.setUrls(urls);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setProcesseEnd(Boolean end) {

        welfare_rec.setLoadingComplete();
    }
}
