package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class Vendita extends Thread {
    private Socket s;
    private Map<String, Integer> disponibilita;
    private List<String> usernames;
    private List<Vendita> sessions;

    public Vendita(Socket s, Map<String, Integer> disponibilita, List<String> usernames, List<Vendita> sessions){
        this.s = s;
        this.disponibilita = disponibilita;
        this.usernames = usernames;
        this.sessions = sessions;
    }

    public void print(String message) throws IOException {
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        out.println(message);
    }
    
    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);

            out.println("WELCOME");

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
                                    if(amt == 0) Main.notifyOutOfStock(this, type[0]);
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
                        usernames.remove(user);
                        sessions.remove(this);
                        s.close();
                        break;
                    default:
                        break;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
