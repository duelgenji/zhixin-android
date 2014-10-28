package com.qubaopen.customui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qubaopen.R;
import com.qubaopen.domain.Options;

public class ShunxuItem extends LinearLayout implements View.OnClickListener {

	private Context context;
	private LayoutInflater inflater;
	private Options quChoice;
	private TextView choiceNoTextView;
	private TextView choiceTitleTextView;

	private boolean isChecked;

	// outsider view
	private ShunxuViewGroup shunxuViewGroup;

	public ShunxuItem(Context context, Options quChoice) {
		super(context);
		this.context = context;

		inflater = LayoutInflater.from(context);
		this.quChoice = quChoice;
		init(quChoice);
	}

	private void init(Options aChoice) {
		isChecked = false;
		inflater.inflate(R.layout.customui_shunxu_item, this);

		choiceNoTextView = (TextView) this.findViewById(R.id.choiceNoTextView);
		choiceNoTextView.setText(aChoice.getOptionNum());
		choiceTitleTextView = (TextView) this
				.findViewById(R.id.choiceTitleTextView);
		choiceTitleTextView.setText(aChoice.getOptionContent());

		this.setOnClickListener(this);
	}

	private void setChecked() {
		choiceNoTextView.setTextColor(context.getResources().getColor(
				R.color.text_more_grey));
		choiceTitleTextView.setTextColor(context.getResources().getColor(
				R.color.text_more_grey));
		isChecked = true;

	}

	public void setUnchecked() {
		choiceNoTextView.setTextColor(context.getResources().getColor(
				R.color.text_grey));
		choiceTitleTextView.setTextColor(context.getResources().getColor(
				R.color.text_grey));
		isChecked = false;

	}

	public Options getQuChoice() {
		return quChoice;
	}

	public boolean isChecked() {
		return isChecked;
	}

	@Override
	public void onClick(View v) {
		if (!isChecked()) {
			setChecked();
			shunxuViewGroup.addText(quChoice, this);
		}

	}

	public void setShunxuViewGroup(ShunxuViewGroup shunxuViewGroup) {
		this.shunxuViewGroup = shunxuViewGroup;

	}

}
