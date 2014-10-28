package com.qubaopen.enums;

public enum QuestionTypeEnums {
	DANXUAN(1), DUOXUAN(2), WENDA(3), SHUNXU(4),DAFEN(5);
 
	private int typeCode;
 
	private QuestionTypeEnums(int s) {
		typeCode = s;
	}

	public int getTypeCode() {
		return typeCode;
	}
	
}