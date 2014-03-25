package com.SystemeEnchere.metier;

import com.SystemeEnchere.metier.SimpleUserFactory.User;

/** R�le de vendeur.
 * 
 * Un vendeur ne peux pas cr�er d'ench�re
 */
public class Seller implements IRole {

	public IOffer doOffer(IUser user, double amount) {
		System.out.println("Erreur : Un vendeur ne peux pas faire d'offre sur une ench�re.");
		return null;
	}
	
	@Override
	public String toString() {
		return "SELLER";
	}

	@Override
	public void configureAlert(boolean AlertReservePriceReached,
			boolean AlertAuctionCanceled, boolean AlertAuctionFinished,
			boolean AlertHigherBidMade) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isWatchingAlertReservePriceReached() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isWatchingAlertAuctionCanceled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isWatchingAlertAuctionFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isWatchingAlertHigherBidMade() {
		// TODO Auto-generated method stub
		return false;
	}
}
