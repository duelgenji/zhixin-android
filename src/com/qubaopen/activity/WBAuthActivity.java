package com.qubaopen.activity;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;

import com.qubaopen.R;
import com.qubaopen.settings.MyApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class WBAuthActivity extends Activity {

	
	  /** 显示认证后的信息，如 AccessToken */
    private TextView mTokenText;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        
        // 获取 Token View，并让提示 View 的内容可滚动（小屏幕可能显示不全）
        mTokenText = (TextView) findViewById(R.id.token_text_view);
       
        
        // Web 授权
        findViewById(R.id.obtain_token_via_signature).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            	Platform sinaWeibo= ShareSDK.getPlatform(MyApplication.getAppContext(), SinaWeibo.NAME);

            	sinaWeibo.setPlatformActionListener(new PlatformActionListener() {
            	                        
            	                        @Override
            	                        public void onError(Platform platform, int action, Throwable t) {
            	                        	Log.i("ssssss","e");
            	                                // TODO Auto-generated method stub                              
            	                        }
            	                        
            	                        @Override
            	                        public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
            	                                // 通过platform.getDb().getUserId();
            	                                String name=platform.getDb().getUserName(); //等等来获取用户信息       
            	                                Log.i("ssssss","s:"+name);
            	                        }
            	                        
            	                        @Override
            	                        public void onCancel(Platform platform, int action) {
            	                        	Log.i("ssssss","c");
            	                                // TODO Auto-generated method stub                              
            	                        }
            	                });
            	sinaWeibo.authorize();
            }
        });
        
      
    }
	
	
	
}
