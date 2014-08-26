package com.zhixin.adapter;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhixin.R;
import com.zhixin.activity.DiaoyanContentActivity;
import com.zhixin.activity.QuceshiContentActivity;

public class XinliziceListAdapter extends CursorAdapter{

	private int type;

	private XinliziceListAdapter _this;

	private Context context;
	
	private class XinliziceListTouchListener implements View.OnTouchListener {
		private int questionnareId;

		public XinliziceListTouchListener(int questionnareId) {
			this.questionnareId = questionnareId;
		}
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.setBackgroundColor(context.getResources().getColor(
						R.color.general_up_background));
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.setBackgroundColor(context.getResources().getColor(
						R.color.general_activity_background));
				Intent intent;
				switch (_this.type) {
				case 0:
					intent = new Intent(context, QuceshiContentActivity.class);
					intent.putExtra(
							QuceshiContentActivity.INTENT_QUESIONNARE_ID,
							questionnareId);
					context.startActivity(intent);
					break;
				case 1:
					intent = new Intent(context, DiaoyanContentActivity.class);
					intent.putExtra(
							DiaoyanContentActivity.INTENT_QUESIONNARE_ID,
							questionnareId);
					context.startActivity(intent);
					break;
				default:
					break;
				}
			} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
				v.setBackgroundColor(context.getResources().getColor(
						R.color.general_activity_background));
			}
			return true;
		}

	}
	public XinliziceListAdapter(Context context, Cursor c, int type) {
		super(context, c, FLAG_REGISTER_CONTENT_OBSERVER);
		_this = this;
		this.context = context;
		this.type = type;
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.adapter_xinlizice_qingxu_item,
				parent, false);

		return updatingContentInView(rowView, cursor);

	}

	@Override
	public void bindView(View rowView, Context context, Cursor cursor) {
		updatingContentInView(rowView, cursor);
	}

	private View updatingContentInView(final View rowView, Cursor cursor) {

		TextView questionareTitle = (TextView) rowView
				.findViewById(R.id.tv_title);
		// necessary injection
		questionareTitle.setText(cursor.getString(cursor
				.getColumnIndex("title")));

		// tag injection

		ImageView  img_xlzc = (ImageView) rowView.findViewById(R.id.img_xlzc);

		String tags = cursor.getString(cursor.getColumnIndex("tags"));
		if (StringUtils.isNotEmpty(tags)) {
			String[] tagArray = tags.split(";");
/**
			for (int i = 0; i < tagArray.length; i++) {
				int index = Integer.parseInt(tagArray[i]);
				FrameLayout frame = frameList.get(i);

				ImageView tagImage = (ImageView) frame
						.findViewById(R.id.tagImage);
				TextView tagText = (TextView) frame.findViewById(R.id.tagText);

				switch (index) {
				case 1:
					tagImage.setImageResource(R.drawable.qu_tuijian_icon);
					tagText.setText(context
							.getString(R.string.quceshi_biaoqian_tuijian));

					break;
				case 2:

					tagImage.setImageResource(R.drawable.qu_remeng_icon);
					tagText.setText(context
							.getString(R.string.quceshi_biaoqian_remeng));

					break; 
				case 3:

					tagImage.setImageResource(R.drawable.qu_zuixing_icon);
					tagText.setText(context
							.getString(R.string.quceshi_biaoqian_zuixin));

					break;
				case 4:

					tagImage.setImageResource(R.drawable.qu_xianshi_icon);
					tagText.setText(context
							.getString(R.string.quceshi_biaoqian_xianshi));

					break;
				case 5:

					tagImage.setImageResource(R.drawable.qu_shaoliang_icon);
					tagText.setText(context
							.getString(R.string.quceshi_biaoqian_shaoliang));

					break;

				case 6:

					tagImage.setImageResource(R.drawable.qu_jinbi_icon);
					tagText.setText(context
							.getString(R.string.quceshi_biaoqian_jinbi));

					break;
				default:
					break;
				}
				frame.setVisibility(View.VISIBLE);
			}
 */
		}
		int questionnareId = cursor.getInt(cursor
				.getColumnIndex("questionnarieId"));
		rowView.setOnTouchListener(new XinliziceListTouchListener(questionnareId));

		return rowView;

	}
	

}
