package com.am.hfinance.model;

import java.math.BigDecimal;
import java.util.Date;

public interface IActivity {

	public abstract long getId();

	public abstract Date getDate();

	public abstract String getComment();

	public abstract BigDecimal getAmount();

	public abstract long getCurrency();

	public abstract long getAccount();

}