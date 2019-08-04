package com.socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Client {

	public static int port = 5000;
	Socket socket;
	ObjectOutputStream out;
	ObjectInputStream in;
	Scanner scan;
	
	public Client() {
		try {
			socket = new Socket(InetAddress.getByName(null), port);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Client(String ip) {
		try {
			socket = new Socket(InetAddress.getByName(ip), port);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		System.out.println("insert ip Address or leave blank for localhost:");
		Scanner sRead = new Scanner(System.in);
		String ip = sRead.nextLine();
		Client client;
		if(ip == null || ip.equals(""))
			client = new Client();
		else
			client = new Client(ip);
		
		client.startCommunication();
		System.out.println("Terminated!");
		sRead.close();
	}
	
	@SuppressWarnings("unchecked")
	public void startCommunication() {
		int action;
		Scanner scan;
		try {
			
			do {
				System.out.println("Insert number of the action you what to do:  ");
				System.out.println("1. Know server date!");
				System.out.println("2. Register your Account!");
				System.out.println("3. Get registered users!!");
				System.out.println("the other choise are under development...!");
				System.out.println("4. Exit! (Alwais present ehehehe!!)");
				scan = new Scanner(System.in);
				action = scan.nextInt();
				
				switch(action) {
					case 1:
						System.out.println("Sending request for Date....");
						out.writeObject("Date");
						out.flush();
						Date date = (Date) in.readObject();
						System.out.println("Server date: " + date.toString());
						break;
					case 2: 
						System.out.println("Registration request accepted....");
						out.writeObject("Registration");
						System.out.println("insert your name:");
						scan.nextLine();//must be used to acquire correct line
						String name = scan.nextLine();
						out.writeObject(name);
						System.out.println("insert your surname:");
						String surname = scan.nextLine();
						out.writeObject(surname);
						System.out.println("insert your ID:");
						out.writeObject(scan.nextInt());
						out.flush();
						
						break;
					case 3:
						System.out.println("Registrated request sended....");
						out.writeObject("Registered");
						out.flush();
						ArrayList<User> users = (ArrayList<User>) in.readObject();
						for(User user : users) {
							System.out.println("Name:" + user.name);
							System.out.println("Surname:" + user.surname);
							System.out.println("ID:" + user.id);
						}
						break;
					case 4:
						System.out.println("Exiting from application...");
						out.writeObject("Exit");
						out.flush();
						break;
				}
				
			} while(action != 4);
			
		} catch(Exception e) {e.printStackTrace();}
	}
}
