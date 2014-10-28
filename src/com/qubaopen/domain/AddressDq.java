package com.qubaopen.domain;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * Created by duel on 14-3-26.
 * 地址中的地区
 */
@Table(name = "address_dq")
public class AddressDq {
    @Id(column = "_id")
    private int _id;

//地区
    private String dqdm;

    private String mc;
//城市
    private String csdm;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getDqdm() {
        return dqdm;
    }

    public void setDqdm(String dqdm) {
        this.dqdm = dqdm;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getCsdm() {
        return csdm;
    }

    public void setCsdm(String csdm) {
        this.csdm = csdm;
    }
    
    @Override
	public String toString() {

		return mc;
	}
}
