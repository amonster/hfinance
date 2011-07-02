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

import com.am.hfinance.dal.IIncomeDao;
import com.am.hfinance.model.Income;

public class IncomeDb implements IIncomeDao {
	public static final String TAG = ExpencesDb.class.getSimpleName();
	private static final String TABLE_INCOME = "income";
	public static final String KEY_INCOME_ID = "id";
	public static final String KEY_INCOME_DATE = "date";
	public static final String KEY_INCOME_COMMENT = "comment";
	public static final String KEY_INCOME_AMOUNT = "amount";
	public static final String KEY_INCOME_CATEGORY = "category";
	public static final String KEY_INCOME_ACCOUNT = "account";
	public static final String KEY_INCOME_CURRENCY = "currency";

	public static final String TABLE_CREATE = "create table "
		+ TABLE_INCOME + " (" + KEY_INCOME_ID
		+ " integer primary key not null," + KEY_INCOME_DATE
		+ " text," + KEY_INCOME_COMMENT + " text,"
		+ KEY_INCOME_AMOUNT + " numeric(10,2) not null,"
		+ KEY_INCOME_CATEGORY + " integer not null,"
		+ KEY_INCOME_ACCOUNT + " integer not null,"
		+ KEY_INCOME_CURRENCY + " integer not null);";
	
	public static final String TABLE_DELETE = "drop table if exists " + TABLE_INCOME;
	
	private SQLiteDatabase db = null;
	
	public IncomeDb(SQLiteDatabase db) {
		this.db = db;
	}	
	
	public void create(Income object) {
		ContentValues values = new ContentValues();
		values.put(KEY_INCOME_DATE, new SimpleDateFormat().format(object
				.getDate()));
		values.put(KEY_INCOME_COMMENT, object.getComment());
		values.put(KEY_INCOME_AMOUNT, object.getAmount().toPlainString());
		values.put(KEY_INCOME_CATEGORY, object.getCategory());
		values.put(KEY_INCOME_ACCOUNT, object.getAccount());
		values.put(KEY_INCOME_CURRENCY, object.getCurrency());
		object.setId(db.insert(TABLE_INCOME, null, values));
	}

	public void delete(Income object) {
		db.delete(TABLE_INCOME, KEY_INCOME_ID + "=?", new String[] { Long
				.toString(object.getId()) });
	}
	
	public void deleteAll() {
		db.delete(TABLE_INCOME, null, null);
	}
	
	public List<Income> read() {
		ArrayList<Income> incomes = new ArrayList<Income>();

		Cursor cursor = db.query(TABLE_INCOME,
				new String[] { KEY_INCOME_ID, KEY_INCOME_DATE, KEY_INCOME_COMMENT,
						KEY_INCOME_AMOUNT, KEY_INCOME_CATEGORY,
						KEY_INCOME_ACCOUNT, KEY_INCOME_CURRENCY }, null,
				null, null, null, null);

		if (cursor.getCount() > 0 && cursor.moveToFirst()) {
			if (cursor.moveToFirst()) {
				do {
					long id = cursor.getLong(cursor.getColumnIndex(KEY_INCOME_ID));
					String dateRaw = cursor.getString(cursor
							.getColumnIndex(KEY_INCOME_DATE));
					String comment = cursor.getString(cursor
							.getColumnIndex(KEY_INCOME_COMMENT));
					String amountRaw = cursor.getString(cursor
							.getColumnIndex(KEY_INCOME_AMOUNT));
					long category = cursor.getLong(cursor
							.getColumnIndex(KEY_INCOME_CATEGORY));
					long account = cursor.getLong(cursor
							.getColumnIndex(KEY_INCOME_ACCOUNT));
					long currency = cursor.getLong(cursor
							.getColumnIndex(KEY_INCOME_CURRENCY));

					Date date;

					try {
						date = new SimpleDateFormat().parse(dateRaw);
					} catch (ParseException e) {
						Log.e(TAG, Log.getStackTraceString(e));
						date = new Date();
					}

					BigDecimal amount = new BigDecimal(amountRaw);

					Income income = new Income(date, comment, amount,
							currency, category, account);
					income.setId(id);
					incomes.add(income);
				} while (cursor.moveToNext());
			}
		}

		return incomes;
	}

	public void update(Income object) {
		ContentValues values = new ContentValues();
		values.put(KEY_INCOME_DATE, new SimpleDateFormat().format(object
				.getDate()));
		values.put(KEY_INCOME_COMMENT, object.getComment());
		values.put(KEY_INCOME_AMOUNT, object.getAmount().toPlainString());
		values.put(KEY_INCOME_CATEGORY, object.getCategory());
		values.put(KEY_INCOME_ACCOUNT, object.getAccount());
		values.put(KEY_INCOME_CURRENCY, object.getCurrency());

		db.update(TABLE_INCOME, values, KEY_INCOME_ID + "=?", new String[] { Long.toString(object.getId()) });
	}
}
