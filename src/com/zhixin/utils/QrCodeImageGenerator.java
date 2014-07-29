package com.zhixin.utils;

import java.util.EnumMap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import android.graphics.Bitmap;
import android.graphics.Color;

public class QrCodeImageGenerator {

	public static Bitmap generateQrImage(String qrMessage) {
		QRCodeWriter writer = new QRCodeWriter();
		BitMatrix matrix = null;
		try {

			EnumMap<EncodeHintType, Object> hint = new EnumMap<EncodeHintType, Object>(
					EncodeHintType.class);
			hint.put(EncodeHintType.CHARACTER_SET, "UTF-8");

			matrix = writer.encode(qrMessage, BarcodeFormat.QR_CODE, 600, 600,
					hint);
		} catch (WriterException e) {
			e.printStackTrace();
		}

		return toBitmap(matrix);

	}

	private static Bitmap toBitmap(BitMatrix matrix) {
		int height = matrix.getHeight();
		int width = matrix.getWidth();
		Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
			}
		}
		return bmp;
	}

}
