package com.qubaopen.adapter;

public class WenJuanShuJu {
	private long  selfId;
    private String managementType;
    private String titile;
	@Override
	public String toString() {
		return "WenJuanShuJu [selfId=" + selfId + ", managementType="
				+ managementType + ", titile=" + titile + "]";
	}
	public long getSelfId() {
		return selfId;
	}
	public void setSelfId(long selfId) {
		this.selfId = selfId;
	}
	public String getManagementType() {
		return managementType;
	}
	public void setManagementType(String managementType) {
		this.managementType = managementType;
	}
	public String getTitile() {
		return titile;
	}
	public void setTitile(String titile) {
		this.titile = titile;
	}
    
}
