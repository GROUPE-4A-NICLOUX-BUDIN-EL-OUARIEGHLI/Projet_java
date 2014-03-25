package com.SystemEnchere.Test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.SystemeEnchere.metier.Buyer;
import com.SystemeEnchere.metier.EnumAuctionDuration;
import com.SystemeEnchere.metier.IOffer;
import com.SystemeEnchere.metier.IRole;
import com.SystemeEnchere.metier.Seller;
import com.SystemeEnchere.metier.SimpleAuctionFactory;
import com.SystemeEnchere.metier.SimpleProductFactory;
import com.SystemeEnchere.metier.SimpleUserFactory;
import com.SystemeEnchere.metier.SimpleAuctionFactory.Auction;
import com.SystemeEnchere.metier.SimpleProductFactory.Product;
import com.SystemeEnchere.metier.SimpleUserFactory.User;

public class TestOffer {

	SimpleAuctionFactory auctionFactory = new SimpleAuctionFactory();
	SimpleProductFactory productfactory = new SimpleProductFactory();
	SimpleUserFactory userFactory = new SimpleUserFactory();
	IRole seller = new Seller();
	IRole buyer = new Buyer();
	User userSeller = userFactory.createUser("CaptainAmerica", "John", "Doe", seller);
	User userBuyer = userFactory.createUser("FanFan", "Eric", "Trappe", buyer);
	Product prodTable = productfactory.createProduct(userSeller, "Table en chêne");
	
	// [Test 1] Un acheteur peu proposer une offre, un vendeur non
	@Test
	public void createOfferTest() {
		System.out.println("----------- TEST 1 -----------");
		assertNull(userSeller.doOffer(10)); // un vendeur ne peux pas proposer d'offre
		assertTrue(userBuyer.doOffer(10) instanceof IOffer ); // un acheteur peux proposer une offre
		System.out.println("----------- Fin TEST 1 -----------\n");				
	}
	
	// [Test 2] Test des paramètre d'une offre
	@Test
	public void createOfferParameterTest() {
		System.out.println("----------- TEST 2 -----------");
		assertNull(userBuyer.doOffer(-1)); // offre avec montant négatif
		IOffer offer = userBuyer.doOffer(15);
		assertEquals(userBuyer, offer.getSender()); // L'utilisateur qui à créé l'offre est bien son créateur
		System.out.println("----------- Fin TEST 2 -----------\n");				
	}
}
