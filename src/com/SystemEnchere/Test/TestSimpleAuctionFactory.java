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
	Product prodTable = productfactory.createProduct(userSeller, "Table en ch�ne");
	
	// [Test 1] Cr�ation d'une ench�re avec des param�tres corrects
	@Test
	public void CreateAuctionWithSellerTest() {
		System.out.println("----------- TEST 1 -----------");
		
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		auction.publish(userSeller);
		assertEquals("Erreur cr�ation ench�re", Auction.class, auction.getClass());
		System.out.println("----------- Fin TEST 1 -----------\n");
	}
	
	// [Test 2] Cr�ation d'une ench�re avec un utilisateur acheteur (pas possible)
	@Test
	public void CreateAuctionwithBuyerTest() {
		System.out.println("----------- TEST 2 -----------");
		User userSeller = userFactory.createUser("CaptainAmerica", "John", "Doe", buyer);
		Product prodTable = productfactory.createProduct(userSeller, "Table en ch�ne");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		
		assertNull("Erreur ench�re non null", auction);
		System.out.println("----------- Fin TEST 2 -----------\n");
	}
	
	// [Test 3] Cr�ation d'une ench�re avec un utilisateur null
	@Test
	public void CreateAuctionwithNullUserTest() {
		System.out.println("----------- TEST 3 -----------");
		Auction auction = auctionFactory.createAuction(null, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		
		assertNull("Erreur ench�re non null", auction);
		System.out.println("----------- Fin TEST 3 -----------\n");
	}
	
	// [Test 4] Cr�ation d'une ench�re avec un produit null
	@Test
	public void CreateAuctionwithNullProductTest() {
		System.out.println("----------- TEST 4 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, null, EnumAuctionDuration.DAYS5, 10.00);
		
		assertNull("Erreur ench�re non null", auction);
		System.out.println("----------- Fin TEST 4 -----------\n");
	}
	
	// [Test 5] Cr�ation d'une ench�re avec un utilisateur acheteur (pas possible)
	@Test
	public void CreateAuctionwithNullParametersTest() {
		System.out.println("----------- TEST 5 -----------");
		SimpleAuctionFactory auctionFactory = new SimpleAuctionFactory();
		Auction auction = auctionFactory.createAuction(null, null, EnumAuctionDuration.DAYS5, 10.00);
		
		assertNull("Erreur ench�re non null", auction);
		System.out.println("----------- Fin TEST 5 -----------\n");
	}
	
	// [Test 6] Test si la dur�e de l'ench�re est conforme au param�tre de dur�e sp�cifi�.
	@Test
	public void DurationAuctionTest() {
		System.out.println("----------- TEST 6 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		
		assertEquals(5, auction.getDuration(userSeller));
		System.out.println("----------- Fin TEST 6 -----------\n");
	}
	
	// [Test 7] Test si l'ench�re est cr�ee correctement avec le prix sp�cifi�
	@Test
	public void AuctionValidPriceTest() {
		System.out.println("----------- TEST 7 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		assertEquals(10.0, auction.getMinimalPrice(userSeller), 0.0);
		System.out.println("----------- Fin TEST 7 -----------\n");
	}
	
	// [Test 8] Une ench�re est cr�e des mauvais param�tres de prix minimum
	@Test
	public void AuctionConditionsToChangeMinimalPeiceTest() {
		System.out.println("----------- TEST 8 -----------");
		User userBuyer2 = userFactory.createUser("Titus", "Bob", "Spped", buyer);
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, -5.0); // prix minimal n�gatif
		assertNull(auction );
		auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10); // ench�re correcte
		auction.setMinimalPrice(userBuyer2, 50); // un utilisateur qui n'est pas le cr�ateur de l'ench�re essai de changer le prix de r�serve 
		assertTrue(auction.getMinimalPrice(userSeller) == 10); // v�rifie que le prix mini n'a pas chang�
		auction.setMinimalPrice(userSeller, 100); // Le vendeur passe le prix mini � 100�
		assertTrue(auction.getMinimalPrice(userSeller) == 100); // v�rifie que le prix mini � chang�
		auction.publish(userSeller); // Le vendeur publie l'ench�re
		auction.setMinimalPrice(userSeller, 200); // Le vendeur essai de passer le prix mini � 200�
		assertFalse(auction.getMinimalPrice(userSeller) == 200); // v�rifie �a n'a pas march�
		System.out.println("----------- Fin TEST 8 -----------\n");
	}
	
	// [Test 9] Un utilisateur qui n'est pas le cr�ateur de l'ench�re ne peux pas fixer de prix de r�serve
	@Test
	public void AuctionInvalidUserToSetResevePriceTest() {
		System.out.println("----------- TEST 9 -----------");
		User userSeller2 = userFactory.createUser("CaptainAmerica", "Alicia", "Page", seller);
		
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller2, 10.0);
		assertEquals("", 0.0, auction.getReservePrice(), 0);
		System.out.println("----------- Fin TEST 9 -----------\n");
	}
	
	// [Test 10] Le prix de r�serve est erron�
	@Test
	public void AuctionInvalidResevePriceTest() {
		System.out.println("----------- TEST 10 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller, -1);
		assertEquals("", 0.0, auction.getReservePrice(), 0);
		System.out.println("----------- Fin TEST 10 -----------\n");
	}
	
	// [Test 11] L'utilisateur qui a cr�e l'ench�re modifie le prix de r�serve alors qu'il a annul� l'ench�re (impossible)
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
	
	// [Test 12] L'utilisateur qui a cr�e l'ench�re modifie le prix de r�serve correctement
	@Test
	public void AuctionUserCreatorSetValidResevePriceTest() {
		System.out.println("----------- TEST 12 -----------");
		
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller, 10.0);
		assertEquals("", 10.0, auction.getReservePrice(), 0);
		System.out.println("----------- Fin TEST 12 -----------\n");
	}
	
	// [Test 13] Un acheteur propose une offre valide sur une ench�re public
	@Test
	public void AuctionUserDoValidOfferTest() {
		System.out.println("----------- TEST 13 -----------");
		Boolean answer = false;
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller, 10.0); // Le cr�ateur de l'ench�re passe le prix de r�serve � 10�
		auction.publish(userSeller); // Le cr�ateur de l'ench�re publie l'ench�re
		auction.addObserver(userBuyer); // l'acheteur suit l'ench�re
		answer = auction.bid(userBuyer.doOffer(30));
		assertTrue(answer);
		System.out.println("----------- Fin TEST 13 -----------\n");				
	}
	
	// [Test 15] Un acheteur ne peut pas �mettre d'offre sur une ench�re qui n'est pas public
		@Test
		public void AuctionUserDoValidOfferOnNotPublicAuctionTest() {
			System.out.println("----------- TEST 15 -----------");
			Boolean answer = false;
			Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
			auction.setReservePrice(userSeller, 10.0); // Le cr�ateur de l'ench�re passe le prix de r�serve � 10�
			auction.addObserver(userBuyer); // l'acheteur suit l'ench�re
			answer = auction.bid(userBuyer.doOffer(30));
			assertFalse(answer); // l'ench�re n'a pas �t� valid�e
			System.out.println("----------- Fin TEST 15 -----------\n");		
			
		}
	
	// [Test 14] Un acheteur qui ne suit pas une ench�re ne peux pas �mettre d'offre sur celle-ci
	@Test
	public void AuctionUserCannotBidWithoutFollowTest() {
		System.out.println("----------- TEST 14 -----------");
		User userSeller = userFactory.createUser("Usopp", "John", "Doe", seller);
		User userBuyerFollow = userFactory.createUser("Klerkk", "Eric", "Trappe", buyer);
		User userBuyerNotFollow = userFactory.createUser("Klerkk", "Eric", "Trappe", buyer);
		Boolean answer = false;
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller, 10.0); // Le cr�ateur de l'ench�re passe le prix de r�serve � 10�
		auction.publish(userSeller); // Le cr�ateur de l'ench�re publie l'ench�re
		auction.addObserver(userBuyerFollow); // l'acheteur suit l'ench�re
		answer = auction.bid(userBuyerFollow.doOffer(30));
		assertTrue(answer);
		answer = auction.bid(userBuyerNotFollow.doOffer(30));
		assertFalse(answer);
		System.out.println("----------- Fin TEST 14 -----------\n");	
	}
	
	
	// [Test 16] L'ench�re poss�de une date limite au del� de laquelle elle se termine
	// - L'ench�re passe bien � l'�tat Finished et est corretement g�r�
	// - La date de cr�ation de l'ench�re et sa date de fin concordent avec sa dur�e
	@Test
	public void AuctionFinishedStateTest() throws ParseException {
		System.out.println("----------- TEST 16 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller, 10);
		auction.publish(userSeller);
		
		System.out.println("Date limite de l'ench�re : " + auction.getLimitDate());
		auction.setState(new StateFinished(auction)); // L'etat Finished est forc�, la transition ne s'affiche donc pas
		System.out.println("Etat actuel : " + auction.getState().toString()); // Etat actuel de l'ench�re
		
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
	
	// [Test 17] Une offre doit �tre sup�ririeur au prix minimum de l'ench�re
	@Test
	public void AuctionUOfferAmountLessThanMinimalPriceTest() {
		System.out.println("----------- TEST 17 -----------");
		Boolean answer = false;
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		System.out.println("Prix mimimum : " + auction.getMinimalPrice(userSeller));
		auction.addObserver(userBuyer); // l'acheteur suit l'ench�re
		answer = auction.bid(userBuyer.doOffer(10)); // l'offre est inf�rieur au prix mini
		assertFalse(answer); // l'ench�re n'a pas �t� valid�e
		System.out.println("----------- Fin TEST 17 -----------\n");		
		
	}
	
	// [Test 18] Le prix minimum est visible par tout les utilisateurs du syst�me
	@Test
	public void AuctionMinimalPriceFreeAcessToAllUserTest() {
		System.out.println("----------- TEST 18 -----------");
		User userBuyer2 = userFactory.createUser("Klerkk", "Eric", "Trappe", buyer);
		User userSeller2 = userFactory.createUser("BipBip", "Rob", "Zombie", seller);
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller, 100);
		// etat privatized
		System.out.println("Prix mimimum : " + auction.getMinimalPrice(userSeller));
		assertTrue(auction.getMinimalPrice(userSeller) == 20); // vendeur cr�ateur regarde le prix
		assertFalse(auction.getMinimalPrice(userBuyer) == 20); // L'acheteur essai de voir le prix (fct pas, retourne -1)
		assertFalse(auction.getMinimalPrice(userSeller2) == 20); // un autre vendeur regarde le prix
		// Etat published
		auction.publish(userSeller); // le vendeur publie son ench�re
		assertTrue(auction.getMinimalPrice(userSeller) == 20); // vendeur cr�ateur regarde le prix
		assertTrue(auction.getMinimalPrice(userBuyer) == 20); // L'acheteur regarde le prix de vente
		auction.addObserver(userBuyer); // l'acheteur suit l'ench�re
		auction.bid(userBuyer.doOffer(21)); // l'acheteur fait une offre
		assertTrue(auction.getMinimalPrice(userBuyer) == 20); // L'acheteur qui vient de faire une offre regarde le prix de vente
		assertTrue(auction.getMinimalPrice(userSeller2) == 20); // un autre vendeur regarde le prix
		// Etat canceled
		auction.cancel(userSeller); // le vendeur annule son ench�re
		assertTrue(auction.getMinimalPrice(userSeller) == 20); // vendeur cr�ateur  regarde le prix
		assertTrue(auction.getMinimalPrice(userBuyer) == 20); // L'acheteur qui a fait une offre regarde le prix de vente
		assertFalse(auction.getMinimalPrice(userSeller2) == 20); // un autre vendeur regarde le prix (pas possible)
		assertFalse(auction.getMinimalPrice(userBuyer2) == 20); // L'acheteur qui n'a pas fait d'offre essai de regarder le prix de vente (pas possible)
		// L'acheteur qui n'a pas fait d'offre n'est pas dans la liste des acheteurs pour l'ench�re : non trouv�
		// vu qu'il n'a pas fait d'offre et que l'ench�re est annul�e : acc�s refuse
		
		// Etat finished (on ne peux pas passer de canceled � finished, on r�p�te avec une nouvelle ench�re)
		System.out.println("Test pour �tat << FINISHED >>.");
		Auction auction2 = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction2.setReservePrice(userSeller, 100);
		auction2.setState(new StateFinished(auction2)); // Force l'�tat termin�
		System.out.println("Etat courant : " + auction2.getState().toString());
		assertTrue(auction2.getMinimalPrice(userSeller) == 20); // vendeur cr�ateur regarde le prix
		assertFalse(auction2.getMinimalPrice(userBuyer) == 20); // L'acheteur qui a fait une offre regarde le prix de vente
		assertFalse(auction2.getMinimalPrice(userSeller2) == 20); // un autre vendeur regarde le prix (pas possible)
		assertFalse(auction2.getMinimalPrice(userBuyer2) == 20); // L'acheteur qui n'a pas fait d'offre essai de regarder le prix de vente 
		System.out.println("----------- Fin TEST 18 -----------\n");		
		
	}
	
	// [Test 19] Un acheteur peut savoir si le prix de r�serve a �t� atteint par son offre ou par celle d'un autre acheteur.
	@Test
	public void AuctionAlertReservePricereachedTest() {
		System.out.println("----------- TEST 19 -----------");
		User userBuyer = userFactory.createUser("T1tanFall", "Georges", "Bush", buyer);
		User userBuyer2 = userFactory.createUser("Klerkk", "Eric", "Trappe", buyer);
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller, 10.0); // Le cr�ateur de l'ench�re passe le prix de r�serve � 10�
		auction.publish(userSeller); // Le cr�ateur de l'ench�re publie l'ench�re
		auction.addObserver(userBuyer); // l'acheteur suit l'ench�re
		auction.addObserver(userBuyer2); // l'acheteur2 suit l'ench�re
		auction.bid(userBuyer.doOffer(30)); // cette offre s�passe le prix de r�serve
		System.out.println(userBuyer.getLastMessage().toString());
		System.out.println(userBuyer2.getLastMessage().toString());
		assertTrue(userBuyer.getLastMessage().getMessage().equals(AlertReservePriceReached.specificMessage)); // message specifique : Votre offre...
		assertTrue(userBuyer2.getLastMessage().getMessage().equals(AlertReservePriceReached.genericMessage)); // message g�n�rique : L'offre d"un autre acheteur...
		System.out.println("----------- Fin TEST 19 -----------\n");				
	}
	
	// [Test 20] Le vendeur � la possibilit� d'annuler une ench�re, si et seulement si, 
	//aucune offre sur cette ench�re n'a atteint le prix de r�serve de l'ench�re.
	@Test
	public void AuctionCancelWithReservePriceExceededTest() {
		System.out.println("----------- TEST 20 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller, 50); // Le cr�ateur de l'ench�re passe le prix de r�serve � 10�
		auction.publish(userSeller); // Le vendeur publie l'ench�re
		auction.addObserver(userBuyer); // Un acheteur suit cette ench�re
		auction.bid(userBuyer.doOffer(55)); // L'acheteur fait une offre qui d�passe le prix de r�serve
		auction.cancel(userSeller); // le vendeur essai d'annuler son ench�re
		assertFalse(auction.getState() instanceof StateCanceled); // pas possible
		System.out.println("----------- Fin TEST 20 -----------\n");
	}
	
	// [Test 21] Le vendeur annule une ench�re dont aucune offre ne d�passe le prix de r�serve
	@Test
	public void AuctionCancelWithReservePriceNotExceededTest() {
		System.out.println("----------- TEST 21 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller, 50); // Le cr�ateur de l'ench�re passe le prix de r�serve � 10�
		auction.publish(userSeller); // Le vendeur publie l'ench�re
		auction.addObserver(userBuyer); // Un acheteur suit cette ench�re
		auction.bid(userBuyer.doOffer(25)); // L'acheteur fait une offre qui d�passe le prix de r�serve
		auction.cancel(userSeller); // le vendeur essai d'annuler son ench�re
		assertTrue(auction.getState() instanceof StateCanceled); // pas possible
		System.out.println("----------- Fin TEST 21 -----------\n");
	}
	
	// [Test 22] Le vendeur ne peux pas ench�rir sur son ench�re
	@Test
	public void AuctionCreatorCanNotBidTest() {
		System.out.println("----------- TEST 22 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 20);
		auction.setReservePrice(userSeller, 50); // Le cr�ateur de l'ench�re passe le prix de r�serve � 10�
		auction.publish(userSeller); // Le vendeur publie l'ench�re
		boolean answer = auction.bid(userSeller.doOffer(25)); // le vendeur fait une offre
		assertFalse(answer); // pas possible
		System.out.println("----------- Fin TEST 22 -----------\n");
	}
	
	
}
