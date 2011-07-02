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
 
package com.am.hfinance;

import android.app.Application;

import com.am.hfinance.dal.IDaoFactory;
import com.am.hfinance.dal.db.HomeFinanceDb;

public class HomeFinanceApplication extends Application {
	HomeFinanceDb db = null;

	@Override
	public void onCreate() {
		super.onCreate();

		db = new HomeFinanceDb(HomeFinanceApplication.this);
		db.open();
	}
	
	public IDaoFactory getDaoFactory() {
		return db;
	}
}


