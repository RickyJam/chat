package com.rmi.commons;

import java.util.ArrayList;

public abstract class Utils {
	
	public static final Boolean isEquals(Object param1, Object param2) {
		if(param1.getClass() != param2.getClass())
			return false;
		else if(param1.equals(param2))
			return true;
		else return false;
	}
	
	public static final UserMessages getUmFromUserId(ArrayList<UserMessages> conversations, Integer userID) {
		for(UserMessages um : conversations) {
			if(isEquals(um.getUser1().getId(),userID) || isEquals(um.getUser2().getId(),userID))
				return um;
		}
		
		return null;
	}
	
}
