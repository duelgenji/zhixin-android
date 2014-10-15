package com.zhixin.activity;

import com.nineoldandroids.animation.AnimatorSet;
import com.zhixin.R;
import com.zhixin.settings.PhoneHelper;
import com.zhixin.utils.AnimationUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * @author Administrator
 * 
 */
public class IntroduceFragment extends Fragment implements View.OnClickListener {

	private int pos;

	private boolean imageClickable;

	private Activity mainActivity;

	private ImageView mainImage;

	private boolean isEasterEgg = false;
	

	public IntroduceFragment(int pos) {
		super();
		this.pos = pos;
	}

	public IntroduceFragment(int pos, boolean isEasterEgg) {
		super();
		this.pos = pos;
		this.isEasterEgg = isEasterEgg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		imageClickable = true;
		ViewGroup rootView = null;
		// 如果不等于3，就出现前面三张介绍图片，否则就滑到“猛戳进入”的页面（通过判断加载不同的xml页面）
		if (pos == 0) {
			rootView = (ViewGroup) inflater.inflate(
					R.layout.fragment_screen_slide_page1, container, false);
			moveLittleMan(rootView);
		} else if (pos == 1) {
			rootView = (ViewGroup) inflater.inflate(
					R.layout.fragment_screen_slide_page, container, false);
		} else if (pos == 2) {
			rootView = (ViewGroup) inflater.inflate(
					R.layout.fragment_screen_slide_page3, container, false);
			threeToOne(rootView);
		} else if (pos == 3) {
			rootView = (ViewGroup) inflater.inflate(
					R.layout.fragment_screen_slide_page4, container, false);
			touchHeart(rootView);
		}

		mainImage = (ImageView) rootView.findViewById(R.id.mainImage);

		int drawableSource = 0;
		switch (pos) {
		case 0:
			drawableSource = R.drawable.introduce_know_heart_01;
			break;
		case 1:
			drawableSource = R.drawable.introduce_know_heart_02;
			break;
		case 2:
			drawableSource = R.drawable.introduce_know_heart_03;
			break;
		case 3:
			drawableSource = R.drawable.introduce_know_heart_04;
			// View clickableArea = rootView.findViewById(R.id.clickArea);
			// clickableArea.setOnClickListener(this);
			break;
		default:
			try {
				throw new Exception();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		mainImage.setImageResource(drawableSource);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mainActivity = activity;

	}

	@Override
	public void onClick(View v) {
		if (imageClickable) {
			imageClickable = false;
			// 跳转到登陆的界面
			Intent intent = new Intent(mainActivity, LoginActivity.class);
			startActivity(intent);
			imageClickable = true;
		}
	}

	// 第一页 小人
	private void moveLittleMan(ViewGroup rootView) {

		final ImageView littleMan = (ImageView) rootView
				.findViewById(R.id.imgLittleMan);

		final int h = PhoneHelper.getPhoneHeight();
		final int w = PhoneHelper.getPhoneWIDTH();

		// 第一段

		AnimationSet animSet = new AnimationSet(true);
		Animation animation = new TranslateAnimation(
				TranslateAnimation.RELATIVE_TO_SELF, (float) (w * 0.45),
				TranslateAnimation.RELATIVE_TO_SELF, (float) (h * 0.12));
		animation.setDuration(800);
		animation.setInterpolator(new DecelerateInterpolator());
		animSet.addAnimation(animation);

		// 第二段
		Animation animation2 = new TranslateAnimation(
				TranslateAnimation.RELATIVE_TO_SELF, -(float) (w * 0.25),
				TranslateAnimation.RELATIVE_TO_SELF, (float) (h * 0.15));
		animation2.setStartOffset(900);
		animation2.setDuration(800);
		animation2.setInterpolator(new DecelerateInterpolator());
		animSet.addAnimation(animation2);

		// 第三段
		animation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF,
				(float) (w * 0.45), TranslateAnimation.RELATIVE_TO_SELF,
				(float) (h * 0.12));
		animation.setStartOffset(1800);
		animation.setDuration(800);
		animation.setInterpolator(new DecelerateInterpolator());
		animSet.addAnimation(animation);

		littleMan.setAnimation(animSet);
		animSet.setFillAfter(true);
		animSet.start();

	}

	// 第三页 三位一体
	private void threeToOne(ViewGroup rootView) {
		ImageView icon1 = (ImageView) rootView.findViewById(R.id.imgIcon1);
		ImageView icon2 = (ImageView) rootView.findViewById(R.id.imgIcon2);
		ImageView icon3 = (ImageView) rootView.findViewById(R.id.imgIcon3);
		ImageView icon4 = (ImageView) rootView.findViewById(R.id.imgIcon4);

		int h = PhoneHelper.getPhoneHeight();
		int w = PhoneHelper.getPhoneWIDTH();

		// 第一图
		AnimationSet animSet = new AnimationSet(true);
		Animation animation = new TranslateAnimation(0, (float) (w * 0.3), 0,
				(float) (0.16 * h));
		animation.setDuration(500);
		animation.setInterpolator(new DecelerateInterpolator());
		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setStartOffset(550);
		alphaAnimation.setDuration(500);
		animSet.addAnimation(animation);
		animSet.addAnimation(alphaAnimation);
		animSet.setFillAfter(true);
		icon1.setAnimation(animSet);

		// 第二图
		Animation animation2 = new TranslateAnimation(0, -(float) (w * 0.28),
				0, (float) (0.16 * h));
		animation2.setDuration(500);
		animation2.setFillAfter(true);
		animation2.setInterpolator(new DecelerateInterpolator());
		AnimationSet animSet2 = new AnimationSet(true);
		animSet2.addAnimation(animation2);
		animSet2.addAnimation(alphaAnimation);
		animSet2.setFillAfter(true);
		icon2.setAnimation(animSet2);

		// 第三图
		Animation animation3 = new TranslateAnimation(0, 0, 0, -100);
		animation3.setDuration(500);
		animation3.setFillAfter(true);
		animation3.setInterpolator(new DecelerateInterpolator());
		AnimationSet animSet3 = new AnimationSet(true);
		animSet3.addAnimation(animation3);
		animSet3.addAnimation(alphaAnimation);
		animSet3.setFillAfter(true);
		icon3.setAnimation(animSet3);

		animSet.start();
		animSet2.start();
		animSet3.start();

		icon4.setVisibility(ImageView.VISIBLE);
		AlphaAnimation alphaAnimation2 = new AlphaAnimation(0, 1);
		alphaAnimation2.setStartOffset(1100);
		alphaAnimation2.setDuration(500);
		icon4.startAnimation(alphaAnimation2);

	}

	// 第四页 移动 心
	private void touchHeart(final ViewGroup rootView) {
		ImageView littleHeart = (ImageView) rootView
				.findViewById(R.id.imgHeart);

		final ImageView imgArrow = (ImageView) rootView
				.findViewById(R.id.imgArrow);

		imgArrow.post(new Runnable() {
			@Override
			public void run() {
				// imgArrow.setBackgroundDrawable(getResources().getDrawable((R.drawable.introduce_know_heart_left_arrow)));
				AnimationUtils.startImgBackGround(imgArrow);
			}
		});

		littleHeart.setClickable(true);

		littleHeart.setOnTouchListener(new View.OnTouchListener() {
			int lastX;
			int lastY;
			int screenWidth = PhoneHelper.getPhoneWIDTH();

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				rootView.getParent().requestDisallowInterceptTouchEvent(true);
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();
					// Log.i("phone", "TouchX:" + lastX);
					// Log.i("phone", "TouchY:" + lastY);
					break;
				case MotionEvent.ACTION_MOVE:

					int dx = (int) event.getRawX() - lastX;
					int dy = (int) event.getRawY() - lastY;

					// Log.i("phone", "dx:"+dx+" , dy:"+dy);
					int left = v.getLeft() + dx;
					int right = v.getRight() + dx;
					// Log.i("phone",
					// "getLeft:"+v.getLeft()+" , getRight:"+v.getRight());

					if (left < 0) {
						left = 0;
						right = left + v.getWidth();
					}
					if (right > screenWidth) {
						right = screenWidth;
						left = right - v.getWidth();
					}

					v.layout(left, v.getTop(), right, v.getBottom());
					lastX = (int) event.getRawX();
					break;
				case MotionEvent.ACTION_UP:
					autoMoveHeart(v);
					break;
				}
				return true;
			}
		});
	}

	private void autoMoveHeart(final View v) {
		int left = v.getLeft();

		int min = (int) (PhoneHelper.getPhoneWIDTH() * 0.25); // 1080的 270
		int max = (int) (PhoneHelper.getPhoneWIDTH() * 0.37); // 1080的 400
		int mid = (int) (PhoneHelper.getPhoneWIDTH() * 0.31); // 1080的 334

		if (min <= left && left <= max) {
			// Log.i("phone", "upX:" + v.getLeft());
			Log.i("phone", "自动距离");
			Animation animation = new TranslateAnimation(0, mid - left, 0, 0);
			animation.setDuration(500);
			animation.setFillAfter(true);
			animation.setFillEnabled(true);
			animation.setInterpolator(new DecelerateInterpolator());
			v.startAnimation(animation);
			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					if (isEasterEgg) {
						mainActivity.onBackPressed();
						return;
					}
					Intent intent = new Intent(mainActivity,
							LoginActivity.class);
					startActivity(intent);
				}
			});

		}
	}

}
