package com.SystemeEnchere.metier;

public class AlertOfferReceived implements IAlert {

	public final static String Message = "Un acheteur � �mis une offre sur votre ench�re.";
	
	@Override
	public String getMessage() {
		return "Un acheteur � �mis une offre sur votre ench�re.";
	}

	@Override
	public String getMessage(Object object) {
		// TODO Auto-generated method stub
		return null;
	}
}
