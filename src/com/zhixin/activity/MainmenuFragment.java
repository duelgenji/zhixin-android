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
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhixin.R;
import com.zhixin.datasynservice.MainMenuService;
import com.zhixin.settings.MyApplication;
import com.zhixin.utils.AnimationUtils;

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

	private RelativeLayout layoutPickMood;

	private RelativeLayout layoutMoodSwitch;

	private ImageView imgMoodClose;
	private ImageView imgMoodArrow;
	private ImageView imgMoodPanel;
	private ImageView imgMoodBackground;
	
	private RelativeLayout layoutMoodFace1,layoutMoodFace2,layoutMoodFace3,layoutMoodFace4,layoutMoodFace5,layoutMoodFace6;
	
	Toast toast;
	
	private boolean isMoodOpen=false;
	private boolean isMoodFirst=true;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.main_activity, container,
				false);
		_this=this;
		toast=new Toast(mainActivity);
		imageLoader = ImageLoader.getInstance();
		imageOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();
		//心里自测的动画
		ll_XLZC = (LinearLayout) view.findViewById(R.id.ll_XLZC);
		ll_XLZC.setOnClickListener(this);
		img1 = (ImageView) view.findViewById(R.id.menu_heart_beat);
		((AnimationDrawable) img1.getDrawable()).stop();
		((AnimationDrawable) img1.getDrawable()).selectDrawable(0);
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

		layoutPickMood =(RelativeLayout) view.findViewById(R.id.layout_pick_mood);
		layoutMoodSwitch =(RelativeLayout) view.findViewById(R.id.layout_mood_switch);
		imgMoodClose =(ImageView) view.findViewById(R.id.img_mood_close);
		imgMoodClose.setOnClickListener(this);
		imgMoodBackground=(ImageView) view.findViewById(R.id.img_mood_background);
		imgMoodBackground.setOnClickListener(this);
		imgMoodPanel=(ImageView) view.findViewById(R.id.img_mood_switch_panel);
		imgMoodPanel.setOnClickListener(this);
		imgMoodArrow=(ImageView) view.findViewById(R.id.img_mood_switch_arrow);
		AnimationUtils.startImgBackGround(imgMoodArrow);

		layoutMoodFace1=(RelativeLayout) view.findViewById(R.id.layout_mood_face_1);
		layoutMoodFace2=(RelativeLayout) view.findViewById(R.id.layout_mood_face_2);
		layoutMoodFace3=(RelativeLayout) view.findViewById(R.id.layout_mood_face_3);
		layoutMoodFace4=(RelativeLayout) view.findViewById(R.id.layout_mood_face_4);
		layoutMoodFace5=(RelativeLayout) view.findViewById(R.id.layout_mood_face_5);
		layoutMoodFace6=(RelativeLayout) view.findViewById(R.id.layout_mood_face_6);
		layoutMoodFace1.setOnClickListener(this);
		layoutMoodFace2.setOnClickListener(this);
		layoutMoodFace3.setOnClickListener(this);
		layoutMoodFace4.setOnClickListener(this);
		layoutMoodFace5.setOnClickListener(this);
		layoutMoodFace6.setOnClickListener(this);
		

		mainMenuService = new MainMenuService(MyApplication.getAppContext());
//		new LoadDataTask().execute();
//		setIndexImage(0);

		return view;
	}
	
	
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
	private MainmenuFragment _this;
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
			intent = new Intent(mainActivity, InterestListActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		case R.id.ll_XLZC:
			final AnimationDrawable hbDrawable = (AnimationDrawable) img1.getDrawable();
			hbDrawable.run();
			 Handler handler = new Handler();  
		        handler.postDelayed(new Runnable() {  
		            public void run() {  
		               //此处调用第二个动画播放方法     
		            	Intent intent = new Intent(mainActivity,SelfListActivity.class);
		    			startActivity(intent);
		            	hbDrawable.selectDrawable(0);
		            }  
		        }, 500);  
		
			v.setEnabled(true);
			break;
			
		case R.id.ll_XQHSZ:
			InitialAnimation();
			img2.startAnimation(animation3);
			v.setEnabled(true);
			break;
		case R.id.ll_QWCS:
			InitialAnimation();
			img3.startAnimation(animation4);
//			intent = new Intent(mainActivity,InterestListActivity.class);
//			startActivity(intent);
			v.setEnabled(true);
			break;
		case R.id.ll_XLQZ:
			InitialAnimation();
			img4.startAnimation(animation5);
			v.setEnabled(true);
			break;
		case R.id.img_mood_switch_panel:
			if(isMoodFirst){
				isMoodFirst=false;
				imgMoodPanel.setBackgroundResource(R.drawable.today_mood_panel_default);
				AnimationUtils.performAnimateMarginBottom(layoutMoodSwitch, 0,-90, 333);
			}
			
			if(!isMoodOpen){
				isMoodOpen=true;
				imgMoodArrow.setBackgroundResource(R.drawable.today_mood_arrow_down);
				AnimationUtils.startImgBackGround(imgMoodArrow);
				AnimationUtils.performAnimateMarginTop(layoutPickMood, 1500,810, 500);
			}
			else{
				isMoodOpen=false;
				imgMoodArrow.setBackgroundResource(R.drawable.today_mood_arrow_up);
				//AnimationUtils.startImgBackGround(imgMoodArrow);
				AnimationUtils.performAnimateMarginTop(layoutPickMood, 810,1500, 500);
				imgMoodPanel.setEnabled(true);
			} 
			break;
		case R.id.img_mood_close:
			isMoodOpen=false;
			imgMoodArrow.setBackgroundResource(R.drawable.today_mood_arrow_up);
			//AnimationUtils.startImgBackGround(imgMoodArrow);
			AnimationUtils.performAnimateMarginTop(layoutPickMood, 810,1500, 500);
			v.setEnabled(true);
			imgMoodPanel.setEnabled(true);
			break;
		case R.id.img_mood_background:
			v.setEnabled(true);
			break;
		case R.id.layout_mood_face_1:
			showToast("face1");
			v.setEnabled(true);
			break;
		case R.id.layout_mood_face_2:
			showToast("face2");
			v.setEnabled(true);
			break;
		case R.id.layout_mood_face_3:
			showToast("face3");
			v.setEnabled(true);
			break;
		case R.id.layout_mood_face_4:
			showToast("face4");
			v.setEnabled(true);
			break;
		case R.id.layout_mood_face_5:
			showToast("face5");
			v.setEnabled(true);
			break;
		case R.id.layout_mood_face_6:
			showToast("face6");
			v.setEnabled(true);
			break;
		default:
			v.setEnabled(true);
			break;
		}
	}

	private void InitialAnimation() {
		animation3 = new RotateAnimation(0.0f, -20.0f,30,43);
		//旋转起始角度；终止角度；圆心的X坐标；圆心的Y坐标
		//animation4 = new RotateAnimation(0.0f, -90.0f, 0, 0);
		animation4 = new RotateAnimation(0, 180,Animation.RELATIVE_TO_SELF, 
				0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		LinearInterpolator lin = new LinearInterpolator();  
		animation4.setInterpolator(lin); 
		animation4.setDuration(300);
		animation4.setAnimationListener(new AnimationListener() {  
		      
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
		    	Intent intent = new Intent(mainActivity,InterestListActivity.class);
				startActivity(intent); 
		    }  
		});  
		
		
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
	
	
    private void showToast(String content) {
    	toast.cancel();
    	toast=Toast.makeText(mainActivity, content, Toast.LENGTH_SHORT);
		toast.show();
    }

}
