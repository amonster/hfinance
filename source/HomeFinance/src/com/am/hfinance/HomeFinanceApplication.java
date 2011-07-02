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


