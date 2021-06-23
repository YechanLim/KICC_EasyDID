package com.example.easydid.util;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import com.example.easydid.EasyDidApplication;
import com.example.easydid.model.EasyDidModel;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class EasySocketServerAsync extends AsyncTask<Handler, Integer, Map<String, Object>> {

    protected SharedPreferences prefs = EasyDidApplication.getInstance().getPrefs();
    protected int serverPort;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;

    @Override
    protected Map<String, Object> doInBackground(Handler... handlers) {
        for (Handler handler : handlers) {

            if(prefs.contains("pref_key_easydid_network_server_port")){
                serverPort = Integer.parseInt(prefs.getString("pref_key_easydid_network_server_port", "0"));
            }
            else
            {
                return null;
            }

            //서버 포트 열기
            try {
                serverSocket = new ServerSocket(serverPort);
            } catch (IOException e) {
                throw new RuntimeException("Cannot open port " + serverPort, e);
            }

            while (!isStopped()) {
                Socket clientSocket = null;
                try {
                    clientSocket = serverSocket.accept();
                } catch (IOException e) {
                    if (isStopped()) {
                        return null;
                    }
                    throw new RuntimeException("Error accepting client connection", e);
                }
                new Thread(new EasySocketServerWorkerRunnable(clientSocket, handler, "Multithreaded Server")
                ).start();
            }

        }
        return null;
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        if(serverSocket != null) {
            try {
                serverSocket.close();
                if (serverSocket.isClosed()) {
                    cancel(true);
                }
            } catch (IOException e) {
                throw new RuntimeException("Error closing server", e);

            }
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
