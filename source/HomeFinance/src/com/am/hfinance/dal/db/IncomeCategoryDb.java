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
