package com.qubaopen.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.qubaopen.R;
import com.qubaopen.utils.ShareUtil;
import com.qubaopen.utils.StringFormatUtil;

public class SelfAnswerActivity extends Activity implements
		View.OnClickListener {

	public static final String INTENT_SELF_ID = "selfId";
	public static final String INTENT_SELF_TITLE = "selfTitle";
	public static final String INTENT_SELF_ANSWER = "selfAnswer";

	private ImageButton iBtnPageBack;
	private TextView txtPageTitle;
	private ImageView btnShare;

	private TextView txtTitle;
	private TextView txtContent;
	

	private ImageButton btnBack;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_self_answer);
		txtPageTitle = (TextView) this.findViewById(R.id.title_of_the_page);
		txtPageTitle.setText(R.string.title_questionnaire_answer);
		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);
		btnShare=(ImageView) this.findViewById(R.id.btnShare);
		btnShare.setOnClickListener(this);

	

		txtTitle = (TextView) this.findViewById(R.id.txt_title);
		txtContent = (TextView) this
				.findViewById(R.id.txt_content);
	

		btnBack = (ImageButton) this.findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);

		
		showText();
		
	}

	private void showText() {
		txtTitle.setText(getIntent().getStringExtra(INTENT_SELF_TITLE));
		//txtContent.setText(getIntent().getStringExtra(INTENT_SELF_ANSWER));
		txtContent.setText(StringFormatUtil.formatBreakLine(getIntent().getStringExtra(INTENT_SELF_ANSWER)));
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, SelfListActivity.class);
		switch (v.getId()) {
		case R.id.backup_btn:
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			break;
		case R.id.btn_back:
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			break;
		case R.id.btnShare:
			ShareUtil.showShare("标题","内容");
			break;
		default:
			break;
		}
	}

}
