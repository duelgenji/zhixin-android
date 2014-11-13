package com.qubaopen.calendar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.Calendar;
import java.util.List;

/**
 * 日历组件 可以直接写在xml中
 *
 * @author zhouyanbin
 * @time 2014/7/31
 */
public class CalendarCardPager extends ViewPager {

    private CardPagerAdapter mCardPagerAdapter;
    private OnCellItemClick mOnCellItemClick;
    private OnMonthChangedListener onMonthChangedListener;
    public static final int TODAY = CardPagerAdapter.TODAY;
    private Calendar currentMonth;


    private OnPageChangeListener listener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int i) {
            if (onMonthChangedListener != null) {
                int realPosition = i - TODAY;
                currentMonth = Calendar.getInstance();
                currentMonth.add(Calendar.MONTH , realPosition);
                onMonthChangedListener.monthChanged(currentMonth);
                checkTodayOrFirstDay(currentMonth);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };


    @Override
    public void setCurrentItem(int item) {
        if (item == getCurrentItem()) {
            listener.onPageSelected(item);
        } else {
            super.setCurrentItem(item);
        }
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (item == getCurrentItem()) {
            listener.onPageSelected(item);
        } else {
            super.setCurrentItem(item, smoothScroll);
        }
    }

    public CalendarCardPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        init(context);
    }

    public CalendarCardPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarCardPager(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                listener.onPageSelected(TODAY);
            }
        });
        setOffscreenPageLimit(1);
        currentMonth = Calendar.getInstance();
        mCardPagerAdapter = new CardPagerAdapter(getContext());
        setAdapter(mCardPagerAdapter);
        setCurrentItem(TODAY, false);
        super.setOnPageChangeListener(listener);
        
    }


    public List<CalendarCard> getCards() {
        return mCardPagerAdapter.getCalendarCards();
    }

    /**
     * 选中今天或者第一天
     *
     * @param calendar
     */
    private void checkTodayOrFirstDay(Calendar calendar) {

        List<CalendarCard> cards = mCardPagerAdapter.getCalendarCards();
        if (cards == null || cards.size() == 0) {
            return;
        }
        CalendarCard card = cards.get(getCurrentItem() % cards.size());
        Calendar cal = Calendar.getInstance();
        if (card != null) {
            int firstDay = card.getFirstDayItem();
            if (calendar.get(Calendar.YEAR) == cal.get(Calendar.YEAR)//如果是现在这个月的话
                    && calendar.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
                    ) {
                int today = cal.get(Calendar.DAY_OF_MONTH);
                firstDay += today;
                firstDay--;
            }
            card.performItemClick(card.getChildAt(firstDay), firstDay, firstDay);
        }
    }


    public void notifyChanged() {
        if (mCardPagerAdapter != null) {
            mCardPagerAdapter.notifyDataSetChanged();
        }
    }

    public OnItemRender getOnItemRender() {
        return mCardPagerAdapter.getOnItemRender();
    }

    public void setOnItemRender(OnItemRender onItemRender) {
        mCardPagerAdapter.setOnItemRender(onItemRender);
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                if (v instanceof CalendarCard) {
                    ((CalendarCard) v).setOnItemRender(onItemRender);
                }
            }
        }
    }

    public CardPagerAdapter getCardPagerAdapter() {
        return mCardPagerAdapter;
    }

    public OnCellItemClick getOnCellItemClick() {
        return mOnCellItemClick;
    }

    public void setOnCellItemClick(OnCellItemClick mOnCellItemClick) {
        this.mOnCellItemClick = mOnCellItemClick;
        mCardPagerAdapter.setDefaultOnCellItemClick(this.mOnCellItemClick);
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                if (v instanceof CalendarCard) {
                    ((CalendarCard) v).setOnCellItemClick(this.mOnCellItemClick);
                }
            }
        }
    }

    public OnMonthChangedListener getOnMonthChangedListener() {
        return onMonthChangedListener;
    }

    public void setOnMonthChangedListener(OnMonthChangedListener onMonthChangedListener) {
        this.onMonthChangedListener = onMonthChangedListener;
    }

    public interface OnMonthChangedListener {
        public void monthChanged(Calendar calendar);
    }


    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        throw new RuntimeException("You can not call this method,because it has bean override. please call setOnMonthChangedListener()!");
    }

}
