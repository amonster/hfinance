package com.am.hfinance.dal.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.am.hfinance.R;
import com.am.hfinance.dal.IAccountDao;
import com.am.hfinance.dal.IDaoFactory;
import com.am.hfinance.dal.IExpenseCategoryDao;
import com.am.hfinance.dal.IExpenseDao;
import com.am.hfinance.dal.IIncomeCategoryDao;
import com.am.hfinance.dal.IIncomeDao;
import com.am.hfinance.dal.ITransferDao;
import com.am.hfinance.model.Account;
import com.am.hfinance.model.ExpenseCategory;
import com.am.hfinance.model.IncomeCategory;

public class HomeFinanceDb implements IDaoFactory {
	private static final String TAG = HomeFinanceDb.class.getSimpleName();
	private static final String DATABASE_NAME = "hfinance.db";
	private static final int DATABASE_VERSION = 5;

	private SQLiteDatabase db;
	private DbOpenHelper dbHelper;
	
	private IExpenseDao expenseDaoImpl = null;
	private IIncomeDao incomeDaoImpl = null;
	private ITransferDao transferDaoImpl = null;
	private IAccountDao accountDaoImpl = null;
	private IIncomeCategoryDao incomeCategoryDaoImpl = null;
	private IExpenseCategoryDao expenseCategoryDaoImpl = null;

	public HomeFinanceDb(Context context) {
		dbHelper = new DbOpenHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	public void close() {
		db.close();
	}

	public void open() throws SQLiteException {
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbHelper.getReadableDatabase();
		}
	}

	public IExpenseDao getExpenseDao() {
		if(expenseDaoImpl == null) {
			expenseDaoImpl = new ExpencesDb(db);
		}
		
		return expenseDaoImpl;
	}

	public IIncomeDao getIncomeDao() {
		if(incomeDaoImpl == null) {
			incomeDaoImpl = new IncomeDb(db);
		}
		return incomeDaoImpl;
	}

	public ITransferDao getTransferDao() {
		if(transferDaoImpl == null) {
			transferDaoImpl = new TransferDb(db);
		}
		return transferDaoImpl;
	}
	
	public IAccountDao getAccountDao() {
		if(accountDaoImpl == null) {
			accountDaoImpl = new AccountDb(db);
		}
		return accountDaoImpl;
	}
	
	public IExpenseCategoryDao getExpenseCategoryDao() {
		if(expenseCategoryDaoImpl == null) {
			expenseCategoryDaoImpl = new ExpenseCategoryDb(db);
		}
		return expenseCategoryDaoImpl;
	}
	
	public IIncomeCategoryDao getIncomeCategoryDao() {
		if(incomeCategoryDaoImpl == null) {
			incomeCategoryDaoImpl = new IncomeCategoryDb(db);
		}
		return incomeCategoryDaoImpl;
	}	
	
	private static class DbOpenHelper extends SQLiteOpenHelper {
		Context context;
		public DbOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			this.context = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(ExpencesDb.TABLE_CREATE);
			db.execSQL(IncomeDb.TABLE_CREATE);
			db.execSQL(TransferDb.TABLE_CREATE);
			db.execSQL(AccountDb.TABLE_CREATE);
			db.execSQL(ExpenseCategoryDb.TABLE_CREATE);
			db.execSQL(IncomeCategoryDb.TABLE_CREATE);
			
			initWithData(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");

			// Drop the old tables
			db.execSQL(ExpencesDb.TABLE_DELETE);
			db.execSQL(IncomeDb.TABLE_DELETE);
			db.execSQL(TransferDb.TABLE_DELETE);
			db.execSQL(AccountDb.TABLE_DELETE);
			db.execSQL(ExpenseCategoryDb.TABLE_DELETE);
			db.execSQL(IncomeCategoryDb.TABLE_DELETE);
			
			// Create a new one.
			onCreate(db);
		}
		
		public void initWithData(SQLiteDatabase db) {
			AccountDb adb = new AccountDb(db);
			String[] accounts = context.getResources().getStringArray(R.array.entries_accounts_default);
			for (String string : accounts) {
				adb.create(new Account(string));
			}
			
			IncomeCategoryDb idb = new IncomeCategoryDb(db);
			String[] incomeCategories = context.getResources().getStringArray(R.array.entries_category_income_default);
			for (String string : incomeCategories) {
				idb.create(new IncomeCategory(string));
			}

			ExpenseCategoryDb edb = new ExpenseCategoryDb(db);
			String[] expenseCategories = context.getResources().getStringArray(R.array.entries_category_expense_default);
			for (String string : expenseCategories) {
				edb.create(new ExpenseCategory(string));
			}
			
		}
	}
}
