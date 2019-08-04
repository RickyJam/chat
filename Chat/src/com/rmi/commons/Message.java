package com.rmi.commons;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String content;
	private Date date = new Date();
	private Integer userID;
	
	public Message(String content, Integer sender) {
		setContent(content);
		setUserID(sender);
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	
}
