package com.SystemeEnchere.metier;

public class AlertReservePriceReached implements IAlert {
	
	private IUser user; // Utilisateur dont l'offre � atteint le prix de r�serve
	public final static String specificMessage = "Votre ench�re viens de d�passer le prix de r�serve de l'ench�re.";
	public final static String genericMessage = "Une offre �mise par un autre acheteur viens de d�passer le prix de r�serve.";
	
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
