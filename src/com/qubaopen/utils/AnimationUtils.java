package com.qubaopen.utils;

import android.annotation.TargetApi;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.IntEvaluator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.qubaopen.settings.MyApplication;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AnimationUtils {

	
	static public void startImgBackGround(ImageView v){

	    AnimationDrawable progressAnimation;
	    progressAnimation=(AnimationDrawable) v.getBackground();
        progressAnimation.start();
	}
	

	// view 改变margin动画
     static public void performAnimateMarginTop(final View target, final int start,
			final int end, Integer duration) {
		ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100000);

		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

			// 持有一个IntEvaluator对象，方便下面估值的时候使用
			IntEvaluator mEvaluator = new IntEvaluator();

			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				// 获得当前动画的进度值，整型，1-100之间
				int currentValue = (Integer) animator.getAnimatedValue();
				// 计算当前进度占整个动画过程的比例，浮点型，0-1之间
				float fraction = currentValue / 100000f;

				// 这里我偷懒了，不过有现成的干吗不用呢
				// 直接调用整型估值器通过比例计算出宽度，然后再设给Button
				((FrameLayout.LayoutParams) target.getLayoutParams()).topMargin = mEvaluator
						.evaluate(fraction, start, end);

				target.requestLayout();

			}

		});
		// valueAnimator.setInterpolator(AnimationUtils.loadInterpolator(mainActivity,
		// android.R.anim.decelerate_interpolator)); //减速
		//
		valueAnimator.setDuration(duration).start();
	}
     
     

 	// view 改变margin动画
      static public void performAnimateMarginBottom(final View target, final int start,
 			final int end, Integer duration) {
 		ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100000);

 		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

 			// 持有一个IntEvaluator对象，方便下面估值的时候使用
 			IntEvaluator mEvaluator = new IntEvaluator();

 			@Override
 			public void onAnimationUpdate(ValueAnimator animator) {
 				// 获得当前动画的进度值，整型，1-100之间
 				int currentValue = (Integer) animator.getAnimatedValue();
 				// 计算当前进度占整个动画过程的比例，浮点型，0-1之间
 				float fraction = currentValue / 100000f;

 				// 这里我偷懒了，不过有现成的干吗不用呢
 				// 直接调用整型估值器通过比例计算出宽度，然后再设给Button
 				((FrameLayout.LayoutParams) target.getLayoutParams()).bottomMargin = mEvaluator
 						.evaluate(fraction, start, end);

 				target.requestLayout();

 			}

 		});
 		// valueAnimator.setInterpolator(AnimationUtils.loadInterpolator(mainActivity,
 		// android.R.anim.decelerate_interpolator)); //减速
 		//
 		valueAnimator.setDuration(duration).start();
 	}
	
      
   	// view 改变roration动画
      static public void performAnimateRoration(final View target, final int start,
 			final int end, Integer duration,final TextView textView) {
 		ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 1000);

 		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

 			// 持有一个IntEvaluator对象，方便下面估值的时候使用
 			IntEvaluator mEvaluator = new IntEvaluator();

 			@Override
 			public void onAnimationUpdate(ValueAnimator animator) {
 				// 获得当前动画的进度值，整型，1-100之间
 				int currentValue = (Integer) animator.getAnimatedValue();
 				// 计算当前进度占整个动画过程的比例，浮点型，0-1之间
 				textView.setText(currentValue/10+"%");
 				float fraction = currentValue / 1000f;

 				// 这里我偷懒了，不过有现成的干吗不用呢
 				// 直接调用整型估值器通过比例计算出宽度，然后再设给Button
 				target.setRotation( mEvaluator
 						.evaluate(fraction, start, end));
 			
 				//target.requestLayout();

 			}

 		});
 		 valueAnimator.setInterpolator(android.view.animation.AnimationUtils.loadInterpolator(MyApplication.getAppContext(),
 		 android.R.anim.linear_interpolator)); //减速
 		
 		valueAnimator.setDuration(duration).start();
 	}
	
}
