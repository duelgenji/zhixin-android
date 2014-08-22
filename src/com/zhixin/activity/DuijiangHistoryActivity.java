package com.zhixin.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.adapter.HistoryDuijiangAdapter;
import com.zhixin.datasynservice.HistoryDuijiangService;
import com.zhixin.dialog.HistoryDuijiangStatusPicker;
import com.zhixin.dialog.QubaopenProgressDialog;
import com.zhixin.domain.HistoryDuijiang;
import com.zhixin.utils.SqlCursorLoader;
/**
 * 兑奖历史页面 （页头有一个按钮还需要改动）
 * @author Administrator
 *
 */
public class DuijiangHistoryActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

	private DuijiangHistoryActivity _this;

//	private View duijiangRecordComp;
//	private View duijiangStatusComp;
	private Button syncDataBtn;
//	private DiaoyanHistoryTabBarLayout radioGroup;
	private ListView listView;
	private View nothingIntheListView;
	private ImageButton iBtnPageBack;

	private HistoryDuijiangAdapter adapter;

	private TextView leftIndicatorTxt;
	private TextView rightIndicatorTxt;

	private HistoryDuijiangStatusPicker statusPicker;
	

	private HistoryDuijiangService service;

	private int status;
	private int record;

	private QubaopenProgressDialog progressDialog;

	private class LoadDataTask extends AsyncTask<Integer, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Integer... params) {
			try {
				return service.refreshData();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean params) {
			if (params != null && params) {
				getSupportLoaderManager().restartLoader(0, null, _this);
			}
		}
	}

/**
  	private class RecordPickerDialogDismissListener implements

			DialogInterface.OnDismissListener {

		@Override
		public void onDismiss(DialogInterface arg0) {

			if (recordPicker.isPickOrderOrNot()) {
				record = recordPicker.getRecord();
				leftIndicatorTxt.setText(recordPicker.getRecordStr());
				getSupportLoaderManager().restartLoader(0, null, _this);

			}
		}
	}
 */
	private class StatusPickerDialogDismissListener implements
			DialogInterface.OnDismissListener {

		@Override
		public void onDismiss(DialogInterface arg0) {

			if (statusPicker.isPickOrderOrNot()) {
				status = statusPicker.getStatus();
				rightIndicatorTxt.setText(statusPicker.getStatusStr());
				getSupportLoaderManager().restartLoader(0, null, _this);

			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_duijiang_history);
		_this = this;
		init();
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

	private void init() {
		status = 8;
		record = 3;

		progressDialog = new QubaopenProgressDialog(this);

		service = new HistoryDuijiangService(this);
		listView = (ListView) this.findViewById(R.id.listView);
		nothingIntheListView = this.findViewById(R.id.nothingIntheListView);
		syncDataBtn = (Button) this.findViewById(R.id.syncDataBtn);

//		leftIndicatorTxt = (TextView) this.findViewById(R.id.leftIndicatorTxt);
//		rightIndicatorTxt = (TextView) this
//				.findViewById(R.id.rightIndicatorTxt);

//		duijiangRecordComp = this.findViewById(R.id.duijiangRecordComp);
//		duijiangStatusComp = this.findViewById(R.id.duijiangStatusComp);
//		radioGroup = (DiaoyanHistoryTabBarLayout) this
//				.findViewById(R.id.radioBtnGroup);

		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);
		((TextView) this.findViewById(R.id.title_of_the_page))
				.setText(getString(R.string.title_duijiang_history));

//		duijiangRecordComp.setOnClickListener(this);
		syncDataBtn.setOnClickListener(this);

		progressDialog.show();
		new LoadDataTask().execute();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		String sql = "select * from history_duijiang";
		List<String> sqlplus = new ArrayList<String>();
		if (status != 8) {
			sqlplus.add("status=" + status);
		}
		if (record != 3) {
			sqlplus.add("type=" + record);
		}
		boolean firstTime = true;
		for (String plusStr : sqlplus) {
			if (firstTime) {
				sql = sql + " where " + plusStr;
				firstTime = false;
			} else {
				sql = sql + " and " + plusStr;
			}

		}
		sql = sql + " order by date desc";
		return new SqlCursorLoader(this, sql, HistoryDuijiang.class);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (cursor.getCount() == 0) {
		
			if (!nothingIntheListView.isShown()) {
				nothingIntheListView.setVisibility(View.VISIBLE);
			}

		} else {

			if (nothingIntheListView.isShown()) {
				nothingIntheListView.setVisibility(View.GONE);
			}

		}
		if (adapter == null) {
			adapter = new HistoryDuijiangAdapter(this, cursor);
			listView.setAdapter(adapter);
			adapter.setHistoryAcitivty(this);

		} else {
			adapter.changeCursor(cursor);
		}
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		if (adapter != null) {
			adapter.changeCursor(null);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backup_btn:
			this.onBackPressed();
			v.setEnabled(true);
			break;
/**		case R.id.duijiangRecordComp:
			radioGroup.setLeftChecked();
			if (recordPicker == null) {
				int[] topAndLeftMarginOfRecordPicker = new int[2];
				recordPicker = new HistoryDuijiangRecordPicker(this);
				duijiangRecordComp
						.getLocationOnScreen(topAndLeftMarginOfRecordPicker);
				recordPicker.setMarginTopAndLeft(
						topAndLeftMarginOfRecordPicker[1],
						topAndLeftMarginOfRecordPicker[0]);

				recordPicker
						.setOnDismissListener(new RecordPickerDialogDismissListener());
			}
			if (!recordPicker.isShowing()) {

				recordPicker.show();
			}

			break;
			*/
		case R.id.syncDataBtn:
		//	radioGroup.setRightChecked();
			if (statusPicker == null) {
				statusPicker = new HistoryDuijiangStatusPicker(this);
				int[] topAndLeftMarginOfStatusPicker = new int[2];
				syncDataBtn
						.getLocationOnScreen(topAndLeftMarginOfStatusPicker);
				statusPicker.setMarginTopAndLeft(
						topAndLeftMarginOfStatusPicker[1],
						topAndLeftMarginOfStatusPicker[0]);
				statusPicker
						.setOnDismissListener(new StatusPickerDialogDismissListener());
			}

			if (!statusPicker.isShowing()) {

				statusPicker.show();
			}
			break;

		default:
			break;

		}

	}

}
