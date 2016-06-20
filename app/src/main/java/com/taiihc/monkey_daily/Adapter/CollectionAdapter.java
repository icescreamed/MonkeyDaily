package com.taiihc.monkey_daily.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taiihc.monkey_daily.Beans.NewContent;
import com.taiihc.monkey_daily.R;
import com.taiihc.monkey_daily.Utils.ListUtils;

import java.util.List;

/**
 * Created by tai on 2016/6/19.
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHoder> {
    private Context context;
    private List<NewContent> newContents;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private ItemClickListener listener;
    public CollectionAdapter(Context context,List<NewContent> newContents){
        this.context = context;
        this.newContents = newContents;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).build();
    }
    @Override
    public CollectionViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_rec_item,parent,false);
        return new CollectionViewHoder(view);
    }

    @Override
    public void onBindViewHolder(CollectionViewHoder holder, final int position) {
          if(!ListUtils.isEmpty(newContents)){
              holder.textView.setText(newContents.get(position).getTitle());
              imageLoader.displayImage(newContents.get(position).getImage(),holder.imageView,options);
              holder.itemView.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      if(listener!=null){
                          listener.onClick(newContents.get(position));
                      }
                  }
              });
          }
    }

    public void setDataList(List<NewContent> newContents){
        this.newContents = newContents;
    }

    @Override
    public int getItemCount() {
        return newContents.size();
    }



    public void setOnitemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    public interface ItemClickListener{
        void onClick(NewContent content);
    }


    public class CollectionViewHoder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public CollectionViewHoder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.item_img);
            textView = (TextView)itemView.findViewById(R.id.item_text);
        }
    }
}
