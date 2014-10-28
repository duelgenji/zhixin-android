package com.qubaopen.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.qubaopen.R;

public class QuceshiTypePickerDialog extends Dialog implements
		View.OnTouchListener, View.OnClickListener {

	private RelativeLayout pickDialog;
	private Context context;

	private float xOfD;
	private float yOfD;
	private float endXOfD;
	private float endYOfD;

	private Button typeAllBtn;
	private Button typeQingganBtn;
	private Button typeQitaBtn;
	private Button typeXingeBtn;
	private Button typeXingzuoBtn;
	private Button typeZhichangBtn;
	private Button typeZhiliBtn;

	private String typeStr;
	private int type;
	private boolean pickTypeOrNot;

	public QuceshiTypePickerDialog(Context context, int theme) {
		super(context, theme);
		setContentView(R.layout.dialog_quceshi_type_picker);

		this.context = context;

		typeAllBtn = (Button) this.findViewById(R.id.quceshiTypeAll);
		typeAllBtn.setOnTouchListener(this);
		typeAllBtn.setOnClickListener(this);

		typeQingganBtn = (Button) this.findViewById(R.id.quceshiTypeQinggan);
		typeQingganBtn.setOnTouchListener(this);
		typeQingganBtn.setOnClickListener(this);

		typeQitaBtn = (Button) this.findViewById(R.id.quceshiTypeQita);
		typeQitaBtn.setOnTouchListener(this);
		typeQitaBtn.setOnClickListener(this);

		typeXingeBtn = (Button) this.findViewById(R.id.quceshiTypeXinge);
		typeXingeBtn.setOnTouchListener(this);
		typeXingeBtn.setOnClickListener(this);

		typeXingzuoBtn = (Button) this.findViewById(R.id.quceshiTypeXingzuo);
		typeXingzuoBtn.setOnTouchListener(this);
		typeXingzuoBtn.setOnClickListener(this);

		typeZhichangBtn = (Button) this.findViewById(R.id.quceshiTypeZhichang);
		typeZhichangBtn.setOnTouchListener(this);
		typeZhichangBtn.setOnClickListener(this);

		typeZhiliBtn = (Button) this.findViewById(R.id.quceshiTypeZhili);
		typeZhiliBtn.setOnTouchListener(this);
		typeZhiliBtn.setOnClickListener(this);

		pickDialog = (RelativeLayout) this.findViewById(R.id.pickDialog);
		pickTypeOrNot = false;
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
				pickTypeOrNot = false;
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

	public int getType() {
		return type;
	}

	public boolean isPickTypeOrNot() {
		return pickTypeOrNot;
	}

	public String getTypeStr() {
		return typeStr;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.quceshiTypeAll:
			type = 0;
			typeStr = context.getString(R.string.quceshi_type_all);
			break;
		case R.id.quceshiTypeQinggan:
			type = 2;
			typeStr = context.getString(R.string.quceshi_type_qinggan);
			break;
		case R.id.quceshiTypeQita:
			type = 6;
			typeStr = context.getString(R.string.quceshi_type_qita);
			break;
		case R.id.quceshiTypeXinge:
			type = 1;
			typeStr = context.getString(R.string.quceshi_type_xinge);
			break;
		case R.id.quceshiTypeXingzuo:
			type = 4;
			typeStr = context.getString(R.string.quceshi_type_xingzuo);
			break;
		case R.id.quceshiTypeZhichang:
			type = 3;
			typeStr = context.getString(R.string.quceshi_type_zhichang);

			break;
		case R.id.quceshiTypeZhili:
			type = 5;
			typeStr = context.getString(R.string.quceshi_type_zhili);
			break;

		default:
			break;

		}
		if (this.isShowing()) {
			pickTypeOrNot = true;
			this.dismiss();
		}
	}

}
