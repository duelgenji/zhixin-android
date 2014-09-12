package com.zhixin.domain;

public class Question2 {

	private int questionnaireId;

	private int questionId;

	/**问题内容*/
	private String questionContent;

	/**问卷类型*/
	private int questionType;

	/**选项数量*/
	private int optionCount;

	/**答题限制时间*/
	private int limitTime;
	
	/**特殊题标记*/
	private boolean special;

	/**题目编号*/
	private String questionNum;
	
	/**答题逻辑*/
	private String questionOrder;

	/**矩阵题编号*/
	private String matrixNo;
	
	/**矩阵题标题*/
	private String matrixTitle;
	
	/**矩阵题标记*/
	private boolean matrix;

	public int getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(int questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getQuestionContent() {
		return questionContent;
	}

	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}

	public int getQuestionType() {
		return questionType;
	}

	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}

	public int getOptionCount() {
		return optionCount;
	}

	public void setOptionCount(int optionCount) {
		this.optionCount = optionCount;
	}

	public int getLimitTime() {
		return limitTime;
	}

	public void setLimitTime(int limitTime) {
		this.limitTime = limitTime;
	}

	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}

	public String getQuestionNum() {
		return questionNum;
	}

	public void setQuestionNum(String questionNum) {
		this.questionNum = questionNum;
	}

	public String getQuestionOrder() {
		return questionOrder;
	}

	public void setQuestionOrder(String questionOrder) {
		this.questionOrder = questionOrder;
	}

	public String getMatrixNo() {
		return matrixNo;
	}

	public void setMatrixNo(String matrixNo) {
		this.matrixNo = matrixNo;
	}

	public String getMatrixTitle() {
		return matrixTitle;
	}

	public void setMatrixTitle(String matrixTitle) {
		this.matrixTitle = matrixTitle;
	}

	public boolean isMatrix() {
		return matrix;
	}

	public void setMatrix(boolean matrix) {
		this.matrix = matrix;
	}
	

}
