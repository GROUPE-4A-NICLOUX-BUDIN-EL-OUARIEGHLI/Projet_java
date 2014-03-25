package com.SystemeEnchere.metier;

import com.SystemeEnchere.metier.SimpleAuctionFactory.Auction;

public class StateFinished implements IState {

	private Auction auction;

	public StateFinished(Auction auction) {
		this.auction = auction;
	}
	
	@Override
	public void goToPrivateState() {
		System.out.println("Erreur : Une ench�re termin�e ne peux plus revenir � l'�tat << PRIVATE >>.");
	}

	@Override
	public void goToPublishedState() {
		System.out.println("Erreur : Une ench�re termin�e ne peux plus revenir � l'�tat << PUBLISHED >>.");
	}

	@Override
	public void goToCanceledState() {
		System.out.println("Erreur : Une ench�re << FINISHED >> passer � l'�tat << CANCELED >>.");
	}

	@Override
	public void goToFinishedState() {
		System.out.println("Erreur : L'ench�re est d�j� � l'�tat << FINISHED >>.");
	}
	
	@Override
	public String toString() {
		return "FINISHED";		
	}
}
