package org.server;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class  AuthenticationServiceImpl extends UnicastRemoteObject implements AuthenticationService {

    private Map<String, String> validUsers;

    protected AuthenticationServiceImpl() throws RemoteException {
        super();

        // Initialize a map of valid username-password pairs
        validUsers = new HashMap<>();
        validUsers.put("user1", "password1");
        validUsers.put("user2", "password2");
        // Add more valid users as needed
    }

    @Override
    public boolean authenticate(String username, String password) throws RemoteException {
        // Check if the username exists and the password matches
        return validUsers.containsKey(username) && validUsers.get(username).equals(password);
    }
}
