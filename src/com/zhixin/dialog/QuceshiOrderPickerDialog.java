package com.zhixin.dialog;

import com.zhixin.R;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class QuceshiOrderPickerDialog extends Dialog implements
		View.OnTouchListener, View.OnClickListener {

	private RelativeLayout pickDialog;
	private Context context;

	private float xOfD;
	private float yOfD;
	private float endXOfD;
	private float endYOfD;

	private Button orderDefaultBtn;
	private Button orderRenqiBtn;
	private Button orderTimeBtn;
	
	private String orderStr;
	private int order;
	private boolean pickOrderOrNot;

	public QuceshiOrderPickerDialog(Context context, int theme) {
		super(context, theme);
		setContentView(R.layout.dialog_quceshi_order_picker);

		this.context = context;

		orderDefaultBtn = (Button) this.findViewById(R.id.quceshiOrderDefault);
		orderDefaultBtn.setOnClickListener(this);
		orderDefaultBtn.setOnTouchListener(this);

	
		orderRenqiBtn = (Button) this.findViewById(R.id.quceshiOrderRenqi);
		orderRenqiBtn.setOnTouchListener(this);
		orderRenqiBtn.setOnClickListener(this);

		orderTimeBtn = (Button) this.findViewById(R.id.quceshiOrderTime);
		orderTimeBtn.setOnTouchListener(this);
		orderTimeBtn.setOnClickListener(this);

	
		pickDialog = (RelativeLayout) this.findViewById(R.id.pickDialog);
		pickOrderOrNot = false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float currentX = event.getX();
		float currentY = event.getY();
		xOfD = pickDialog.getLeft();
		endXOfD = pickDialog.getRight();
		yOfD = pickDialog.getTop();
		endYOfD = pickDialog.getBottom();

		if (currentX < xOfD || currentX > endXOfD || currentY < yOfD
				|| currentY > endYOfD) {

			if (this.isShowing()) {
				this.dismiss();
			}
		}
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			v.setBackgroundResource(R.drawable.dialog_picker_item_background_pressed);
			break;
		case MotionEvent.ACTION_UP:
			v.setBackgroundDrawable(null);
			break;

		case MotionEvent.ACTION_CANCEL:
			v.setBackgroundDrawable(null);
			break;

		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quceshiOrderDefault:
			order = 0;
			orderStr = context.getString(R.string.quceshi_order_default);
			break;
		case R.id.quceshiOrderRenqi:
			order = 1;
			orderStr = context.getString(R.string.quceshi_order_renqi);
			break;
		case R.id.quceshiOrderTime:
			order = 2;
			orderStr = context.getString(R.string.quceshi_order_time);
			break;
		default:
			break;

		}
		if (this.isShowing()) {
			pickOrderOrNot = true;
			this.dismiss();
		}

	}

	public String getOrderStr() {
		return orderStr;
	}

	public int getOrder() {
		return order;
	}

	public boolean isPickOrderOrNot() {
		return pickOrderOrNot;
	}

}
