package com.qubaopen.domain;

public class Choices {
	protected boolean isMatrix;
	protected String choiceNo;

	protected String choiceTitle;

	protected int choiceId;

	protected int questionId;

	public String getChoiceNo() {
		return choiceNo;
	}

	public void setChoiceNo(String choiceNo) {
		this.choiceNo = choiceNo;
	}

	public String getChoiceTitle() {
		return choiceTitle;
	}

	public void setChoiceTitle(String choiceTitle) {
		this.choiceTitle = choiceTitle;
	}

	public int getChoiceId() {
		return choiceId;
	}

	public void setChoiceId(int choiceId) {
		this.choiceId = choiceId;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public boolean isMatrix() {
		return isMatrix;
	}

	public void setMatrix(boolean isMatrix) {
		this.isMatrix = isMatrix;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + choiceId;
		result = prime * result
				+ ((choiceNo == null) ? 0 : choiceNo.hashCode());
		result = prime * result
				+ ((choiceTitle == null) ? 0 : choiceTitle.hashCode());
		result = prime * result + (isMatrix ? 1231 : 1237);
		result = prime * result + questionId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Choices other = (Choices) obj;
		if (choiceId != other.choiceId)
			return false;
		if (choiceNo == null) {
			if (other.choiceNo != null)
				return false;
		} else if (!choiceNo.equals(other.choiceNo))
			return false;
		if (choiceTitle == null) {
			if (other.choiceTitle != null)
				return false;
		} else if (!choiceTitle.equals(other.choiceTitle))
			return false;
		if (isMatrix != other.isMatrix)
			return false;
		if (questionId != other.questionId)
			return false;
		return true;
	}

	
	

}
