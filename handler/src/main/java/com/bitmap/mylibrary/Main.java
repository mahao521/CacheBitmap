package com.bitmap.mylibrary;

/**
 * Created by Administrator on 2018/6/5.
 */

public class Main {

    /**
     *   looper对象，在那个线程创建，就应该在那个线程中获取。
     * @param args
     */
    public static void main(String[] args){

        Looper.prepare();
        System.out.println("Looper.getMainLooper()==" + Looper.getMainLooper());
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //主线程接收
                System.out.println(Thread.currentThread()+"receive====" + msg.obj );
            }
        };
        Message message = new Message();
        message.obj = "mahao";
        handler.sendMessage(message);
        final Looper myLooper = Looper.getMainLooper();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
               /* System.out.println("Looper.getMainLooper()" + Looper.getMainLooper());
                System.out.println("Looper.getMainLooper()---" + Looper.getMainLooper().messageQueue);
                Looper.getMainLooper().messageQueue.quite();*/
                myLooper.quite();
            }
        }).start();
        Looper.loop();
    }
}
