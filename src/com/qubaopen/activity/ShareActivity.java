package com.qubaopen.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.qubaopen.R;
import com.qubaopen.database.DbManager;
import com.qubaopen.domain.UserInfo;
import com.qubaopen.settings.CurrentUserHelper;
import com.qubaopen.utils.QBPShareFunction;

public class ShareActivity extends Activity {

	private TextView titleOfPage;
	private ImageButton backUp;

	private QBPShareFunction shareFunction;

	private ShareActivity _this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		
		_this = this;

		titleOfPage = (TextView) this.findViewById(R.id.title_of_the_page);
		titleOfPage.setText(this.getString(R.string.head_share));


		backUp = (ImageButton) this.findViewById(R.id.backup_btn);
		backUp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ShareActivity.this.onBackPressed();

			}
		});
		new Thread(new Runnable() {
			@Override
			public void run() {
				final UserInfo userInfo = DbManager.getDatabase()
						.findUniqueByWhere(
								UserInfo.class,
								"userId="
										+ CurrentUserHelper
												.getCurrentUserId());
				_this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
//						inviteCode.setText(userInfo.getYqm());
						shareFunction = new QBPShareFunction(_this
								.findViewById(R.id.shareComponent),
								QBPShareFunction.SHARE_APP, null,
								_this, 0);
//						shareFunction = new QBPShareFunction(_this.findViewById(R.id.shareAppComp), QBPShareFunction.SHARE_APP, _this, 0);
					}
				});

			}

		}).start();

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

}
