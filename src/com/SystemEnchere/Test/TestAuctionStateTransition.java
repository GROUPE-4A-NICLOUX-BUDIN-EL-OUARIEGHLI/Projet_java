package com.SystemEnchere.Test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.SystemeEnchere.metier.EnumAuctionDuration;
import com.SystemeEnchere.metier.IRole;
import com.SystemeEnchere.metier.Seller;
import com.SystemeEnchere.metier.SimpleAuctionFactory;
import com.SystemeEnchere.metier.SimpleProductFactory;
import com.SystemeEnchere.metier.SimpleUserFactory;
import com.SystemeEnchere.metier.SimpleAuctionFactory.Auction;
import com.SystemeEnchere.metier.SimpleProductFactory.Product;
import com.SystemeEnchere.metier.SimpleUserFactory.User;
import com.SystemeEnchere.metier.StateCanceled;
import com.SystemeEnchere.metier.StateFinished;
import com.SystemeEnchere.metier.StatePrivatized;
import com.SystemeEnchere.metier.StatePublished;

public class TestAuctionStateTransition {
	
	SimpleAuctionFactory auctionFactory = new SimpleAuctionFactory();
	SimpleProductFactory productfactory = new SimpleProductFactory();
	SimpleUserFactory userFactory = new SimpleUserFactory();
	IRole seller = new Seller(); // L'utilisateur est un vendeur
	User userSeller = userFactory.createUser("CaptainAmerica", "John", "Doe", seller);
	Product prodTable = productfactory.createProduct(userSeller, "Table en chêne");
	
	// ############## Etat Privatized vers les autres états #################
	
	@Test
	public void StatePrivatizedToPrivatizedTest() {
		System.out.println("----------- TEST 1 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		auction.privatize(userSeller);
		assertTrue( auction.getState() instanceof StatePrivatized);
		assertFalse( auction.getState() instanceof StatePublished);
		assertFalse( auction.getState() instanceof StateCanceled);
		assertFalse( auction.getState() instanceof StateFinished);
		System.out.println("----------- Fin TEST 1 -----------\n");
	}
	
	@Test
	public void StatePrivatizedToPublishedTest() {
		System.out.println("----------- TEST 2 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		auction.privatize(userSeller);
		auction.publish(userSeller);
		assertFalse( auction.getState() instanceof StatePrivatized);
		assertTrue( auction.getState() instanceof StatePublished);
		assertFalse( auction.getState() instanceof StateCanceled);
		assertFalse( auction.getState() instanceof StateFinished);
		System.out.println("----------- Fin TEST 2 -----------\n");
	}
	
	@Test
	public void StatePrivatizedToCanceledTest() {
		System.out.println("----------- TEST 3 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		auction.cancel(userSeller);
		assertFalse( auction.getState() instanceof StatePrivatized);
		assertFalse( auction.getState() instanceof StatePublished);
		assertTrue( auction.getState() instanceof StateCanceled);
		assertFalse( auction.getState() instanceof StateFinished);
		System.out.println("----------- Fin TEST 3 -----------\n");
	}
	
	@Test
	public void StatePrivatizedToFinishedTest() {
		System.out.println("----------- TEST 4 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		auction.finish(); // dépend normalement du temps, le compte à rebours n'est pas implémenté
		assertFalse( auction.getState() instanceof StatePrivatized);
		assertFalse(auction.getState() instanceof StatePublished);
		assertFalse(auction.getState() instanceof StateCanceled);
		assertTrue(auction.getState() instanceof StateFinished);
		System.out.println("----------- Fin TEST 4 -----------\n");
	}
	
	// ############## Etat Published vers les autres états #################
	
	@Test
	public void StatePublishedToPrivatizedTest() {
		System.out.println("----------- TEST 5 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		auction.publish(userSeller);
		auction.privatize(userSeller);
		assertFalse( auction.getState() instanceof StatePrivatized);
		assertTrue(auction.getState() instanceof StatePublished);
		assertFalse(auction.getState() instanceof StateCanceled);
		assertFalse(auction.getState() instanceof StateFinished);
		System.out.println("----------- Fin TEST 5 -----------\n");
	}
	
	@Test
	public void StatePublishedToPublishedTest() {
		System.out.println("----------- TEST 6 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		auction.publish(userSeller);
		auction.publish(userSeller);
		assertFalse( auction.getState() instanceof StatePrivatized);
		assertTrue(auction.getState() instanceof StatePublished);
		assertFalse(auction.getState() instanceof StateCanceled);
		assertFalse(auction.getState() instanceof StateFinished);
		System.out.println("----------- Fin TEST 6 -----------\n");
	}
	
	@Test
	public void StatePublishedToCanceledTest() {
		System.out.println("----------- TEST 7 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		auction.publish(userSeller);
		auction.cancel(userSeller);
		assertFalse( auction.getState() instanceof StatePrivatized);
		assertFalse(auction.getState() instanceof StatePublished);
		assertTrue(auction.getState() instanceof StateCanceled);
		assertFalse(auction.getState() instanceof StateFinished);
		System.out.println("----------- Fin TEST 7 -----------\n");
	}
	
	@Test
	public void StatePublishedToFinishedTest() {
		System.out.println("----------- TEST 8 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		auction.publish(userSeller);
		auction.finish();
		assertFalse( auction.getState() instanceof StatePrivatized);
		assertFalse(auction.getState() instanceof StatePublished);
		assertFalse(auction.getState() instanceof StateCanceled);
		assertTrue(auction.getState() instanceof StateFinished);
		System.out.println("----------- Fin TEST 8 -----------\n");
	}
	
	// ############## Etat Canceled vers les autres états #################
	
	@Test
	public void StateCanceledToPrivatizedTest() {
		System.out.println("----------- TEST 9 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		auction.publish(userSeller);
		auction.cancel(userSeller);
		auction.privatize(userSeller);
		assertFalse( auction.getState() instanceof StatePrivatized);
		assertFalse(auction.getState() instanceof StatePublished);
		assertTrue(auction.getState() instanceof StateCanceled);
		assertFalse(auction.getState() instanceof StateFinished);
		System.out.println("----------- Fin TEST 9 -----------\n");
	}
	
	@Test
	public void StateCanceledToPublishedTest() {
		System.out.println("----------- TEST 10 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		auction.publish(userSeller);
		auction.cancel(userSeller);
		auction.publish(userSeller);
		assertFalse( auction.getState() instanceof StatePrivatized);
		assertFalse(auction.getState() instanceof StatePublished);
		assertTrue(auction.getState() instanceof StateCanceled);
		assertFalse(auction.getState() instanceof StateFinished);
		System.out.println("----------- Fin TEST 10 -----------\n");
	}
	
	@Test
	public void StateCanceledToCanceledTest() {
		System.out.println("----------- TEST 11 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		auction.publish(userSeller);
		auction.cancel(userSeller);
		auction.cancel(userSeller);
		assertFalse( auction.getState() instanceof StatePrivatized);
		assertFalse(auction.getState() instanceof StatePublished);
		assertTrue(auction.getState() instanceof StateCanceled);
		assertFalse(auction.getState() instanceof StateFinished);
		System.out.println("----------- Fin TEST 11 -----------\n");
	}
	
	@Test
	public void StateCanceledToFinishedTest() {
		System.out.println("----------- TEST 12 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		auction.publish(userSeller);
		auction.cancel(userSeller);
		auction.finish();
		assertFalse( auction.getState() instanceof StatePrivatized);
		assertFalse(auction.getState() instanceof StatePublished);
		assertTrue(auction.getState() instanceof StateCanceled);
		assertFalse(auction.getState() instanceof StateFinished);
		System.out.println("----------- Fin TEST 12 -----------\n");
	}

	// ############## Etat Finished vers les autres états #################
	
	@Test
	public void StateFinishedToProvatizeTest() {
		System.out.println("----------- TEST 13 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		auction.publish(userSeller);
		auction.finish();
		auction.privatize(userSeller);
		assertFalse( auction.getState() instanceof StatePrivatized);
		assertFalse(auction.getState() instanceof StatePublished);
		assertFalse(auction.getState() instanceof StateCanceled);
		assertTrue(auction.getState() instanceof StateFinished);
		System.out.println("----------- Fin TEST 13 -----------\n");
	}
	
	@Test
	public void StateFinishedToPublishedTest() {
		System.out.println("----------- TEST 14 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		auction.publish(userSeller);
		auction.finish();
		auction.publish(userSeller);
		assertFalse( auction.getState() instanceof StatePrivatized);
		assertFalse(auction.getState() instanceof StatePublished);
		assertFalse(auction.getState() instanceof StateCanceled);
		assertTrue(auction.getState() instanceof StateFinished);
		System.out.println("----------- Fin TEST 14 -----------\n");
	}
	
	@Test
	public void StateFinishedToCanceledTest() {
		System.out.println("----------- TEST 15 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		auction.publish(userSeller);
		auction.finish();
		auction.cancel(userSeller);
		assertFalse( auction.getState() instanceof StatePrivatized);
		assertFalse(auction.getState() instanceof StatePublished);
		assertFalse(auction.getState() instanceof StateCanceled);
		assertTrue(auction.getState() instanceof StateFinished);
		System.out.println("----------- Fin TEST 15 -----------\n");
	}
	
	@Test
	public void StateFinishedToFinishedTest() {
		System.out.println("----------- TEST 16 -----------");
		Auction auction = auctionFactory.createAuction(userSeller, prodTable, EnumAuctionDuration.DAYS5, 10.00);
		auction.publish(userSeller);
		auction.finish();
		auction.finish();
		assertFalse( auction.getState() instanceof StatePrivatized);
		assertFalse(auction.getState() instanceof StatePublished);
		assertFalse(auction.getState() instanceof StateCanceled);
		assertTrue(auction.getState() instanceof StateFinished);
		System.out.println("----------- Fin TEST 16 -----------\n");
	}
}
