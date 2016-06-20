package com.taiihc.monkey_daily.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taiihc.monkey_daily.Beans.NewsEntry;
import com.taiihc.monkey_daily.Beans.TopNewsEntry;
import com.taiihc.monkey_daily.ContentActivity;
import com.taiihc.monkey_daily.R;
import com.taiihc.monkey_daily.widgets.CycleView;
import com.taiihc.monkey_daily.widgets.LoadingView;

import java.util.List;

import butterknife.ButterKnife;


public class NewsRecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NewsEntry> entryList;
   private List<TopNewsEntry> headerlist;
    private Context context;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEMS = 1;
    private static final int TYPE_FOOTER = 2;
    private CycleView cv;
    private LoadingView lv;

    public NewsRecAdapter(Context context,List<TopNewsEntry> headerlist,List<NewsEntry> entryList){
        this.context = context;
        this.entryList = entryList;
        this.headerlist = headerlist;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();


    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType==TYPE_HEADER){
            view = LayoutInflater.from(context).inflate(R.layout.rec_header,parent,false);
            return new HeaderViewHolder(view);
        } else if(viewType==TYPE_ITEMS){
            view = LayoutInflater.from(context).inflate(R.layout.news_rec_item,parent,false);
            return new ItemViewHolder(view);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.rec_footer,parent,false);

            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ItemViewHolder){
            if(entryList!=null && entryList.size()>0){
                imageLoader.displayImage(entryList.get(position-1).getImages()[0],((ItemViewHolder) holder).imageView,options);
                ((ItemViewHolder) holder).textView.setText(entryList.get(position-1).getTitle());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickResponse(entryList.get(position-1).getId());
                    }
                });
            }
        }else if(holder instanceof HeaderViewHolder){
            if(headerlist!=null)
            {
                Log.e("HeaderViewHolder","binddata");
                ((HeaderViewHolder) holder).cycleView.setData(headerlist);
                ((HeaderViewHolder) holder).cycleView.setCycleOnClicklistener(new CycleView.OnCycleClickListener() {
                    @Override
                    public void onClick(int id) {
                        itemClickResponse(id);
                    }
                });
            }

        }else if(holder instanceof FooterViewHolder) {

        }

    }

    private void itemClickResponse(int id){
        Intent intent = new Intent(context, ContentActivity.class);
        intent.putExtra(ContentActivity.CONTENT_ID,id);
        context.startActivity(intent);
    }



    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return TYPE_HEADER;
        }
        else if(position==entryList.size()+1){
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEMS;
        }
    }

    @Override
    public int getItemCount() {
        return entryList.size()+2;

    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = ButterKnife.findById(itemView,R.id.item_img);
            textView = ButterKnife.findById(itemView,R.id.item_text);
        }
    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        public CycleView cycleView;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            cycleView = ButterKnife.findById(itemView,R.id.cycle_view);
            cv = cycleView;
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder{
        LoadingView loadingView;

        public FooterViewHolder(View itemView) {
            super(itemView);
            loadingView = (LoadingView) itemView.findViewById(R.id.foot_loadingview);
            lv = loadingView;
        }
    }

    public void setHeaderlist(List<TopNewsEntry> headerlist){
        this.headerlist = headerlist;
    }

    public List<TopNewsEntry> getHeaderlist() {
        return headerlist;
    }

    public List<NewsEntry> getEntryList() {
        return entryList;
    }

    public void setEntrygList(List<NewsEntry> entrygList) {
        this.entryList = entrygList;
    }

    public void headerplay(boolean play){
        if(play){
            cv.startplay();
        }else {
            cv.stopplay();
        }
    }

    public boolean isplaying(){
        return  cv.isplaying();
    }

    public boolean isloading(){
        return  lv.isLoading();
    }

    public void startLoading(){
       lv.startloadingAnimation();
    }

    public void stopLoading(){
        lv.stoploadingAnimation();
    }

}
