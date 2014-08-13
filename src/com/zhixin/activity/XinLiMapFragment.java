package com.zhixin.activity;

import java.util.ArrayList;

import com.zhixin.R;
import com.zhixin.adapter.MyFragmentPagerAdapter;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class XinLiMapFragment extends Fragment {

	Resources resources;
	private ViewPager mPager;
	private ArrayList<Fragment> fragmentsList;

	private TextView txtPageTitle; 
	private int currIndex = 0;
	private int offset = 0;
	private int position_one;
	public final static int num = 3;
	Fragment home1;
	Fragment home2;
	Fragment home3;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_xinlimap_parent, container,false);
		txtPageTitle = (TextView) view.findViewById(R.id.title_of_the_page);
		
		InitViewPager(view);
		
		return view;
	}



	private void InitViewPager(View parentView) {
		mPager = (ViewPager) parentView.findViewById(R.id.pager);
		fragmentsList = new ArrayList<Fragment>();

		home1 = new XinliMapCharacterFragment();
		home2 = new XinliMapMoodFragment();
		home3 = new XinliMapPersonalFragment();

		fragmentsList.add(home1);
		fragmentsList.add(home2);
		fragmentsList.add(home3);

		mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(),
				fragmentsList));
		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
			
				if(position==0){
					txtPageTitle.setText("性格分析");
				}else if(position==1){
					txtPageTitle.setText("情绪管理");
				}else if(position==2){
					txtPageTitle.setText("个人发展");
				}
				
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	
		mPager.setCurrentItem(0);
		txtPageTitle.setText("性格分析");

	}

	
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};



}
