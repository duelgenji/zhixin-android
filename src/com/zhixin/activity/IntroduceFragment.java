package com.zhixin.activity;

import com.zhixin.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

	public IntroduceFragment(int pos) {
		super();
		this.pos = pos;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		imageClickable = true;
		ViewGroup rootView = null;
//如果不等于3，就出现前面三张介绍图片，否则就滑到“猛戳进入”的页面（通过判断加载不同的xml页面）
		if (pos != 3) {
			rootView = (ViewGroup) inflater.inflate(
					R.layout.fragment_screen_slide_page, container, false);
		} else {
			rootView = (ViewGroup) inflater.inflate(
					R.layout.fragment_screen_slide_page4, container, false);
		}

		mainImage = (ImageView)rootView.findViewById(R.id.mainImage);
		
		int drawableSource = 0;
		switch (pos) {
		case 0:
			drawableSource = R.drawable.introduce_page_0;
			break;
		case 1:
			drawableSource = R.drawable.introduce_page_1;
			break;
		case 2:
			drawableSource = R.drawable.introduce_page_2;
			break;
		case 3:
			drawableSource = R.drawable.introduce_page_3;
			View clickableArea = rootView.findViewById(R.id.clickArea);
			clickableArea.setOnClickListener(this);		
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
//			跳转到登陆的界面
			Intent intent = new Intent(mainActivity, LoginActivity.class);
			startActivity(intent);
			imageClickable = true;
		}
	}

}
