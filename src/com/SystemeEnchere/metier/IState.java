package com.SystemeEnchere.metier;

public interface IState {

	public void goToPrivateState();
	public void goToPublishedState();
	public void goToCanceledState();
	public void goToFinishedState();
}
