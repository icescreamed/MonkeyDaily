package com.taiihc.monkey_daily.widgets;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taiihc.monkey_daily.Beans.TopNewsEntry;
import com.taiihc.monkey_daily.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 循环轮放控件
 */
public class CycleView  extends FrameLayout implements View.OnClickListener{
    private static final String TAG ="CycleView";
    private CycleViewPager cycle_vp;
    private List<View> viewList;
    private Context context;
    private ImageLoader imageLoader;
    private OnCycleClickListener listener;
    private DisplayImageOptions options;
    private int indicatorlen;
    private LinearLayout indicationll;
    private static final int SCROLL_WHAT = 100;
    private AutoPlayHandler handler;
    private List<ImageView> indicator_views;
    private MyPaperAdapter myPaperAdapter;
    private List<TopNewsEntry> datalist;
    private boolean isplaying = false;
    private boolean isStopByTouch = false;
    public CycleView(Context context) {
        this(context,null);

    }

    public CycleView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context,attrs,defStyleAttr,0);
    }

    public CycleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true).build();
        initView();
    }

    private void initView(){
        View view = LayoutInflater.from(context).inflate(R.layout.cycle_layout,this,true);
        cycle_vp = (CycleViewPager) view.findViewById(R.id.vp);
        indicationll = (LinearLayout)view.findViewById(R.id.layout_indicator);
        handler = new AutoPlayHandler(cycle_vp);
        viewList = new ArrayList<>();
        indicator_views = new ArrayList<>();
    }

    public void setData(List<TopNewsEntry> datalist){
        this.datalist = datalist;
        viewList.clear();
        indicationll.removeAllViews();
        indicator_views.clear();
        if(datalist!=null && datalist.size()>0){
            indicatorlen = datalist.size();
            for(int i = 0;i<indicatorlen;i++){
                View fm = LayoutInflater.from(context).inflate(R.layout.cycle_layout_content,null);
                ImageView imageView =(ImageView) fm.findViewById(R.id.cycle_image);
                TextView textView = (TextView)fm.findViewById(R.id.cycle_text);
                imageLoader.displayImage(datalist.get(i).getImages(),imageView,options);
                textView.setText(datalist.get(i).getTitle());
                fm.setOnClickListener(this);
                viewList.add(fm);
            }
            for(int i = 0;i<indicatorlen;i++){
                ImageView imageView = new ImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                       LinearLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 5;
                params.rightMargin = 5;
                params.bottomMargin = 5;
                indicator_views.add(imageView);
                indicationll.addView(imageView,params);
            }
        }
        if(myPaperAdapter!=null){
            myPaperAdapter.setViewList(viewList);
            myPaperAdapter.notifyDataSetChanged();
            cycle_vp.setOnPageChangeListener(new MyPagerChangelistener());
            cycle_vp.setCurrentItem(0);
            imageLoader.displayImage("drawable://"+R.drawable.selected_point,indicator_views.get(0),options);
            return;
        }
        myPaperAdapter = new MyPaperAdapter(viewList);
        cycle_vp.setOnPageChangeListener(new MyPagerChangelistener());
        cycle_vp.setAdapter(myPaperAdapter);
        cycle_vp.setCurrentItem(0);
        if(indicator_views.size()>0)
            imageLoader.displayImage("drawable://"+R.drawable.selected_point,indicator_views.get(0),options);
    }

    public boolean isplaying(){
        return  isplaying;
    }

    public void startplay(){
        isplaying = true;
        handler.sendEmptyMessageDelayed(SCROLL_WHAT,3000);
    }

    public void stopplay(){
        isplaying = false;
        handler.removeMessages(SCROLL_WHAT);
    }

    private  class MyPagerChangelistener implements CycleViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for(int i=0;i<indicatorlen;i++){
                if(i==position){
                    imageLoader.displayImage("drawable://"+R.drawable.selected_point,indicator_views.get(i),options);
                }else {
                    imageLoader.displayImage("drawable://"+R.drawable.un_selected_point,indicator_views.get(i),options);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }



    private static class AutoPlayHandler extends Handler{
        private final long delayTimeInMills = 3000;
        private final WeakReference<CycleViewPager> cycleViewPager;
         public AutoPlayHandler(CycleViewPager cycleViewPager){
             this.cycleViewPager = new WeakReference<>(cycleViewPager);
         }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SCROLL_WHAT:
                    CycleViewPager vp  = cycleViewPager.get();
                    int currentItem =vp.getCurrentItem();
                    vp.setCurrentItem(currentItem+1);
                    removeMessages(SCROLL_WHAT);
                    sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
            }
        }
    }
    private class MyPaperAdapter extends PagerAdapter{
        List<View> viewList;

        public void setViewList(List<View> viewList) {
            this.viewList = viewList;
        }

        public MyPaperAdapter(List<View> views){
            viewList = views;
        }
    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }
    }

    public void setCycleOnClicklistener(OnCycleClickListener listener){
        this.listener = listener;
    }
    public interface  OnCycleClickListener{
        void onClick(int id);

    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(datalist.get(cycle_vp.getCurrentItem()%viewList.size()).getId());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if(action ==MotionEvent.ACTION_DOWN && isplaying){
            isStopByTouch = true;
            stopplay();
        }else if(action == MotionEvent.ACTION_UP && isStopByTouch){
            isStopByTouch = false;
            startplay();
        }
        return super.dispatchTouchEvent(ev);
    }


}
