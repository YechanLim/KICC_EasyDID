package com.example.easydid;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.lifecycle.MutableLiveData;

import com.example.easydid.model.EasyDidModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class EasyDidApplication extends Application {

    public static Context mContext;
    private static EasyDidApplication mInstance;
    private SharedPreferences prefs;
    private List<String> cardList = new ArrayList<>();

    public static EasyDidApplication get(Activity activity) {
        return (EasyDidApplication)activity.getApplication();
    }

    public static EasyDidApplication getInstance() {
        return mInstance;
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }

    public List<String> getCardList() {return cardList;}

    public void setCardList(List<String> inCardList){
        if(inCardList != null)
        {
            cardList = inCardList;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("EasyDidDB.realm").build();

        Realm.setDefaultConfiguration(config);

        mInstance = this;

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }
}

