package com.zhixin.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> fragmentsList;

	public MyFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public MyFragmentPagerAdapter(FragmentManager fm,
			ArrayList<Fragment> fragments) {
		super(fm);
		this.fragmentsList = fragments;
	}

	@Override
	public int getCount() {
		return fragmentsList.size();
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragmentsList.get(arg0);
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

//	@Override
//	public void setPrimaryItem(ViewGroup container, int position, Object object) {
//		// 知道当前是第几页，但是每次滑动后可能会调用多次
//		// 这个方法是重点
//		super.setPrimaryItem(container, position, object);
//		fragmentsList.get(position).show();
//	}
//
//	@Override
//	public int getItemPosition(Object object) {
//		// 加此方法可以使viewpager可以进行刷新
//		return PagerAdapter.POSITION_NONE;
//	}
//
//	// 使用此方法刷新数据 每次都要NEW一个新的List，不然没有刷新效果
//	// 转至http://blog.sina.com.cn/s/blog_783ede03010173b4.html
//	public void setFragments(ArrayList<Fragment> fragments) {
//		if (this.fragments != null) {
//			FragmentTransaction ft = fm.beginTransaction();
//			for (Fragment f : this.fragments) {
//				ft.remove(f);
//			}
//			ft.commit();
//			ft = null;
//			fm.executePendingTransactions();
//		}
//		this.fragments = fragments;
//		notifyDataSetChanged();
//	}
//
//	@Override
//	public void destroyItem(ViewGroup container, int position, Object object) {
//		// 注释自带的销毁方法防止页面被销毁
//		// 这个方法是重点
//		// super.destroyItem(container, position, object);}
//	}
}
