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
 
package com.am.hfinance.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.am.hfinance.HomeFinanceApplication;
import com.am.hfinance.R;
import com.am.hfinance.dal.IExpenseDao;
import com.am.hfinance.dal.IIncomeDao;
import com.am.hfinance.dal.ITransferDao;
import com.am.hfinance.model.Account;
import com.am.hfinance.model.Expense;
import com.am.hfinance.model.ExpenseCategory;
import com.am.hfinance.model.IActivity;
import com.am.hfinance.model.Income;
import com.am.hfinance.model.IncomeCategory;
import com.am.hfinance.model.Transfer;

public class HomeActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private ListView listActivities;
	private HomeFinanceApplication app;
	private List<Account> accounts;
	private List<IncomeCategory> incomeCategories;
	private List<ExpenseCategory> expenseCategories;

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.help_add_expense:
		case R.id.btn_add_expense:
			startActivity(new Intent(this, AddExpenseActivity.class));
			break;
		case R.id.btn_add_income:
			startActivity(new Intent(this, AddIncomeActivity.class));
			break;
		case R.id.btn_add_transfer:
			startActivity(new Intent(this, AddTransferActivity.class));
			break;
		case R.id.btn_synchronize:
			startActivity(new Intent(this, SendSmsActivity.class));
			break;
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		IActivity activity = (IActivity) listActivities.getAdapter().getItem(
				position);
		Intent editActivityIntent;

		if (activity instanceof Expense) {
			editActivityIntent = new Intent(this, AddExpenseActivity.class);
			editActivityIntent.putExtra(AddExpenseActivity.EXTRA_EXPENSE,
					(Parcelable) activity);
		} else if (activity instanceof Income) {
			editActivityIntent = new Intent(this, AddIncomeActivity.class);
			editActivityIntent.putExtra(AddIncomeActivity.EXTRA_INCOME,
					(Parcelable) activity);
		} else if (activity instanceof Transfer) {
			editActivityIntent = new Intent(this, AddTransferActivity.class);
			editActivityIntent.putExtra(AddTransferActivity.EXTRA_TRANSFER,
					(Parcelable) activity);
		} else {
			throw new IllegalArgumentException();
		}

		editActivityIntent.setAction(Intent.ACTION_EDIT);
		startActivity(editActivityIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_settings:
			startActivity(new Intent(this, PreferenceActivity.class));
			break;
		case R.id.menu_item_about:
			startActivity(new Intent(this, AboutActivity.class));
			break;
		}

		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		initReferences();
	}

	@Override
	protected void onResume() {
		super.onResume();

		List<IActivity> activities = new ArrayList<IActivity>();

		
		IExpenseDao expenseDao = app.getDaoFactory().getExpenseDao();
		activities.addAll(expenseDao.read());

		IIncomeDao incomeDao = app.getDaoFactory().getIncomeDao();
		activities.addAll(incomeDao.read());

		ITransferDao transferDao = app.getDaoFactory().getTransferDao();
		activities.addAll(transferDao.read());
		
		accounts = app.getDaoFactory().getAccountDao().read();
		incomeCategories = app.getDaoFactory().getIncomeCategoryDao().read();
		expenseCategories = app.getDaoFactory().getExpenseCategoryDao().read();

		ListAdapter adapter = new BaseAdapter<IActivity>(this, activities) {
			public View getView(int position, View view, ViewGroup parent) {

				IActivity activity = (IActivity) getItem(position);
				View result = null;
				if (activity instanceof Expense || activity instanceof Income) {
					result = new ExpenseIncomeItemViewBuilder(accounts, expenseCategories, incomeCategories)
							.buildView(getContext(), getLayoutInflater(),
									activity, parent);
				} else {
					result = new TransferItemViewBuilder(accounts)
							.buildView(getContext(), getLayoutInflater(),
									activity, parent);
				}

				return result;
			}
		};

		ListView listActivities = (ListView) findViewById(R.id.list_activities);
		listActivities.setAdapter(adapter);

		View helpView = findViewById(R.id.help_add_expense);
		if (activities.size() > 0) {
			listActivities.setVisibility(View.VISIBLE);
			helpView.setVisibility(View.GONE);
		} else {
			listActivities.setVisibility(View.GONE);
			helpView.setVisibility(View.VISIBLE);
		}
	}

	private void initReferences() {
		app = (HomeFinanceApplication) getApplication();
		listActivities = ((ListView) findViewById(R.id.list_activities));
		findViewById(R.id.btn_add_expense).setOnClickListener(this);
		findViewById(R.id.btn_add_income).setOnClickListener(this);
		findViewById(R.id.btn_add_transfer).setOnClickListener(this);
		findViewById(R.id.btn_synchronize).setOnClickListener(this);
		findViewById(R.id.help_add_expense).setOnClickListener(this);
		listActivities.setOnItemClickListener(this);
	}
}
