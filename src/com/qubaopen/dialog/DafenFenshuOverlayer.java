package com.qubaopen.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qubaopen.R;
import com.qubaopen.customui.DafenItem;

public class DafenFenshuOverlayer extends Dialog implements
		View.OnTouchListener, View.OnClickListener {

	private float xOfD;
	private float yOfD;
	private float endXOfD;
	private float endYOfD;
	
	private Context context;

	private int maxScore;

	private int selectedScore;

	private LinearLayout content;

	private ViewGroup rootView;

	private DafenItem dafenItem;

	private LayoutInflater inflater;

	public DafenFenshuOverlayer(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.setContentView(R.layout.customui_dafen_layout);

		this.setOwnerActivity((Activity) context);
		this.context = context;
			
		this.setCanceledOnTouchOutside(true);
		
		content = (LinearLayout) this.findViewById(R.id.dafenContent);
		rootView = (ViewGroup) this.findViewById(R.id.rootViewOfDafen);
		inflater = LayoutInflater.from(context);
	}

	public void initScore(int maxScore) {
		this.maxScore = maxScore;
		content.removeAllViews();

		for (int i = 0; i < maxScore; i++) {

			FrameLayout wrapper = (FrameLayout) inflater.inflate(
					R.layout.customui_dafen_select_menu_one_item, content,
					false);

			ImageView imageView = (ImageView) wrapper
					.findViewById(R.id.dafenSelectItemImage);
			imageView.setImageBitmap(null);

			TextView textView = (TextView) wrapper
					.findViewById(R.id.dafenSelectTextView);
			textView.setText(String.valueOf(i + 1));

			wrapper.setOnTouchListener(this);
			wrapper.setOnClickListener(this);

			content.addView(wrapper);

		}

		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) rootView
				.getLayoutParams();
		// int oneWidth = (int) TypedValue.applyDimension(
		// TypedValue.COMPLEX_UNIT_DIP, 45, context.getResources()
		// .getDisplayMetrics());

		final float scale = getContext().getResources().getDisplayMetrics().density;
		int oneWidth = (int) (45 * scale + 0.5f);

		params.width = oneWidth * 4;
		rootView.setLayoutParams(params);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView imageView = (ImageView) v
				.findViewById(R.id.dafenSelectItemImage);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			imageView.setImageResource(R.drawable.dafen_selected_background);
			break;
		case MotionEvent.ACTION_UP:
			imageView.setImageBitmap(null);
			break;

		case MotionEvent.ACTION_CANCEL:
			imageView.setImageBitmap(null);
			break;

		}
		return false;
	}

	@Override
	public void onClick(View v) {
		TextView textView = (TextView) v.findViewById(R.id.dafenSelectTextView);
		selectedScore = Integer.parseInt(textView.getText().toString());

		this.hide();
		dafenItem.setScore(selectedScore);
	}

	public int getSelectedScore() {
		return selectedScore;
	}

	public void setMarginAmount(int marginAmount) {
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) rootView
				.getLayoutParams();
		int plusMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 11, context.getResources()
						.getDisplayMetrics());

		params.topMargin = marginAmount + plusMargin;
		rootView.setLayoutParams(params);
	}

	public void setDafenItem(DafenItem dafenItem) {
		this.dafenItem = dafenItem;
	}

	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float currentX = event.getX();
		float currentY = event.getY();
		xOfD = rootView.getLeft();
		endXOfD = rootView.getRight();
		yOfD = rootView.getTop();
		endYOfD = rootView.getBottom();

		if (currentX < xOfD || currentX > endXOfD || currentY < yOfD
				|| currentY > endYOfD) {

			if (this.isShowing()) {
				this.dismiss();

			}
		}
		return false;
	}
	
}
