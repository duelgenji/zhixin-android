package com.zhixin.customui;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zhixin.R;
import com.zhixin.domain.Choices;
import com.zhixin.domain.Options;

public class ShunxuTitleItem extends FrameLayout implements
		View.OnClickListener {

	private Context context;
	private LayoutInflater inflater;

	private TextView titleText;

	private Options currentChoices;

	private ShunxuItem boundedItem;

	private String choiceText;

	public ShunxuTitleItem(Context context, ViewGroup parent) {
		super(context);
		this.context = context;
		inflater = LayoutInflater.from(context);
		init(parent);
	}

	public void init(ViewGroup parent) {
		inflater.inflate(R.layout.customui_shunxu_title, this);
		titleText = (TextView) this.findViewById(R.id.shunxuChoiceNo);

		this.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if (boundedItem != null) {
			if (StringUtils.isNotBlank(titleText.getText())) {
				titleText.setText("");
				boundedItem.setUnchecked();
			}
		}
	}

	private void setText(String str) {
		titleText.setText(str);
		this.choiceText = str;
	}

	public Options getCurrentChoices() {
		return currentChoices;
	}

	public void setCurrentChoices(Options currentChoices) {
		this.currentChoices = currentChoices;
		setText(currentChoices.getOptionNum());
	}

	public boolean hasValueYet() {
		return StringUtils.isNotBlank(titleText.getText());

	}

	public void setBoundedItem(ShunxuItem boundedItem) {
		this.boundedItem = boundedItem;
	}

	public String getChoiceText() {
		return this.choiceText;
	}

}
