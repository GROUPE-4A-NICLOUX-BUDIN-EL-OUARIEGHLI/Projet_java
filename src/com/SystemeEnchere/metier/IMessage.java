package com.SystemeEnchere.metier;

public interface IMessage {
	public void write(String message);
	public String getMessage();
	public String getDate();
	public String getSender();
	public String getReceiver();
	public String toString();
}
