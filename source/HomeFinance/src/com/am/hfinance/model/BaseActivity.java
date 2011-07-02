/*
 * Copyright (C) 2011 Android Monsters
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
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