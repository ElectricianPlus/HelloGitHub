package com.electrician;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatClientReaderThread extends Thread {
    private Socket socket;
    public ChatClientReaderThread(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream ps = new PrintStream("chatserver\\User\\chatNotes.txt");
            String msg;
            while ((msg = br.readLine())!=null){
                time();
                System.out.println("你收到消息: " + msg);
                ps.println(msg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void time(){
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String s = sf.format(date);
        System.out.println(s);
    }
}
