package com.SystemeEnchere.metier;

public class AlertAuctionCanceled implements IAlert{

	@Override
	public String getMessage() {
		return "L'enchère à été annulée.";
	}

	@Override
	public String getMessage(Object object) {
		// TODO Auto-generated method stub
		return null;
	}	
}
