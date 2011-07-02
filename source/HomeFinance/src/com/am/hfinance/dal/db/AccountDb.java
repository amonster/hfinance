package com.am.hfinance.dal.db;


import android.database.sqlite.SQLiteDatabase;

import com.am.hfinance.dal.IAccountDao;
import com.am.hfinance.model.Account;
import com.am.hfinance.model.IFactoryMethod;

public class AccountDb extends SimpleDictionaryDb<Account> implements IAccountDao {
	public static final String TAG = ExpencesDb.class.getSimpleName();
	public static final String TABLE_ACCOUNT = "account";
	public static final String TABLE_CREATE = "create table "
		+ TABLE_ACCOUNT + " (" + KEY_ID
		+ " integer primary key not null," + KEY_NAME
		+ " text);";
	
	public static final String TABLE_DELETE = "drop table if exists " + TABLE_ACCOUNT + ";";	

	public AccountDb(SQLiteDatabase db) {
		super(db, new IFactoryMethod<Account>() {

			public Account create() {
				return new Account();
			}
		});
	}

	@Override
	protected String getTableName() {
		return TABLE_ACCOUNT;
	}
}