package com.SystemeEnchere.metier;

import java.util.Random;

import com.SystemeEnchere.metier.SimpleUserFactory.User;

public class SimpleProductFactory {
	
	public SimpleProductFactory() {}
	
	public Product createProduct(IUser user, String name) {
		
		if( user == null ) {
			System.out.println("Erreur : L'utilisateur est nul.");
			return null;
		}
		
		if( name == null && name == "" ) {
			System.out.println("Erreur : Le nom du produit n'est pas renseigné.");
			return null;
		}
			
		if( RoleTypeDeterminer.isBuyer((User) user) ) {
			System.out.println("Erreur : Seul les vendeurs peuvent créer et vendre un produit.");
			return null;
		}
		
		return new Product(user, name);
	}
	
	/**
	 * Classe Product
	 * 
	 * Crée un objet qui sera vendu aux enchères.
	 */	
	public class Product implements IProduct {
		
		private String name;
		private String ID;
		private String Description = "Aucune description.";
		private IUser creator;
		
		private Product(IUser user, String name) {
			this.name = name;
			this.creator = user;
			this.ID = generateID();
		}
		
		private String generateID() {
			String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			int len = 6;
			Random random = new Random();

			StringBuilder sb = new StringBuilder( len );
			for( int i = 0; i < len; i++ ) 
				sb.append( str.charAt( random.nextInt(str.length()) ) );
			return sb.toString();
		}
		
		public IUser getCreator() {
			return this.creator;
		}
		
		public void setDescription(String description) {
			this.Description = description;
		}
		
		public String getDescription() {
			return this.Description;
		}
		
		public String getName() {
			return this.name;
		}
		
		public String getID() {
			return this.ID;
		}
		
		@Override
		public String toString() {
			return "--------------------------------------\n" +
					"[" + this.ID + "]" + " " + this.creator.getLogin() + " " + this.name + "\n" + 
					"Description : \n" + this.Description + 
					"\n------------------------------------";	
		}
	}
}
