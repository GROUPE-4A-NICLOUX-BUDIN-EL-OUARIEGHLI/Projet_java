package com.SystemeEnchere.metier;

public class AlertAuctionFinished implements IAlert {

	@Override
	public String getMessage() {
		return "L'ench�re est termin�e.";
	}

	@Override
	public String getMessage(Object object) {
		// TODO Auto-generated method stub
		return null;
	}
}
