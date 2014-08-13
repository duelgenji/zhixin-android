package com.zhixin.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.commons.io.FilenameUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhixin.R;
import com.zhixin.datasynservice.UserService;
import com.zhixin.datasynservice.ZhibiService;
import com.zhixin.dialog.InstructionDialog;
import com.zhixin.dialog.QubaopenProgressDialog;
import com.zhixin.domain.UserInfo;
import com.zhixin.provider.InternalStorageContentProvider;
import com.zhixin.service.CropImageIntentService;
import com.zhixin.settings.CurrentUserHelper;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.RecToCircleTask;
import com.zhixin.utils.SqlCursorLoader;

import eu.janmuller.android.simplecropimage.CropImage;

/**
 * @author Administrator
 *趣社交
 */
public class ZhibiFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private FragmentActivity mainActivity;

	private ImageView headImageView;
	private ImageView headImageViewPlaceHolder;

	private ZhibiService zhibiService;
	private FinalBitmap fb;

	private TextView textViewForCreditAvailable;
	private TextView titleOfThePage;
	private TextView nickNameTextView;
	private TextView coinNumberText;

	private ImageButton editNickNameBtn;

	private LinearLayout layoutCoinQSJ;

	private Handler closePicImageDialogHander;

	private Looper changePhotoLooper;

	private BroadcastReceiver mReceiver;

	private UserService userService;

	private QubaopenProgressDialog progressDialog;

	static final int PICK_PIC_FROM_CAMERA_ACTION = 10;
	static final int PICK_PIC_FORM_GALLERY_ACTION = 20;
	static final int CROP_IMAGE_ACTION = 30;

	private ImageLoader imageLoader;
	private DisplayImageOptions rankListImageOptions;

	private RefreshDataTask refreshDataTask;

	// public static final String PHTOT_FILE_SAVE_PATH = MyApplication
	// .getAppContext().a
	// + SettingValues.PATH_USER_TX_PREFIX;

	public static final String TEMP_PHOTO_FILE_PATH = Environment
			.getExternalStorageDirectory()
			+ SettingValues.PATH_USER_TX_PREFIX
			+ "temp.jpg";

	private class RecToCircleTaskInQushejiao extends
			AsyncTask<String, Void, Bitmap> {
		protected Bitmap doInBackground(String... urls) {
			Bitmap bitmap = BitmapFactory.decodeFile(urls[0]);
			return RecToCircleTask.transferToCircle(bitmap);
		}

		protected void onPostExecute(Bitmap result) {
			if (isAdded()) {
				CurrentUserHelper.saveBitmap(result);
				headImageViewPlaceHolder
						.setImageResource(R.drawable.head_white_ring_background);
				headImageView.setImageBitmap(result);

			}

		}

	}

	private class RefreshDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			return zhibiService.saveQushejiaoInfo();
		}

		@Override
		protected void onPostExecute(Void result) {
			if (isAdded()) {
				getLoaderManager().restartLoader(0, null,
						ZhibiFragment.this);
			}
		}
	}

	private class NicknameIconClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			editNickNameBtn.setEnabled(false);
			Intent intent = new Intent(mainActivity,
				ModifyNicknameActivity.class);
			String nickName = nickNameTextView.getText().toString();
			if (nickName != null && !nickName.equals("")) {
				intent.putExtra(ModifyNicknameActivity.INTENT_NICKNAME,
						nickName);
			}
			startActivity(intent);
			editNickNameBtn.setEnabled(true);
		}

	}

//	private class EncashAccountClickListener implements View.OnClickListener {
//		@Override
//		public void onClick(View v) {
//			layoutCoinQSJ.setEnabled(false);
//			Intent intent = new Intent(mainActivity,
//					EncashAccountActivity.class);
//			startActivity(intent);
//			layoutCoinQSJ.setEnabled(true);
//		}
//	}
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

			cancelBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();

				}
			});
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

//	private class QuFriendClickListener implements View.OnClickListener {
//
//		@Override
//		public void onClick(View v) {
//			iconForQuFriend.setEnabled(false);
//			Intent intent = new Intent(mainActivity, QufriendActivity.class);
//			startActivity(intent);
//			iconForQuFriend.setEnabled(true);
//
//		}
//
//	}
//
//	private class QuBaoBaoBtnClickListener implements View.OnClickListener {
//
//		@Override
//		public void onClick(View v) {
//			quBaoBaoBtn.setEnabled(false);
//			Intent intent = new Intent(mainActivity, QuBaobaoActivity.class);
//			startActivity(intent);
//			quBaoBaoBtn.setEnabled(true);
//
//		}
//
//	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		imageLoader = ImageLoader.getInstance();
		rankListImageOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true).build();
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mainActivity = this.getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_qushejiao, container,
				false);

		progressDialog = new QubaopenProgressDialog(mainActivity);

		fb = FinalBitmap.create(mainActivity);
		zhibiService = new ZhibiService(mainActivity);
		userService = new UserService(mainActivity);
		headImageView = (ImageView) view.findViewById(R.id.headIconQSJ);
		titleOfThePage = (TextView) view.findViewById(R.id.title_of_the_page);
		nickNameTextView = (TextView) view.findViewById(R.id.nickNameTextView);
		coinNumberText = (TextView) view.findViewById(R.id.coinNumberText);
		textViewForCreditAvailable = (TextView) view
				.findViewById(R.id.textViewForCreditAvailable);
		editNickNameBtn = (ImageButton) view.findViewById(R.id.editNickNameBtn);
		editNickNameBtn.setOnClickListener(new NicknameIconClickListener());
		layoutCoinQSJ = (LinearLayout) view.findViewById(R.id.layoutCoinQSJ);
//		layoutCoinQSJ.setOnClickListener(new EncashAccountClickListener());


		headImageViewPlaceHolder = (ImageView) view
				.findViewById(R.id.headIconPHQSJ);
		headImageViewPlaceHolder
				.setOnClickListener(new ClickImageToChangeHeadIcon());

		titleOfThePage.setText(this.getString(R.string.footer_zhibi));

		getLoaderManager().initLoader(0, null, ZhibiFragment.this);

		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
/**
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				SharedPreferences sharedPref = mainActivity
						.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
								Context.MODE_PRIVATE);
				return sharedPref.getBoolean(
						SettingValues.INSTRUCTION_QUSHEJIAO, true);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					new InstructionDialog(mainActivity,
							SettingValues.INSTRUCTION_QUSHEJIAO).show();
				}
			}

		}.execute();

 */
		return view;
	}
	@Override
	public void onStart() {
		super.onStart();
		if (refreshDataTask == null) {
			refreshDataTask = new RefreshDataTask();
			refreshDataTask.execute();
		} else {
			if (refreshDataTask.getStatus() == AsyncTask.Status.PENDING) {

				refreshDataTask.execute();
			} else if (refreshDataTask.getStatus() == AsyncTask.Status.FINISHED) {
				refreshDataTask = new RefreshDataTask();
				refreshDataTask.execute();
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
//
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

	private void updateContentAfterLoading(Cursor cursor) {
		cursor.moveToFirst();
		
		
		nickNameTextView.setText(cursor.getString(cursor
				.getColumnIndex("nickname")));
		coinNumberText.setText(cursor.getString(cursor.getColumnIndex("coin")));
		textViewForCreditAvailable.setText(cursor.getString(cursor
				.getColumnIndex("credit")));
		
		File fileFolder = new File(Environment.getExternalStorageDirectory()
				+ SettingValues.PATH_USER_TX_PREFIX);
		if (!fileFolder.exists()) {
			fileFolder.mkdirs();
		}

		// Program exploit:if there is no external storage,program will crash
		final String target = Environment.getExternalStorageDirectory()
				+ SettingValues.PATH_USER_TX_PREFIX
				+ FilenameUtils.getName(cursor.getString(cursor
						.getColumnIndex("picUrl")));
		File file = new File(target);
		if (!file.exists()) {
			String downloadUrl = SettingValues.URL_PREFIX
					+ cursor.getString(cursor.getColumnIndex("picUrl"));
			FinalHttp fh = new FinalHttp();
			fh.download(downloadUrl, null, target + ".temp", false,
					new AjaxCallBack<File>() {
						@Override
						public void onSuccess(File t) {
							if (isAdded()) {
				 				if (t.renameTo(new File(target))) {

									new RecToCircleTaskInQushejiao()
											.execute(target);
								}
							}
						}

					});
		} else {
			if (CurrentUserHelper.getBitmap() == null) {
				new RecToCircleTaskInQushejiao().execute(target);
			} else {
				headImageViewPlaceHolder
						.setImageResource(R.drawable.head_white_ring_background);
				headImageView.setImageBitmap(CurrentUserHelper.getBitmap());
			}

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

	private void copyStream(InputStream input, OutputStream output)
			throws IOException {

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new SqlCursorLoader(mainActivity,
				ZhibiService.QushejiaoSqlMaker.makeSql(CurrentUserHelper
						.getCurrentPhone()), UserInfo.class);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		if (cursor != null && cursor.getCount() != 0) {
			updateContentAfterLoading(cursor);
		}
//		if (progressDialog.isShowing()) {
//			progressDialog.dismiss();
//		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}

	public Handler getClosePicImageDialogHander() {
		return closePicImageDialogHander;
	}

	@Override
	public void onPause() {
		super.onPause();

		mainActivity.unregisterReceiver(this.mReceiver);
	}

	@Override
	public void onResume() {
		super.onResume();
		IntentFilter intentFilter = new IntentFilter(
				CropImageIntentService.IMAGE_UPLOAD_DONE_RECEIVER);
		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String success = intent.getStringExtra("success");
				if (success.equals("1")) {
					new AsyncTask<Void, Void, Void>() {
						@Override
						protected Void doInBackground(Void... params) {
							return zhibiService.saveQushejiaoInfo();
						}

						@Override
						protected void onPostExecute(Void result) {
							if (isAdded()) {
								getLoaderManager().restartLoader(0, null,
										ZhibiFragment.this);
							}
						}
					}.execute();
				} else {
					Toast.makeText(context,
							getString(R.string.toast_upload_photo_fail_tips),
							Toast.LENGTH_SHORT).show();
				}
			}
		};
		mainActivity.registerReceiver(mReceiver, intentFilter);
	}

	
	private void showToast(String content) {
		Toast.makeText(mainActivity, content, 3).show();
	}

}
