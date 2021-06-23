package com.example.easydid.ui.fragment;

import android.content.SharedPreferences;
import android.os.Environment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.easydid.EasyDidApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class EasyVideoViewModel extends ViewModel {

    protected SharedPreferences prefs = EasyDidApplication.getInstance().getPrefs();

    public EasyVideoViewModel(){
        initialize();
    }

    private void initialize(){
    }

}
