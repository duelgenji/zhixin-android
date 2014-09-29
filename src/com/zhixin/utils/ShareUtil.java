package com.zhixin.utils;

import com.zhixin.R;
import com.zhixin.cn.sharesdk.onekeyshare.OnekeyShare;
import com.zhixin.settings.MyApplication;

public class ShareUtil {
	

	public static void showShare(String title,String comment) {
	       
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        
        // 分享时Notification的图标和文字
        oks.setNotification(R.drawable.ic_launcher, MyApplication.getAppContext().getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(MyApplication.getAppContext().getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(MyApplication.getAppContext().getResources().getString(R.string.share_redirec_url));
        // text是分享文本，所有平台都需要这个字段
        oks.setText(title);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(MyApplication.getAppContext().getResources().getString(R.string.share_redirec_url));
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(comment);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(MyApplication.getAppContext().getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(MyApplication.getAppContext().getResources().getString(R.string.share_redirec_url));

        // 启动分享GUI
        oks.show(MyApplication.getAppContext());
   }

}
