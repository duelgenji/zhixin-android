package com.zhixin.customui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhixin.R;
import com.zhixin.domain.Options;

public class DanxuanItem2 extends RelativeLayout implements View.OnClickListener {

	private Context context;
	private LayoutInflater inflater;
	private Options quChoice;

	private RadioButton choiceCheckBox;

	public DanxuanItem2(Context context, Options quChoice) {
		super(context);
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.quChoice = quChoice;
		init(quChoice);
	}

	private void init(Options aChoice) {
		inflater.inflate(R.layout.customui_danxuan_item, this);
		TextView choiceNoTextView = (TextView) this
				.findViewById(R.id.choiceNoTextView);
		choiceNoTextView.setText(aChoice.getOptionNum());
		TextView choiceTitleTextView = (TextView) this
				.findViewById(R.id.choiceTitleTextView);
		choiceTitleTextView.setText(aChoice.getOptionContent());
		choiceCheckBox = (RadioButton) this.findViewById(R.id.choiceCheckBox);

		this.setOnClickListener(this);
	}

	public void setChecked() {
		choiceCheckBox.setChecked(true);
	}

	public void setUnchecked() {
		choiceCheckBox.setChecked(false);

	}

	public void setOnCheckedChangeListener(
			CompoundButton.OnCheckedChangeListener listener) {
		choiceCheckBox.setOnCheckedChangeListener(listener);
	}

	public Options getQuChoice() {
		return quChoice;
	}

	public boolean isChecked() {
		return choiceCheckBox.isChecked();

	}

	@Override
	public void onClick(View v) {
		if (!isChecked()) {
			setChecked();
		}

	}

}
