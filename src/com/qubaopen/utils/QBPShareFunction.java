package com.qubaopen.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.qubaopen.R;

public class QBPShareFunction implements View.OnClickListener {
	public static final int SHARE_APP = 1;
	public static final int QU_CESHI = 2;
	public static final int QU_DIAOYAN = 3;
	public static final int QU_HUATI = 4;
	public static final int QU_CHOUJIANG = 5;
	public static final int QU_DUIJANG = 6;

	private int shareId;

	private View xlwbBtn;

	private View txwbBtn;

	private View wxBtn;

	private View qqkjBtn;

	private View pyqBtn;

	private View smsBtn;

	private String shareTitle;

	private String shareContent;

	private String keyWord;

	private Context context;

	private int keyId;

	public QBPShareFunction(View keyView, int shareId, String keyWord,
			Context context, int keyId) {
		xlwbBtn = keyView.findViewById(R.id.btnShareXlwb);
		xlwbBtn.setOnClickListener(this);
		txwbBtn = keyView.findViewById(R.id.btnShareTxwb);
		txwbBtn.setOnClickListener(this);
		wxBtn = keyView.findViewById(R.id.btnShareWx);
		wxBtn.setOnClickListener(this);
		qqkjBtn = keyView.findViewById(R.id.btnShareQqkj);
		qqkjBtn.setOnClickListener(this);
		pyqBtn = keyView.findViewById(R.id.btnSharePyq);
		pyqBtn.setOnClickListener(this);
		smsBtn = keyView.findViewById(R.id.btnShareSMS);
		if (smsBtn != null) {
			smsBtn.setOnClickListener(this);
		}

//		this.keyWord = keyWord;
		this.shareId = shareId;
		this.context = context;
		this.keyId = keyId;
		ShareSDK.initSDK(context);

		init();
	}

	private void init() {
		switch (shareId) {
		case SHARE_APP:
			this.shareTitle = context.getString(R.string.share_title_sharesoft);
			this.shareContent = context
					.getString(R.string.share_content_sharesoft);
//					+ " 接头暗号:"
//					+ keyWord;
			break;
		case QU_CESHI:
			this.shareTitle = context.getString(R.string.share_title_quceshi);
			this.shareContent = context
					.getString(R.string.share_content_quceshi_part1)
//					+ keyWord
					+ context.getString(R.string.share_content_quceshi_part2);
			break;
		case QU_DIAOYAN:
			this.shareTitle = context.getString(R.string.share_title_qudiaoyan);
			this.shareContent = context
					.getString(R.string.share_content_qudiaoyan_part1)
//					+ keyWord
					+ context.getString(R.string.share_content_qudiaoyan_part2);
			break;
		case QU_HUATI:
			this.shareTitle = context.getString(R.string.share_title_quhuati);
			this.shareContent = context
					.getString(R.string.share_content_quhuati_part1)
//					+ keyWord
					+ context.getString(R.string.share_content_quhuati_part2);

			break;
		case QU_CHOUJIANG:
			this.shareTitle = context
					.getString(R.string.share_title_quchoujiang);
			this.shareContent = context
					.getString(R.string.share_content_quchoujiang_part1)
//					+ keyWord
					+ context
							.getString(R.string.share_content_quchoujiang_part2);
			break;

		case QU_DUIJANG:
			this.shareTitle = context
					.getString(R.string.share_title_quduijiang);
			this.shareContent = context
					.getString(R.string.share_content_quduijiang_part1)
//					+ keyWord
					+ context
							.getString(R.string.share_content_quduijiang_part2);
			break;
		default:
			break;

		}

	}

	@Override
	public void onClick(View v) {
		ShareSDK.initSDK(context);
		v.setEnabled(false);
		switch (v.getId()) {
		case R.id.btnShareXlwb:
			shareXlwb(v.getId());
			break;
		case R.id.btnShareTxwb:
			shareTxwb(v.getId());
			break;
		case R.id.btnShareQqkj:
			shareQqkj(v.getId());
			break;
		case R.id.btnShareWx:
			shareWx(v.getId());
			break;
		case R.id.btnSharePyq:
			sharePyq(v.getId());
			break;
		case R.id.btnShareSMS:
			shareSms(v.getId());
			break;
		default:
			break;
		}
	}

	private void shareXlwb(int btnId) {
		cn.sharesdk.sina.weibo.SinaWeibo.ShareParams spSina = new cn.sharesdk.sina.weibo.SinaWeibo.ShareParams();
		spSina.setTitle(shareTitle);
		spSina.setText(shareContent
				+ context.getString(R.string.share_redirec_url));
		spSina.setImageUrl(context.getString(R.string.share_image_url));
		Platform weibo = ShareSDK.getPlatform(context, SinaWeibo.NAME);
		weibo.setPlatformActionListener(new QbpPlatformAction(btnId,
				this.shareId, keyId, context)); // 设置分享事件回调
		weibo.share(spSina);
	}

	private void shareTxwb(int btnId) {
		cn.sharesdk.tencent.weibo.TencentWeibo.ShareParams spTencent = new cn.sharesdk.tencent.weibo.TencentWeibo.ShareParams();
		spTencent.setText(shareContent
				+ context.getString(R.string.share_redirec_url));
		spTencent.setImageUrl(context.getString(R.string.share_image_url));
		Platform txwb = ShareSDK.getPlatform(context, TencentWeibo.NAME);
		txwb.setPlatformActionListener(new QbpPlatformAction(btnId,
				this.shareId, keyId, context));
		txwb.share(spTencent);

	}

	private void shareQqkj(int btnId) {
		cn.sharesdk.tencent.qzone.QZone.ShareParams sp = new cn.sharesdk.tencent.qzone.QZone.ShareParams();
		sp.setTitle(shareTitle);
		sp.setText(shareContent + context.getString(R.string.share_redirec_url));
		sp.setImageUrl(context.getString(R.string.share_image_url));
		sp.setTitleUrl(context.getString(R.string.share_web_url));
		sp.setSite(context.getString(R.string.share_web_url));
		sp.setSiteUrl(context.getString(R.string.share_web_url));
		Platform qzone = ShareSDK.getPlatform(context, "QZone");
		qzone.setPlatformActionListener(new QbpPlatformAction(btnId,
				this.shareId, keyId, context));
		qzone.share(sp);
		qqkjBtn.setEnabled(true);

	}

	private void shareWx(int btnId) {
		cn.sharesdk.wechat.friends.Wechat.ShareParams spWeFriend = new cn.sharesdk.wechat.friends.Wechat.ShareParams();

		spWeFriend.setShareType(Platform.SHARE_WEBPAGE);
		spWeFriend.setText(shareContent
				+ context.getString(R.string.share_redirec_url));
		spWeFriend.setTitle(shareTitle);
		spWeFriend.setImageUrl(context.getString(R.string.share_image_url));
		spWeFriend.setUrl(context.getString(R.string.share_web_url));

		Platform wechatFriend = ShareSDK.getPlatform(context, Wechat.NAME);

		wechatFriend.setPlatformActionListener(new QbpPlatformAction(btnId,
				this.shareId, keyId, context));
		wechatFriend.share(spWeFriend);

	}

	private void sharePyq(int btnId) {

		cn.sharesdk.wechat.moments.WechatMoments.ShareParams spWeMoments = new cn.sharesdk.wechat.moments.WechatMoments.ShareParams();
		spWeMoments.setShareType(Platform.SHARE_WEBPAGE);
		spWeMoments.setText(shareContent
				+ context.getString(R.string.share_redirec_url));
		spWeMoments.setTitle(shareContent
				+ context.getString(R.string.share_redirec_url));
		spWeMoments.setImageUrl(context.getString(R.string.share_image_url));
		spWeMoments.setUrl(context.getString(R.string.share_web_url));

		Platform wechatMoments = ShareSDK.getPlatform(context,
				WechatMoments.NAME);

		wechatMoments.setPlatformActionListener(new QbpPlatformAction(btnId,
				this.shareId, keyId, context));
		wechatMoments.share(spWeMoments);

	}

	private void shareSms(int btnId) {

		Intent sendIntent = new Intent(Intent.ACTION_VIEW);
		sendIntent.setData(Uri.parse("sms:"));
		sendIntent.putExtra(
				"sms_body",
				this.shareContent
						+ context.getString(R.string.share_redirec_url));
		context.startActivity(sendIntent);

	}

}
