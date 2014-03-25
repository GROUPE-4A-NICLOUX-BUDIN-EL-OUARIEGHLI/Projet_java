package com.SystemeEnchere.metier;

import com.SystemeEnchere.metier.SimpleUserFactory.User;

/**
 * Un acheteur peux faire des offres sur des enchères publics
 */

public class Buyer implements IRole {
	
	// Par défaut l'utilisateur est Alerté de tout les énènements
	private boolean AlertReservePriceReached; // être averti si le prix de réserve est atteint
	private boolean AlertAuctionCanceled; 
	private boolean AlertAuctionFinished;
	private boolean AlertHigherBidMade; // être averti si une enchère supérieure est émise par un autre acheteur
	
	public Buyer() {
		this.AlertReservePriceReached = true;
		this.AlertAuctionCanceled = true;
		this.AlertAuctionFinished = true;
		this.AlertHigherBidMade = true;
	}

	public IOffer doOffer(IUser user, double amount) {
		
		IOffer offer = new Offer(user, amount);
		return offer;
	}
	
	/**
	 * 
	 * @param user : l'acheteur veux mofifier la config des alertes
	 * @param AlertReservePriceReached
	 * @param AlertAuctionCanceled
	 * @param AlertAuctionFinished
	 * @param AlertHigherBidMade
	 * @return true, alertes modidiées, false sinon
	 */
	@Override
	public void configureAlert(
				boolean alertReservePriceReached, 
				boolean alertAuctionCanceled, 
				boolean alertAuctionFinished,
				boolean alertHigherBidMade ) {
		
		this.AlertReservePriceReached = alertReservePriceReached;
		this.AlertAuctionCanceled = alertAuctionCanceled;
		this.AlertAuctionFinished = alertAuctionFinished;
		this.AlertHigherBidMade = alertHigherBidMade;
	}
	
	@Override
	public boolean isWatchingAlertReservePriceReached() {
		return this.AlertReservePriceReached;
	}
	
	@Override
	public boolean isWatchingAlertAuctionCanceled() {
		return this.AlertAuctionCanceled;
	}
	
	@Override
	public boolean isWatchingAlertAuctionFinished() {
		return this.AlertAuctionFinished;
	}
	
	@Override
	public boolean isWatchingAlertHigherBidMade() {
		return this.AlertHigherBidMade;
	}
	
	@Override
	public String toString() {
		return "BUYER";
	}
}
