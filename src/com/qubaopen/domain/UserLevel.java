package com.qubaopen.domain;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "user_level")
public class UserLevel {

	@Id(column = "_id")
	private int _id;

	private String mc;

	private int zdjf;

	private int bbslXz;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getMc() {
		return mc;
	}

	public void setMc(String mc) {
		this.mc = mc;
	}

	public int getZdjf() {
		return zdjf;
	}

	public void setZdjf(int zdjf) {
		this.zdjf = zdjf;
	}

	public int getBbslXz() {
		return bbslXz;
	}

	public void setBbslXz(int bbslXz) {
		this.bbslXz = bbslXz;
	}

}
