package com.SystemeEnchere.metier;

import com.SystemeEnchere.metier.SimpleAuctionFactory.Auction;

public class StateCanceled implements IState {
	
	private Auction auction;
	
	// Le constructeur prends une ench�re en param�tre pour d�clancher une transition vers un autre �tat
	public StateCanceled(Auction auction) {
		this.auction = auction;
	}

	@Override
	public void goToPrivateState() {
		System.out.println("Erreur : Une ench�re annul�e ne peux plus revenir � l'�tat << PRIVATE >>.");
	}

	@Override
	public void goToPublishedState() {
		System.out.println("Erreur : Une ench�re annul�e ne peux plus revenir � l'�tat << PUBLISHED >>.");
	}

	@Override
	public void goToCanceledState() {
		System.out.println("Erreur : L'ench�re est d�j� pass� � l'�tat << CANCELED >>.");		
	}

	@Override
	public void goToFinishedState() {
		System.out.println("Erreur : Une ench�re << CANCELED >> ne peux pas passer � l'�tat << FINISHED >>.");
	}
	
	@Override
	public String toString() {
		return "CANCELED";
		
	}
}
