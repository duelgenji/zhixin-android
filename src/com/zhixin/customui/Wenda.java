package com.zhixin.customui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.zhixin.R;
import com.zhixin.cache.PreviousUserQuestionCache;
import com.zhixin.domain.UserQuestionAnswer;
import com.zhixin.enums.QuestionTypeEnums;

public class Wenda extends RadioGroup {

	private Context context;
	private LayoutInflater inflater;

	private int questionId;

	private int choiceNumber;

	private ArrayList<WendaItem> wendaItemList;

	private List<? extends UserQuestionAnswer> userQuestionAnswer;

	public Wenda(Context context, int quQuestion, int choiceNumber,
			List<? extends UserQuestionAnswer> userQuestionAnswer) {
		super(context);
		this.context = context;
		this.questionId = quQuestion;
		this.choiceNumber = choiceNumber;
		this.userQuestionAnswer = userQuestionAnswer;
		PreviousUserQuestionCache.clearCache();
		wendaItemList = new ArrayList<WendaItem>();
		init();

	}

	private void init() {

		inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.customui_danxuan, this);

		for (int i = 0; i < choiceNumber; i++) {
			WendaItem choiceItem = new WendaItem(context);
			choiceItem.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			if (userQuestionAnswer != null) {
				choiceItem.setAnswer(userQuestionAnswer.get(i).getAnswer());

			}

			addView(choiceItem);
			wendaItemList.add(choiceItem);
		}

		// addCheckLogic();
	}

	public List<UserQuestionAnswer> getAnswer() {
		List<UserQuestionAnswer> ans = null;
		for (WendaItem anItem : wendaItemList) {
			if (anItem.isAnswerEmpty()) {
				return null;
			} else {
				for (int i = 0; i < (wendaItemList.size() - 1); i++) {
					for (int j = i + 1; j < wendaItemList.size(); j++) {
						if (wendaItemList.get(i).getAnswer()
								.equals(wendaItemList.get(j).getAnswer())) {
							return null;
						}
					}
				}
			}
		}
		for (WendaItem anItem : wendaItemList) {
			if (ans == null) {
				ans = new ArrayList<UserQuestionAnswer>();
			}
			UserQuestionAnswer userQuestionAnswer = new UserQuestionAnswer();

			userQuestionAnswer.setQuestionType(QuestionTypeEnums.WENDA
					.getTypeCode());
			userQuestionAnswer.setQuestionId(questionId);
			userQuestionAnswer.setAnswer(anItem.getAnswer());
			ans.add(userQuestionAnswer);
		}

		return ans;
	}

}
