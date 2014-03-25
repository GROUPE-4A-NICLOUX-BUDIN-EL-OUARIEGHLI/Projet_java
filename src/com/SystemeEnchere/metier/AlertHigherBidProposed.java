package com.SystemeEnchere.metier;

public class AlertHigherBidProposed implements IAlert {

	@Override
	public String getMessage() {
		return "Un autre acheteur à proposé une enchère supérieur à la vôtre.";
	}

	@Override
	public String getMessage(Object object) {
		// TODO Auto-generated method stub
		return null;
	}
}
