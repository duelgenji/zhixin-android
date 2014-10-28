package com.qubaopen.customui;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.qubaopen.R;
import com.qubaopen.domain.Options;

public class WendaItem extends RelativeLayout {

	private Context context;
	private LayoutInflater inflater;
	private EditText wendaArea;
	private Options quChoice;
	


	public WendaItem(Context context, Options quChoice) {
		super(context);
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.quChoice = quChoice;
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
	
	public Options getQuChoice() {
		return quChoice;
	}

	public void setQuChoice(Options quChoice) {
		this.quChoice = quChoice;
	}
}
