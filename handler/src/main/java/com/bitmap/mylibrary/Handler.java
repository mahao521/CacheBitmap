package com.bitmap.mylibrary;

/**
 * Created by Administrator on 2018/6/5.
 */

public class Handler {

    MessageQueue mMessageQueue;
    public Handler(){
        Looper looper = Looper.getMainLooper();
        mMessageQueue = looper.messageQueue;
    }

    public void sendMessage(Message message){
        message.target = this;
        mMessageQueue.enqueMessage(message);
    }

    public void handleMessage(Message msg){

    }

}
