package com.example.s157006.rescuerobot.Networking;

/**
 * Created by s157006 on 15-10-2017.
 *
 */

public interface IConnectionHandler {
    void onMessage(String msg);
    String getNextMessage();
    void onConnectionStatusChange(ConnectionStatus status);

    void addToSendQueue(String msg);
}
