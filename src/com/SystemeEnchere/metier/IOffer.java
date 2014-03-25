package com.SystemeEnchere.metier;

import java.util.Date;

public interface IOffer {
	
	public double getAmount();
	public IUser getSender();
	public Date getDate();
}
