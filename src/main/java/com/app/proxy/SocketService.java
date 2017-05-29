package com.app.proxy;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by dhruv.suri on 11/05/17.
 */
@Service
public class SocketService {
    final Queue<ServerThread> list = new LinkedList<>();
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

    public void cleanConnectionPool() {
        int size = list.size();
        int i = 0;
        while (i < size) {
            ServerThread thread = list.poll();
            if (thread.isSocketConnected()) {
                list.add(thread);
            }
            i++;
        }
    }

    public String sendProxyRequest(String url) {
        if (list.size() == 0) {
            System.out.println("No Connections available..!!");
            return null;
        }


        ServerThread serverThread = list.poll();
        if (serverThread.isSocketConnected()) {
            System.out.println("SENDING TO: " + serverThread.hashCode());
            list.add(serverThread);
            return serverThread.sendRequest(url);
        } else {
            list.remove(serverThread);
        }
        return null;
    }

}


