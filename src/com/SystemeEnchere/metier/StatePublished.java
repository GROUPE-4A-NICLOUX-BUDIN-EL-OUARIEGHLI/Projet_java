package com.SystemeEnchere.metier;
import com.SystemeEnchere.metier.SimpleAuctionFactory.Auction;

public class StatePublished implements IState {

	private Auction auction;
	
	public StatePublished(Auction auction) {
		this.auction = auction;
	}
	
	@Override
	public void goToPrivateState() {
		System.out.println("Erreur : Une ench�re publi�e ne peux plus revenir � l'�tat << PRIVATE >>.");	
	}

	@Override
	public void goToPublishedState() {
		System.out.println("Erreur : L'ench�re est d�j� dans l'�tat << PUBLISHED >>.");
	}

	@Override
	public void goToCanceledState() {
		
		IOffer lastOffer = this.auction.getLastOffer();
		
		if (lastOffer != null) {	
			if (lastOffer.getAmount() >= this.auction.getReservePrice()) {
				System.out.println("Erreur : Impossible de passer � l'�tat << CANCELED >>, le montant d'une offre d�passe le prix de r�serve.");
				return;
			}
		}
		
		if (lastOffer == null) {
			System.out.println("Pas d'offre sur cette ench�re (null).");
		}
		
		this.auction.setState(this.auction.getCanceledState());
		System.out.println("L'ench�re est pass� de l'�tat << PUBLISHED >> � l'�tat << CANCELED >>.");		
	}

	@Override
	public void goToFinishedState() {
		// Test si date courrante > date limite
		this.auction.setState(this.auction.getFinishedState());
		auction.notifyObervers(new AlertAuctionFinished());
		System.out.println("L'ench�re est pass� de l'�tat << PUBLISHED >> � l'�tat << FINISHED >>.");
	}
	
	@Override
	public String toString() {
		return "PUBLISHED";
	}

}
