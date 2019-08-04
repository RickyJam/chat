package com.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class Server {

	public static int port = 5000;
	ServerSocket serverSocket;
	Socket socket;
	ObjectOutputStream out;
	ObjectInputStream in;
	ArrayList<User> users;
	
	public Server() {	
		users = new ArrayList<>();
		try {
			serverSocket = new ServerSocket(port);
			socket = serverSocket.accept();
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		Server server = new Server();
		server.startConnection();
		
	}
	
	public void startConnection() {
		String action;
		try {
			do {
				System.out.println("server ready and waiting for action: ...");
				action = (String) in.readObject();
				if(action.equals("Date")) {
					out.writeObject(new Date());
					out.flush();
				} else if (action.equals("Registration")) {
					String name = (String) in.readObject();
					String surname = (String) in.readObject();
					Integer id = (Integer) in.readObject();
					addUser(new User(name, surname, id));
				} else if (action.equals("Registered")) {
					out.writeObject(users);
				} else if (action.equals("Exit")) {
					System.out.println("closing server runtime application...");
					out.close();
					in.close();
					socket.close();
					serverSocket.close();
					break;
				}
				
				
			} while(!action.equals("Exit"));
			
			System.out.println("server terminated correctly");
		} catch(Exception e) {}
	}

	private void addUser(User user) {
		users.add(user);
	}
}
