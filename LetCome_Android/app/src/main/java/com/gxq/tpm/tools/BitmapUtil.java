package com.gxq.tpm.tools;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapUtil {
	
	public static Bitmap zoom(Bitmap bitmap, float zf) {
		Matrix matrix = new Matrix();
		matrix.postScale(zf, zf);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
	}

}
