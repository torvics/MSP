package msp;

import msp.UserManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ConnectionHandler implements Runnable {

    public static final String CLASS_NAME = ConnectionHandler.class.getSimpleName();
    public static final Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private UserManager users;
    private Socket clientSocket = null;
    private BufferedReader input;
    private PrintWriter output;
    ArrayList<String> UsersList;

    public ConnectionHandler(UserManager u, Socket s) {
        users = u;
        clientSocket = s;

        try {
            input = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        String buffer = null;
        Boolean isRunning = true;
        while (true) {
            try {
                buffer = input.readLine();
            } catch (IOException e) {
                LOGGER.severe(e.getMessage());
                e.printStackTrace();
            }
            String command = buffer.trim();
            if( command.startsWith("CONNECT") ) {
                String userName = command.substring(command.indexOf(' ')  ).trim();
                System.out.println(userName);
               boolean isConnected =  users.connect(userName,clientSocket);
               if( isConnected ) {
                   output.println("Connected");
               } else {
                   output.println("valió cochi the connection");
               }
            }

            if( command.startsWith("SEND") ) {
                String message = command.substring(command.indexOf('#')+1,
                        command.indexOf('@') );
                if (message.length() < 140){
                    System.out.println(message);
                    String userName = command.substring(command.indexOf('@')+1 ).trim();
                    System.out.println(userName);
                    users.send(message);
                }else{
                    System.out.println("The message has to be less than 140 characters.");
                }
            }

            if( command.startsWith("DISCONNECT") ) {
                String userName = command.substring(command.indexOf(' ')  ).trim();
                //UsersList.add(userName);
                boolean isdisConnected =  users.disconnect(userName);
                if( isdisConnected ) {
                    output.println("The user " + userName + " has been disconnected.");
                } else {
                    output.println("valió cochi la desconectada");
                }
            }
            if( command.startsWith("SHOW") ) {
                users.ShowUsers();
            }

        }


    }
}
