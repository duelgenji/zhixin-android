package com.zhixin.activity;

import com.zhixin.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//四种动物版，已经取消 2014-08-05
public class XinliMapListFragment_animal extends Fragment implements
		View.OnClickListener {

	private Activity mainActivity;

	private TextView txtPageTitle;

	private Intent intent;

	private RelativeLayout layoutPicture;

	private ImageButton leftBorder, rightBorder, bodyBorder, headBorder,
			eyeBorder, leftInside, rightInside, bodyInside, headInside,
			btnSwitchAnimal;

	private boolean canTouch;

	private int tempColor;

	private ImageView activeView;
	
	private int ANIMAL_BIRD=0,ANIMAL_LION=1,ANIMAL_PIG=2,ANIMAL_MONKEY=3;
	
	private int current_animal;

	private Bitmap bitmap_left, bitmap_right, bitmap_body, bitmap_head;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mainActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_xinlimap_animal,
				container, false);
		txtPageTitle = (TextView) view.findViewById(R.id.title_of_the_page);
		txtPageTitle.setText(this.getString(R.string.title_me));

		leftBorder = (ImageButton) view.findViewById(R.id.btn_left_border);
		rightBorder = (ImageButton) view.findViewById(R.id.btn_right_border);
		bodyBorder = (ImageButton) view.findViewById(R.id.btn_body_border);
		headBorder = (ImageButton) view.findViewById(R.id.btn_head_border);
		eyeBorder = (ImageButton) view.findViewById(R.id.btn_eye);
		leftInside = (ImageButton) view.findViewById(R.id.btn_left_inside);
		rightInside = (ImageButton) view.findViewById(R.id.btn_right_inside);
		headInside = (ImageButton) view.findViewById(R.id.btn_head_inside);
		bodyInside = (ImageButton) view.findViewById(R.id.btn_body_inside);

		btnSwitchAnimal = (ImageButton) view
				.findViewById(R.id.btn_switch_animal);
		btnSwitchAnimal.setOnClickListener(this);

		current_animal=ANIMAL_BIRD;
		bitmap_left = ((BitmapDrawable) (leftInside.getDrawable())).getBitmap(); // 設定觸控螢幕監聽
		bitmap_right = ((BitmapDrawable) (rightInside.getDrawable()))
				.getBitmap(); // 設定觸控螢幕監聽
		bitmap_body = ((BitmapDrawable) (bodyInside.getDrawable())).getBitmap(); // 設定觸控螢幕監聽
		bitmap_head = ((BitmapDrawable) (headInside.getDrawable())).getBitmap(); // 設定觸控螢幕監聽

		canTouch = true;
		tempColor = 0;
		eyeBorder.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN && canTouch) {
					canTouch = false;
					int point_x = (int) event.getX();
					int point_y = (int) event.getY();
					if (point_x < bitmap_left.getWidth()
							&& point_y < bitmap_left.getHeight()
							&& bitmap_left.getPixel(point_x, point_y) != 0) {
						activeView = leftInside;
						Log.i("login", "left");
						// leftBorder.getDrawable().setColorFilter(new
						// LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
						tempColor = bitmap_left.getPixel((int) (event.getX()),
								((int) event.getY()));
						activeView.getDrawable().setColorFilter(0xffff0000,
								PorterDuff.Mode.SRC_IN);
						// Toast.makeText(mainActivity, "点到l", 3).show();
						return true;
					} else if (point_x < bitmap_right.getWidth()
							&& point_y < bitmap_right.getHeight()
							&& bitmap_right.getPixel(point_x, point_y) != 0) {
						Log.i("login", "right");
						activeView = rightInside;
						tempColor = bitmap_right.getPixel((int) (event.getX()),
								((int) event.getY()));
						activeView.getDrawable().setColorFilter(0xffff0000,
								PorterDuff.Mode.SRC_IN);
						// Toast.makeText(mainActivity, "点到r", 3).show();
						return true;
					} else if (point_x < bitmap_body.getWidth()
							&& point_y < bitmap_body.getHeight()
							&& bitmap_body.getPixel(point_x, point_y) != 0) {
						Log.i("login", "body");
						activeView = bodyInside;
						tempColor = bitmap_right.getPixel((int) (event.getX()),
								((int) event.getY()));
						activeView.getDrawable().setColorFilter(0xffff0000,
								PorterDuff.Mode.SRC_IN);
						// Toast.makeText(mainActivity, "点到r", 3).show();
						return true;
					} else if (point_x < bitmap_head.getWidth()
							&& point_y < bitmap_head.getHeight()
							&& bitmap_head.getPixel(point_x, point_y) != 0) {
						Log.i("login", "head");
						activeView = headInside;
						tempColor = bitmap_right.getPixel((int) (event.getX()),
								((int) event.getY()));
						activeView.getDrawable().setColorFilter(0xffff0000,
								PorterDuff.Mode.SRC_IN);
						// Toast.makeText(mainActivity, "点到r", 3).show();
						return true;
					} else {
						Log.i("login", "mei");
						// Toast.makeText(mainActivity, "nothing", 3).show();
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					canTouch = true;
					if (activeView != null) {
						activeView.getDrawable().setColorFilter(tempColor,
								PorterDuff.Mode.DST);
						activeView = null;
					}

				}

				return false;
			};
		});

		return view;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_switch_animal:
			this.swtichAnimal();
			Toast.makeText(mainActivity, "switch", 3).show();
			break;
		default:
			break;
		}
	}

	private void swtichAnimal() {
		
		if(current_animal==ANIMAL_BIRD){
			leftBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_lion_left_border));
			rightBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_lion_right_border));
			bodyBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_lion_body_border));
			headBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_lion_head_border));
			eyeBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_lion_eye_border));
			leftInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_lion_left_inside));
			rightInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_lion_right_inside));
			headInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_lion_head_inside));
			bodyInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_lion_body_inside));
			current_animal=ANIMAL_LION;
		}else if(current_animal==ANIMAL_LION){
			leftBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_pig_left_border));
			rightBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_pig_right_border));
			bodyBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_pig_body_border));
			headBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_pig_head_border));
			eyeBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_pig_eye_border));
			leftInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_pig_left_inside));
			rightInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_pig_right_inside));
			headInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_pig_head_inside));
			bodyInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_pig_body_inside));
			current_animal=ANIMAL_PIG;
		}else if(current_animal==ANIMAL_PIG){
		
			rightBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_monkey_right_border));
			bodyBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_monkey_body_border));
			headBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_monkey_head_border));
			eyeBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_monkey_eye_border));
			rightInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_monkey_right_inside));
			headInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_monkey_head_inside));
			bodyInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_monkey_body_inside));
			leftBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_monkey_left_border));
			leftInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_monkey_left_inside));
			leftBorder.bringToFront();
			leftInside.bringToFront();
			eyeBorder.bringToFront();
			current_animal=ANIMAL_MONKEY;
		}else if(current_animal==ANIMAL_MONKEY){
			leftBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_bird_left_border));
			rightBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_bird_right_border));
			bodyBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_bird_body_border));
			headBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_bird_head_border));
			eyeBorder.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_bird_eye_border));
			leftInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_bird_left_inside));
			rightInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_bird_right_inside));
			headInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_bird_head_inside));
			bodyInside.setImageDrawable(getResources().getDrawable(
					R.drawable.psychology_map_bird_body_inside));
			current_animal=ANIMAL_BIRD;
		}
		
		
		bitmap_left = ((BitmapDrawable) (leftInside.getDrawable())).getBitmap(); // 設定觸控螢幕監聽
		bitmap_right = ((BitmapDrawable) (rightInside.getDrawable())).getBitmap(); // 設定觸控螢幕監聽
		bitmap_body = ((BitmapDrawable) (bodyInside.getDrawable())).getBitmap(); // 設定觸控螢幕監聽
		bitmap_head = ((BitmapDrawable) (headInside.getDrawable())).getBitmap(); // 設定觸控螢幕監聽
	}

}
