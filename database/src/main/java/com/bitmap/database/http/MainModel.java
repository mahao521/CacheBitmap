package com.bitmap.database.http;

import com.bitmap.database.bean.ChannelListData;
import com.bitmap.database.bean.VideoListData;
import com.bitmap.database.utils.GsonUtil;
import com.bitmap.database.utils.function.HttpFunction;

import io.reactivex.Observable;
import io.reactivex.Observer;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/7/10.
 */

public class MainModel extends BaseModel{

    public static MainModel getInstance(){
        return getPresent(MainModel.class);
    }

    public void executeChannelList(Observer<ChannelListData> observer){
        addParamsMap(getCommonMap());
        RequestBody requestBody = RequestBody.create(JSON, GsonUtil.mapToJson(mMap));
        Observable observable = mServertApi.getChannel(requestBody).map(new HttpFunction());
        toSubscribe(observable,observer);
    }

    public void executeVideoList(String videoChannelId, String backData, String direction, Observer<VideoListData> listDataObserver){
        addParams("videoChannelId", videoChannelId);
        addParams("backdata", backData);
        addParams("direction", direction);
        RequestBody requestBody = RequestBody.create(JSON,GsonUtil.mapToJson(mMap));
        Observable observable = mServertApi.getChannel(requestBody).map(new HttpFunction());
        toSubscribe(observable,listDataObserver);
    }


}
