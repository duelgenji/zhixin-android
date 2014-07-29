package com.zhixin.adapter;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.zhixin.R;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.RecToCircleTask;

public class QuFriendAnswerItem extends CursorAdapter {

	private Context context;

	private ImageLoader imageLoader;
	private DisplayImageOptions rankListImageOptions;

	private class QuFriendAnswerHeadIconImage implements ImageLoadingListener {

		private ImageView headIconImage;

		public QuFriendAnswerHeadIconImage(ImageView view) {
			this.headIconImage = view;

		}

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			headIconImage.setImageResource(R.drawable.head_icon_default);
		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			headIconImage.setImageBitmap(RecToCircleTask
					.transferToCircle(loadedImage));
		}

		@Override
		public void onLoadingCancelled(String arg0, View arg1) {

		}

		@Override
		public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

		}

	}

	public QuFriendAnswerItem(Context context, Cursor cursor) {
		super(context, cursor, FLAG_REGISTER_CONTENT_OBSERVER);
		this.context = context;

		imageLoader = ImageLoader.getInstance();
		rankListImageOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true).build();
	}

	@Override
	public void bindView(View rowView, Context context, Cursor cursor) {
		updatingContentInView(rowView, cursor);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(
				R.layout.adapter_quceshi_friend_answer_item, parent, false);

		return updatingContentInView(rowView, cursor);
	}

	private View updatingContentInView(final View rowView, Cursor cursor) {
		TextView nickname = (TextView) rowView.findViewById(R.id.nickname);
		ImageView headIconPlaceHolder = (ImageView) rowView
				.findViewById(R.id.headIconPlaceHolderQHYDA);
		ImageView headIconImage = (ImageView) rowView
				.findViewById(R.id.headIconQHYDA);

		nickname.setText(cursor.getString(cursor.getColumnIndex("nickname")));

		// load images
		String imageUri = cursor.getString(cursor.getColumnIndex("picUrl"));
		if (StringUtils.isNotEmpty(imageUri)) {
			imageUri = SettingValues.URL_PREFIX + imageUri;
			imageLoader.displayImage(imageUri, headIconImage,
					rankListImageOptions, new QuFriendAnswerHeadIconImage(
							headIconImage), null);
			headIconPlaceHolder
					.setImageResource(R.drawable.head_white_ring_background);
		} else {
			headIconPlaceHolder.setImageResource(R.drawable.head_icon_default);
			headIconImage.setImageBitmap(null);
		}
		return rowView;

	}

}
