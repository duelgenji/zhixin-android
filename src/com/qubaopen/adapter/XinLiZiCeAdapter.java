package com.qubaopen.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qubaopen.R;

public class XinLiZiCeAdapter extends BaseAdapter{
	private Context context;
	private List<WenJuanShuJu> list;
	public XinLiZiCeAdapter(Context context,List<WenJuanShuJu> list) {
		this.context = context;
		this.list = list;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(
				R.layout.adapter_self_list_item, null);
		LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.ll_xlzc);
//		ImageView img_xlzc = (ImageView) convertView.findViewById(R.id.img_xlzc);
		TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
//		String type = list.get(position).getManagementType();
		String titile = list.get(position).getTitile();
//		if(type.equals("Personal")){
//			img_xlzc.setImageResource(R.drawable.icon_self_type_character);
//		}else if (type.equals("Character")) {
//			img_xlzc.setImageResource(R.drawable.icon_self_type_personal);
//		}else if (type.equals("Emotional")) {
//			img_xlzc.setImageResource(R.drawable.icon_self_type_emotional);
//		}
		tv_title.setText(titile);
		return convertView;
	}

}
