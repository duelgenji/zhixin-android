package com.qubaopen.customui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.qubaopen.R;
import com.qubaopen.cache.PreviousUserQuestionCache;
import com.qubaopen.domain.DiaoyanUserQuestionAnswer;
import com.qubaopen.domain.Options;
import com.qubaopen.domain.QuUserQuestionAnswer;
import com.qubaopen.domain.UserQuestionAnswer;
import com.qubaopen.enums.QuestionTypeEnums;

public class DanxuanChoice2 extends RadioGroup {

	private Context context;
	private LayoutInflater inflater;
	private Options checkedQuChoice;

	private List<UserQuestionAnswer> currentQuDatiQuestionAnswer;

	private ArrayList<DanxuanItem2> danxuanItemList;

	private List<? extends UserQuestionAnswer> userQuestionAnswer;

	public DanxuanChoice2(Context context, List<? extends Options> choiceList,
			List<? extends UserQuestionAnswer> userQuestionAnswer) {
		super(context);
		this.context = context;
		this.userQuestionAnswer = userQuestionAnswer;
		PreviousUserQuestionCache.clearCache();
		init(choiceList);
	}

	private void init(List<? extends Options> choiceList) {
		inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.customui_danxuan, this);
		danxuanItemList = new ArrayList<DanxuanItem2>();
		for (Options aChoice : choiceList) {
			DanxuanItem2 choiceItem = new DanxuanItem2(context, aChoice);
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
			danxuanItemList.add(choiceItem);
		}
		addCheckLogic();
	}

	private void addCheckLogic() {
		for (final DanxuanItem2 choiceItem : danxuanItemList) {

			choiceItem
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton theButton,
								boolean isChecked) {
							if (isChecked) {
								for (DanxuanItem2 otherchoiceItem : danxuanItemList) {

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

	public Options getCheckedQuChoice() {
		return checkedQuChoice;
	}

	public List<UserQuestionAnswer> getCurrentQuDatiQuestionAnswer() {
		currentQuDatiQuestionAnswer = null;
		for (DanxuanItem2 danxuanItem : danxuanItemList) {
			if (danxuanItem.isChecked()) {
				currentQuDatiQuestionAnswer = new ArrayList<UserQuestionAnswer>();
				UserQuestionAnswer userQuestionAnswer = new UserQuestionAnswer();

				userQuestionAnswer.setQuestionType(QuestionTypeEnums.DANXUAN
						.getTypeCode());
				userQuestionAnswer.setQuestionId(danxuanItem.getQuChoice()
						.getQuestionId());
				userQuestionAnswer.setOptionId(danxuanItem.getQuChoice()
						.getOptionId());
				userQuestionAnswer.setOptionNum(danxuanItem.getQuChoice()
						.getOptionNum());
				currentQuDatiQuestionAnswer.add(userQuestionAnswer);
				break;
			}
		}

		return currentQuDatiQuestionAnswer;
	}

}
