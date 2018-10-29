package com.zunder.smart.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapService {
	public static Bitmap getBitmapFromByteSpace(byte[] temp) {
		Bitmap bitmap = null;
		if (temp != null) {
			bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
//			bitmap.recycle();
		}
		return bitmap;
	}
	public static byte[] getBitmapByte(Bitmap bitmap) {
		if (bitmap != null) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return out.toByteArray();
		} else {
			return null;
		}
	}
}
