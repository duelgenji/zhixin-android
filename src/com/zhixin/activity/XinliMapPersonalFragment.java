package com.zhixin.activity;

import java.util.ArrayList;
import java.util.List;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.IntEvaluator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.zhixin.R;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class XinliMapPersonalFragment extends Fragment implements
		View.OnClickListener {

	private Activity mainActivity;

	private Intent intent;

	private View _view;
	
	private RelativeLayout layoutFirst, layoutSecond, layoutThird,
			layoutFourth, layoutFifth;

	private List<RelativeLayout> layoutList;
	private List<Boolean> isOpenList;
	private List<Integer> changePixelList;

	private ValueAnimator currentAnimator;
	
	private boolean isRunning=false;
	
	private RelativeLayout nextAnimLayout;
	
	private Button btnAdd;
	
	private ScrollView scrollView;

	private RelativeLayout layoutParent;

	//数量 包括0
	private int numberOfLayout = 4;
	
	private List<Integer> drawableList; 

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mainActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_xinlimap_personal,
				container, false);
		_view= view;
		layoutList = new ArrayList<RelativeLayout>();
		isOpenList = new ArrayList<Boolean>();
		changePixelList = new ArrayList<Integer>();
		btnAdd= (Button) view.findViewById(R.id.btn_add);		
		btnAdd.setOnClickListener(this);
		
		scrollView = (ScrollView) view.findViewById(R.id.layout_scroll);
		layoutParent=(RelativeLayout)_view.findViewById(R.id.layout_parent);
		
		
		layoutFirst = (RelativeLayout) view.findViewById(R.id.layout_first);
		layoutSecond = (RelativeLayout) view.findViewById(R.id.layout_second);
		layoutThird = (RelativeLayout) view.findViewById(R.id.layout_third);
		layoutFourth = (RelativeLayout) view.findViewById(R.id.layout_fourth);
		layoutFifth = (RelativeLayout) view.findViewById(R.id.layout_fifth);
		layoutList.add(layoutFirst);
		isOpenList.add(false);
		changePixelList.add(0);
		layoutList.add(layoutSecond);
		isOpenList.add(false);
		changePixelList.add(0);
		layoutList.add(layoutThird);
		isOpenList.add(false);
		changePixelList.add(0);
		layoutList.add(layoutFourth);
		isOpenList.add(false);
		changePixelList.add(0);
		layoutList.add(layoutFifth);
		isOpenList.add(false);
		changePixelList.add(0);
		
		
		drawableList=new ArrayList<Integer>();
		drawableList.add(R.drawable.folder_blue_small);
		drawableList.add(R.drawable.folder_green_small);
		drawableList.add(R.drawable.folder_jade_small);
		drawableList.add(R.drawable.folder_red_small);
		drawableList.add(R.drawable.folder_orange_small);
		drawableList.add(R.drawable.folder_six_small);
		drawableList.add(R.drawable.folder_seven_small);

		layoutFirst.setOnClickListener(this);
		layoutSecond.setOnClickListener(this);
		layoutThird.setOnClickListener(this);
		layoutFourth.setOnClickListener(this);
		layoutFifth.setOnClickListener(this);
		return view;

	}

	
	//view height动画
	private void performAnimate(final View target, final int start,
			final int end,Integer duration) {

		isRunning=true;
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

				isRunning=false;
				if(nextAnimLayout!=null){
					changeHeight(nextAnimLayout,null);
					nextAnimLayout=null;
				}else{
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
	
	//view margin动画
	private void performAnimateMargin(final View target, final int start,
			final int end,Integer duration) {
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

	@Override
	public void onClick(View v) {
		int index, currentHeight;
		switch (v.getId()) {
		case R.id.layout_fifth:
			closeOpened((RelativeLayout) v);
			changeHeight((RelativeLayout) v,null);
			// Animation scaleAnimation= new ScaleAnimation((float) 1.0, (float)
			// 1.0,
			// (float) 1.0, (float) 1.5);
			//
			// ValueAnimator animation = ValueAnimator.ofFloat(0f, 1f);
			// animation.setDuration(1000);
			// Animation am =AnimationUtils.loadAnimation(mainActivity,
			// R.anim.changeheight);
			// scaleAnimation.setDuration(1000);
			// scaleAnimation.setFillAfter(true);
			// layoutFifth.startAnimation(scaleAnimation);
			break;
		case R.id.layout_fourth:
			closeOpened((RelativeLayout) v);
			changeHeight((RelativeLayout) v,null);
			break;
		case R.id.layout_third:
			closeOpened((RelativeLayout) v);
			changeHeight((RelativeLayout) v,null);
			break;
		case R.id.layout_second:
			closeOpened((RelativeLayout) v);
			changeHeight((RelativeLayout) v,null);
			break;
		case R.id.layout_first:
			closeOpened((RelativeLayout) v);
			changeHeight((RelativeLayout) v,null);
			break;
		case R.id.btn_add:
			addLayout();
			break;
		default:
			break;
		}
	}

	// 点击文件夹 展开
	private void changeHeight(RelativeLayout rl,Integer deruation) {
		int index, currentHeight;
		// 判断是否有动画正在运行
		if (isRunning) {
			return;
		}
		rl.setEnabled(false);
		index = layoutList.indexOf(rl);
		currentHeight = rl.getHeight();
		deruation=(deruation==null?200:deruation);
		if (!isOpenList.get(index)) {
			changePixelList.set(index, 360);
			performAnimate(rl, currentHeight,
					currentHeight + changePixelList.get(index),deruation);
			changeMargin(rl,deruation);
			isOpenList.set(index, true);

		} else {
			performAnimate(rl, currentHeight,
					currentHeight - changePixelList.get(index),deruation);
			changeMargin(rl,deruation);
			isOpenList.set(index, false);
		}
	}

	private void closeOpened(RelativeLayout rl) {
		for (int i = 0; i < isOpenList.size(); i++) {
			if (isOpenList.get(i) && layoutList.get(i)!=rl) {
				changeHeight(layoutList.get(i),50);
				nextAnimLayout=rl;
			}
		}

	}

	// 遍历在自己之上的layout 进行margin动画
	private void changeMargin(RelativeLayout rl,Integer deruation) {
		int pos = layoutList.indexOf(rl);
		int changePixel = changePixelList.get(pos);
		deruation=(deruation==null?200:deruation);
		if (!isOpenList.get(pos)) {
			Log.d("login1", "pos: " + pos);
			for (int i = pos + 1; i <= numberOfLayout; i++) {
				RelativeLayout layout = layoutList.get(i);
				int currentMargin = ((RelativeLayout.LayoutParams) layout
						.getLayoutParams()).topMargin;
				performAnimateMargin(layout, currentMargin, currentMargin
						+ changePixel,deruation);
			}
		} else {
			for (int i = pos + 1; i <= numberOfLayout; i++) {
				RelativeLayout layout = layoutList.get(i);
				int currentMargin = ((RelativeLayout.LayoutParams) layout
						.getLayoutParams()).topMargin;
				performAnimateMargin(layout, currentMargin, currentMargin
						- changePixel,deruation);
			}
		}

	}
	
	
	//增加一块文件夹
	private void addLayout(){
		RelativeLayout rl=new RelativeLayout(mainActivity);
		RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,180);
		//lp.topMargin=layoutList.size()*50*3;
		lp.topMargin=((RelativeLayout.LayoutParams)layoutList.get(layoutList.size()-1).getLayoutParams()).topMargin+150;
		rl.setLayoutParams(lp);
		rl.setBackgroundResource(drawableList.get((layoutList.size())%7));
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
				changeHeight((RelativeLayout) v,null);
			}
		});
		
		
		
	}

}
