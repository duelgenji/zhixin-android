package com.qubaopen.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.qubaopen.R;

/**
 * 日历月视图
 *
 * @author zhouyanbin
 */
public class CalendarCard extends GridView implements AdapterView.OnItemClickListener {

    private OnItemRender mOnItemRender;
    private OnCellItemClick mOnCellItemClick;
    private Calendar dateDisplay;
    private int firstDayItem = 0;
    private int lastDayItem = 0;
    public static final int CELLS_COUNTS = 42;
    public static final String TAG = "CalendarCard";
    private CardAdapter adapter;
    private List<CardGridItem> gridItems = new ArrayList<CardGridItem>();

    public CalendarCard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public CalendarCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarCard(Context context) {
        super(context);
        init(context);
    }

    @SuppressLint("NewApi") private void init(Context ctx) {
        setSelector(new ColorDrawable(Color.parseColor("#00000000")));
        setNumColumns(7);
        setHorizontalSpacing(0);
        setVerticalSpacing(0);
        if ( Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB ){
            setChoiceMode(CHOICE_MODE_SINGLE);
        }
        setPadding(0, 0, 0, 0);
        if (dateDisplay == null)
            dateDisplay = Calendar.getInstance();
        adapter = new CardAdapter();
        super.setAdapter(adapter);
        super.setOnItemClickListener(this);
    }


    @Override
    public void setAdapter(ListAdapter adapter) {
        throw new RuntimeException("You can not call this method,because it has bean override!");
    }


    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        throw new RuntimeException("You can not call this method,because it has bean override!");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CardGridItem item = (CardGridItem) adapter.getItem(position);
        if (item != null && item.isEnabled()) {
           clearSelectState();
            CheckableLayout layout = (CheckableLayout) getChildAt(position);
            layout.setChecked(true);
            if (mOnCellItemClick != null) {
                mOnCellItemClick.onCellClick(view, item);
            }
        } else {
            if (mOnCellItemClick != null) {
                mOnCellItemClick.onClickOutSide();
            }
        }
    }

    public void clearSelectState() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            CheckableLayout child = (CheckableLayout) getChildAt(i);
            if (child != null) {
                child.setChecked(false);
            }
        }
    }

    private class CardAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        CardAdapter() {
            this.inflater = LayoutInflater.from(getContext());
        }

        @Override
        public int getCount() {
            return gridItems.size();
        }

        @Override
        public Object getItem(int position) {
            return gridItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            long start = System.currentTimeMillis();
            if (convertView == null) {
                holder = new ViewHolder();
                holder.getView().setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            CardGridItem item = (CardGridItem) getItem(position);
            if (mOnItemRender != null) {
                mOnItemRender.onRender(holder.getView(), item);
            }
            if (item != null) {
                holder.getView().setEnabled(item.isEnabled());
                holder.getView().setChecked(item.isChecked());
                holder.getText().setText(item.getDayOfMonth().toString());
            }
            return holder.getView();
        }

        private class ViewHolder {
            private CheckableLayout view;
            private CheckedTextView text;

            public CheckableLayout getView() {
                if (view == null) {
                    view = (CheckableLayout) inflater.inflate(R.layout.calendar_card_cell, null);
                }
                return view;
            }

            public CheckedTextView getText() {
                if (text == null) {
                    text = (CheckedTextView) getView().findViewById(R.id.calendar_item_text);
                }
                return text;
            }

        }


    }




    public List<CardGridItem> getGridItem() {
        return gridItems;
    }

    /**
     * 本月的第一天的星期数 和 周日相差几天
     *
     * @param calendar
     * @return
     */
    private int getDaySpacing(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (Calendar.SUNDAY == dayOfWeek) {
            cal.set(Calendar.DAY_OF_MONTH, 1);
            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            int dy = CELLS_COUNTS - lastDay;
            if (dy > 7) {
                return 7;
            } else {
                return 0;
            }
        } else {
            return dayOfWeek - 1;
        }
    }

    private void updateCells() {
        gridItems.clear();
        Calendar cal;
        Integer counter = 0;
        if (dateDisplay != null)
            cal = (Calendar) dateDisplay.clone();
        else
            cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, 1);

        //本月的第一天的星期数 和 周日相差几天
        int daySpacing = getDaySpacing(cal);

        //相差几天就把上个月的几天补齐
        if (daySpacing > 0) {
            Calendar prevMonth = (Calendar) cal.clone();
            prevMonth.add(Calendar.MONTH, -1);
            prevMonth.set(Calendar.DAY_OF_MONTH, prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH) - daySpacing + 1);
            for (int i = 0; i < daySpacing; i++) {
                CardGridItem item = new CardGridItem(Integer.valueOf(prevMonth.get(Calendar.DAY_OF_MONTH)))
                        .setEnabled(false).setDate((Calendar) prevMonth.clone());
                gridItems.add(item);
                counter++;
                prevMonth.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        //填充本月的所有天数
        int firstDay = cal.get(Calendar.DAY_OF_MONTH);
        firstDayItem = counter;
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        int lastDay = cal.get(Calendar.DAY_OF_MONTH) + 1;
        for (int i = firstDay; i < lastDay; i++) {
            cal.set(Calendar.DAY_OF_MONTH, i - 1);
            Calendar date = (Calendar) cal.clone();
            date.add(Calendar.DAY_OF_MONTH, 1);
            CardGridItem item = new CardGridItem(i).setEnabled(true).setDate(date);
            gridItems.add(item);
            counter++;
        }

        if (dateDisplay != null)
            cal = (Calendar) dateDisplay.clone();
        else
            cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Calendar nextMonth = (Calendar) cal.clone();
        nextMonth.add(Calendar.MONTH, 1);
        //把剩下的单元格用下个月的天数补齐
        int offset = 0;
        lastDayItem = counter;
        for (int i = counter; i < CELLS_COUNTS; i++) {
            nextMonth.set(Calendar.DAY_OF_MONTH, offset + 1);
            CardGridItem item = new CardGridItem(offset + 1).setEnabled(false).setDate((Calendar) nextMonth.clone()); // .setDate((Calendar)cal.clone())
            gridItems.add(item);
            counter++;
            offset++;
        }
    }


    public int getFirstDayItem() {
        return firstDayItem;
    }

    public int getLastDayItem() {
        return lastDayItem;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() > 0) {
            int size = (int) ((r - l) / 1.2f / 6f);
            int counts = getChildCount();
            for (int i = 0; i < counts; i++) {
                View child = getChildAt(i);
                if (child != null && child.getLayoutParams() != null) {
                    child.getLayoutParams().height = size;
                }
            }
        }
        super.onLayout(changed, l, t, r, b);

    }


    public OnItemRender getOnItemRender() {
        return mOnItemRender;
    }

    public void setOnItemRender(OnItemRender mOnItemRender) {
        this.mOnItemRender = mOnItemRender;
    }

    public Calendar getDateDisplay() {
        return dateDisplay;
    }

    public void setDateDisplay(Calendar dateDisplay) {
        this.dateDisplay = dateDisplay;
    }

    public OnCellItemClick getOnCellItemClick() {
        return mOnCellItemClick;
    }

    public void setOnCellItemClick(OnCellItemClick mOnCellItemClick) {
        this.mOnCellItemClick = mOnCellItemClick;
    }

    /**
     * 刷新
     */
    public void notifyChanges() {
        updateCells();
        adapter.notifyDataSetChanged();
    }
}
