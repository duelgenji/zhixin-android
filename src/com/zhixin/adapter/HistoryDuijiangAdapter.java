package com.zhixin.adapter;

import java.text.ParseException;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.support.v4.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.R;
import com.zhixin.activity.DuijiangHistoryActivity;
import com.zhixin.activity.QuHistoryCheckActivity;
import com.zhixin.activity.QuduijiangAddressListActivity;
import com.zhixin.daos.HistoryDuijiangDao;
import com.zhixin.datasynservice.QuCjDjActionService;
import com.zhixin.settings.ErrHashMap;

public class HistoryDuijiangAdapter extends CursorAdapter {

	private final static int BOTTOM_FRAMELAYOUT = 1;
	private final static int TOP_FRAMELAYOUT = 2;

	private HistoryDuijiangAdapter _this;

	private Context context;

	private HashMap<Integer, Integer> picCate;

	private int padding;

	private QuCjDjActionService quCjDjActionService;

	private HistoryDuijiangDao historyDuijiangDao;

	private DuijiangHistoryActivity ownerActivity;

	private class HistoryDuijiangTouchListener implements View.OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.setBackgroundColor(context.getResources().getColor(
						R.color.general_up_background));

			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.setBackgroundColor(context.getResources().getColor(
						R.color.general_activity_background));

			} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
				v.setBackgroundColor(context.getResources().getColor(
						R.color.general_activity_background));
			}

			return true;
		}

	}

	public HistoryDuijiangAdapter(Context context, Cursor c) {
		super(context, c, FLAG_REGISTER_CONTENT_OBSERVER);
		_this = this;
		this.context = context;
		quCjDjActionService = new QuCjDjActionService(context);
		historyDuijiangDao = new HistoryDuijiangDao();
		picCate = new HashMap<Integer, Integer>();
		picCate.put(0, R.drawable.choujiangzhong_pic);
		picCate.put(1, R.drawable.weizhongjiang_pic);
		picCate.put(3, R.drawable.fahuozhong_pic);
		picCate.put(5, R.drawable.yiqueren_pic);
		picCate.put(6, R.drawable.chulizhong_pic);

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.adapter_history_duijiang_item,
				parent, false);
		return updatingContentInView(rowView, cursor);
	}

	@Override
	public void bindView(View rowView, Context context, Cursor cursor) {
		updatingContentInView(rowView, cursor);
	}

	private View updatingContentInView(final View rowView, Cursor cursor) {

		padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				5, context.getResources().getDisplayMetrics());

		TextView duijiangNo = (TextView) rowView.findViewById(R.id.duijiangNo);
		TextView title = (TextView) rowView.findViewById(R.id.title);
		TextView duijiangDate = (TextView) rowView
				.findViewById(R.id.duijiangDate);
		TextView duijiangJifen = (TextView) rowView
				.findViewById(R.id.duijiangJifen);

		RelativeLayout querenComp = (RelativeLayout) rowView
				.findViewById(R.id.querenComp);
		querenComp.removeAllViews();

		int duijiangId = cursor.getInt(cursor.getColumnIndex("duijiangId"));

		duijiangNo.setText(String.valueOf(duijiangId));

		int choujiangOrDuijiangType = cursor.getInt(cursor
				.getColumnIndex("type"));
		title.setText(cursor.getString(cursor.getColumnIndex("title")));

		duijiangDate.setText(cursor.getString(cursor.getColumnIndex("date"))
				.substring(0, 10));

		int coin = cursor.getInt(cursor.getColumnIndex("coin"));
		int credit = cursor.getInt(cursor.getColumnIndex("credit"));

		if (coin > 0 && credit == 0) {
			duijiangJifen.setTextColor(context.getResources().getColor(
					R.color.text_orange));
			duijiangJifen.setText(String.valueOf(coin));
		} else {
			duijiangJifen.setTextColor(context.getResources().getColor(
					R.color.text_black));
			duijiangJifen.setText(String.valueOf(credit));
		}
		int statusCode = cursor.getInt(cursor.getColumnIndex("status"));
		switch (statusCode) {
		case 0:
		case 1:
		case 3:
		case 5:
		case 6:
			ImageView imageView = new ImageView(context);
			int imageMargin = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources()
							.getDisplayMetrics());
			RelativeLayout.LayoutParams imageViewLayout = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);

			imageViewLayout.setMargins(0, imageMargin, 0, imageMargin);

			imageViewLayout.addRule(RelativeLayout.CENTER_IN_PARENT);

			imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			imageView.setImageResource(picCate.get(statusCode));

			imageView.setLayoutParams(imageViewLayout);

			String imageViewParamsOutput = "";
			imageViewParamsOutput = imageViewLayout
					.debug(imageViewParamsOutput);

			querenComp.addView(imageView);
			break;
		case 2:
		case 7:
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			FrameLayout fm = (FrameLayout) inflater.inflate(
					R.layout.customui_history_duijiang_apapter_lingjiang_item,
					querenComp, false);
			TextView textViewInComp = (TextView) fm
					.findViewById(R.id.lingjiangText);
			ImageButton btn = (ImageButton) fm.findViewById(R.id.lingjiangBtn);

			if (statusCode == 2) {
				textViewInComp.setText(context
						.getString(R.string.duijiang_history_lingjiang));
				btn.setOnClickListener(new LingjiangListener(duijiangId,
						choujiangOrDuijiangType));

			} else {
				textViewInComp.setText(context
						.getString(R.string.duijiang_history_chakanjiangping));
				btn.setOnClickListener(new ChaKanJiangPingListener(duijiangId,
						choujiangOrDuijiangType));

			}

			querenComp.addView(fm);

			break;
		case 4:
			querenComp.addView(getFrameLayout(TOP_FRAMELAYOUT, querenComp,
					duijiangId, choujiangOrDuijiangType));
			querenComp.addView(getFrameLayout(BOTTOM_FRAMELAYOUT, querenComp,
					duijiangId, choujiangOrDuijiangType));
			break;
		default:
			break;

		}

		return rowView;

	}

	private FrameLayout getFrameLayout(int cate, ViewGroup parent,
			int duijiangId, int choujiangOrDuijiangType) {
		FrameLayout fm = null;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ImageButton keyButton;
		switch (cate) {
		case BOTTOM_FRAMELAYOUT:

			fm = (FrameLayout) inflater
					.inflate(
							R.layout.customui_history_duijiang_adapter_bottom_cancel_layout,
							parent, false);

			keyButton = (ImageButton) fm.findViewById(R.id.keyButton);

			keyButton.setOnClickListener(new LingjiangBtnCancelListener(
					duijiangId, choujiangOrDuijiangType));

			break;
		case TOP_FRAMELAYOUT:
			fm = (FrameLayout) inflater
					.inflate(
							R.layout.customui_history_duijiang_adapter_top_confirm_layout,
							parent, false);

			keyButton = (ImageButton) fm.findViewById(R.id.keyButton);

			keyButton.setOnClickListener(new LingjiangBtnConfirmListener(
					duijiangId, choujiangOrDuijiangType));
			break;
		}

		return fm;

	}

	private class BtnListener implements View.OnClickListener {

		public int duijiangId;

		// choujiang is 0 while duijiang is 1
		public int choujiangOrDuijiang;

		public BtnListener(int duijiangId, int choujiangOrDuijiang) {
			this.duijiangId = duijiangId;
			this.choujiangOrDuijiang = choujiangOrDuijiang;

		}

		@Override
		public void onClick(View v) {

		}

	}

	private class LingjiangBtnConfirmListener extends BtnListener {

		public LingjiangBtnConfirmListener(int duijiangId,
				int choujiangOrDuijiang) {
			super(duijiangId, choujiangOrDuijiang);
		}

		@Override
		public void onClick(View v) {
			final AlertDialog dlg = new AlertDialog.Builder(context).create();
			dlg.show();
			Window window = dlg.getWindow();
			window.setContentView(R.layout.dialog_alert_dialog);
			// 为确认按钮添加事件,执行退出应用操作
			TextView txtAlertContent = (TextView) window
					.findViewById(R.id.txtAlertContent);
			txtAlertContent.setText(R.string.lingjiang_confirm_tips);
			Button ok = (Button) window.findViewById(R.id.btnConfirm);
			ok.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					new LoadDataTask().execute(0, choujiangOrDuijiang,
							duijiangId);
					dlg.cancel();
				}
			});
			// 关闭alert对话框架
			Button cancel = (Button) window.findViewById(R.id.btnCancel);
			cancel.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					dlg.cancel();// 对话框关闭。
				}
			});
		}

	}

	private class LingjiangBtnCancelListener extends BtnListener {

		public LingjiangBtnCancelListener(int duijiangId,
				int choujiangOrDuijiang) {
			super(duijiangId, choujiangOrDuijiang);
		}

		@Override
		public void onClick(View v) {
			final AlertDialog dlg = new AlertDialog.Builder(context).create();
			dlg.show();
			Window window = dlg.getWindow();
			window.setContentView(R.layout.dialog_alert_dialog);
			// 为确认按钮添加事件,执行退出应用操作
			TextView txtAlertContent = (TextView) window
					.findViewById(R.id.txtAlertContent);
			txtAlertContent.setText(R.string.lingjiang_deny_tips);
			Button ok = (Button) window.findViewById(R.id.btnConfirm);
			ok.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					new LoadDataTask().execute(1, choujiangOrDuijiang,
							duijiangId);
					dlg.cancel();
				}
			});
			// 关闭alert对话框架
			Button cancel = (Button) window.findViewById(R.id.btnCancel);
			cancel.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					dlg.cancel();// 对话框关闭。
				}
			});
		}

	}

	private class ChaKanJiangPingListener extends BtnListener {

		public ChaKanJiangPingListener(int duijiangId, int choujiangOrDuijiang) {
			super(duijiangId, choujiangOrDuijiang);
		}

		@Override
		public void onClick(View v) {

            Intent intent = new Intent(context,
                    QuHistoryCheckActivity.class);
            intent.putExtra(
                    QuHistoryCheckActivity.KEY_INTENT_LOTTERY_ID,
                    duijiangId);
            intent.putExtra(
                    QuHistoryCheckActivity.KEY_INTENT_LOTTERY_TYPE,
                    choujiangOrDuijiang);
            context.startActivity(intent);
		}

	}

	private class LingjiangListener extends BtnListener {

		public LingjiangListener(int duijiangId, int choujiangOrDuijiang) {
			super(duijiangId, choujiangOrDuijiang);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context,
					QuduijiangAddressListActivity.class);
			intent.putExtra(
					QuduijiangAddressListActivity.KEY_INTENT_DUIJIANG_ID,
					duijiangId);
			context.startActivity(intent);
		}

	}

	private class LoadDataTask extends AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result = new JSONObject();
			try {
				Integer type = Integer.parseInt(params[0].toString());
				// 0cj 1dj
				Integer lotteryType = Integer.parseInt(params[1].toString());
				Integer lotteryId = Integer.parseInt(params[2].toString());
				if (type.equals(0)) {
					result = quCjDjActionService.confirmLottery(lotteryType,
							lotteryId);
				} else if (type.equals(1)) {
					result = quCjDjActionService.denyLottery(lotteryType,
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
					Integer type = jbo.getInt("type");
					Integer lotteryId = jbo.getInt("lotteryId");
					if (type == 0) {
						historyDuijiangDao.changeStatus(lotteryId, 5);
					} else if (type == 1) {
						historyDuijiangDao.changeStatus(lotteryId, 6);
					}
					showToast("操作成功");

					ownerActivity.getSupportLoaderManager().restartLoader(0,
							null, ownerActivity);

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

	public void setHistoryAcitivty(DuijiangHistoryActivity activity) {
		this.ownerActivity = activity;

	}

	private void showToast(String content) {
		Toast.makeText(context, content, 3).show();
	}

}
