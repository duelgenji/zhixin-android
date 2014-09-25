package com.zhixin.customui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhixin.R;
import com.zhixin.domain.Options;

public class DuoxuanItem2 extends LinearLayout implements View.OnClickListener {

	private Context context;
	private LayoutInflater inflater;
	private Options quChoice;

	private CheckBox choiceCheckBox;

	public DuoxuanItem2(Context context, Options quChoice) {
		super(context);
		this.context = context;

		inflater = LayoutInflater.from(context);
		this.quChoice = quChoice;
		init(quChoice);
	}

	private void init(Options aChoice) {

		inflater.inflate(R.layout.customui_duoxuan_item, this);
		
		
		TextView choiceNoTextView = (TextView) this
				.findViewById(R.id.choiceNoTextView);
		choiceNoTextView.setText(aChoice.getOptionNum());
		TextView choiceTitleTextView = (TextView) this
				.findViewById(R.id.choiceTitleTextView);
		choiceTitleTextView.setText(aChoice.getOptionContent());
		choiceCheckBox = (CheckBox) this.findViewById(R.id.choiceCheckBox);
		
		
		this.setOnClickListener(this);
	}

	public void setChecked() {
		choiceCheckBox.setChecked(true);
	}

	public void setUnchecked() {
		choiceCheckBox.setChecked(false);

	}

	public Options getQuChoice() {
		return quChoice;
	}

	public boolean isChecked() {
		return choiceCheckBox.isChecked();
	}

	@Override
	public void onClick(View v) {
		if (isChecked()){
			setUnchecked();
			
		}else{
			setChecked();		
		}
	}
}
