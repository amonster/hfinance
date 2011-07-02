package com.am.hfinance.dal.db;

import com.am.hfinance.dal.IExpenseCategoryDao;
import com.am.hfinance.model.ExpenseCategory;
import com.am.hfinance.model.IFactoryMethod;

import android.database.sqlite.SQLiteDatabase;

public class ExpenseCategoryDb extends SimpleDictionaryDb<ExpenseCategory> implements IExpenseCategoryDao {
	public static final String TAG = ExpencesDb.class.getSimpleName();
	public static final String TABLE_EXPENSE_CATEGORY = "expense_category";
	public static final String TABLE_CREATE = "create table "
		+ TABLE_EXPENSE_CATEGORY + " (" + KEY_ID
		+ " integer primary key not null," + KEY_NAME
		+ " text);";
	
	public static final String TABLE_DELETE = "drop table if exists " + TABLE_EXPENSE_CATEGORY + ";";	

	public ExpenseCategoryDb(SQLiteDatabase db) {
		super(db, new IFactoryMethod<ExpenseCategory>() {
			public ExpenseCategory create() {
				return new ExpenseCategory();
			}
		});
	}
	
	@Override
	protected String getTableName() {
		return TABLE_EXPENSE_CATEGORY;
	}
}
