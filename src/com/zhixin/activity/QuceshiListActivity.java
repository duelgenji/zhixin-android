package com.zhixin.activity;

import java.text.ParseException;

import org.json.JSONException;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
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
import com.zhixin.adapter.QuceshiListAdapter;
import com.zhixin.datasynservice.QuListService;
import com.zhixin.dialog.InstructionDialog;
import com.zhixin.dialog.QubaopenProgressDialog;
import com.zhixin.dialog.QuceshiOrderPickerDialog;
import com.zhixin.dialog.QuceshiTypePickerDialog;
import com.zhixin.domain.QuList;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.SqlCursorLoader;

public class QuceshiListActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener,
		OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
//类型的选择
	private QuceshiTypePickerDialog quceshiTypePickerDialog;
	private QuceshiOrderPickerDialog quceshiOrderPickerDialog;

	private ViewGroup qucehiTypePickerComp;
	private View quceshiOrderComponent;

	private TextView quceshiTypeIndicator;
	private TextView quceshiOrderIndicator;

	private ListView quList;
	private QubaopenProgressDialog progressDialog;

	private QuceshiListAdapter adapter;

	private QuListService quListService;

	private QuceshiListActivity _this;

	private boolean refreshListIsRefreshing;

	private int order = 0;

	private int type = 0;

	private int currentFirstVisibleItem;
	private int currentVisibleItemCount;
	private int currentTotalItemCount;
	private int currentScrollState;

	private LoadDataTask refreshDataTask;

	private boolean shouldGetMoreData;

	private SwipeRefreshLayout quListParent;

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
				return quListService.refreshData(order, type, this.refreshFlag);
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
						QuceshiListActivity.this);
			} else {
				if (quList != null) {

				}
				if (params.equals("err204")) {
					shouldGetMoreData = false;
				}
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}

			}
			if (quListParent.isRefreshing()) {
				quListParent.setRefreshing(false);
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();

			if (quListParent.isRefreshing()) {
				quListParent.setRefreshing(false);
			}
		}

	}

	private class TypePickerDialogDismissListener implements
			DialogInterface.OnDismissListener {

		@Override
		public void onDismiss(DialogInterface arg0) {

			if (quceshiTypePickerDialog.isPickTypeOrNot()) {
				if (type != quceshiTypePickerDialog.getType()) {
					type = quceshiTypePickerDialog.getType();
					quceshiTypeIndicator.setText(quceshiTypePickerDialog
							.getTypeStr());

					refreshDataTask = new LoadDataTask(true);
					if (!progressDialog.isShowing()) {
						progressDialog.show();
					}
					refreshDataTask.execute(order, type);
				}
			} else {
				if (refreshDataTask.isCancelled()) {
					refreshDataTask = new LoadDataTask(true);
					if (!progressDialog.isShowing()) {
						progressDialog.show();
					}
					refreshDataTask.execute(order, type);
				}
			}
		}
	}

	private class OrderPickerDialogDismissListener implements
			DialogInterface.OnDismissListener {

		@Override
		public void onDismiss(DialogInterface arg0) {

			if (quceshiOrderPickerDialog.isPickOrderOrNot()) {
				if (order != quceshiOrderPickerDialog.getOrder()) {

					order = quceshiOrderPickerDialog.getOrder();
					quceshiOrderIndicator.setText(quceshiOrderPickerDialog
							.getOrderStr());

					refreshDataTask = new LoadDataTask(true);
					if (!progressDialog.isShowing()) {
						progressDialog.show();
					}
					refreshDataTask.execute(order, type);
				}
			} else {
				if (refreshDataTask.isCancelled()) {
					refreshDataTask = new LoadDataTask(true);
					if (!progressDialog.isShowing()) {
						progressDialog.show();
					}

					refreshDataTask.execute(order, type);
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quceshi_list);
		_this = this;
		init();
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
				order, type), QuList.class);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (adapter == null) {
			adapter = new QuceshiListAdapter(this, cursor, 0);
			quList.setAdapter(adapter);
		} else {
			adapter.changeCursor(cursor);
		}
		// if (this.refreshListIsRefreshing) {
		// quList.onRefreshComplete();
		// this.refreshListIsRefreshing = false;
		// }

		quList.setOnScrollListener(this);

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
	public void onClick(View v) {
		boolean cancelSuccess = true;
		switch (v.getId()) {
		case R.id.quceshiTypeComponent:

			if (refreshDataTask.getStatus() == AsyncTask.Status.RUNNING) {
				cancelSuccess = refreshDataTask.cancel(true);
			}

			if (!quceshiTypePickerDialog.isShowing()) {
				quceshiTypePickerDialog.show();
			}

			break;
		case R.id.quceshiOrderComponent:

			if (refreshDataTask.getStatus() == AsyncTask.Status.RUNNING) {
				cancelSuccess = refreshDataTask.cancel(true);
			}

			if (!quceshiOrderPickerDialog.isShowing()) {
				quceshiOrderPickerDialog.show();
			}

			break;
		case R.id.backup_btn:
			this.onBackPressed();

			break;
		default:
			break;
		}

	}

	private void init() {
		shouldGetMoreData = true;

		quListService = new QuListService(this);
		progressDialog = new QubaopenProgressDialog(this);

		quListParent = (SwipeRefreshLayout) this
				.findViewById(R.id.quListParent);
		quListParent.setColorScheme(R.color.text_blue,
				R.color.general_activity_background, R.color.text_blue,
				R.color.general_activity_background);

		quListParent.setOnRefreshListener(this);

		this.findViewById(R.id.backup_btn).setOnClickListener(this);

		quceshiTypePickerDialog = new QuceshiTypePickerDialog(this,
				android.R.style.Theme_Translucent_NoTitleBar);
		quceshiTypePickerDialog
				.setOnDismissListener(new TypePickerDialogDismissListener());
		quceshiTypePickerDialog.setOwnerActivity(this);
		qucehiTypePickerComp = (ViewGroup) this
				.findViewById(R.id.quceshiTypeComponent);

		quceshiOrderPickerDialog = new QuceshiOrderPickerDialog(this,
				android.R.style.Theme_Translucent_NoTitleBar);

		quceshiOrderPickerDialog
				.setOnDismissListener(new OrderPickerDialogDismissListener());
		quceshiOrderPickerDialog.setOwnerActivity(this);
		quceshiOrderComponent = (View) this
				.findViewById(R.id.quceshiOrderComponent);

		quList = (ListView) this.findViewById(R.id.quList);

		quceshiTypeIndicator = (TextView) this
				.findViewById(R.id.quceshiTypeIndicator);
		quceshiOrderIndicator = (TextView) this
				.findViewById(R.id.quceshiOrderIndicator);

		refreshDataTask = new LoadDataTask(true);
		refreshListIsRefreshing = false;
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
		refreshDataTask.execute(order, type);

		quceshiOrderComponent.setOnClickListener(this);
		qucehiTypePickerComp.setOnClickListener(this);

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
				if (result) {
					InstructionDialog qushouyeFirst = new InstructionDialog(
							_this, SettingValues.INSTRUCTION_QUCESHI_LIST1);
					qushouyeFirst.setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							new InstructionDialog(_this,
									SettingValues.INSTRUCTION_QUCESHI_LIST2)
									.show();
						}
					});

					qushouyeFirst.show();

				}
			}

		}.execute();

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
