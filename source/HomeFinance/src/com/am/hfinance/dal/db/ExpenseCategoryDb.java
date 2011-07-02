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
