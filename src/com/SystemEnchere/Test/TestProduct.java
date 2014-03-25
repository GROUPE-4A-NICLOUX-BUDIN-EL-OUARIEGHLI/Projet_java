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
	Product prodTable = productfactory.createProduct(userSeller, "Table en ch�ne");
	
	String description = "Cette petite table en ch�ne de fabrication europ�enne, d'allure � la fois rustique et sophistiqu�e, " + 
			"\nest une cr�ation de Simon Pengelly. Aussi � sa place dans une cuisine que dans une salle � manger, elle accueillera" + 
			"\n jusqu'� 6 personnes." +
			"\nSon assemblage � onglets et ses angles arrondis conf�rent de l'�l�gance � cette gamme de meubles de salle � manger" +
			"\net de chambre en ch�ne massif. \nDimensions : l.90 x H.74 x L.150 cm ";
	
	// [Test 1] Cr�ation d'un produit valide
	@Test
	public void createValidProductTest() {
		System.out.println("----------- TEST 1 -----------");
		Product productTable = productfactory.createProduct(userSeller, "Table en ch�ne");
		productTable.setDescription(description);
		assertTrue(productTable.getName().equals("Table en ch�ne")); // le nom du produit est correct
		assertTrue(productTable.getDescription().equals(description)); // le nom du produit est correct
		assertEquals(productTable.getCreator(),userSeller ); // Le propri�taire de l'objet est bien son cr�ateur
		System.out.println(productTable.toString());
		System.out.println("----------- Fin TEST 1 -----------\n");
	}
	
	// [Test 2] Cr�ation d'un produit avec un acheteur (impossible)
	@Test
	public void createProductWithBuyer() {
		System.out.println("----------- TEST 2 -----------");
		Product productTable = productfactory.createProduct(userBuyer, "Table en ch�ne");
		assertNull(productTable);
		System.out.println("----------- Fin TEST 2 -----------\n");
	}
	
	// [Test 3] Cr�ation d'un produit avec un acheteur null (impossible)
	@Test
	public void createProductWithNullUser() {
		System.out.println("----------- TEST 3 -----------");
		Product productTable = productfactory.createProduct(null, "Table en ch�ne");
		assertNull(productTable);
		System.out.println("----------- Fin TEST 3 -----------\n");
	}
	
	// [Test 4] Cr�ation d'un produit avec un nom vide (impossible)
	@Test
	public void createProductWithEmptyName() {
		System.out.println("----------- TEST 4 -----------");
		Product productTable = productfactory.createProduct(userBuyer, "");
		assertNull(productTable);
		System.out.println("----------- Fin TEST 4 -----------\n");
	}
}
