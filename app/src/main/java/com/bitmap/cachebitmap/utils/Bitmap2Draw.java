package com.bitmap.cachebitmap.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.bitmap.cachebitmap.R;

/**
 * Created by Administrator on 2018/5/31.
 */
public class Bitmap2Draw {

    /**
     *   bitmap 转化成drawable;
     * @param resouce
     * @param bitmap
     * @return
     */
    public static Drawable bitmap2Drwable(Resources resouce, Bitmap bitmap){
        Drawable drawable = new BitmapDrawable(resouce,bitmap);
        return drawable;
    }

//---------------------------------------------------------------------------------
    /**
     *   drawable转化bitmap
     * @param resources
     * @return
     */
    public static Bitmap drawable2Bitmap(Resources resources){
        Drawable drawable = resources.getDrawable(R.drawable.ic_launcher);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        return bitmap;
    }

    /**
     *  draw转化为bitmap
     * @param resources
     * @param context
     * @return
     */
    public static Bitmap drawable2Bitmap1(Resources resources,Context context){
        Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher);
        return bitmap;
    }

    public static Bitmap drawable2Bitmap2(Drawable drawable){
        int drawW = drawable.getIntrinsicWidth();
        int draH = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888: Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(drawW,draH,config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,drawW,draH);
        drawable.draw(canvas);
        return bitmap;
    }

    //----------------------------------------------------------------------------------
    /**
     *   获取stream
     *   resources.openRawResource(resourceId);
     *
     *   获取assert Stream
     *   Assermanager am = context.getResource().getAssert();
     *   stream = am.openFile(path)
     *
     */

}
