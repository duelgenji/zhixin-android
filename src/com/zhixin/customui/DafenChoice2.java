package com.zhixin.customui;

import java.util.ArrayList;
import java.util.List;

import com.zhixin.R;
import com.zhixin.cache.PreviousUserQuestionCache;
import com.zhixin.dialog.DafenFenshuOverlayer2;
import com.zhixin.domain.Question2;
import com.zhixin.domain.UserQuestionAnswer;
import com.zhixin.enums.QuestionTypeEnums;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

public class DafenChoice2 extends RadioGroup {

	private Context context;
	private LayoutInflater inflater;

	private List<? extends Question2> questionList;

	private ArrayList<DafenItem2> dafenItemList;

	private DafenFenshuOverlayer2 dafenFenshuOverlayer;

	private List<? extends UserQuestionAnswer> userQuestionAnswer;

	public DafenChoice2(Context context, List<? extends Question2> questionList,
			DafenFenshuOverlayer2 dafenFenshuOverlayer,
			List<? extends UserQuestionAnswer> userQuestionAnswer) {
		super(context);
		this.context = context;
		this.questionList = questionList;
		this.dafenFenshuOverlayer = dafenFenshuOverlayer;
		this.userQuestionAnswer = userQuestionAnswer;
		PreviousUserQuestionCache.clearCache();
		init();
	}

	private void init() {

		inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.customui_duoxuan, this);
		dafenItemList = new ArrayList<DafenItem2>();
		for (Question2 aChoice : questionList) {
			DafenItem2 choiceItem = new DafenItem2(context, aChoice,
					dafenFenshuOverlayer);
			choiceItem.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			if (userQuestionAnswer != null) {
				for (UserQuestionAnswer anAnswer : userQuestionAnswer) {
					if (aChoice.getQuestionId() == anAnswer.getQuestionId()) {
						choiceItem.setScore(anAnswer.getScore());
					}
				}
			}

			addView(choiceItem);

			dafenItemList.add(choiceItem);
		}
	}

	public List<UserQuestionAnswer> getAnswer() {
		List<UserQuestionAnswer> quDatiQuestionAnswer = null;
		if (hasValuesYet()) {
			quDatiQuestionAnswer = new ArrayList<UserQuestionAnswer>();
			UserQuestionAnswer anAns;
			for (DafenItem2 anItem : dafenItemList) {
				anAns = new UserQuestionAnswer();
				anAns.setQuestionType(QuestionTypeEnums.DAFEN.getTypeCode());
				anAns.setQuestionId(anItem.getQuestion().getQuestionId());
				anAns.setScore(anItem.getScore());
				anAns.setMainNo(anItem.getQuestion().getMatrixNo());
				quDatiQuestionAnswer.add(anAns);
			}
		}
		return quDatiQuestionAnswer;
	}

	private boolean hasValuesYet() {
		for (DafenItem2 anItem : dafenItemList) {
			if (anItem.isFieldEmpty()) {
				return false;
			}
		}
		return true;
	}

}
