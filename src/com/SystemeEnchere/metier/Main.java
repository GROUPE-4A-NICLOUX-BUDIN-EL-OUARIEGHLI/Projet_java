package com.SystemeEnchere.metier;

import com.SystemeEnchere.metier.SimpleAuctionFactory.Auction;
import com.SystemeEnchere.metier.SimpleProductFactory.Product;
import com.SystemeEnchere.metier.SimpleUserFactory.User;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String description = "Cette petite table en chêne de fabrication européenne, d'allure à la fois rustique et sophistiquée, est une création de Simon Pengelly." +
				"\nAussi à sa place dans une cuisine que dans une salle à manger, elle accueillera jusqu'à 6 personnes. " +
				"\nSon assemblage à onglets et ses angles arrondis confèrent de l'élégance à cette gamme de meubles de salle à manger et de chambre en chêne massif." +
				"\n\nDimensions : l.90 x H.74 x L.150 cm ";
		
		IRole seller = new Seller();
		IRole buyer = new Buyer();
		
		SimpleUserFactory userFactory = new SimpleUserFactory();
		
		User userSeller = userFactory.createUser("CaptainAmerica", "John", "Doe", seller);
		User userBuyer = userFactory.createUser("TotoFunky", "Johnny", "Tall", buyer);
		User userBuyer2 = userFactory.createUser("Fatality", "Eddy", "Kling", buyer);
		
		SimpleProductFactory productFactory = new SimpleProductFactory();
		Product prodTable = productFactory.createProduct(userSeller, "Table en chêne");
		prodTable.setDescription(description);
		//System.out.println(prodTable.toString());
		
		Product productSofa = productFactory.createProduct(userSeller, "Sofa");
		productSofa.setDescription("Ce sofa est très confortable et se nettoie facilement!");
		//System.out.println(productSofa.toString());
		
		SimpleAuctionFactory auctionFactory = new SimpleAuctionFactory();
		Auction auction = auctionFactory.createAuction(userSeller, productSofa, EnumAuctionDuration.DAYS5, 10.0);
		
		System.out.println("Date de création: " + auction.getCreationDate(userSeller));
		System.out.println("Date de fin: " + auction.getLimitDate());
		
		System.out.println("Etat de l'enchère: " + auction.getState().toString());
		auction.publish(userSeller);
		System.out.println("Etat de l'enchère: " + auction.getState().toString());
		auction.privatize(userSeller);
		System.out.println("Etat de l'enchère: " + auction.getState().toString());
		
		auction.addObserver(userBuyer);
		auction.addObserver(userBuyer2);
		auction.bid(userBuyer.doOffer(12));
		
		System.out.println(userSeller.getLastMessage());
		System.out.println(userBuyer.getLastMessage());
		
		System.out.println(auction.hasMakeAnOffer(userSeller));
	}
}
