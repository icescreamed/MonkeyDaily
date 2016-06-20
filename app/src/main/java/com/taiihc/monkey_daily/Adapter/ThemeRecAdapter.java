package com.taiihc.monkey_daily.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.taiihc.monkey_daily.Beans.ThemeBean;
import com.taiihc.monkey_daily.R;

import java.util.List;


public class ThemeRecAdapter  extends RecyclerView.Adapter<ThemeRecAdapter.ThemeViewHolder> {
    private List<ThemeBean> themeBeanList;
    private Context context;
    private OnButtonClickListener listener;
    public ThemeRecAdapter(List<ThemeBean> themeBeanList,Context context){
        this.themeBeanList = themeBeanList;
        this.context = context;
    }

    @Override
    public ThemeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.theme_item_layout,parent,false);
        return new ThemeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ThemeViewHolder holder, final int position) {
        if(themeBeanList.get(position).isUsed()){
           holder.button.setText("使用中");
        }else {
            holder.button.setText("使用");
        }
        setColor(holder,themeBeanList.get(position).getColorname());
        holder.textView.setText(themeBeanList.get(position).getColorname());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClick(themeBeanList.get(position).getColorname());

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return themeBeanList.size();
    }



    public class ThemeViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        Button button;
        public ThemeViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.theme_text);
            button = (Button)itemView.findViewById(R.id.theme_button);
        }
    }

    private void setColor(ThemeViewHolder holder,String colorname){
        if("咸蛋黄".equals(colorname)){
            holder.textView.setTextColor(Color.YELLOW);
            holder.button.setTextColor(Color.YELLOW);
        }else if("早苗绿".equals(colorname)){
            holder.textView.setTextColor(Color.GREEN);
            holder.button.setTextColor(Color.GREEN);
        }else if("烈焰红".equals(colorname)){
            holder.textView.setTextColor(Color.RED);
            holder.button.setTextColor(Color.RED);
        }else {
            holder.textView.setTextColor(Color.BLUE);
            holder.button.setTextColor(Color.BLUE);
        }
    }
    public interface OnButtonClickListener{
        void onClick(String colorname);
    }

    public void setButtonClickListener(OnButtonClickListener listener){
        this.listener = listener;
    }

}
