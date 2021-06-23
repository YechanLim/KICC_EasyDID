package com.example.easydid.model;

import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EasyDidModel extends RealmObject {

    @PrimaryKey
    @SerializedName("order_num")
    private String order_num;

    @SerializedName("call_state")
    private String call_state;

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getCall_state() {
        return call_state;
    }

    public void setCall_state(String call_state) {
        this.call_state = call_state;
    }
}
