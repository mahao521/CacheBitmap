package com.bitmap.database.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/7/3.
 */

public class CategoryInfo implements Parcelable {

    public String id;
    public String name;
    public String description;
    public int columns;
    public int type;
    public int cornermark;
    public int showtype;
    public long end_at;

    public CategoryInfo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    protected CategoryInfo(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        columns = in.readInt();
        type = in.readInt();
        cornermark = in.readInt();
        showtype = in.readInt();
        end_at = in.readLong();
    }

    public static final Creator<CategoryInfo> CREATOR = new Creator<CategoryInfo>() {
        @Override
        public CategoryInfo createFromParcel(Parcel in) {
            return new CategoryInfo(in);
        }

        @Override
        public CategoryInfo[] newArray(int size) {
            return new CategoryInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(columns);
        dest.writeInt(type);
        dest.writeInt(cornermark);
        dest.writeInt(showtype);
        dest.writeLong(end_at);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCornermark() {
        return cornermark;
    }

    public void setCornermark(int cornermark) {
        this.cornermark = cornermark;
    }

    public int getShowtype() {
        return showtype;
    }

    public void setShowtype(int showtype) {
        this.showtype = showtype;
    }

    public long getEnd_at() {
        return end_at;
    }

    public void setEnd_at(long end_at) {
        this.end_at = end_at;
    }
}
