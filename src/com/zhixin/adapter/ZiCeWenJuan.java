package com.zhixin.adapter;

import java.util.LinkedList;

public class ZiCeWenJuan {
	private String success;
	private String message;
	private LinkedList<WenJuanShuJu> list;
	@Override
	public String toString() {
		return "ZiCeWenJuan [success=" + success + ", message=" + message
				+ ", list=" + list + "]";
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public LinkedList<WenJuanShuJu> getList() {
		return list;
	}
	public void setList(LinkedList<WenJuanShuJu> list) {
		this.list = list;
	}
	
	
}
