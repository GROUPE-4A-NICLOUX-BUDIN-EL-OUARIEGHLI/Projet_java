package com.SystemeEnchere.metier;

import java.util.ArrayList;

public class SimpleUserFactory {
	
	public SimpleUserFactory() {}
	
	public User createUser(String login, String firstName, String lastName, IRole role) {
		
		if (login == "") {
			System.out.println("Erreur : Le login est nul, L'utilisateur n'a pas �t� cr�e.");
			return null;
		}
		if (firstName == "") {
			System.out.println("Erreur : Le pr�nom est nul, , L'utilisateur n'a pas �t� cr�e.");
			return null; 
		}
		if (lastName == "") {
			System.out.println("Erreur : Le nom est nul, , L'utilisateur n'a pas �t� cr�e.");
			return null;
		}
		if (role == null) {
			System.out.println("Erreur : Le r�le est nul, , L'utilisateur n'a pas �t� cr�e.");
			return null; 
		}
		
		return new User(login, firstName, lastName, role);	
	}
	
	public class User implements IUser, IObserver{
		
		public String Login = "";
		public String FirstName = "";
		public String LastName = "";
		public IRole Role;
		
		private ArrayList<IMessage> messageHistory;
		private IMessage LastMessage;
		
		private User(String login, String firstName, String lastName, IRole role) {
			this.Login = login;
			this.FirstName = firstName;
			this.LastName = lastName;
			this.Role = role;
			this.messageHistory = new ArrayList<IMessage>();
		}
		
		public String getLogin() {
			return this.Login;
		}
		
		public IRole getRole() {
			return this.Role;
		}
		
		public void setRole(IRole newRole) {
			this.Role = newRole;
		}
		
		public IMessage getLastMessage() {
			if (this.LastMessage != null)
				return this.LastMessage;
			else return null;
		}
		
		public String showLastMessage() {
			if (this.LastMessage != null)
				return this.LastMessage.toString();
			else return "Aucun message re�u.";
		}

		@Override
		public void update(IObservable sender, Object args) {
			IMessage message = (Message) args;
			this.LastMessage = message;
			this.messageHistory.add(message);
		}

		public IOffer doOffer(double amount) {
			if (amount <= 0) {
				System.out.println("Erreur : Le montant de l'offre doit �tre sup�rieur � 0�.");
				return null;
			}
			IOffer offer = this.Role.doOffer(this, amount);
			return offer;
		}
		
		@Override
		public String toString() {
			return this.Login + " " + this.FirstName + " " + this.LastName + " " + this.Role.toString();
		}
	}
}
