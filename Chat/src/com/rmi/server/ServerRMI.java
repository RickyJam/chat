package com.rmi.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import com.rmi.client.ClientInterf;
import com.rmi.commons.Message;
import com.rmi.commons.User;
import com.rmi.commons.UserMessages;
import com.rmi.commons.Utils;
import com.rmi.exception.NotExixtingUserException;
import com.rmi.exception.RecieverException;
import com.rmi.userChecker.UserDataStructure;
import com.rmi.userChecker.UserManager;

public class ServerRMI extends UnicastRemoteObject implements ServerInterf{
	private static final long serialVersionUID = -3781466511617456479L;

	public static int port = 1099;
	private ArrayList<UserMessages> conversations;
	private UserManager userManager;
	private UserDataStructure userData;
	
	protected ServerRMI(Registry registro) throws RemoteException {
		super();
		conversations = new ArrayList<UserMessages>();
		setUserData(new UserDataStructure());
		setUserManager(new UserManager(userData));
	}

	public static void main(String[] args) {
		try {
			Registry registro = LocateRegistry.createRegistry(port);
			ServerRMI server = new ServerRMI(registro);
			registro.rebind("chat", server);
			System.out.println("Server Registrato su: chat");
		} catch (RemoteException e) {}
	}

	
	
	// PUBLIC methods
	
	public Object register(User us, ClientInterf client) {
		return userData.register(us, client);
	}

	public void unregisterUser(Integer unregistered) {
		userData.addUserToBeRemoved(unregistered);
	}
	
	public Boolean sendMessage(Integer sender, Integer reciever, String message) throws RemoteException, NotExixtingUserException, RecieverException {
		
		UserMessages um = findConversation(sender, reciever);
		
		if(um == null) {
			
			User user1 = existingUser(sender);
			User user2 = existingUser(reciever);
			if(user1 != null && user2 != null) {
				um = new UserMessages(user1, user2);
				um.addMessages(new Message(message, sender));
				conversations.add(um);
			}
			else {
				throw new NotExixtingUserException();
			}
		} else {
			um.addMessages(new Message(message, sender));
		}
		um.increaseUnreadMessage();
		
		try {
//			userManager.updateHistoryMessage(reciever, um);
			userData.notifyMessage(reciever,sender);
		} catch(RemoteException e) {
			throw new RecieverException();
		}

		return Boolean.TRUE;
	}

	public Boolean recieveMessage() throws RemoteException {
		return null;
	}

	public ArrayList<User> getUserList() throws RemoteException {
		return userData.getUsers();
	}
	
	public ArrayList<UserMessages> getConversation(Integer userID) throws RemoteException {
		ArrayList<UserMessages> convers = new ArrayList<>();
		
		for(UserMessages um : conversations) {
			if(( Utils.isEquals(um.getUser1().getId(),userID) || Utils.isEquals(um.getUser2().getId(),userID))) {
				convers.add(um);
			}
		}
		
		return convers;
	}

	
	
//	PRIVATE methods
	
	private UserMessages findConversation(Integer userID1, Integer userID2) {
		for(UserMessages um : conversations) {
			if(( Utils.isEquals(um.getUser1().getId(),userID1) || Utils.isEquals(um.getUser1().getId(),userID2)) &&
					(Utils.isEquals(um.getUser2().getId(),userID1) || Utils.isEquals(um.getUser2().getId(),userID2))) {
				return um;
			}
		}
		
		return null;
	}
	
	private User existingUser(Integer userID) {
		return userData.existingUser(userID);
	}
	

	public UserDataStructure getUserData() {
		return userData;
	}
	

	public void setUserData(UserDataStructure userData) {
		this.userData = userData;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}


}
