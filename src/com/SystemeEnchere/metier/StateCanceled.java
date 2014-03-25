package com.SystemeEnchere.metier;

import com.SystemeEnchere.metier.SimpleAuctionFactory.Auction;

public class StateCanceled implements IState {
	
	private Auction auction;
	
	// Le constructeur prends une enchère en paramètre pour déclancher une transition vers un autre état
	public StateCanceled(Auction auction) {
		this.auction = auction;
	}

	@Override
	public void goToPrivateState() {
		System.out.println("Erreur : Une enchère annulée ne peux plus revenir à l'état << PRIVATE >>.");
	}

	@Override
	public void goToPublishedState() {
		System.out.println("Erreur : Une enchère annulée ne peux plus revenir à l'état << PUBLISHED >>.");
	}

	@Override
	public void goToCanceledState() {
		System.out.println("Erreur : L'enchère est déjà passé à l'état << CANCELED >>.");		
	}

	@Override
	public void goToFinishedState() {
		System.out.println("Erreur : Une enchère << CANCELED >> ne peux pas passer à l'état << FINISHED >>.");
	}
	
	@Override
	public String toString() {
		return "CANCELED";
		
	}
}
