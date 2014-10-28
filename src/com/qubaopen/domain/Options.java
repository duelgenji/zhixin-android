package com.qubaopen.domain;

public class Options {
	protected boolean isMatrix;
	
	protected String optionNum;

	protected String optionContent;

	protected int optionId;

	protected int questionId;

	
	
	public String getOptionNum() {
		return optionNum;
	}

	public void setOptionNum(String optionNum) {
		this.optionNum = optionNum;
	}

	public String getOptionContent() {
		return optionContent;
	}

	public void setOptionContent(String optionContent) {
		this.optionContent = optionContent;
	}

	public int getOptionId() {
		return optionId;
	}

	public void setOptionId(int optionId) {
		this.optionId = optionId;
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
		result = prime * result + optionId;
		result = prime * result
				+ ((optionNum == null) ? 0 : optionNum.hashCode());
		result = prime * result
				+ ((optionContent == null) ? 0 : optionContent.hashCode());
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
		Options other = (Options) obj;
		if (optionId != other.optionId)
			return false;
		if (optionNum == null) {
			if (other.optionNum != null)
				return false;
		} else if (!optionNum.equals(other.optionNum))
			return false;
		if (optionContent == null) {
			if (other.optionContent != null)
				return false;
		} else if (!optionContent.equals(other.optionContent))
			return false;
		if (isMatrix != other.isMatrix)
			return false;
		if (questionId != other.questionId)
			return false;
		return true;
	}

	
	

}
