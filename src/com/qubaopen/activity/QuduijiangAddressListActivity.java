package com.qubaopen.activity;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.qubaopen.R;
import com.qubaopen.adapter.UserAddressDuijiangAdapter;
import com.qubaopen.datasynservice.QuduijiangConfirmService;
import com.qubaopen.datasynservice.UserAddressService;
import com.qubaopen.domain.UserAddress;
import com.qubaopen.settings.ErrHashMap;
import com.qubaopen.utils.SqlCursorLoader;

/**
 * Created by duel on 14-3-20.
 */
public class QuduijiangAddressListActivity extends FragmentActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,View.OnClickListener {

    public static final String KEY_INTENT_DUIJIANG_ID="lotteryId";
    private TextView txtPageTitle;
    private ImageButton iBtnPageBack;
    private Button btnAddressManage;
    private Button btnConfirm;

    private ListView userAddressList;

    private QuduijiangAddressListActivity _this;
    private QuduijiangConfirmService quduijiangConfirmService;
    private UserAddressService userAddressService;
    private UserAddressDuijiangAdapter adapter;
    private String lotterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quduijiang_address_list);
        _this=this;
        userAddressService=new UserAddressService(this);
        quduijiangConfirmService=new QuduijiangConfirmService(this);
        txtPageTitle= (TextView)
                this.findViewById(R.id.title_of_the_page);
        iBtnPageBack =(ImageButton) this.findViewById(R.id.backup_btn);
        iBtnPageBack.setOnClickListener(this);
        btnAddressManage=(Button) this.findViewById(R.id.btnAddressManage);
        btnAddressManage.setOnClickListener(this);
        txtPageTitle.setText(this.getString(R.string.title_pick_address));
        userAddressList=(ListView) this.findViewById(R.id.userAddressList);
        btnConfirm=(Button) this.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
        lotterId=getIntent().getStringExtra(KEY_INTENT_DUIJIANG_ID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onClick(View v) {

        v.setEnabled(false);
        switch (v.getId()) {
            case R.id.backup_btn:
                this.onBackPressed();
                v.setEnabled(true);
                break;
            case R.id.btnAddressManage:
                Intent intent =new Intent(this,UserInfoAddressActivity.class);
                startActivity(intent);
                v.setEnabled(true);
                break;
            case R.id.btnConfirm:
                AlertDialog alert = new AlertDialog.Builder(this).setTitle("提示")
                        .setMessage("确认兑奖吗？")
                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//设置确定按钮
                            @Override//处理确定按钮点击事件
                            public void onClick(DialogInterface dialog, int which) {
                                new LoadDataTask().execute();
                                dialog.cancel();//对话框关闭。
                            }
                        })
                        .setNegativeButton("取消",new DialogInterface.OnClickListener() {//设置取消按钮
                            @Override//取消按钮点击事件
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();//对话框关闭。
                            }
                        }).create();
                alert.show();
                v.setEnabled(true);
                break;
            default:
                break;

        }
        v.setEnabled(true);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new SqlCursorLoader(this,
                UserAddressService.UserAddressSqlMaker.makeSql(), UserAddress.class);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.getCount() != 0) {
            if (adapter == null) {
                adapter = new UserAddressDuijiangAdapter(this, cursor);
                userAddressList.setAdapter(adapter);
            } else {
                adapter.changeCursor(cursor);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (adapter != null) {
            adapter.changeCursor(null);
        }
    }

    private class LoadDataTask extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {
            try {
                String a=lotterId;
                String b=adapter.getCheckedDzId()+"";
                return quduijiangConfirmService.sendRequest(a,b);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jbo) {
            if (jbo != null) {
                try {
                    if (jbo.has("success")
                            && jbo.getString("success").equals("1")) {
                        showToast(_this
                                .getString(R.string.toast_duijiang_success));
                        Intent intent = new Intent(_this,MainActivity.class);
                        startActivity(intent);

                    } else if (jbo.getString("success").equals("0")) {
                        String context = ErrHashMap.getErrMessage(jbo
                                .getString("message"));
                        context = context == null ? _this
                                .getString(R.string.toast_unknown) : context;
                        Toast.makeText(_this, context, 5).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    private void showToast(String content) {
        Toast.makeText(QuduijiangAddressListActivity.this, content, 3).show();
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
