package com.zhixin.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.zhixin.R;

public class MoreAboutusActivity extends Activity implements View.OnClickListener{
	private ImageButton backup_btn;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_user_aboutus);
			backup_btn = (ImageButton) findViewById(R.id.backup_btn);
			backup_btn.setOnClickListener(this);
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.backup_btn:
				v.setEnabled(false);
				this.onBackPressed();
				v.setEnabled(true);
				break;

			default:
				break;
			}
		}
		
		
}
