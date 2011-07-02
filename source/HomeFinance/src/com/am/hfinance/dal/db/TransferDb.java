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

import com.am.hfinance.dal.ITransferDao;
import com.am.hfinance.model.Transfer;

public class TransferDb implements ITransferDao {
	public static final String TAG = ExpencesDb.class.getSimpleName();
	private static final String TABLE_TRANSFER = "transfer";
	public static final String KEY_TRANSFER_ID = "id";
	public static final String KEY_TRANSFER_DATE = "date";
	public static final String KEY_TRANSFER_COMMENT = "comment";
	public static final String KEY_TRANSFER_DEBIT_AMOUNT = "debit_amount";
	public static final String KEY_TRANSFER_DEBIT_ACCOUNT = "debit_account";
	public static final String KEY_TRANSFER_DEBIT_CURRENCY = "debit_currency";
	public static final String KEY_TRANSFER_CREDIT_AMOUNT = "credit_amount";
	public static final String KEY_TRANSFER_CREDIT_ACCOUNT = "credit_account";
	public static final String KEY_TRANSFER_CREDIT_CURRENCY = "credit_currency";

	public static final String TABLE_CREATE = "create table " + TABLE_TRANSFER
			+ " (" + KEY_TRANSFER_ID + " integer primary key not null,"
			+ KEY_TRANSFER_DATE + " text," + KEY_TRANSFER_COMMENT + " text,"
			+ KEY_TRANSFER_DEBIT_AMOUNT + " numeric(10,2) not null,"
			+ KEY_TRANSFER_DEBIT_ACCOUNT + " integer not null,"
			+ KEY_TRANSFER_DEBIT_CURRENCY + " integer not null,"
			+ KEY_TRANSFER_CREDIT_AMOUNT + " numeric(10,2) not null,"
			+ KEY_TRANSFER_CREDIT_ACCOUNT + " integer not null,"
			+ KEY_TRANSFER_CREDIT_CURRENCY + " integer not null);";

	public static final String TABLE_DELETE = "drop table if exists "
			+ TABLE_TRANSFER;

	private SQLiteDatabase db = null;

	public TransferDb(SQLiteDatabase db) {
		this.db = db;
	}

	public void create(Transfer object) {
		ContentValues values = new ContentValues();
		values.put(KEY_TRANSFER_DATE, new SimpleDateFormat().format(object
				.getDate()));
		values.put(KEY_TRANSFER_COMMENT, object.getComment());
		values.put(KEY_TRANSFER_DEBIT_AMOUNT, object.getAmount()
				.toPlainString());
		values.put(KEY_TRANSFER_DEBIT_ACCOUNT, object.getAccount());
		values.put(KEY_TRANSFER_DEBIT_CURRENCY, object.getCurrency());
		values.put(KEY_TRANSFER_CREDIT_AMOUNT, object.getCreditAmount()
				.toPlainString());
		values.put(KEY_TRANSFER_CREDIT_ACCOUNT, object.getCreditAccount());
		values.put(KEY_TRANSFER_CREDIT_CURRENCY, object.getCreditCurrency());
		object.setId(db.insert(TABLE_TRANSFER, null, values));
	}

	public void delete(Transfer object) {
		db.delete(TABLE_TRANSFER, KEY_TRANSFER_ID + "=?", new String[] { Long
				.toString(object.getId()) });
	}
	
	public void deleteAll() {
		db.delete(TABLE_TRANSFER, null, null);
	}

	public List<Transfer> read() {
		ArrayList<Transfer> transfers = new ArrayList<Transfer>();

		Cursor cursor = db.query(TABLE_TRANSFER, new String[] {
				KEY_TRANSFER_ID, KEY_TRANSFER_DATE, KEY_TRANSFER_COMMENT,
				KEY_TRANSFER_DEBIT_AMOUNT, KEY_TRANSFER_DEBIT_ACCOUNT,
				KEY_TRANSFER_DEBIT_CURRENCY, KEY_TRANSFER_CREDIT_AMOUNT, KEY_TRANSFER_CREDIT_ACCOUNT,
				KEY_TRANSFER_CREDIT_CURRENCY }, null, null, null, null, null);

		if (cursor.getCount() > 0 && cursor.moveToFirst()) {
			if (cursor.moveToFirst()) {
				do {
					long id = cursor.getLong(cursor.getColumnIndex(KEY_TRANSFER_ID));
					String dateRaw = cursor.getString(cursor
							.getColumnIndex(KEY_TRANSFER_DATE));
					String comment = cursor.getString(cursor
							.getColumnIndex(KEY_TRANSFER_COMMENT));
					String amountRaw = cursor.getString(cursor
							.getColumnIndex(KEY_TRANSFER_DEBIT_AMOUNT));
					long account = cursor.getLong(cursor
							.getColumnIndex(KEY_TRANSFER_DEBIT_ACCOUNT));
					long currency = cursor.getLong(cursor
							.getColumnIndex(KEY_TRANSFER_DEBIT_CURRENCY));
					String creditAmountRaw = cursor.getString(cursor
							.getColumnIndex(KEY_TRANSFER_CREDIT_AMOUNT));
					long creditAccount = cursor.getLong(cursor
							.getColumnIndex(KEY_TRANSFER_CREDIT_ACCOUNT));
					long creditCurrency = cursor.getLong(cursor
							.getColumnIndex(KEY_TRANSFER_CREDIT_CURRENCY));

					Date date;

					try {
						date = new SimpleDateFormat().parse(dateRaw);
					} catch (ParseException e) {
						Log.e(TAG, Log.getStackTraceString(e));
						date = new Date();
					}

					BigDecimal amount = new BigDecimal(amountRaw);
					BigDecimal creditAmount = new BigDecimal(creditAmountRaw);

					Transfer transfer = new Transfer(date, comment, amount, currency, account, creditAmount, creditCurrency, creditAccount);
					transfer.setId(id);
					transfers.add(transfer);
				} while (cursor.moveToNext());
			}
		}

		return transfers;
	}

	public void update(Transfer object) {
		ContentValues values = new ContentValues();
		values.put(KEY_TRANSFER_DATE, new SimpleDateFormat().format(object
				.getDate()));
		values.put(KEY_TRANSFER_COMMENT, object.getComment());
		values.put(KEY_TRANSFER_DEBIT_AMOUNT, object.getAmount()
				.toPlainString());
		values.put(KEY_TRANSFER_DEBIT_ACCOUNT, object.getAccount());
		values.put(KEY_TRANSFER_DEBIT_CURRENCY, object.getCurrency());
		values.put(KEY_TRANSFER_CREDIT_AMOUNT, object.getCreditAmount()
				.toPlainString());
		values.put(KEY_TRANSFER_CREDIT_ACCOUNT, object.getCreditAccount());
		values.put(KEY_TRANSFER_CREDIT_CURRENCY, object.getCreditCurrency());

		db.update(TABLE_TRANSFER, values, KEY_TRANSFER_ID + "=?", new String[] { Long.toString(object.getId()) });
	}
}
