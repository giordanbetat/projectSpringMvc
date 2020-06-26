package com.giordanbetat.projectspringboot.model;

public enum Office {

	JUNIOR("Junior"), 
	PLENO("Pleno"), 
	SENIOR("Senior");

	private String office;

	private Office(String office) {
		this.office = office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getOffice() {
		return office;
	}
	
	@Override
	public String toString() {
		return this.name();
	}
}
