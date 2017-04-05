/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datahandler;
import static datahandler.CHESSsocket.privateLobbyData;
import static datahandler.CHESSsocket.publicLobbyData;
import java.net.*;
import java.io.*;
import java.util.*;
import jdk.nashorn.internal.objects.NativeString;
import java.util.concurrent.ThreadLocalRandom;
/**
 *
 * @author Boo
 */
public class SocketUserThread  {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    public int state;
    public SocketUserThread(ServerSocket tempServerSocket, Socket tempClientSocket){
        state = 0;
        serverSocket = tempServerSocket;
        clientSocket = tempClientSocket;
        try{
            doWork();
        }
        catch(IOException ioex){
            System.err.println("I/O failed.");
        }
    }
    private void doWork() throws IOException{
        //PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
        //BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        String outputLine;
        String inputLine;
        //CHESSlanguage kkp = new CHESSlanguage();
        //outputLine = kkp.processInput(null);
        //out.writeObject(outputLine);
        try{
            while ((inputLine = (String)in.readObject()) != null) {
                String opcode = inputLine.substring(0,2);
                state = 0;
                //Creates a lobby
                if (opcode.equals("crl")){
                    System.out.println("1");
                    String lName = "";
                    String lPassword = "";
                    String lPlayer = "";
                    String lSecure = "";
                    for(int i = 4;inputLine.charAt(i)!='$';i++){
                        if(inputLine.charAt(i)=='/'||inputLine.charAt(i)=='$')state++;
                        else{
                            if(state==0)lName=lName+inputLine.charAt(i);
                            if(state==1)lPassword=lPassword+inputLine.charAt(i);
                            if(state==2)lPlayer=lPlayer+inputLine.charAt(i);
                        }
                    }
                    if(lPlayer.isEmpty()==true)lSecure="false";
                    else lSecure="true";
                    String lID = Integer.toString(ThreadLocalRandom.current().nextInt(100000, 200000 + 1));
                    LobbyPrivate newPrivateLobby = new LobbyPrivate(lName,lID,lPassword,lPlayer,lSecure);
                    LobbyPublic newPublicLobby = new LobbyPublic(lName,lID,lSecure);
                    privateLobbyData.add(newPrivateLobby);
                    publicLobbyData.add(newPublicLobby);
                    System.out.println(publicLobbyData.get(0).getLobbyName()+" "+publicLobbyData.get(0).getLobbyID());
                }
                //Refreshes lobby list
                if (opcode.equals("ref")){
                    out.writeObject(publicLobbyData);
                }
                //Checks for password
                if (opcode.equals("pwc")){
                    
                }
                //Makes a move
                if (opcode.equals("pwc")){
                    
                }
                //Joins a lobby
                if (opcode.equals("pwc")){
                    
                }
                
                //outputLine = kkp.processInput((String)inputLine);
                //out.writeObject(outputLine);
            }
        }
        catch(ClassNotFoundException cnfe){
            System.err.println("Data has not been found.");
        }
        out.close();
        in.close();
        clientSocket.close();
    }
}
