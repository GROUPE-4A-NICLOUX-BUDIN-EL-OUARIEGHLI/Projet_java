package com.SystemeEnchere.metier;
import com.SystemeEnchere.metier.SimpleAuctionFactory.Auction;

public class StatePublished implements IState {

	private Auction auction;
	
	public StatePublished(Auction auction) {
		this.auction = auction;
	}
	
	@Override
	public void goToPrivateState() {
		System.out.println("Erreur : Une enchère publiée ne peux plus revenir à l'état << PRIVATE >>.");	
	}

	@Override
	public void goToPublishedState() {
		System.out.println("Erreur : L'enchère est déjà dans l'état << PUBLISHED >>.");
	}

	@Override
	public void goToCanceledState() {
		
		IOffer lastOffer = this.auction.getLastOffer();
		
		if (lastOffer != null) {	
			if (lastOffer.getAmount() >= this.auction.getReservePrice()) {
				System.out.println("Erreur : Impossible de passer à l'état << CANCELED >>, le montant d'une offre dépasse le prix de réserve.");
				return;
			}
		}
		
		if (lastOffer == null) {
			System.out.println("Pas d'offre sur cette enchère (null).");
		}
		
		this.auction.setState(this.auction.getCanceledState());
		System.out.println("L'enchère est passé de l'état << PUBLISHED >> à l'état << CANCELED >>.");		
	}

	@Override
	public void goToFinishedState() {
		// Test si date courrante > date limite
		this.auction.setState(this.auction.getFinishedState());
		auction.notifyObervers(new AlertAuctionFinished());
		System.out.println("L'enchère est passé de l'état << PUBLISHED >> à l'état << FINISHED >>.");
	}
	
	@Override
	public String toString() {
		return "PUBLISHED";
	}

}
