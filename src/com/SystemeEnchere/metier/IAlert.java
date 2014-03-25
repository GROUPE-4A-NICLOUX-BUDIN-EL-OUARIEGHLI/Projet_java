package com.SystemeEnchere.metier;

public interface IAlert {
	
	public String getMessage();
	public String getMessage(Object object); // Paramètre pour choisir parmis plusieurs messages
}
