package com.bitmap.mylibrary;

/**
 * Created by Administrator on 2018/6/5.
 */

public class Looper {

    static final ThreadLocal<Looper> sThreadLocal =  new ThreadLocal<>();
    public MessageQueue messageQueue;
    private Looper(){
        messageQueue = new MessageQueue();
    }

    public static  Looper getMainLooper(){
        return sThreadLocal.get();
    }

    /**
     *   为调用此函数的线程 准备looper
     *
     */
    public static void prepare(){
        if(null != sThreadLocal.get()){
            throw new RuntimeException(Thread.currentThread()+"已经有了looper");
        }
        sThreadLocal.set(new Looper());
    }

    /**
     *  循环从消息队列中取出消息
     */
    public static  void loop(){

        Looper looper = Looper.getMainLooper();
        MessageQueue queue = looper.messageQueue;
        for(;;){
           Message message =  queue.next();
           System.out.println("message === " + message);
           if(message == null){
               break;
            }
           message.target.handleMessage(message);
        }
    }

    public void quite(){
        messageQueue.quite();
    }
}
