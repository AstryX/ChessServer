/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datahandler;

/**
 *
 * @author Boo
 */
public class LobbyPrivate {
    private String lobbyName;
    private String lobbyID;
    private String lobbyPassword;
    private String lobbyPlayer1;
    private String lobbyPlayer2;
    private String isLobbySecure;
    public LobbyPrivate(String tempName, String tempID, String tempPassword, String tempPlayer, String tempSecure){
        lobbyName = tempName;
        lobbyID = tempID;
        lobbyPassword = tempPassword;
        lobbyPlayer1 = tempPlayer;
        lobbyPlayer2 = "";
        isLobbySecure = tempSecure;
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
        return lobbyPlayer1;
    }
    public String getLobbyPlayer2(){
        return lobbyPlayer2;
    }
    public void modifyLobbyPlayer2(String tempPlayer){
        lobbyPlayer2 = tempPlayer;
    }
    public String getLobbySecure(){
        return isLobbySecure;
    }
}
