package com.qubaopen.activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.qubaopen.R;
import com.qubaopen.cache.PreviousUserQuestionCache;
import com.qubaopen.customui.DafenChoice2;
import com.qubaopen.customui.DanxuanChoice2;
import com.qubaopen.customui.DuoxuanChoice2;
import com.qubaopen.customui.ShunxuChoice;
import com.qubaopen.customui.ShunxuTitleItem;
import com.qubaopen.customui.ShunxuViewGroup;
import com.qubaopen.customui.Wenda;
import com.qubaopen.datasynservice.SelfAnswerService;
import com.qubaopen.datasynservice.SelfContentService;
import com.qubaopen.dialog.CommonDialog;
import com.qubaopen.dialog.CommonDialog.CommonDialogListener;
import com.qubaopen.dialog.DafenFenshuOverlayer2;
import com.qubaopen.dialog.QubaopenProgressDialog;
import com.qubaopen.domain.SelfUserAnswer;
import com.qubaopen.domain.UserQuestionAnswer;
import com.qubaopen.enums.QuestionTypeEnums;
import com.qubaopen.logic.DoDataObject;
import com.qubaopen.logic.DoLogicObject;
import com.qubaopen.logic.DoQuestionAnswer;
import com.qubaopen.utils.NetworkUtils;
import com.qubaopen.utils.StringFormatUtil;

public class SelfContentActivity extends Activity implements
		View.OnClickListener, DoDataObject.DiaoyanDatiLoadFinished {

	public static final String INTENT_SELF_ID = "selfId";
	public static final String INTENT_QUESTIONNAIRE_TITLE = "questionnaireTitle";
	public static final String CURRENT_QUESTION = "currentQuestion";
	public static final String INTENT_QUESTIONNAIRE_ISRETEST = "questionnaireIsRetest";

	private SelfContentService selfContentService;
	private SelfAnswerService selfAnswerService;
	private Integer selfId;
	private boolean isRetest;

	private RelativeLayout quceshiContentArea;

	private DoDataObject doDataObject;
	private DoLogicObject doLogicObject;

	private View nextQuestionBtn;
	private View prevQuestionBtn;
	private TextView txtNoQCSC;
	private TextView remainingTime;
	private ImageButton iBtnPageBack;
	private TextView txtPageTitle;

	private DanxuanChoice2 danxuanChoice;
	private DuoxuanChoice2 duoxuanChoice;
	private Wenda wenda;

	private CountDownTimer countDownTimer;

	private QubaopenProgressDialog progressDialog;

	private ShunxuViewGroup shunxuViewGroup;
	private RelativeLayout questionTitleViewGroup;
	private DafenFenshuOverlayer2 dafenFenshuOverlayer;
	private DafenChoice2 dafenChoice;

	private class LoadDataTask extends AsyncTask<Integer, Void, String> {
		private boolean theFirstTime;

		public LoadDataTask(boolean theFirstTime) {
			this.theFirstTime = theFirstTime;
		}

		@Override
		protected String doInBackground(Integer... params) {
			try {
				if (theFirstTime) {
					String message = selfContentService
							.getSelfContent(params[0]);
					if (message == null) {
						doLogicObject = new DoLogicObject(selfId, 2);
					}

					return message;
				} else {
					doLogicObject = new DoLogicObject(params[0], true, 2);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String params) {
			if (params == null) {
				doDataObject = new DoDataObject(doLogicObject
						.getCurrentQuestion().getQuestionId(), 2);
				doDataObject.setmQuDatiLoadFinished(SelfContentActivity.this);
				doDataObject.execute();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quceshi_content);
		init(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		init(intent);
	}

	private void init(Intent intent) {
		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);
		txtPageTitle = (TextView) this.findViewById(R.id.title_of_the_page);
		txtPageTitle.setText(R.string.title_questionnaire_content);
		progressDialog = new QubaopenProgressDialog(this);

		quceshiContentArea = (RelativeLayout) this
				.findViewById(R.id.quceshiContentArea);
		quceshiContentArea.removeAllViews();

		nextQuestionBtn = this.findViewById(R.id.nextQuestionBtn);
		nextQuestionBtn.setOnClickListener(null);
		prevQuestionBtn = this.findViewById(R.id.prevQuestionBtn);
		prevQuestionBtn.setOnClickListener(null);
		remainingTime = (TextView) this.findViewById(R.id.remainingTime);

		txtNoQCSC = (TextView) this.findViewById(R.id.txtTotalNo);

		questionTitleViewGroup = (RelativeLayout) this
				.findViewById(R.id.questionTitleViewGroup);

		selfContentService = new SelfContentService(this);
		selfAnswerService = new SelfAnswerService(this);
		String title = intent.getStringExtra(INTENT_QUESTIONNAIRE_TITLE);
		((TextView) this.findViewById(R.id.txt_title)).setText(title);
		selfId = getIntent().getIntExtra(INTENT_SELF_ID, 0);
		isRetest = getIntent().getBooleanExtra(INTENT_QUESTIONNAIRE_ISRETEST,
				false);
		Log.i("SelfContentActivity", "isRetest...first..." + isRetest);
		int questionId = intent.getIntExtra(CURRENT_QUESTION, 0);
		if (questionId == 0) {
			if (!progressDialog.isShowing()) {
				progressDialog.show();
			}
			new LoadDataTask(true).execute(selfId);
		} else {
			new LoadDataTask(false).execute(questionId);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backup_btn:
			this.onBackPressed();
			break;
		case R.id.nextQuestionBtn:
			List<UserQuestionAnswer> ans;
			switch (doDataObject.getQuestionType()) {
			case 1:
				ans = danxuanChoice.getCurrentQuDatiQuestionAnswer();
				try {
					nextQuestionLogic(ans);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 2:
				ans = duoxuanChoice.getAnswer();
				try {
					nextQuestionLogic(ans);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 3:
				ans = wenda.getAnswer();
				try {
					nextQuestionLogic(ans);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 4:
				ans = shunxuViewGroup.getAnswer();
				try {
					nextQuestionLogic(ans);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 5:
				ans = dafenChoice.getAnswer();
				try {
					nextQuestionLogic(ans);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
			break;
		case R.id.prevQuestionBtn:
			prevQuestionLogic();
			break;
		default:
			break;
		}
	}

	private void nextQuestionLogic(List<UserQuestionAnswer> ans)
			throws Exception {
		if (ans != null) {
			doLogicObject.setAnswer(ans);
			Integer nextQuestionId = doLogicObject.getNextQuestionId();
			// Integer nextQuestionId=quDatiDataObject.getQuestionId()+1;
			if (nextQuestionId != null) {
				Intent intent = new Intent(this, SelfContentActivity.class);
				intent.putExtra(INTENT_QUESTIONNAIRE_TITLE, getIntent()
						.getStringExtra(INTENT_QUESTIONNAIRE_TITLE));
				intent.putExtra(CURRENT_QUESTION, nextQuestionId.intValue());
				intent.putExtra(INTENT_QUESTIONNAIRE_ISRETEST, isRetest);
				startActivity(intent);
			} else {

				if (NetworkUtils.isNetworkAvailable(this)) {
					Log.i("SelfContentActivity", "isRetest...second..." + isRetest);
					if (!isRetest) {
						CommonDialog dialog = new CommonDialog(this,
								getString(R.string.submmit_wj_tips),
								new CommonDialogListener() {

									@Override
									public void onClick(View view) {
										switch (view.getId()) {
										case R.id.dialog_common_confirm:
											nextQuestionBtn.setEnabled(false);
											new SubmitDataTask()
													.execute(doDataObject
															.getWjId());
											break;
										case R.id.dialog_common_cancel:
											break;

										default:
											break;
										}
									}
								});
						dialog.show();

					} else {
						AlertDialog alert = new AlertDialog.Builder(this)
								.setTitle("提示")
								.setMessage(
										SelfContentActivity.this
												.getString(R.string.submmit_wj_tips))
								.setNeutralButton("不覆盖",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												nextQuestionBtn
														.setEnabled(false);
												new SubmitDataTask()
														.execute(doDataObject
																.getWjId());
											}

										})
								.setPositiveButton("覆盖",
										new DialogInterface.OnClickListener() {// 设置确定按钮
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {

												nextQuestionBtn
														.setEnabled(false);
												new SubmitDataTask()
														.execute(doDataObject
																.getWjId());
											}
										})
								.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {// 设置取消按钮
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {

											}
										}).create();
						alert.show();
					}
				} else {
					Toast.makeText(this,
							this.getString(R.string.toast_network_unvaiable),
							Toast.LENGTH_SHORT).show();
				}
			}
		} else {
			Toast.makeText(this, getString(R.string.dati_question_unanswered),
					Toast.LENGTH_SHORT).show();
		}
	}

	private void prevQuestionLogic() {
		Integer prevQuestionId = doLogicObject.getHistoryAnswerCursor()
				.previousQuestion();
		if (prevQuestionId != null) {
			Intent intent = new Intent(this, SelfContentActivity.class);
			intent.putExtra(INTENT_QUESTIONNAIRE_TITLE, getIntent()
					.getStringExtra(INTENT_QUESTIONNAIRE_TITLE));
			intent.putExtra(CURRENT_QUESTION, prevQuestionId.intValue());
			startActivity(intent);
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
	public void onBackPressed() {

		CommonDialog dialog = new CommonDialog(this,
				getString(R.string.back_wj_tips), new CommonDialogListener() {

					@Override
					public void onClick(View view) {
						switch (view.getId()) {
						case R.id.dialog_common_confirm:
							finish();
							break;
						case R.id.dialog_common_cancel:
							break;

						default:
							break;
						}
					}
				});
		dialog.show();

	}

	@Override
	public void displayThings() {
		boolean hasCountDownTimer = false;
		if (PreviousUserQuestionCache.getInstance() == null) {
			hasCountDownTimer = true;
		}

		TextView textView = (TextView) this.findViewById(R.id.txtTitleQCSC);
		textView.setText(StringFormatUtil.formatBreakLine(doDataObject
				.getQuestionTitle()));

		txtNoQCSC.setText(doDataObject.getQuestionNo() + "/"
				+ String.valueOf(doDataObject.getQuestionSumInWj()));
		if (shunxuViewGroup != null) {
			questionTitleViewGroup.removeView(shunxuViewGroup);
		}

		if (doDataObject.getQuestionType() == QuestionTypeEnums.SHUNXU
				.getTypeCode()) {
			shunxuViewGroup = new ShunxuViewGroup(this, R.id.txtTitleQCSC);
			questionTitleViewGroup.addView(shunxuViewGroup);
			int count = doDataObject.getDiaoyanQuestion().getOptionCount();
			while (count > 0) {
				final ShunxuTitleItem item = new ShunxuTitleItem(this,
						shunxuViewGroup);
				shunxuViewGroup.addView(item);
				count--;
			}

		}

		switch (doDataObject.getQuestionType()) {
		case 1:
			danxuanChoice = new DanxuanChoice2(this, doDataObject.getChoices(),
					PreviousUserQuestionCache.getInstance());
			quceshiContentArea.addView(danxuanChoice);

			break;
		case 2:
			duoxuanChoice = new DuoxuanChoice2(this, doDataObject.getChoices(),
					PreviousUserQuestionCache.getInstance());
			duoxuanChoice.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			quceshiContentArea.addView(duoxuanChoice);
			break;
		case 3:
			wenda = new Wenda(this, doDataObject.getChoices(),
					PreviousUserQuestionCache.getInstance());
			wenda.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			quceshiContentArea.addView(wenda);
			break;
		case 4:
			ShunxuChoice aShunxuChoice = new ShunxuChoice(this,
					doDataObject.getChoices(), shunxuViewGroup,
					PreviousUserQuestionCache.getInstance());
			aShunxuChoice.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			quceshiContentArea.addView(aShunxuChoice);

			break;
		case 5:
			if (dafenFenshuOverlayer == null) {
				dafenFenshuOverlayer = new DafenFenshuOverlayer2(this);
			}
			dafenFenshuOverlayer.initScore(doDataObject.getScore());
			dafenChoice = new DafenChoice2(this,
					doDataObject.getDiaoyanQuestionMatrixList(),
					dafenFenshuOverlayer,
					PreviousUserQuestionCache.getInstance());
			dafenChoice.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			quceshiContentArea.addView(dafenChoice);
			break;
		default:

			break;

		}
		prevQuestionBtn.setOnClickListener(this);

		if (countDownTimer != null) {
			countDownTimer.cancel();
			remainingTime.setVisibility(View.GONE);
			nextQuestionBtn.setBackgroundResource(R.drawable.question_next);

		}

		int limitTime = 0;
		if (hasCountDownTimer) {
			limitTime = doDataObject.getLimitTime();
		}

		if (limitTime == 0) {
			nextQuestionBtn.setOnClickListener(this);
		} else {
			nextQuestionBtn
					.setBackgroundResource(R.drawable.question_next_disable);
			remainingTime.setVisibility(View.VISIBLE);
			remainingTime.setText(String.valueOf(limitTime));
			countDownTimer = new CountDownTimer(limitTime * 1000, 100) {

				public void onTick(long millisUntilFinished) {
					remainingTime.setText(String
							.valueOf((millisUntilFinished / 1000) + 1));
				}

				public void onFinish() {
					remainingTime.setVisibility(View.GONE);
					nextQuestionBtn
							.setBackgroundResource(R.drawable.question_next);
					nextQuestionBtn
							.setOnClickListener(SelfContentActivity.this);
				}
			};
			countDownTimer.start();

		}

		if (this.progressDialog.isShowing()) {
			progressDialog.dismiss();
		}

	}

	// 完成问卷提交答案
	private class SubmitDataTask extends AsyncTask<Integer, Void, Object> {

		@Override
		protected Object doInBackground(Integer... params) {
			try {
				JSONObject jbo = DoQuestionAnswer.loadSelfAnswers(params[0]);
				Log.i("SelfContentActivity", "isRetest...three..." + isRetest);
				jbo.put("refresh", isRetest);// 是否是重测
				return selfAnswerService.getAnswer(jbo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object params) {
			if (params != null) {
				Intent intent = new Intent(SelfContentActivity.this,
						SelfAnswerActivity.class);
				intent.putExtra(SelfAnswerActivity.INTENT_SELF_TITLE,
						getIntent().getStringExtra(INTENT_QUESTIONNAIRE_TITLE));
				intent.putExtra(SelfAnswerActivity.INTENT_SELF_ANSWER,
						((SelfUserAnswer) params).getContent());
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				SelfContentActivity.this.finish();
			}
		}
	}

}
