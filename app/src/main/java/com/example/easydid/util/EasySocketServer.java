package com.example.easydid.util;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import android.os.Handler;

public class EasySocketServer implements Runnable {

    private static final String TAG = "EmenuOrderSocketServer";

    protected int serverPort = 8888;
    protected ServerSocket serverSocket = null;
    protected Handler handler = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;

    public EasySocketServer(int port, Handler handler) {
        this.serverPort = port;
        this.handler = handler;
    }

    @Override
    public void run() {
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(!isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            new Thread(new EasySocketServerWorkerRunnable(clientSocket, handler,"Multithreaded Server")
            ).start();
        }
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + serverPort , e);
        }
    }

}
