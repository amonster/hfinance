package com.am.hfinance.model;

public class Account extends SimpleEntity {
	public Account() {
		super();
	}
	
	public Account(long id) {
		super(id);
	}
	
	public Account(String name) {
		super(name);
	}
	
	public Account(long id, String name) {
		super(id, name);
	}
}
