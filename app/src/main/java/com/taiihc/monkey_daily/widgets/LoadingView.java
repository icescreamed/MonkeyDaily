package com.taiihc.monkey_daily.widgets;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 上拉加载底端显示加载进度的view
 */
public class LoadingView extends FrameLayout {
    private boolean loading = false;
    private CircleImageView mCircleView;
    private static final int DEFUALT_ROTATION_DURATION=2000;
    private static final int DEFUALT_REPEAT_COUNT = 100;
    private ObjectAnimator rotationAnimator;
    private void createProgressView() {
        mCircleView = new CircleImageView(getContext());

        addView(mCircleView);
    }
    public boolean isLoading(){
        return  loading;
    }

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        createProgressView();

    }
    public void stoploadingAnimation(){
        if(rotationAnimator!=null && rotationAnimator.isRunning()){
            rotationAnimator.cancel();
            loading = false;
        }
    }

    public void startloadingAnimation(){
        loading = true;
         rotationAnimator = startRotationAimation(this,DEFUALT_ROTATION_DURATION);
         if(!rotationAnimator.isRunning()){
             rotationAnimator.start();
         }
    }

    private ObjectAnimator startRotationAimation(View view, long duration){
        PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat(ROTATION,30f,60f,90f,120f,150f,180f,210f,
                240f,270f,300f,330f,360f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view,rotation);
        objectAnimator.setDuration(duration);
        objectAnimator.setRepeatCount(DEFUALT_REPEAT_COUNT);
        return objectAnimator;
    }

}
