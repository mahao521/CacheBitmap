package com.bitmap.skin;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bitmap.skin.utils.SkinResource;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by Administrator on 2018/7/18.
 */

public class SkinAttribute {

    private static final List<String> mAttributes = new ArrayList<>();
    private static final String TAG = "SkinAttribute";
    static {
        mAttributes.add("background");
        mAttributes.add("src");
        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");
        mAttributes.add("skinTypeface");
    }
    private Typeface mTypeface;
    //记录换肤需要操作的View和属性信息
    private List<SkinView>  mSkinViews = new ArrayList<>();

    public SkinAttribute(Typeface typeface){
        this.mTypeface = typeface;
    }

    public void setTypeface(Typeface typeface){
        this.mTypeface = typeface;
    }

    public void Load(View view, AttributeSet attributeSet){
        List<SkinPair> mSkinPairs = new ArrayList<>();
        //获取属性名
        for (int i = 0 ; i < attributeSet.getAttributeCount(); i++){
            String attributeName = attributeSet.getAttributeName(i);
            if(mAttributes.contains(attributeName)){
                String attributeValue = attributeSet.getAttributeValue(i);
                //如果是color的，以#开头表示写死的颜色，不可以用于换肤
                if(attributeValue.startsWith("#")){
                    continue;
                }
                int resId ;
                if(attributeValue.startsWith("?")){
                    int attrId = Integer.parseInt(attributeValue.substring(1));
                    resId = SkinThemeutils.getResId(view.getContext(),new int[]{attrId})[0];
                }else{
                    resId = Integer.parseInt(attributeValue.substring(1));
                }
                SkinPair skinPair = new SkinPair(attributeName,resId);
                mSkinPairs.add(skinPair);
            }
        }
        if(!mSkinPairs.isEmpty()){
            SkinView skinView = new SkinView(view,mSkinPairs);
            skinView.applySkin(mTypeface);
            mSkinViews.add(skinView);
        }else if(view instanceof TextView || view instanceof SkinViewSupport){
            //没有属性满足，但是需要修改字体
            SkinView skinView = new SkinView(view,mSkinPairs);
            skinView.applySkin(mTypeface);
            mSkinViews.add(skinView);
        }
    }

    public void applySkin(){
        for (SkinView mSkinView : mSkinViews){
            mSkinView.applySkin(mTypeface);
        }
    }

    static class SkinView{
        View view;
        List<SkinPair> mSkinPairs = new ArrayList<>();

        public SkinView(View view, List<SkinPair> skinPairs){
            this.view = view;
            this.mSkinPairs = skinPairs;
        }

        public void applySkin(Typeface typeface){

             Log.d(TAG, "applySkin: " +  typeface);
             applyTypeFace(typeface);
             applySkinSupport();
             for(SkinPair skinPair : mSkinPairs){
                 Drawable left = null,top = null,right = null,bottom = null;
                 Object background;
                 Log.d(TAG, "applySkin: " +  skinPair.attributeName +" resId " + skinPair.resId);
                 if(skinPair.resId == 0){
                     continue;
                 }
                 switch (skinPair.attributeName){
                     case "background":
                         background= SkinResource.getInstance().getBackground(skinPair.resId);
                         if(background instanceof Integer){
                             view.setBackgroundColor((Integer) background);
                         }else{
                             ViewCompat.setBackground(view,(Drawable) background);
                           //  view.setBackground((Drawable) background);
                         }
                         break;
                     case "src":
                         background = SkinResource.getInstance().getBackground(skinPair.resId);
                         if(background instanceof Integer){
                             ((ImageView)view).setImageDrawable(new ColorDrawable((Integer) background));
                         }else{
                             ((ImageView)view).setImageDrawable((Drawable) background);
                         }
                         break;
                     case "textColor":
                         ((TextView)view).setTextColor(SkinResource.getInstance().getStateList(skinPair.resId));
                         break;
                     case "drawableLeft":
                         left = SkinResource.getInstance().getDrawable(skinPair.resId);
                         break;
                     case "drawableTop":
                         left = SkinResource.getInstance().getDrawable(skinPair.resId);
                         break;
                     case "drawableRight":
                         left = SkinResource.getInstance().getDrawable(skinPair.resId);
                         break;
                     case "drawableBottom":
                         left = SkinResource.getInstance().getDrawable(skinPair.resId);
                         break;
                     case "skinTypeface":
                         applyTypeFace(SkinResource.getInstance().getTypeFace(skinPair.resId));
                         break;

                 }
                 if(null != left || null != right || null != top || null != bottom){
                     ((TextView)view).setCompoundDrawablesWithIntrinsicBounds(left,right,top,bottom);
                 }
             }
        }

        public void applyTypeFace(Typeface typeface){
            if(view instanceof TextView){
                ((TextView)view).setTypeface(typeface);
            }
        }

        public void applySkinSupport(){
            if(view instanceof SkinViewSupport){
                ((SkinViewSupport)view).applySkin();
            }
        }
    }

    static class SkinPair{
        String attributeName;
        int resId;
        public SkinPair(String attributeName,int resId){
            this.attributeName = attributeName;
            this.resId = resId;
        }
    }
}
