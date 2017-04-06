/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datahandler;
import static datahandler.CHESSsocket.privateLobbyData;
import static datahandler.CHESSsocket.publicLobbyData;
import static datahandler.CHESSsocket.gamesList;
import java.net.*;
import java.io.*;
import java.util.*;
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
                String opcode = inputLine.substring(0,3);
                state = 0;
                //Creates a lobby
                if (opcode.equals("crl")){
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
                    if(lPassword.isEmpty()==true)lSecure="false";
                    else lSecure="true";
                    
                    String lID = Integer.toString(ThreadLocalRandom.current().nextInt(100000, 200000 + 1));
                    LobbyPrivate newPrivateLobby = new LobbyPrivate(lName,lID,lPassword,lPlayer,lSecure,out,in);
                    LobbyPublic newPublicLobby = new LobbyPublic(lName,lID,lSecure);
                    privateLobbyData.add(newPrivateLobby);
                    publicLobbyData.add(newPublicLobby);
                }
                //Refreshes lobby list
                if (opcode.equals("ref")){
                    System.out.println("1");
                    String refreshText = "";
                    for(int i = 0; i < publicLobbyData.size(); i++){
                        if(i==publicLobbyData.size()-1)refreshText=publicLobbyData.get(i).getLobbyName()+"/"+publicLobbyData.get(i).getLobbyID()+"/"+publicLobbyData.get(i).getLobbySecure()+"$";
                        else refreshText=publicLobbyData.get(i).getLobbyName()+"/"+publicLobbyData.get(i).getLobbyID()+"/"+publicLobbyData.get(i).getLobbySecure()+"/";
                    }
                    
                    out.writeObject(refreshText);
                    System.out.println("2");
                }
                //Checks for password
                if (opcode.equals("joi")){
                    System.out.println("Move request received");
                    String lName = "";
                    String lPassword = "";
                    String lPlayer = "";
                    String lID = "";
                    int correctIndex = 0;
                    
                    int moveFirst = ThreadLocalRandom.current().nextInt(1, 2 + 1);
                    for(int i = 4;inputLine.charAt(i)!='$';i++){
                        if(inputLine.charAt(i)=='/'||inputLine.charAt(i)=='$')state++;
                        else{
                            if(state==0)lName=lName+inputLine.charAt(i);
                            if(state==1)lID=lID+inputLine.charAt(i);
                            if(state==2)lPassword=lPassword+inputLine.charAt(i);
                            if(state==3)lPlayer=lPlayer+inputLine.charAt(i);
                        }
                    }
                    for(int i = 0; i < privateLobbyData.size(); i++){
                        if(privateLobbyData.get(i).getLobbyName().equals(lName)){
                            if(privateLobbyData.get(i).getLobbyID().equals(lID)){
                                if(privateLobbyData.get(i).getLobbyPassword().equals(lPassword)){
                                    System.out.println("Lobby found");
                                    ChessMatch tempMatch;
                                    
                                    if(moveFirst==1){
                                        tempMatch = new ChessMatch(lName, lID, lPassword,
                                            privateLobbyData.get(i).getLobbyPlayer1(), lPlayer,
                                            privateLobbyData.get(i).getOutputStream(),
                                            privateLobbyData.get(i).getInputStream(), out, in);
                                        
                                    }
                                    else{
                                        tempMatch = new ChessMatch(lName, lID, lPassword,
                                            lPlayer, privateLobbyData.get(i).getLobbyPlayer1(),
                                            out,
                                            in, privateLobbyData.get(i).getOutputStream(), privateLobbyData.get(i).getInputStream());
                                        
                                    }
                                    
                                    gamesList.add(tempMatch);
                                    correctIndex=gamesList.indexOf(tempMatch);
                                    privateLobbyData.remove(i);
                                    break;
                                }
                            }
                        }
                    }
                    gamesList.get(correctIndex).runMatch();
                }
                //Makes a move
                if (opcode.equals("mkm")){
                    System.out.println("Move request received");
                    String lOriginY = "";
                    String lOriginX = "";
                    String lDestinationY = "";
                    String lDestinationX = "";
                    String lName = "";
                    String lID = "";
                    String lWhite = "";
                    String lBlack = "";
                    String lTurn = "";
                    for(int i = 4;inputLine.charAt(i)!='$';i++){
                        if(inputLine.charAt(i)=='/'||inputLine.charAt(i)=='$')state++;
                        else{
                            if(state==0)lOriginY=lOriginY+inputLine.charAt(i);
                            if(state==1)lOriginX=lOriginX+inputLine.charAt(i);
                            if(state==2)lDestinationY=lDestinationY+inputLine.charAt(i);
                            if(state==3)lDestinationX=lDestinationX+inputLine.charAt(i);
                            if(state==4)lName=lName+inputLine.charAt(i);
                            if(state==5)lID=lID+inputLine.charAt(i);
                            if(state==6)lWhite=lWhite+inputLine.charAt(i);
                            if(state==7)lBlack=lBlack+inputLine.charAt(i);
                            if(state==8)lTurn=lTurn+inputLine.charAt(i);
                        }
                    }
                    
                    for(int i = 0; i < gamesList.size(); i++){
                        if(gamesList.get(i).getLobbyName().equals(lName)){
                            if(gamesList.get(i).getLobbyID().equals(lID)){
                                if(gamesList.get(i).getLobbyPlayer1().equals(lWhite)){
                                    if(gamesList.get(i).getLobbyPlayer2().equals(lBlack)){
                                        if(lTurn.equals("1")){
                                            gamesList.get(i).sendMsgP2(lOriginY+"/"+lOriginX+"/"+lDestinationY+"/"+lDestinationX+"$");
                                        }
                                        else{
                                            gamesList.get(i).sendMsgP1(lOriginY+"/"+lOriginX+"/"+lDestinationY+"/"+lDestinationX+"$");
                                        }
                                    }
                                }
                            }
                        }
                    }
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
