package org.ticketplatform.java.model;

public enum TicketStatus {
	
    DA_FARE("da fare"),
    IN_CORSO("in corso"),
    COMPLETATO("completato");

    private String displayValue;

    TicketStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	} 
    
}


