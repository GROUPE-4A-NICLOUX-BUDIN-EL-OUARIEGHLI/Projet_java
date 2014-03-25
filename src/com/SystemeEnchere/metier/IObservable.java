package com.SystemeEnchere.metier;

public interface IObservable {
	
	public void addObserver(IObserver observer);
	public void removeObserver(IObserver observer);
	public void notifyObervers(IAlert alert);
}
