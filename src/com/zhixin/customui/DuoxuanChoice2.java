package com.zhixin.customui;

import java.util.ArrayList;
import java.util.List;

import com.zhixin.R;
import com.zhixin.cache.PreviousUserQuestionCache;
import com.zhixin.domain.Choices;
import com.zhixin.domain.DiaoyanUserQuestionAnswer;
import com.zhixin.domain.Options;
import com.zhixin.domain.QuUserQuestionAnswer;
import com.zhixin.domain.UserQuestionAnswer;
import com.zhixin.enums.QuestionTypeEnums;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

public class DuoxuanChoice2 extends RadioGroup {

	private Context context;
	private LayoutInflater inflater;

	private List<? extends Options> choiceList;

	private ArrayList<DuoxuanItem2> duoxuanItemList;

	private List<? extends UserQuestionAnswer> userQuestionAnswer;

	public DuoxuanChoice2(Context context, List<? extends Options> choiceList,
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
		duoxuanItemList = new ArrayList<DuoxuanItem2>();
		for (Options aChoice : choiceList) {
			DuoxuanItem2 choiceItem = new DuoxuanItem2(context, aChoice);
			choiceItem.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			if (userQuestionAnswer != null) {
				for (UserQuestionAnswer anAnswer : userQuestionAnswer) {
					if (anAnswer instanceof QuUserQuestionAnswer) {
						if (aChoice.getOptionNum()
								.equals(anAnswer.getOptionNum())) {
							choiceItem.setChecked();
						}
					} else if (anAnswer instanceof DiaoyanUserQuestionAnswer) {
						if (aChoice.getOptionId() == anAnswer.getOptionId()) {
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
		for (DuoxuanItem2 duoxuanItem : duoxuanItemList) {
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
						.getOptionId());
				userQuestionAnswer.setOptionNum(duoxuanItem.getQuChoice()
						.getOptionNum());
				quDatiQuestionAnswer.add(userQuestionAnswer);

			}
		}
		return quDatiQuestionAnswer;
	}

}
