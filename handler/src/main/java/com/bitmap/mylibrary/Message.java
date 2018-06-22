package com.bitmap.mylibrary;

/**
 * Created by Administrator on 2018/6/5.
 */
public class Message {

     Message next;
     int msg1;
     int msg2;
     Object obj;

     //发送给指定handler消息。
     Handler target;
     public Message(){}

     public void recycle(){
         target = null;
         next = null;
         obj = null;
     }
}
