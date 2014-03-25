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
			System.out.println("Erreur : Le prix d'une enchère doit être supérieur à 0.01€.");
			return null;
		}
		
		if( user != null && product != null) {
			
			if( RoleTypeDeterminer.isSeller(user) ) {
				System.out.println("Enchère Créee.");
				return new Auction(user, product, duration, minimalPrice);
			}
				
			System.out.println("Erreur : Seul les vendeurs peuvent créer une enchère.");
		}
		System.out.println("Erreur : L'utilisateur ou le produit est nul.");
		return null;
	}
	
	/* Classe Auction
	 * 
	 * Permet d'effectuer des enchères
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
		private double minimalPrice; // prix fixé par le créateur de l'enchère
		private double bidAmount = 0.00; // montant de l'enchère
		
		private IProduct product;
		private String name;
		private IUser creator;
		private IUser actualWinner;
		private int duration;		
		
		private Auction(IUser user, IProduct product, EnumAuctionDuration duration, double price) {
			this.product = product;
			this.creator = user;
			this.minimalPrice = price;
			this.name = "[Enchère] " + product.getName();
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
		 * Test si un utilisateur est le créateur de l'enchère
		 * @return true si l'utilisateur est le créateur de l'enchère, false sinon
		 */
		private boolean isCreator(IUser user) {
			return this.creator.equals(user);
		}
		
		/**
		 * Test si un utilisateur suit l'enchère (Si il est dans la liste des observateurs)
		 * @param user : Utilisateur a tester
		 * @return : true, l'utilisateur suit l'enchère, false sinon
		 */
		private boolean isFollowing(IUser user) {
			for (Entry<IObserver, Boolean> entry : this.observerList.entrySet()) {
				User userInList = (User) entry.getKey();
			    if (userInList.equals(user)) return true; 
			}
			return false;
		}
		
		/**
		 * Détermine si un utilisateur peux lire les informations de l'enchère en fonction de l'état de celle-ci
		 * et du type d'utilisateur
		 * @param user
		 * @return true, l'utilisateur peux lire l'info, false sinon
		 */
		private boolean canReadInfo(IUser user) {
			if (isCreator(user) == true) { // Le créateur de l'enchère à toujours accès aux données de ses enchères
				return true;
			}
			
			if (isCanceled(this.currentState) == true) {
				if (hasMakeAnOffer(user)) { // Si l'utilisateur a fait une offre sur cette enchère
					return true;
				}
				System.out.println("Erreur : L'enchère est annulée, vous n'avez fait aucune offre, accès refusé.");
				return false;
			}
			
			if (isPublished(this.currentState) == false) {
				System.out.println("Erreur : L'enchère n'est pas public, accès refusé.");
				return false;
			}
			return true;
		}
		
		private boolean countDown() {
			// non implémenté, non indispensable pour tester l'état terminé.
			return false;		
		}
		
		// Calcule la date de fin de l'enchère en additionnant sa durée à l'instant de création
		private void setLimitDate(int duration) {
			Calendar calendar = Calendar.getInstance();
			calendar.add (Calendar.DATE, duration);
			this.endDate = calendar.getTime();
		}
		
		// Sauvegarde la date de création de l'enchère
		private void setCreationDate() {
			Calendar calendar = Calendar.getInstance();
			this.creationDate = calendar.getTime();
		}
		
		public String getLimitDate() {
			return dateFormat.format(this.endDate);
		}
		
		// Renvoi la date de créationnde l'enchère sous forme de chaine de caractere
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
			
			if (isCreator(user) == false) { // Si l'utilisateur n'est pas le créateur de l'enchère
				System.out.println("Erreur : Vous n'êtes pas le créateur de cette enchère, vous ne pouvez pas modifier le prix de réserve.");
				return;
			}
				
			if (this.minimalPrice <= 0) {
				System.out.println("Erreur : Le prix minimal doit être supérieur à 0€.");
				return;
			}
					
			if (isPrivatized(this.currentState) == false) {
				System.out.println("Erreur : Le prix minimal ne peux être modidié que si l'enchère est à l'état Privatized. Etat courant: " + this.currentState.toString() + ".");
				return;
			}	
			
			this.minimalPrice = minimalPrice;
			System.out.println("Le prix minimal est maintenant de " + minimalPrice + "€");
		}
		
		public double getMinimalPrice(IUser user) {

			if (canReadInfo(user) == true) {
				return this.minimalPrice;
			}
			return -1;	
		}
		
		/*/////////////////// Fin Creer des messages /////////////////// */
		
		/*/////////////////// Gestion du prix de réserve /////////////////// */
		
		public void setReservePrice(User user, double reservePrice) {
	
			if (isCreator(user) == false) { // Si l'utilisateur n'est pas le créateur de l'enchère
				System.out.println("Erreur : Vous n'êtes pas le créateur de cette enchère, vous ne pouvez pas modifier le prix de réserve.");
				return;
			}
				
			if (reservePrice <= 0) {
				System.out.println("Erreur : Le prix de réserve doit être au moins de 0.01€.");
				return;
			}
					
			if (isCanceled(this.currentState) == true || isFinished(this.currentState) == true) {
				System.out.println("Erreur : Le prix de réserve n'est pas modifiable sur une enchère annulée ou terminée. Etat courant: " + this.currentState.toString() + ".");
				return;
			}	
			
			this.ReservePrice = reservePrice;
			System.out.println("Le prix de réserve est maintenant de " + reservePrice + "€");
		}
		
		public double getReservePrice() {
			return this.ReservePrice;
		}
		/*/////////////////// Fin Gestion du prix de réserve /////////////////// */
				
		/*/////////////////// Gestion des offres sur l'enchère /////////////////// */
		
		/**Faire une offre sur l'enchère
		 * @return accepted : si true, offre acceptée, false sinon
		 */
		public boolean bid(IOffer offer) {
			
			if (offer == null ) {
				System.out.println("Erreur : L'offre est nulle.");
				return false;
			}
			
			// Un vendeur ne peux pas émettre d'offre
			if (RoleTypeDeterminer.isSeller(offer.getSender()) == true) {
				System.out.println("Erreur : Un vendeur ne peux pas faire une offre sur une enchère.");
				return false;
			}
			
			// Le créateur de l'enchère ne peux pas emetre des offres sur celle-ci
			if (isCreator(offer.getSender()) == true) {
				System.out.println("Erreur : Vous ne pouvez pas émettre d'offre sur vos propres enchères.");
				return false;
			}
			
			// Un utilisateur doit suivre l'enchère pour emmetre des offres sur celle-ci
			if (isFollowing(offer.getSender()) == false) {
				System.out.println("Erreur : Vous n'êtes pas abonné à cette enchère, vous ne pouvez donc pas enchérir.");
				return false;
			}
			
			// Une offre n'est possible que sur des enchères public
			if (isPublished(this.currentState) == false) {
				System.out.println("Erreur : Il n'est possible de faires des offres que sur les enchères publiées.");
				return false;
			}
			
			// Le montant de l'offre doit être supérieur au mantant de la meilleur enchère
			if (offer.getAmount() <= this.bidAmount) {
				System.out.println("Erreur : Le montant de l'offre doit être supérieur au montant de la dernère enchère.");
				return false;
			}
			
			// Le montant de l'offre doit être supérieur au prix minimal
			if (offer.getAmount() <= this.minimalPrice) {
				System.out.println("Erreur : Le montant de l'offre doit être supérieur au prix minimale enchère.");
				return false;
			}
			
			// Préviens les achteurs que le prix de réserve à été atteind par leur offre
			if (offer.getAmount() >= this.ReservePrice) {
				System.out.println("Le prix de réserve à été atteint.");
				notifyObervers(new AlertReservePriceReached(offer.getSender()));
			}
			
			// Previens le créateur de l'enchère qu'une offre vient d'être émise sur son enchère
			notifyObervers(new AlertOfferReceived());
			
			this.offerList.add(offer);
			this.observerList.put((IObserver) offer.getSender(), true);
			this.actualWinner = offer.getSender();
			System.out.println("Offre accpetée, vous êtes le meilleur enchérisseur.");
			return true;
		}
		
		public IOffer getLastOffer() {
			if (this.offerList.size() > 0) {
				return this.offerList.get(this.offerList.size() - 1);
			}
			else return null;
		}
		
		/**
		 * Test si un utilisateur à fait une offre sur l'enchère ou non
		 * @param user : Utilisateur à tester
		 * @return true, si l'itilisateur à émis une offre sur l'enchère, false sinon
		 */
		public Boolean hasMakeAnOffer(IUser user) {
			
			for (Entry<IObserver, Boolean> entry : this.observerList.entrySet()) {
				if (entry.getKey().equals(user)) { // Si l'entrée testé contient l'utilisateur
					//System.out.println(entry.getKey().toString() + " - " + entry.getValue());
					return entry.getValue(); // valeur correspndante à la clé
				}
			}
			System.out.println("Erreur : Utilisateur non trouvé.");
			return false; // si non trouvé
		}
		
		/*/////////////////// Fin Gestion des offres sur l'enchère /////////////////// */
		
		/*/////////////////// Gestion de l'état de l'enchère /////////////////// */
		
		// Les methodes de transition d'état prennent un utilisateur en paramètre pour vérifier
		// quel utilisateur 
		
		public void privatize(User user) {
			if (isCreator(user))
				this.currentState.goToPrivateState();
			else 
				System.out.println("Erreur : Seul le propriétaire de l'enchère peut la faire changer d'état.");
		}
		
		public void publish(IUser user) {
			if (isCreator(user))
				this.currentState.goToPublishedState();
			else 
				System.out.println("Erreur : Seul le propriétaire de l'enchère peut la faire changer d'état.");
		}
		
		public void cancel(IUser user) {
			if (isCreator(user))
				this.currentState.goToCanceledState();
			else 
				System.out.println("Erreur : Seul le propriétaire de l'enchère peut la faire changer d'état.");
			if (isCanceled(this.currentState)) {
				notifyObervers(new AlertAuctionCanceled());
			}
		}
		
		public void finish() { // aucun utilisateur ne peux terminer l'enchère, cette transition dépend du temps écoulé
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
		
		/*/////////////////// Fin Gestion de l'état de l'enchère /////////////////// */
	
		/*/////////////////// Gestion des Observateurs /////////////////// */
		
		@Override
		public void addObserver(IObserver observer) {
			
			if (RoleTypeDeterminer.isSeller( (IUser) observer)) {
				System.out.println("Erreur : Les vendeurs ne peuvent pas suivre d'nechère.");
				return;
			}
			
			if (this.currentState.equals(this.published) == false) {
				System.out.println("Erreur : Seul les enchères publiées peuvent être suivi.");
				return;
			}
			
			if (isFollowing( (IUser) observer) == true) {
				System.out.println("Erreur : Vous suivez déjà cette enchère.");
				return;
			}
			this.observerList.put(observer, false);
		}

		@Override
		public void removeObserver(IObserver observer) {
			this.observerList.remove(observer);
		}
		
		// Informe tout les utilisateurs d'un changement d'état de l'enchère
		@Override
		public void notifyObervers(IAlert alert) {
										
			// Informe le créateur de l'enchère qu'une offre à été émise sur son enchère
			if (alert instanceof AlertOfferReceived) {
				IObserver observer = (IObserver) this.creator;
				Message message = new Message(this.name,  this.creator.getLogin() + " (" + this.creator.getRole().toString() + ")");
				message.write(alert.getMessage());
				observer.update(this, message);
			}
			
			// Informe les acheteur qui suivent l'enchère et qui écoutent l'alerte "enchère annulée", que l'enchère est annulée
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
			
			// Informe les acheteur qui suivent l'enchère et qui écoutent l'alerte "enchère terminé", que l'enchère est terminée
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
			
			// Informe les acheteur que leur offre ou celle d'un autre utilisateur vient de dépasser le prix de réserve
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
