package com.SystemeEnchere.metier;

/**
 * Product est une classe qui sera surement amené à changer.
 * Les produits peuvent se diversifier, avec de nouvelles catégories, ou au contraire se spécialiser.
 * Cette interface rend chaque produit interchangeable sans se soucier de son type.
 */
public interface IProduct {
	
	public void setDescription(String description);
	public String getName();
}
