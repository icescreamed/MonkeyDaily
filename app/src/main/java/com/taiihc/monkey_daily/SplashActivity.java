package com.taiihc.monkey_daily;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 启动界面
 */
public class SplashActivity extends AppCompatActivity{
    @BindView(R.id.monkey_img)
    public ImageView monkey_img;
    @BindView(R.id.monkey_shape)
    public ImageView shape_img;
    private AnimatorSet animatorSet;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        playAnimation(startScaleAnimation(shape_img,1.1f,0.5f,2000),startTranslateAimation(monkey_img,-20f,20f,2000));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private ObjectAnimator startTranslateAimation(View view,float translateh,float translatel,long duration){
        PropertyValuesHolder translateY = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_Y,
                Keyframe.ofFloat(0f,1.0f),
                Keyframe.ofFloat(0.10f,translateh),
                Keyframe.ofFloat(0.25f,translatel),
                Keyframe.ofFloat(0.45f,translateh),
                Keyframe.ofFloat(0.65f,translatel),
                Keyframe.ofFloat(0.80f,translateh*2),
                Keyframe.ofFloat(1.0f,1.0f));
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view,translateY);
        objectAnimator.setDuration(duration);
        return objectAnimator;
    }

    private ObjectAnimator startScaleAnimation(View view, float scalelarge, float scalesmall, long duration){
        PropertyValuesHolder scaleholderx = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f,1.0f),
                Keyframe.ofFloat(0.10f,scalesmall),
                Keyframe.ofFloat(0.25f,scalelarge),
                Keyframe.ofFloat(0.45f,scalesmall),
                Keyframe.ofFloat(0.65f,scalelarge),
                Keyframe.ofFloat(0.80f,scalesmall/2),
                Keyframe.ofFloat(1.0f,1.0f));
        PropertyValuesHolder scaleholdery = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f,1.0f),
                Keyframe.ofFloat(0.10f,scalesmall),
                Keyframe.ofFloat(0.25f,scalelarge),
                Keyframe.ofFloat(0.45f,scalesmall),
                Keyframe.ofFloat(0.65f,scalelarge),
                Keyframe.ofFloat(0.80f,scalesmall/2),
                Keyframe.ofFloat(1.0f,1.0f));

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view,scaleholderx,scaleholdery);
        objectAnimator.setDuration(duration);

        return objectAnimator;
    }

    public void playAnimation(ObjectAnimator... objectAnimators){
        animatorSet = new AnimatorSet();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.playTogether(objectAnimators);
        animatorSet.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        animatorSet = null;
    }
}
