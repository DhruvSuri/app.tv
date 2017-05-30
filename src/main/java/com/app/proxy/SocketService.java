package com.app.proxy;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by dhruv.suri on 11/05/17.
 */
@Service
public class SocketService {
    final Queue<ServerThread> queue = new LinkedList<>();
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
                        queue.add(st);
                        System.out.println("New connection Established.Total : " + queue.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }.start();


    }

    public void cleanConnectionPool() {
        System.out.println("Cleaning : " + queue.size());
        int size = queue.size();
        int i = 0;
        while (i < size) {
            ServerThread serverThread = queue.poll();
            if (serverThread.isSocketConnected()) {
                queue.add(serverThread);
            }
            i++;
        }
        System.out.println("Cleaned : " + queue.size());
    }

    public String sendProxyRequest(String url) {
        if (queue.size() == 0) {
            System.out.println("No Connections available..!!");
            return null;
        }

        while (queue.size() > 0) {
            System.out.println(queue.size());
            ServerThread serverThread = queue.poll();


            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<String> future = executor.submit(new Callable() {

                public String call() throws Exception {
                    System.out.println("SENDING TO: " + serverThread.hashCode());
                    String response = serverThread.sendRequest(url);
                    if (response != null){
                        queue.add(serverThread);
                        return response;
                    }
                    return null;
                }
            });
            try {
                //TODO orphan threads kill connection

                //Decide timeout from heuristics
                System.out.println(future.get(3, TimeUnit.SECONDS)); //timeout is in 2 seconds
            } catch (TimeoutException e) {
                System.err.println("Timeout");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            executor.shutdownNow();
        }


        return null;
    }


    public static void main(String args[]){

    }

}


