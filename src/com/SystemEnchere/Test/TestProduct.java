package com.SystemEnchere.Test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.SystemeEnchere.metier.Buyer;
import com.SystemeEnchere.metier.IRole;
import com.SystemeEnchere.metier.Seller;
import com.SystemeEnchere.metier.SimpleAuctionFactory;
import com.SystemeEnchere.metier.SimpleProductFactory;
import com.SystemeEnchere.metier.SimpleUserFactory;
import com.SystemeEnchere.metier.SimpleProductFactory.Product;
import com.SystemeEnchere.metier.SimpleUserFactory.User;

public class TestProduct {
	
	SimpleAuctionFactory auctionFactory = new SimpleAuctionFactory();
	SimpleProductFactory productfactory = new SimpleProductFactory();
	SimpleUserFactory userFactory = new SimpleUserFactory();
	IRole seller = new Seller();
	IRole buyer = new Buyer();
	User userSeller = userFactory.createUser("CaptainAmerica", "John", "Doe", seller);
	User userBuyer = userFactory.createUser("FanFan", "Eric", "Trappe", buyer);
	Product prodTable = productfactory.createProduct(userSeller, "Table en chêne");
	
	String description = "Cette petite table en chêne de fabrication européenne, d'allure à la fois rustique et sophistiquée, " + 
			"\nest une création de Simon Pengelly. Aussi à sa place dans une cuisine que dans une salle à manger, elle accueillera" + 
			"\n jusqu'à 6 personnes." +
			"\nSon assemblage à onglets et ses angles arrondis confèrent de l'élégance à cette gamme de meubles de salle à manger" +
			"\net de chambre en chêne massif. \nDimensions : l.90 x H.74 x L.150 cm ";
	
	// [Test 1] Création d'un produit valide
	@Test
	public void createValidProductTest() {
		System.out.println("----------- TEST 1 -----------");
		Product productTable = productfactory.createProduct(userSeller, "Table en chêne");
		productTable.setDescription(description);
		assertTrue(productTable.getName().equals("Table en chêne")); // le nom du produit est correct
		assertTrue(productTable.getDescription().equals(description)); // le nom du produit est correct
		assertEquals(productTable.getCreator(),userSeller ); // Le propriétaire de l'objet est bien son créateur
		System.out.println(productTable.toString());
		System.out.println("----------- Fin TEST 1 -----------\n");
	}
	
	// [Test 2] Création d'un produit avec un acheteur (impossible)
	@Test
	public void createProductWithBuyer() {
		System.out.println("----------- TEST 2 -----------");
		Product productTable = productfactory.createProduct(userBuyer, "Table en chêne");
		assertNull(productTable);
		System.out.println("----------- Fin TEST 2 -----------\n");
	}
	
	// [Test 3] Création d'un produit avec un acheteur null (impossible)
	@Test
	public void createProductWithNullUser() {
		System.out.println("----------- TEST 3 -----------");
		Product productTable = productfactory.createProduct(null, "Table en chêne");
		assertNull(productTable);
		System.out.println("----------- Fin TEST 3 -----------\n");
	}
	
	// [Test 4] Création d'un produit avec un nom vide (impossible)
	@Test
	public void createProductWithEmptyName() {
		System.out.println("----------- TEST 4 -----------");
		Product productTable = productfactory.createProduct(userBuyer, "");
		assertNull(productTable);
		System.out.println("----------- Fin TEST 4 -----------\n");
	}
}
