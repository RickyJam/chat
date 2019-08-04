package com.rmi.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class UserMessages implements Serializable {
	private static final long serialVersionUID = 1L;
	
	ArrayList<Message> messagesHistory;
	private Integer unreadMessages;
	
	private User user1;
	private User user2;
	
	private Integer userMessageId;
	
	public UserMessages(User user1, User user2) {
		messagesHistory = new ArrayList<Message>();
		setUserMessageId(new Random().nextInt());
		setUser1(user1);
		setUser2(user2);
		setUnreadMessages(new Integer(0));
	}

	public ArrayList<Message> getMessagesHistory() {
		return messagesHistory;
	}

	public void setMessagesHistory(ArrayList<Message> messagesHistory) {
		this.messagesHistory = messagesHistory;
	}
	
	public void addMessages(Message message) {
		
		if(!Utils.isEquals(message.getUserID(),user1.getId()) && !Utils.isEquals(message.getUserID(),user2.getId()))
			return;
		if(message == null || message.getUserID() == null || message.getContent() == null) {
			return;
		}
		
		messagesHistory.add(message);
	}
	
	public void increaseUnreadMessage() {
		setUnreadMessages(getUnreadMessages() + 1);
	}

	public Integer getUserMessageId() {
		return this.userMessageId;
	}

	public void setUserMessageId(Integer usereMessageId) {
		this.userMessageId = usereMessageId;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public Integer getUnreadMessages() {
		return unreadMessages;
	}

	public void setUnreadMessages(Integer unreadMessages) {
		this.unreadMessages = unreadMessages;
	}

}
