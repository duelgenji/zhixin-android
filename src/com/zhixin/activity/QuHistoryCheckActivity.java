package com.zhixin.activity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.zhixin.R;
import com.zhixin.datasynservice.QuCjDjActionService;
import com.zhixin.settings.ErrHashMap;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.QBPShareFunction;
import com.zhixin.utils.QrCodeImageGenerator;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by duel on 14-3-20.
 */
public class QuHistoryCheckActivity extends FragmentActivity implements
		View.OnClickListener {

	public static final String KEY_INTENT_LOTTERY_ID = "lotteryId";
	public static final String KEY_INTENT_LOTTERY_TYPE = "lotteryType";

	private TextView txtPageTitle;
	private ImageButton iBtnPageBack;

	private ImageView qrCodeImage;

    private LinearLayout layoutQRCode;

	private QuHistoryCheckActivity _this;

	private QuCjDjActionService quCjDjActionService;

	private ImageLoader imageLoader;
	private DisplayImageOptions imageOptions;

	private int lotteryId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quhistory_check);

		qrCodeImage = (ImageView) this.findViewById(R.id.qrcodeImage);

		_this = this;
		quCjDjActionService = new QuCjDjActionService(this);
		imageLoader = ImageLoader.getInstance();
		imageOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();
		txtPageTitle = (TextView) this.findViewById(R.id.title_of_the_page);
		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
        layoutQRCode=(LinearLayout)this.findViewById(R.id.layoutQRCode);
		iBtnPageBack.setOnClickListener(this);
		txtPageTitle.setText(this.getString(R.string.title_history_check));

	}

	@Override
	protected void onStart() {
		super.onStart();
		lotteryId = this.getIntent().getIntExtra(
				QuHistoryCheckActivity.KEY_INTENT_LOTTERY_ID, 0);
		int lotteryType = this.getIntent().getIntExtra(
				QuHistoryCheckActivity.KEY_INTENT_LOTTERY_TYPE, 0);
		new LoadDataTask().execute("0", lotteryType, lotteryId);
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

	private class LoadDataTask extends AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result = new JSONObject();
			try {
				Integer type = Integer.parseInt(params[0].toString());
				Integer lotteryType = Integer.parseInt(params[1].toString());
				Integer lotteryId = Integer.parseInt(params[2].toString());
				if (type.equals(0)) {
					result = quCjDjActionService.checkLottery(lotteryType,
							lotteryId);
				}
				result.put("type", type);
				result.put("lotteryId", lotteryId);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(JSONObject jbo) {
			try {
				if (jbo.has("success") && jbo.getString("success").equals("1")) {

					updatingContentInView(jbo);

				} else if (jbo.getString("success").equals("0")) {
					String content = ErrHashMap.getErrMessage(jbo
							.getString("message"));
					content = content == null ? _this
							.getString(R.string.toast_unknown) : content;
					Toast.makeText(_this, content, 5).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	private void updatingContentInView(JSONObject jbo) {
		try {
			String sContent = "";
			String sRemark = "";
			String sTitle = "";
			String sRemainTime = "";
			String sQRCode = "";
			String sMM = "";
			String sPicUrl = "";
			String jpXxId = "";
			String sOutPut = "";

			if (jbo.has("sTitle")) {
				sTitle = jbo.getString("sTitle");
				if (!sTitle.equals("")) {
					sOutPut += "<font color=#269BF6>" + sTitle + "</font><br/>";
				}
				new QBPShareFunction(
						QuHistoryCheckActivity.this
								.findViewById(R.id.shareComponent),
						QBPShareFunction.QU_CESHI, sTitle,
						QuHistoryCheckActivity.this, lotteryId);

			}
			if (jbo.has("sContent")) {
				sContent = jbo.getString("sContent");
				if (!sContent.equals("")) {
					sOutPut += "<font color=#323231>" + sContent
							+ "</font><br/>";
				}
			}
			if (jbo.has("sMM")) {
				sMM = jbo.getString("sMM");
				if (!sMM.equals("")) {
					sOutPut += "<font color=#ff0000>" + sMM + "</font><br/>";
				}
			}

			if (jbo.has("sRemainTime")) {
				sRemainTime = jbo.getString("sRemainTime");
				if (!sRemainTime.equals("")) {
					sOutPut += "有效期至:" + sRemainTime + "<br/>";
				}
			}
			if (jbo.has("sRemark")) {
				sRemark = jbo.getString("sRemark");
				if (!sRemark.equals("")) {
					sOutPut += "备注:" + sRemark;
				}
			}
			if (jbo.has("sQRCode")) {
				sQRCode = jbo.getString("sQRCode");
                if(!sQRCode.equals("")){
                    layoutQRCode.setVisibility(View.VISIBLE);
                }
			}
			if (jbo.has("sPicUrl")) {
				sPicUrl = jbo.getString("sPicUrl");
				ImageView rewardImage = (ImageView) this
						.findViewById(R.id.rewardImage);
				if (StringUtils.isNotEmpty(sPicUrl)) {
					sPicUrl = SettingValues.URL_PREFIX + sPicUrl;

					imageLoader.displayImage(sPicUrl, rewardImage,
							imageOptions, new ImageLoading(rewardImage), null);
				} else {
					rewardImage
							.setImageResource(R.drawable.quchoujiang_default_background);
				}
			}

			TextView txtItemContent = (TextView) this
					.findViewById(R.id.txtItemContent);
			txtItemContent.setText(Html.fromHtml(sOutPut));
			final String finalSQRCode = sQRCode;
			if (!StringUtils.isBlank(finalSQRCode)) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						String qrStr = finalSQRCode;
						final Bitmap qrBitmap = QrCodeImageGenerator
								.generateQrImage(qrStr);
						qrCodeImage.post(new Runnable() {
							@Override
							public void run() {
								qrCodeImage.setImageBitmap(qrBitmap);
							}
						});
					}
				}).start();
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private class ImageLoading implements ImageLoadingListener {

		private ImageView imageView;

		public ImageLoading(ImageView view) {
			this.imageView = view;

		}

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			imageView
					.setImageResource(R.drawable.quchoujiang_default_background_large);
		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			imageView.setImageBitmap(loadedImage);
		}

		@Override
		public void onLoadingCancelled(String arg0, View arg1) {

		}

		@Override
		public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

		}

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
