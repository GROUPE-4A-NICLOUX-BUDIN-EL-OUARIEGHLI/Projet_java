package com.SystemeEnchere.metier;

public class AlertAuctionCanceled implements IAlert{

	@Override
	public String getMessage() {
		return "L'ench�re � �t� annul�e.";
	}

	@Override
	public String getMessage(Object object) {
		// TODO Auto-generated method stub
		return null;
	}	
}
