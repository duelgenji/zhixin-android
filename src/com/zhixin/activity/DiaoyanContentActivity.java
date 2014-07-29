package com.zhixin.activity;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.cache.PreviousUserQuestionCache;
import com.zhixin.customui.DafenChoice;
import com.zhixin.customui.DanxuanChoice;
import com.zhixin.customui.DuoxuanChoice;
import com.zhixin.customui.ShunxuChoice;
import com.zhixin.customui.ShunxuTitleItem;
import com.zhixin.customui.ShunxuViewGroup;
import com.zhixin.customui.Wenda;
import com.zhixin.datasynservice.DiaoyanAnswerService;
import com.zhixin.datasynservice.DiaoyanContentService;
import com.zhixin.dialog.DafenFenshuOverlayer;
import com.zhixin.dialog.QubaopenProgressDialog;
import com.zhixin.domain.UserQuestionAnswer;
import com.zhixin.enums.QuestionTypeEnums;
import com.zhixin.logic.DatiDataObject;
import com.zhixin.logic.DatiLogicObject;
import com.zhixin.logic.DatiQuestionAnswer;
import com.zhixin.utils.NetworkUtils;

public class DiaoyanContentActivity extends Activity implements
		View.OnClickListener, DatiDataObject.DiaoyanDatiLoadFinished {

	private int questionnareId;

	// intent string
	public static final String INTENT_QUESIONNARE_ID = "quesionnareId";
	public static final String CURRENT_QUESTION = "currentQuestion";

	// data sync services
	private DiaoyanContentService diaoyanContentService;
	private DiaoyanAnswerService diaoyanAnswerService;

	// Logic object
	private DatiDataObject diaoyanDatiDataObject;
	private DatiLogicObject diaoyanDatiLogicObject;

	// View Object
	private RelativeLayout quceshiContentArea;
	private View nextQuestionBtn;
	private View prevQuestionBtn;
	private TextView txtNoQCSC;
	private TextView remainingTime;
	private RelativeLayout questionTitleViewGroup;
	private ViewGroup programeOverlayDisplayControllArea;
	private DafenFenshuOverlayer dafenFenshuOverlayer;

	// Question object
	private DanxuanChoice danxuanChoice;
	private DuoxuanChoice duoxuanChoice;
	private Wenda wenda;
	private ShunxuViewGroup shunxuViewGroup;
	private DafenChoice dafenChoice;

	private CountDownTimer countDownTimer;

	private QubaopenProgressDialog progressDialog;

	private class LoadDataTask extends AsyncTask<Integer, Void, String> {
		private boolean theFirstTime;

		public LoadDataTask(boolean theFirstTime) {
			this.theFirstTime = theFirstTime;

		}

		@Override
		protected String doInBackground(Integer... params) {
			try {
				if (theFirstTime) {
					String message = diaoyanContentService.saveData(params[0]);
					if (message == null) {
						diaoyanDatiLogicObject = new DatiLogicObject(
								questionnareId, 1);
					}

					return message;
				} else {
					diaoyanDatiLogicObject = new DatiLogicObject(params[0],
							true, 1);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String params) {
			if (params == null) {
				diaoyanDatiDataObject = new DatiDataObject(
						diaoyanDatiLogicObject.getCurrentQuestion()
								.getQuestionId(), 1);
				diaoyanDatiDataObject
						.setmQuDatiLoadFinished(DiaoyanContentActivity.this);
				diaoyanDatiDataObject.execute();
			}
		}
	}

	private class SubmitDataTask extends AsyncTask<Integer, Void, Boolean> {
		private int wjId;

		@Override
		protected Boolean doInBackground(Integer... params) {
			try {
				JSONObject jbo = DatiQuestionAnswer
						.loadDiaoyanAnswers(params[0]);
				this.wjId = jbo.getInt("iWjId");
				return diaoyanAnswerService.getAnswer(jbo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean params) {
			if (params != null && params == true) {
				Intent intent = new Intent(DiaoyanContentActivity.this,
						DiaoyanResultActivity.class);
				intent.putExtra(DiaoyanResultActivity.INTENT_WJ_ID, this.wjId);

				intent.putExtra(DiaoyanResultActivity.SUCCESS_OR_NOT, params);

				startActivity(intent);
				DiaoyanContentActivity.this.finish();
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
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		init(intent);

	}

	private void init(Intent intent) {
		progressDialog = new QubaopenProgressDialog(this);
		programeOverlayDisplayControllArea = (ViewGroup) this
				.findViewById(R.id.programeOverlayDisplayControllArea);

		quceshiContentArea = (RelativeLayout) this
				.findViewById(R.id.quceshiContentArea);
		quceshiContentArea.removeAllViews();

		questionTitleViewGroup = (RelativeLayout) this
				.findViewById(R.id.questionTitleViewGroup);

		nextQuestionBtn = this.findViewById(R.id.nextQuestionBtn);
		nextQuestionBtn.setOnClickListener(null);
		prevQuestionBtn = this.findViewById(R.id.prevQuestionBtn);
		prevQuestionBtn.setOnClickListener(null);
		remainingTime = (TextView) this.findViewById(R.id.remainingTime);

		txtNoQCSC = (TextView) this.findViewById(R.id.txtNoQCSC);

		diaoyanContentService = new DiaoyanContentService(this);
		diaoyanAnswerService = new DiaoyanAnswerService(this);

		questionnareId = getIntent().getIntExtra(INTENT_QUESIONNARE_ID, 0);
		int questionId = intent.getIntExtra(CURRENT_QUESTION, 0);

		((TextView) this.findViewById(R.id.title_of_the_page))
				.setText(getString(R.string.title_qu_diaoyan));
		this.findViewById(R.id.backup_btn).setOnClickListener(this);

		if (!progressDialog.isShowing()) {

			progressDialog.show();
		}
		if (questionId == 0) {
			new LoadDataTask(true).execute(new Integer(questionnareId));
		} else {
			new LoadDataTask(false).execute(new Integer(questionId));
		}
	}

	@Override
	public void displayThings() {
		boolean hasCountDownTimer = false;
		if (PreviousUserQuestionCache.getInstance() == null) {
			hasCountDownTimer = true;
		}

		TextView textView = (TextView) this.findViewById(R.id.txtTitleQCSC);
		textView.setText(diaoyanDatiDataObject.getQuestionTitle());
		txtNoQCSC.setText(diaoyanDatiDataObject.getQuestionNo() + "/"
				+ String.valueOf(diaoyanDatiDataObject.getQuestionSumInWj()));
		if (shunxuViewGroup != null) {
			questionTitleViewGroup.removeView(shunxuViewGroup);
		}

		if (diaoyanDatiDataObject.getQuestionType() == QuestionTypeEnums.SHUNXU
				.getTypeCode()) {
			shunxuViewGroup = new ShunxuViewGroup(this, R.id.txtTitleQCSC);

			questionTitleViewGroup.addView(shunxuViewGroup);

			int count = diaoyanDatiDataObject.getDiaoyanQuestion()
					.getChoiceNumber();

			while (count > 0) {
				ShunxuTitleItem item = new ShunxuTitleItem(this,
						shunxuViewGroup);

				shunxuViewGroup.addView(item);
				count--;
			}

		}

		switch (diaoyanDatiDataObject.getQuestionType()) {
		case 1:
			danxuanChoice = new DanxuanChoice(this,
					diaoyanDatiDataObject.getChoices(),
					PreviousUserQuestionCache.getInstance());
			quceshiContentArea.addView(danxuanChoice);

			break;
		case 2:
			duoxuanChoice = new DuoxuanChoice(this,
					diaoyanDatiDataObject.getChoices(),
					PreviousUserQuestionCache.getInstance());
			duoxuanChoice.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			quceshiContentArea.addView(duoxuanChoice);
			break;
		case 3:
			wenda = new Wenda(this, diaoyanDatiDataObject.getQuestionId(),
					diaoyanDatiDataObject.getDiaoyanQuestion()
							.getChoiceNumber(),
					PreviousUserQuestionCache.getInstance());
			wenda.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			quceshiContentArea.addView(wenda);
			break;
		case 4:
			ShunxuChoice aShunxuChoice = new ShunxuChoice(this,
					diaoyanDatiDataObject.getChoices(), shunxuViewGroup,
					PreviousUserQuestionCache.getInstance());
			aShunxuChoice.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			quceshiContentArea.addView(aShunxuChoice);

			break;
		case 5:
			if (dafenFenshuOverlayer == null) {
				dafenFenshuOverlayer = new DafenFenshuOverlayer(this);
			}
			dafenFenshuOverlayer.initScore(diaoyanDatiDataObject.getScore());
			dafenChoice = new DafenChoice(this,
					diaoyanDatiDataObject.getDiaoyanQuestionMatrixList(),
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
			limitTime = diaoyanDatiDataObject.getLimitTime();
		}

		if (limitTime == 0) {
			nextQuestionBtn.setOnClickListener(this);

		} else {
			nextQuestionBtn
					.setBackgroundResource(R.drawable.question_next_disable);
			remainingTime.setVisibility(View.VISIBLE);
			remainingTime.setText(String.valueOf(limitTime));
			countDownTimer = new CountDownTimer(limitTime * 1000, 1000) {

				public void onTick(long millisUntilFinished) {
					remainingTime.setText(String
							.valueOf(millisUntilFinished / 1000));
				}

				public void onFinish() {
					remainingTime.setVisibility(View.GONE);
					nextQuestionBtn
							.setBackgroundResource(R.drawable.question_next);
					nextQuestionBtn
							.setOnClickListener(DiaoyanContentActivity.this);
				}

			};
			countDownTimer.start();

		}

		if (this.progressDialog.isShowing()) {
			progressDialog.dismiss();
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
			switch (diaoyanDatiDataObject.getQuestionType()) {
			case 1:
				ans = danxuanChoice.getCurrentQuDatiQuestionAnswer();
				nextQuestionLogic(ans);
				break;

			case 2:
				ans = duoxuanChoice.getAnswer();
				nextQuestionLogic(ans);
				break;
			case 3:
				ans = wenda.getAnswer();
				nextQuestionLogic(ans);
				break;
			case 4:
				ans = shunxuViewGroup.getAnswer();
				nextQuestionLogic(ans);
				break;
			case 5:
				ans = dafenChoice.getAnswer();
				nextQuestionLogic(ans);
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

	private void nextQuestionLogic(List<UserQuestionAnswer> ans) {
		if (ans != null) {
			diaoyanDatiLogicObject.setAnswer(ans);
			Integer nextQuestionId;
			try {
				nextQuestionId = diaoyanDatiLogicObject.nextQuestionId();

				if (nextQuestionId != null) {
					Intent intent = new Intent(this,
							DiaoyanContentActivity.class);
					intent.putExtra(CURRENT_QUESTION, nextQuestionId.intValue());
					startActivity(intent);
				} else {

					if (NetworkUtils.isNetworkAvailable(this)) {

						AlertDialog alert = new AlertDialog.Builder(this)
								.setTitle("提示")
								.setMessage(
										this.getString(R.string.submmit_wj_tips))
								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {// 设置确定按钮
											@Override
											// 处理确定按钮点击事件
											public void onClick(
													DialogInterface dialog,
													int which) {
												nextQuestionBtn
														.setEnabled(false);
												new SubmitDataTask()
														.execute(diaoyanDatiDataObject
																.getWjId());
											}
										})
								.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {// 设置取消按钮
											@Override
											// 取消按钮点击事件
											public void onClick(
													DialogInterface dialog,
													int which) {

											}
										}).create();
						alert.show();

					} else {
						Toast.makeText(this, this
								.getString(R.string.toast_network_unvaiable),
								Toast.LENGTH_SHORT).show();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(this, getString(R.string.dati_question_unanswered),
					Toast.LENGTH_SHORT).show();
		}
	}

	private void prevQuestionLogic() {
		Integer prevQuestionId = diaoyanDatiLogicObject
				.getHistoryAnswerCursor().previousQuestion();
		if (prevQuestionId != null) {
			Intent intent = new Intent(this, DiaoyanContentActivity.class);
			intent.putExtra(CURRENT_QUESTION, prevQuestionId.intValue());
			startActivity(intent);
		}
	}

	@Override
	public void onBackPressed() {

		AlertDialog alert = new AlertDialog.Builder(this)
				.setTitle("提示")
				.setMessage(this.getString(R.string.back_wj_tips))
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {// 设置确定按钮
							@Override
							// 处理确定按钮点击事件
							public void onClick(DialogInterface dialog,
									int which) {
								DiaoyanContentActivity.super.onBackPressed();
							}
						})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {// 设置取消按钮
							@Override
							// 取消按钮点击事件
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).create();
		alert.show();

	}
}
