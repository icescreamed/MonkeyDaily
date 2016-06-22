package com.taiihc.monkey_daily.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taiihc.monkey_daily.R;

import java.util.List;

import butterknife.ButterKnife;


public class WelfareRecAdapter  extends RecyclerView.Adapter<WelfareRecAdapter.MyViewHolder>{
    private Context context;
    private List<String> urls;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private OnImageClickListener listener;
    public WelfareRecAdapter(Context context, List<String> urls){
        this.context = context;
        this.urls = urls;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).build();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.welfare_rec_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        imageLoader.displayImage(urls.get(position),holder.imageView,options);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClick(urls.get(position));
                }
            }
        });
    }
    @Override
    public int getItemCount() {

        return urls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = ButterKnife.findById(itemView,R.id.rec_item_img);
        }
    }

    public interface OnImageClickListener{
        void onClick(String imgurl);
    }

    public void setOnImageClickListener(OnImageClickListener listener){
        this.listener = listener;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
