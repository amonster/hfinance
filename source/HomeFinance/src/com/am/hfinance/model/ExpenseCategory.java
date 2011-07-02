package com.am.hfinance.model;

public class ExpenseCategory extends SimpleEntity {

	public ExpenseCategory() {
		super();
	}

	public ExpenseCategory(long id) {
		super(id);
	}

	public ExpenseCategory(String name) {
		super(name);
	}

	public ExpenseCategory(long id, String name) {
		super(id, name);
	}
}
