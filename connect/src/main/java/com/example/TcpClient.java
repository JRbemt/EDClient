package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class TcpClient {

    public static final String LAN_IP  = "192.168.1.1";
    public static final String WIFI_IP = "192.168.51.40";
    public static final int PORT = 2001;

    private final static int TIMEOUT = 10000;

    private Socket skt;
    private BufferedReader bufferIn;
    private PrintWriter bufferOut;

    public final String ip;
    public final int port;

    public TcpClient(String ip, int port){
       this.ip = ip;
       this.port = port;
    }


    public TcpClient(String ip){
        this(ip, TcpClient.PORT);
    }

    public void connect(){
        try {
            skt = new Socket();
            skt.connect(new InetSocketAddress(ip, port), TIMEOUT);

            bufferIn  = new BufferedReader(new InputStreamReader(skt.getInputStream()));
            bufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(skt.getOutputStream())), true);
        } catch(Exception e) {
            e.printStackTrace();
            skt = null;
        }
    }

    public boolean connectionEstablished(){
        return skt != null && skt.isConnected();
    }



    public static void main(String[] args){
        test2();
    }

    private static void test1(){
        TcpClient mc = new TcpClient(TcpClient.WIFI_IP);
        mc.connect();

        Scanner scanner = new Scanner(System.in);

        String cmd = "";

        while (!cmd.equals("exit")){
            System.out.print("Input command: ");
            cmd = scanner.nextLine();

            if (cmd.equals("listen")){
                System.out.println(mc.listen());
                cmd = "";
            } else{
                mc.send(cmd);
            }
        }
        mc.close();
    }

    private static void test2(){
        TcpClient mc = new TcpClient(TcpClient.LAN_IP);
        mc.connect();

        final String HEARTBEAT = "_";
        boolean heartBeatReceived = true;

        boolean led_on = true;

        while (true) {
            ArrayList<String> ins = mc.listen();
            for (String in : ins) {
                if (!in.equals("")) {
                    if (in.equals(HEARTBEAT)) {
                        heartBeatReceived = true;
                    } else {
                        System.out.println("in -> " + in);
                    }
                }
            }
            if (heartBeatReceived) {
                String out = "#l@"+(led_on ? "1" : "0")+"@0!";
                led_on = !led_on;
                if (out != null) {
                    System.out.println("out -> " + out);
                    mc.send(out);
                    heartBeatReceived = false;
                }
            }
        }
    }

    public Socket getSocket() {
        return skt;
    }


    public void send(String str){
        if (bufferOut != null && !bufferOut.checkError()) {
            bufferOut.println(str);    // important that we end each msg with \n
            bufferOut.flush();
        }
    }

    public ArrayList<String> listen(){
        ArrayList<String> lines = new ArrayList<>();
        if (bufferIn != null) {
            try {
                String line;
                while (bufferIn.ready() && (line = bufferIn.readLine()) != null) { // check if inputStream has bytes available
                    lines.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lines;
    }



    public void close(){
        //TODO can this give an exception if the skt is closed??
        if (bufferOut != null){
            bufferOut.flush();
            bufferOut.close();
        }
        try {
            if (skt != null)
                skt.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        try {
            if (bufferIn != null)
                bufferIn.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        bufferOut = null;
        bufferIn = null;
        skt = null;
    }


}
