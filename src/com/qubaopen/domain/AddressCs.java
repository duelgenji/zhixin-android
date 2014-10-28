package com.qubaopen.domain;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * Created by duel on 14-3-26.
 * 地址中的城市
 */
@Table(name = "address_cs")
public class AddressCs {
	@Id(column = "_id")
	private int _id;
//	城市
	private String csdm;

	private String mc;
//  省份
	private String sfdm;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getCsdm() {
		return csdm;
	}

	public void setCsdm(String csdm) {
		this.csdm = csdm;
	}

	public String getMc() {
		return mc;
	}

	public void setMc(String mc) {
		this.mc = mc;
	}

	public String getSfdm() {
		return sfdm;
	}

	public void setSfdm(String sfdm) {
		this.sfdm = sfdm;
	}

	@Override
	public String toString() {

		return mc;
	}
	
}
