package com.bitmap.cachebitmap;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.util.LruCache;

import com.bitmap.cachebitmap.DisLruCache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Administrator on 2018/5/30.
 */

public class ImageCache {

    private final String TAG = "ImageCache";
    private Context mContext;
    public static ImageCache instance;
    private Set<WeakReference<Bitmap>> mWeakPool;
    private LruCache<String, Bitmap> mMempryCache;
    private DiskLruCache mDiskLruCache;

    private ImageCache(){
    }
    public static ImageCache getInstance(){
        if(null == instance){
            synchronized (ImageCache.class){
                if(instance == null){
                    instance = new ImageCache();
                }
            }
        }
        return instance;
    }

    public void init(Context context,String path){
        this.mContext = context;
        //复用池
        mWeakPool = Collections.synchronizedSet(new HashSet<WeakReference<Bitmap>>());
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        //获取最大运行内存
        int memoryClass = am.getMemoryClass();
        Log.i(TAG,memoryClass+"..." + Runtime.getRuntime().maxMemory());
        //每一张图片的存储到内存的大小
    /**
     *   当bitmap从lru中移除 回调
     * @param evicted
     * @param key
     * @param oldValue
     * @param newValue
     */ //3.0以下，Bitmap内存在native中
    //8.0 Biamtp native;
        mMempryCache = new LruCache<String,Bitmap>(100*1024){

            //每一张图片的存储到内存的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                if(Build.VERSION.SDK_INT  > Build.VERSION_CODES.KITKAT){
                    return value.getAllocationByteCount();
                }
                return value.getByteCount();
            }

            /**
             *   当bitmap从lru中移除 回调
             * @param evicted
             * @param key
             * @param oldValue
             * @param newValue
             */
            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                if(oldValue.isMutable()){
                    //3.0以下，Bitmap内存在native中
                    //8.0 Biamtp native;
                    mWeakPool.add(new WeakReference<Bitmap>(oldValue,getReferenceQueue()));
                }else{
                    oldValue.recycle();
                }
            }
        };

        try {
            mDiskLruCache = DiskLruCache.open(new File(path), BuildConfig.VERSION_CODE, 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  加入内存缓存
     * @param key
     * @param bitmap
     */
    public void putBitmap2Memory(String key,Bitmap bitmap){
        mMempryCache.put(key,bitmap);
    }

    /**
     *  加入磁盘缓存
     * @param key
     * @param bitmap
     */
    public void putBitmap2Disk(String key , Bitmap bitmap){
        DiskLruCache.Snapshot snapshot = null;
        OutputStream os = null;
        try {
            snapshot = mDiskLruCache.get(key);
            if(snapshot == null){
                DiskLruCache.Editor edit = mDiskLruCache.edit(key);
                if(edit != null){
                    os = edit.newOutputStream(0);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,50,os);
                    edit.commit();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(os != null){
                    os.close();
                }
                if (snapshot != null){
                    snapshot.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *   从内存中获取图片
     * @param key
     * @return
     */
    public Bitmap getBitMapMemory(String key){
        return mMempryCache.get(key);
    }


    /**
     *  从磁盘中获取bitmap
     *
     *  android4.4之前只有格式为jpg 和 png同等宽高的 insampleSize 为1的才可以复用
     *
     *  android4.4之后复用的bitmap必须要大于等于待申请bitmap的内存。
     *
     * @param key
     * @param reUse
     * @return
     */
    public Bitmap getBitmapLruCache(String key,Bitmap reUse){

        DiskLruCache.Snapshot snapshot = null;
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            snapshot = mDiskLruCache.get(key);
            if(null == snapshot){
                return null;
            }
            inputStream = snapshot.getInputStream(0);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            options.inBitmap = reUse;
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            if(bitmap != null){
                mMempryCache.put(key,bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(snapshot != null){
                snapshot.close();
            }
            try {
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    boolean isShutDown;
    ReferenceQueue mReferenceQueue;
    Thread clearReferenceQueue;
    private ReferenceQueue<Bitmap> getReferenceQueue(){
        if(null == mReferenceQueue){
            mReferenceQueue = new ReferenceQueue();
            clearReferenceQueue = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(!isShutDown){
                        Reference remove = null;
                        try {
                            remove = mReferenceQueue.remove();
                            Bitmap bitmap = (Bitmap) remove.get();
                            if(bitmap != null && bitmap.isRecycled()){
                                bitmap.recycle();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        if (!clearReferenceQueue.isAlive()) {
            clearReferenceQueue.start();
        }
        return mReferenceQueue;
    }

    /**
     *   获取能重用的bitmap
     * @param w
     * @param h
     * @param insample
     * @return
     */
    public  Bitmap  getResuable(int w,int h ,int insample){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            return null;
        }
        Bitmap resuable = null;
        Iterator<WeakReference<Bitmap>> iterator = mWeakPool.iterator();
        while (iterator.hasNext()){
            Bitmap bitmap = iterator.next().get();
            if(null != bitmap){
                //可以被复用
                 if(checkUsable(bitmap,w,h,insample)){
                     resuable = bitmap;
                     //移除复用池；
                    iterator.remove();
                    break;
                }
            }else{
                iterator.remove();
            }
        }
        return resuable;
    }

    /**
     *   检测bitmap是否可以被重用 4.0 之前，4.0之后复用有不同要求
     * @param bitmap
     * @param width
     * @param height
     * @param insample
     * @return
     */
    public boolean checkUsable(Bitmap bitmap,int width ,int height,int insample){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            return bitmap.getWidth()==width && bitmap.getHeight() == height && insample == 1;
        }
        if(insample > 1){
            width /= insample;
            height /= insample;
        }
        int byecount = width*height*getPixCount(bitmap.getConfig());
        return byecount > bitmap.getAllocationByteCount();
    }

    private int getPixCount(Bitmap.Config config){
        if(config == Bitmap.Config.ARGB_8888){
            return 4;
        }
        return 2;
    }

    /**
     *  从网络下载图片
     * @param key
     */
    public Bitmap getInterBitmap(String key){
        return BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.ic_launcher,null);
    }
}
