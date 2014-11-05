package com.qubaopen.activity;

import java.util.ArrayList;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qubaopen.R;
import com.qubaopen.adapter.MyFragmentPagerAdapter;

public class XinLiMapFragment extends Fragment {

	Resources resources;
	private ViewPager mPager;
	private ArrayList<Fragment> fragmentsList;

	private TextView txtPageTitle;
	private ImageView imgFirst;
	private ImageView imgSecond;
	private ImageView imgThird;
	private ImageView imgFour;
	// private int currIndex = 0;
	// private int offset = 0;
	// private int position_one;
	public final static int num = 4;
	Fragment characterFragment;
	Fragment moodFragment;
	Fragment personalFragment;
	Fragment personalFragment1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_xinlimap_parent,
				container, false);
		
		InitViewPager(view);
		return view;
	}

	private void InitViewPager(View parentView) {
		txtPageTitle = (TextView) parentView.findViewById(R.id.title_of_the_page);
		imgFirst = (ImageView) parentView.findViewById(R.id.img_select_point_first);
		imgSecond = (ImageView) parentView.findViewById(R.id.img_select_point_second);
		imgThird = (ImageView) parentView.findViewById(R.id.img_select_point_third);
		imgFour = (ImageView) parentView.findViewById(R.id.img_select_point_four);
//		Log.i("map", imgFirst + "...");
		mPager = (ViewPager) parentView.findViewById(R.id.pager);
		
		fragmentsList = new ArrayList<Fragment>();
		characterFragment = new XinliMapCharactorCardFragment();
		moodFragment = new XinliMapMoodCardFragment();
		personalFragment = new XinliMapPersonalCardFragment();
		personalFragment1 = new XinliMapPersonalCardFragment();

		fragmentsList.add(characterFragment);
		fragmentsList.add(moodFragment);
		fragmentsList.add(personalFragment);
		fragmentsList.add(personalFragment1);
		// 设置预加载个数为0， 为了防止 预加载
		mPager.setOffscreenPageLimit(4);

		mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(),
				fragmentsList));
		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				if (position == 0) {
//					txtPageTitle.setText("性格分析");
					txtPageTitle.setText("性格");
					imgFirst.setImageResource(R.drawable.round_pot_white);
					imgSecond.setImageResource(R.drawable.round_pot_grey);
					imgThird.setImageResource(R.drawable.round_pot_grey);
					imgFour.setImageResource(R.drawable.round_pot_grey);
				} else if (position == 1) {
//					txtPageTitle.setText("情绪管理");
					txtPageTitle.setText("交往");
					imgFirst.setImageResource(R.drawable.round_pot_grey);
					imgSecond.setImageResource(R.drawable.round_pot_white);
					imgThird.setImageResource(R.drawable.round_pot_grey);
					imgFour.setImageResource(R.drawable.round_pot_grey);
				} else if (position == 2) {
//					txtPageTitle.setText("个人发展");
					txtPageTitle.setText("职导");
					imgFirst.setImageResource(R.drawable.round_pot_grey);
					imgSecond.setImageResource(R.drawable.round_pot_grey);
					imgThird.setImageResource(R.drawable.round_pot_white);
					imgFour.setImageResource(R.drawable.round_pot_grey);
				}else if (position == 3) {
//					txtPageTitle.setText("个人发展");
					txtPageTitle.setText("健康");
					imgFirst.setImageResource(R.drawable.round_pot_grey);
					imgSecond.setImageResource(R.drawable.round_pot_grey);
					imgThird.setImageResource(R.drawable.round_pot_grey);
					imgFour.setImageResource(R.drawable.round_pot_white);
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		mPager.setCurrentItem(0);
//		txtPageTitle.setText("性格分析");
		txtPageTitle.setText("性格");
		imgFirst.setImageResource(R.drawable.round_pot_white);
		imgSecond.setImageResource(R.drawable.round_pot_grey);
		imgThird.setImageResource(R.drawable.round_pot_grey);
		imgFour.setImageResource(R.drawable.round_pot_grey);
	}

//	public class MyOnClickListener implements View.OnClickListener {
//		private int index = 0;
//
//		public MyOnClickListener(int i) {
//			index = i;
//		}
//
//		@Override
//		public void onClick(View v) {
//			mPager.setCurrentItem(index);
//		}
//	};

}
