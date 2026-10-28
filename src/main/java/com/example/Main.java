package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    
    public static Map<String,Integer> disponibilita = new HashMap<>();
    
    public static void main(String[] args) throws IOException {
        List<String> usernames = new ArrayList<>();
        
        System.out.println("SERVER UP");
        disponibilita.put("Gold", 10);
        disponibilita.put("Pit", 30);
        disponibilita.put("Parterre", 60);

        ServerSocket ss = new ServerSocket(3000);
        Socket s = ss.accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);

        out.println("WELCOME");
        usernames.add("bbb");

        String user;
        while(true){
            user = in.readLine();
            String[] spl = user.split(" ",3);
            if(spl.length == 2 && spl[0].equals("LOGIN")){
                if(!usernames.contains(spl[1])){
                    user = spl[1];
                    usernames.add(user);
                    break;
                }
                out.println("ERR USERINUSE");
            }else{
                out.println("ERR UNKNOWNCMD");
            }
        }
        out.println("OK");
        
        boolean quit = false;
        while(!quit){
            String cmd = in.readLine();
            switch (cmd){
                case "N":
                    String avail = "AVAIL";
                    for(String key : disponibilita.keySet()) avail = avail.concat(" " + key + ":" + disponibilita.get(key));
                    out.println(avail);
                    break;
                case "BUY":
                    String[] type = in.readLine().split(" ");
                    try{
                        if(type.length == 2){
                            int amtreq = Integer.parseInt(type[1]);
                            if(disponibilita.containsKey(type[0])){
                                Integer amt = disponibilita.get(type[0]);
                                if(amt == 0 || amtreq > amt){ out.println("KO"); break; }
                                amt = amt.intValue() - amtreq;
                                disponibilita.put(type[0], amt);
                                out.println("OK");
                            }else out.println("ERR UNKNOWNTYPE");
                        }else out.println("ERR SYNTAX");
                    } catch(NumberFormatException e){ 
                        out.println("ERR SYNTAX"); 
                        break; 
                    }
                    break;
                case "QUIT":
                    out.println("BYE");
                    quit = true;
                    ss.close();
                    break;
                default:
                    break;
            }
        }
    }
}