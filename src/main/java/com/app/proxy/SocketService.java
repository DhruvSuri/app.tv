package com.app.proxy;

import com.app.Redis.RedisFactory;
import com.app.proxy.Beans.ProxyResponse;
import com.app.proxy.SocketIO.ChatObject;
import com.app.utils.AzazteUtils;
import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
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
    private final int DefaultPort = 9000;
    private final String DefaultEvent = "proxy";
    private static SocketIOServer server;


    public SocketService() {
        new Thread() {

            public void run() {
                Configuration config = new Configuration();
                config.setPingInterval(2000);
                config.setPingTimeout(2500);
                config.setPort(DefaultPort);

                server = new SocketIOServer(config);

                server.addEventListener(DefaultEvent, String.class, new DataListener<String>() {
                    @Override
                    public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
                        System.out.println("Received by server : " + data);
                        client.sendEvent(DefaultEvent, "Received by server : " + data);
                    }
                });


                server.addConnectListener(new ConnectListener() {
                    @Override
                    public void onConnect(SocketIOClient socketIOClient) {
                        System.out.println("Connected socket : " + socketIOClient.getSessionId());
                        System.out.println("Total Sockets available : " + server.getAllClients().size());
                    }
                });

                server.addDisconnectListener(new DisconnectListener() {
                    @Override
                    public void onDisconnect(SocketIOClient socketIOClient) {
                        System.out.println("disconnected socket : " + socketIOClient.getSessionId());
                        System.out.println("Total Sockets available : " + server.getAllClients().size());
                    }
                });

                server.start();

                try {
                    Thread.sleep(Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                server.stop();
            }
        }.start();


    }

    public String sendProxyRequest(String url) {

        while (true) {

            List<SocketIOClient> clients = (List<SocketIOClient>) server.getAllClients();
            int size = clients.size();

//            if (size == 0) {
//                return "No active connections available";
//            }

            final String[] response = new String[1];
            SocketIOClient client = clients.get((int) (Math.random() % clients.size()));

//        client.sendEvent(DefaultEvent, url);

            client.sendEvent(DefaultEvent, new AckCallback<String>(String.class) {
                @Override
                public void onSuccess(String result) {
                    System.out.println("Response from client: " + client.getSessionId() + " data: " + result);
                    response[0] = result;
                    synchronized (this){
                        this.notify();
                    }
                }

                @Override
                public void onTimeout() {
                    super.onTimeout();
                    synchronized (this){
                        this.notify();
                    }
                }
            }, url);

            try {
                synchronized (this){
                    this.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (response.length == 0){
                System.out.println("Retrying ...");
                continue;
            }

            return response[0];
        }

    }
}


