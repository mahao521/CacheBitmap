package com.bitmap.cachebitmap;

import android.app.Dialog;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bitmap.cachebitmap.handler.MultiActivity;
import com.bitmap.cachebitmap.view.ViewActivity;
import com.bitmap.database.MainTabActivity;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    /**
     *   option.ismulite = true;
     *   inbitmap 仅仅在重复使用一张照片有用。
     *   图片占用内存的大小，只和 长 x 宽 x 像素 有关
     *   和质量大小无关。
     */
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sdcard/mahao/目录下/可以查看到文件
        ImageCache.getInstance().init(this, Environment.getExternalStorageDirectory()+"/mahao");
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(new MyAdapter(this));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    InputStream inputStream = getAssets().open("tvplayer_progress_bg_music.png");
                    Log.d(TAG, "onItemClick: " + inputStream.available() + " read " + inputStream.read());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(position % 5 == 1){
                    Intent intent = new Intent(MainActivity.this,MultiActivity.class);
                    startActivity(intent);
                }else if(position % 5 == 2){
                    Intent intent = new Intent(MainActivity.this,ViewActivity.class);
                    startActivity(intent);
                }else if(position % 5 == 3){
                    Intent intent = new Intent(MainActivity.this, MainTabActivity.class);
                    startActivity(intent);
                }else if(position % 5 == 4){
                     Intent intent = new Intent(MainActivity.this,SkinActivity.class);
                     startActivity(intent);
                }
            }
        });
    }

}
