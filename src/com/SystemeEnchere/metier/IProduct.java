package com.SystemeEnchere.metier;

/**
 * Product est une classe qui sera surement amen� � changer.
 * Les produits peuvent se diversifier, avec de nouvelles cat�gories, ou au contraire se sp�cialiser.
 * Cette interface rend chaque produit interchangeable sans se soucier de son type.
 */
public interface IProduct {
	
	public void setDescription(String description);
	public String getName();
}
