package com.zhixin.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.database.DbManager;
import com.zhixin.datasynservice.MainMenuService;
import com.zhixin.domain.UserSettings;
import com.zhixin.provider.InternalStorageContentProvider;
import com.zhixin.service.CropImageIntentService;
import com.zhixin.settings.CurrentUserHelper;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;
import com.zhixin.utils.RecToCircleTask;

import eu.janmuller.android.simplecropimage.CropImage;

public class MeFragment extends Fragment implements View.OnClickListener{
	private Activity mainActivity;
	/***/
//	private ImageLoader imageLoader;
	/***/
//	private DisplayImageOptions imageOptions;
	private ImageView shezhi;
	/** 标题*/
	private TextView txtPageTitle;
	
	private ImageView headImageViewPlaceHolder;
	
	private Handler closePicImageDialogHander;
	
	private MainMenuService service;
	
	private LinearLayout layoutMyprofile,layoutDuijiang,layoutSuggestion,layoutAbout,layoutShareAppComp;
	
	private View scoreTheApp;
	
	private Intent intent;
	
	private TextView nickNameTextView;
	
	private String nickName;
	
	private ImageButton editNickNameBtn;
	
	static final int PICK_PIC_FROM_CAMERA_ACTION = 10;
	static final int PICK_PIC_FORM_GALLERY_ACTION = 20;
	static final int CROP_IMAGE_ACTION = 30;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mainActivity = activity;
	}
	//修改昵称的部分
/**	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				nickNameTextView.setText(nickName);
				break;
			default:
				break;
			}
		};
	};
	*/
	private class NicknameIconClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			editNickNameBtn.setEnabled(false);
			intent = new Intent(mainActivity,
					ModifyNicknameActivity.class);
			nickName = nickNameTextView.getText().toString();
			if (nickName != null && !nickName.equals("")) {
				intent.putExtra(ModifyNicknameActivity.INTENT_NICKNAME,
						nickName);
			}
			startActivity(intent);
			editNickNameBtn.setEnabled(true);
		}

	}
	//初始化	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.me_main, container,
				false);
		txtPageTitle = (TextView) view.findViewById(R.id.title_of_the_page);
		txtPageTitle.setText(this.getString(R.string.title_me));
		
		nickNameTextView = (TextView) view.findViewById(R.id.nickNameTextView);
		nickNameTextView.setOnClickListener(this);
		editNickNameBtn = (ImageButton) view.findViewById(R.id.editNickNameBtn);
		editNickNameBtn.setOnClickListener(new NicknameIconClickListener());
		
		layoutMyprofile = (LinearLayout) view.findViewById(R.id.layoutMyprofile);
		layoutMyprofile.setOnClickListener(this);
		
		layoutDuijiang = (LinearLayout) view.findViewById(R.id.layoutDuijiang);
		layoutDuijiang.setOnClickListener(this);
		
//		layoutDafen = (LinearLayout) view.findViewById(R.id.layoutDafen);
//		layoutDafen.setOnClickListener(this);
		layoutShareAppComp = (LinearLayout) view.findViewById(R.id.layoutShareAppComp);
		layoutShareAppComp.setOnClickListener(this);
		
		layoutSuggestion = (LinearLayout) view.findViewById(R.id.layoutSuggestion);
		layoutSuggestion.setOnClickListener(this);
		
		layoutAbout = (LinearLayout) view.findViewById(R.id.layoutAbout);
		layoutAbout.setOnClickListener(this);
		
		shezhi = (ImageView) view.findViewById(R.id.shezhi);
		shezhi.setOnClickListener(this);
		
		scoreTheApp = view.findViewById(R.id.scoreTheApp);
		scoreTheApp.setOnClickListener(this);
		
		headImageViewPlaceHolder = (ImageView) view
				.findViewById(R.id.headIconPHQSJ);
		headImageViewPlaceHolder
				.setOnClickListener(new ClickImageToChangeHeadIcon());
		return view;
		
	}
	//设置的连接后台
	private class LoadDataTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			if (params[0] != null && params[0].equals("userSetting")) {
				String requestUrl = SettingValues.URL_PREFIX
						+ getString(R.string.URL_GET_OPTION);
				// JSONObject jsonParams = new JSONObject();
				try {
					JSONObject result = HttpClient
							.requestSync(requestUrl, null);
					if (result != null
							&& result.getString("success").equals("1")) {

						UserSettings us = DbManager.getDatabase().findById(1,
								UserSettings.class);

						us.setPublicAnswersToFriend(result.getBoolean("publicAnswersToFriend"));
						DbManager.getDatabase().update(us);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} 
			else if (params[0] != null && params[0].equals("newVersion")) {
				service = new MainMenuService(mainActivity);
				try {
					return service.newVersion();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				}
			}

			return params[0];
		}
	}
	@Override
	public void onClick(View v) {
		v.setEnabled(false);
		Intent intent;
		switch (v.getId()) {
		case R.id.shezhi:
			intent = new Intent(mainActivity,MoreSetting.class);
			startActivity(intent);
			shezhi.setEnabled(true);
			break;
		case R.id.layoutMyprofile:
			intent = new Intent(mainActivity, UserInfoActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		case R.id.layoutDuijiang:
			intent = new Intent(mainActivity, DuijiangActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		case R.id.layoutAbout:
			intent = new Intent(mainActivity, MoreAboutusActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
//			打分，这里的Uri还是以前的，需要改
		case R.id.scoreTheApp:

			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://details?id=com.qubaopen"));

			if (intent.resolveActivity(mainActivity.getPackageManager()) != null) {
				startActivity(intent);
			} else {

				Toast.makeText(mainActivity, "您没有安装任何市场，无法打分",
						Toast.LENGTH_SHORT).show();
			}
			v.setEnabled(true);
			break;
		case R.id.layoutSuggestion:
			intent = new Intent(mainActivity, MoreSuggestionActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		case R.id.layoutShareAppComp:
			intent = new Intent(mainActivity, ShareActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		default:
			break;

		}
	}
	
	public static final String TEMP_PHOTO_FILE_PATH = Environment
			.getExternalStorageDirectory()
			+ SettingValues.PATH_USER_TX_PREFIX
			+ "temp.jpg";

	private class RecToCircleTaskInQushejiao extends AsyncTask<String, Void, Bitmap> {
		protected Bitmap doInBackground(String... urls) {
			Bitmap bitmap = BitmapFactory.decodeFile(urls[0]);
			return RecToCircleTask.transferToCircle(bitmap);
		}

		protected void onPostExecute(Bitmap result) {
			if (isAdded()) {
				CurrentUserHelper.saveBitmap(result);
				headImageViewPlaceHolder
						.setImageResource(R.drawable.head_white_ring_background);
				headImageViewPlaceHolder.setImageBitmap(result);

			}

		}

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		switch (requestCode) {
		case PICK_PIC_FROM_CAMERA_ACTION:
			if (resultCode == Activity.RESULT_OK) {
				startCropImage();
			}
			break;
		case PICK_PIC_FORM_GALLERY_ACTION:
			if (resultCode == Activity.RESULT_OK) {
//				if (!progressDialog.isShowing()) {
//					progressDialog.show();
//				}
				new Thread(new Runnable() {
					@Override
					public void run() {
						InputStream inputStream;
						try {
							inputStream = mainActivity.getContentResolver()
									.openInputStream(data.getData());
							FileOutputStream fileOutputStream = new FileOutputStream(
									new File(TEMP_PHOTO_FILE_PATH));
							copyStream(inputStream, fileOutputStream);
							fileOutputStream.close();
							inputStream.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						mainActivity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
//								if (progressDialog.isShowing()) {
//									progressDialog.dismiss();
//								}
								startCropImage();
							}
						});
					}
				}).start();
			}
			break;

		case CROP_IMAGE_ACTION:
			if (resultCode == Activity.RESULT_OK) {

				Intent intent = new Intent(mainActivity,
						CropImageIntentService.class);
				intent.putExtra(CropImage.IMAGE_PATH,
						data.getStringExtra(CropImage.IMAGE_PATH));
				mainActivity.startService(intent);
				if (closePicImageDialogHander != null) {
					closePicImageDialogHander.sendEmptyMessage(0);
				}
			}
			break;
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(this);
//		long userId = CurrentUserHelper.getCurrentUserId();
		 String requestUrl = SettingValues.URL_PREFIX
					+ getString(R.string.URL_USER_INFO_ADD);
//	        requestUrl+="/"+userId;
		new LoadDataTask1().execute(1,requestUrl,null,HttpClient.TYPE_GET);
	}
	private class LoadDataTask1 extends AsyncTask<Object, Void, JSONObject>{

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result=null;
			Integer syncType=(Integer)params[0];
			try {
				switch(syncType){
				case 1:
					//null。。。。传参方式是get
					//(Integer)params[3]对应上面的HttpClient.TYPE_POST
					result = HttpClient.requestSync(params[1].toString(), null,(Integer)params[3]);
					result.put("syncType", syncType);
					break;
				
				default :
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
				Integer syncType=result.getInt("syncType");
				switch(syncType){
				case 1:
					if (result != null && result.getInt("success") == 1) {
		                //。。。。。。。。。
						Toast.makeText(mainActivity, "获取个人资料成功！", Toast.LENGTH_SHORT).show();
						nickName = result.getString("nickName");
	//					Message msg = Message.obtain();
	//					msg.what = 0;
	//					handler.sendMessage(msg);
						nickNameTextView.setText(nickName);
					}else {
						Toast.makeText(mainActivity, "获取数据失败！", Toast.LENGTH_SHORT).show();
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
	private void copyStream(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}
	
	private void startCropImage() {
		Intent intent = new Intent(mainActivity, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, TEMP_PHOTO_FILE_PATH);
		intent.putExtra(CropImage.SCALE, true);
		intent.putExtra(CropImage.ASPECT_X, 1);
		intent.putExtra(CropImage.ASPECT_Y, 1);
		startActivityForResult(intent, CROP_IMAGE_ACTION);
	}
	
	public Handler getClosePicImageDialogHander() {
		return closePicImageDialogHander;
	}
	
	private class ClickImageToChangeHeadIcon implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			File file = new File(Environment.getExternalStorageDirectory()
					+ SettingValues.PATH_USER_TX_PREFIX);
			if (!file.exists()) {
				file.mkdirs();
			}
			final Dialog dialog = new Dialog(mainActivity,
					android.R.style.Theme_Translucent_NoTitleBar);
			dialog.setContentView(R.layout.dialog_pick_head_image);

			Window window = dialog.getWindow();
			WindowManager.LayoutParams lp = window.getAttributes();
			lp.dimAmount = 0.7f;
			dialog.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_DIM_BEHIND);
			window.setAttributes(lp);
			Button cameraBtn = (Button) dialog
					.findViewById(R.id.dialogCameraBtn);
			Button galleryBtn = (Button) dialog
					.findViewById(R.id.dialogGalleryBtn);
			Button cancelBtn = (Button) dialog
					.findViewById(R.id.dialogCancelBtn);

			closePicImageDialogHander = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					dialog.dismiss();
				}
			};
//取消按钮
			cancelBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();

				}
			});
//相机按钮			
			cameraBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					try {
						Uri mImageCaptureUri = null;
						String state = Environment.getExternalStorageState();

						if (Environment.MEDIA_MOUNTED.equals(state)) {
							mImageCaptureUri = Uri.fromFile(new File(
									TEMP_PHOTO_FILE_PATH));
						} else {
							mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
						}
						intent.putExtra(
								android.provider.MediaStore.EXTRA_OUTPUT,
								mImageCaptureUri);

						startActivityForResult(intent,
								PICK_PIC_FROM_CAMERA_ACTION);
					} catch (ActivityNotFoundException e) {

						e.printStackTrace();
					}

				}
			});
//相册按钮
			galleryBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
					photoPickerIntent.setType("image/*");

					startActivityForResult(photoPickerIntent,
							PICK_PIC_FORM_GALLERY_ACTION);

				}
			});

			dialog.show();

		}

	}
	
}
