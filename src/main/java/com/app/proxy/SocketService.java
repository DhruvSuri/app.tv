package com.app.proxy;

import com.app.Redis.RedisFactory;
import com.app.proxy.Beans.ProxyResponse;
import com.app.utils.AzazteUtils;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(SocketService.class);
    final Queue<ServerThread> queue = new LinkedList<>();
    private final int DefaultPort = 12345;
    private Socket socket = null;
    private ServerSocket serverSocket = null;


    public SocketService() {
        log.debug("Server Listening......");
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
                        log.debug("New connection Established.Total : " + queue.size());
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

        while (true) {
            if (queue.size() == 0) {
                return "No Connections available..!! Please contact support";
            }

            log.debug("Queue size : " + queue.size());


            ServerThread serverThread = queue.poll();


            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<String> future = executor.submit(new Callable() {

                public String call() throws Exception {
                    System.out.println("SENDING TO: " + serverThread.hashCode());
                    return serverThread.sendRequest(url);
                }
            });
            try {
                String response = future.get(4, TimeUnit.SECONDS);
                if (response != null) {
                    queue.add(serverThread);
//                    ProxyResponse proxyResponse = AzazteUtils.fromJson(response, ProxyResponse.class);
//                    System.out.println("Response entity : name : " + proxyResponse.getName() + " " + proxyResponse.getNumber() + " " + proxyResponse.getDataUsage());
                    RedisFactory.proxy(response);
                    return response;
                }
                serverThread.close();
                System.out.println("Retrying .... Null Response from API");
                log.debug("Retrying .... Null Response from API");
            } catch (TimeoutException e) {
                //TODO might be due to blockage
                System.err.println("Thread timed out.Removing from queue and Retrying");
                log.debug("Thread timed out.Removing from queue and Retrying");
                serverThread.close(); // Killing orphan threads
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            executor.shutdownNow();
        }
    }

    public void checkPoolHealth() {
        if (queue.size() < 5) {
            log.debug("[Alert] Pool status : Red..." + queue.size());
            System.out.println("[Alert] Pool status : Red..." + queue.size());
        }
    }
}


