package com.zhixin.logic;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

import android.util.Log;

import com.zhixin.daos.DiaoyanContentDao;
import com.zhixin.daos.InterestContentDao;
import com.zhixin.database.DbManager;
import com.zhixin.domain.DiaoyanQuestionOrder;
import com.zhixin.domain.InterestQuestion;
import com.zhixin.domain.QuQuestionOrder;
import com.zhixin.domain.Question2;
import com.zhixin.domain.QuestionOrder;
import com.zhixin.domain.UserQuestionAnswer;

public class DoLogicObject {

	private Question2 currentQuestion;

	private List<? extends UserQuestionAnswer> currentAnswer;

	private boolean shouldBeFirstQuesion;

	private List<? extends Question2> questionList;

	private DiaoyanContentDao diaoyancontentDao;

	private InterestContentDao interestContentDao;

	private DatiAnswerCursor historyAnswerCursor;

	// cate 0 for quceshi
	// cate 1 for qudiaoyan
	private int cate;

	public DoLogicObject(int currentQuestionNo, boolean isQuestion, int cate) {
		switch (cate) {
		case 0:
			interestContentDao = new InterestContentDao();
			break;
		case 1:
			diaoyancontentDao = new DiaoyanContentDao();
			break;

		}

		shouldBeFirstQuesion = false;
		this.cate = cate;
		init(currentQuestionNo, true);

	}

	public DoLogicObject(int wjId, int cate) {
		switch (cate) {
		case 0:
			interestContentDao = new InterestContentDao();
			break;
		case 1:
			diaoyancontentDao = new DiaoyanContentDao();
			break;

		}
		this.cate = cate;
		shouldBeFirstQuesion = true;
		init(wjId);
	}

	private void init(int wjId) {
		historyAnswerCursor = new DatiAnswerCursor(wjId, cate);
		boolean shouldPullFromHistory = false;
		if (historyAnswerCursor.getCurrentTopQuestion() != null) {
			shouldPullFromHistory = true;
			currentAnswer = historyAnswerCursor
					.getCurrentTopQuestionAnswer(historyAnswerCursor
							.getCurrentTopQuestion().intValue());
			switch (cate) {

			case 0:
				currentQuestion = DbManager.getDatabase().findUniqueBySql(
						InterestQuestion.class,
						"select * from interest_question where questionId="
								+ historyAnswerCursor.getCurrentTopQuestion()
										.intValue());
				break;
			case 1:
//				currentQuestion = DbManager.getDatabase().findUniqueBySql(
//						DiaoyanQuestion.class,
//						"select * from diaoyan_question where questionId="
//								+ historyAnswerCursor.getCurrentTopQuestion()
//										.intValue());
//				break;
			}

		} else {

			switch (cate) {

			case 0:
				currentQuestion = DbManager.getDatabase().findUniqueBySql(
						InterestQuestion.class,
						"select * from interest_question where interestId=" + wjId
								+ " order by questionId asc limit 1");
				break;
			case 1:
//				currentQuestion = DbManager.getDatabase().findUniqueBySql(
//						DiaoyanQuestion.class,
//						"select * from diaoyan_question where wjId=" + wjId
//								+ " order by questionId asc limit 1");
				break;
			}

		}
		initQuestions(wjId);
		if (shouldPullFromHistory) {
			try {
				switch (cate) {
				case 0:
					currentQuestion = DbManager.getDatabase().findUniqueBySql(
							InterestQuestion.class,
							"select * from interest_question where questionId="
									+ nextQuestionId());
					break;
				case 1:
//					currentQuestion = DbManager.getDatabase().findUniqueBySql(
//							DiaoyanQuestion.class,
//							"select * from diaoyan_question where questionId="
//									+ nextQuestionId());
					break;
				}
				initQuestions(wjId);
			} catch (Exception e) {
				e.printStackTrace();

			}
		}

	}

	private void init(int currentQuestionNo, boolean isQuestion) {
		switch (cate) {
		case 0:
			currentQuestion = DbManager.getDatabase().findUniqueBySql(
					InterestQuestion.class,
					"select * from interest_question where questionId="
							+ currentQuestionNo + " limit 1");

			break;
		case 1:
//			currentQuestion = DbManager.getDatabase().findUniqueBySql(
//					DiaoyanQuestion.class,
//					"select * from diaoyan_question where questionId="
//							+ currentQuestionNo + " limit 1");
			break;
		}

		initQuestions(currentQuestion.getInterestId());

		historyAnswerCursor = new DatiAnswerCursor(currentQuestion.getInterestId(),
				cate, currentQuestionNo);

	}

	public Integer nextQuestionId() throws Exception {
		if (currentAnswer != null) {
			if (!currentQuestion.isSpecial()) {
				Integer nextOne = noSpecialQuestionNextLogic();
				if (nextOne != null) {
					return nextOne;
				}
			} else {
				Integer nextOne = specialQuestionNextLogic();
				return nextOne;
			}
		}
		return null;
	}

	public Question2 getCurrentQuestion() {
		return this.currentQuestion;

	}

	public void setAnswer(List<? extends UserQuestionAnswer> datiAnswer) {
		this.currentAnswer = datiAnswer;

	}
	
	
	//获取下一题 题目id
	public Integer getNextQuestionId(){
		//存答案
		saveAnswer();
		String questionOrder=currentQuestion.getQuestionOrder();
		if(!questionOrder.isEmpty()){
			StringTokenizer st = new StringTokenizer(questionOrder, "|");
			if(currentAnswer.size()==1){
				//单选题跳转
				while (st.hasMoreTokens()) {
					String anOrder = st.nextToken();
					StringTokenizer innerSt = new StringTokenizer(anOrder, ":");
					if(innerSt.hasMoreTokens()){
						innerSt.nextToken();
						String midStr=innerSt.nextToken();
						if(midStr.equals("0") || midStr.equals(currentAnswer.get(0))){
							String lastStr=innerSt.nextToken();
							Log.i("login1","单选题型跳转  逻辑跳转"+lastStr);	
							return Integer.parseInt(lastStr);
						}
					}
				}
			}else{
				//其他题跳转
				while (st.hasMoreTokens()) {
					String anOrder = st.nextToken();
					StringTokenizer innerSt = new StringTokenizer(anOrder, ":");
					if(innerSt.hasMoreTokens()){
						innerSt.nextToken();
						String midStr=innerSt.nextToken();
						if(midStr.equals("0")){
							String lastStr=innerSt.nextToken();
							Log.i("login1","其他题型跳转  逻辑跳转"+lastStr);	
							
							return Integer.parseInt(lastStr);
						}
					}
				}
			}
		

			Log.i("login1","莫名跳转");	
			return currentQuestion.getQuestionId()+1;
		}else{
			Log.i("login1","无order跳转");	
			return currentQuestion.getQuestionId()+1;
		}
		
//		switch (currentQuestion.getQuestionType()) {	
//		
//		case 1:
//			break;
//		case 2:
//			break;
//		case 3:
//			break;
//		case 4:
//			break;
//		case 5:
//			break;
//		default:
//			break;
//			}
	}
	

	private Integer noSpecialQuestionNextLogic() throws Exception {
//		if (!currentQuestion.isMatrix()) {
//			saveAnswer();
//			switch (currentQuestion.getQuestionType()) {
//			case 1:
//				for (QuestionOrder anOrder : orderList) {
//
//					if (anOrder.getCurrentQuestionId() == currentAnswer.get(0)
//							.getQuestionId()) {// useless code exactly
//
//						switch (cate) {
//						case 0:
//							if (anOrder.getChoiceNo() == null
//									|| StringUtils.isEmpty(anOrder
//											.getChoiceNo())) {
//								if (anOrder.isNextOneEnd()) {
//									return null;
//								} else {
//									return anOrder.getNextQuestionId();
//								}
//							}
//							if (anOrder.getChoiceNo().equals(
//									currentAnswer.get(0).getChoiceNo())) {
//								if (anOrder.isNextOneEnd()) {
//									return null;
//								} else {
//									return anOrder.getNextQuestionId();
//								}
//							}
//
//							break;
//						case 1:
//							if (anOrder.getChoiceId() == 0) {
//								if (anOrder.isNextOneEnd()) {
//									return null;
//								} else {
//									return anOrder.getNextQuestionId();
//								}
//
//							}
//							if (anOrder.getChoiceId() == currentAnswer.get(0)
//									.getChoiceId()) {
//								if (anOrder.isNextOneEnd()) {
//									return null;
//								} else {
//									return anOrder.getNextQuestionId();
//								}
//							}
//
//							break;
//						}
//
//					}
//				}
//				break;
//			case 2:
//			case 3:
//			case 4:
//				if (orderList.size() != 0) {
//					if (orderList.get(orderList.size()-1).isNextOneEnd()) {
//						return null;
//					} else {
//						return orderList.get(orderList.size()-1).getNextQuestionId();
//					}
//				}
//				break;
//			case 5:
//				if (orderList.size() != 0) {
//					if (orderList.get(0).isNextOneEnd()) {
//						return null;
//					} else {
//						return orderList.get(0).getNextQuestionId();
//					}
//				}
//				break;
//			default:
//				break;
//			}
//		} else {
//			saveMatrixAnswer();
//			// Program exploit: when the matrix question's order list is
//			// empty,the next question will not be found correctly.
//			// In this case,the next question inside the matrix question group
//			// will be found.
//			if (orderList.size() != 0) {
//				if (orderList.get(0).isNextOneEnd()) {
//					return null;
//				} else {
//					return orderList.get(0).getNextQuestionId();
//				}
//			}
//		}
		throw new Exception();
	}

	private Integer specialQuestionNextLogic() throws Exception {
		//saveAnswer();
//		for (QuestionOrder anOrder : orderList) {
//			// the code below will be executed more times than actually needed.
//			List<? extends UserQuestionAnswer> questionAnswer = DatiAnswerCursor
//					.getAnswer(anOrder.getCurrentQuestionId(), cate);
//			if (questionAnswer.size() == 1) {
//				if (questionAnswer.get(0).getChoiceId() == anOrder
//						.getChoiceId()) {
//					if (anOrder.isNextOneEnd()) {
//						return null;
//					} else {
//						return anOrder.getNextQuestionId();
//					}
//				}
//			} else if (questionAnswer.size() > 1) {
//				if (anOrder.getChoiceId() == 0) {
//					if (anOrder.isNextOneEnd()) {
//						return null;
//					} else {
//						return anOrder.getNextQuestionId();
//					}
//				}
//			}
//		}
		throw new Exception();
	}

	private void saveAnswer() {
		switch (cate) {
		case 0:
			interestContentDao.saveAnswer(currentAnswer, this);
			break;
		case 1:
			//diaoyancontentDao.saveAnswer(currentAnswer, this);
			break;
		}

	}

	public int getWjId() {
		return currentQuestion.getInterestId();
	}

	private void saveMatrixAnswer() {
		//diaoyancontentDao.saveMatrixAnswer(currentAnswer, this);

	}

	public DatiAnswerCursor getHistoryAnswerCursor() {
		return historyAnswerCursor;
	}

	
	//加载问卷所有题目
	private void initQuestions(int wjId){
		switch(cate){
			case 0:
			questionList = DbManager.getDatabase().findAllByWhere(
					InterestQuestion.class,
					"interestId=" + wjId );

			break;
			case 1:	
//				questionList = DbManager.getDatabase().findAllByWhere(
//				InterestQuestion.class,
//				"interestId=" + wjId );
			break;
		}
	}
	
	

}
