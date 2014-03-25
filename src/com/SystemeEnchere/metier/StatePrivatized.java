package com.SystemeEnchere.metier;

import com.SystemeEnchere.metier.SimpleAuctionFactory.Auction;

public class StatePrivatized implements IState {
	
	private Auction auction;
	
	// Le constructeur prends une ench�re en param�tre pour d�clancher une transition vers un autre �tat
	public StatePrivatized(Auction auction) {
		this.auction = auction;
	}

	@Override
	public void goToPrivateState() {
		System.out.println("Erreur : L'ench�re est d�j� dans l'�tat << PRIVATE >>.");
	}

	@Override
	public void goToPublishedState() {
		this.auction.setState(this.auction.getPublishedState());
		System.out.println("L'ench�re est pass� de l'�tat << PRIVATE >> � l'�tat << PUBLISHED >>.");
	}

	@Override
	public void goToCanceledState() {
		this.auction.setState(this.auction.getCanceledState());
		System.out.println("L'ench�re est pass� de l'�tat << PRIVATE >> � l'�tat << CANCELED >>.");		
	}

	@Override
	public void goToFinishedState() {
		// Test si date courrante > date limite
		this.auction.setState(this.auction.getFinishedState());
		auction.notifyObervers(new AlertAuctionFinished());
		System.out.println("L'ench�re est pass� � l'�tat << FINISHED >>.");
	}
	
	@Override
	public String toString() {
		return "PRIVATE";
		
	}

}
