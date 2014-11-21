package com.qubaopen.customui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.qubaopen.R;
import com.qubaopen.cache.PreviousUserQuestionCache;
import com.qubaopen.dialog.DafenFenshuOverlayer2;
import com.qubaopen.domain.Options;
import com.qubaopen.domain.Question2;
import com.qubaopen.domain.UserQuestionAnswer;
import com.qubaopen.enums.QuestionTypeEnums;

public class DafenChoice2 extends RadioGroup {

	private Context context;
	private LayoutInflater inflater;

	private List<? extends Options> questionList;

	private ArrayList<DafenItem2> dafenItemList;

	private DafenFenshuOverlayer2 dafenFenshuOverlayer;

	private List<? extends UserQuestionAnswer> userQuestionAnswer;

	public DafenChoice2(Context context, List<? extends Options> questionList,
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
		for (Options aChoice : questionList) {
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
				anAns.setQuestionId(anItem.getQuChoice().getQuestionId());
				anAns.setScore(anItem.getScore());
				anAns.setMainNo(anItem.getQuChoice().getOptionNum());
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
