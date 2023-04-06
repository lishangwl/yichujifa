package com.xieqing.codeutils.util;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class FormatCastUtils {
    public static byte[] getBytesByBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bitmap.getByteCount());
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }
}
