package com.qubaopen.customui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qubaopen.R;
import com.qubaopen.domain.HuatiChoices;

public class HuatiDuoxuanChoice extends LinearLayout implements
		View.OnClickListener {

	private Context context;
	private LayoutInflater inflater;
	private HuatiChoices huatiChoice;

	private CheckBox choiceCheckBox;

	public HuatiDuoxuanChoice(Context context, HuatiChoices quChoice) {
		super(context);
		this.context = context;

		inflater = LayoutInflater.from(context);
		this.huatiChoice = quChoice;
		init(quChoice);
	}

	private void init(HuatiChoices aChoice) {

		inflater.inflate(R.layout.customui_duoxuan_item, this);
		
		TextView choiceNoTextView = (TextView) this
				.findViewById(R.id.choiceNoTextView);
		choiceNoTextView.setText(aChoice.getChoiceNo());
		TextView choiceTitleTextView = (TextView) this
				.findViewById(R.id.choiceTitleTextView);
		choiceTitleTextView.setText(aChoice.getChoiceTitle());
		choiceCheckBox = (CheckBox) this.findViewById(R.id.choiceCheckBox);
		
		
		
		this.setOnClickListener(this);
	}

	public void setChecked() {
		choiceCheckBox.setChecked(true);
	}

	public void setUnchecked() {
		choiceCheckBox.setChecked(false);

	}

	public HuatiChoices getHuatiChoice() {
		return huatiChoice;
	}

	public boolean isChecked() {
		return choiceCheckBox.isChecked();
	}

	@Override
	public void onClick(View v) {
		if (!isChecked()) {
			setChecked();
		} else {
			setUnchecked();
		}
	}
}
