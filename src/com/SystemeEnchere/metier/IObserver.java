package com.SystemeEnchere.metier;

public interface IObserver {
	
	public void update(IObservable sender, Object args);
}
