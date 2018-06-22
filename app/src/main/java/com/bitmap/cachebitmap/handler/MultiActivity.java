package com.bitmap.cachebitmap.handler;

import android.app.Instrumentation;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bitmap.cachebitmap.R;

import java.lang.reflect.Field;

//监听消息队列，是否还有消息
public class MultiActivity extends AppCompatActivity {

    private static final String TAG = "MultiActivity";
    private SingleMoudle mSingleMoudle;
    private Button mBtnDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi);
        mSingleMoudle = new SingleMoudle();
        mBtnDown = findViewById(R.id.btn_down);
    }

    public void onClick(View view) {
        mBtnDown.setText("下载");
        mSingleMoudle.httpDown();
    }

    public void updateBtn(){
        Log.i(TAG,"updatebtn");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBtnDown.setText("12");
            }
        });
    }

    class SingleMoudle implements MessageQueue.IdleHandler{

        private static final String TAG = "SingleMoudle";
        private  Handler mHandler;
        public SingleMoudle(){
            HandlerThread handlerThread = new HandlerThread(TAG);
            handlerThread.start();
            Looper looper = handlerThread.getLooper();
            MessageQueue queue = null;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                queue = looper.getQueue();
            }else {
                try {
                    Field queueField = looper.getClass().getDeclaredField("mQueue");
                    queueField.setAccessible(true);
                    queue = (MessageQueue) queueField.get(looper);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            queue.addIdleHandler(this);
            mHandler = new Handler(looper){
                @Override
                public void handleMessage(Message msg) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        }

        /**
         *    true   持续监听，消息池是否有消息
         *    false  只监听一次。
         * @return
         */
        @Override
        public boolean queueIdle() {
            updateBtn();
            return true;
        }

        public void httpDown(){
            Message message = Message.obtain();
            message.what = 1;
            message.obj = "1234";
            mHandler.sendMessage(message);
        }
    }
}
