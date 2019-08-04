package com.rmi.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.rmi.commons.UserMessages;

public interface ClientInterf extends Remote{

	@Deprecated
	public void updateHistoryMessage(UserMessages um) throws RemoteException;
	
	public Boolean isConnected() throws RemoteException;
	
	public void notifyMessage(Integer userID) throws RemoteException;
}
