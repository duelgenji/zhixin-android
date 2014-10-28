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
import com.qubaopen.customui.DuoxuanChoice;
import com.qubaopen.customui.ShunxuChoice;
import com.qubaopen.customui.ShunxuTitleItem;
import com.qubaopen.customui.ShunxuViewGroup;
import com.qubaopen.customui.Wenda;
import com.qubaopen.datasynservice.InterestAnswerService;
import com.qubaopen.datasynservice.InterestContentService;
import com.qubaopen.datasynservice.QuceshiAnswerService;
import com.qubaopen.dialog.DafenFenshuOverlayer2;
import com.qubaopen.dialog.QubaopenProgressDialog;
import com.qubaopen.domain.InterestUserAnswer;
import com.qubaopen.domain.UserQuestionAnswer;
import com.qubaopen.enums.QuestionTypeEnums;
import com.qubaopen.logic.DoDataObject;
import com.qubaopen.logic.DoLogicObject;
import com.qubaopen.logic.DoQuestionAnswer;
import com.qubaopen.utils.NetworkUtils;

public class InterestContentActivity extends Activity implements
		View.OnClickListener, DoDataObject.DiaoyanDatiLoadFinished {

	public static final String INTENT_INTEREST_ID = "interestId";
	public static final String INTENT_QUESTIONNAIRE_TITLE = "questionnaireTitle";
	public static final String CURRENT_QUESTION = "currentQuestion";

	private InterestContentService interestContentService;
	private InterestAnswerService interestAnswerService;
	private QuceshiAnswerService quceshiAnswerService;
	private Integer interestId;

	private RelativeLayout quceshiContentArea;

	private DoDataObject quDatiDataObject;
	private DoLogicObject quDatiLogicObject;

	private View nextQuestionBtn;
	private View prevQuestionBtn;
	private TextView txtNoQCSC;
	private TextView remainingTime;
    private ImageButton iBtnPageBack;
	private TextView txtPageTitle;

	private DanxuanChoice2 danxuanChoice;
	private DuoxuanChoice duoxuanChoice;
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
					String message = interestContentService.getInterestContent(params[0]);
					if (message == null) {
						quDatiLogicObject = new DoLogicObject(interestId,0);
					}

					return message;
				} else {
					quDatiLogicObject = new DoLogicObject(params[0], true, 0);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String params) {
			if (params == null) {
				quDatiDataObject = new DoDataObject(quDatiLogicObject
						.getCurrentQuestion().getQuestionId(), 0);
				quDatiDataObject
						.setmQuDatiLoadFinished(InterestContentActivity.this);
				quDatiDataObject.execute();
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
		iBtnPageBack =(ImageButton) this.findViewById(R.id.backup_btn);
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
		
		interestContentService =new InterestContentService(this);
		interestAnswerService = new InterestAnswerService(this);
		String title=intent.getStringExtra(INTENT_QUESTIONNAIRE_TITLE);
		((TextView) this.findViewById(R.id.txt_title)).setText(title);
		
		interestId = getIntent().getIntExtra(INTENT_INTEREST_ID, 0);
		int questionId = intent.getIntExtra(CURRENT_QUESTION, 0);
		if (questionId == 0) {
			new LoadDataTask(true).execute(interestId);
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
			switch (quDatiDataObject.getQuestionType()) {
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
			quDatiLogicObject.setAnswer(ans);
			Integer nextQuestionId = quDatiLogicObject.getNextQuestionId();
			//Integer nextQuestionId=quDatiDataObject.getQuestionId()+1;
			if (nextQuestionId != null) {
				Intent intent = new Intent(this, InterestContentActivity.class);
				intent.putExtra(CURRENT_QUESTION, nextQuestionId.intValue());
				intent.putExtra(INTENT_QUESTIONNAIRE_TITLE,getIntent().getStringExtra(INTENT_QUESTIONNAIRE_TITLE));
				startActivity(intent);
			} else {

				if (NetworkUtils.isNetworkAvailable(this)) {

					AlertDialog alert = new AlertDialog.Builder(this)
							.setTitle("提示")
							.setMessage(
									InterestContentActivity.this
											.getString(R.string.submmit_wj_tips))
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {// 设置确定按钮
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											
											nextQuestionBtn.setEnabled(false);
											new SubmitDataTask()
											.execute(quDatiDataObject
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
		Integer prevQuestionId = quDatiLogicObject.getHistoryAnswerCursor()
				.previousQuestion();
		if (prevQuestionId != null) {
			Intent intent = new Intent(this, InterestContentActivity.class);
			intent.putExtra(INTENT_QUESTIONNAIRE_TITLE,getIntent().getStringExtra(INTENT_QUESTIONNAIRE_TITLE));
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

		AlertDialog alert = new AlertDialog.Builder(this)
				.setTitle("提示")
				.setMessage(
						InterestContentActivity.this
								.getString(R.string.back_wj_tips))
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {// 设置确定按钮
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								InterestContentActivity.super.onBackPressed();
							}
						})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {// 设置取消按钮
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}).create();
		alert.show();

	}

	@Override
	public void displayThings() {
		boolean hasCountDownTimer = false;
		if (PreviousUserQuestionCache.getInstance() == null) {
			hasCountDownTimer = true;
		}

		TextView textView = (TextView) this.findViewById(R.id.txtTitleQCSC);
		
		String content=quDatiDataObject.getQuestionTitle();
		content=content.replace("\\n", "\n");
		textView.setText(content);
		
		txtNoQCSC.setText(quDatiDataObject.getQuestionNo() + "/"
				+ String.valueOf(quDatiDataObject.getQuestionSumInWj()));
		if (shunxuViewGroup != null) {
			questionTitleViewGroup.removeView(shunxuViewGroup);
		}

		if (quDatiDataObject.getQuestionType() == QuestionTypeEnums.SHUNXU
				.getTypeCode()) {
			shunxuViewGroup = new ShunxuViewGroup(this, R.id.txtTitleQCSC);
			questionTitleViewGroup.addView(shunxuViewGroup);
			int count = quDatiDataObject.getDiaoyanQuestion().getOptionCount();
			while (count > 0) {
				final ShunxuTitleItem item = new ShunxuTitleItem(this,
						shunxuViewGroup);
				shunxuViewGroup.addView(item);
				count--;
			}

		}

		switch (quDatiDataObject.getQuestionType()) {
		case 1:
			danxuanChoice = new DanxuanChoice2(this,
					quDatiDataObject.getChoices(),
					PreviousUserQuestionCache.getInstance());
			quceshiContentArea.addView(danxuanChoice);

			break;
		case 2:
//			duoxuanChoice = new DuoxuanChoice(this,
//					quDatiDataObject.getChoices(),
//					PreviousUserQuestionCache.getInstance());
//			duoxuanChoice.setLayoutParams(new LayoutParams(
//					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//			quceshiContentArea.addView(duoxuanChoice);
			break;
		case 3:
			wenda = new Wenda(this, quDatiDataObject.getChoices(),
					PreviousUserQuestionCache.getInstance());
			wenda.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			quceshiContentArea.addView(wenda);
			break;
		case 4:
			ShunxuChoice aShunxuChoice = new ShunxuChoice(this,
					quDatiDataObject.getChoices(), shunxuViewGroup,
					PreviousUserQuestionCache.getInstance());
			aShunxuChoice.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			quceshiContentArea.addView(aShunxuChoice);

			break;
		case 5:
			if (dafenFenshuOverlayer == null) {
				dafenFenshuOverlayer = new DafenFenshuOverlayer2(this);
			}
			dafenFenshuOverlayer.initScore(quDatiDataObject.getScore());
			dafenChoice = new DafenChoice2(this,
					quDatiDataObject.getDiaoyanQuestionMatrixList(),
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
			limitTime = quDatiDataObject.getLimitTime();
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
							.valueOf(millisUntilFinished / 1000));
				}

				public void onFinish() {
					remainingTime.setVisibility(View.GONE);
					nextQuestionBtn
							.setBackgroundResource(R.drawable.question_next);
					nextQuestionBtn
							.setOnClickListener(InterestContentActivity.this);
				}
			};
			countDownTimer.start();

		}

		if (this.progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		
	}
	
	
	

	//完成问卷提交答案
	private class SubmitDataTask extends AsyncTask<Integer, Void, Object> {

		@Override
		protected Object doInBackground(Integer... params) {
			try {
				JSONObject jbo = DoQuestionAnswer.loadQuAnswers(params[0]);
				return interestAnswerService.getAnswer(jbo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object params) {
			if (params != null) {
				Intent intent = new Intent(InterestContentActivity.this,
						InterestAnswerActivity.class);
				intent.putExtra(InterestAnswerActivity.INTENT_INTEREST_TITLE,getIntent().getStringExtra(INTENT_QUESTIONNAIRE_TITLE));
				intent.putExtra(InterestAnswerActivity.INTENT_INTEREST_ANSWER,((InterestUserAnswer)params).getContent());
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				InterestContentActivity.this.finish();
			}
		}
	}

}
