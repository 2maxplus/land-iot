package com.hyf.iot.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.util.List;


public class ImageUtil {
    public static synchronized void showImg(Context context, String imgUri, @DrawableRes int defaultSrcId, @DrawableRes int errorSrcId, ImageView target, float sizeMultiplier) {
        if (TextUtils.isEmpty(imgUri)) {
            return;
        }
        Glide.with(context)
                .load(imgUri)
                .apply(new RequestOptions()
                        .placeholder(defaultSrcId)
                        .error(errorSrcId))
                .thumbnail((sizeMultiplier > 0 && sizeMultiplier < 1) ? sizeMultiplier : 1.0f)
                .into(target);
    }

    public static synchronized void showImg(Context context, String imgUri, SimpleTarget<Drawable> target, @DrawableRes int defaultSrcId, @DrawableRes int errorSrcId, float sizeMultiplier) {
        if (TextUtils.isEmpty(imgUri)) {
            return;
        }
        Glide.with(context).load(imgUri)
                .apply(new RequestOptions()
                        .placeholder(defaultSrcId)
                        .error(errorSrcId))
                .thumbnail((sizeMultiplier > 0 && sizeMultiplier < 1) ? 1.0f : sizeMultiplier)
                .into(target);
    }

    public static synchronized void showImg(Context context, String imgUri, @DrawableRes int defaultSrcId, @DrawableRes int errorSrcId, SimpleTarget<Bitmap> target, float sizeMultiplier) {
        if (TextUtils.isEmpty(imgUri)) {
            return;
        }
        Glide.with(context).asBitmap().load(imgUri)
                .apply(new RequestOptions()
                        .placeholder(defaultSrcId)
                        .error(errorSrcId))
                .thumbnail((sizeMultiplier > 0 && sizeMultiplier < 1) ? 1.0f : sizeMultiplier)
                .into(target);
    }

    public static synchronized Bitmap cropSquareBitmap(Bitmap bitmap) {//从中间截取一个正方形
        return cropSquareBitmap(bitmap, Integer.MAX_VALUE);
    }

    public static synchronized Bitmap cropSquareBitmap(Bitmap bitmap, int maxWH) {//从中间截取一个正方形
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int cropWidth = Math.min(maxWH, Math.min(w, h));
        return Bitmap.createBitmap(bitmap, (bitmap.getWidth() - cropWidth) / 2, (bitmap.getHeight() - cropWidth) / 2, cropWidth, cropWidth);
    }

    private static File getCacheDir() {
        String cacheDirName = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "testCache";
        File cacheDirFile = new File(cacheDirName);
        if (cacheDirFile.exists()) {
            return cacheDirFile;
        } else {
            if (cacheDirFile.mkdir()) {
                return cacheDirFile;
            }
        }
        return null;
    }

    public interface CompressImgListener {
        void onSuccess(List<File> compressedImgFileList);

        void onFailed();
    }
}
