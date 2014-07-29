package com.zhixin.customui;

import com.zhixin.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TabBarLayout extends RelativeLayout{
	private boolean checked;

	private TextView titleTextView;
	private ImageView imageBackground;

	public TabBarLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TabBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TabBarLayout(Context context) {
		super(context);
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;

		viewFiledOperation();
		if (checked) {
			checkedTureOperation();
		} else {
			checkedFalseOperation();
		}

	}

	private void checkedTureOperation() {
		switch (this.getId()) {
		case R.id.zhijiBtn:
			imageBackground
					.setImageResource(R.drawable.zhiji_default);
			break;
		case R.id.xinliMapBtn:
			imageBackground
					.setImageResource(R.drawable.xinlimap_pressed);
			break;

		case R.id.messageBtn:
			imageBackground
					.setImageResource(R.drawable.message_pressed);
			break;

		case R.id.zhibiBtn:
			imageBackground
					.setImageResource(R.drawable.zhibi_pressed);
			break;
		case R.id.meBtn:
			imageBackground
					.setImageResource(R.drawable.me_pressed);
			break;
		}
		titleTextView.setTextColor(Color.rgb(37, 155, 245));
	}
	
	private void checkedFalseOperation() {
		switch (this.getId()) {
		case R.id.zhijiBtn:
			imageBackground
					.setImageResource(R.drawable.zhiji_pressed);
			break;
		case R.id.xinliMapBtn:
			imageBackground
					.setImageResource(R.drawable.xinlimap_default);
			break;

		case R.id.messageBtn:
			imageBackground
					.setImageResource(R.drawable.message_default);
			break;

		case R.id.zhibiBtn:
			imageBackground
					.setImageResource(R.drawable.zhibi_defalut);
			break;
		case R.id.meBtn:
			imageBackground
					.setImageResource(R.drawable.me_default);
			break;
		}
		titleTextView.setTextColor(Color.rgb(141, 141, 141));
	}
	

	private void viewFiledOperation() {
		switch (this.getId()) {
		case R.id.zhijiBtn:
			imageBackground = (ImageView) this.findViewById(R.id.zhijiImage);
			titleTextView = (TextView) this.findViewById(R.id.zhijiText);
			break;
		case R.id.xinliMapBtn:
			imageBackground = (ImageView) this
					.findViewById(R.id.xinliMapImage);
			titleTextView = (TextView) this.findViewById(R.id.xinliMapText);
			break;

		case R.id.messageBtn:
			imageBackground = (ImageView) this.findViewById(R.id.messageImage);
			titleTextView = (TextView) this.findViewById(R.id.messageText);
			break;

		case R.id.zhibiBtn:
			imageBackground = (ImageView) this
					.findViewById(R.id.zhibiImage);
			titleTextView = (TextView) this.findViewById(R.id.zhibiText);
			break;
		case R.id.meBtn:
			imageBackground = (ImageView) this.findViewById(R.id.meImage);
			titleTextView = (TextView) this.findViewById(R.id.meText);
			break;

		}

	}

}
