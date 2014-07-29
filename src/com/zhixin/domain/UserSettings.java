package com.zhixin.domain;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

import java.sql.Time;

/**
 * Created by duel on 14-3-7.
 */

@Table(name = "user_settings")
public class UserSettings {
    @Id(column = "_id")
    /**id*/
    private int _id;
    /**是否提示*/
    private Integer isTs;
    /**是否对好友公开问卷*/
    private Integer isHygkwj;
    /**是否对门主公开问卷*/
    private Integer isMzgkwj;
    /**是否公开答题*/
    private Integer isGkdt;
    /**是否开启省流量模式*/
    private Integer isSllms;
    /**开始时间*/
    private String kssj;
    /**结束时间*/
    private String jssj;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Integer getIsTs() {
        return isTs;
    }

    public void setIsTs(Integer isTs) {
        this.isTs = isTs;
    }

    public Integer getIsHygkwj() {
        return isHygkwj;
    }

    public void setIsHygkwj(Integer isHygkwj) {
        this.isHygkwj = isHygkwj;
    }

    public Integer getIsMzgkwj() {
        return isMzgkwj;
    }

    public void setIsMzgkwj(Integer isMzgkwj) {
        this.isMzgkwj = isMzgkwj;
    }

    public Integer getIsGkdt() {
        return isGkdt;
    }

    public void setIsGkdt(Integer isGkdt) {
        this.isGkdt = isGkdt;
    }

    public Integer getIsSllms() {
        return isSllms;
    }

    public void setIsSllms(Integer isSllms) {
        this.isSllms = isSllms;
    }

    public String getKssj() {
        return kssj;
    }

    public void setKssj(String kssj) {
        this.kssj = kssj;
    }

    public String getJssj() {
        return jssj;
    }

    public void setJssj(String jssj) {
        this.jssj = jssj;
    }
}
