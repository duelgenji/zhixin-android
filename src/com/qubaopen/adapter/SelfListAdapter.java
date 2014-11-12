package com.qubaopen.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qubaopen.R;
import com.qubaopen.activity.SelfPrefaceActivity;
import com.qubaopen.database.DbManager;

public class SelfListAdapter extends CursorAdapter {

	private Context context;
	private boolean isRetest;

	private class SelfListClickListener implements View.OnClickListener {
		private int selfId;
		private boolean isRetest;

		public SelfListClickListener(int selfId, boolean isRetest) {
			super();
			this.selfId = selfId;
			this.isRetest = isRetest;
		}

		@Override
		public void onClick(View v) {
			if (isRetest) {
				deleteCurrentQuesitonAnswerByselfId(selfId);
			}
			Intent intent;
			intent = new Intent(context, SelfPrefaceActivity.class);
			intent.putExtra(SelfPrefaceActivity.INTENT_SELF_ID, selfId);
			intent.putExtra("isRetest", isRetest);
			context.startActivity(intent);

		}

	}

	public SelfListAdapter(Context context, Cursor c, boolean isRetest) {
		super(context, c, FLAG_REGISTER_CONTENT_OBSERVER);
		this.isRetest = isRetest;
		this.context = context;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.adapter_self_list_item,
				parent, false);

		return updatingContentInView(rowView, cursor);

	}

	@Override
	public void bindView(View rowView, Context context, Cursor cursor) {
		updatingContentInView(rowView, cursor);
	}

	private View updatingContentInView(final View rowView, Cursor cursor) {

		TextView txtTitle = (TextView) rowView.findViewById(R.id.tv_title);
		txtTitle.setText(cursor.getString(cursor.getColumnIndex("title")));

		// int type = cursor.getInt(cursor.getColumnIndex("managementType"));
		// ImageView img_xlzc = (ImageView) rowView.findViewById(R.id.img_xlzc);
		// if (type == 1) {
		// img_xlzc.setImageResource(R.drawable.icon_self_type_character);
		// } else if (type == 2) {
		// img_xlzc.setImageResource(R.drawable.icon_self_type_emotional);
		// } else if (type == 3) {
		// img_xlzc.setImageResource(R.drawable.icon_self_type_personal);
		// }

		int questionnareId = cursor.getInt(cursor.getColumnIndex("selfId"));
		rowView.setOnClickListener(new SelfListClickListener(questionnareId,
				isRetest));

		return rowView;

	}

	private void deleteCurrentQuesitonAnswerByselfId(int selfId) {

		String sql = "delete from self_user_question_answer where questionId="
				+ selfId;
		DbManager.getDatabase().exeCustomerSql(sql);
	}
}
