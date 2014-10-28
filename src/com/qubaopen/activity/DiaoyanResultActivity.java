package com.qubaopen.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.qubaopen.R;
import com.qubaopen.daos.DiaoyanContentDao;
import com.qubaopen.daos.DiaoyanDao;
import com.qubaopen.domain.DiaoyanList;
import com.qubaopen.utils.QBPShareFunction;

public class DiaoyanResultActivity extends Activity implements
		View.OnClickListener {
	public static final String INTENT_WJ_ID = "intentwjid";
	public static final String SUCCESS_OR_NOT = "successornot";

	private int wjId;
	private boolean isResultSuccess;
	private DiaoyanDao diaoyanListDao;

	private TextView txtPageTitle;
	private ImageButton iBtnPageBack;

	private ImageView diaoyanSuccessOrFailImage;
	private TextView txtContentQDYA;
	private TextView titleOfPage;

	private QBPShareFunction share;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qudiaoyan_result);
		txtPageTitle = (TextView) this.findViewById(R.id.title_of_the_page);
		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);
		txtPageTitle.setText(this.getString(R.string.title_qu_diaoyan));
		init();
	}

	@Override
	public void onClick(View v) {

		v.setEnabled(false);
		switch (v.getId()) {
		case R.id.backup_btn:
			this.onBackPressed();
			v.setEnabled(true);
			break;
		default:
			break;

		}
		v.setEnabled(true);
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

	private void init() {
		diaoyanListDao = new DiaoyanDao();
		wjId = this.getIntent().getIntExtra(INTENT_WJ_ID, 0);
		isResultSuccess = this.getIntent().getBooleanExtra(SUCCESS_OR_NOT,
				false);

		diaoyanSuccessOrFailImage = (ImageView) this
				.findViewById(R.id.diaoyanSuccessOrFailImage);
		txtContentQDYA = (TextView) this.findViewById(R.id.txtContentQDYA);
		DiaoyanList diaoyanWj = diaoyanListDao.getWjByWjId(wjId);
		if (isResultSuccess) {
			diaoyanSuccessOrFailImage
					.setImageResource(R.drawable.qudiaoyan_result_success);
			txtContentQDYA
					.setText(getString(R.string.qudiaoyan_result_success_content_part1)
							+ String.valueOf(diaoyanWj.getCredit())
							+ getString(R.string.qudiaoyan_result_success_content_part2));

		} else {
			diaoyanSuccessOrFailImage
					.setImageResource(R.drawable.qudiaoyan_result_fail);
			txtContentQDYA
					.setText(getString(R.string.qudiaoyan_result_failure_content_part1)
							+ String.valueOf(diaoyanWj.getFailCredit())
							+ getString(R.string.qudiaoyan_result_failure_content_part2));

		}

		titleOfPage = (TextView) this.findViewById(R.id.title_of_the_page);
		titleOfPage.setText(getString(R.string.title_qu_diaoyan));
		diaoyanListDao.deleteWjInList(wjId);
//分享
	//	share = new QBPShareFunction(this.findViewById(R.id.shareComponent),
	//			QBPShareFunction.QU_DIAOYAN, diaoyanWj.getTitle(), this,
	//			diaoyanWj.getQuestionnarieId());
		new Thread(new Runnable() {
			@Override
			public void run() {
				new DiaoyanContentDao().deleteAllAnsweredQuesitons(wjId);
			}
		}).start();

	}

}
