package com.zhixin.activity;

import com.zhixin.R;
import com.zhixin.settings.SettingValues;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

/**
 * @author Administrator
 *第一页
 */
public class FristInIntroduceActivity extends FragmentActivity {
	/**页面数*/
	private static final int NUM_PAGES = 4;

	public static final String IS_EASTER_EGG = "is_easter_egg";

	private ViewPager mPager;

	private PagerAdapter mPagerAdapter;
	/**上下文*/
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		context = this;

		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.pager);
		//设置预加载个数为0， 为了防止 第三张图 预加载就开始进行动画
		mPager.setOffscreenPageLimit(0);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		
		mPager.setAdapter(mPagerAdapter);

//		new Thread(runnalbe).start();
		new Thread(
				
				new Runnable() {
					public void run() {

//						写入共享偏好文件
//						要向一个共享偏好文件中写入，就要通过调用SharedPreferences上的edit()方法来创建一个SharedPreferences.Editor对象。
						SharedPreferences sharedPref = context.getSharedPreferences(
								SettingValues.FILE_NAME_SETTINGS, Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = sharedPref.edit();
//						String APP_FIRSTTIME_IN = "isFirstIn";
						editor.putBoolean(SettingValues.APP_FIRSTTIME_IN, false);
						editor.commit();
					
					}
				}
				).start();
	}
//内部类的适配器
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if(position==3){
				boolean isEasterEgg=getIntent().getBooleanExtra(IS_EASTER_EGG, false);
				return new IntroduceFragment(position,isEasterEgg);
			}
			return new IntroduceFragment(position);

		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}

		
	}

//	private Runnable runnalbe = new Runnable() {
//
//		@Override
//		public void run() {
////			写入共享偏好文件
////			要向一个共享偏好文件中写入，就要通过调用SharedPreferences上的edit()方法来创建一个SharedPreferences.Editor对象。
//			SharedPreferences sharedPref = context.getSharedPreferences(
//					SettingValues.FILE_NAME_SETTINGS, Context.MODE_PRIVATE);
//			SharedPreferences.Editor editor = sharedPref.edit();
////			String APP_FIRSTTIME_IN = "isFirstIn";
//			editor.putBoolean(SettingValues.APP_FIRSTTIME_IN, false);
//			editor.commit();
//		}
//	};

}
