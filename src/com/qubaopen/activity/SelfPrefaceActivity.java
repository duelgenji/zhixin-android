package com.qubaopen.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.qubaopen.R;
import com.qubaopen.database.DbManager;
import com.qubaopen.datasynservice.SelfListService;
import com.qubaopen.dialog.QubaopenProgressDialog;
import com.qubaopen.domain.SelfList;

public class SelfPrefaceActivity extends Activity implements
		View.OnClickListener {
	public static final String INTENT_TYPE = "type";
	public static final String INTENT_SELF_ID = "selfId";
	public static final String INTENT_SELF_ISRETEST = "selfIsRetest";

	private ImageButton iBtnPageBack;
	private TextView txtPageTitle;

	private TextView txtTitle;
	private TextView txtIntruductionTitle;
	private TextView txtIntruduction;
	private View divider;
	private TextView txtGuideTitle;
	private TextView txtGuide;
	
	private QubaopenProgressDialog progressDialog;

	private ImageButton btnBegin;

	private SelfListService selfListService;

	private int type;
	private int selfId;
	private boolean isRetest;

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
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(SelfList selfList) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (selfList != null) {
				showText(selfList);
			}
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_self_preface);
		initIntent();
		initView();
		
	}

	private void initIntent(){
		type = getIntent().getIntExtra(INTENT_TYPE, 0);
		selfId = getIntent().getIntExtra(INTENT_SELF_ID, 0);
		isRetest = getIntent().getBooleanExtra(INTENT_SELF_ISRETEST, false);
	}
	private void initView(){
		txtPageTitle = (TextView) this.findViewById(R.id.title_of_the_page);
		if (type == 1) {
			txtPageTitle.setText(getString(R.string.self_list_title_1));
		}else if (type == 2) {
			txtPageTitle.setText(getString(R.string.self_list_title_2));
		}else if (type == 3) {
			txtPageTitle.setText(getString(R.string.self_list_title_3));
		}else if (type == 4) {
			txtPageTitle.setText(getString(R.string.self_list_title_4));
		}
		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);
		progressDialog = new QubaopenProgressDialog(this);

		divider = (View) this.findViewById(R.id.divider);

		txtTitle = (TextView) this.findViewById(R.id.txt_title);
		txtIntruductionTitle = (TextView) this
				.findViewById(R.id.txt_intruduction_title);
		txtIntruduction = (TextView) this.findViewById(R.id.txt_intruduction);
		txtGuideTitle = (TextView) this.findViewById(R.id.txt_guide_title);
		txtGuide = (TextView) this.findViewById(R.id.txt_guide);

		btnBegin = (ImageButton) this.findViewById(R.id.btn_begin);
		btnBegin.setOnClickListener(this);

		
		selfListService = new SelfListService(this);
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
		new LoadDataTask().execute();
	}
	private void showText(SelfList selfList) {
		txtTitle.setText(selfList.getTitle());
		
		//替换换行符
		String remark=selfList.getRemark();
		remark=remark.replace("\\n", "\n");
		txtIntruduction.setText(remark);

		String guidance=selfList.getGuidanceSentence();
		guidance=guidance.replace("\\n", "\n");
		txtGuide.setText(guidance);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backup_btn:
			this.onBackPressed();
			break;
		case R.id.btn_begin:
			Intent intent = new Intent(this, SelfContentActivity.class);
			intent.putExtra(SelfContentActivity.INTENT_SELF_ID, selfId);
			intent.putExtra(SelfContentActivity.INTENT_QUESTIONNAIRE_ISRETEST, isRetest);
			intent.putExtra(SelfContentActivity.INTENT_QUESTIONNAIRE_TITLE, txtTitle.getText());
			startActivity(intent);
			//Toast.makeText(this, "进入答题页面" + selfId, Toast.LENGTH_SHORT).show();

			break;
		default:
			break;
		}
	}

}
