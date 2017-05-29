package com.app.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by dhruv.suri on 13/05/17.
 */
public class ServerThread {
    private Socket socket = null;
    private BufferedReader inputStream = null;
    private PrintWriter outputStream = null;

    public ServerThread(Socket s) throws IOException {
        this.socket = s;
        inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputStream = new PrintWriter(socket.getOutputStream());
        socket.setKeepAlive(true);
    }

    public boolean isSocketConnected() {
        try {
            if (this.socket.getInetAddress().isReachable(1000)){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String sendRequest(String content) {
        if (content != null) {
            outputStream.println(content);
            outputStream.flush();
            try {
                return inputStream.readLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}

