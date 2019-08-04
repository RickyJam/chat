package com.rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.rmi.client.ClientInterf;
import com.rmi.commons.User;
import com.rmi.commons.UserMessages;
import com.rmi.exception.NotExixtingUserException;
import com.rmi.exception.RecieverException;

public interface ServerInterf extends Remote{

	public Boolean sendMessage(Integer sender, Integer reciever, String message) throws RemoteException,NotExixtingUserException, RecieverException;
	
	public Object register(User us, ClientInterf client) throws RemoteException;
	
	public ArrayList<User> getUserList() throws RemoteException;
	
	public void unregisterUser(Integer unregistered) throws RemoteException;

	public ArrayList<UserMessages> getConversation(Integer id) throws RemoteException;;
	
}
