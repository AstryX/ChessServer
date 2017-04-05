package datahandler;


import java.net.*;
import java.io.*;
import java.util.*;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author rdereskevicius
 */
public class CHESSsocket {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    public static List<LobbyPublic> publicLobbyData;
    public static List<LobbyPrivate> privateLobbyData;
    
    public CHESSsocket(){
        publicLobbyData = new ArrayList<LobbyPublic>();
        privateLobbyData = new ArrayList<LobbyPrivate>();
        serverSocket = null;
        try {
            serverSocket = new ServerSocket(25565);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }

        

        clientSocket = null;
        while(true){
            
            try {
                clientSocket = serverSocket.accept();
                new Thread(){
                    @Override
                    public void run() {
                        SocketUserThread acceptUser = new SocketUserThread(serverSocket,clientSocket);
                    }
                }.start();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        CHESSsocket work = new CHESSsocket();
        
    }
}
