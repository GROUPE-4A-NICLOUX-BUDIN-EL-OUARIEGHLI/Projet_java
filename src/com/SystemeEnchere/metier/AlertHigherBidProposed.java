package com.SystemeEnchere.metier;

public class AlertHigherBidProposed implements IAlert {

	@Override
	public String getMessage() {
		return "Un autre acheteur � propos� une ench�re sup�rieur � la v�tre.";
	}

	@Override
	public String getMessage(Object object) {
		// TODO Auto-generated method stub
		return null;
	}
}
