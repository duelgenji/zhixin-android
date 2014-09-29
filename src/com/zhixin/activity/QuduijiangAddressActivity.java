package com.zhixin.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baidu.mobstat.StatService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zhixin.R;
import com.zhixin.datasynservice.QuduijiangConfirmService;
import com.zhixin.datasynservice.UserAddressService;
import com.zhixin.domain.UserAddress;
import com.zhixin.settings.ErrHashMap;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.SqlCursorLoader;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by duel on 14-3-20.
 */
public class QuduijiangAddressActivity extends FragmentActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    public static final String KEY_INTENT_DUIJIANG_ID="lotteryId";
    public static final String KEY_INTENT_DUIJIANG_TITLE="title";
    public static final String KEY_INTENT_DUIJIANG_COIN="coin";
    public static final String KEY_INTENT_DUIJIANG_CREDIT="credit";
    public static final String KEY_INTENT_DUIJIANG_URL="picUrl";


    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    private TextView txtPageTitle;
    private ImageButton iBtnPageBack;
    private Button btnHistory;


    private UserAddressService userAddressService;
    private ImageView rewardImage;
    private TextView txtContent;
    private Button btnConfirm;
    private FrameLayout layoutLeftFlag;
    private FrameLayout layoutRightFlag;
    private LinearLayout layoutOtherAddress;
    private LinearLayout layoutDefaultAddress;
    private ToggleButton toggleBtnAddressOther;
    private ToggleButton toggleBtnAddressDefault;
    private TextView txtDefaultAddress;
    private TextView costValue;
    private TextView costType;
    private ImageView costImage;
    private TextView costValueSub;
    private TextView costTypeSub;
    private ImageView costImageSub;

    private QuduijiangAddressActivity _this;
    private int dzId;
    private String lotteryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quduijiang_address);
        _this=this;
        userAddressService=new UserAddressService(this);
        imageLoader = ImageLoader.getInstance();
        imageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true).build();

        txtPageTitle= (TextView)
                this.findViewById(R.id.title_of_the_page);
        iBtnPageBack =(ImageButton) this.findViewById(R.id.backup_btn);
        txtPageTitle.setText(this.getString(R.string.title_quduijiang));
        btnHistory=(Button) this.findViewById(R.id.btnHistory);
        txtDefaultAddress=(TextView) this.findViewById(R.id.txtDefaultAddress);
        btnConfirm=(Button) this.findViewById(R.id.btnConfirm);
        layoutDefaultAddress=(LinearLayout)this.findViewById(R.id.layoutDefaultAddress);
        layoutOtherAddress=(LinearLayout)this.findViewById(R.id.layoutOtherAddress);
        toggleBtnAddressOther=(ToggleButton)this.findViewById(R.id.toggleBtnAddressOther);
        toggleBtnAddressDefault=(ToggleButton)this.findViewById(R.id.toggleBtnAddressDefault);
        rewardImage=(ImageView)this.findViewById(R.id.rewardImage);
        txtContent=(TextView) this.findViewById(R.id.txtContent);
        layoutLeftFlag=(FrameLayout) this.findViewById(R.id.layoutLeftFlag);
        costValue=(TextView) this.findViewById(R.id.costValue);
        costType=(TextView) this.findViewById(R.id.costType);
        costImage=(ImageView) this.findViewById(R.id.costImage);
        costValueSub=(TextView) this.findViewById(R.id.costValueSub);
        costTypeSub=(TextView) this.findViewById(R.id.costTypeSub);
        costImageSub=(ImageView) this.findViewById(R.id.costImageSub);

        layoutLeftFlag.setVisibility(View.GONE);
        toggleBtnAddressOther.setEnabled(false);
        toggleBtnAddressDefault.setEnabled(false);

        layoutDefaultAddress.setOnClickListener(this);
        layoutOtherAddress.setOnClickListener(this);
        iBtnPageBack.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        layoutDefaultAddress.setVisibility(View.GONE);

        String blueText="<font color=#269BF6>"+getIntent().getStringExtra(KEY_INTENT_DUIJIANG_TITLE)+"</font>";
        txtContent.setText(Html.fromHtml("兑换奖品''"+blueText+"',将发送到"));
        int coin=getIntent().getIntExtra(KEY_INTENT_DUIJIANG_COIN,0);
        int credit=getIntent().getIntExtra(KEY_INTENT_DUIJIANG_CREDIT,0);
        lotteryId=getIntent().getIntExtra(KEY_INTENT_DUIJIANG_ID,0)+"";

        Boolean hasRight=false;
        if (credit>0) {
            costValue.setText(credit+"");
            costType.setText(this.getString(R.string.duijiang_credit));
            costImage.setImageResource(R.drawable.flag_green_large);
            hasRight=true;
        }

        if (coin>0) {
            if(!hasRight){
                costValue.setText(coin+"");
                costType.setText(this.getString(R.string.duijiang_coin));
                costImage.setImageResource(R.drawable.flag_red_large);
            }else{
                layoutLeftFlag.setVisibility(View.VISIBLE);
                costValueSub.setText(coin+"");
                costTypeSub.setText(this.getString(R.string.duijiang_coin));
                costImageSub.setImageResource(R.drawable.flag_red_large);
            }
        }


        String imageUri = getIntent().getStringExtra(KEY_INTENT_DUIJIANG_URL);
        if (StringUtils.isNotEmpty(imageUri)) {
            imageUri = SettingValues.URL_PREFIX + imageUri;
            imageLoader.displayImage(imageUri, rewardImage,
                    imageOptions, new ImageLoading(
                    rewardImage), null);
        } else {
            rewardImage.setImageResource(R.drawable.quchoujiang_default_background);
        }


    }

    @Override
    public void onClick(View v) {

        v.setEnabled(false);
        switch (v.getId()) {
            case R.id.backup_btn:
                this.onBackPressed();
                v.setEnabled(true);
                break;
            case R.id.btnConfirm:
                doNext();
                v.setEnabled(true);
                break;
            case R.id.layoutDefaultAddress:
                toggleBtnAddressOther.setChecked(false);
                toggleBtnAddressDefault.setChecked(true);
                v.setEnabled(true);
                break;
            case R.id.layoutOtherAddress:
                toggleBtnAddressDefault.setChecked(false);
                toggleBtnAddressOther.setChecked(true);
                v.setEnabled(true);
                break;
            case R.id.btnHistory:
                Intent intent = new Intent(_this,
                        DuijiangHistoryActivity.class);
                startActivity(intent);
                v.setEnabled(true);
                break;
            default:
                break;

        }
        v.setEnabled(true);
    }


    @Override
    protected void onStart() {
        super.onStart();
        getSupportLoaderManager().restartLoader(0, null, this);
        new LoadDataTask().execute("0");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new SqlCursorLoader(this,
                UserAddressService.UserAddressSqlMaker.makeDefaultAddressSql(), UserAddress.class);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.getCount()!=0){
            updateContentAfterLoading(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    private void updateContentAfterLoading(Cursor cursor) {
        cursor.moveToFirst();
        String defaultAddress="";
        defaultAddress=cursor.getString(cursor.getColumnIndex("phone"))+"";
        defaultAddress+=cursor.getString(cursor.getColumnIndex("name"))+"";
        dzId=cursor.getInt(cursor.getColumnIndex("dzId"));
        layoutDefaultAddress.setVisibility(View.VISIBLE);


        txtDefaultAddress.setText(defaultAddress);
        toggleBtnAddressDefault.setChecked(true);
        toggleBtnAddressOther.setChecked(false);

    }

    private class LoadDataTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                if(params[0].equals("0")){
                    //请求默认地址
                    JSONObject jbo=new JSONObject();
                    userAddressService.saveUserAddress();
                        jbo.put("service","0");
                    return jbo;
                }
                else if(params[0].equals("1")){
                    //选择默认地址 兑奖
                    String a=lotteryId;
                    String b=dzId+"";
                    JSONObject jbo=new QuduijiangConfirmService(QuduijiangAddressActivity.this).sendRequest(a,b);
                    jbo.put("service","1");
                    return jbo;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jbo) {
            try {
                if(jbo.getString("service").equals("0"))
                getSupportLoaderManager().restartLoader(0, null,QuduijiangAddressActivity.this);
                else if(jbo.getString("service").equals("1")){
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

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void doNext(){
        if(toggleBtnAddressDefault.isChecked()){
//            AlertDialog alert = new AlertDialog.Builder(this).setTitle("提示")
//                    .setMessage("确定兑奖吗？")
//                    .setPositiveButton("确定",new DialogInterface.OnClickListener() {//设置确定按钮
//                        @Override//处理确定按钮点击事件
//                        public void onClick(DialogInterface dialog, int which) {
//                            new LoadDataTask().execute("1");
//                            finish();
//                        }
//                    })
//                    .setNegativeButton("取消",new DialogInterface.OnClickListener() {//设置取消按钮
//                        @Override//取消按钮点击事件
//                        public void onClick(DialogInterface dialog, int which) {
//                            showToast("取消");
//                            dialog.cancel();//对话框关闭。
//                        }
//                    }).create();
//            alert.show();
            final AlertDialog dlg = new AlertDialog.Builder(this).create();
            dlg.show();
            Window window = dlg.getWindow();
            window.setContentView(R.layout.dialog_alert_dialog);
            // 为确认按钮添加事件,执行退出应用操作
            TextView txtAlertContent=(TextView) window.findViewById(R.id.txtAlertContent);
            txtAlertContent.setText("确定兑奖吗？");
            Button ok = (Button) window.findViewById(R.id.btnConfirm);
            ok.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    new LoadDataTask().execute("1");
                    finish();
                }
            });
            // 关闭alert对话框架
            Button cancel = (Button) window.findViewById(R.id.btnCancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    showToast("取消");
                    dlg.cancel();//对话框关闭。
                }
            });



        }else if(toggleBtnAddressOther.isChecked()){
            Intent intent =new Intent(this,QuduijiangAddressListActivity.class);
            intent.putExtra(QuduijiangAddressListActivity.KEY_INTENT_DUIJIANG_ID,lotteryId);
            startActivity(intent);
        }
    }


    private class ImageLoading implements ImageLoadingListener {

        private ImageView imageView;

        public ImageLoading(ImageView view) {
            this.imageView = view;

        }

        @Override
        public void onLoadingStarted(String imageUri, View view) {
            imageView.setImageResource(R.drawable.quchoujiang_default_background_large);
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


    private void showToast(String content) {
        Toast.makeText(QuduijiangAddressActivity.this, content, 3).show();
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
