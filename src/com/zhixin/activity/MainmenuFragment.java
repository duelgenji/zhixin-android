package com.zhixin.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhixin.R;
import com.zhixin.datasynservice.MainMenuService;
import com.zhixin.settings.MyApplication;

public class MainmenuFragment extends Fragment implements
LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener{

	/***/
	
	/***/
//	private FragmentManager fm;
	/**心理自测按钮*/
	private LinearLayout ll_XLZC;
	/**心情回收站按钮*/
	private LinearLayout ll_XQHSZ;
	/**趣味测试按钮*/
	private LinearLayout ll_QWCS;
	/**心理求助按钮*/
	
	private LinearLayout ll_XLQZ;
	/**调研测试按钮*/
	private LinearLayout ll_DYCS;
	
	private Animation animation, animation1, animation2,animation3,animation4,animation5;
	private ImageView img1,img2,img3,img4, img5;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				img5.setAnimation(animation1);
				break;
			case 1:
				img5.setAnimation(animation2);
				break;

			}
		};
	};
	
	
	
	/**主菜单service*/
	private MainMenuService mainMenuService;
	/***/
//	private Integer prevSetImage;

	/**动画*/
	AnimationDrawable progressAnimation;
	/***/
	private ImageLoader imageLoader;
	/***/
	private DisplayImageOptions imageOptions;
	private Activity mainActivity;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mainActivity = activity;
	}
	
	@Override
	public void onClick(View v) {
		v.setEnabled(false);
		Intent intent;
		switch (v.getId()) {
		case R.id.ll_DYCS:
			InitialAnimation();
			// img1.startAnimation(animation);
			img5.startAnimation(animation);
			break;
		case R.id.ll_XLZC:
			AnimationDrawable hbDrawable = (AnimationDrawable) img1.getDrawable();
			hbDrawable.start();
			intent = new Intent(mainActivity,XinliziceListActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		case R.id.ll_XQHSZ:
			InitialAnimation();
			img2.startAnimation(animation3);
			break;
		case R.id.ll_QWCS:
			InitialAnimation();
			img3.startAnimation(animation4);
			intent = new Intent(mainActivity,QuceshiListActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		case R.id.ll_XLQZ:
			InitialAnimation();
			img4.startAnimation(animation5);
		default:
			break;
		}
	}

	private void InitialAnimation() {
		animation3 = new RotateAnimation(0.0f, -20.0f,30,43);
		//旋转起始角度；终止角度；圆心的X坐标；圆心的Y坐标
		animation4 = new RotateAnimation(0.0f, -90.0f, 0, 0);
		 animation5 = new ScaleAnimation(1f, 1.2f, 1f, 1.2f,
		 Animation.RELATIVE_TO_SELF, 0.8f, Animation.RELATIVE_TO_SELF,
		 0.8f);
		animation = new TranslateAnimation(0, -15, 0, 15);
		animation.setDuration(500);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Message msg = Message.obtain();
				msg.what = 0;
				handler.sendMessage(msg);
			}
		});
		animation1 = new TranslateAnimation(-15, 15, 15, 15);
		animation1.setDuration(500);
		animation1.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				Message msg = Message.obtain();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		});
		animation2 = new TranslateAnimation(15, 0, 15, 0);
		animation2.setDuration(500);
	
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_activity, container,
				false);

		imageLoader = ImageLoader.getInstance();
		imageOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();
		//心里自测的动画
		ll_XLZC = (LinearLayout) view.findViewById(R.id.ll_XLZC);
		ll_XLZC.setOnClickListener(this);
		img1 = (ImageView) view.findViewById(R.id.menu_heart_beat);
		//心情回收站的动画
		ll_XQHSZ = (LinearLayout) view.findViewById(R.id.ll_XQHSZ);
		ll_XQHSZ.setOnClickListener(this);
		img2 = (ImageView) view.findViewById(R.id.img_huishouzhangaizi);
		//趣味测试的动画
		img3 = (ImageView) view.findViewById(R.id.img_QWCS);
		ll_QWCS =  (LinearLayout) view.findViewById(R.id.ll_QWCS);
		ll_QWCS.setOnClickListener(this);
		//心理求助的动画
		ll_XLQZ= (LinearLayout) view.findViewById(R.id.ll_XLQZ);
		ll_XLQZ.setOnClickListener(this);
		img4 = (ImageView) view.findViewById(R.id.img_xin);
		//调研测试
		ll_DYCS = (LinearLayout) view.findViewById(R.id.ll_DYCS);
		ll_DYCS.setOnClickListener(this);
		img5 = (ImageView) view.findViewById(R.id.img_dycs);

		mainMenuService = new MainMenuService(MyApplication.getAppContext());
//		new LoadDataTask().execute();
//		setIndexImage(0);

		return view;
	}
}
