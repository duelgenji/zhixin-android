package com.qubaopen.customui;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qubaopen.R;
import com.qubaopen.dialog.DafenFenshuOverlayer2;
import com.qubaopen.domain.DiaoyanQuestion;
import com.qubaopen.domain.Options;
import com.qubaopen.domain.Question2;

public class DafenItem2 extends LinearLayout implements View.OnClickListener {

	private Context context;
	private LayoutInflater inflater;
	private DiaoyanQuestion diaoyanQuestion;

	private TextView fenshuTextView;

	private TextView dafenContentTextView;

	private View dafenCheckBox;

	private Options quChoice;
	
	private int score;

	private DafenFenshuOverlayer2 dafenFenshuOverlayer;

	public DafenItem2(Context context, Options quChoice,
			DafenFenshuOverlayer2 dafenFenshuOverlayer) {
		super(context);
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.quChoice = quChoice;
		this.dafenFenshuOverlayer = dafenFenshuOverlayer;
		init(quChoice);

	}

	private void init(Options aChoice) {

		inflater.inflate(R.layout.customui_dafen_item, this);
		dafenContentTextView = (TextView) this
				.findViewById(R.id.dafenContentTextView);
		fenshuTextView = (TextView) this.findViewById(R.id.fenshuTextView);
		dafenCheckBox = this.findViewById(R.id.dafenCheckBox);

		dafenContentTextView.setText(aChoice.getOptionNum() + "."
				+ aChoice.getOptionContent());

		this.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int[] location = new int[2];
		this.getLocationOnScreen(location);
		dafenFenshuOverlayer.setDafenItem(this);
		dafenFenshuOverlayer.setMarginAmount(location[1]);
		dafenFenshuOverlayer.show();
	}

	public void setScore(int score) {
		this.score = score;
		//question.setScore(score);
		fenshuTextView.setText(String.valueOf(score));
	}
	
	public boolean isFieldEmpty(){
		return StringUtils.isBlank(fenshuTextView.getText());
	}

	public Options getQuChoice() {
		return quChoice;
	}

	public int getScore() {
		return score;
	}
	
	
	
}
