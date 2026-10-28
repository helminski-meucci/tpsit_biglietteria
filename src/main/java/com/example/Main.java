package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    
    static Map<String,Integer> disponibilita = new HashMap<>();
    static List<String> usernames = new ArrayList<>();
    static List<Vendita> sessions = new ArrayList<>();
    
    public static void main(String[] args) throws IOException {
        System.out.println("SERVER UP");

        disponibilita.put("Gold", 10);
        disponibilita.put("Pit", 30);
        disponibilita.put("Parterre", 60);

        ServerSocket ss = new ServerSocket(3000);
        while(true){
            Socket s = ss.accept();
            Vendita v = new Vendita(s, disponibilita, usernames, sessions);
            v.start();

            sessions.add(v);
        }
    }

    public static void notifyOutOfStock(Vendita causeSession, String key) throws IOException{
        for(Vendita v : sessions){
            if(!v.equals(causeSession)) v.print("NOTIFY SoldOut " + key);
        }
    }
}