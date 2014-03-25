package com.SystemeEnchere.metier;

import com.SystemeEnchere.metier.SimpleAuctionFactory.Auction;

public class StateFinished implements IState {

	private Auction auction;

	public StateFinished(Auction auction) {
		this.auction = auction;
	}
	
	@Override
	public void goToPrivateState() {
		System.out.println("Erreur : Une enchère terminée ne peux plus revenir à l'état << PRIVATE >>.");
	}

	@Override
	public void goToPublishedState() {
		System.out.println("Erreur : Une enchère terminée ne peux plus revenir à l'état << PUBLISHED >>.");
	}

	@Override
	public void goToCanceledState() {
		System.out.println("Erreur : Une enchère << FINISHED >> passer à l'état << CANCELED >>.");
	}

	@Override
	public void goToFinishedState() {
		System.out.println("Erreur : L'enchère est déjà à l'état << FINISHED >>.");
	}
	
	@Override
	public String toString() {
		return "FINISHED";		
	}
}
