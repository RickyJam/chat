package com.thread;

public class Main {

	public static int nThread = 2;
	
	public static void main(String[] args) {
		
		Rubrica rubrica = new Rubrica();
		
		for(int i = 0; i < Main.nThread; i++) {
			new ClientThread(i,rubrica);
		}
		
	}
	
}
