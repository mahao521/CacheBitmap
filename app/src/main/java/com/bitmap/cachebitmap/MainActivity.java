package com.bitmap.cachebitmap;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    /**
     *   option.ismulite = true;
     *   inbitmap 仅仅在重复使用一张照片有用。
     *   图片占用内存的大小，只和 长 x 宽 x 像素 有关
     *   和质量大小无关。
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sdcard/mahao/目录下/可以查看到文件
        ImageCache.getInstance().init(this, Environment.getExternalStorageDirectory()+"/mahao");
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(new MyAdapter(this));
    }
}
