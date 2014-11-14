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
import com.qubaopen.domain.SelfList;

public class SelfRetestListAdapter extends BaseAdapter {
	private Context context;
	private List<SelfList> list;
	private ViewHolder viewHolder;

	public SelfRetestListAdapter(Context context) {
		this.context = context;
	}

	public void setList(List<SelfList> list) {
		if (list == null) {
			return;
		}
			this.list = list;
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
					R.layout.adapter_self_retest_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.self_retest_title);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (list == null) {
			return convertView;
		}
		SelfList selfList = list.get(position);
		Log.i("interestHistory", "position......" + position
				+ "interestUserAnswer......" + list.get(position));
		viewHolder.title.setText(selfList.getTitle());
		viewHolder.title.setTag(selfList.getSelfId());
		return convertView;
	}

	public final class ViewHolder {
		public TextView title;
	}

}
