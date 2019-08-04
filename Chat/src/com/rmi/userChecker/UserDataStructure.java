package com.rmi.userChecker;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.rmi.client.ClientInterf;
import com.rmi.commons.User;
import com.rmi.commons.UserMessages;
import com.rmi.commons.Utils;

public class UserDataStructure {
	
	private ArrayList<Integer> toBeRemoved;
	private HashMap<Integer,Integer> checkerCounter;
	private HashMap<Integer,ClientInterf> clientInterf;
	private ArrayList<User> users;
	private Integer uniqueID = 0;

	private final int waitingSeconds = 10 * 1000;
	
	
	public UserDataStructure() {
		setCheckerCounter(new HashMap<>());
		setClientInterf(new HashMap<>());
		setToBeRemoved(new ArrayList<>());
		setUsers(new ArrayList<>());
	}
	
	
	//PUBLIC Methods
	
	public synchronized void removeUser() {
		System.out.println("starting Old users unsubscription: ");
		Iterator<Integer> idIter = toBeRemoved.iterator();
		while(idIter.hasNext()) {
			Integer id = idIter.next();
			Iterator<User> checkerIter = users.iterator();
			Boolean hasEliminated = Boolean.FALSE;
			while(checkerIter.hasNext() && !hasEliminated) {
				User checker = checkerIter.next();
				if(Utils.isEquals(id,checker.getId())) {
					System.out.println("User Removed: " + checker.getName() + " " + checker.getSurname());
					idIter.remove();
					checkerIter.remove();
					clientInterf.remove(id);
					hasEliminated = Boolean.TRUE;
				}
			}
		}
	}
	
	@Deprecated
	public synchronized void updateHistoryMessage(Integer reciever, UserMessages um) throws RemoteException {
		clientInterf.get(reciever).updateHistoryMessage(um);
	}
	
	public synchronized void notifyMessage(Integer reciever, Integer sender) throws RemoteException {
		clientInterf.get(reciever).notifyMessage(sender);
	}
	
	public User existingUser(Integer userID) {
		synchronized(users) {
			for(User user : users) {
				if(Utils.isEquals(userID,user.getId()))
					return user;
			}
		}
		return null;
	}
	
	public User register(User us, ClientInterf client) {
		
		us.setId(generateUniqueID());
		
		synchronized(users) {
			for(User search: users) {
				if(search.getName().equals(us.getName()) && search.getSurname().equals(us.getSurname())) {
					System.out.println("User: " + us.getName() + " is already Registered!");
					return search;
				}
			}
			
			users.add(us);
			System.out.println("User: " + us.getName() + " has been Registered!");
		}
		
		synchronized (toBeRemoved) {
			for(Integer userID : toBeRemoved) {
				if(Utils.isEquals(userID,us.getId()))
					toBeRemoved.remove(toBeRemoved.indexOf(userID));
			}
		}
		
		synchronized (clientInterf){
			clientInterf.put(us.getId(), client);
		}
		
		return us;
	}
	
	public void addUserToBeRemoved(Integer id) {
		synchronized(toBeRemoved) {
			System.out.println("new user To be removed: " + id);
			toBeRemoved.add(id);
		}
	}

	public synchronized void waitAndCheckUserToBeRemoved() throws InterruptedException {
		while(toBeRemoved.size() == 0) {
			System.out.println("List of users is empty. Waiting 10 seconds, check and then repeat!");
			this.wait(waitingSeconds);
	
			Iterator<Entry<Integer, ClientInterf>> it = clientInterf.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Integer, ClientInterf> entry = it.next();
				Integer id = entry.getKey();
				ClientInterf client = entry.getValue();
				
				try {
					if(!client.isConnected()) {
						if(checkerCounter.containsKey(id)) 
							checkerCounter.replace(id, checkerCounter.get(id) + 1);
						else
							checkerCounter.put(id, 1);
					}
					
				}catch(Exception e) {
					if(checkerCounter.containsKey(id)) 
						checkerCounter.replace(id, checkerCounter.get(id) + 1);
					else
						checkerCounter.put(id, 1);
				}
				
				if(checkerCounter.containsKey(id) && Utils.isEquals(checkerCounter.get(id), new Integer(3)) ) {
					System.out.println("User is offline, adding user to remove list");
					checkerCounter.remove(id);
					toBeRemoved.add(id);
					it.remove();
				}
				
			}
			
		}
	}
	
	
	
	//PRIVATE Methods
	
	private synchronized Integer generateUniqueID() {
		return uniqueID++;
	}
	
	
	//GETTER & SETTER
	
	public synchronized ArrayList<Integer> getToBeRemoved() {
		return toBeRemoved;
	}
	
	public void setToBeRemoved(ArrayList<Integer> toBeRemoved) {
		this.toBeRemoved = toBeRemoved;
	}
	
	public HashMap<Integer, Integer> getCheckerCounter() {
		return checkerCounter;
	}
	
	public void setCheckerCounter(HashMap<Integer, Integer> checkerCounter) {
		this.checkerCounter = checkerCounter;
	}
	
	public HashMap<Integer, ClientInterf> getClientInterf() {
		return clientInterf;
	}
	
	public void setClientInterf(HashMap<Integer, ClientInterf> clientInterf) {
		this.clientInterf = clientInterf;
	}
	
	public synchronized ArrayList<User> getUsers() {
		return users;
	}
	
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
}
