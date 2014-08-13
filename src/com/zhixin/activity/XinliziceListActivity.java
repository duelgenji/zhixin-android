package com.zhixin.activity;

import java.text.ParseException;

import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.adapter.XinliziceListAdapter;
import com.zhixin.datasynservice.QuListService;
import com.zhixin.datasynservice.XinliziceListService;
import com.zhixin.dialog.QubaopenProgressDialog;
import com.zhixin.domain.XinliziceList;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.SqlCursorLoader;

public class XinliziceListActivity extends FragmentActivity implements
LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener,
OnScrollListener, SwipeRefreshLayout.OnRefreshListener {

	private ViewGroup xinliziceTypePickerComp;

	private TextView xinliziceTypeIndicator;

	private ListView xinliziceList;
	private QubaopenProgressDialog progressDialog;

	private XinliziceListAdapter adapter;

	private XinliziceListService xinliziceListService;

	private XinliziceListActivity _this;

	private boolean refreshListIsRefreshing;

	private int order = 0;

	private int type = 0;

	private int currentFirstVisibleItem;
	private int currentVisibleItemCount;
	private int currentTotalItemCount;
	private int currentScrollState;

	private LoadDataTask refreshDataTask;

	private boolean shouldGetMoreData;

	private SwipeRefreshLayout xinliziceListParent;
	
	private class LoadDataTask extends AsyncTask<Integer, Void, String> {
		private boolean refreshFlag;

		public LoadDataTask(boolean refreshFlag) {
			super();
			this.refreshFlag = refreshFlag;
			if (refreshFlag) {
				shouldGetMoreData = true;
			}
		}

		@Override
		protected String doInBackground(Integer... params) {

			int order = params[0];
			int type = params[1];
			try {
				return xinliziceListService.refreshData(order, type, this.refreshFlag);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String params) {

			if (params == null) {
				getSupportLoaderManager().restartLoader(0, null,
						XinliziceListActivity.this);
			} else {
				if (xinliziceList != null) {

				}
				if (params.equals("err204")) {
					shouldGetMoreData = false;
				}
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}

			}
			if (xinliziceListParent.isRefreshing()) {
				xinliziceListParent.setRefreshing(false);
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();

			if (xinliziceListParent.isRefreshing()) {
				xinliziceListParent.setRefreshing(false);
			}
		}

	}
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_xinlizice_list);
		_this = this;
		init();
	}
	
	private void init() {
		shouldGetMoreData = true;

		xinliziceListService = new XinliziceListService(this);
		progressDialog = new QubaopenProgressDialog(this);

		xinliziceListParent = (SwipeRefreshLayout) this
				.findViewById(R.id.xinliziceListParent);
		xinliziceListParent.setColorScheme(R.color.text_blue,
				R.color.general_activity_background, R.color.text_blue,
				R.color.general_activity_background);

		xinliziceListParent.setOnRefreshListener(this);

		this.findViewById(R.id.backup_btn).setOnClickListener(this);

		xinliziceTypePickerComp = (ViewGroup) this
				.findViewById(R.id.xinliziceTypeComponent);


		xinliziceList = (ListView) this.findViewById(R.id.xinliziceList);

		xinliziceTypeIndicator = (TextView) this
				.findViewById(R.id.xinliziceTypeIndicator);

		refreshDataTask = new LoadDataTask(true);
		refreshListIsRefreshing = false;
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
		refreshDataTask.execute(order, type);

		xinliziceTypePickerComp.setOnClickListener(this);

		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				SharedPreferences sharedPref = _this.getSharedPreferences(
						SettingValues.FILE_NAME_SETTINGS, Context.MODE_PRIVATE);
				return sharedPref.getBoolean(
						SettingValues.INSTRUCTION_QUCESHI_LIST1, true);
			}

			@Override
			protected void onPostExecute(Boolean result) {
//				if (result) {
//					InstructionDialog qushouyeFirst = new InstructionDialog(
//							_this, SettingValues.INSTRUCTION_QUCESHI_LIST1);
//					qushouyeFirst.setOnDismissListener(new OnDismissListener() {
//						@Override
//						public void onDismiss(DialogInterface dialog) {
//							new InstructionDialog(_this,
//									SettingValues.INSTRUCTION_QUCESHI_LIST2)
//									.show();
//						}
//					});

//					qushouyeFirst.show();

//				}
			}

		}.execute();

	}


	@Override
	public void onClick(View v) {
		boolean cancelSuccess = true;
		switch (v.getId()) {
		case R.id.backup_btn:
			this.onBackPressed();

			break;
		default:
			break;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (adapter != null) {
			getSupportLoaderManager().restartLoader(0, null, this);
		}
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		return new SqlCursorLoader(this, QuListService.QuceshiSqlMaker.makeSql(
				order, type), XinliziceList.class);
	}


	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		if (adapter == null) {
			adapter = new XinliziceListAdapter(this, cursor, 0);
			xinliziceList.setAdapter(adapter);
		} else {
			adapter.changeCursor(cursor);
		}
		// if (this.refreshListIsRefreshing) {
		// quList.onRefreshComplete();
		// this.refreshListIsRefreshing = false;
		// }

		xinliziceList.setOnScrollListener(this);

		if (progressDialog.isShowing()
				&& refreshDataTask.getStatus() == AsyncTask.Status.FINISHED) {
			progressDialog.dismiss();
		}
	
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (adapter != null) {
			adapter.changeCursor(null);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		this.currentFirstVisibleItem = firstVisibleItem;
		this.currentVisibleItemCount = visibleItemCount;
		this.currentTotalItemCount = totalItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.currentScrollState = scrollState;
		this.isScrollCompletedAndTouchedBottom();

	}

	private void isScrollCompletedAndTouchedBottom() {
		if (shouldGetMoreData) {
			if (this.currentVisibleItemCount > 0
					&& this.currentScrollState == SCROLL_STATE_IDLE) {
				if ((this.currentFirstVisibleItem + this.currentVisibleItemCount) == this.currentTotalItemCount) {
					if (refreshDataTask.getStatus() != AsyncTask.Status.RUNNING) {
						refreshDataTask = new LoadDataTask(false);
						if (!progressDialog.isShowing()) {
							progressDialog.show();
						}
						refreshDataTask.execute(order, type);
					}
				}
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}

	@Override
	public void onRefresh() {
		if (refreshDataTask.getStatus() != AsyncTask.Status.RUNNING) {
			this.refreshListIsRefreshing = true;
			refreshDataTask = new LoadDataTask(true);
			// if (!progressDialog.isShowing()) {
			// progressDialog.show();
			// }
			refreshDataTask.execute(order, type);
		}

	}
}
