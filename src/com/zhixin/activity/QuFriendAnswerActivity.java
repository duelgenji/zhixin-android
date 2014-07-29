package com.zhixin.activity;

import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.customui.QuFriendAnswerGroup;
import com.zhixin.datasynservice.QuceshiAnswerService;
import com.zhixin.dialog.InstructionDialog;
import com.zhixin.domain.QuUserWjAnswer;
import com.zhixin.domain.QuWjAnswerList;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.SqlCursorLoader;

public class QuFriendAnswerActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

	public static final String INTENT_WJ_ID = "intentwjid";

	public QuceshiAnswerService quceshiAnswerService;

	private static final int LOADER_WJ_TITLE = 1;
	private static final int LOADER_ANSWER_LIST = 2;

	private TextView titleOfThePage;
	private TextView wjTitle;

	private LinearLayout container;

	private int wjId;
	
	private QuFriendAnswerActivity _this;

	private class LoadDataTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			try {
				return quceshiAnswerService.getFriendAnswer(wjId);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String params) {
			if (params == null) {
				getSupportLoaderManager().restartLoader(LOADER_ANSWER_LIST,
						null, QuFriendAnswerActivity.this);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quceshi_friend_answer);
		_this = this;
		

		quceshiAnswerService = new QuceshiAnswerService(this);

		this.findViewById(R.id.backup_btn).setOnClickListener(this);
		titleOfThePage = (TextView) this.findViewById(R.id.title_of_the_page);
		titleOfThePage.setText(R.string.title_quceshi);

		wjTitle = (TextView) this.findViewById(R.id.wjTitle);
		container = (LinearLayout) this.findViewById(R.id.container);

		wjId = this.getIntent().getIntExtra(INTENT_WJ_ID, 0);

		this.getSupportLoaderManager().restartLoader(LOADER_WJ_TITLE, null,
				this);

		new LoadDataTask().execute();
		
		
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				SharedPreferences sharedPref = _this
						.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
								Context.MODE_PRIVATE);
				return sharedPref.getBoolean(
						SettingValues.INSTRUCTION_FRIEND_ANSWER, true);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					new InstructionDialog(_this,
							SettingValues.INSTRUCTION_FRIEND_ANSWER).show();
				}
			}

		}.execute();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		switch (id) {
		case LOADER_WJ_TITLE:
			return new SqlCursorLoader(this,
					"select * from qu_user_wj_answer where wjId=" + wjId
							+ " limit 1", QuUserWjAnswer.class);
		case LOADER_ANSWER_LIST:
			return new SqlCursorLoader(this,
					QuceshiAnswerService.QuceshiAnswerSqlMaker.makeSql(wjId),
					QuWjAnswerList.class);
		default:
			return null;
		}

	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		switch (loader.getId()) {
		case LOADER_WJ_TITLE:
			cursor.moveToFirst();
			wjTitle.setText(cursor.getString(cursor.getColumnIndex("wjTitle")));
			break;
		case LOADER_ANSWER_LIST:
			cursor.moveToFirst();
			do {
				QuFriendAnswerGroup quFriendAnswerGroup = new QuFriendAnswerGroup(
						this,
						cursor.getString(cursor.getColumnIndex("answerNo")),
						cursor.getString(cursor.getColumnIndex("answerTitle")),
						cursor.getString(cursor.getColumnIndex("answerContent")),
						cursor.getInt(cursor.getColumnIndex("wjId")));

				container.addView(quFriendAnswerGroup);

				if (!cursor.isLast()) {
					View splitView = new View(this);
					LayoutParams lp = new LayoutParams(
							LayoutParams.MATCH_PARENT, 1);
					splitView.setLayoutParams(lp);
					splitView
							.setBackgroundResource(R.drawable.dati_xxfgx);
					container.addView(splitView);
				}

			} while (cursor.moveToNext());

			break;
		default:
			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backup_btn:
			v.setEnabled(false);
			this.onBackPressed();
			break;
		default:
			break;
		}
	}

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
