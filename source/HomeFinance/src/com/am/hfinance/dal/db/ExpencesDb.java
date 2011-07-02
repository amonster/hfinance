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
 
package com.am.hfinance.dal.db;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.am.hfinance.dal.IExpenseDao;
import com.am.hfinance.model.Expense;

public class ExpencesDb implements IExpenseDao {
	public static final String TAG = ExpencesDb.class.getSimpleName();
	public static final String TABLE_EXPENSE = "expense";
	public static final String KEY_EXPENSE_ID = "id";
	public static final String KEY_EXPENSE_DATE = "date";
	public static final String KEY_EXPENSE_COMMENT = "comment";
	public static final String KEY_EXPENSE_AMOUNT = "amount";
	public static final String KEY_EXPENSE_CATEGORY = "category";
	public static final String KEY_EXPENSE_ACCOUNT = "account";
	public static final String KEY_EXPENSE_CURRENCY = "currency";
	
	public static final String TABLE_CREATE = "create table "
		+ TABLE_EXPENSE + " (" + KEY_EXPENSE_ID
		+ " integer primary key not null," + KEY_EXPENSE_DATE
		+ " text," + KEY_EXPENSE_COMMENT + " text,"
		+ KEY_EXPENSE_AMOUNT + " numeric(10,2) not null,"
		+ KEY_EXPENSE_CATEGORY + " integer not null,"
		+ KEY_EXPENSE_ACCOUNT + " integer not null,"
		+ KEY_EXPENSE_CURRENCY + " integer not null);";
	
	public static final String TABLE_DELETE = "drop table if exists " + TABLE_EXPENSE + ";";
	
	private SQLiteDatabase db = null;
	
	public ExpencesDb(SQLiteDatabase db) {
		this.db = db;
	}
	
	public void create(Expense object) {
		ContentValues values = new ContentValues();
		values.put(KEY_EXPENSE_DATE, new SimpleDateFormat().format(object
				.getDate()));
		values.put(KEY_EXPENSE_COMMENT, object.getComment());
		values.put(KEY_EXPENSE_AMOUNT, object.getAmount().toPlainString());
		values.put(KEY_EXPENSE_CATEGORY, object.getCategory());
		values.put(KEY_EXPENSE_ACCOUNT, object.getAccount());
		values.put(KEY_EXPENSE_CURRENCY, object.getCurrency());
		object.setId(db.insert(TABLE_EXPENSE, null, values));
	}

	public void delete(Expense object) {
		db.delete(TABLE_EXPENSE, KEY_EXPENSE_ID + "=?", new String[] { Long
				.toString(object.getId()) });
	}
	
	public void deleteAll() {
		db.delete(TABLE_EXPENSE, null, null);
	}

	public List<Expense> read() {
		ArrayList<Expense> expenses = new ArrayList<Expense>();

		Cursor cursor = db.query(TABLE_EXPENSE,
				new String[] { KEY_EXPENSE_ID, KEY_EXPENSE_DATE, KEY_EXPENSE_COMMENT,
						KEY_EXPENSE_AMOUNT, KEY_EXPENSE_CATEGORY,
						KEY_EXPENSE_ACCOUNT, KEY_EXPENSE_CURRENCY }, null,
				null, null, null, null);

		if (cursor.getCount() > 0 && cursor.moveToFirst()) {
			if (cursor.moveToFirst()) {
				do {
					long id = cursor.getLong(cursor
							.getColumnIndex(KEY_EXPENSE_ID));
					String dateRaw = cursor.getString(cursor
							.getColumnIndex(KEY_EXPENSE_DATE));
					String comment = cursor.getString(cursor
							.getColumnIndex(KEY_EXPENSE_COMMENT));
					String amountRaw = cursor.getString(cursor
							.getColumnIndex(KEY_EXPENSE_AMOUNT));
					long category = cursor.getLong(cursor
							.getColumnIndex(KEY_EXPENSE_CATEGORY));
					long account = cursor.getLong(cursor
							.getColumnIndex(KEY_EXPENSE_ACCOUNT));
					long currency = cursor.getLong(cursor
							.getColumnIndex(KEY_EXPENSE_CURRENCY));

					Date date;

					try {
						date = new SimpleDateFormat().parse(dateRaw);
					} catch (ParseException e) {
						Log.e(TAG, Log.getStackTraceString(e));
						date = new Date();
					}

					BigDecimal amount = new BigDecimal(amountRaw);

					Expense expense = new Expense(date, comment, amount,
							currency, category, account);
					expense.setId(id);
					expenses.add(expense);
				} while (cursor.moveToNext());
			}
		}

		return expenses;
	}

	public void update(Expense object) {
		ContentValues values = new ContentValues();
		values.put(KEY_EXPENSE_DATE, new SimpleDateFormat().format(object
				.getDate()));
		values.put(KEY_EXPENSE_COMMENT, object.getComment());
		values.put(KEY_EXPENSE_AMOUNT, object.getAmount().toPlainString());
		values.put(KEY_EXPENSE_CATEGORY, object.getCategory());
		values.put(KEY_EXPENSE_ACCOUNT, object.getAccount());
		values.put(KEY_EXPENSE_CURRENCY, object.getCurrency());

		db.update(TABLE_EXPENSE, values, KEY_EXPENSE_ID + "=?", new String[] { Long.toString(object.getId()) });
	}
}
