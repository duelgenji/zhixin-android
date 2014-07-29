package com.zhixin.customui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.zhixin.R;
import com.zhixin.cache.PreviousUserQuestionCache;
import com.zhixin.domain.Choices;
import com.zhixin.domain.DiaoyanUserQuestionAnswer;
import com.zhixin.domain.QuUserQuestionAnswer;
import com.zhixin.domain.UserQuestionAnswer;
import com.zhixin.enums.QuestionTypeEnums;

public class DanxuanChoice extends RadioGroup {

	private Context context;
	private LayoutInflater inflater;
	private Choices checkedQuChoice;

	private List<UserQuestionAnswer> currentQuDatiQuestionAnswer;

	private ArrayList<DanxuanItem> danxuanItemList;

	private List<? extends UserQuestionAnswer> userQuestionAnswer;

	public DanxuanChoice(Context context, List<? extends Choices> choiceList,
			List<? extends UserQuestionAnswer> userQuestionAnswer) {
		super(context);
		this.context = context;
		this.userQuestionAnswer = userQuestionAnswer;
		PreviousUserQuestionCache.clearCache();
		init(choiceList);
	}

	private void init(List<? extends Choices> choiceList) {
		inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.customui_danxuan, this);
		danxuanItemList = new ArrayList<DanxuanItem>();
		for (Choices aChoice : choiceList) {
			DanxuanItem choiceItem = new DanxuanItem(context, aChoice);
			if (userQuestionAnswer != null) {
				for (UserQuestionAnswer anAnswer : userQuestionAnswer) {
					if (anAnswer instanceof QuUserQuestionAnswer) {
						if (aChoice.getChoiceNo()
								.equals(anAnswer.getChoiceNo())) {
							choiceItem.setChecked();
						}
					} else if (anAnswer instanceof DiaoyanUserQuestionAnswer) {
						if (aChoice.getChoiceId() == anAnswer.getChoiceId()) {
							choiceItem.setChecked();
						}
					}

				}
			}
			addView(choiceItem);
			danxuanItemList.add(choiceItem);
		}
		addCheckLogic();
	}

	private void addCheckLogic() {
		for (final DanxuanItem choiceItem : danxuanItemList) {

			choiceItem
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton theButton,
								boolean isChecked) {
							if (isChecked) {
								for (DanxuanItem otherchoiceItem : danxuanItemList) {

									if (otherchoiceItem != choiceItem
											&& otherchoiceItem.isChecked()) {
										otherchoiceItem.setUnchecked();
									}

								}
							}
						}

					});
		}

	}

	public Choices getCheckedQuChoice() {
		return checkedQuChoice;
	}

	public List<UserQuestionAnswer> getCurrentQuDatiQuestionAnswer() {
		currentQuDatiQuestionAnswer = null;
		for (DanxuanItem danxuanItem : danxuanItemList) {
			if (danxuanItem.isChecked()) {
				currentQuDatiQuestionAnswer = new ArrayList<UserQuestionAnswer>();
				UserQuestionAnswer userQuestionAnswer = new UserQuestionAnswer();

				userQuestionAnswer.setQuestionType(QuestionTypeEnums.DANXUAN
						.getTypeCode());
				userQuestionAnswer.setQuestionId(danxuanItem.getQuChoice()
						.getQuestionId());
				userQuestionAnswer.setChoiceId(danxuanItem.getQuChoice()
						.getChoiceId());
				userQuestionAnswer.setChoiceNo(danxuanItem.getQuChoice()
						.getChoiceNo());
				currentQuDatiQuestionAnswer.add(userQuestionAnswer);
				break;
			}
		}

		return currentQuDatiQuestionAnswer;
	}

}
