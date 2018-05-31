package com.bitmap.cachebitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ImageReader;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/5/30.
 */

public class MyAdapter extends BaseAdapter {

    private static final String TAG = "MyAdapter";
    private Context mContext;
    public MyAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 888;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //查看内存
        Log.i(TAG,"查看内存-----------0");
        Bitmap bitmap = ImageCache.getInstance().getBitMapMemory(String.valueOf(position));
        if(bitmap == null){
            //查看缓存池
            Log.i(TAG,"查看缓存池-----------1");
            Bitmap resuable = ImageCache.getInstance().getResuable(60, 60, 1);
            if(resuable == null){
                //查看磁盘
                Log.i(TAG,"查看磁盘---------2");
                bitmap = ImageCache.getInstance().getBitmapLruCache(String.valueOf(position), bitmap);
                if(bitmap == null){
                    //网络下载；
                    Log.i(TAG,"获取网络--------3");
                    Bitmap interBitmap = ImageCache.getInstance().getInterBitmap(String.valueOf(position));
                    if(interBitmap != null){
                        interBitmap = ImageResize.resizeBitmap(mContext,R.mipmap.ic_launcher,60,60,false,bitmap);
                        ImageCache.getInstance().putBitmap2Memory(String.valueOf(position),interBitmap);
                        ImageCache.getInstance().putBitmap2Disk(String.valueOf(position),interBitmap);
                        Log.i(TAG,"保存--------4");
                    }
                }else{
                    ImageCache.getInstance().putBitmap2Disk(String.valueOf(position),bitmap);
                }
            }
        }
        holder.mImageView.setImageBitmap(bitmap);
        holder.mTextView.setText("i = " + position);
        return convertView;
    }

    class ViewHolder{
        ImageView mImageView;
        TextView mTextView;
        ViewHolder(View view){
            mImageView = view.findViewById(R.id.iv);
            mTextView = view.findViewById(R.id.tv);
        }
    }
}
