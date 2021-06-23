package com.example.easydid.ui.activity;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.easydid.EasyDidApplication;
import com.example.easydid.model.EasyDidModel;
import com.example.easydid.util.EasySocketServerAsync;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Predicate;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class EasyDidViewModel extends ViewModel {

    private SharedPreferences prefs = EasyDidApplication.getInstance().getPrefs();
    private Integer RcvOrderNumlimit;
    private vmHandler vmHandler = new vmHandler();
    Timer orderRmvTimer = new Timer();
    long rmvTimeCycle = 5*60000;

    //대기 리스트
    List<String> cardList;
    //삭제할 리스트
    List<String> rmvCardList = new ArrayList<>();
    MutableLiveData<List<String>> mCardList = new MutableLiveData<>();

    //서버 쓰레드
    EasySocketServerAsync easySocketServer = new EasySocketServerAsync();

    Integer tmpNum = 0;

    public EasyDidViewModel() {
        InitPrefs();
        cardList = EasyDidApplication.getInstance().getCardList();
        if(cardList.size() > RcvOrderNumlimit && cardList.size() > 0){
            int rmvCnt = cardList.size() - RcvOrderNumlimit;
            for(int i = 0; i < rmvCnt; i++)
            {
                cardList.remove(cardList.size()-1);
            }
        }
        mCardList.setValue(cardList);
        easySocketServer.execute(vmHandler);
    }

    private void InitPrefs() {
        if(prefs.contains("pref_key_easydid_rcv_order_num_limit"))
        {
            RcvOrderNumlimit = Integer.parseInt(prefs.getString("pref_key_easydid_rcv_order_num_limit", "7"));
        }
        else
            RcvOrderNumlimit = 7;

        if(prefs.contains("pref_key_easydid_call_num_rmv_time"))
        {
            rmvTimeCycle = Long.parseLong(prefs.getString("pref_key_easydid_call_num_rmv_time", "5"))*6000;
        }
        else
            rmvTimeCycle = 5*6000;
    }

    public void addCard() {
        cardList.add(0, String.valueOf(tmpNum));

        if(cardList.size() > RcvOrderNumlimit)
            cardList.remove(cardList.size()-1);

        mCardList.setValue(cardList);

        tmpNum++;
    }

    public void removeCard() {
        if(cardList.size() > 0) {
            cardList.remove(cardList.size() - 1);
            mCardList.setValue(cardList);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        easySocketServer.stop();
    }

    protected class vmHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            EasyDidModel rcvOrder = (EasyDidModel) msg.obj;

            String callState = rcvOrder.getCall_state();
            String orderNum = rcvOrder.getOrder_num();

            if(callState.equals("R")) {
                //중복체크
                if(cardList.contains(orderNum)) return;
                cardList.add(0, orderNum);

                if(cardList.size() > RcvOrderNumlimit)
                    cardList.remove(cardList.size()-1);

                mCardList.setValue(cardList);
            }
            else if(callState.equals("C")){
                String rmvCard = "";
                if(cardList.size() > 0) {
                    for(String card: cardList)
                    {
                        if(card.equals(orderNum))
                        {
                            rmvCard = card;
                        }
                    }

                    if (!rmvCard.isEmpty())
                    {
                     cardList.remove(rmvCard);
                    }
                    else return;
                    mCardList.setValue(cardList);
                }
            }

            super.handleMessage(msg);
        }
    }

}
