package com.zhixin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhixin.R;

public class MoreAboutusActivity extends Activity implements View.OnClickListener{
	private ImageButton backup_btn;
	private TextView txtPageTitle; 
	
	private ImageView imgLogo;
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_user_aboutus);
			
			txtPageTitle=(TextView) findViewById(R.id.title_of_the_page);
			txtPageTitle.setText(R.string.title_more_aboutus);
			
			backup_btn = (ImageButton) findViewById(R.id.backup_btn);
			backup_btn.setOnClickListener(this);
			
			imgLogo= (ImageView) findViewById(R.id.imgLogo);
			imgLogo.setOnClickListener(this);
		
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.backup_btn:
				v.setEnabled(false);
				finish();
				v.setEnabled(true);
				break;
			case R.id.imgLogo:
				Intent intent = new Intent(MoreAboutusActivity.this,
						FristInIntroduceActivity.class);
				intent.putExtra(FristInIntroduceActivity.IS_EASTER_EGG, true);
				startActivity(intent);
				v.setEnabled(true);
				break;
			default:
				break;
			}
		}
		
		
}
