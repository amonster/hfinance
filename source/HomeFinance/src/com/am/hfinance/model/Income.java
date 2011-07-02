package com.am.hfinance.model;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Income extends BaseCategorizedActivity implements Parcelable {

	public Income(Date date, String comment, BigDecimal amount, long currency,
			long category, long account) {
		super(date, comment, amount, currency, category, account);
	}
	
	public Income(Parcel in) {
		readFromParcel(in);
	}
	
	@Override
	public String toString() {
		return String.format("+%1$s %2$s #%3$d#%4$d#%5$d", getAmount().toPlainString(), getComment(), getCategory(), getAccount(), getCurrency());
	}

	public static final Parcelable.Creator<Income> CREATOR = new Parcelable.Creator<Income>() {
		public Income createFromParcel(Parcel in) {
			return new Income(in);
		}

		public Income[] newArray(int size) {
			return new Income[size];
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
		dest.writeLong(getCategory());
		dest.writeLong(getAccount());
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
		setCategory(in.readLong());
		setAccount(in.readLong());
	}
}
