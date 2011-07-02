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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.am.hfinance.HomeFinanceApplication;
import com.am.hfinance.R;
import com.am.hfinance.model.Account;
import com.am.hfinance.model.Expense;
import com.am.hfinance.model.ExpenseCategory;

public class AddExpenseActivity extends Activity implements OnClickListener {
	public enum Mode {
		add,
		edit
	}

	public static final String EXTRA_EXPENSE = "expense";
	private Expense expense;
	private Mode mode = Mode.add;
	private Spinner listCategories;
	private Spinner listAccounts;
	private ArrayAdapter<Account> accountsAdapter;
	private HomeFinanceApplication app;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.income_expense);

		initReferences();
		processIntent();
    }
    
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_add:
			if(mode == Mode.add) {
				addExpense();
			} else {
				updateExpense(expense);
			}
			
			break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(mode == Mode.edit) {
			getMenuInflater().inflate(R.menu.edit_expense, menu);
		}

		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_save:
			updateExpense(expense);
			break;
		default:
			deleteExpense(expense);
			break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}

	private void processIntent() {
		// Fill dropdown list of expense categories
		List<ExpenseCategory> expenseCategories = app.getDaoFactory().getExpenseCategoryDao().read();
		ArrayAdapter<ExpenseCategory> adapter = new ArrayAdapter<ExpenseCategory>(this,
				android.R.layout.simple_spinner_item, expenseCategories);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		listCategories.setAdapter(adapter);
		
		List<Account> accounts = app.getDaoFactory().getAccountDao().read();
		accountsAdapter = new ArrayAdapter<Account>(this, android.R.layout.simple_spinner_item, accounts);
		accountsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		listAccounts.setAdapter(accountsAdapter);
		
		Intent intent = getIntent();
		String action = intent.getAction();
		
		if(Intent.ACTION_EDIT.equalsIgnoreCase(action)) {
			mode = Mode.edit;
			expense = (Expense)intent.getParcelableExtra(EXTRA_EXPENSE);
			populateWithData(expense);
			((TextView)findViewById(R.id.title)).setText(R.string.title_edit_expense);
			this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		} else {
			((TextView)findViewById(R.id.title)).setText(R.string.title_add_expense);
			this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);			
		}
		
	}

	private void populateWithData(Expense expense) {
		UiHelper.setText(this, R.id.text_comment, expense.getComment());
		UiHelper.setText(this, R.id.text_amount, expense.getAmount().toString());
		
		UiHelper.setAdapterViewSelectionId(this, R.id.list_currency, (int)expense.getCurrency() - 1);
		UiHelper.setAdapterViewSelectionId(this, R.id.list_account, (int)expense.getAccount() - 1);
		UiHelper.setAdapterViewSelectionId(this, R.id.list_category, (int)expense.getCategory() - 1);
		
		UiHelper.setText(this, R.id.btn_add, R.string.save);
	}

	private void initReferences() {
		app = (HomeFinanceApplication)getApplication();
		listCategories = (Spinner)findViewById(R.id.list_category);
		listAccounts = (Spinner)findViewById(R.id.list_account);
		
		findViewById(R.id.btn_add).setOnClickListener(this);
		
	}

	private void addExpense() {
		String comment = UiHelper.getText(this, R.id.text_comment);
		String amount = UiHelper.getText(this, R.id.text_amount);

		if(comment.length() == 0) {
			Toast.makeText(this, R.string.toast_empty_comment, Toast.LENGTH_LONG).show();
			return;
		}
		
		if(amount.length() == 0) {
			Toast.makeText(this, R.string.toast_empty_amount, Toast.LENGTH_LONG).show();
			return;
		}
		
		long currency = UiHelper.getAdapterViewSelectionId(this, R.id.list_currency) + 1;
		long account = UiHelper.getAdapterViewSelectionId(this, R.id.list_account) + 1;
		long category = UiHelper.getAdapterViewSelectionId(this, R.id.list_category) + 1;
		
		Expense expense = new Expense(new Date(), comment, new BigDecimal(amount), currency, category, account);
		((HomeFinanceApplication)getApplication()).getDaoFactory().getExpenseDao().create(expense);
		
		Toast.makeText(this, R.string.toast_added_expense, Toast.LENGTH_LONG).show();
	}
	
	private void updateExpense(Expense expense) {
		String comment = UiHelper.getText(this, R.id.text_comment);
		String amount = UiHelper.getText(this, R.id.text_amount);

		if(comment.length() == 0) {
			Toast.makeText(this, R.string.toast_empty_comment, Toast.LENGTH_LONG).show();
			return;
		}
		
		if(amount.length() == 0) {
			Toast.makeText(this, R.string.toast_empty_amount, Toast.LENGTH_LONG).show();
			return;
		}		
		
		long currency = UiHelper.getAdapterViewSelectionId(this, R.id.list_currency) + 1;
		long account = UiHelper.getAdapterViewSelectionId(this, R.id.list_account) + 1;
		long category = UiHelper.getAdapterViewSelectionId(this, R.id.list_category) + 1;

		expense.setAmount(new BigDecimal(amount));
		expense.setComment(comment);
		
		expense.setCurrency(currency);
		expense.setAccount(account);
		expense.setCategory(category);
		
		((HomeFinanceApplication)getApplication()).getDaoFactory().getExpenseDao().update(expense);
		
		Toast.makeText(this, R.string.toast_edited_expense, Toast.LENGTH_LONG).show();
	}
	
	
	private void deleteExpense(Expense expense) {
		((HomeFinanceApplication)getApplication()).getDaoFactory().getExpenseDao().delete(expense);
		Toast.makeText(this, R.string.toast_deleted_expense, Toast.LENGTH_LONG).show();
		finish();
	}
}