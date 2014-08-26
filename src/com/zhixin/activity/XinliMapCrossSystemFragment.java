package com.zhixin.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhixin.R;
import com.zhixin.settings.CrossSystemMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//四种动物版，已经取消 2014-08-05
public class XinliMapCrossSystemFragment extends Fragment implements
		View.OnClickListener {

	private Activity mainActivity;

	private View _view;

	private TextView txtPageTitle;

	private Intent intent;

	private RelativeLayout layoutPicture;

	private ImageView activeView;

	private ImageButton btnSwitch;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mainActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_xinlimap_cross_system,
				container, false);
		_view = view;
		txtPageTitle = (TextView) view.findViewById(R.id.title_of_the_page);

		layoutPicture = (RelativeLayout) view.findViewById(R.id.layout_picture);

		btnSwitch = (ImageButton) view.findViewById(R.id.btn_switch);
		btnSwitch.setOnClickListener(this);

		return view;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_switch:
			this.swtichCross();
			break;
		default:
			break;
		}
	}

	private void swtichCross() {

		Toast.makeText(mainActivity, "switch", 3).show();
		Integer count = layoutPicture.getChildCount();
		if (count > 2) {
			layoutPicture.removeViewsInLayout(2, count - 2);
		}

		int postion = (int) (Math.random() * 32 + 1);
		int number = 3;
		String color = "r";
		ImageView newView;
		while (number > 0) {
			newView = new ImageView(mainActivity);
			int resource = CrossSystemMap.getErrMessage("r1");
			int p=postion;
			switch (number) {
			case 3:
				resource = CrossSystemMap.getErrMessage(color + p);
				break;
			case 2:
				//右边
				if(postion==32){
					p=1;
				}else{
					p++;
				}
				resource = CrossSystemMap.getErrMessage(color + p);
				break;
			case 1:
				//左边
				if(postion==1){
					p=32;
				}else{
					p--;
				}
				resource = CrossSystemMap.getErrMessage(color + p);
				break;
			default:
				break;
			}
			Bitmap bm = BitmapFactory.decodeResource(getResources(), resource);
			newView.setImageBitmap(bm);
//			((BitmapDrawable)newView.getDrawable()).getBitmap().recycle();
			
			//newView.setImageDrawable(getResources().getDrawable(resource));
			newView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
			newView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			layoutPicture.addView(newView);
			
			number--;
			color = "y";
		}

	}

}
