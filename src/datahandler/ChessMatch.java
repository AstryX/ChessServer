/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datahandler;
import java.io.*;
/**
 *
 * @author Boo
 */
public class ChessMatch {
    private String lobbyName;
    private String lobbyID;
    private String lobbyPassword;
    private String whitePlayer;
    private String blackPlayer;
    private ObjectOutputStream p1out;
    private ObjectInputStream p1in;
    private ObjectOutputStream p2out;
    private ObjectInputStream p2in;
    private String fromWhitePlayer;
    private String fromBlackPlayer;
    public ChessMatch(String tempName, String tempID, String tempPassword, String tempPlayer1, String tempPlayer2, ObjectOutputStream temp1Out, ObjectInputStream temp1In, ObjectOutputStream temp2Out, ObjectInputStream temp2In){
        fromWhitePlayer = "";
        fromBlackPlayer = "";
        p1out = temp1Out;
        p1in = temp1In;
        p2out = temp2Out;
        p2in = temp2In;
        lobbyName = tempName;
        lobbyID = tempID;
        lobbyPassword = tempPassword;
        whitePlayer = tempPlayer1;
        blackPlayer = tempPlayer2;
    }
    
    public void runMatch(){
        String matchDataP1;
        String matchDataP2;
        matchDataP1="true/"+lobbyName+"/"+lobbyID+"/"+"1/"+whitePlayer+"/"+blackPlayer+"$";
        matchDataP2="true/"+lobbyName+"/"+lobbyID+"/"+"2/"+whitePlayer+"/"+blackPlayer+"$";
        try{
            p1out.writeObject(matchDataP1);
            p2out.writeObject(matchDataP2);
        }
        catch(IOException ioex){
            System.err.println("I/O failed");
        }
        
        
    }
    
    
    public void sendMsgP1(String theMessage){
        try{
            p1out.writeObject(theMessage);
        }
        catch(IOException ioex){
            System.err.println("I/O failed");
        }
    }
    
    public void sendMsgP2(String theMessage){
        try{
            p2out.writeObject(theMessage);
        }
        catch(IOException ioex){
            System.err.println("I/O failed");
        }
    }
    
    public String getLobbyName(){
        return lobbyName;
    }
    public String getLobbyID(){
        return lobbyID;
    }
    public String getLobbyPassword(){
        return lobbyPassword;
    }
    public String getLobbyPlayer1(){
        return whitePlayer;
    }
    public String getLobbyPlayer2(){
        return blackPlayer;
    }
    public ObjectOutputStream getP1OutputStream(){
        return p1out;
    }
    
    public ObjectInputStream getP1InputStream(){
        return p1in;
    }
    public ObjectOutputStream getP2OutputStream(){
        return p2out;
    }
    
    public ObjectInputStream getP2InputStream(){
        return p2in;
    }
}
