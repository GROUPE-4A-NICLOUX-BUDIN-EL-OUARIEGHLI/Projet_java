package com.SystemeEnchere.metier;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Message implements IMessage {
	
	private String sender;
	private String receiver;
	private String message;
	private String date;
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public Message(String sender, String receiver) {
		this.sender = sender;
		this.receiver = receiver;
		Calendar calendar = Calendar.getInstance();
		this.date = dateFormat.format(calendar.getTime());
	}
	
	@Override
	public void write(String message) {
		this.message = message;	
	}

	@Override
	public String getDate() {
		return this.date;
	}

	@Override
	public String getSender() {
		return this.sender;
	}
	
	@Override
	public String getReceiver() {
		return this.receiver;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
	
	public String toString() {
		return "\n.............................................\n" + 
				this.date + " " + "Sender : " + this.sender + "\n" +
				"Receiver : " + this.receiver + "\n" +
				this.message + "\n" + 
				"...............................................";
	}
}
