package com.taiihc.monkey_daily.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
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
    private ImageLoader imageLoader;
    private DisplayImageOptions options;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.welfare_layout,container,false);
        ButterKnife.bind(this,view);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
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
                mPresenter.start();
            }
        });
        mAdapter.setOnImageClickListener(new WelfareRecAdapter.OnImageClickListener() {
            @Override
            public void onClick(String imgurl) {
                openBigImgView(imgurl);
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheInMemory(true).build();
    }

    private void openBigImgView(final String imgurl){
         View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_layout,null);
         ImageView magnifyingImg =(ImageView)view.findViewById(R.id.dialog_img);
         final Button save = (Button)view.findViewById(R.id.dialog_save);
         imageLoader.displayImage(imgurl,magnifyingImg,options);

        final Dialog dialog = new Dialog(getContext(),R.style.Dialog_Fullscreen);

         dialog.setContentView(view);
         save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.downLoadImage(imgurl);
                save.setVisibility(View.GONE);
            }
         });
         magnifyingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
         magnifyingImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                save.setVisibility(View.VISIBLE);
                return true;
            }
         });
        dialog.show();
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

    @Override
    public void refreshProgress(boolean begin) {
      refreshLayout.setRefreshing(begin);
    }

    @Override
    public void downLoadFinish(Boolean sucessed) {
        if(sucessed){
            Toast.makeText(getContext(),"download sucessed",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getContext(),"网络好像有点问题",Toast.LENGTH_LONG).show();
        }
    }
}
