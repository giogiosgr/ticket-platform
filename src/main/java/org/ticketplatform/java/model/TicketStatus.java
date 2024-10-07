package org.ticketplatform.java.model;

public enum TicketStatus {
	
    DA_FARE("Da fare"),
    IN_CORSO("In corso"),
    COMPLETATO("Completato");

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


