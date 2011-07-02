package com.am.hfinance.model;

public abstract class SimpleEntity {

	private long id;
	private String name;

	public SimpleEntity() {
	}
	
	public SimpleEntity(long id) {
		this.id = id;
	}	
	
	public SimpleEntity(String name) {
		this.name = name;
	}
	
	public SimpleEntity(long id, String name) {
		this.id = id;
		this.name = name;
	}	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}