package com.sweven.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

public class QRCodeUtil {
    private static final int BLACK = 0xff000000;
    private static final int WHILT = 0xffffffff;

    public static Bitmap createQRCode(String str, int widthAndHeight,
                                      Bitmap logoBm) throws WriterException {
        String format = "jpg";

        // Hashtable<EncodeHintType, String> hints = new
        // Hashtable<EncodeHintType, String>();

        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 容错级别
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 设置空白边距的宽度
        // hints.put(EncodeHintType.MARGIN, 2); //default is 4

        BitMatrix matrix = new MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = BLACK;
                } else {
                    pixels[y * width + x] = WHILT;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        if (logoBm != null) {
            bitmap = addLogo(bitmap, logoBm);
        }

        return bitmap;
    }

    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        // 获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getWidth();
        // int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        // logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 7 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight,
                Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth >> 1, srcHeight >> 1);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) >> 1, (srcHeight - logoHeight) >> 1, null);

            canvas.save();
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }
}
