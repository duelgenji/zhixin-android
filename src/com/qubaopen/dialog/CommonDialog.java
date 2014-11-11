package com.qubaopen.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.qubaopen.R;

/**
 * Created by duel on 14-3-27.
 */
public class CommonDialog extends Dialog implements View.OnClickListener {

	private Context context;
	private TextView commonContent;
	private TextView btnOk;
	private TextView btnCancel;
	private CommonDialogListener listener;

	public interface CommonDialogListener {
		public void onClick(View view);
	}

	public CommonDialog(Context context, String content,
			CommonDialogListener listener) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.context = context;
		this.listener = listener;
		init(content);
	}

	public void init(String content) {

		this.setContentView(R.layout.dialog_common);
		this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		Window window = this.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.dimAmount = 0.7f;
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		window.setAttributes(lp);
		commonContent = (TextView) this.findViewById(R.id.dialog_common_content);
		commonContent.setText(content);
		btnOk = (TextView) this.findViewById(R.id.dialog_common_confirm);
		btnCancel = (TextView) this.findViewById(R.id.dialog_common_cancel);

		btnCancel.setOnClickListener(this);
		btnOk.setOnClickListener(this);
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
