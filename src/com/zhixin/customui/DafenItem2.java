package com.zhixin.customui;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhixin.R;
import com.zhixin.dialog.DafenFenshuOverlayer2;
import com.zhixin.domain.DiaoyanQuestion;
import com.zhixin.domain.Question2;

public class DafenItem2 extends LinearLayout implements View.OnClickListener {

	private Context context;
	private LayoutInflater inflater;
	private DiaoyanQuestion diaoyanQuestion;

	private TextView fenshuTextView;

	private TextView dafenContentTextView;

	private View dafenCheckBox;

	private Question2 question;
	
	private int score;

	private DafenFenshuOverlayer2 dafenFenshuOverlayer;

	public DafenItem2(Context context, Question2 diaoyanQuestion,
			DafenFenshuOverlayer2 dafenFenshuOverlayer) {
		super(context);
		this.context = context;
		inflater = LayoutInflater.from(context);
		question = diaoyanQuestion;
		this.dafenFenshuOverlayer = dafenFenshuOverlayer;
		init(diaoyanQuestion);

	}

	private void init(Question2 diaoyanQuestion) {

		inflater.inflate(R.layout.customui_dafen_item, this);
		dafenContentTextView = (TextView) this
				.findViewById(R.id.dafenContentTextView);
		fenshuTextView = (TextView) this.findViewById(R.id.fenshuTextView);
		dafenCheckBox = this.findViewById(R.id.dafenCheckBox);

		dafenContentTextView.setText(diaoyanQuestion.getQuestionNum() + "."
				+ diaoyanQuestion.getQuestionContent());

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

	public Question2 getQuestion() {
		return question;
	}

	public int getScore() {
		return score;
	}
	
	
	
}
