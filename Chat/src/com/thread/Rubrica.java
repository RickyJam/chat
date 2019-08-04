package com.thread;

import java.util.ArrayList;

public class Rubrica {

	private ArrayList<VoceRubrica> lista;
	
	public Rubrica() {
		setLista(new ArrayList<>());
	}
	
	public Rubrica(ArrayList<VoceRubrica> lista) {
		this.setLista(lista);
	}

	
	public synchronized void addVoceRubrica(VoceRubrica voce) {
		lista.add(voce);
		System.out.println("This Voce has been added to the Rubrica:\n" + voce.toString() );
		notifyAll();
	}
	
	@Deprecated
	public void removeVoceRubrica(VoceRubrica voce) {
		int index = lista.indexOf(voce);
		
		if(index < 0) {
			return;
		} else {
			lista.remove(index);
		}
		return;
	}
	
	public synchronized void removeVoceRubrica(String name) {
		int index = -1;
		
		for(VoceRubrica voce : lista) {
			if(voce.getName().equals(name)) {
				index = lista.indexOf(voce);
				break;
			}
		}
		
		if(index >= 0) {
			System.out.println("This Voce is going to be removed from the Rubrica:\n" + lista.get(index).toString() );
			lista.remove(index);
			System.out.println("Voce has beeen removed correcly!");
			
		}
	}
	
	public synchronized VoceRubrica getVoce(int index) {
		return lista.get(index);
	}
	
	public synchronized VoceRubrica waitingVoce(String name) {
		int index = -1;
		
		do {
			for(VoceRubrica voce : lista) {
				if(voce.getName().equals(name)) {
					index = lista.indexOf(voce);
					break;
				}
			}
			if(index < 0) {
				try {
					System.out.println(Thread.currentThread() + "will wait!");
					wait();
				} catch (InterruptedException e) {e.printStackTrace();}
			}
		} while(index < 0);
		
		System.out.println(Thread.currentThread() + "has found the Voce required!");
		return lista.get(index);
	}
	
	public synchronized void printRubrica() {
		int i = 1;
		for(VoceRubrica voce : lista) {
			System.out.println("Numero " + i + ": ****************");
			System.out.println(voce.toString());
			i++;
		}
	}
	
	///////////////////////////////////////////////////////
	//GETTER e SETTER INUTILI
	public ArrayList<VoceRubrica> getLista() {
		return lista;
	}

	public void setLista(ArrayList<VoceRubrica> lista) {
		this.lista = lista;
	}
}
