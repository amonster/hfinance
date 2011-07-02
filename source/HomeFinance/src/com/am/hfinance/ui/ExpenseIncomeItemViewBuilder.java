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
import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.am.hfinance.R;
import com.am.hfinance.model.Account;
import com.am.hfinance.model.Expense;
import com.am.hfinance.model.ExpenseCategory;
import com.am.hfinance.model.IActivity;
import com.am.hfinance.model.ICategrorizedActivity;
import com.am.hfinance.model.IncomeCategory;

public class ExpenseIncomeItemViewBuilder implements IItemViewBuilder {
	private List<Account> accounts;
	List<ExpenseCategory> expenseCategories;
	List<IncomeCategory> incomeCategories;

	public ExpenseIncomeItemViewBuilder(List<Account> accounts, List<ExpenseCategory> expenseCategories, List<IncomeCategory> incomeCategories) {
		this.accounts = accounts;
		this.expenseCategories = expenseCategories;
		this.incomeCategories = incomeCategories;
	}

	/* (non-Javadoc)
	 * @see com.am.hfinance.ui.IItemViewBuilder#buildView(android.content.Context, android.view.LayoutInflater, com.am.hfinance.model.IActivity, android.view.ViewGroup)
	 */
	public View buildView(Context context, LayoutInflater inflater, IActivity activity, ViewGroup parent) {
		View result = inflater.inflate(R.layout.item_activity, parent, false);
		ICategrorizedActivity catActivity = (ICategrorizedActivity)activity;
		Resources resources = context.getResources();
		
		String currency = resources.getStringArray(R.array.entries_currency_sign)[(int)catActivity.getCurrency() - 1];
		long index = catActivity.getAccount();
		String account = index > 0 && index <= accounts.size() ? accounts.get((int)index - 1).getName() : "";
		String category = "";
		
		TextView amountView = (TextView) result.findViewById(R.id.item_amount);
		if (catActivity instanceof Expense) {
			amountView.setText(String.format("%s%s %s", catActivity.getAmount().equals(BigDecimal.ZERO)? "": "-", catActivity.getAmount().toPlainString(), currency));
			amountView.setTextColor(resources.getColor(R.color.red));
			
			index = (int)catActivity.getCategory();
			category = index > 0 && index <= expenseCategories.size() ? expenseCategories.get((int)index - 1).getName() : "";
		} else {
			amountView.setText(String.format("%s%s %s", catActivity.getAmount().equals(BigDecimal.ZERO)? "": "+", catActivity.getAmount().toPlainString(), currency));
			amountView.setTextColor(resources.getColor(R.color.green));
			
			index = (int)catActivity.getCategory();
			category = index > 0 && index <= incomeCategories.size() ? incomeCategories.get((int)index - 1).getName() : "";
		}
		
		((TextView) result.findViewById(R.id.item_comment)).setText(catActivity.getComment());
		((TextView) result.findViewById(R.id.item_date)).setText(new SimpleDateFormat().format(catActivity.getDate()));
		((TextView) result.findViewById(R.id.item_category)).setText(category);
		((TextView) result.findViewById(R.id.item_account)).setText(account);
		
		return result;
	}
}
