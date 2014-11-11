package com.qubaopen.activity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.qubaopen.R;
import com.qubaopen.adapter.InterestHistoryAdapter;
import com.qubaopen.datasynservice.InterestHistoryService;
import com.qubaopen.dialog.QubaopenProgressDialog;
import com.qubaopen.dialog.QuceshiTypePickerDialog;
import com.qubaopen.domain.InterestUserAnswer;

public class InterestHistoryActivity extends Activity implements
		OnClickListener, OnRefreshListener, OnScrollListener {
	private ImageButton btnBack;
	private RelativeLayout layoutInterestHistoryType;
	private TextView txtInterestHistoryType;
	/** loading */
	private QubaopenProgressDialog progressDialog;

	private InterestHistoryService interestHistoryService;

	private ListView interstHistoryList;
	private InterestHistoryAdapter interestHistoryAdapter;
	private List<InterestUserAnswer> list;
	private InterestHistoryActivity _this;
	/** 下拉刷新 */
	private SwipeRefreshLayout interestHistoryParent;
	private RelativeLayout layoutInterestHistoryEmpty;

	private QuceshiTypePickerDialog quceshiTypePickerDialog;
	private int type = 0;

	private LoadDataTask refreshDataTask;
	private boolean refreshListIsRefreshing;
	private int historyId = 0;
	private boolean shouldGetMoreData;

	private int currentFirstVisibleItem;
	private int currentVisibleItemCount;
	private int currentTotalItemCount;
	private int currentScrollState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interest_history);
		_this = this;
		initView();

	}

	private void initView() {
		interestHistoryService = new InterestHistoryService(this);
		interestHistoryParent = (SwipeRefreshLayout) this
				.findViewById(R.id.interest_history_list_parent);
		interestHistoryParent.setOnRefreshListener(this);

		quceshiTypePickerDialog = new QuceshiTypePickerDialog(this,
				android.R.style.Theme_Translucent_NoTitleBar);
		quceshiTypePickerDialog
				.setOnDismissListener(new TypePickerDialogDismissListener());
		quceshiTypePickerDialog.setOwnerActivity(this);

		layoutInterestHistoryEmpty = (RelativeLayout) this
				.findViewById(R.id.layout_interest_history_empty);
		layoutInterestHistoryEmpty.setVisibility(View.GONE);

		layoutInterestHistoryType = (RelativeLayout) this
				.findViewById(R.id.layout_interest_history_type_component);
		layoutInterestHistoryType.setOnClickListener(this);
		txtInterestHistoryType = (TextView) this
				.findViewById(R.id.txt_interest_history_type);
		btnBack = (ImageButton) this.findViewById(R.id.backup_btn);
		btnBack.setOnClickListener(this);
		interstHistoryList = (ListView) this
				.findViewById(R.id.interest_history_list);
		progressDialog = new QubaopenProgressDialog(this);
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
		// new LoadDataTask().execute(0);
		list = new ArrayList<InterestUserAnswer>();
		refreshDataTask = new LoadDataTask(true);
		refreshDataTask.execute(type, historyId);
	}

	private class TypePickerDialogDismissListener implements
			DialogInterface.OnDismissListener {

		@Override
		public void onDismiss(DialogInterface arg0) {

			if (quceshiTypePickerDialog.isPickTypeOrNot()) {
				if (type != quceshiTypePickerDialog.getType()) {
					type = quceshiTypePickerDialog.getType();
					txtInterestHistoryType.setText(quceshiTypePickerDialog
							.getTypeStr());

					refreshDataTask = new LoadDataTask(true);
					if (!progressDialog.isShowing()) {
						progressDialog.show();
					}
					// refreshDataTask.execute(order, type);
					refreshDataTask.execute(type, historyId);
				}
			} else {
				if (refreshDataTask.isCancelled()) {
					refreshDataTask = new LoadDataTask(true);
					if (!progressDialog.isShowing()) {
						progressDialog.show();
					}
					// refreshDataTask.execute(order, type);
					refreshDataTask.execute(type, historyId);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backup_btn:
			finish();
			break;
		case R.id.layout_interest_history_type_component:
			if (!quceshiTypePickerDialog.isShowing()) {
				quceshiTypePickerDialog.show();
			}
			break;
		default:
			break;
		}
	}

	private class LoadDataTask extends AsyncTask<Integer, Void, JSONObject> {
		private boolean refreshFlag;

		public LoadDataTask(boolean refreshFlag) {
			super();
			this.refreshFlag = refreshFlag;
			if (refreshFlag) {
				shouldGetMoreData = true;
				historyId = 0;
				list.clear();

			}
		}

		@Override
		protected JSONObject doInBackground(Integer... params) {
			JSONObject result = new JSONObject();
			Integer syncType = (Integer) params[0];
			int type = params[0] == null ? 0 : params[0];
			int historyId = params[1] == null ? 0 : params[1];
			try {
				Log.i("historyId", "......" + historyId);
				result = interestHistoryService
						.getInterestList(type, historyId);
				Log.i("interestHistory", "兴趣历史。。。。。。" + result);
				result.put("syncType", syncType);

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			try {
				Integer syncType = result.getInt("syncType");
				shouldGetMoreData = result.getBoolean("more");

				if (result.getString("success").equals("1")) {
					JSONArray array = new JSONArray();
					array = result.getJSONArray("data");
					Log.i("interestHistory", "获取的数组长度" + array.length());
					if (array.length() != 0) {
						for (int i = 0; i < array.length(); i++) {
							String interestTitle = array.getJSONObject(i)
									.getString("interestTitle");
							String content = array.getJSONObject(i).getString(
									"content");
							InterestUserAnswer interestUserAnswer = new InterestUserAnswer();
							interestUserAnswer.setInterestTitle(interestTitle);
							interestUserAnswer.setContent(content);
							list.add(interestUserAnswer);
						}
						Log.i("interestHistory", "最终的数组长度" + list.size());
						if (shouldGetMoreData) {
							historyId = array.getJSONObject(array.length() - 1)
									.getInt("historyId");
						}

						interestHistoryAdapter = new InterestHistoryAdapter(
								_this);
						interestHistoryAdapter.setList(list, true);
						interstHistoryList.setAdapter(interestHistoryAdapter);
						interstHistoryList.setOnScrollListener(_this);
					} else {
						layoutInterestHistoryEmpty.setVisibility(View.VISIBLE);
					}

					// if (progressDialog.isShowing()
					// && refreshDataTask.getStatus() ==
					// AsyncTask.Status.FINISHED) {
					// progressDialog.dismiss();
					// }
					if (progressDialog.isShowing()) {
						progressDialog.dismiss();

					}

				} else {
					showToast(getString(R.string.toast_get_userinfo_failed));
				}
				if (interestHistoryParent.isRefreshing()) {
					interestHistoryParent.setRefreshing(false);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	private void showToast(String content) {
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onRefresh() {
		if (refreshDataTask.getStatus() != AsyncTask.Status.RUNNING) {
			this.refreshListIsRefreshing = true;
			refreshDataTask = new LoadDataTask(true);
			refreshDataTask.execute(type, historyId);
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
						// refreshDataTask.execute(order, type);
						Log.i("interestHistory",
								"isScrollCompletedAndTouchedBottom......"
										+ historyId);
						refreshDataTask.execute(type, historyId);
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
}
