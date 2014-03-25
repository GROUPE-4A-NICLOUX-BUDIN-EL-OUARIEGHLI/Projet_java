package com.SystemeEnchere.metier;

import com.SystemeEnchere.metier.SimpleUserFactory.User;

// D�termine de quel r�le impl�mente la classe User

public class RoleTypeDeterminer {

	public static boolean isSeller(IUser user) {
		if( user.getRole() instanceof Seller ) {
			return true;
		}
		else 
			return false;
	}
	
	public static boolean isBuyer(User user) {
		if( user.getRole() instanceof Buyer ) {
			return true;
		}
		else 
			return false;
	}
}
