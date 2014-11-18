package com.qubaopen.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.qubaopen.R;

/**
 * Created by duel on 14-3-27.
 */
public class RecoverDialog extends Dialog implements View.OnClickListener {

	private Context context;
	private String title;
	private TextView recoverContent;
	private TextView btnRecover;
	private TextView btnUnRecover;
	private TextView btnCancel;
	private RecoverDialogListener listener;

	public interface RecoverDialogListener {
		public void onClick(View view);
	}

	public RecoverDialog(Context context,
			RecoverDialogListener listener) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.context = context;
		this.listener = listener;
		init();
	}

	public void init() {

		this.setContentView(R.layout.dialog_recover);
		this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		Window window = this.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.dimAmount = 0.7f;
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		window.setAttributes(lp);
		
		btnRecover = (TextView) this.findViewById(R.id.dialog_btn_recover);
		btnRecover.setOnClickListener(this);
		
		btnUnRecover = (TextView) this.findViewById(R.id.dialog_btn_not_recover);
		btnUnRecover.setOnClickListener(this);
		
		btnCancel = (TextView) this.findViewById(R.id.dialog_recover_cancel);
		btnCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		listener.onClick(v);
		this.dismiss();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}
