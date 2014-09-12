package com.zhixin.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.R;
import com.zhixin.database.DbManager;
import com.zhixin.datasynservice.SelfListService;
import com.zhixin.domain.SelfList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SelfPrefaceActivity extends Activity implements
		View.OnClickListener {

	public static final String INTENT_SELF_ID = "selfId";

	private ImageButton iBtnPageBack;
	private TextView txtPageTitle;

	private TextView txtTitle;
	private TextView txtIntruductionTitle;
	private TextView txtIntruduction;
	private View divider;
	private TextView txtGuideTitle;
	private TextView txtGuide;

	private ImageButton btnBegin;

	private SelfListService selfListService;

	private int selfId;

	private class LoadDataTask extends AsyncTask<Integer, Void, SelfList> {

		@Override
		protected SelfList doInBackground(Integer... params) {
			try {
				
				/**判断数据库里有没有 本自测问卷的数据   （简介、指导语）
				 * 如果有  直接show出来
				 * 没有的话调用请求 再show */
				
				JSONObject result = selfListService.requestSelfListInfo(selfId);
				if (result != null && result.getString("success").equals("1")) {
					return DbManager.getDatabase().findUniqueByWhere(
							SelfList.class, "selfId=" + selfId);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(SelfList selfList) {
			if (selfList != null) {
				showText(selfList);
			}
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_self_preface);
		txtPageTitle = (TextView) this.findViewById(R.id.title_of_the_page);
		txtPageTitle.setText("心理自测");
		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);

		divider = (View) this.findViewById(R.id.divider);

		txtTitle = (TextView) this.findViewById(R.id.txt_title);
		txtIntruductionTitle = (TextView) this
				.findViewById(R.id.txt_intruduction_title);
		txtIntruduction = (TextView) this.findViewById(R.id.txt_intruduction);
		txtGuideTitle = (TextView) this.findViewById(R.id.txt_guide_title);
		txtGuide = (TextView) this.findViewById(R.id.txt_guide);

		btnBegin = (ImageButton) this.findViewById(R.id.btn_begin);
		btnBegin.setOnClickListener(this);

		selfId = getIntent().getIntExtra(INTENT_SELF_ID, 0);
		selfListService = new SelfListService(this);
		new LoadDataTask().execute();
	}

	private void showText(SelfList selfList) {
		txtTitle.setText(selfList.getTitle());
		txtIntruduction.setText(selfList.getRemark());
		txtGuide.setText(selfList.getGuidanceSentence());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backup_btn:
			this.onBackPressed();
			break;
		case R.id.btn_begin:
			Intent intent = new Intent(this, SelfContentActivity.class);
			intent.putExtra(INTENT_SELF_ID, selfId);
			startActivity(intent);
			//Toast.makeText(this, "进入答题页面" + selfId, Toast.LENGTH_SHORT).show();

			break;
		default:
			break;
		}
	}

}
