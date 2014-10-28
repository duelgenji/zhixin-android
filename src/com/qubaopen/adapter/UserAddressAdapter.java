package com.qubaopen.adapter;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qubaopen.R;
import com.qubaopen.activity.UserInfoAddressActivity;
import com.qubaopen.activity.UserInfoAddressModifyActivity;
import com.qubaopen.daos.UserAddressDao;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

/**
 * Created by duel on 14-3-26.
 */
public class UserAddressAdapter extends CursorAdapter {

	private Context context;
	// private Cursor cursor;

	private UserAddressDao userAddressDao;

	public UserAddressAdapter(Context context, Cursor cursor) {
		super(context, cursor, FLAG_REGISTER_CONTENT_OBSERVER);
		this.context = context;
		// this.cursor = cursor;
		this.userAddressDao = new UserAddressDao();
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.adapter_user_address_item,
				parent, false);
		return updatingContentInView(rowView, cursor);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		updatingContentInView(view, cursor);
	}

	@SuppressLint("ResourceAsColor")
	private View updatingContentInView(final View view, Cursor cursor) {

		TextView txtNameUserAddressItem = (TextView) view
				.findViewById(R.id.txtNameUserAddressItem);
		txtNameUserAddressItem.setText(cursor.getString(cursor
				.getColumnIndex("name")) + "");

		TextView txtPhoneUserAddressItem = (TextView) view
				.findViewById(R.id.txtPhoneUserAddressItem);
		txtPhoneUserAddressItem.setText(cursor.getString(cursor
				.getColumnIndex("phone")) + "");

		TextView txtAddressIsDefault = (TextView) view
				.findViewById(R.id.tvIsDefault);

		TextView txtAddressUserAddressItem = (TextView) view
				.findViewById(R.id.txtAddressUserAddressItem);

		ImageView imgIsDefault = (ImageView) view
				.findViewById(R.id.imgIsDefault);

		LinearLayout deleteLayout = (LinearLayout) view
				.findViewById(R.id.layout_address_delete);

		LinearLayout editLayout = (LinearLayout) view
				.findViewById(R.id.layout_address_edit);

		Integer Id = cursor.getInt(cursor.getColumnIndex("dzId"));

		String sfmc = cursor.getString(cursor.getColumnIndex("sfmc"));
		String csmc = cursor.getString(cursor.getColumnIndex("csmc"));
		String dqmc = cursor.getString(cursor.getColumnIndex("dqmc"));
		String address = cursor.getString(cursor.getColumnIndex("address"));

		boolean isDefault;
		Integer isDefaultNum;

		isDefaultNum = cursor.getInt(cursor.getColumnIndex("isDefault"));
		String detail = "";
		if (sfmc != null) {
			detail += sfmc;
			if (csmc != null) {
				detail += "," + csmc;
				if (dqmc != null) {
					detail += "," + dqmc;
				}
			}
		}
		if (address != null) {
			detail = "";
			detail += address;
			if (detail.length() > 18) {
				detail = detail.substring(0, 18) + "...";
			}
		}

		txtAddressUserAddressItem.setText(detail);

		if (isDefaultNum == 0) {
			isDefault = false;
			imgIsDefault.setImageResource(R.drawable.address_add_yes_pressed);
			txtAddressIsDefault.setTextColor(R.color.text_black);
		} else {
			isDefault = true;
			imgIsDefault.setImageResource(R.drawable.address_add_yes_default);
			txtAddressIsDefault.setTextColor(R.color.text_red);
		}

		imgIsDefault.setOnTouchListener(new IsDefaultTouchListener(Id,
				isDefault));
		deleteLayout.setOnTouchListener(new DeleteTouchListener(Id));
		editLayout.setOnTouchListener(new EditTouchListener(Id));
		return view;

	}

	private class IsDefaultTouchListener implements OnTouchListener {
		private int id;
		private boolean isDefault;

		public IsDefaultTouchListener(int id, boolean isDefault) {
			this.id = id;
			this.isDefault = isDefault;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				String defaultUrl = SettingValues.URL_PREFIX
						+ context.getString(R.string.URL_MODIFY_USER_ADDRESS);
				defaultUrl += "?id=" + id;
				JSONObject defaultParams = new JSONObject();
				try {
					defaultParams.put("id", id);
					if (isDefault) {
						defaultParams.put("defaultAddress", false);
					} else {
						defaultParams.put("defaultAddress", true);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				new LoadDataTask1().execute(2, defaultUrl, defaultParams,
						HttpClient.TYPE_POST_FORM);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {

			} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {

			}
			return false;
		}

	}

	private class DeleteTouchListener implements OnTouchListener {
		private int id;

		public DeleteTouchListener(int id) {
			this.id = id;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				final AlertDialog dlg = new AlertDialog.Builder(context)
						.create();
				dlg.show();
				Window window = dlg.getWindow();
				window.setContentView(R.layout.dialog_alert_dialog);
				// 为确认按钮添加事件,执行退出应用操作
				TextView txtAlertContent = (TextView) window
						.findViewById(R.id.txtAlertContent);
				txtAlertContent.setText("确认删除地址？");
				Button ok = (Button) window.findViewById(R.id.btnConfirm);
				ok.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						String deleteUrl = SettingValues.URL_PREFIX
								+ context
										.getString(R.string.URL_DELETE_USER_ADDRESS);
						deleteUrl += "?id=" + id;
						Log.i("delete", deleteUrl);
						new LoadDataTask1().execute(1, deleteUrl, id,
								HttpClient.TYPE_DELETE);

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
			} else if (event.getAction() == MotionEvent.ACTION_UP) {

			} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {

			}
			return false;
		}
	}

	private class EditTouchListener implements OnTouchListener {
		private int id;

		public EditTouchListener(int id) {
			this.id = id;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				Intent intent = new Intent(context,
						UserInfoAddressModifyActivity.class);
				intent.putExtra(
						UserInfoAddressModifyActivity.INTENT_ADDRESS_ID, id);
				context.startActivity(intent);

			} else if (event.getAction() == MotionEvent.ACTION_UP) {

			} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {

			}
			return false;
		}

	}

	private class LoadDataTask1 extends AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result = null;
			Integer syncType = (Integer) params[0];
			try {
				switch (syncType) {
				case 1:
					result = HttpClient.requestSync(params[1].toString(), null,
							(Integer) params[3]);
					Log.i("delete", result + "");
					result.put("syncType", syncType);
					result.put("dzId", params[2]);
					break;
				case 2:
					result = HttpClient.requestSync(params[1].toString(),
							params[2], (Integer) params[3]);
					
					Log.i("modify", result + "");
					result.put("syncType", syncType);
					result.put("params", params[2]);
					break;
				default:
					break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			try {
				Integer syncType = result.getInt("syncType");
				switch (syncType) {
				case 1:
					if (result != null
							&& result.getString("success").equals("1")) {
						Toast.makeText(context, "删除地址成功！", Toast.LENGTH_SHORT)
								.show();
						userAddressDao.deleteAddressById((Integer) result.get("dzId"));
						UserInfoAddressActivity.userInfoAddressActivity
								.reSetAddress();
					} else {
						Toast.makeText(context, "删除地址失败！", Toast.LENGTH_SHORT)
								.show();
					}

					break;
				case 2:
					if (result != null
							&& result.getString("success").equals("1")) {
						Toast.makeText(context, "修改地址成功！", Toast.LENGTH_SHORT)
								.show();
						userAddressDao
								.updateSingleUserAddress((JSONObject) result.getJSONObject("params"));
						UserInfoAddressActivity.userInfoAddressActivity
								.reSetAddress();
					} else {
						Toast.makeText(context, "修改地址失败！", Toast.LENGTH_SHORT)
								.show();
					}
					break;
				default:
					break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
}
