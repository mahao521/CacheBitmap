package com.bitmap.database.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * Created by Administrator on 2018/7/3.
 */

public class TextSizePx {
    public static DisplayMetrics matrics = null;

    private TextSizePx() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static int a(Context var0, float var1) {
        return (int) TypedValue.applyDimension(1, var1, var0.getResources().getDisplayMetrics());
    }

    public static int b(Context var0, float var1) {
        return (int)TypedValue.applyDimension(2, var1, var0.getResources().getDisplayMetrics());
    }

    public static float c(Context var0, float var1) {
        float var2 = var0.getResources().getDisplayMetrics().density;
        return var1 / var2;
    }

    public static float d(Context var0, float var1) {
        return var1 / var0.getResources().getDisplayMetrics().scaledDensity;
    }

    public static int a(float var0,Context context) {
        float var1 = getMatrics(context).density;
        return (int)(var0 * var1 + 0.5F);
    }

    public static DisplayMetrics getMatrics(Context context) {
        if(matrics == null) {
            matrics = new DisplayMetrics();
            ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(
                    matrics);
        }

        return matrics;
    }
}