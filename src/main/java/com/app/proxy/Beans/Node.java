package com.app.proxy.Beans;

import com.corundumstudio.socketio.SocketIOClient;

import java.util.UUID;

/**
 * Created by dhruv.suri on 22/06/17.
 */
public class Node {

    private Profile profile;
    private UUID sessionID;
    private SocketIOClient client;

    public Node(Profile profile, SocketIOClient client) {
        this.profile = profile;
        this.client = client;
    }

    public Node(SocketIOClient client) {
        this.client = client;
        this.sessionID = client.getSessionId();
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public UUID getSessionID() {
        return sessionID;
    }

    public void setSessionID(UUID sessionID) {
        this.sessionID = sessionID;
    }

    public SocketIOClient getClient() {
        return client;
    }

    public void setClient(SocketIOClient client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Node{" +
                "profile=" + profile +
                ", sessionID=" + sessionID +
                '}';
    }
}
