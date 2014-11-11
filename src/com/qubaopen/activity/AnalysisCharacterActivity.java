package com.qubaopen.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.qubaopen.R;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.ShareUtil;

public class AnalysisCharacterActivity extends FragmentActivity implements
		OnClickListener {
	public static String USER_CHARACTER_DESIGNATION = "userCharacterDesignation";
	public static String USER_CHARACTER_PERCENT = "userCharacterPercent";
	public static String USER_CHARACTER_ISCHECKED = "userCharacterIschecked";
	private String desination;
	private Double percent;
	private boolean isChecked;
	private ImageButton btnBack;
	private TextView title;
	private ImageView btnShare;
	private Fragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_analysis_character);
		btnBack = (ImageButton) this.findViewById(R.id.backup_btn);
		btnBack.setOnClickListener(this);
		title = (TextView) this.findViewById(R.id.title_of_the_page);
		title.setText("性格解析度");
		btnShare = (ImageView) this.findViewById(R.id.btnShare);
		btnShare.setOnClickListener(this);

		desination = getIntent().getStringExtra(USER_CHARACTER_DESIGNATION);
		// percent = getIntent().getDoubleExtra(USER_CHARACTER_PERCENT, 0);
		isChecked = getIntent().getBooleanExtra(USER_CHARACTER_ISCHECKED, true);
		percent = 100.00;
		if (percent == 100 && !isChecked) {
			fragment = new AnalysisCompletedFragment();
		} else {
			fragment = new AnalysisNotCompletedFragment();
		}
		Bundle bundle = new Bundle();
		bundle.putString("desination", desination);
		bundle.putDouble("percent", percent);
		fragment.setArguments(bundle);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.layout_analysis, fragment)
				.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public void onClick(View v) {
		v.setEnabled(false);
		switch (v.getId()) {
		case R.id.backup_btn:
			finish();
			v.setEnabled(true);
			break;
		case R.id.btnShare:
			ShareUtil.showShare("分享解析度", "我正在用知心");
			v.setEnabled(true);
			break;

		default:
			break;
		}
	}
}
