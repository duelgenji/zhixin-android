package com.zhixin.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zhixin.R;

/**
 * Created by duel on 14-3-20.
 */
public class MorePrivacyActivity extends FragmentActivity implements View.OnClickListener {

    private TextView txtPageTitle;
    private ImageButton iBtnPageBack;

    private MorePrivacyActivity _this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_privacy);

        _this=this;
        txtPageTitle= (TextView)
                this.findViewById(R.id.title_of_the_page);
        iBtnPageBack =(ImageButton) this.findViewById(R.id.backup_btn);
        iBtnPageBack.setOnClickListener(this);
        txtPageTitle.setText(this.getString(R.string.title_privacy));

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
