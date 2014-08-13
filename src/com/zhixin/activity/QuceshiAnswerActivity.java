package com.zhixin.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager.OnCancelListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.daos.QuListDao;
import com.zhixin.daos.QuWjAnswerDao;
import com.zhixin.daos.UserSettingsDao;
import com.zhixin.database.DbManager;
import com.zhixin.dialog.InstructionDialog;
import com.zhixin.domain.QuList;
import com.zhixin.domain.QuUserWjAnswer;
import com.zhixin.service.QuAnswerPrivacySettingService;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.QBPShareFunction;

public class QuceshiAnswerActivity extends Activity implements
		View.OnClickListener, CompoundButton.OnCheckedChangeListener {
	private int wjId;

	private TextView txtAnswerTitleQCSA;
	private TextView txtTitleQCSA;
	private TextView hideAnswerTxt;
	private ToggleButton btnHideAnswerQCSA;
	private ImageView btnSingleQHTP;

	private QuWjAnswerDao quWjAnswerDao;
	private QuListDao quListDao;

	private UserSettingsDao userSettingsDao;

	private boolean isPublicAnswer;

	private QuceshiAnswerActivity _this;

	public static final String INTENT_WJ_ID = "intentwjid";
	public static final String INTENT_IS_SHOW = "intentisshow";
	public static final String INTENT_FROM_HISTORY = "intentFromHistroy";
//还需要修改的类
	private QBPShareFunction share;

	private boolean fromHistoryOrNot;
	
	private ImageView add_adress;
	private Boolean pickerExist;
	private Dialog currentDialog;

	private class LoadDataTask extends AsyncTask<Integer, Void, QuUserWjAnswer> implements OnCancelListener{

		@Override
		protected QuUserWjAnswer doInBackground(Integer... params) {
			quListDao.deleteWjInList(wjId);
			return quWjAnswerDao.getTheAnswer(params[0]);
		}

		@Override
		protected void onPostExecute(QuUserWjAnswer wjAnswer) {

			if (wjAnswer != null) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						quWjAnswerDao.deletAllAnsweredQuesitons(wjId);
					}
				}).start();

				String titleText = wjAnswer.getAnswerChoiceNo()
						+ ":"
						+ (wjAnswer.getAnswerChoiceTitle() == null ? ""
								: wjAnswer.getAnswerChoiceTitle());
				txtAnswerTitleQCSA.setText(titleText);
				txtTitleQCSA.setText(wjAnswer.getAnswerChoiceContent());
				btnSingleQHTP.setOnClickListener(QuceshiAnswerActivity.this);
				if (wjAnswer.getIsPublicAnwer() != null) {
					isPublicAnswer = wjAnswer.getIsPublicAnwer().equals(1) ? true
							: false;
					initBtnHideAnswerQCSA();
				}
//				share = new QBPShareFunction(QuceshiAnswerActivity.this.findViewById(R.id.shareComponent),
//分享的
//				share = new QBPShareFunction(QuceshiAnswerActivity.this.findViewById(R.layout.dialog_share_friend),
//						QBPShareFunction.QU_CESHI, wjAnswer.getWjTitle(),
//						QuceshiAnswerActivity.this, wjAnswer.getWjId());

				if (!fromHistoryOrNot) {
					new AsyncTask<Void	, Void, QuList>() {

						@Override
						protected QuList doInBackground(Void... params) {
							return DbManager.getDatabase().findUniqueByWhere(
									QuList.class,
									"questionnarieId=" + wjId);
						}

						@Override
						protected void onPostExecute(QuList result) {
							if (result != null) {

								StringBuffer contentBuff = new StringBuffer();
								contentBuff.append("获得");
								contentBuff.append(result.getCredit());
								contentBuff.append("积分");

								Toast.makeText(_this, contentBuff.toString(),
										Toast.LENGTH_SHORT).show();
							}

						}

					}.execute();
				}

			}
			btnHideAnswerQCSA
					.setOnCheckedChangeListener(QuceshiAnswerActivity.this);
		}

		@Override
		public void onCancel() {
			
		}

		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quceshi_answer);
		_this = this;
		
		quWjAnswerDao = new QuWjAnswerDao();
		quListDao = new QuListDao();
		userSettingsDao = new UserSettingsDao();

		fromHistoryOrNot = this.getIntent().getBooleanExtra(
				INTENT_FROM_HISTORY, false);

		((TextView) this.findViewById(R.id.title_of_the_page))
				.setText(getString(R.string.title_quceshi));
		this.findViewById(R.id.backup_btn).setOnClickListener(this);

		btnSingleQHTP = (ImageView) this.findViewById(R.id.btnSingleQHTP);

		wjId = this.getIntent().getIntExtra(INTENT_WJ_ID, 0);
		txtAnswerTitleQCSA = (TextView) this
				.findViewById(R.id.txtAnswerTitleQCSA);
		txtTitleQCSA = (TextView) this.findViewById(R.id.txtTitleQCSA);

		add_adress = (ImageView) findViewById(R.id.add_address);
		add_adress.setOnClickListener(this);
		
		isPublicAnswer = userSettingsDao.getUserSettings().getIsHygkwj()
				.equals(1) ? true : false;
		btnHideAnswerQCSA = (ToggleButton) this
				.findViewById(R.id.btnHideAnswerQCSA);
		hideAnswerTxt = (TextView) this.findViewById(R.id.hideAnswerTxt);

		initBtnHideAnswerQCSA();

		new LoadDataTask().execute(wjId);

		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				SharedPreferences sharedPref = _this.getSharedPreferences(
						SettingValues.FILE_NAME_SETTINGS, Context.MODE_PRIVATE);
				return sharedPref.getBoolean(
						SettingValues.INSTRUCTION_QUCESHI_ANSWER, true);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					new InstructionDialog(_this,
							SettingValues.INSTRUCTION_QUCESHI_ANSWER).show();
				}
			}

		}.execute();

	}

	@Override
	public void onClick(View v) {
		v.setEnabled(false);
		switch (v.getId()) {
		case R.id.backup_btn:
			this.onBackPressed();
			break;
		case R.id.btnSingleQHTP:
			Intent intent = new Intent(this, QuFriendAnswerActivity.class);
			intent.putExtra(QuFriendAnswerActivity.INTENT_WJ_ID, wjId);
			startActivity(intent);
			v.setEnabled(true);
		case R.id.add_address:
			if(!pickerExist){
                pickerExist=true;
                this.SharePicker();
            }
			break;
		default:
			break;
		}
	}

	private void SharePicker() {
		final Dialog dialog = new Dialog(this,
                android.R.style.Theme_Translucent_NoTitleBar){
            @Override
            public void dismiss() {
                super.dismiss();
                pickerExist=false;
            }
        };
        dialog.setContentView(R.layout.dialog_share_friend);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.7f;
        dialog.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(lp);
        Button btn_share_xlwb = (Button) dialog
                .findViewById(R.id.btn_share_xlwb);
        Button btn_share_wx = (Button) dialog
                .findViewById(R.id.btn_share_wx);
        Button btn_share_qq = (Button) dialog
                .findViewById(R.id.btn_share_qq);
        Button btn_share_pyq = (Button) dialog
                .findViewById(R.id.btn_share_pyq);
        Button btn_cancel = (Button) dialog
                .findViewById(R.id.btn_cancel);
        
        currentDialog=dialog;
        
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        
        
        
        
        
        
        
        
        
        
        
        dialog.show();
        
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.btnHideAnswerQCSA:
			if (isChecked) {
				btnHideAnswerQCSA
						.setBackgroundResource(R.drawable.quceshi_show_answer);
				hideAnswerTxt.setText(this
						.getString(R.string.quceshi_public_answer));

				Toast.makeText(this,
						getString(R.string.toast_hide_wj_answer_success),
						Toast.LENGTH_SHORT).show();
			} else {
				btnHideAnswerQCSA
						.setBackgroundResource(R.drawable.quceshi_hide_answer);
				hideAnswerTxt.setText(this
						.getString(R.string.quceshi_hide_answer));

				Toast.makeText(this,
						getString(R.string.toast_public_wj_answer_success),
						Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}

	}

	private void setWjShowStatus() {
		boolean shouldSend = true;
		if (isPublicAnswer) {
			if (hideAnswerTxt.getText().equals(
					this.getString(R.string.quceshi_hide_answer))) {
				shouldSend = false;
			}
		} else {
			if (hideAnswerTxt.getText().equals(
					this.getString(R.string.quceshi_public_answer))) {
				shouldSend = false;
			}

		}

		if (shouldSend) {
			Intent intent = new Intent(this,
					QuAnswerPrivacySettingService.class);
			intent.putExtra(INTENT_WJ_ID, wjId);
			if (hideAnswerTxt.getText().equals(
					this.getString(R.string.quceshi_hide_answer))) {

				intent.putExtra(INTENT_IS_SHOW, 1);

			} else {

				intent.putExtra(INTENT_IS_SHOW, 0);

			}

			startService(intent);
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
		setWjShowStatus();
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

	private void initBtnHideAnswerQCSA() {
		if (isPublicAnswer) {
			btnHideAnswerQCSA.setChecked(false);

		} else {
			btnHideAnswerQCSA.setChecked(true);

		}
		if (isPublicAnswer) {
			btnHideAnswerQCSA
					.setBackgroundResource(R.drawable.quceshi_hide_answer);
			hideAnswerTxt.setText(this.getString(R.string.quceshi_hide_answer));

		} else {
			btnHideAnswerQCSA
					.setBackgroundResource(R.drawable.quceshi_show_answer);
			hideAnswerTxt.setText(this
					.getString(R.string.quceshi_public_answer));
		}

	}
}
