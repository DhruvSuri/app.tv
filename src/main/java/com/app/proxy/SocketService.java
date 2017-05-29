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
    final Queue<ServerThread> queue = new LinkedList<>();
    final List<ServerThread> list = new ArrayList<>();
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
        for (ServerThread serverThread : list) {
            if (serverThread.isSocketConnected()){
                queue.add(serverThread);
            }
        }
    }

    public String sendProxyRequest(String url) {
        if (list.size() == 0) {
            System.out.println("No Connections available..!!");
            return null;
        }

        
        ServerThread serverThread = queue.poll();
        if (serverThread.isSocketConnected()) {
            System.out.println("SENDING TO: " + serverThread.hashCode());
            return serverThread.sendRequest(url);
        } else {
            list.remove(serverThread);
        }
        return null;
    }

}


