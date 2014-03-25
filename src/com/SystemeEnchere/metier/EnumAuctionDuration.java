package com.SystemeEnchere.metier;

// Liste des valeurs possible pour la durée d'une enchère

public enum EnumAuctionDuration {
	
	DAYS3(3), DAYS5(5), DAYS7(7), DAYS10(10);
    
	private int value;

    private EnumAuctionDuration(int value) {
            this.value = value;
    }
    
    public int toInt() {
		return this.value;	
    }
};  

