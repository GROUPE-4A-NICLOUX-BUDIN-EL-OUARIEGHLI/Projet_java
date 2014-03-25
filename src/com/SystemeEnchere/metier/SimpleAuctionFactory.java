package com.SystemeEnchere.metier;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;

import com.SystemeEnchere.metier.SimpleUserFactory.User;

public class SimpleAuctionFactory {

	public SimpleAuctionFactory() {}
	
	public Auction createAuction(IUser user, IProduct product, EnumAuctionDuration duration, double minimalPrice) {
		
		if (minimalPrice < 0.01) {
			System.out.println("Erreur : Le prix d'une ench�re doit �tre sup�rieur � 0.01�.");
			return null;
		}
		
		if( user != null && product != null) {
			
			if( RoleTypeDeterminer.isSeller(user) ) {
				System.out.println("Ench�re Cr�ee.");
				return new Auction(user, product, duration, minimalPrice);
			}
				
			System.out.println("Erreur : Seul les vendeurs peuvent cr�er une ench�re.");
		}
		System.out.println("Erreur : L'utilisateur ou le produit est nul.");
		return null;
	}
	
	/* Classe Auction
	 * 
	 * Permet d'effectuer des ench�res
	 */
	
	public class Auction implements IObservable {
		
		private HashMap<IObserver, Boolean > observerList;
		private ArrayList<IOffer> offerList;
		
		private static final int interval = 1000; // 1000ms = 1s
		private Timer timer;
		
		private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		private Date limitDate;
		private Date creationDate;
		private Date endDate;
		
		private IState privatized;
		private IState published;
		private IState canceled;
		private IState finished;
		private IState currentState;
		
		public static final double MinimalOffer = 0.01;
		private double ReservePrice = 0.00;
		private double minimalPrice; // prix fix� par le cr�ateur de l'ench�re
		private double bidAmount = 0.00; // montant de l'ench�re
		
		private IProduct product;
		private String name;
		private IUser creator;
		private IUser actualWinner;
		private int duration;		
		
		private Auction(IUser user, IProduct product, EnumAuctionDuration duration, double price) {
			this.product = product;
			this.creator = user;
			this.minimalPrice = price;
			this.name = "[Ench�re] " + product.getName();
			observerList = new HashMap<IObserver, Boolean>();
			offerList = new ArrayList<IOffer>();
			this.duration = duration.toInt();
			setCreationDate();
			setLimitDate(duration.toInt());
			initState();
		}
		
		private void initState() {
			this.privatized = new StatePrivatized(this);
			this.published = new StatePublished(this);
			this.canceled  = new StateCanceled(this);
			this.finished = new StateFinished(this);
			this.currentState = this.privatized;
		}
		
		/**
		 * Test si un utilisateur est le cr�ateur de l'ench�re
		 * @return true si l'utilisateur est le cr�ateur de l'ench�re, false sinon
		 */
		private boolean isCreator(IUser user) {
			return this.creator.equals(user);
		}
		
		/**
		 * Test si un utilisateur suit l'ench�re (Si il est dans la liste des observateurs)
		 * @param user : Utilisateur a tester
		 * @return : true, l'utilisateur suit l'ench�re, false sinon
		 */
		private boolean isFollowing(IUser user) {
			for (Entry<IObserver, Boolean> entry : this.observerList.entrySet()) {
				User userInList = (User) entry.getKey();
			    if (userInList.equals(user)) return true; 
			}
			return false;
		}
		
		/**
		 * D�termine si un utilisateur peux lire les informations de l'ench�re en fonction de l'�tat de celle-ci
		 * et du type d'utilisateur
		 * @param user
		 * @return true, l'utilisateur peux lire l'info, false sinon
		 */
		private boolean canReadInfo(IUser user) {
			if (isCreator(user) == true) { // Le cr�ateur de l'ench�re � toujours acc�s aux donn�es de ses ench�res
				return true;
			}
			
			if (isCanceled(this.currentState) == true) {
				if (hasMakeAnOffer(user)) { // Si l'utilisateur a fait une offre sur cette ench�re
					return true;
				}
				System.out.println("Erreur : L'ench�re est annul�e, vous n'avez fait aucune offre, acc�s refus�.");
				return false;
			}
			
			if (isPublished(this.currentState) == false) {
				System.out.println("Erreur : L'ench�re n'est pas public, acc�s refus�.");
				return false;
			}
			return true;
		}
		
		private boolean countDown() {
			// non impl�ment�, non indispensable pour tester l'�tat termin�.
			return false;		
		}
		
		// Calcule la date de fin de l'ench�re en additionnant sa dur�e � l'instant de cr�ation
		private void setLimitDate(int duration) {
			Calendar calendar = Calendar.getInstance();
			calendar.add (Calendar.DATE, duration);
			this.endDate = calendar.getTime();
		}
		
		// Sauvegarde la date de cr�ation de l'ench�re
		private void setCreationDate() {
			Calendar calendar = Calendar.getInstance();
			this.creationDate = calendar.getTime();
		}
		
		public String getLimitDate() {
			return dateFormat.format(this.endDate);
		}
		
		// Renvoi la date de cr�ationnde l'ench�re sous forme de chaine de caractere
		public String getCreationDate(IUser user) {
			if (canReadInfo(user) == true) {
				return dateFormat.format(this.creationDate);
			}
			return null;
			
		}
		
		public int getDuration(IUser user) {
			if (canReadInfo(user) == true) {
				return this.duration;
			}
			return -1;
		}
		
		public void setMinimalPrice(User user, double minimalPrice) {
			
			if (isCreator(user) == false) { // Si l'utilisateur n'est pas le cr�ateur de l'ench�re
				System.out.println("Erreur : Vous n'�tes pas le cr�ateur de cette ench�re, vous ne pouvez pas modifier le prix de r�serve.");
				return;
			}
				
			if (this.minimalPrice <= 0) {
				System.out.println("Erreur : Le prix minimal doit �tre sup�rieur � 0�.");
				return;
			}
					
			if (isPrivatized(this.currentState) == false) {
				System.out.println("Erreur : Le prix minimal ne peux �tre modidi� que si l'ench�re est � l'�tat Privatized. Etat courant: " + this.currentState.toString() + ".");
				return;
			}	
			
			this.minimalPrice = minimalPrice;
			System.out.println("Le prix minimal est maintenant de " + minimalPrice + "�");
		}
		
		public double getMinimalPrice(IUser user) {

			if (canReadInfo(user) == true) {
				return this.minimalPrice;
			}
			return -1;	
		}
		
		/*/////////////////// Fin Creer des messages /////////////////// */
		
		/*/////////////////// Gestion du prix de r�serve /////////////////// */
		
		public void setReservePrice(User user, double reservePrice) {
	
			if (isCreator(user) == false) { // Si l'utilisateur n'est pas le cr�ateur de l'ench�re
				System.out.println("Erreur : Vous n'�tes pas le cr�ateur de cette ench�re, vous ne pouvez pas modifier le prix de r�serve.");
				return;
			}
				
			if (reservePrice <= 0) {
				System.out.println("Erreur : Le prix de r�serve doit �tre au moins de 0.01�.");
				return;
			}
					
			if (isCanceled(this.currentState) == true || isFinished(this.currentState) == true) {
				System.out.println("Erreur : Le prix de r�serve n'est pas modifiable sur une ench�re annul�e ou termin�e. Etat courant: " + this.currentState.toString() + ".");
				return;
			}	
			
			this.ReservePrice = reservePrice;
			System.out.println("Le prix de r�serve est maintenant de " + reservePrice + "�");
		}
		
		public double getReservePrice() {
			return this.ReservePrice;
		}
		/*/////////////////// Fin Gestion du prix de r�serve /////////////////// */
				
		/*/////////////////// Gestion des offres sur l'ench�re /////////////////// */
		
		/**Faire une offre sur l'ench�re
		 * @return accepted : si true, offre accept�e, false sinon
		 */
		public boolean bid(IOffer offer) {
			
			if (offer == null ) {
				System.out.println("Erreur : L'offre est nulle.");
				return false;
			}
			
			// Un vendeur ne peux pas �mettre d'offre
			if (RoleTypeDeterminer.isSeller(offer.getSender()) == true) {
				System.out.println("Erreur : Un vendeur ne peux pas faire une offre sur une ench�re.");
				return false;
			}
			
			// Le cr�ateur de l'ench�re ne peux pas emetre des offres sur celle-ci
			if (isCreator(offer.getSender()) == true) {
				System.out.println("Erreur : Vous ne pouvez pas �mettre d'offre sur vos propres ench�res.");
				return false;
			}
			
			// Un utilisateur doit suivre l'ench�re pour emmetre des offres sur celle-ci
			if (isFollowing(offer.getSender()) == false) {
				System.out.println("Erreur : Vous n'�tes pas abonn� � cette ench�re, vous ne pouvez donc pas ench�rir.");
				return false;
			}
			
			// Une offre n'est possible que sur des ench�res public
			if (isPublished(this.currentState) == false) {
				System.out.println("Erreur : Il n'est possible de faires des offres que sur les ench�res publi�es.");
				return false;
			}
			
			// Le montant de l'offre doit �tre sup�rieur au mantant de la meilleur ench�re
			if (offer.getAmount() <= this.bidAmount) {
				System.out.println("Erreur : Le montant de l'offre doit �tre sup�rieur au montant de la dern�re ench�re.");
				return false;
			}
			
			// Le montant de l'offre doit �tre sup�rieur au prix minimal
			if (offer.getAmount() <= this.minimalPrice) {
				System.out.println("Erreur : Le montant de l'offre doit �tre sup�rieur au prix minimale ench�re.");
				return false;
			}
			
			// Pr�viens les achteurs que le prix de r�serve � �t� atteind par leur offre
			if (offer.getAmount() >= this.ReservePrice) {
				System.out.println("Le prix de r�serve � �t� atteint.");
				notifyObervers(new AlertReservePriceReached(offer.getSender()));
			}
			
			// Previens le cr�ateur de l'ench�re qu'une offre vient d'�tre �mise sur son ench�re
			notifyObervers(new AlertOfferReceived());
			
			this.offerList.add(offer);
			this.observerList.put((IObserver) offer.getSender(), true);
			this.actualWinner = offer.getSender();
			System.out.println("Offre accpet�e, vous �tes le meilleur ench�risseur.");
			return true;
		}
		
		public IOffer getLastOffer() {
			if (this.offerList.size() > 0) {
				return this.offerList.get(this.offerList.size() - 1);
			}
			else return null;
		}
		
		/**
		 * Test si un utilisateur � fait une offre sur l'ench�re ou non
		 * @param user : Utilisateur � tester
		 * @return true, si l'itilisateur � �mis une offre sur l'ench�re, false sinon
		 */
		public Boolean hasMakeAnOffer(IUser user) {
			
			for (Entry<IObserver, Boolean> entry : this.observerList.entrySet()) {
				if (entry.getKey().equals(user)) { // Si l'entr�e test� contient l'utilisateur
					//System.out.println(entry.getKey().toString() + " - " + entry.getValue());
					return entry.getValue(); // valeur correspndante � la cl�
				}
			}
			System.out.println("Erreur : Utilisateur non trouv�.");
			return false; // si non trouv�
		}
		
		/*/////////////////// Fin Gestion des offres sur l'ench�re /////////////////// */
		
		/*/////////////////// Gestion de l'�tat de l'ench�re /////////////////// */
		
		// Les methodes de transition d'�tat prennent un utilisateur en param�tre pour v�rifier
		// quel utilisateur 
		
		public void privatize(User user) {
			if (isCreator(user))
				this.currentState.goToPrivateState();
			else 
				System.out.println("Erreur : Seul le propri�taire de l'ench�re peut la faire changer d'�tat.");
		}
		
		public void publish(IUser user) {
			if (isCreator(user))
				this.currentState.goToPublishedState();
			else 
				System.out.println("Erreur : Seul le propri�taire de l'ench�re peut la faire changer d'�tat.");
		}
		
		public void cancel(IUser user) {
			if (isCreator(user))
				this.currentState.goToCanceledState();
			else 
				System.out.println("Erreur : Seul le propri�taire de l'ench�re peut la faire changer d'�tat.");
			if (isCanceled(this.currentState)) {
				notifyObervers(new AlertAuctionCanceled());
			}
		}
		
		public void finish() { // aucun utilisateur ne peux terminer l'ench�re, cette transition d�pend du temps �coul�
			this.currentState.goToFinishedState();
		}
		
		public boolean isPrivatized(IState currentState) {
			if (this.currentState instanceof StatePrivatized) {
				return true;
			}
			return false;
		}
		
		public boolean isPublished(IState currentState) {
			if (this.currentState instanceof StatePublished) {
				return true;
			}
			return false;
		}
		
		public boolean isCanceled(IState currentState) {
			if (this.currentState instanceof StateCanceled) {
				return true;
			}
			return false;
		}
		
		public boolean isFinished(IState currentState) {
			if (this.currentState instanceof StateFinished) {
				return true;
			}
			return false;
		}
		
		public IState getPrivatizedState() {
			return this.privatized;
		}
		
		public IState getPublishedState() {
			return this.published;
		}
		
		public IState getCanceledState() {
			return this.canceled;
		}
		
		public IState getFinishedState() {
			return this.finished;
		}
		
		public void setState(IState nextState) {
			this.currentState = nextState;
		}
		
		public IState getState() {
			return this.currentState;
		}
		
		/*/////////////////// Fin Gestion de l'�tat de l'ench�re /////////////////// */
	
		/*/////////////////// Gestion des Observateurs /////////////////// */
		
		@Override
		public void addObserver(IObserver observer) {
			
			if (RoleTypeDeterminer.isSeller( (IUser) observer)) {
				System.out.println("Erreur : Les vendeurs ne peuvent pas suivre d'nech�re.");
				return;
			}
			
			if (this.currentState.equals(this.published) == false) {
				System.out.println("Erreur : Seul les ench�res publi�es peuvent �tre suivi.");
				return;
			}
			
			if (isFollowing( (IUser) observer) == true) {
				System.out.println("Erreur : Vous suivez d�j� cette ench�re.");
				return;
			}
			this.observerList.put(observer, false);
		}

		@Override
		public void removeObserver(IObserver observer) {
			this.observerList.remove(observer);
		}
		
		// Informe tout les utilisateurs d'un changement d'�tat de l'ench�re
		@Override
		public void notifyObervers(IAlert alert) {
										
			// Informe le cr�ateur de l'ench�re qu'une offre � �t� �mise sur son ench�re
			if (alert instanceof AlertOfferReceived) {
				IObserver observer = (IObserver) this.creator;
				Message message = new Message(this.name,  this.creator.getLogin() + " (" + this.creator.getRole().toString() + ")");
				message.write(alert.getMessage());
				observer.update(this, message);
			}
			
			// Informe les acheteur qui suivent l'ench�re et qui �coutent l'alerte "ench�re annul�e", que l'ench�re est annul�e
			if (alert instanceof AlertAuctionCanceled) {
				for (Entry<IObserver, Boolean> entry : this.observerList.entrySet()) {
					User user = (User) entry.getKey();
					if (user.getRole().isWatchingAlertAuctionCanceled() == true) {
						Message message = new Message(this.name, user.getLogin() + " (" + user.getRole().toString() + ")");
						message.write(alert.getMessage());
						user.update(this, message);
					}
				}
			}
			
			// Informe les acheteur qui suivent l'ench�re et qui �coutent l'alerte "ench�re termin�", que l'ench�re est termin�e
			if (alert instanceof AlertAuctionFinished) {
				for (Entry<IObserver, Boolean> entry : this.observerList.entrySet()) {
					User user = (User) entry.getKey();
					if (user.getRole().isWatchingAlertAuctionFinished() == true) {
						Message message = new Message(this.name, user.getLogin() + " (" + user.getRole().toString() + ")");
						message.write(alert.getMessage());
						user.update(this, message);
					}
				}
			}
			
			// Informe les acheteur que leur offre ou celle d'un autre utilisateur vient de d�passer le prix de r�serve
			if (alert instanceof AlertReservePriceReached) {
				for (Entry<IObserver, Boolean> entry : this.observerList.entrySet()) {
					User user = (User) entry.getKey();
					Message message = new Message(this.name, user.getLogin() + " (" + user.getRole().toString() + ")");
					message.write(alert.getMessage(user));
					user.update(this, message);
				}
			}
		}
		
		/*/////////////////// Fin Gestion des Observateurs /////////////////// */

	}
}
