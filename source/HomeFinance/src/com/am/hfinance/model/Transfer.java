package com.am.hfinance.model;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Transfer extends BaseActivity implements Parcelable {
	private BigDecimal creditAmount;
	private long creditCurrency;
	private long creditAccount;	
	
	public Transfer(Date date, String comment, BigDecimal amount,
			long currency, long account, BigDecimal creditAmount, long creditCurrency, long creditAccount) {
		super(date, comment, amount, currency, account);

		this.creditAmount = creditAmount;
		this.creditCurrency = creditCurrency > 0 ? creditCurrency : 1;
		this.creditAccount = creditAccount > 0 ? creditAccount : 1;
	}
	
	public Transfer(Parcel in) {
		readFromParcel(in);
	}
	
	public BigDecimal getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}

	public long getCreditCurrency() {
		return creditCurrency;
	}
	
	public void setCreditCurrency(long creditCurrency) {
		this.creditCurrency = creditCurrency;
	}

	public long getCreditAccount() {
		return creditAccount;
	}
	
	public void setCreditAccount(long creditAccount) {
		this.creditAccount = creditAccount;
	}
	
	@Override
	public String toString() {
		return String.format("@%1$s %2$s #%3$d#%4$d#%5$d#%6$d#%7$s", getAmount().toPlainString(), getComment(), getAccount(), getCreditAccount(), getCurrency(), getCreditCurrency(), getCreditAmount().toString());
	}
	
	public static final Parcelable.Creator<Transfer> CREATOR = new Parcelable.Creator<Transfer>() {
		public Transfer createFromParcel(Parcel in) {
			return new Transfer(in);
		}

		public Transfer[] newArray(int size) {
			return new Transfer[size];
		}
	};

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(getId());
		dest.writeString(new SimpleDateFormat().format(getDate()));
		dest.writeString(getComment().toString());
		dest.writeString(getAmount().toString());
		dest.writeLong(getCurrency());
		dest.writeLong(getAccount());
		dest.writeString(creditAmount.toString());
		dest.writeLong(creditCurrency);
		dest.writeLong(creditAccount);
	}

	public void readFromParcel(Parcel in) {
		setId(in.readLong());
		try {
			setDate(new SimpleDateFormat().parse(in.readString()));
		} catch (ParseException e) {
			setDate(new Date());
		}
		
		setComment(in.readString());
		setAmount(new BigDecimal(in.readString()));
		setCurrency(in.readLong());
		setAccount(in.readLong());
		setCreditAmount(new BigDecimal(in.readString()));
		setCreditCurrency(in.readLong());
		setCreditAccount(in.readLong());
	}
}
