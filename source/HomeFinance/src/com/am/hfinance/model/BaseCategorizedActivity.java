package com.am.hfinance.model;

import java.math.BigDecimal;
import java.util.Date;

public class BaseCategorizedActivity extends BaseActivity implements ICategrorizedActivity {
	private long category;
	
	public BaseCategorizedActivity() {
	}
	
	public BaseCategorizedActivity(Date date, String comment, BigDecimal amount, long currency,
			long category, long account) {
		super(date, comment, amount, currency, account);
		this.category = category > 0 ? category : 1;
	}	
	
	public long getCategory() {
		return category;
	}

	public void setCategory(long category) {
		this.category = category;
	}
}
