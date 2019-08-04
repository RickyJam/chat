package com.thread;

public class ClientThread extends Thread{

	private int ID;
	private Rubrica rubrica;
	
	public ClientThread(int ID, Rubrica rubrica) {
		this.setID(ID);
		this.setRubrica(rubrica);
		start();
	}

	@Override
	public void run() {
		
		if(this.ID == 0) {
			rubrica.addVoceRubrica(new VoceRubrica("Riccardo", "Pacifico", "3403275558"));
			rubrica.addVoceRubrica(new VoceRubrica("Davide", "Pacifico", "3403275558"));
			rubrica.addVoceRubrica(new VoceRubrica("Massimo", "Pacifico", "3403275558"));
			try {
				sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			rubrica.addVoceRubrica(new VoceRubrica("Test", "Test", "1234567890"));
		} else {
			rubrica.addVoceRubrica(new VoceRubrica("Anna", "Tabano", "3403275558"));
			rubrica.addVoceRubrica(new VoceRubrica("Silvia", "Salierno", "3403275558"));
			rubrica.removeVoceRubrica("Massimo");
			System.out.println(rubrica.waitingVoce("Test").toString());
			rubrica.printRubrica();
		}
		
		
		
		
	}
	
	
	//////////////////////////////////////////////////////////////
	//GETTER & SETTER
	
	public Rubrica getRubrica() {
		return rubrica;
	}

	public void setRubrica(Rubrica rubrica) {
		this.rubrica = rubrica;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

}
