package com.qubaopen.calendar;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 日历Adapter
 * @author zhouyanbin
 */
public class CardPagerAdapter extends PagerAdapter {

    private Context mContext;
    private OnCellItemClick defaultOnCellItemClick;
    public static final int MAX_COUNT = 500;
    public static final int TODAY = MAX_COUNT/ 2;
    private OnItemRender onItemRender;
    private List<CalendarCard> calendarCards = new ArrayList<CalendarCard>();


    public List<CalendarCard> getCalendarCards() {
        return calendarCards;
    }

    public CardPagerAdapter(Context ctx) {
        mContext = ctx;
        calendarCards.add(null);
        calendarCards.add(null);
        calendarCards.add(null);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = position - TODAY;
        int viewPosition = position % calendarCards.size();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, realPosition);
        CalendarCard card = calendarCards.get(viewPosition);
        if ( card == null ) {
            card = new CalendarCard(mContext);
            calendarCards.set(viewPosition, card);
        }
        ViewGroup parent = ( ViewGroup ) card.getParent();
        if ( parent != null ) {
            parent.removeView(card);
        }
        card.setOnCellItemClick(defaultOnCellItemClick);
        card.setOnItemRender(onItemRender);
        card.setDateDisplay(cal);
        card.notifyChanges();
        card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        try {
            (( ViewPager ) container).addView(card,0);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return card;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (( View ) object);
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return MAX_COUNT;
    }

    public OnCellItemClick getDefaultOnCellItemClick() {
        return defaultOnCellItemClick;
    }

    public void setDefaultOnCellItemClick(OnCellItemClick defaultOnCellItemClick) {
        this.defaultOnCellItemClick = defaultOnCellItemClick;
    }

    public void setOnItemRender(OnItemRender onItemRender) {
        this.onItemRender = onItemRender;
    }

    public OnItemRender getOnItemRender() {
        return onItemRender;
    }
}
