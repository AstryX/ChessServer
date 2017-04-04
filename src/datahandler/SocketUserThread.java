/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datahandler;
import java.net.*;
import java.io.*;
/**
 *
 * @author Boo
 */
public class SocketUserThread  {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    public SocketUserThread(ServerSocket tempServerSocket, Socket tempClientSocket){
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
        System.out.println("1");
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        System.out.println("2");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                clientSocket.getInputStream()));
        System.out.println("3");
        
        String inputLine, outputLine;
        CHESSlanguage kkp = new CHESSlanguage();
        System.out.println("4");
        outputLine = kkp.processInput(null);
        System.out.println("5");
        out.println(outputLine);
        System.out.println("6");

        while ((inputLine = in.readLine()) != null) {
             outputLine = kkp.processInput(inputLine);
             out.println(outputLine);
             if (outputLine.equals("Bye."))
                break;
        }
        out.close();
        in.close();
        clientSocket.close();
    }
}