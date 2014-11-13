package com.qubaopen.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qubaopen.R;
import com.qubaopen.domain.InterestUserAnswer;

public class InterestHistoryAdapter extends BaseAdapter {
	private Context context;
	private List<InterestUserAnswer> list;
	private ViewHolder viewHolder;

	public InterestHistoryAdapter(Context context) {
		this.context = context;
	}

	public void setList(List<InterestUserAnswer> list, boolean isRefresh) {
		if (list == null) {
			return;
		}
		if (isRefresh) {
			this.list = list;
		} else {
			this.list.addAll(list);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.adapter_interest_history_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.interest_history_questionare);
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.interest_history_time);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (list == null) {
			return convertView;
		}
		InterestUserAnswer interestUserAnswer = list.get(position);
		Log.i("interestHistory", "position......" + position
				+ "interestUserAnswer......" + list.get(position));
		viewHolder.title.setText(interestUserAnswer.getInterestTitle());
		viewHolder.time.setText(interestUserAnswer.getDate());
		viewHolder.title.setTag(interestUserAnswer);
		return convertView;
	}

	public final class ViewHolder {
		public TextView title;
		public TextView time;
	}

}
