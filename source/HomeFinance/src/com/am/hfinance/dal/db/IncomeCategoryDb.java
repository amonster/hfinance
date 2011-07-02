package com.am.hfinance.dal.db;

import com.am.hfinance.dal.IIncomeCategoryDao;
import com.am.hfinance.model.IFactoryMethod;
import com.am.hfinance.model.IncomeCategory;

import android.database.sqlite.SQLiteDatabase;

public class IncomeCategoryDb extends SimpleDictionaryDb<IncomeCategory> implements IIncomeCategoryDao {
	public static final String TAG = ExpencesDb.class.getSimpleName();
	public static final String TABLE_INCOME_CATEGORY = "income_category";
	public static final String TABLE_CREATE = "create table "
		+ TABLE_INCOME_CATEGORY + " (" + KEY_ID
		+ " integer primary key not null," + KEY_NAME
		+ " text);";
	
	public static final String TABLE_DELETE = "drop table if exists " + TABLE_INCOME_CATEGORY + ";";	

	public IncomeCategoryDb(SQLiteDatabase db) {
		super(db, new IFactoryMethod<IncomeCategory>() {
			public IncomeCategory create() {
				return new IncomeCategory();
			}
		});
	}
	
	@Override
	protected String getTableName() {
		return TABLE_INCOME_CATEGORY;
	}
}
