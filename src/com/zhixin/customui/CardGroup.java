package com.zhixin.customui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.IntEvaluator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.zhixin.R;

//层叠卡片型ui

public class CardGroup {

	private View _view;

	private Context context;

	private List<RelativeLayout> layoutList;
	private List<Boolean> isOpenList;
	private List<Integer> changePixelList;

	private List<Integer> colorList;

	private ValueAnimator currentAnimator;

	private boolean isRunning = false;

	private RelativeLayout nextAnimLayout;

	private ScrollView scrollView;

	private RelativeLayout layoutParent;

	private int numberOfLayout;

	public CardGroup(View v, Context c) {
		// TODO Auto-generated constructor stub
		context = c;
		_view = v;
		layoutList = new ArrayList<RelativeLayout>();
		isOpenList = new ArrayList<Boolean>();
		changePixelList = new ArrayList<Integer>();
		scrollView = (ScrollView) _view.findViewById(R.id.layout_scroll);
		layoutParent = (RelativeLayout) _view.findViewById(R.id.layout_parent);

		colorList = new ArrayList<Integer>();
		colorList.add(Color.parseColor("#42aaff"));
		colorList.add(Color.parseColor("#db3c41"));
		colorList.add(Color.parseColor("#c671fb"));
		colorList.add(Color.parseColor("#fb8371"));
		colorList.add(Color.parseColor("#4aae32"));
		colorList.add(Color.parseColor("#ecaa5b"));
		colorList.add(Color.parseColor("#f3542d"));
		numberOfLayout = -1;

	}

	// view 改变height动画
	private void performAnimate(final View target, final int start,
			final int end, Integer duration) {

		isRunning = true;
		ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100000);
		valueAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				// TODO Auto-generated method stub

				isRunning = false;
				if (nextAnimLayout != null) {
					changeHeight(nextAnimLayout, null);
					nextAnimLayout = null;
				} else {
					scrollView.requestChildFocus(layoutParent, target);
				}

			}

			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub

			}
		});
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

			// 持有一个IntEvaluator对象，方便下面估值的时候使用
			private IntEvaluator mEvaluator = new IntEvaluator();

			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				// 获得当前动画的进度值，整型，1-100之间
				int currentValue = (Integer) animator.getAnimatedValue();
				// 计算当前进度占整个动画过程的比例，浮点型，0-1之间
				float fraction = currentValue / 100000f;

				// 这里我偷懒了，不过有现成的干吗不用呢
				// 直接调用整型估值器通过比例计算出宽度，然后再设给Button
				target.getLayoutParams().height = mEvaluator.evaluate(fraction,
						start, end);
				target.requestLayout();

				if (target.getLayoutParams().height == end) {
					target.setEnabled(true);
				}
			}

		});
		// valueAnimator.setInterpolator(AnimationUtils.loadInterpolator(mainActivity,
		// android.R.anim.accelerate_interpolator)); //加速
		// valueAnimator.setInterpolator(AnimationUtils.loadInterpolator(mainActivity,
		// android.R.anim.decelerate_interpolator)); //减速
		// valueAnimator.setInterpolator(AnimationUtils.loadInterpolator(mainActivity,
		// android.R.anim.accelerate_decelerate_interpolator)); //加速 reverse 减速
		// valueAnimator.setInterpolator(AnimationUtils.loadInterpolator(mainActivity,
		// android.R.anim.anticipate_interpolator)); //先往后退一点再加速前进
		// valueAnimator.setInterpolator(AnimationUtils.loadInterpolator(mainActivity,
		// android.R.anim.overshoot_interpolator)); //减速前进,冲过终点前再后退
		// valueAnimator.setInterpolator(AnimationUtils.loadInterpolator(mainActivity,
		// android.R.anim.anticipate_overshoot_interpolator)); //case 上2的结合体
		// valueAnimator.setInterpolator(AnimationUtils.loadInterpolator(mainActivity,
		// android.R.anim.bounce_interpolator)); //case 上2的结合体

		currentAnimator = valueAnimator;
		valueAnimator.setDuration(duration).start();
	}

	// view 改变margin动画
	private void performAnimateMargin(final View target, final int start,
			final int end, Integer duration) {
		ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100000);

		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

			// 持有一个IntEvaluator对象，方便下面估值的时候使用
			private IntEvaluator mEvaluator = new IntEvaluator();

			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				// 获得当前动画的进度值，整型，1-100之间
				int currentValue = (Integer) animator.getAnimatedValue();
				// 计算当前进度占整个动画过程的比例，浮点型，0-1之间
				float fraction = currentValue / 100000f;

				// 这里我偷懒了，不过有现成的干吗不用呢
				// 直接调用整型估值器通过比例计算出宽度，然后再设给Button
				((RelativeLayout.LayoutParams) target.getLayoutParams()).topMargin = mEvaluator
						.evaluate(fraction, start, end);

				target.requestLayout();

			}

		});
		// valueAnimator.setInterpolator(AnimationUtils.loadInterpolator(mainActivity,
		// android.R.anim.decelerate_interpolator)); //减速
		//
		valueAnimator.setDuration(duration).start();
	}

	// 点击文件夹 展开
	private void changeHeight(RelativeLayout rl, Integer duration) {
		int index, currentHeight;
		// 判断是否有动画正在运行
		if (isRunning) {
			return;
		}
		rl.setEnabled(false);
		index = layoutList.indexOf(rl);
		currentHeight = rl.getHeight();
		duration = (duration == null ? 200 : duration);
		if (!isOpenList.get(index)) {
			changePixelList.set(index, 360);
			performAnimate(rl, currentHeight,
					currentHeight + changePixelList.get(index), duration);
			changeMargin(rl, duration);
			rotateArrow(rl,false,duration);
			isOpenList.set(index, true);

		} else {
			performAnimate(rl, currentHeight,
					currentHeight - changePixelList.get(index), duration);
			changeMargin(rl, duration);
			rotateArrow(rl,true,duration);
			isOpenList.set(index, false);
		}
		
	
		
	}

	// 关闭已经打开的了
	private void closeOpened(RelativeLayout rl) {
		for (int i = 0; i < isOpenList.size(); i++) {
			if (isOpenList.get(i) && layoutList.get(i) != rl) {
				changeHeight(layoutList.get(i), 50);
				nextAnimLayout = rl;
			}
		}

	}

	// 遍历在自己之上的layout 进行margin动画
	private void changeMargin(RelativeLayout rl, Integer deruation) {
		int pos = layoutList.indexOf(rl);
		int changePixel = changePixelList.get(pos);
		deruation = (deruation == null ? 200 : deruation);
		if (!isOpenList.get(pos)) {
			Log.d("login1", "pos: " + pos);
			for (int i = pos + 1; i <= numberOfLayout; i++) {
				RelativeLayout layout = layoutList.get(i);
				int currentMargin = ((RelativeLayout.LayoutParams) layout
						.getLayoutParams()).topMargin;
				performAnimateMargin(layout, currentMargin, currentMargin
						+ changePixel, deruation);
			}
		} else {
			for (int i = pos + 1; i <= numberOfLayout; i++) {
				RelativeLayout layout = layoutList.get(i);
				int currentMargin = ((RelativeLayout.LayoutParams) layout
						.getLayoutParams()).topMargin;
				performAnimateMargin(layout, currentMargin, currentMargin
						- changePixel, deruation);
			}
		}

	}

	// 增加一层文件夹
	public void addLayout() {
		RelativeLayout rl = new RelativeLayout(context);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 180);
		// lp.topMargin=layoutList.size()*50*3;
		if (layoutList.size() == 0) {
			lp.topMargin = 0;
		} else {
			lp.topMargin = ((RelativeLayout.LayoutParams) layoutList.get(
					layoutList.size() - 1).getLayoutParams()).topMargin + 135;

		}
		rl.setLayoutParams(lp);
		// rl.setBackgroundResource(drawableList.get((layoutList.size())%7));
		rl.setBackgroundResource(R.drawable.card_small_origin);
		addColorBlock(rl);
		addArrow(rl);
		layoutParent.addView(rl);
		layoutList.add(rl);
		isOpenList.add(false);
		changePixelList.add(0);
		numberOfLayout++;

		rl.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				closeOpened((RelativeLayout) v);
				changeHeight((RelativeLayout) v, null);
			}
		});

	}

	// 左边色块
	private void addColorBlock(RelativeLayout rl) {

		ImageView view = new ImageView(context);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(24,
				180);
		view.setLayoutParams(lp);
		view.setBackgroundColor(colorList.get((layoutList.size()) % 7));
		view.setTag("block");
		rl.addView(view);

	}		
	
	//右侧箭头
	private void addArrow(RelativeLayout rl){
		ImageView view = new ImageView(context);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp.topMargin=60;
		lp.rightMargin=45;
		view.setLayoutParams(lp);
		view.setImageResource(R.drawable.card_arrow_origin);
		view.setBackgroundColor(colorList.get((layoutList.size()) % 7));
		view.setTag("arrow");
		rl.addView(view);
	}
	
	//旋转箭头
	private void rotateArrow(RelativeLayout rl,boolean isOpen,int duration){
		ImageView iv=(ImageView)rl.findViewWithTag("arrow");
		RotateAnimation operatingAnim;
		
		if(!isOpen){
			operatingAnim = new RotateAnimation(0, 179,Animation.RELATIVE_TO_SELF, 
					0.5f,Animation.RELATIVE_TO_SELF,0.5f);  
		}else{
			operatingAnim = new RotateAnimation(180, 359,Animation.RELATIVE_TO_SELF, 
					0.5f,Animation.RELATIVE_TO_SELF,0.5f);  
		}
		LinearInterpolator lin = new LinearInterpolator();  
		operatingAnim.setInterpolator(lin); 
		operatingAnim.setDuration(duration);
		operatingAnim.setFillAfter(true);
		iv.startAnimation(operatingAnim);  
	}

	//增加卡片标题
	private void addCardTitle(RelativeLayout rl){}
	
	//增加卡片内容
	private void addCardContent(RelativeLayout rl){}
}
