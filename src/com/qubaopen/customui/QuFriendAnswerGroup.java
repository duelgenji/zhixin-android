package com.qubaopen.customui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qubaopen.R;
import com.qubaopen.adapter.QuFriendAnswerItem;
import com.qubaopen.database.DbManager;
import com.qubaopen.datasynservice.QuceshiAnswerService;
import com.qubaopen.domain.QuFriendAnswer;

public class QuFriendAnswerGroup extends LinearLayout {

	private Context context;

	private TextView answerTitleTextView;
	private TextView answerContentTextView;

	private HorizontalListView friendAnswerList;

	private Cursor cursorOfAdapter;

	private QuFriendAnswerItem adapter;

	private int wjId;

	private String choiceNo;

	private class GetDataRunnable implements Runnable {
		@Override
		public void run() {

			cursorOfAdapter = DbManager.getDatabase().findAllBySqlReturnCursor(
					QuFriendAnswer.class,
					QuceshiAnswerService.QuceshiAnswerSqlMaker.makeFriendSql(
							wjId, choiceNo));
			QuFriendAnswerGroup.this.post(new Runnable() {
				@Override
				public void run() {
					if (cursorOfAdapter.getCount() > 0) {
						if (adapter == null) {
							adapter = new QuFriendAnswerItem(context,
									cursorOfAdapter);
						}
						friendAnswerList.setAdapter(adapter);
					} else {

						friendAnswerList.setVisibility(View.GONE);
					}
				}
			});
		}

	}

	public QuFriendAnswerGroup(Context context, String answerNo,
			String answerTitle, String answerContent, int answerId) {
		super(context);

		init(context, answerNo, answerTitle, answerContent, answerId);

	}

	private void init(Context context, String answerNo, String answerTitle,
			String answerContent, int wjId) {
		this.context = context;
		this.wjId = wjId;
		this.choiceNo = answerNo;
		LayoutInflater.from(context).inflate(R.layout.customui_friend_answer,
				this);
		answerTitleTextView = (TextView) this.findViewById(R.id.answerTitle);
		answerContentTextView = (TextView) this
				.findViewById(R.id.answerContent);
		friendAnswerList = (HorizontalListView) this
				.findViewById(R.id.friendAnswerList);

		if (answerTitle == null || answerContent == null) {
			if (answerTitle == null) {
				answerTitleTextView.setText(answerNo + "." + answerContent);
			} else if (answerContent == null) {
				answerTitleTextView.setText(answerNo + "." + answerTitle);

			}
			answerContentTextView.setVisibility(View.GONE);
		} else {
			answerTitleTextView.setText(answerNo + "." + answerTitle);
			answerContentTextView.setText(answerContent);
		}

		new Thread(new GetDataRunnable()).start();

	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (cursorOfAdapter != null && !cursorOfAdapter.isClosed()) {
			cursorOfAdapter.close();
		}

	}
}
