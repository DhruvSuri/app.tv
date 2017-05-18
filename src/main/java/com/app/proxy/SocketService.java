package com.app.proxy;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dhruv.suri on 11/05/17.
 */
@Service
public class SocketService {
    final List<ServerThread> list = new ArrayList();
    private final int DefaultPort = 12345;
    private Socket socket = null;
    private ServerSocket serverSocket = null;


    public SocketService() {
        System.out.println("Server Listening......");
        try {
            serverSocket = new ServerSocket(DefaultPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread() {

            public void run() {
                while (true) {
                    try {
                        socket = serverSocket.accept();
                        ServerThread st = new ServerThread(socket);
                        list.add(st);
                        System.out.println("New connection Established.Total : " + list.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }.start();


    }

    public String sendProxyRequest(String url) {
        ServerThread temp = list.get(DateTime.now().getMillisOfSecond() % list.size());
        if (temp.isSocketConnected()) {
            System.out.println("SENDING TO: " + temp.hashCode());
            return temp.sendRequest(url);
        }
        return null;
    }

}


