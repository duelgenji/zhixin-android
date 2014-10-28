package com.qubaopen.customui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.qubaopen.R;
import com.qubaopen.domain.HuatiChoices;
import com.qubaopen.enums.QuestionTypeEnums;
import com.qubaopen.logic.QuDatiQuestionAnswer;

public class HuatiDanxuan extends RadioGroup {

	private Context context;
	private LayoutInflater inflater;

	private QuDatiQuestionAnswer currentQuDatiQuestionAnswer;

	private ArrayList<HuatiDanxuanChoice> danxuanItemList;

	public HuatiDanxuan(Context context, List<HuatiChoices> choiceList) {
		super(context);
		this.context = context;
		init(choiceList);
	}

	private void init(List<HuatiChoices> choiceList) {
		inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.customui_danxuan, this);

		danxuanItemList = new ArrayList<HuatiDanxuanChoice>();
		for (HuatiChoices aChoice : choiceList) {
			HuatiDanxuanChoice choiceItem = new HuatiDanxuanChoice(context,
					aChoice);
			addView(choiceItem);
			danxuanItemList.add(choiceItem);
		}
		addCheckLogic();
	}

	private void addCheckLogic() {
		for (final HuatiDanxuanChoice choiceItem : danxuanItemList) {
			choiceItem
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton theButton,
								boolean isChecked) {
							if (isChecked) {
								for (HuatiDanxuanChoice otherchoiceItem : danxuanItemList) {

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

	public QuDatiQuestionAnswer getCurrentQuDatiQuestionAnswer() {
		currentQuDatiQuestionAnswer = null;
		for (HuatiDanxuanChoice danxuanItem : danxuanItemList) {
			if (danxuanItem.isChecked()) {
				currentQuDatiQuestionAnswer = new QuDatiQuestionAnswer(
						QuestionTypeEnums.DANXUAN.getTypeCode());

				currentQuDatiQuestionAnswer.setQuestionId(danxuanItem
						.getHuatiChoice().getQuestionId());
				currentQuDatiQuestionAnswer.setChoiceId(danxuanItem
						.getHuatiChoice().getChoiceId());
				currentQuDatiQuestionAnswer.setChoiceNo(danxuanItem
						.getHuatiChoice().getChoiceNo());

				break;
			}
		}

		return currentQuDatiQuestionAnswer;
	}

}
