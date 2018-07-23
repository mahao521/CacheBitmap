package com.bitmap.cachebitmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bitmap.skin.SkinManager;

/**
 * 换肤
 * 颜色: colors.xml 配置需要替换的颜色name 为不同的颜色值
 * 图片： 同上
 * 选择器：同上 (如 颜色选择器，皮肤包中的颜色选择器会使用皮肤包中的颜色)
 * 字体：strings.xml 配置 typeface 路径指向 assets 目录下字体文件
 * 自定义View 需要实现SkinViewSupport接口自行实现换肤逻辑(包括support中的View )
 */
public class SkinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);
    }

    public void changeSkin(View view) {
        SkinManager.getInstance().loadSkin("/sdcard/app-skin-debug.skin1");
    }

    public void restoreSkin(View view) {
        SkinManager.getInstance().loadSkin(null);
    }
}
