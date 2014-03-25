package com.SystemeEnchere.metier;

import com.SystemeEnchere.metier.SimpleUserFactory.User;

/** 
 * <b>Interface de marquage.</b>
 * 
 * <p> En impl�mantant cette interface, une classe d�fini un r�le.</p>
 */
public interface IRole {
	
	public IOffer doOffer(IUser user, double amount);
	public void configureAlert(
			boolean AlertReservePriceReached, 
			boolean AlertAuctionCanceled, 
			boolean AlertAuctionFinished,
			boolean AlertHigherBidMade );

	public boolean isWatchingAlertReservePriceReached();
	public boolean isWatchingAlertAuctionCanceled();
	public boolean isWatchingAlertAuctionFinished();
	public boolean isWatchingAlertHigherBidMade();
}
