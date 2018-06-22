package com.bitmap.mylibrary;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Administrator on 2018/6/5.
 */

public class MessageQueue {

    private boolean isQuit = false;
    private Message mMessage;
    public Message next(){
        synchronized (this){
            Message message;
            System.out.println("mes = " + mMessage);
            for(;;){
                if(isQuit){
                    return null;
                }
                message = this.mMessage;
                if(message != null){
                    break;
                }
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //将链表指向下一个节点
            mMessage = mMessage.next;
            System.out.println("next = " + message);
            return message;
        }
    }

    public void enqueMessage(Message message){
        synchronized (this){
            if(isQuit){
                return ;
            }
            Message messsPrv = mMessage;
            if(messsPrv == null){
                this.mMessage = message;
                System.out.println("next = " + 1);
            }else {
                //循环将message插入到链表尾部
                Message prev;
                for(;;){
                    prev = messsPrv;
                    messsPrv = messsPrv.next;
                    if(messsPrv == null){
                        break;
                    }
                }
                prev.next = message;
                System.out.print("next = " + 2);
            }
            //通知 获取message解除阻塞
            notify();
        }
    }

    public void quite(){
        synchronized (this){
            isQuit = true;
            Message message = this.mMessage;
            while (null != message){
                //获取下一个message
                Message next = message.next;
                //当前message清空
                message.recycle();
                message = next;
                System.out.println("msg quite = " + message);
            }
            notify();
        }
    }
}
