package com.qubaopen.calendar;

import android.view.View;

public interface OnCellItemClick {
	
	public void onCellClick(View v, CardGridItem item);

    /**
     * 点击了本月日期之外的
     */
    public void onClickOutSide();

}
