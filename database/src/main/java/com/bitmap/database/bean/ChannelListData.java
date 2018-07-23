package com.bitmap.database.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/5.
 */

public class ChannelListData {

    private List<ChannelListItem> mChannelList;

    public class ChannelListItem{

        private String name;
        private int id;

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }

    public List<ChannelListItem> getChannelList() {
        return mChannelList;
    }
}
