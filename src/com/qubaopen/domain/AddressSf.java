package com.qubaopen.domain;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * Created by duel on 14-3-26.
 * 地址中的省份
 */

@Table(name = "address_sf")
public class AddressSf {
    @Id(column = "_id")
    private int _id;

    private String sfdm;

    private String mc;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getSfdm() {
        return sfdm;
    }

    public void setSfdm(String sfdm) {
        this.sfdm = sfdm;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }
    
    @Override
	public String toString() {

		return mc;
	}
}
