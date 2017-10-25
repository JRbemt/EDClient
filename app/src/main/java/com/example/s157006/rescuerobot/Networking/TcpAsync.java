package com.example.s157006.rescuerobot.Networking;

import android.os.AsyncTask;
import android.util.Log;

import com.example.TcpClient;

/**
 * Created by s157006 on 3-10-2017.
 *
 */

public class TcpAsync extends AsyncTask<Void, Void, Void> {

    private IConnectionHandler listener;
    private final static String TAG = "TcpClient";
    private final TcpClient client;

    public TcpAsync(TcpClient client, IConnectionHandler listener){
        this.listener = listener;
        this.client = client;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private final String HEARTBEAT = "_";
    private final long INTERVAL = 10000;           // send a heartbeat every 10 seconds
    private final long TIMEOUT = 10000;             // timeout after 2 seconds without response
    private boolean heartBeatReceived  = true;
    private long timeLastHeartbeatSend = 0;



    @Override
    protected Void doInBackground(Void... params) {
        connect();

        //TODO notice disconnect and try reconnecting
        while (!isCancelled()) {
            try {
                for(String in : client.listen()) {
                    if (!in.equals("")) {
                        if (in.equals(HEARTBEAT)) {
                            heartBeatReceived = true;
                            Log.i(TAG,"Heartbeat received, time: "+(System.currentTimeMillis() - timeLastHeartbeatSend)+" ms");
                        } else {
                            Log.i(TAG, "in -> " + in);
                            listener.onMessage(in);
                        }
                    }
                }
                if (timeLastHeartbeatSend == 0) {
                    sendHeartbeat();
                } else {
                    if (heartBeatReceived) {
                        // Heartbeat ontvangen, kijk of we de volgende kunnen sturen
                        if ((System.currentTimeMillis() - timeLastHeartbeatSend) > INTERVAL) {
                            sendHeartbeat();
                        }
                    } else {
                        // Heartbeat niet ontvangen, kijk of hij
                        if ((System.currentTimeMillis() - timeLastHeartbeatSend) > TIMEOUT) {
                            timeLastHeartbeatSend = 0;
                            throw new RuntimeException("Connection timed out: heartbeat not received");
                        }
                    }
                }

                if (heartBeatReceived) {
                    // make sure outgoing commands do not interrupt the heartbeat
                    String out = listener.getNextMessage();
                    if (out != null) {
                        Log.i(TAG, "out -> " + out);
                        client.send(out);
                    }
                }
            } catch (Exception ex){
                ex.printStackTrace();
                connect();
            }
        }
        Log.i(TAG,"disconnected to : "+client.ip+":"+client.port);
        client.close();
        return null;
    }

    private void sendHeartbeat(){
        client.send(HEARTBEAT);
        timeLastHeartbeatSend = System.currentTimeMillis();
        heartBeatReceived = false;
        Log.i(TAG,"Heartbeat send");
    }

    private void connect(){
        boolean connected = false;
        Log.i(TAG,"trying to connect to :"+client.ip+":"+client.port);
        listener.onConnectionStatusChange(ConnectionStatus.CONNECTING);
        while (!connected){
            client.close();
            if (isCancelled()){
                listener.onConnectionStatusChange(ConnectionStatus.OFFLINE);
                return;
            }
            client.connect();
            connected = client.connectionEstablished();
        }
        Log.i(TAG,"connected to : "+client.ip+":"+client.port);
        listener.onConnectionStatusChange(ConnectionStatus.ONLINE);
    }
}
