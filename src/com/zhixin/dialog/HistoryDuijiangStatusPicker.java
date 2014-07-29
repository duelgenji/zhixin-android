package com.zhixin.dialog;

import com.zhixin.R;

import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class HistoryDuijiangStatusPicker extends Dialog implements
		View.OnTouchListener, View.OnClickListener {

	private RelativeLayout pickDialog;
	private View rootView;
	private Context context;

	private float xOfD;
	private float yOfD;
	private float endXOfD;
	private float endYOfD;

	private Button choujiangzhongBtn;
	private Button weizhongjiangBtn;
	private Button dailingjiangBtn;
	private Button fahuozhongBtn;
	private Button chankanjiangpingBtn;
	private Button daiquerenBtn;
	private Button yiquerenBtn;
	private Button chulizhongBtn;
	private Button allStatusBtn;

	private String statusStr;
	private int status;
	private boolean pickOrderOrNot;

	public HistoryDuijiangStatusPicker(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		setContentView(R.layout.dialog_history_duijiang_status_picker);

		this.context = context;

		rootView = this.findViewById(R.id.rootViewOfStatusPicker);

		choujiangzhongBtn = (Button) this.findViewById(R.id.choujiangzhongBtn);
		choujiangzhongBtn.setOnClickListener(this);
		choujiangzhongBtn.setOnTouchListener(this);

		weizhongjiangBtn = (Button) this.findViewById(R.id.weizhongjiangBtn);
		weizhongjiangBtn.setOnTouchListener(this);
		weizhongjiangBtn.setOnClickListener(this);

		dailingjiangBtn = (Button) this.findViewById(R.id.dailingjiangBtn);
		dailingjiangBtn.setOnTouchListener(this);
		dailingjiangBtn.setOnClickListener(this);

		fahuozhongBtn = (Button) this.findViewById(R.id.fahuozhongBtn);
		fahuozhongBtn.setOnTouchListener(this);
		fahuozhongBtn.setOnClickListener(this);

		chankanjiangpingBtn = (Button) this
				.findViewById(R.id.chakanjiangpingBtn);
		chankanjiangpingBtn.setOnTouchListener(this);
		chankanjiangpingBtn.setOnClickListener(this);

		daiquerenBtn = (Button) this.findViewById(R.id.daiquerenBtn);
		daiquerenBtn.setOnClickListener(this);
		daiquerenBtn.setOnTouchListener(this);

		yiquerenBtn = (Button) this.findViewById(R.id.yiquerenBtn);
		yiquerenBtn.setOnClickListener(this);
		yiquerenBtn.setOnTouchListener(this);

		chulizhongBtn = (Button) this.findViewById(R.id.chulizhongBtn);
		chulizhongBtn.setOnClickListener(this);
		chulizhongBtn.setOnTouchListener(this);
		
		allStatusBtn = (Button)this.findViewById(R.id.allStatusBtn);
		allStatusBtn.setOnClickListener(this);
		allStatusBtn.setOnTouchListener(this);

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
		case R.id.choujiangzhongBtn:
			status = 0;
			statusStr = context
					.getString(R.string.duijiang_history_choujiangzhong);
			break;
		case R.id.weizhongjiangBtn:
			status = 1;
			statusStr = context
					.getString(R.string.duijiang_history_weizhongjiang);
			break;
		case R.id.dailingjiangBtn:
			status = 2;
			statusStr = context
					.getString(R.string.duijiang_history_dailingjiang);
			break;
		case R.id.fahuozhongBtn:
			status = 3;
			statusStr = context.getString(R.string.duijiang_history_fahuozhong);
			break;
		case R.id.daiquerenBtn:
			status = 4;
			statusStr = context.getString(R.string.duijiang_history_daiqueren);
			break;
		case R.id.yiquerenBtn:
			status = 5;
			statusStr = context.getString(R.string.duijiang_history_yiqueren);
			break;
		case R.id.chulizhongBtn:
			status = 6;
			statusStr = context.getString(R.string.duijiang_history_chulizhong);
			break;
		case R.id.chakanjiangpingBtn:
			status = 7;
			statusStr = context
					.getString(R.string.duijiang_history_chakanjiangping);
			break;
		case R.id.allStatusBtn:
			status = 8;
			statusStr = context.getString(R.string.duijiang_history_all_status);
			break;
		default:
			break;

		}
		if (this.isShowing()) {
			pickOrderOrNot = true;
			this.dismiss();
		}

	}

	public String getStatusStr() {
		return statusStr;
	}

	public int getStatus() {
		return status;
	}

	public boolean isPickOrderOrNot() {
		return pickOrderOrNot;
	}

	public void setMarginTopAndLeft(int top, int left) {

		int plusMarginTop = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 11, context.getResources()
						.getDisplayMetrics());
		int plusMarginLeft = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 17, context.getResources()
						.getDisplayMetrics());

		rootView.setPadding(left + plusMarginLeft, top + plusMarginTop, 0, 0);

	}

}
