package com.SystemEnchere.Test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.SystemeEnchere.metier.AlertAuctionCanceled;
import com.SystemeEnchere.metier.AlertAuctionFinished;
import com.SystemeEnchere.metier.AlertOfferReceived;
import com.SystemeEnchere.metier.Buyer;
import com.SystemeEnchere.metier.EnumAuctionDuration;
import com.SystemeEnchere.metier.IAlert;
import com.SystemeEnchere.metier.IRole;
import com.SystemeEnchere.metier.Seller;
import com.SystemeEnchere.metier.SimpleAuctionFactory;
import com.SystemeEnchere.metier.SimpleAuctionFactory.Auction;
import com.SystemeEnchere.metier.SimpleProductFactory;
import com.SystemeEnchere.metier.SimpleProductFactory.Product;
import com.SystemeEnchere.metier.SimpleUserFactory;
import com.SystemeEnchere.metier.SimpleUserFactory.User;

public class TestAlerte {
	
	SimpleAuctionFactory auctionFactory = new SimpleAuctionFactory();
	SimpleProductFactory productfactory = new SimpleProductFactory();
	SimpleUserFactory userFactory = new SimpleUserFactory();
	IRole seller = new Seller();
	IRole buyer = new Buyer();
	User userSeller = userFactory.createUser("CaptainAmerica", "John", "Doe", seller);
	User userBuyer = userFactory.createUser("FanFan", "Bruce", "Wayne", buyer);
	Product prodTable = productfactory.createProduct(userSeller, "Table en chêne");
	
	
	
	
	// [Test 1] Le vendeur reçoit une alerte quand une offre est faite sur son enchère
	@Test
	public void AuctionAlertOfferReceivedTest() {
		System.out.println("----------- TEST 1 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller, 50); // Le créateur de l'enchère passe le prix de réserve à 10€
		auction.publish(userSeller); // Le vendeur publie l'enchère
		auction.addObserver(userBuyer); // L'acheteur suit l'enchère
		auction.bid(userBuyer.doOffer(25)); // le l'acheteur fait une offre
		System.out.println(userSeller.getLastMessage().toString());
		assertTrue(userSeller.getLastMessage().getMessage().equals(AlertOfferReceived.Message));
		System.out.println("----------- Fin TEST 1 -----------\n");
	}
	
	// [Test 2] Une offre est émise sur une enchère, vérifie qu'un acheteur ne reçoi pas d'alerte
	@Test
	public void AlertOfferReceivedToBuyerTest() {
		System.out.println("----------- TEST 2 -----------");
		User userSeller = userFactory.createUser("CaptainAmerica", "John", "Doe", seller);
		User userBuyer = userFactory.createUser("FanFan", "Bruce", "Wayne", buyer);
		Product prodTable = productfactory.createProduct(userSeller, "Table en chêne");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS3, 10);
		auction.setReservePrice(userSeller, 50);
		auction.publish(userSeller); // Le vendeur publie son enchère
		auction.addObserver(userBuyer); // Un acheteur décide de suivre cette enchère
		auction.bid(userBuyer.doOffer(11)); // L'acheteur propose une offre de 11 euros
		//System.out.println(userBuyer.getLastMessage().toString());
		// L'acheteur viens d'être initilisé et n'a pas encore reçu de message, si il n'a pas reçu de message, il renvoi null
		assertNull(userBuyer.getLastMessage()); // 
		System.out.println("----------- Fin TEST 2 -----------\n");
	}
	
	// [Test 4] L'utilisateur configure les alertes qu'il désir recevoir
		@Test
		public void AlertUserConfigureAlertTest() {
			System.out.println("----------- TEST 4 -----------");
			User userBuyer = userFactory.createUser("FanFan", "Bruce", "Wayne", buyer);
			User userSeller = userFactory.createUser("BipBip", "Stephane", "Tigre", seller);
			userBuyer.getRole().configureAlert(true, true, true, true);
			assertTrue(userBuyer.getRole().isWatchingAlertAuctionCanceled());
			assertTrue(userBuyer.getRole().isWatchingAlertAuctionFinished());
			assertTrue(userBuyer.getRole().isWatchingAlertHigherBidMade());
			assertTrue(userBuyer.getRole().isWatchingAlertReservePriceReached());
			
			userBuyer.getRole().configureAlert(false, false, false, false);
			assertFalse(userBuyer.getRole().isWatchingAlertAuctionCanceled());
			assertFalse(userBuyer.getRole().isWatchingAlertAuctionFinished());
			assertFalse(userBuyer.getRole().isWatchingAlertHigherBidMade());
			assertFalse(userBuyer.getRole().isWatchingAlertReservePriceReached());
			
			userBuyer.getRole().configureAlert(false, false, true, true);
			assertFalse(userBuyer.getRole().isWatchingAlertAuctionCanceled());
			assertTrue(userBuyer.getRole().isWatchingAlertAuctionFinished());
			assertTrue(userBuyer.getRole().isWatchingAlertHigherBidMade());
			assertFalse(userBuyer.getRole().isWatchingAlertReservePriceReached());
			
			// un vendeur ne peux pas confirurer d'alerte
			userBuyer.getRole().configureAlert(true, true, true, true);
			assertFalse(userSeller.getRole().isWatchingAlertAuctionCanceled());
			assertFalse(userSeller.getRole().isWatchingAlertAuctionFinished());
			assertFalse(userSeller.getRole().isWatchingAlertHigherBidMade());
			assertFalse(userSeller.getRole().isWatchingAlertReservePriceReached());	
			System.out.println("----------- Fin TEST 4 -----------\n");
		}

	/**
	 * [Test 3] Une enchère est annulée
	 * - un vendeur ne reçoi pas de message d'annulation d'enchère
	 * - les acheteur qui écoutent les alertes d'annulation d'enchère doivent recevoir un message
	 * - les acheteurs qui écoutent pas les alertes d'annulation d'enchère ne doivent pas recevoir un message
	 * 
	 */
	@Test
	public void AlertAuctionCanceledTest() {
		System.out.println("----------- TEST 3 -----------");
		User userSeller = userFactory.createUser("CaptainAmerica", "John", "Doe", new Seller());
		User userBuyer = userFactory.createUser("FanFan", "Bruce", "Wayne", new Buyer());
		User userBuyer2 = userFactory.createUser("Terra", "David", "Bow", new Buyer());
		User userBuyer3 = userFactory.createUser("BigMac", "Dave", "Crit", new Buyer());
		userBuyer2.getRole().configureAlert(true, true, true, true);
		userBuyer2.getRole().configureAlert(true, true, true, true);
		userBuyer3.getRole().configureAlert(true, false, true, true); // userBuyer 3 ne suit pas les alertes "enchère annulé"
		
		System.out.println("Alerte : " + userBuyer.getRole().isWatchingAlertAuctionCanceled());
		Product prodTable = productfactory.createProduct(userSeller, "Table en chêne");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS3, 10);
		auction.setReservePrice(userSeller, 50);
		
		auction.publish(userSeller); // Le vendeur publie son enchère
		auction.addObserver(userBuyer); // Un acheteur décide de suivre cette enchère
		auction.addObserver(userBuyer2);
		auction.addObserver(userBuyer3);
		auction.bid(userBuyer.doOffer(12)); // L'acheteur propose une offre
		
		auction.cancel(userSeller); // L'acheteur annule l'enchère

		IAlert alert = new AlertAuctionCanceled();
		
		boolean answer1 = new String(userSeller.getLastMessage().getMessage()).equals(alert.getMessage()); // le vendeur n'a pas reçu ce type de massage
		System.out.println(userSeller.showLastMessage());
		boolean answer2 = new String(userBuyer.getLastMessage().getMessage()).equals(alert.getMessage());
		System.out.println(userBuyer.showLastMessage());
		boolean answer3 = new String(userBuyer2.getLastMessage().getMessage()).equals(alert.getMessage());
		System.out.println(userBuyer2.showLastMessage());
		boolean answer4 = new String(userBuyer3.showLastMessage()).equals("Aucun message reçu.");
		System.out.println("Bigmac (Buyer3) : " + userBuyer3.showLastMessage());
		assertFalse(answer1); // Vérifie que userSeller n'a pas reçu de message d'enchère annulé
		assertTrue(answer2);
		assertTrue(answer3);
		assertTrue(answer4);
		System.out.println("----------- Fin TEST 3 -----------\n");
	}
	
	/**
	 * [Test 5] Une enchère est terminée
	 * - un vendeur ne reçoi pas de message d'enchère terminé
	 * - les acheteur qui écoutent les alertes enchère terminé doivent recevoir un message
	 * - les acheteurs qui écoutent pas les alertes enchère terminé ne doivent pas recevoir un message
	 * 
	 */
	@Test
	public void AlertAuctionFinishedTest() {
		System.out.println("----------- TEST 5 -----------");
		User userSeller = userFactory.createUser("CaptainAmerica", "John", "Doe", new Seller());
		User userBuyer = userFactory.createUser("FanFan", "Bruce", "Wayne", new Buyer());
		User userBuyer2 = userFactory.createUser("Terra", "David", "Bow", new Buyer());
		User userBuyer3 = userFactory.createUser("BigMac", "Dave", "Crit", new Buyer());
		userBuyer2.getRole().configureAlert(true, true, true, true);
		userBuyer2.getRole().configureAlert(true, true, true, true);
		userBuyer3.getRole().configureAlert(true, true, false, true); // userBuyer 3 ne suit pas les alertes "enchère terminé"
		
		Product prodTable = productfactory.createProduct(userSeller, "Table en chêne");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS3, 10);
		auction.setReservePrice(userSeller, 50);
		
		auction.publish(userSeller); // Le vendeur publie son enchère
		auction.addObserver(userBuyer); // Un acheteur décide de suivre cette enchère
		auction.addObserver(userBuyer2);
		auction.addObserver(userBuyer3);
		auction.bid(userBuyer.doOffer(12)); // L'acheteur propose une offre
		
		auction.finish(); // l'enchère est terminée

		IAlert alert = new AlertAuctionFinished();
		
		boolean answer1 = new String(userSeller.getLastMessage().getMessage()).equals(alert.getMessage()); // le vendeur n'a pas reçu ce type de massage
		System.out.println(userSeller.showLastMessage());
		System.out.println(userBuyer.showLastMessage());
		boolean answer2 = new String(userBuyer.getLastMessage().getMessage()).equals(alert.getMessage());
		System.out.println(userBuyer.showLastMessage());
		boolean answer3 = new String(userBuyer2.getLastMessage().getMessage()).equals(alert.getMessage());
		System.out.println(userBuyer2.showLastMessage());
		boolean answer4 = new String(userBuyer3.showLastMessage()).equals("Aucun message reçu.");
		System.out.println("Bigmac (Buyer3) : " + userBuyer3.showLastMessage());
		assertFalse(answer1); // Vérifie que userSeller n'a pas reçu de message d'enchère annulé
		assertTrue(answer2);
		assertTrue(answer3);
		assertTrue(answer4);
		System.out.println("----------- Fin TEST 5 -----------\n");
	}

}
