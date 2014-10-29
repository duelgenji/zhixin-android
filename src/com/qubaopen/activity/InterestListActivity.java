package com.qubaopen.activity;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.qubaopen.R;
import com.qubaopen.adapter.InterestListAdapter;
import com.qubaopen.datasynservice.InterestListService;
import com.qubaopen.dialog.QubaopenProgressDialog;
import com.qubaopen.dialog.QuceshiOrderPickerDialog;
import com.qubaopen.dialog.QuceshiTypePickerDialog;
import com.qubaopen.domain.InterestList;
import com.qubaopen.utils.SqlCursorLoader;

public class InterestListActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener,
		OnScrollListener, SwipeRefreshLayout.OnRefreshListener {

	private QuceshiTypePickerDialog quceshiTypePickerDialog;
	private QuceshiOrderPickerDialog quceshiOrderPickerDialog;
	private ViewGroup qucehiTypePickerComp;
	private View quceshiOrderComponent;

	private TextView txtInterestType;
	private TextView txtInterestOrderType;

	/** 趣测试列表 */
	private ListView quList;

	private RelativeLayout layoutInterestListEmpty;
	/** loading */
	private QubaopenProgressDialog progressDialog;
	/** 趣测试adapter */
	private InterestListAdapter adapter;

	private InterestListService quListService;

	private InterestListActivity _this;

	private boolean refreshListIsRefreshing;

	private int order = 0;

	private int type = 0;

	private int currentFirstVisibleItem;
	private int currentVisibleItemCount;
	private int currentTotalItemCount;
	private int currentScrollState;

	private LoadDataTask refreshDataTask;

	private boolean shouldGetMoreData;
	private int currentListPage;
	/** 下拉刷新 */
	private SwipeRefreshLayout quListParent;

	private class LoadDataTask extends AsyncTask<Integer, Void, JSONObject> {
		private boolean refreshFlag;

		public LoadDataTask(boolean refreshFlag) {
			super();
			this.refreshFlag = refreshFlag;
			if (refreshFlag) {
				shouldGetMoreData = true;
				currentListPage = 0;
			}
		}

		@Override
		protected JSONObject doInBackground(Integer... params) {
			int order = params[0] == null ? 0 : params[0];
			int type = params[1] == null ? 0 : params[1];
			try {
				return quListService.getInterestList(order, type,
						currentListPage);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject params) {
			try {
				shouldGetMoreData = !params.getBoolean("lastPage");
				if (shouldGetMoreData) {
					currentListPage++;
				}

				getSupportLoaderManager().restartLoader(0, null, _this);
				if (quListParent.isRefreshing()) {
					quListParent.setRefreshing(false);
				}
			} catch (JSONException e) {
				e.printStackTrace();
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interest_list);
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

		return new SqlCursorLoader(this,
				InterestListService.QuceshiSqlMaker.makeSql(),
				InterestList.class);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if(cursor.getCount()==0){
			quListParent.setVisibility(View.GONE);
			layoutInterestListEmpty.setVisibility(View.VISIBLE);
		}else{
			quListParent.setVisibility(View.VISIBLE);
			layoutInterestListEmpty.setVisibility(View.GONE);
		}
		if (adapter == null) {
			adapter = new InterestListAdapter(this, cursor);
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
		switch (v.getId()) {
		case R.id.backup_btn:
			finish();
			break;
		case R.id.txtInterestType:

			// if (refreshDataTask.getStatus() == AsyncTask.Status.RUNNING) {
			// cancelSuccess = refreshDataTask.cancel(true);
			// }

			if (!quceshiTypePickerDialog.isShowing()) {
				quceshiTypePickerDialog.show();
			}

			break;
		case R.id.txtInterestOrderType:

			// if (refreshDataTask.getStatus() == AsyncTask.Status.RUNNING) {
			// cancelSuccess = refreshDataTask.cancel(true);
			// }

			if (!quceshiOrderPickerDialog.isShowing()) {
				quceshiOrderPickerDialog.show();

			}

			break;
		default:
			break;
		}

	}

	private class TypePickerDialogDismissListener implements
			DialogInterface.OnDismissListener {

		@Override
		public void onDismiss(DialogInterface arg0) {

			if (quceshiTypePickerDialog.isPickTypeOrNot()) {
				if (type != quceshiTypePickerDialog.getType()) {
					type = quceshiTypePickerDialog.getType();
					txtInterestType.setText(quceshiTypePickerDialog
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
					txtInterestOrderType.setText(quceshiOrderPickerDialog
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

	private void init() {
		shouldGetMoreData = true;
		currentListPage = 0;
		quListService = new InterestListService(this);
		progressDialog = new QubaopenProgressDialog(this);
		quListParent = (SwipeRefreshLayout) this
				.findViewById(R.id.quListParent);
		layoutInterestListEmpty =(RelativeLayout) this.findViewById(R.id.layoutInterestListEmpty);
		quListParent.setColorScheme(R.color.know_heart_theme,
				R.color.general_activity_background, R.color.know_heart_theme,
				R.color.general_activity_background);
		quListParent.setOnRefreshListener(this);

		this.findViewById(R.id.backup_btn).setOnClickListener(this);
		quList = (ListView) this.findViewById(R.id.quList);

		refreshDataTask = new LoadDataTask(true);
		refreshListIsRefreshing = false;
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
		refreshDataTask.execute(order, type);

		// 类型和排序
		txtInterestType = (TextView) this.findViewById(R.id.txtInterestType);
		txtInterestOrderType = (TextView) this
				.findViewById(R.id.txtInterestOrderType);
		txtInterestType.setOnClickListener(this);
		txtInterestOrderType.setOnClickListener(this);

		quceshiTypePickerDialog = new QuceshiTypePickerDialog(this,
				android.R.style.Theme_Translucent_NoTitleBar);
		quceshiTypePickerDialog
				.setOnDismissListener(new TypePickerDialogDismissListener());
		quceshiTypePickerDialog.setOwnerActivity(this);

		quceshiOrderPickerDialog = new QuceshiOrderPickerDialog(this,
				android.R.style.Theme_Translucent_NoTitleBar);
		quceshiOrderPickerDialog
				.setOnDismissListener(new OrderPickerDialogDismissListener());
		quceshiOrderPickerDialog.setOwnerActivity(this);

		//第一次进入 列表 加载引导图
//		new AsyncTask<Void, Void, Boolean>() {
//			@Override
//			protected Boolean doInBackground(Void... params) {
//				SharedPreferences sharedPref = _this.getSharedPreferences(
//						SettingValues.FILE_NAME_SETTINGS, Context.MODE_PRIVATE);
//				return sharedPref.getBoolean(
//						SettingValues.INSTRUCTION_QUCESHI_LIST1, true);
//			}
//
//			@Override
//			protected void onPostExecute(Boolean result) {
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
//			}
//		}.execute();
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
