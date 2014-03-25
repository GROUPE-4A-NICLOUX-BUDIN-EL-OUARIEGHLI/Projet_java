package com.SystemeEnchere.metier;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.SystemeEnchere.metier.SimpleUserFactory.User;

public class Offer implements IOffer {
	
	private double amount = 0.00;
	private IUser user;
	private Date date;
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public Offer(IUser user, double amount) {
		this.amount = amount;
		this.user = user;
		Calendar calendar = Calendar.getInstance();
		this.date = calendar.getTime();
	}
	
	public double getAmount() {
		return this.amount;
	}
	
	public IUser getSender() {
		return this.user;
	}
	
	public Date getDate() {
		return this.date;
	}
	
	public String toString() {
		return dateFormat.format(this.date) + " " + user.getLogin() + " " + this.amount + "€";	
	}
}
