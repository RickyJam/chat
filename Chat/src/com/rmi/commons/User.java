package com.rmi.commons;

import java.io.Serializable;

public class User implements Serializable, Cloneable{
	private static final long serialVersionUID = -6116813215685712171L;
	
	private String name;
	private String surname;
	private Integer id;
	
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

	@Override
	public User clone() throws CloneNotSupportedException {

	    return (User) super.clone();
	}
	
}
