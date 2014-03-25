package com.SystemeEnchere.metier;

import com.SystemeEnchere.metier.SimpleUserFactory.User;

public interface IUser {
	public String getLogin();
	public IRole getRole();
	public void setRole(IRole newRole);
	public IOffer doOffer(double amount);
}
