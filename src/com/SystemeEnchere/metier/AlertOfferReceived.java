package com.SystemeEnchere.metier;

public class AlertOfferReceived implements IAlert {

	public final static String Message = "Un acheteur à émis une offre sur votre enchère.";
	
	@Override
	public String getMessage() {
		return "Un acheteur à émis une offre sur votre enchère.";
	}

	@Override
	public String getMessage(Object object) {
		// TODO Auto-generated method stub
		return null;
	}
}
