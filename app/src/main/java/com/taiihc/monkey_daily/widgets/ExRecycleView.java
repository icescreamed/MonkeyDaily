package com.taiihc.monkey_daily.widgets;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

import com.taiihc.monkey_daily.R;

import java.util.ArrayList;
import java.util.List;

/**
 *到达底端时 上拉显示底端空白的RecycleView 可配合LoadingView使用
 */
public class ExRecycleView extends RecyclerView {
    private static final int DEFAULT_DRAG_DISTENCE = 50;
    private boolean loadenable;
    private int dragmaxdistence;
    private int touchSlop;
    private List<LoadMoreListener> loadMoreListenerList;
    private int mLastitemposition;
    private static boolean isloading = false;
    private float scollstartX;
    private float scollstartY;
    private static final float DRAG_RATE = 0.5f;
    private ObjectAnimator resetanimator;
    private Interpolator acinterpolator = new AccelerateInterpolator();
    private Interpolator interpolator = new AccelerateInterpolator();

    public ExRecycleView(Context context) {
        this(context,null);
    }

    public ExRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ExRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ExRecycleView);
        loadenable = array.getBoolean(R.styleable.ExRecycleView_loadenable,false);
        int max = array.getInt(R.styleable.ExRecycleView_maxdragdistance,DEFAULT_DRAG_DISTENCE);
        dragmaxdistence = convertdptoPixl(context,max);
        array.recycle();


    }



    public void setLoadenable(boolean loadenable) {
        this.loadenable = loadenable;
    }
    public void setLoadingComplete(){
        isloading = false;
        animtionOffsettoend(0f,acinterpolator,"translationY");
       smoothScrollToPosition(mLastitemposition+1 );
    }
    public Boolean isLoadenable(){
        return loadenable;
    }



    private int convertdptoPixl(Context context, int dp){
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(density*dp);
    }



    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
    }

    public void addLoadMoreListener(LoadMoreListener listener){
        if(null == loadMoreListenerList){
            loadMoreListenerList = new ArrayList<>();
        }
        loadMoreListenerList.add(listener);
    }

    public void removeLoadMoreListener(LoadMoreListener listener){
        loadMoreListenerList.remove(listener);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
       if (getLayoutManager() instanceof LinearLayoutManager){
           mLastitemposition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
       }else if(getLayoutManager() instanceof GridLayoutManager){
           mLastitemposition = ((GridLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
       }else if(getLayoutManager() instanceof StaggeredGridLayoutManager){
           StaggeredGridLayoutManager lm = (StaggeredGridLayoutManager) getLayoutManager();
           int column = lm.getColumnCountForAccessibility(null,null);
           int[] positions = new int[column];
           int[] lastposition = lm.findLastVisibleItemPositions(positions);
           int maxpostion = lastposition[0];
           for(int value :lastposition){
               if(maxpostion<value){
                   maxpostion = value;
               }
           }
           mLastitemposition = maxpostion;
       }else {
           throw new
                   RuntimeException("Unsupported LayoutManager,please use LinearLayoutManager,GridLayoutManager or StaggeredGridLayoutManager");
       }
    }
    //if the recycleview can still up(not to the end),return true
    private boolean canScrollfoot(){
        return ViewCompat.canScrollVertically(this,1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(!loadenable || isloading || isEmtry() || canScrollfoot()){
            return super.onTouchEvent(e);
        }
        boolean canScrollY = getLayoutManager().canScrollVertically();
        int action = e.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                scollstartX = e.getX();
                scollstartY = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = e.getX();
                float moveY = e.getY();
                float distanceY = moveY - scollstartY;
                float distanceX = moveX - scollstartX;
                if(Math.abs(distanceY)>touchSlop && Math.abs(distanceX)<Math.abs(distanceY) && canScrollY && distanceY<0 ){
                      float end = -dampAxis(distanceY);
                      setTranslationY(end);
                      return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
               if(canScrollY){
                   float endY = e.getY();
                   float dis = (scollstartY - endY)*DRAG_RATE;
                   if(dis>dragmaxdistence){
                       isloading = true;
                       dispatchOnLoadingMoreListeners();
                   }else {
                       isloading =false;
                       animtionOffsettoend(0f,interpolator,"translationY");
                   }
                   break;
               }

        }

         return super.onTouchEvent(e);
    }



    private void dispatchOnLoadingMoreListeners() {
        if(loadMoreListenerList!=null){
            for(LoadMoreListener listener:loadMoreListenerList){
                listener.onLoad();
            }
        }

    }

    private float dampAxis(float delta) {

        final float scrollEnd = delta * DRAG_RATE;

        float mCurrentDragPercent = scrollEnd / dragmaxdistence;
        //如果超出了总上拉 这个比例就是1
        float boundedDragPercent = Math.min(1f, Math.abs(mCurrentDragPercent));
          //得到剩下的上拉距离
        float extraOS = Math.abs(scrollEnd) - dragmaxdistence;
        float slingshotDist = dragmaxdistence;
       //计算一个张力
        float tensionSlingshotPercent = Math.max(0,
                Math.min(extraOS, slingshotDist * 2) / slingshotDist);
        float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow(
                (tensionSlingshotPercent / 4), 2)) * 2f;
        float extraMove = (slingshotDist) * tensionPercent / 2;
        float targetEnd = (slingshotDist * boundedDragPercent) + extraMove;
        return targetEnd;
    }

    private boolean isEmtry(){
        if(getAdapter()==null || getAdapter().getItemCount()==0 ){
            return true;
        }
        return false;
    }

    private void animtionOffsettoend(float value, Interpolator interpolator,String name){
        if(resetanimator==null){
            resetanimator = new ObjectAnimator();
            resetanimator.setTarget(this);
        }


        resetanimator.setPropertyName(name);
        resetanimator.setFloatValues(value);
        resetanimator.setInterpolator(interpolator);
        resetanimator.start();

    }
}
