package com.qubaopen.adapter;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.qubaopen.R;
import com.qubaopen.activity.QuduijiangAddressActivity;
import com.qubaopen.datasynservice.QuduijiangConfirmService;
import com.qubaopen.settings.ErrHashMap;
import com.qubaopen.settings.SettingValues;

public class QuduijiangListAdapter extends CursorAdapter {

    private Context context;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    private QuduijiangConfirmService service;

    public QuduijiangListAdapter(Context context, Cursor c) {
		super(context, c, FLAG_REGISTER_CONTENT_OBSERVER);
        this.context=context;
        service=new QuduijiangConfirmService(context);
        imageLoader = ImageLoader.getInstance();
        imageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true).build();
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		/* TextView pointsConsumeTextView = (TextView) view
				 .findViewById(R.id.pointsConsume);
		
		 pointsConsumeTextView.setText(cursor.getString(cursor
		 .getColumnIndex("pointsConsume")));*/

        updatingContentInView(view,cursor);
	}

	@Override
	public View newView(Context context, Cursor cursor,
			ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.adapter_duijiang_gift_item,
				parent, false);

        updatingContentInView(rowView,cursor);

        return rowView;
	}
    private View updatingContentInView(View rowView, final Cursor cursor) {


        Boolean hasRight=false;
        TextView costValue=(TextView) rowView.findViewById(R.id.costValue);
        TextView costType=(TextView) rowView.findViewById(R.id.costType);
        final ImageView lotteryImage = (ImageView) rowView
                .findViewById(R.id.rewardImage);
        ImageView flagImage=(ImageView) rowView.findViewById(R.id.costImage);
        Button confirmBtn = (Button) rowView.findViewById(R.id.btnDuijiang);
        FrameLayout layoutLeftFlag=(FrameLayout) rowView.findViewById(R.id.layoutLeftFlag);
        layoutLeftFlag.setVisibility(View.GONE);

        if("0".equals(cursor.getString(cursor.getColumnIndex("type")))){
            confirmBtn.setText(R.string.duijiang_choujiang_btn);
        }else{
            confirmBtn.setText(R.string.duijiang_duijinag_btn);
        }



        if (cursor.getInt(cursor.getColumnIndex("creditConsume"))>0) {
            costValue.setText(cursor.getString(cursor
                    .getColumnIndex("creditConsume")));
            costType.setText(context.getString(R.string.duijiang_credit));
            flagImage.setImageResource(R.drawable.flag_green);
            hasRight=true;
        }

        if (cursor.getInt(cursor.getColumnIndex("coinConsume"))>0) {
            if(!hasRight){
                costValue.setText(cursor.getString(cursor
                        .getColumnIndex("coinConsume")));
                costType.setText(context.getString(R.string.duijiang_coin));
                flagImage.setImageResource(R.drawable.flag_red);
            }else{
                layoutLeftFlag.setVisibility(View.VISIBLE);
                TextView costValueSub=(TextView) rowView.findViewById(R.id.costValueSub);
                TextView costTypeSub=(TextView) rowView.findViewById(R.id.costTypeSub);
                ImageView flagImageSub=(ImageView) rowView.findViewById(R.id.costImageSub);
                costValueSub.setText(cursor.getString(cursor
                        .getColumnIndex("coinConsume")));
                costTypeSub.setText(context.getString(R.string.duijiang_coin));
                flagImageSub.setImageResource(R.drawable.flag_red);
            }

        }

        final String lotteryId=cursor.getInt(cursor.getColumnIndex("lotteryId"))+"";
        final String content = cursor.getString(cursor.getColumnIndex("content"));
        final String title = cursor.getString(cursor.getColumnIndex("title"));
        final int coin=cursor.getInt(cursor.getColumnIndex("coinConsume"));
        final int credit=cursor.getInt(cursor.getColumnIndex("creditConsume"));
        final String picUrl=cursor.getString(cursor.getColumnIndex("picUrl"));
        String imageUri = cursor.getString(cursor.getColumnIndex("picUrl"));
        if (StringUtils.isNotEmpty(imageUri)) {
            imageUri = SettingValues.URL_PREFIX + imageUri;
            imageLoader.displayImage(imageUri, lotteryImage,
                    imageOptions, new ImageLoading(
                    lotteryImage), null);

            final String finalImageUri = imageUri;
            lotteryImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
   //                 Intent intent = new Intent(context, QuChoujiangDetailActivity.class);
  //                  intent.putExtra(QuChoujiangDetailActivity.INTENT_IMAGE_URI, finalImageUri);
  //                  intent.putExtra(QuChoujiangDetailActivity.INTENT_CONTENT, content);
  //                  intent.putExtra(QuChoujiangDetailActivity.INTENT_TITLE, title);
  //                  intent.putExtra(QuChoujiangDetailActivity.INTENT_COIN, coin);
 //                   intent.putExtra(QuChoujiangDetailActivity.INTENT_CREDIT, credit);
 //                   context.startActivity(intent);
                }});

        } else {
           lotteryImage.setImageResource(R.drawable.quchoujiang_default_background);
        }
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("0".equals(cursor.getString(cursor.getColumnIndex("type")))){
                    final AlertDialog dlg = new AlertDialog.Builder(context).create();
                    dlg.show();
                    Window window = dlg.getWindow();
                    window.setContentView(R.layout.dialog_alert_dialog);
                    // 为确认按钮添加事件,执行退出应用操作
                    TextView txtAlertContent=(TextView) window.findViewById(R.id.txtAlertContent);
                    txtAlertContent.setText("确认抽奖吗？");
                    Button ok = (Button) window.findViewById(R.id.btnConfirm);
                    ok.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            new LoadDataTask().execute(lotteryId,title,credit,coin,picUrl);
                            dlg.cancel();
                        }
                    });
                    // 关闭alert对话框架
                    Button cancel = (Button) window.findViewById(R.id.btnCancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            dlg.cancel();//对话框关闭。
                        }
                    });

                }else if("1".equals(cursor.getString(cursor.getColumnIndex("type")))){

                    SharedPreferences sharedPref = context.getSharedPreferences(
                            SettingValues.FILE_NAME_SETTINGS, Context.MODE_PRIVATE);
                    Boolean isAddressSaved = sharedPref.getBoolean(
                            SettingValues.KEY_CURRENT_ADDRESS_SAVED, false);
                    if(!isAddressSaved){
                        Toast.makeText(context, "正在加载数据中", 3).show();
                        return;
                    }

                    Intent intent;
                    intent = new Intent(context, QuduijiangAddressActivity.class);

                    intent.putExtra(QuduijiangAddressActivity.KEY_INTENT_DUIJIANG_ID,lotteryId);
                    intent.putExtra(QuduijiangAddressActivity.KEY_INTENT_DUIJIANG_TITLE,title);
                    intent.putExtra(QuduijiangAddressActivity.KEY_INTENT_DUIJIANG_COIN,coin);
                    intent.putExtra(QuduijiangAddressActivity.KEY_INTENT_DUIJIANG_CREDIT,credit);
                    intent.putExtra(QuduijiangAddressActivity.KEY_INTENT_DUIJIANG_URL,picUrl);
                    context.startActivity(intent);
                }
            }
        });

        return rowView;
    }


    private class ImageLoading implements ImageLoadingListener {

        private ImageView imageView;

        public ImageLoading(ImageView view) {
            this.imageView = view;

        }

        @Override
        public void onLoadingStarted(String imageUri, View view) {
            imageView.setImageResource(R.drawable.quchoujiang_default_background);
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


    private class LoadDataTask extends AsyncTask<Object, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Object... params) {
            JSONObject result=new JSONObject();
            try {
                result= service.sendRequest(params[0].toString(), params[0].toString());
                result.put("title",params[1].toString());
                result.put("creditConsume",params[2].toString());
                result.put("coinConsume",params[3].toString());
                result.put("picUrl",params[4].toString());
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
                if (jbo.has("success")
                        && jbo.getString("success").equals("1")) {
 //                   Intent intent = new Intent(context, QuchoujiangResultActivity.class);
 //                   intent.putExtra(QuchoujiangResultActivity.KEY_INTENT_CHOUJIANG_ID,jbo.getString("lotteryNumber"));
 //                   intent.putExtra(QuchoujiangResultActivity.KEY_INTENT_CHOUJIANG_TITLE,jbo.getString("title"));
 //                   intent.putExtra(QuchoujiangResultActivity.KEY_INTENT_CHOUJIANG_CREDIT,jbo.getString("creditConsume"));
 //                   intent.putExtra(QuchoujiangResultActivity.KEY_INTENT_CHOUJIANG_COIN,jbo.getString("coinConsume"));
 //                   intent.putExtra(QuchoujiangResultActivity.KEY_INTENT_CHOUJIANG_URL,jbo.getString("picUrl"));
 //                   context.startActivity(intent);
                } else if (jbo.getString("success").equals("0")) {
                    String content = ErrHashMap.getErrMessage(jbo
                            .getString("message"));
                    content = content == null ? context
                            .getString(R.string.toast_unknown) : content;
                    Toast.makeText(context, content, 5).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    private void showToast(String content) {
        Toast.makeText(context, content, 3).show();
    }


}
