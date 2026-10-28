package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Vendita extends Thread{
    private Socket s;
    
    public Vendita(Socket s){
        this.s = s;
    }

    public void run(){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);

            out.println("WELCOME");
            
        } catch (IOException e) { throw new RuntimeException(e); }

    }
}
