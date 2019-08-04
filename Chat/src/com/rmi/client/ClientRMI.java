package com.rmi.client;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.rmi.commons.Message;
import com.rmi.commons.User;
import com.rmi.commons.UserMessages;
import com.rmi.commons.Utils;
import com.rmi.exception.NotExixtingUserException;
import com.rmi.exception.RecieverException;
import com.rmi.server.ServerInterf;

public class ClientRMI extends UnicastRemoteObject implements Serializable, ClientInterf {
	private static final long serialVersionUID = 1L;

	private ServerInterf server;
	private Registry registro;
	private ArrayList<User> contatti;
	private User myself;
	private transient Scanner scanner;  // Create a Scanner object
	private Boolean chatMode = false;
	private ArrayList<UserMessages> conversations;
	private Integer chatOpen = null;
	
	
	public ClientRMI() throws RemoteException{
		setScanner(new Scanner(System.in));
		setContatti(new ArrayList<>());
		setConversations(new ArrayList<>());
		myself = new User();
		myself.setId(new Random().nextInt());
		try {
			registro = LocateRegistry.getRegistry();
			server = (ServerInterf) registro.lookup("chat");
			System.out.println("Acquisito registro di: chat");
		} catch (Exception e) { e.printStackTrace();}
	}
	
	public static void main(String[] args) {
		ClientRMI client = null;
		try {
			client = new ClientRMI();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		Boolean registered = Boolean.FALSE;
		do {
			
			try {
				registered = client.registerUser();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}	
			
		} while(!registered);
		
		try {
			System.out.println("What to do");
			System.out.println("1. Normal flow");
			System.out.println("2. Chat Only flow");
			if(client.scanner.nextLine().equals("1"))
				client.startCommunication();
			else
				client.startChat();
		
		} catch (RemoteException | NotExixtingUserException e) {
			e.printStackTrace();
		}
	}

	
	
//	PUBLIC Methods
	
	public Boolean registerUser() throws CloneNotSupportedException {
		System.out.println("Please insert your name:");
		myself.setName(scanner.nextLine());
		System.out.println("and your Surname:");
		myself.setSurname(scanner.nextLine());
		
		try {
			Object resp = server.register(myself, this);
			if(resp instanceof User) {
				myself =  ((User) resp).clone();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			return Boolean.FALSE;
		}
		
		System.out.println("your ID is: " + myself.getId());
		return Boolean.TRUE;
	}

	public void startCommunication() throws RemoteException, NotExixtingUserException{
		Integer cmdLine = null;
		
		do {
			showMenu();
			switch(cmdLine = scanner.nextInt()) {
				case 1:
					printContacts();
					break;
				case 2:
					printContacts();
					System.out.println("insert reciever id: ");
					Integer reciever = scanner.nextInt();
					scanner.nextLine();//Used to consume /n
					System.out.println("write message: ");
					String message = scanner.nextLine();
					
					try {
						server.sendMessage(myself.getId(), reciever, message);
					} catch(RemoteException e) {
						System.out.println("an error occured while sending the message!");
					} catch(RecieverException e) {
						System.out.println("Reciever offline!");
					}
					
					break;
				case 3:
					break;
				case 4:
					System.out.println("Closing application!");
					server.unregisterUser(myself.getId());
					break;
				default:
					System.out.println("Please insert one of the numbers!");
			}
			
		} while (cmdLine != 4);
		
		scanner.close(); 
	}
	
	public void startChat() throws RemoteException, NotExixtingUserException{

		setChatMode(Boolean.TRUE);
		Boolean exit = false;
		
		while(!exit) {
			System.out.println("chat availlable: ");
			printStartedConverstion();
			System.out.println("or press new to start a new chat.");
			String choise = scanner.nextLine().toLowerCase();
			
			if(Utils.isEquals(choise, "new")) {
				printContacts();
				System.out.println("insert reciever number: ");
				Integer reciever = contatti.get(scanner.nextInt()-1).getId();
				scanner.nextLine();//Used to consume /n
				exit = openChat(reciever);
			} else if (Utils.isEquals(choise, "<shutdown>")){
				exit = Boolean.TRUE;
			} else {
				Integer reciever = null;
				if(Utils.isEquals(conversations.get(new Integer(choise) - 1).getUser1().getId(), myself.getId()))
					reciever = conversations.get(new Integer(choise) - 1).getUser2().getId();
				else 
					reciever = conversations.get(new Integer(choise) - 1).getUser1().getId();
				exit = openChat(reciever);
			}
		}
		System.out.println("Closing application!");
		server.unregisterUser(myself.getId());
		scanner.close();
	}
	
	public void updateHistoryMessage(UserMessages um) throws RemoteException {
		if(getChatMode()) {
			printHistory(um);
		} else {
			if(Utils.isEquals(um.getUser1().getId(),um.getUser2().getId())) { // when you speak with yourself...
				return;
			} else if(Utils.isEquals(um.getUser1().getId(),myself.getId())) {
				System.out.println("A message from: " + um.getUser2().getName() );
			} else if(Utils.isEquals(um.getUser2().getId(),myself.getId())) {
				System.out.println("A message from: " + um.getUser1().getName() );
			}
			
			String line = null;
			do {
				System.out.println("Do you want to read it? (Y/N)");
				line = scanner.nextLine();
				if(line.equals("Y")) {
					printHistory(um);
				}
				
			} while(!(line.equals("Y") || line.equals("N")));
		}
	}

	public Boolean isConnected() throws RemoteException {
		return Boolean.TRUE;
	}

	public void notifyMessage(Integer userID) throws RemoteException{
		getConversation();
		
		UserMessages um = Utils.getUmFromUserId(conversations, userID);
		if(Utils.isEquals(getChatOpen(),userID))
			printHistory(um);
		else
			System.out.println("Message arrive from: " + (Utils.isEquals(um.getUser1().getId(),userID) ? 
					um.getUser2().getSurname() : um.getUser1().getSurname()));
	}

	
		
//	PRIVATE methods
	
	private void printContacts() throws RemoteException {
		//updating contact list
		contatti = server.getUserList();
		int counter = 1;
		System.out.println("Lista Contatti: ");
		for(User utente : contatti) {
			String contact = counter + ". " + utente.getName() + " " + utente.getSurname() + " " + utente.getId();
			if(Utils.isEquals(utente.getId(),myself.getId()))
				contact += " (myself)";
			System.out.println(contact);
			counter++;
		}
	}
	
	private void printHistory(UserMessages um) {
		final int maxMessages = 20;
		int mexIndexStart = 0;
		if (um.getMessagesHistory().size() >= maxMessages) {
			mexIndexStart = um.getMessagesHistory().size() - maxMessages;
		}
		List<Message> messages = um.getMessagesHistory().subList(mexIndexStart, um.getMessagesHistory().size());
		if(messages.size() > 0) {
			System.out.println("-------------------------------------------");
			for(int i = 0; i < messages.size(); i++) {
				String messageSpace = "";
			    if(Utils.isEquals(messages.get(i).getUserID(), myself.getId())) {
			    	messageSpace = "          ";
			    }
		    
			    System.out.println(messageSpace + messages.get(i).getContent());
			}
			System.out.println("-------------------------------------------");
		} else {
			System.out.println("No messages availlable for this conversation");
		}
	}
	
	private void showMenu() {
		System.out.println("Availlable Options: ");
		System.out.println("1. Print contact List!");
		System.out.println("2. Send Message!");
		System.out.println("4. Close Connection!");
	}
	
	private void getConversation() {
		try {
			setConversations(server.getConversation(myself.getId()));
		} catch (RemoteException e) {
			System.out.println("client offline!");
		}
	}
	
	private void printStartedConverstion() {
		getConversation();
		
		int counter = 1;
		
		for(UserMessages um: conversations) {
			String unreadMess = "(" + um.getUnreadMessages() + ")";
			if(Utils.isEquals(um.getUser1().getId(),myself.getId()))
				System.out.println(counter + ". " + um.getUser1().getSurname() + (um.getUnreadMessages() > 0 ? unreadMess : null) );
			else
				System.out.println(counter + ". " + um.getUser2().getSurname());		
			counter++;
		}
	}
	
	private Boolean openChat(Integer reciever) {
		
		setChatOpen(reciever);
		String message = null;
		System.out.println("write message: (write '<close>' to close chat, <shutdown> to turn off the program)");
		do {
					
			message = scanner.nextLine();
			try {
				if(!message.equals("<close>") && !message.equals("<shutdown>"))
					server.sendMessage(myself.getId(), reciever, message);
			} catch(RemoteException e) {
				System.out.println("an error occured while sending the message!");
			} catch(RecieverException e) {
				System.out.println("Reciever offline!");
			} catch (NotExixtingUserException e) {
				System.out.println("Not existing user!");
			}

		} while (!message.equals("<close>") && !message.equals("<shutdown>"));
		
		setChatOpen(null);
		
		if(message.equals("<shotdown>"))
			return Boolean.TRUE;
		else 
			return Boolean.FALSE;
	}
	
	
	
//	GETTER & SETTER
	
	public ArrayList<User> getContatti() {
		return contatti;
	}

	public void setContatti(ArrayList<User> contatti) {
		this.contatti = contatti;
	}
	
	public User getMyself() {
		return myself;
	}

	public void setMyself(User myself) {
		this.myself = myself;
	}

	public Scanner getScanner() {
		return scanner;
	}

	public void setScanner(Scanner scanner) {
		this.scanner = scanner;
	}

	public Boolean getChatMode() {
		return chatMode;
	}

	public void setChatMode(Boolean chatMode) {
		this.chatMode = chatMode;
	}

	public ArrayList<UserMessages> getConversations() {
		return conversations;
	}

	public void setConversations(ArrayList<UserMessages> conversations) {
		this.conversations = conversations;
	}

	public Integer getChatOpen() {
		return chatOpen;
	}

	public void setChatOpen(Integer chatOpen) {
		this.chatOpen = chatOpen;
	}

}
