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

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.am.hfinance.model.IFactoryMethod;
import com.am.hfinance.model.SimpleEntity;

public abstract class SimpleDictionaryDb<T extends SimpleEntity> {

	public static final String KEY_ID = "id";
	public static final String KEY_NAME = "name";
	private SQLiteDatabase db = null;
	private IFactoryMethod<T> factoryMethod;

	public SimpleDictionaryDb(SQLiteDatabase db, IFactoryMethod<T> factoryMethod) {
		this.db = db;
		this.factoryMethod = factoryMethod;
	}

	public void create(T object) {
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, object.getName());
		object.setId(db.insert(getTableName(), null, values));		
	}

	public void delete(T object) {
		db.delete(getTableName(), KEY_ID + "=?", new String[] { Long
				.toString(object.getId()) });
	}

	public void deleteAll() {
		db.delete(getTableName(), null, null);
	}

	public List<T> read() {
		ArrayList<T> entities = new ArrayList<T>();
	
		Cursor cursor = db.query(getTableName(),
				new String[] { KEY_ID, KEY_NAME }, null,
				null, null, null, null);
	
		if (cursor.getCount() > 0 && cursor.moveToFirst()) {
			if (cursor.moveToFirst()) {
				do {
					long id = cursor.getLong(cursor
							.getColumnIndex(KEY_ID));
					String name = cursor.getString(cursor
							.getColumnIndex(KEY_NAME));
	
					T entity = factoryMethod.create();
					entity.setId(id);
					entity.setName(name);
					entities.add(entity);
				} while (cursor.moveToNext());
			}
		}
	
		return entities;
	}

	public void update(T object) {
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, object.getName());
		db.update(getTableName(), values, KEY_ID + "=?", new String[] { Long.toString(object.getId()) });
	}
	
	protected abstract String getTableName();

}