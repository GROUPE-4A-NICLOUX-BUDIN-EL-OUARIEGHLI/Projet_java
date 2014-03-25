package com.SystemEnchere.Test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import com.SystemeEnchere.metier.*;
import com.SystemeEnchere.metier.SimpleAuctionFactory.Auction;
import com.SystemeEnchere.metier.SimpleProductFactory.Product;
import com.SystemeEnchere.metier.SimpleUserFactory.User;

public class TestSimpleAuctionFactory {
	
	SimpleAuctionFactory auctionFactory = new SimpleAuctionFactory();
	SimpleProductFactory productfactory = new SimpleProductFactory();
	SimpleUserFactory userFactory = new SimpleUserFactory();
	IRole seller = new Seller();
	IRole buyer = new Buyer();
	User userSeller = userFactory.createUser("CaptainAmerica", "John", "Doe", seller);
	User userBuyer = userFactory.createUser("FanFan", "Eric", "Trappe", buyer);
	Product prodTable = productfactory.createProduct(userSeller, "Table en chêne");
	
	// [Test 1] Création d'une enchère avec des paramètres corrects
	@Test
	public void CreateAuctionWithSellerTest() {
		System.out.println("----------- TEST 1 -----------");
		
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		auction.publish(userSeller);
		assertEquals("Erreur création enchère", Auction.class, auction.getClass());
		System.out.println("----------- Fin TEST 1 -----------\n");
	}
	
	// [Test 2] Création d'une enchère avec un utilisateur acheteur (pas possible)
	@Test
	public void CreateAuctionwithBuyerTest() {
		System.out.println("----------- TEST 2 -----------");
		User userSeller = userFactory.createUser("CaptainAmerica", "John", "Doe", buyer);
		Product prodTable = productfactory.createProduct(userSeller, "Table en chêne");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		
		assertNull("Erreur enchère non null", auction);
		System.out.println("----------- Fin TEST 2 -----------\n");
	}
	
	// [Test 3] Création d'une enchère avec un utilisateur null
	@Test
	public void CreateAuctionwithNullUserTest() {
		System.out.println("----------- TEST 3 -----------");
		Auction auction = auctionFactory.createAuction(null, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		
		assertNull("Erreur enchère non null", auction);
		System.out.println("----------- Fin TEST 3 -----------\n");
	}
	
	// [Test 4] Création d'une enchère avec un produit null
	@Test
	public void CreateAuctionwithNullProductTest() {
		System.out.println("----------- TEST 4 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, null, EnumAuctionDuration.DAYS5, 10.00);
		
		assertNull("Erreur enchère non null", auction);
		System.out.println("----------- Fin TEST 4 -----------\n");
	}
	
	// [Test 5] Création d'une enchère avec un utilisateur acheteur (pas possible)
	@Test
	public void CreateAuctionwithNullParametersTest() {
		System.out.println("----------- TEST 5 -----------");
		SimpleAuctionFactory auctionFactory = new SimpleAuctionFactory();
		Auction auction = auctionFactory.createAuction(null, null, EnumAuctionDuration.DAYS5, 10.00);
		
		assertNull("Erreur enchère non null", auction);
		System.out.println("----------- Fin TEST 5 -----------\n");
	}
	
	// [Test 6] Test si la durée de l'enchère est conforme au paramètre de durée spécifié.
	@Test
	public void DurationAuctionTest() {
		System.out.println("----------- TEST 6 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		
		assertEquals(5, auction.getDuration(userSeller));
		System.out.println("----------- Fin TEST 6 -----------\n");
	}
	
	// [Test 7] Test si l'enchère est créee correctement avec le prix spécifié
	@Test
	public void AuctionValidPriceTest() {
		System.out.println("----------- TEST 7 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		assertEquals(10.0, auction.getMinimalPrice(userSeller), 0.0);
		System.out.println("----------- Fin TEST 7 -----------\n");
	}
	
	// [Test 8] Une enchère est crée des mauvais paramètres de prix minimum
	@Test
	public void AuctionConditionsToChangeMinimalPeiceTest() {
		System.out.println("----------- TEST 8 -----------");
		User userBuyer2 = userFactory.createUser("Titus", "Bob", "Spped", buyer);
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, -5.0); // prix minimal négatif
		assertNull(auction );
		auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10); // enchère correcte
		auction.setMinimalPrice(userBuyer2, 50); // un utilisateur qui n'est pas le créateur de l'enchère essai de changer le prix de réserve 
		assertTrue(auction.getMinimalPrice(userSeller) == 10); // vérifie que le prix mini n'a pas changé
		auction.setMinimalPrice(userSeller, 100); // Le vendeur passe le prix mini à 100€
		assertTrue(auction.getMinimalPrice(userSeller) == 100); // vérifie que le prix mini à changé
		auction.publish(userSeller); // Le vendeur publie l'enchère
		auction.setMinimalPrice(userSeller, 200); // Le vendeur essai de passer le prix mini à 200€
		assertFalse(auction.getMinimalPrice(userSeller) == 200); // vérifie ça n'a pas marché
		System.out.println("----------- Fin TEST 8 -----------\n");
	}
	
	// [Test 9] Un utilisateur qui n'est pas le créateur de l'enchère ne peux pas fixer de prix de réserve
	@Test
	public void AuctionInvalidUserToSetResevePriceTest() {
		System.out.println("----------- TEST 9 -----------");
		User userSeller2 = userFactory.createUser("CaptainAmerica", "Alicia", "Page", seller);
		
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller2, 10.0);
		assertEquals("", 0.0, auction.getReservePrice(), 0);
		System.out.println("----------- Fin TEST 9 -----------\n");
	}
	
	// [Test 10] Le prix de réserve est erroné
	@Test
	public void AuctionInvalidResevePriceTest() {
		System.out.println("----------- TEST 10 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller, -1);
		assertEquals("", 0.0, auction.getReservePrice(), 0);
		System.out.println("----------- Fin TEST 10 -----------\n");
	}
	
	// [Test 11] L'utilisateur qui a crée l'enchère modifie le prix de réserve alors qu'il a annulé l'enchère (impossible)
	@Test
	public void AuctionResevePriceOnInvalidStateTest() {
		System.out.println("----------- TEST 11 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.publish(userSeller);
		auction.cancel(userSeller);
		//auction.setReservePrice(userSeller, 10);
		///assertEquals(0.0, auction.getReservePrice(), 0);
		System.out.println("----------- Fin TEST 11 -----------\n");
	}
	
	// [Test 12] L'utilisateur qui a crée l'enchère modifie le prix de réserve correctement
	@Test
	public void AuctionUserCreatorSetValidResevePriceTest() {
		System.out.println("----------- TEST 12 -----------");
		
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller, 10.0);
		assertEquals("", 10.0, auction.getReservePrice(), 0);
		System.out.println("----------- Fin TEST 12 -----------\n");
	}
	
	// [Test 13] Un acheteur propose une offre valide sur une enchère public
	@Test
	public void AuctionUserDoValidOfferTest() {
		System.out.println("----------- TEST 13 -----------");
		Boolean answer = false;
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller, 10.0); // Le créateur de l'enchère passe le prix de réserve à 10€
		auction.publish(userSeller); // Le créateur de l'enchère publie l'enchère
		auction.addObserver(userBuyer); // l'acheteur suit l'enchère
		answer = auction.bid(userBuyer.doOffer(30));
		assertTrue(answer);
		System.out.println("----------- Fin TEST 13 -----------\n");				
	}
	
	// [Test 15] Un acheteur ne peut pas émettre d'offre sur une enchère qui n'est pas public
		@Test
		public void AuctionUserDoValidOfferOnNotPublicAuctionTest() {
			System.out.println("----------- TEST 15 -----------");
			Boolean answer = false;
			Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
			auction.setReservePrice(userSeller, 10.0); // Le créateur de l'enchère passe le prix de réserve à 10€
			auction.addObserver(userBuyer); // l'acheteur suit l'enchère
			answer = auction.bid(userBuyer.doOffer(30));
			assertFalse(answer); // l'enchère n'a pas été validée
			System.out.println("----------- Fin TEST 15 -----------\n");		
			
		}
	
	// [Test 14] Un acheteur qui ne suit pas une enchère ne peux pas émettre d'offre sur celle-ci
	@Test
	public void AuctionUserCannotBidWithoutFollowTest() {
		System.out.println("----------- TEST 14 -----------");
		User userSeller = userFactory.createUser("Usopp", "John", "Doe", seller);
		User userBuyerFollow = userFactory.createUser("Klerkk", "Eric", "Trappe", buyer);
		User userBuyerNotFollow = userFactory.createUser("Klerkk", "Eric", "Trappe", buyer);
		Boolean answer = false;
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller, 10.0); // Le créateur de l'enchère passe le prix de réserve à 10€
		auction.publish(userSeller); // Le créateur de l'enchère publie l'enchère
		auction.addObserver(userBuyerFollow); // l'acheteur suit l'enchère
		answer = auction.bid(userBuyerFollow.doOffer(30));
		assertTrue(answer);
		answer = auction.bid(userBuyerNotFollow.doOffer(30));
		assertFalse(answer);
		System.out.println("----------- Fin TEST 14 -----------\n");	
	}
	
	
	// [Test 16] L'enchère possède une date limite au delà de laquelle elle se termine
	// - L'enchère passe bien à l'état Finished et est corretement géré
	// - La date de création de l'enchère et sa date de fin concordent avec sa durée
	@Test
	public void AuctionFinishedStateTest() throws ParseException {
		System.out.println("----------- TEST 16 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller, 10);
		auction.publish(userSeller);
		
		System.out.println("Date limite de l'enchère : " + auction.getLimitDate());
		auction.setState(new StateFinished(auction)); // L'etat Finished est forcé, la transition ne s'affiche donc pas
		System.out.println("Etat actuel : " + auction.getState().toString()); // Etat actuel de l'enchère
		
		// Test : limitDate - duration = testCreationDate
		// Si trueCreationDate == testCreationDate -> Test ok
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date limitDate = formatter.parse(auction.getLimitDate());
		Date trueCreationDate = formatter.parse(auction.getCreationDate(userSeller));
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(limitDate);
		cal.add(Calendar.DATE, - auction.getDuration(userSeller));
		Date testCreationDate = cal.getTime();
		assertEquals(trueCreationDate, testCreationDate);
		System.out.println("----------- Fin TEST 16 -----------\n");
	}
	
	// [Test 17] Une offre doit être supéririeur au prix minimum de l'enchère
	@Test
	public void AuctionUOfferAmountLessThanMinimalPriceTest() {
		System.out.println("----------- TEST 17 -----------");
		Boolean answer = false;
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		System.out.println("Prix mimimum : " + auction.getMinimalPrice(userSeller));
		auction.addObserver(userBuyer); // l'acheteur suit l'enchère
		answer = auction.bid(userBuyer.doOffer(10)); // l'offre est inférieur au prix mini
		assertFalse(answer); // l'enchère n'a pas été validée
		System.out.println("----------- Fin TEST 17 -----------\n");		
		
	}
	
	// [Test 18] Le prix minimum est visible par tout les utilisateurs du système
	@Test
	public void AuctionMinimalPriceFreeAcessToAllUserTest() {
		System.out.println("----------- TEST 18 -----------");
		User userBuyer2 = userFactory.createUser("Klerkk", "Eric", "Trappe", buyer);
		User userSeller2 = userFactory.createUser("BipBip", "Rob", "Zombie", seller);
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller, 100);
		// etat privatized
		System.out.println("Prix mimimum : " + auction.getMinimalPrice(userSeller));
		assertTrue(auction.getMinimalPrice(userSeller) == 20); // vendeur créateur regarde le prix
		assertFalse(auction.getMinimalPrice(userBuyer) == 20); // L'acheteur essai de voir le prix (fct pas, retourne -1)
		assertFalse(auction.getMinimalPrice(userSeller2) == 20); // un autre vendeur regarde le prix
		// Etat published
		auction.publish(userSeller); // le vendeur publie son enchère
		assertTrue(auction.getMinimalPrice(userSeller) == 20); // vendeur créateur regarde le prix
		assertTrue(auction.getMinimalPrice(userBuyer) == 20); // L'acheteur regarde le prix de vente
		auction.addObserver(userBuyer); // l'acheteur suit l'enchère
		auction.bid(userBuyer.doOffer(21)); // l'acheteur fait une offre
		assertTrue(auction.getMinimalPrice(userBuyer) == 20); // L'acheteur qui vient de faire une offre regarde le prix de vente
		assertTrue(auction.getMinimalPrice(userSeller2) == 20); // un autre vendeur regarde le prix
		// Etat canceled
		auction.cancel(userSeller); // le vendeur annule son enchère
		assertTrue(auction.getMinimalPrice(userSeller) == 20); // vendeur créateur  regarde le prix
		assertTrue(auction.getMinimalPrice(userBuyer) == 20); // L'acheteur qui a fait une offre regarde le prix de vente
		assertFalse(auction.getMinimalPrice(userSeller2) == 20); // un autre vendeur regarde le prix (pas possible)
		assertFalse(auction.getMinimalPrice(userBuyer2) == 20); // L'acheteur qui n'a pas fait d'offre essai de regarder le prix de vente (pas possible)
		// L'acheteur qui n'a pas fait d'offre n'est pas dans la liste des acheteurs pour l'enchère : non trouvé
		// vu qu'il n'a pas fait d'offre et que l'enchère est annulée : accès refuse
		
		// Etat finished (on ne peux pas passer de canceled à finished, on répète avec une nouvelle enchère)
		System.out.println("Test pour état << FINISHED >>.");
		Auction auction2 = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction2.setReservePrice(userSeller, 100);
		auction2.setState(new StateFinished(auction2)); // Force l'état terminé
		System.out.println("Etat courant : " + auction2.getState().toString());
		assertTrue(auction2.getMinimalPrice(userSeller) == 20); // vendeur créateur regarde le prix
		assertFalse(auction2.getMinimalPrice(userBuyer) == 20); // L'acheteur qui a fait une offre regarde le prix de vente
		assertFalse(auction2.getMinimalPrice(userSeller2) == 20); // un autre vendeur regarde le prix (pas possible)
		assertFalse(auction2.getMinimalPrice(userBuyer2) == 20); // L'acheteur qui n'a pas fait d'offre essai de regarder le prix de vente 
		System.out.println("----------- Fin TEST 18 -----------\n");		
		
	}
	
	// [Test 19] Un acheteur peut savoir si le prix de réserve a été atteint par son offre ou par celle d'un autre acheteur.
	@Test
	public void AuctionAlertReservePricereachedTest() {
		System.out.println("----------- TEST 19 -----------");
		User userBuyer = userFactory.createUser("T1tanFall", "Georges", "Bush", buyer);
		User userBuyer2 = userFactory.createUser("Klerkk", "Eric", "Trappe", buyer);
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller, 10.0); // Le créateur de l'enchère passe le prix de réserve à 10€
		auction.publish(userSeller); // Le créateur de l'enchère publie l'enchère
		auction.addObserver(userBuyer); // l'acheteur suit l'enchère
		auction.addObserver(userBuyer2); // l'acheteur2 suit l'enchère
		auction.bid(userBuyer.doOffer(30)); // cette offre sépasse le prix de réserve
		System.out.println(userBuyer.getLastMessage().toString());
		System.out.println(userBuyer2.getLastMessage().toString());
		assertTrue(userBuyer.getLastMessage().getMessage().equals(AlertReservePriceReached.specificMessage)); // message specifique : Votre offre...
		assertTrue(userBuyer2.getLastMessage().getMessage().equals(AlertReservePriceReached.genericMessage)); // message générique : L'offre d"un autre acheteur...
		System.out.println("----------- Fin TEST 19 -----------\n");				
	}
	
	// [Test 20] Le vendeur à la possibilité d'annuler une enchère, si et seulement si, 
	//aucune offre sur cette enchère n'a atteint le prix de réserve de l'enchère.
	@Test
	public void AuctionCancelWithReservePriceExceededTest() {
		System.out.println("----------- TEST 20 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller, 50); // Le créateur de l'enchère passe le prix de réserve à 10€
		auction.publish(userSeller); // Le vendeur publie l'enchère
		auction.addObserver(userBuyer); // Un acheteur suit cette enchère
		auction.bid(userBuyer.doOffer(55)); // L'acheteur fait une offre qui dépasse le prix de réserve
		auction.cancel(userSeller); // le vendeur essai d'annuler son enchère
		assertFalse(auction.getState() instanceof StateCanceled); // pas possible
		System.out.println("----------- Fin TEST 20 -----------\n");
	}
	
	// [Test 21] Le vendeur annule une enchère dont aucune offre ne dépasse le prix de réserve
	@Test
	public void AuctionCancelWithReservePriceNotExceededTest() {
		System.out.println("----------- TEST 21 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller, 50); // Le créateur de l'enchère passe le prix de réserve à 10€
		auction.publish(userSeller); // Le vendeur publie l'enchère
		auction.addObserver(userBuyer); // Un acheteur suit cette enchère
		auction.bid(userBuyer.doOffer(25)); // L'acheteur fait une offre qui dépasse le prix de réserve
		auction.cancel(userSeller); // le vendeur essai d'annuler son enchère
		assertTrue(auction.getState() instanceof StateCanceled); // pas possible
		System.out.println("----------- Fin TEST 21 -----------\n");
	}
	
	// [Test 22] Le vendeur ne peux pas enchérir sur son enchère
	@Test
	public void AuctionCreatorCanNotBidTest() {
		System.out.println("----------- TEST 22 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller, 50); // Le créateur de l'enchère passe le prix de réserve à 10€
		auction.publish(userSeller); // Le vendeur publie l'enchère
		boolean answer = auction.bid(userSeller.doOffer(25)); // le vendeur fait une offre
		assertFalse(answer); // pas possible
		System.out.println("----------- Fin TEST 22 -----------\n");
	}
	
	
}
