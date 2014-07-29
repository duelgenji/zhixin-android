package com.zhixin.customui;

import org.apache.commons.lang3.StringUtils;

import com.zhixin.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WendaItem extends RelativeLayout {

	private Context context;
	private LayoutInflater inflater;
	private EditText wendaArea;
	private int questionId;
	
	public WendaItem(Context context) {
		super(context);
		this.context = context;
		inflater = LayoutInflater.from(context);
		init();
	}

	private void init() {
		inflater.inflate(R.layout.customui_wenda_item, this);
		wendaArea = (EditText) this.findViewById(R.id.wendaArea);
	}

	public boolean isAnswerEmpty() {
		return StringUtils.isBlank(wendaArea.getText().toString().trim());
	}

	public String getAnswer(){
		return wendaArea.getText().toString().trim();
	}
	
	public void setAnswer(String str){
		wendaArea.setText(str);	
	}
}
