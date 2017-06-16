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
                log.debug("Starting server");
                Configuration config = new Configuration();
                config.setPingInterval(2000);
                config.setPingTimeout(2500);
                config.setPort(DefaultPort);

                server = new SocketIOServer(config);

                server.addConnectListener(new ConnectListener() {
                    @Override
                    public void onConnect(SocketIOClient socketIOClient) {
                        log.debug("Connected socket : " + socketIOClient.getSessionId());
                        log.debug("Total Sockets available : " + server.getAllClients().size());
                    }
                });

                server.addDisconnectListener(new DisconnectListener() {
                    @Override
                    public void onDisconnect(SocketIOClient socketIOClient) {
                        log.debug("Disconnected socket : " + socketIOClient.getSessionId());
                        log.debug("Total Sockets available : " + server.getAllClients().size());
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

            Collection<SocketIOClient> clients = server.getAllClients();
            int size = clients.size();

            if (size == 0) {
                return "No active connections available";
            }

            final ArrayList<String> response = new ArrayList<String>();
            SocketIOClient client = clients.iterator().next();

            client.sendEvent(DefaultEvent, new AckCallback<String>(String.class) {
                @Override
                public void onSuccess(String result) {
                    System.out.println("Response from client: " + client.getSessionId() + " data: " + result);
                    response.add(result);
                    synchronized (this){
                        this.notify();
                    }
                }

                @Override
                public void onTimeout(){
                    System.out.println("Timed out");
                    synchronized (this){
                        this.notify();
                    }
                }
            }, url);

            synchronized (this){
                try {
                    System.out.println("Waiting - ");
                    this.wait(2000);
                    System.out.println("Notified - ");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (response.size() == 0){
                continue;
            }

            if (response.size() > 1){
                log.debug("Horrible ... !! How can number of responses go above 1... Gandu coding skills");
                System.out.println("Horrible ... !! How can number of responses go above 1... Gandu coding skills");
            }

            return response.get(0);
        }

    }
}


