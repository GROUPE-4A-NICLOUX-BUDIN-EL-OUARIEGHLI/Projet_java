package com.SystemeEnchere.metier;

public class AlertReservePriceReached implements IAlert {
	
	private IUser user; // Utilisateur dont l'offre à atteint le prix de réserve
	public final static String specificMessage = "Votre enchère viens de dépasser le prix de réserve de l'enchère.";
	public final static String genericMessage = "Une offre émise par un autre acheteur viens de dépasser le prix de réserve.";
	
	public AlertReservePriceReached(IUser userWithOfferExeedReservePrice) {
		this.user = userWithOfferExeedReservePrice;
	}
	
	@Override @Deprecated
	public String getMessage() {
		return "";
	}
	
	public IUser getUserWithOfferExeedReservePrice() {
		return this.user;
	}
	
	public String getMessage(Object object) {
		IUser user = (IUser) object;
		if (user.equals(this.user)) {
			return specificMessage;
		}
		return genericMessage;
	}
}
