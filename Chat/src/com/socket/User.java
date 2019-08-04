package com.socket;

import java.io.Serializable;

public class User implements Serializable{
	private static final long serialVersionUID = -6116813215685712171L;
	
	String name;
	String surname;
	Integer id;
	
	public User(String name, String surname, Integer id) {
		this.name = name;
		this.surname = surname;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
}
