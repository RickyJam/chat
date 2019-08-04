package com.thread;

public class VoceRubrica {

	private String name;
	private String surname;
	private String number;
	
	public VoceRubrica(String name, String surname, String number) {
		this.setName(name);
		this.setSurname(surname);
		this.setNumber(number);
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
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	@Override
	public String toString() {
		String result = "Name: " + this.getName() + "\n" +
						"Surname: " + this.getSurname() + "\n" + 
						"Number: " + this.getNumber();	
		return result;	
	}
}
