package com.zhixin.customui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zhixin.R;
import com.zhixin.cache.PreviousUserQuestionCache;
import com.zhixin.domain.Choices;
import com.zhixin.domain.UserQuestionAnswer;
import com.zhixin.logic.DatiQuestionAnswer;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

public class ShunxuChoice extends RadioGroup {

	private Context context;
	private LayoutInflater inflater;

	private List<? extends Choices> choiceList;
	private HashMap<Integer, ShunxuItem> shunxuItemList;
	private ShunxuViewGroup shunxuViewGroup;
	
	private List<? extends UserQuestionAnswer> answerList;

	public ShunxuChoice(Context context, List<? extends Choices> choiceList,
			ShunxuViewGroup shunxuViewGroup,
			List<? extends UserQuestionAnswer> answerList) {
		super(context);
		this.context = context;
		this.choiceList = choiceList;
		this.shunxuViewGroup = shunxuViewGroup;
		this.answerList = answerList;
		PreviousUserQuestionCache.clearCache();
		init();
	}

	private void init() {

		inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.customui_duoxuan, this);
		shunxuItemList = new HashMap<Integer, ShunxuItem>();
		for (Choices aChoice : choiceList) {
			ShunxuItem choiceItem = new ShunxuItem(context, aChoice);
			choiceItem.setShunxuViewGroup(shunxuViewGroup);
			choiceItem.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			addView(choiceItem);

			shunxuItemList.put(aChoice.getChoiceId(), choiceItem);

		}
		if (answerList != null){
			setHistoryAnswerChecked();
		}

		// addCheckLogic();
	}
	
	private void setHistoryAnswerChecked(){
		for (UserQuestionAnswer userQuestionAnswer:answerList){
			for (Choices aChoice : choiceList){
				if (aChoice.getChoiceId() == userQuestionAnswer.getChoiceId()){
					shunxuItemList.get(aChoice.getChoiceId()).performClick();
					break;
				}
			}
		}
	}
	

	public DatiQuestionAnswer getAnswer() {
		DatiQuestionAnswer quDatiQuestionAnswer = null;
		// for (DuoxuanItem duoxuanItem : duoxuanItemList) {
		// if (duoxuanItem.isChecked()) {
		// if (quDatiQuestionAnswer == null) {
		// if (context instanceof DiaoyanContentActivity) {
		// quDatiQuestionAnswer = new DiaoyanDatiQuestionAnswer(
		// QuestionTypeEnums.DUOXUAN.getTypeCode());
		// } else if (context instanceof QuceshiContentActivity) {
		// quDatiQuestionAnswer = new QuDatiQuestionAnswer(
		// QuestionTypeEnums.DUOXUAN.getTypeCode());
		// }
		// quDatiQuestionAnswer.setQuestionId(duoxuanItem
		// .getQuChoice().getQuestionId());
		// }
		//
		// quDatiQuestionAnswer.addChoiceId(duoxuanItem.getQuChoice()
		// .getChoiceId());
		// quDatiQuestionAnswer.addChoiceNo(duoxuanItem.getQuChoice()
		// .getChoiceNo());
		//
		// }
		// }
		return quDatiQuestionAnswer;
	}

}
