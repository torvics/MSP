package msp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Logger;

public class UserManager {

    public static final String CLASS_NAME = UserManager.class.getSimpleName();
    public static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private HashMap<String, Socket> connections;

    public UserManager() {
        super();
        connections = new  HashMap<String, Socket>();
    }
    public void ShowUsers() {
        if (connections.isEmpty()) {
            System.out.println("There is no users connected.");
        } else {
            int ea = 1;
            for (String userName : connections.keySet()) {
                System.out.println("Usuario " + ea + ": " + userName);
                ea++;
            }
        }
    }

    public boolean disconnect(String user){
        boolean result = true;
        if (connections.containsKey(user)){
            connections.remove(user);
        }else {
            result = false;
        }
        return result;
    }
    public boolean connect(String user, Socket socket) {
        boolean result = true;

       Socket s = connections.put(user,socket);
       if( s != null) {
           result = false;
       }
       return result;
    }

    public Socket get(String user) {

        Socket s = connections.get(user);

        return s;
    }

    public void send(String message) {

        Collection<Socket> conexiones = connections.values();

        for( Socket s: conexiones) {
            try {
                PrintWriter output = new PrintWriter(s.getOutputStream(), true);
                output.println(message);
            } catch (IOException e) {
                LOGGER.severe(e.getMessage());
                e.printStackTrace();
            }
        }

    }




}
