package com.SystemeEnchere.metier;

public interface IAlert {
	
	public String getMessage();
	public String getMessage(Object object); // Param�tre pour choisir parmis plusieurs messages
}
