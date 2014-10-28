package com.qubaopen.customui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.qubaopen.R;
import com.qubaopen.cache.PreviousUserQuestionCache;
import com.qubaopen.domain.Choices;
import com.qubaopen.domain.DiaoyanUserQuestionAnswer;
import com.qubaopen.domain.QuUserQuestionAnswer;
import com.qubaopen.domain.UserQuestionAnswer;
import com.qubaopen.enums.QuestionTypeEnums;

public class DuoxuanChoice extends RadioGroup {

	private Context context;
	private LayoutInflater inflater;

	private List<? extends Choices> choiceList;

	private ArrayList<DuoxuanItem> duoxuanItemList;

	private List<? extends UserQuestionAnswer> userQuestionAnswer;

	public DuoxuanChoice(Context context, List<? extends Choices> choiceList,
			List<? extends UserQuestionAnswer> userQuestionAnswer) {
		super(context);
		this.context = context;
		this.choiceList = choiceList;
		this.userQuestionAnswer = userQuestionAnswer;
		PreviousUserQuestionCache.clearCache();
		init();
	}

	private void init() {

		inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.customui_duoxuan, this);
		duoxuanItemList = new ArrayList<DuoxuanItem>();
		for (Choices aChoice : choiceList) {
			DuoxuanItem choiceItem = new DuoxuanItem(context, aChoice);
			choiceItem.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			if (userQuestionAnswer != null) {
				for (UserQuestionAnswer anAnswer : userQuestionAnswer) {
					if (anAnswer instanceof QuUserQuestionAnswer) {
						if (aChoice.getChoiceNo()
								.equals(anAnswer.getOptionNum())) {
							choiceItem.setChecked();
						}
					} else if (anAnswer instanceof DiaoyanUserQuestionAnswer) {
						if (aChoice.getChoiceId() == anAnswer.getOptionId()) {
							choiceItem.setChecked();
						}
					}
				}
			}

			addView(choiceItem);

			duoxuanItemList.add(choiceItem);
		}

		// addCheckLogic();
	}

	public List<UserQuestionAnswer> getAnswer() {
		List<UserQuestionAnswer> quDatiQuestionAnswer = null;
		for (DuoxuanItem duoxuanItem : duoxuanItemList) {
			if (duoxuanItem.isChecked()) {
				if (quDatiQuestionAnswer == null) {
					quDatiQuestionAnswer = new ArrayList<UserQuestionAnswer>();
				}
				UserQuestionAnswer userQuestionAnswer = new UserQuestionAnswer();
				userQuestionAnswer.setQuestionType(QuestionTypeEnums.DUOXUAN
						.getTypeCode());
				userQuestionAnswer.setQuestionId(duoxuanItem.getQuChoice()
						.getQuestionId());
				userQuestionAnswer.setOptionId(duoxuanItem.getQuChoice()
						.getChoiceId());
				userQuestionAnswer.setOptionNum(duoxuanItem.getQuChoice()
						.getChoiceNo());
				quDatiQuestionAnswer.add(userQuestionAnswer);

			}
		}
		return quDatiQuestionAnswer;
	}

}
