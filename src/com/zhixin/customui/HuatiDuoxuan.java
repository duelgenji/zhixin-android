package com.zhixin.customui;

import java.util.ArrayList;
import java.util.List;

import com.zhixin.R;
import com.zhixin.domain.HuatiChoices;
import com.zhixin.enums.QuestionTypeEnums;
import com.zhixin.logic.QuDatiQuestionAnswer;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;

public class HuatiDuoxuan extends RadioGroup {

	private Context context;
	private LayoutInflater inflater;

	private List<HuatiChoices> choiceList;

	private ArrayList<HuatiDuoxuanChoice> duoxuanItemList;

	public HuatiDuoxuan(Context context, List<HuatiChoices> choiceList) {
		super(context);
		this.context = context;
		this.choiceList = choiceList;
		init();
	}

	private void init() {

		inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.customui_duoxuan, this);
		duoxuanItemList = new ArrayList<HuatiDuoxuanChoice>();
		for (HuatiChoices aChoice : choiceList) {
			HuatiDuoxuanChoice choiceItem = new HuatiDuoxuanChoice(context,
					aChoice);
			choiceItem.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			addView(choiceItem);

			duoxuanItemList.add(choiceItem);
		}

		// addCheckLogic();
	}

	public QuDatiQuestionAnswer getAnswer() {
		QuDatiQuestionAnswer quDatiQuestionAnswer = null;
		for (HuatiDuoxuanChoice duoxuanItem : duoxuanItemList) {
			if (duoxuanItem.isChecked()) {
				if (quDatiQuestionAnswer == null) {
					quDatiQuestionAnswer = new QuDatiQuestionAnswer(
							QuestionTypeEnums.DUOXUAN.getTypeCode());
					quDatiQuestionAnswer.setQuestionId(duoxuanItem
							.getHuatiChoice().getQuestionId());
				}

				quDatiQuestionAnswer.addChoiceId(duoxuanItem.getHuatiChoice()
						.getChoiceId());
				quDatiQuestionAnswer.addChoiceNo(duoxuanItem.getHuatiChoice()
						.getChoiceNo());

			}
		}
		return quDatiQuestionAnswer;
	}

}
