package com.SystemeEnchere.metier;

import com.SystemeEnchere.metier.SimpleAuctionFactory.Auction;

public class StatePrivatized implements IState {
	
	private Auction auction;
	
	// Le constructeur prends une enchère en paramètre pour déclancher une transition vers un autre état
	public StatePrivatized(Auction auction) {
		this.auction = auction;
	}

	@Override
	public void goToPrivateState() {
		System.out.println("Erreur : L'enchère est déjà dans l'état << PRIVATE >>.");
	}

	@Override
	public void goToPublishedState() {
		this.auction.setState(this.auction.getPublishedState());
		System.out.println("L'enchère est passé de l'état << PRIVATE >> à l'état << PUBLISHED >>.");
	}

	@Override
	public void goToCanceledState() {
		this.auction.setState(this.auction.getCanceledState());
		System.out.println("L'enchère est passé de l'état << PRIVATE >> à l'état << CANCELED >>.");		
	}

	@Override
	public void goToFinishedState() {
		// Test si date courrante > date limite
		this.auction.setState(this.auction.getFinishedState());
		auction.notifyObervers(new AlertAuctionFinished());
		System.out.println("L'enchère est passé à l'état << FINISHED >>.");
	}
	
	@Override
	public String toString() {
		return "PRIVATE";
		
	}

}
