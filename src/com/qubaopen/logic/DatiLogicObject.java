package com.qubaopen.logic;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.qubaopen.daos.DiaoyanContentDao;
import com.qubaopen.daos.QuContentDao;
import com.qubaopen.database.DbManager;
import com.qubaopen.domain.DiaoyanQuestion;
import com.qubaopen.domain.DiaoyanQuestionOrder;
import com.qubaopen.domain.QuQuestion;
import com.qubaopen.domain.QuQuestionOrder;
import com.qubaopen.domain.Question;
import com.qubaopen.domain.QuestionOrder;
import com.qubaopen.domain.UserQuestionAnswer;

public class DatiLogicObject {

	private Question currentQuestion;

	private List<? extends UserQuestionAnswer> currentAnswer;

	private boolean shouldBeFirstQuesion;

	private List<? extends QuestionOrder> orderList;

	private DiaoyanContentDao diaoyancontentDao;

	private QuContentDao qucontentDao;

	private DatiAnswerCursor historyAnswerCursor;

	// cate 0 for quceshi
	// cate 1 for qudiaoyan
	private int cate;

	public DatiLogicObject(int currentQuestionNo, boolean isQuestion, int cate) {
		switch (cate) {
		case 0:
			qucontentDao = new QuContentDao();
			break;
		case 1:
			diaoyancontentDao = new DiaoyanContentDao();
			break;

		}

		shouldBeFirstQuesion = false;
		this.cate = cate;
		init(currentQuestionNo, true);

	}

	public DatiLogicObject(int wjId, int cate) {
		switch (cate) {
		case 0:
			qucontentDao = new QuContentDao();
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
						QuQuestion.class,
						"select * from qu_question where questionId="
								+ historyAnswerCursor.getCurrentTopQuestion()
										.intValue());
				break;
			case 1:
				currentQuestion = DbManager.getDatabase().findUniqueBySql(
						DiaoyanQuestion.class,
						"select * from diaoyan_question where questionId="
								+ historyAnswerCursor.getCurrentTopQuestion()
										.intValue());
				break;
			}

		} else {

			switch (cate) {

			case 0:
				currentQuestion = DbManager.getDatabase().findUniqueBySql(
						QuQuestion.class,
						"select * from qu_question where wjId=" + wjId
								+ " order by questionId asc limit 1");
				break;
			case 1:
				currentQuestion = DbManager.getDatabase().findUniqueBySql(
						DiaoyanQuestion.class,
						"select * from diaoyan_question where wjId=" + wjId
								+ " order by questionId asc limit 1");
				break;
			}

		}
		initOrder(wjId);
		if (shouldPullFromHistory) {
			try {
				switch (cate) {
				case 0:
					currentQuestion = DbManager.getDatabase().findUniqueBySql(
							QuQuestion.class,
							"select * from qu_question where questionId="
									+ nextQuestionId());
					break;
				case 1:
					currentQuestion = DbManager.getDatabase().findUniqueBySql(
							DiaoyanQuestion.class,
							"select * from diaoyan_question where questionId="
									+ nextQuestionId());
					break;
				}
				initOrder(wjId);
			} catch (Exception e) {
				e.printStackTrace();

			}
		}

	}

	private void init(int currentQuestionNo, boolean isQuestion) {
		switch (cate) {
		case 0:
			currentQuestion = DbManager.getDatabase().findUniqueBySql(
					QuQuestion.class,
					"select * from qu_question where questionId="
							+ currentQuestionNo + " limit 1");

			break;
		case 1:
			currentQuestion = DbManager.getDatabase().findUniqueBySql(
					DiaoyanQuestion.class,
					"select * from diaoyan_question where questionId="
							+ currentQuestionNo + " limit 1");
			break;
		}

		initOrder(currentQuestion.getWjId());

		historyAnswerCursor = new DatiAnswerCursor(currentQuestion.getWjId(),
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

	public Question getCurrentQuestion() {
		return this.currentQuestion;

	}

	public void setAnswer(List<? extends UserQuestionAnswer> datiAnswer) {
		this.currentAnswer = datiAnswer;

	}

	private Integer noSpecialQuestionNextLogic() throws Exception {
		if (!currentQuestion.isMatrix()) {
			saveAnswer();
			switch (currentQuestion.getQuestionType()) {
			case 1:
				for (QuestionOrder anOrder : orderList) {

					if (anOrder.getCurrentQuestionId() == currentAnswer.get(0)
							.getQuestionId()) {// useless code exactly

						switch (cate) {
						case 0:
							if (anOrder.getChoiceNo() == null
									|| StringUtils.isEmpty(anOrder
											.getChoiceNo())) {
								if (anOrder.isNextOneEnd()) {
									return null;
								} else {
									return anOrder.getNextQuestionId();
								}
							}
							if (anOrder.getChoiceNo().equals(
									currentAnswer.get(0).getOptionNum())) {
								if (anOrder.isNextOneEnd()) {
									return null;
								} else {
									return anOrder.getNextQuestionId();
								}
							}

							break;
						case 1:
							if (anOrder.getChoiceId() == 0) {
								if (anOrder.isNextOneEnd()) {
									return null;
								} else {
									return anOrder.getNextQuestionId();
								}

							}
							if (anOrder.getChoiceId() == currentAnswer.get(0)
									.getOptionId()) {
								if (anOrder.isNextOneEnd()) {
									return null;
								} else {
									return anOrder.getNextQuestionId();
								}
							}

							break;
						}

					}
				}
				break;
			case 2:
			case 3:
			case 4:
				if (orderList.size() != 0) {
					if (orderList.get(orderList.size()-1).isNextOneEnd()) {
						return null;
					} else {
						return orderList.get(orderList.size()-1).getNextQuestionId();
					}
				}
				break;
			case 5:
				if (orderList.size() != 0) {
					if (orderList.get(0).isNextOneEnd()) {
						return null;
					} else {
						return orderList.get(0).getNextQuestionId();
					}
				}
				break;
			default:
				break;
			}
		} else {
			saveMatrixAnswer();
			// Program exploit: when the matrix question's order list is
			// empty,the next question will not be found correctly.
			// In this case,the next question inside the matrix question group
			// will be found.
			if (orderList.size() != 0) {
				if (orderList.get(0).isNextOneEnd()) {
					return null;
				} else {
					return orderList.get(0).getNextQuestionId();
				}
			}
		}
		throw new Exception();
	}

	private Integer specialQuestionNextLogic() throws Exception {
		saveAnswer();
		for (QuestionOrder anOrder : orderList) {
			// the code below will be executed more times than actually needed.
			List<? extends UserQuestionAnswer> questionAnswer = DatiAnswerCursor
					.getAnswer(anOrder.getCurrentQuestionId(), cate);
			if (questionAnswer.size() == 1) {
				if (questionAnswer.get(0).getOptionId() == anOrder
						.getChoiceId()) {
					if (anOrder.isNextOneEnd()) {
						return null;
					} else {
						return anOrder.getNextQuestionId();
					}
				}
			} else if (questionAnswer.size() > 1) {
				if (anOrder.getChoiceId() == 0) {
					if (anOrder.isNextOneEnd()) {
						return null;
					} else {
						return anOrder.getNextQuestionId();
					}
				}
			}
		}
		throw new Exception();
	}

	private void saveAnswer() {
		switch (cate) {
		case 0:
			qucontentDao.saveAnswer(currentAnswer, this);
			break;
		case 1:
			diaoyancontentDao.saveAnswer(currentAnswer, this);
			break;
		}

	}

	public int getWjId() {
		return currentQuestion.getWjId();
	}

	private void saveMatrixAnswer() {
		diaoyancontentDao.saveMatrixAnswer(currentAnswer, this);

	}

	public DatiAnswerCursor getHistoryAnswerCursor() {
		return historyAnswerCursor;
	}

	private void initOrder(int wjId) {
		if (!currentQuestion.isMatrix()) {
			switch (cate) {
			case 0:
				orderList = DbManager.getDatabase().findAllByWhere(
						QuQuestionOrder.class,
						"wjId=" + wjId + " and questionId="
								+ currentQuestion.getQuestionId());

				break;
			case 1:
				orderList = DbManager.getDatabase().findAllByWhere(
						DiaoyanQuestionOrder.class,
						"wjId=" + wjId + " and questionId="
								+ currentQuestion.getQuestionId());
				break;
			}

		} else {
			orderList = DbManager
					.getDatabase()
					.findAllByWhere(
							DiaoyanQuestionOrder.class,
							"wjId="
									+ wjId
									+ " and questionId in "
									+ "(select distinct questionId from diaoyan_question where mainNo='"
									+ currentQuestion.getMainNo() + "') order by currentQuestionId desc");
		}

	}

}
