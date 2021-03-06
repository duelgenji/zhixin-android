package com.qubaopen.customui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qubaopen.R;
import com.qubaopen.domain.HuatiChoices;

public class HuatiDanxuanChoice extends RelativeLayout implements  View.OnClickListener{

	private Context context;
	private LayoutInflater inflater;
	private HuatiChoices huatiChoice;

	private RadioButton choiceCheckBox;

	public HuatiDanxuanChoice(Context context, HuatiChoices quChoice) {
		super(context);
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.huatiChoice = quChoice;
		init(quChoice);
	}

	private void init(HuatiChoices aChoice) {
		inflater.inflate(R.layout.customui_danxuan_item, this);
		TextView choiceNoTextView = (TextView) this
				.findViewById(R.id.choiceNoTextView);
		choiceNoTextView.setText(aChoice.getChoiceNo());
		TextView choiceTitleTextView = (TextView) this
				.findViewById(R.id.choiceTitleTextView);
		choiceTitleTextView.setText(aChoice.getChoiceTitle());
		choiceCheckBox = (RadioButton) this.findViewById(R.id.choiceCheckBox);
		this.setOnClickListener(this);

	}

	private void setChecked() {
		choiceCheckBox.setChecked(true);
	}

	public void setUnchecked() {
		choiceCheckBox.setChecked(false);

	}

	public void setOnCheckedChangeListener(
			CompoundButton.OnCheckedChangeListener listener) {
		choiceCheckBox.setOnCheckedChangeListener(listener);
	}

	public HuatiChoices getHuatiChoice() {
		return huatiChoice;
	}

	public boolean isChecked() {
		return choiceCheckBox.isChecked();

	}

	@Override
	public void onClick(View v) {
		if (!isChecked()){
			setChecked();
		}
	}
	
	
	

}
