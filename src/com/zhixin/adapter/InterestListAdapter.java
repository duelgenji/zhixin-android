package com.zhixin.adapter;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zhixin.R;
import com.zhixin.activity.InterestContentActivity;
import com.zhixin.settings.SettingValues;

public class InterestListAdapter extends CursorAdapter {

	// type 0 for quceshi
	// type 1 for qudiaoyan
	// private int type;

	private InterestListAdapter _this;

	private Context context;

	private ImageLoader imageLoader;

	private DisplayImageOptions imageOptions;

	private class QuListTouchListener implements View.OnTouchListener {
		private int interestId;

		public QuListTouchListener(int interestId) {
			this.interestId = interestId;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.setBackgroundColor(context.getResources().getColor(
						R.color.general_up_background));
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.setBackgroundColor(context.getResources().getColor(
						R.color.general_activity_background));
				Intent intent;
				intent = new Intent(context, InterestContentActivity.class);
				intent.putExtra(InterestContentActivity.INTENT_INTEREST_ID,
						interestId);
				String title=((TextView) v.findViewById(R.id.questionareTitle)).getText().toString();
				intent.putExtra(InterestContentActivity.INTENT_QUESTIONNAIRE_TITLE, title);
				context.startActivity(intent);
			} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
				v.setBackgroundColor(context.getResources().getColor(
						R.color.general_activity_background));
			}
			return true;
		}

	}

	public InterestListAdapter(Context context, Cursor c) {
		super(context, c, FLAG_REGISTER_CONTENT_OBSERVER);
		_this = this;
		this.context = context;
		imageLoader = ImageLoader.getInstance();
		imageOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).build();
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.adapter_interest_list_item,
				parent, false);

		return updatingContentInView(rowView, cursor);

	}

	@Override
	public void bindView(View rowView, Context context, Cursor cursor) {
		updatingContentInView(rowView, cursor);
	}

	private View updatingContentInView(final View rowView, Cursor cursor) {
		ImageView img_quceshi_list = (ImageView) rowView
				.findViewById(R.id.img_quceshi_list);
		final TextView questionareTitle = (TextView) rowView
				.findViewById(R.id.questionareTitle);
		TextView answerNumberTextView = (TextView) rowView
				.findViewById(R.id.answerNumberTextView);
		TextView answerFriendNumber = (TextView) rowView
				.findViewById(R.id.answerFriendNumber);
		
		final int interestId = cursor.getInt(cursor.getColumnIndex("interestId"));

		// necessary injection
		questionareTitle.setText(cursor.getString(cursor
				.getColumnIndex("title")));
		if (StringUtils.isNotEmpty(cursor.getString(cursor
				.getColumnIndex("totalRespondentsCount")))) {
			answerNumberTextView.setText(cursor.getString(cursor
					.getColumnIndex("totalRespondentsCount")));
		} else {
			answerNumberTextView.setText("0");
		}

		if (StringUtils.isNotEmpty(cursor.getString(cursor
				.getColumnIndex("friendCount")))) {
			answerFriendNumber.setText(cursor.getString(cursor
					.getColumnIndex("friendCount")));
		} else {
			answerFriendNumber.setText("0");
		}

		
		
		//需要增加省流量模式判断
		if (StringUtils.isNotEmpty(cursor.getString(cursor
				.getColumnIndex("picPath")))) {
			img_quceshi_list.setVisibility(View.VISIBLE);
			String imageUri = cursor
					.getString(cursor.getColumnIndex("picPath"));
			if (StringUtils.isNotEmpty(imageUri)) {
				imageUri = SettingValues.URL_PREFIX + imageUri;
				imageLoader.displayImage(imageUri, img_quceshi_list,
						imageOptions, new ImageLoading(img_quceshi_list), null);

				img_quceshi_list.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent;
						intent = new Intent(context, InterestContentActivity.class);
						intent.putExtra(InterestContentActivity.INTENT_INTEREST_ID,
								interestId);
						String title=questionareTitle.getText().toString();
						intent.putExtra(InterestContentActivity.INTENT_QUESTIONNAIRE_TITLE, title);
						context.startActivity(intent);
					}
				});

			} else {
				img_quceshi_list
						.setImageResource(R.drawable.interest_list_default_image);
			}
		}

		// tag injection标签显示

		FrameLayout tagFragment1 = (FrameLayout) rowView
				.findViewById(R.id.tagFragment1);
		FrameLayout tagFragment2 = (FrameLayout) rowView
				.findViewById(R.id.tagFragment2);
		FrameLayout tagFragment3 = (FrameLayout) rowView
				.findViewById(R.id.tagFragment3);
		FrameLayout tagFragment4 = (FrameLayout) rowView
				.findViewById(R.id.tagFragment4);
		FrameLayout tagFragment5 = (FrameLayout) rowView
				.findViewById(R.id.tagFragment5);
		tagFragment1.setVisibility(View.GONE);
		tagFragment2.setVisibility(View.GONE);
		tagFragment3.setVisibility(View.GONE);
		tagFragment4.setVisibility(View.GONE);
		tagFragment5.setVisibility(View.GONE);
		ArrayList<FrameLayout> frameList = new ArrayList<FrameLayout>();

		frameList.add(tagFragment1);
		frameList.add(tagFragment2);
		frameList.add(tagFragment3);
		frameList.add(tagFragment4);
		frameList.add(tagFragment5);

		String tags = cursor.getString(cursor
				.getColumnIndex("questionnaireTagType"));
		if (StringUtils.isNotEmpty(tags)) {
			String[] tagArray = tags.split(";");

			for (int i = 0; i < tagArray.length; i++) {
				int index = Integer.parseInt(tagArray[i]);
				FrameLayout frame = frameList.get(i);

				ImageView tagImage = (ImageView) frame
						.findViewWithTag("tagImage");
				TextView tagText = (TextView) frame.findViewWithTag("tagText");

				switch (index) {
				case 1:
					tagImage.setImageResource(R.drawable.qu_tuijian_icon);
					tagText.setText(context
							.getString(R.string.quceshi_biaoqian_tuijian));

					break;
				case 2:
					tagImage.setImageResource(R.drawable.qu_remeng_icon);
					tagText.setText(context
							.getString(R.string.quceshi_biaoqian_remeng));
					break;
				case 3:
					tagImage.setImageResource(R.drawable.qu_zuixing_icon);
					tagText.setText(context
							.getString(R.string.quceshi_biaoqian_zuixin));
					break;
				case 4:
					tagImage.setImageResource(R.drawable.qu_xianshi_icon);
					tagText.setText(context
							.getString(R.string.quceshi_biaoqian_xianshi));

					break;
				case 5:

					tagImage.setImageResource(R.drawable.qu_shaoliang_icon);
					tagText.setText(context
							.getString(R.string.quceshi_biaoqian_shaoliang));

					break;

				case 6:

					tagImage.setImageResource(R.drawable.qu_jinbi_icon);
					tagText.setText(context
							.getString(R.string.quceshi_biaoqian_jinbi));

					break;
				default:
					break;
				}
				frame.setVisibility(View.VISIBLE);
			}

		}
		
		rowView.setOnTouchListener(new QuListTouchListener(interestId));

		return rowView;

	}

	private class ImageLoading implements ImageLoadingListener {

		private ImageView imageView;

		public ImageLoading(ImageView view) {
			this.imageView = view;

		}

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			imageView.setImageResource(R.drawable.interest_list_default_image);
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

}
