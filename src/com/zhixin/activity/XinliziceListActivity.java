package com.zhixin.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.adapter.WenJuanShuJu;
import com.zhixin.adapter.XinLiZiCeAdapter;
import com.zhixin.adapter.SelfListAdapter;
import com.zhixin.daos.SelfListDao;
import com.zhixin.datasynservice.XinliziceListService;
import com.zhixin.dialog.QubaopenProgressDialog;
import com.zhixin.domain.XinliziceList;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

public class XinliziceListActivity extends FragmentActivity implements
LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener,
OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
	private TextView xinliziceTypeIndicator;
	private ListView xinliziceList;
	private QubaopenProgressDialog progressDialog;
	private SelfListAdapter adapter;


	private XinliziceListActivity _this;
	private boolean refreshListIsRefreshing;
	private int order = 0;
	private Context context;

	private int currentFirstVisibleItem;
	private int currentVisibleItemCount;
	private int currentTotalItemCount;
	private int currentScrollState;

	private LoadDataTask refreshDataTask;

	private boolean shouldGetMoreData;

	private SwipeRefreshLayout xinliziceListParent;
	
	private List<WenJuanShuJu> list = new ArrayList<WenJuanShuJu>();;
	
	private XinLiZiCeAdapter adapter2;

	private class LoadDataTask extends AsyncTask<Integer, Void, List<WenJuanShuJu>> {
		private boolean refreshFlag;
		public LoadDataTask(boolean refreshFlag) {
			super();
			this.refreshFlag = refreshFlag;
			if (refreshFlag) {
				shouldGetMoreData = true;
			}
		}

		@Override
		protected ArrayList<WenJuanShuJu> doInBackground(Integer... params) {
			XinliziceList xinliziceList = new XinliziceList();
			int order = params[0];
			try {
				String requestUrl = SettingValues.URL_PREFIX
						+ context.getString(R.string.URL_XINLIZICE_LIST);
				JSONObject result = HttpClient.requestSync(requestUrl, null, HttpClient.TYPE_GET);
				if (result != null && result.getString("success").equals("1")) {
					SelfListDao selfListDao=new SelfListDao();
					selfListDao.saveSelfList(result);
					WenJuanShuJu wjsj=null;
					JSONArray json=(JSONArray) result.get("data");
					for (int i = 0; i < json.length(); i++) {
						wjsj=new WenJuanShuJu();
						JSONObject obj=json.getJSONObject(i);
						wjsj.setManagementType(obj.get("managementType").toString());
						wjsj.setTitile(obj.get("title").toString());
						wjsj.setSelfId(Long.valueOf(obj.get("selfId").toString()
								));
						list.add(wjsj);
					}
					//adapter2 = new XinLiZiCeAdapter(_this, list);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return (ArrayList<WenJuanShuJu>) list;
		}


		@Override
		protected void onPostExecute(List<WenJuanShuJu> list) {
			if (list!=null) {
				adapter2 = new XinLiZiCeAdapter(_this, list);
				xinliziceList.setAdapter(adapter2);
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
		setContentView(R.layout.activity_self_list);
		_this = this;
		init();
	}
	
	private void init() {
		shouldGetMoreData = true;

		context = this.getApplicationContext();
		
		progressDialog = new QubaopenProgressDialog(this);

		xinliziceListParent = (SwipeRefreshLayout) this
				.findViewById(R.id.selfListParent);
		xinliziceListParent.setColorScheme(R.color.text_orange,
				R.color.general_activity_background, R.color.text_orange,
				R.color.general_activity_background);

		xinliziceListParent.setOnRefreshListener(this);

		this.findViewById(R.id.backup_btn).setOnClickListener(this);

		xinliziceList = (ListView) this.findViewById(R.id.selfList);
		
		
		
		xinliziceTypeIndicator = (TextView) this
				.findViewById(R.id.title_of_the_page);
		xinliziceTypeIndicator.setText(getString(R.string.title_xinlizice));
		
		refreshDataTask = new LoadDataTask(true);
		refreshListIsRefreshing = false;
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
		refreshDataTask.execute(order);

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
			}

		}.execute();

	}


	@Override
	public void onClick(View v) {
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
		return null;
//		return new SqlCursorLoader(this, QuListService.QuceshiSqlMaker.makeSql(
//				order, type), XinliziceList.class);
	}
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (adapter == null) {
			adapter = new SelfListAdapter(this, cursor);
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
						refreshDataTask.execute(order);
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
			try {
				list=refreshDataTask.execute(order).get();
				adapter2 = new XinLiZiCeAdapter(_this, list); 
				xinliziceList.setAdapter(adapter2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}

	/*private class ItemCheckListener implements OnItemSelectedListener{
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
	}*/
}
