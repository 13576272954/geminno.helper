package com.example.administrator.helper.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.administrator.helper.entity.Task;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dliu on 2016/10/10.
 */
public class ParcebleMap implements Parcelable {


    public Map<Task, Integer> getMap() {
        return map;
    }

    public void setMap(Map<Task, Integer> map) {
        this.map = map;
    }

    private Map<Task,Integer> map;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.map.size());
        for (Map.Entry<Task, Integer> entry : this.map.entrySet()) {
            dest.writeParcelable(entry.getKey(), flags);
            dest.writeValue(entry.getValue());
        }
    }

    public ParcebleMap() {
    }

    protected ParcebleMap(Parcel in) {
        int mapSize = in.readInt();
        this.map = new HashMap<Task, Integer>(mapSize);
        for (int i = 0; i < mapSize; i++) {
           Task key = in.readParcelable(Task.class.getClassLoader());
            Integer value = (Integer) in.readValue(Integer.class.getClassLoader());
            this.map.put(key, value);
        }
    }

    public static final Creator<ParcebleMap> CREATOR = new Creator<ParcebleMap>() {
        @Override
        public ParcebleMap createFromParcel(Parcel source) {
            return new ParcebleMap(source);
        }

        @Override
        public ParcebleMap[] newArray(int size) {
            return new ParcebleMap[size];
        }
    };
}
