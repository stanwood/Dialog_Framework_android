package io.stanwood.framework.dialog.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class CropCircleTransformation extends BitmapTransformation {

    private static final String ID = "de.stanwood.comm.glide.CropCircleTransformation";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private static BitmapTransformation transformation;

    public CropCircleTransformation(Context context) {
        this();
    }

    public CropCircleTransformation(BitmapPool pool) {
        this();
    }

    public CropCircleTransformation() {
        // Intentionally empty.
    }

    public static BitmapTransformation circleCrop() {
        if (transformation == null) {
            transformation = new CropCircleTransformation();
        }
        return transformation;
    }

    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int size = Math.min(toTransform.getWidth(), toTransform.getHeight());
        int width = (toTransform.getWidth() - size) / 2;
        int height = (toTransform.getHeight() - size) / 2;
        Bitmap bitmap = pool.get(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(toTransform, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        if (width != 0 || height != 0) {
            // source isn't square, move viewport to center
            Matrix matrix = new Matrix();
            matrix.setTranslate(-width, -height);
            shader.setLocalMatrix(matrix);
        }
        paint.setShader(shader);
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        return bitmap;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CropCircleTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}