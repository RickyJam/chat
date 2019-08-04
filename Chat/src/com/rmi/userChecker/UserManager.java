package com.rmi.userChecker;

import java.util.Random;

public class UserManager extends Thread{
	
	private UserDataStructure userData;
	private int myID;

	public UserManager(UserDataStructure userData) {
		setUserData(userData);
		setMyID(new Random().nextInt(10));
		start();
	}
	
	public void run() {
		while(true) {
			try {
				userData.waitAndCheckUserToBeRemoved();
				userData.removeUser();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	
	
	/***************************************************************/
	// GETTER & SETTER...

	public int getMyID() {
		return myID;
	}

	public void setMyID(int myID) {
		this.myID = myID;
	}

	public UserDataStructure getUserData() {
		return userData;
	}

	public void setUserData(UserDataStructure userData) {
		this.userData = userData;
	}
	
}
