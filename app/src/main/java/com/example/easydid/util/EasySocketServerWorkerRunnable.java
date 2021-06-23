package com.example.easydid.util;

import com.example.easydid.model.EasyDidModel;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import android.os.Handler;
import android.os.Message;

import io.realm.Realm;

public class EasySocketServerWorkerRunnable implements Runnable {

    private static final String TAG = "EmenuOrderSocketServerWokerRunnable";

    protected Socket clientSocket = null;
    protected Handler handler = null;
    protected String serverText = null;
    Realm realm = Realm.getDefaultInstance();

    public EasySocketServerWorkerRunnable(Socket clientSocket, Handler handler, String serverText) {
        this.clientSocket = clientSocket;
        this.handler = handler;
        this.serverText = serverText;
    }

    @Override
    public void run() {

        try {
            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            PrintWriter out = new PrintWriter(output);

            String strMsg = in.readLine();
            System.out.println(strMsg);

            Gson gson = new Gson();
            EasyDidModel rcvData = gson.fromJson(strMsg, EasyDidModel.class);

            if (rcvData != null) {
                Message msg = handler.obtainMessage();
                msg.what = 0;
                msg.obj = rcvData;
                handler.sendMessage(msg);
            }

            System.out.println("수신 완료");

            out.println("수신 완료");
            out.flush();
            output.close();
            input.close();
            clientSocket.close();

        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }

}
