package com.bitmap.cachebitmap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2018/5/30.
 */

public class ImageResize {

    /**
     *   图片内存优化
     * @param context
     * @param id
     * @param maxW
     * @param maxH
     * @param hasAlpha
     * @param reusable
     * @return
     */
    public static Bitmap resizeBitmap(Context context,int id,int maxW,int maxH,boolean hasAlpha,Bitmap reusable){
        Resources resources = context.getResources();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources,id,options);
      //  reusable.compress(Bitmap.CompressFormat.JPEG,500,new FileOutputStream(new File("mahao.jpg"));
        int bitW = options.outWidth;
        int bitH = options.outHeight;
        options.inSampleSize = cacluteSize(bitW,bitH,maxW,maxH);
        if(!hasAlpha){
            options.inPreferredConfig = Bitmap.Config.RGB_565;
        }
        options.inJustDecodeBounds = false;
        //复用内存
        options.inMutable = true;
        options.inBitmap = reusable;
        return BitmapFactory.decodeResource(resources,id,options);
    }


    /**
     *    比较当前图片大小，计算采样率（缩放比）
     * @param bitW
     * @param bitH
     * @param maxW
     * @param maxH
     * @return
     */
    public static int cacluteSize(int bitW,int bitH,int maxW,int maxH){

        int inSample  = 1;
        if(bitW > maxW && bitH > maxH){
            inSample = 2 ;
            while(bitW/inSample > maxW && bitH /inSample > maxH){
                inSample *= 2 ;
            }
        }
        return inSample;
    }

    /**
     *  bitmap转化成byte[];
     */
    public byte[] bitmap2Stream(Bitmap bitmap){
        ByteArrayOutputStream outputStream  = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,outputStream);
        return outputStream.toByteArray();
    }

}


















