package com.am.hfinance.model;

import java.math.BigDecimal;
import java.util.Date;

public class BaseActivity implements IActivity {

	private long id;
	private Date date;
	private String comment;
	private BigDecimal amount;
	private long currency;
	private long account;

	public BaseActivity() {
	}
	
	public BaseActivity(Date date, String comment, BigDecimal amount, long currency, long account) {
		
		this.date = date;
		this.comment = comment;
		this.amount = amount;
		this.currency = currency > 0 ? currency : 1;
		this.account = account > 0 ? account : 1 ;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public long getCurrency() {
		return currency;
	}

	public void setCurrency(long currency) {
		this.currency = currency;
	}

	public long getAccount() {
		return account;
	}

	public void setAccount(long account) {
		this.account = account;
	}
}