package com.SystemEnchere.Test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.SystemeEnchere.metier.Buyer;
import com.SystemeEnchere.metier.IRole;
import com.SystemeEnchere.metier.Seller;
import com.SystemeEnchere.metier.SimpleUserFactory;
import com.SystemeEnchere.metier.SimpleUserFactory.User;;

public class TestUser {
	
	private String login = "CaptainAmerica";
	private String firstName = "John";
	private String lastName = "Doe";
	private IRole seller = new Seller();
	private IRole buyer = new Buyer();
	
	// [Test 1] Test si un utilisateur est crée en respectant les paramètre d'entrée
	@Test
	public void testUser() {
		System.out.println("----------- TEST 1 -----------");
		SimpleUserFactory userFactory = new SimpleUserFactory();
		User user = userFactory.createUser(login, firstName, lastName, seller);		
		System.out.println(user.toString());
		
		assertEquals("Erreur login", user.Login, login);
		assertEquals("Erreur prenom", user.FirstName, firstName);
		assertEquals("Erreur nom", user.LastName, lastName);
		assertEquals("Erreur role", user.getRole(), seller);
		System.out.println("----------- Fin TEST 1 -----------\n");
	}
	
	// [Test 2] Vérifie le rôle d'un utilisateur
	@Test
	public void testGetRole() {
		System.out.println("----------- TEST 2 -----------");
		SimpleUserFactory userFactory = new SimpleUserFactory();
		User user = userFactory.createUser(login, firstName, lastName, buyer);
		assertEquals("Erreur role", user.getRole(), buyer);
		User user2 = userFactory.createUser("Fatality", "Chris", "Brown", seller);
		assertEquals("Erreur role", user2.getRole(), seller);
		System.out.println(user.toString());
		System.out.println(user2.toString());
		System.out.println("----------- Fin TEST 2 -----------\n");
	}

	@Test
	public void testSetRole() {
		SimpleUserFactory userFactory = new SimpleUserFactory();
		//Changement de role à l'execution
		User user = userFactory.createUser(login, firstName, lastName, buyer);
		user.setRole(seller);
		System.out.println(user.toString());
		
		assertEquals("Erreur role", user.getRole(), seller);
	}

}
