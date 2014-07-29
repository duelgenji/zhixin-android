package com.zhixin.utils;

import com.zhixin.settings.CurrentUserHelper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.os.AsyncTask;
import android.widget.ImageView;

public class RecToCircleTask extends AsyncTask<String, Void, Bitmap> {
	private ImageView imageView;
	private boolean currentUserOrNot;
	private Bitmap transferedBitmap;

	public RecToCircleTask(ImageView imageView, boolean currentUserOrNot) {
		super();
		this.imageView = imageView;
		this.currentUserOrNot = currentUserOrNot;

	}

	protected Bitmap doInBackground(String... urls) {

		Bitmap bitmap = BitmapFactory.decodeFile(urls[0]);
		Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);

		BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP,
				TileMode.CLAMP);
		Paint paint = new Paint();
		paint.setShader(shader);

		Canvas c = new Canvas(circleBitmap);
		c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				bitmap.getWidth() / 2, paint);
		return circleBitmap;
	}

	protected void onPostExecute(Bitmap result) {
		if (currentUserOrNot) {
			CurrentUserHelper.saveBitmap(result);
			this.transferedBitmap = result;
		}
		imageView.setImageBitmap(result);
	}

	public Bitmap getTransferedBitmap() {
		return transferedBitmap;
	}

	public static Bitmap transferToCircle(Bitmap bitmap) {
		if (bitmap != null) {
			Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Bitmap.Config.ARGB_8888);

			BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP,
					TileMode.CLAMP);
			Paint paint = new Paint();
			paint.setShader(shader);

			Canvas c = new Canvas(circleBitmap);
			c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
					bitmap.getWidth() / 2, paint);
			return circleBitmap;
		}
		return null;

	}
}
