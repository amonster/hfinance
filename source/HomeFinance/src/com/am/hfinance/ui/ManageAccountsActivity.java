package com.am.hfinance.ui;

import android.widget.Toast;

import com.am.hfinance.R;
import com.am.hfinance.dal.IDao;
import com.am.hfinance.model.Account;

public class ManageAccountsActivity extends ListEditorActivity<Account> {

	@Override
	protected IDao<Account> getDao() {
		return getApp().getDaoFactory().getAccountDao();
	}

	@Override
	protected Account newEntityFromText(String text) {
		return new Account(text);
	}

	@Override
	protected void updateEntityWithText(Account entity, String text) {
		entity.setName(text);
	}

	@Override
	protected boolean add() {
		boolean result = super.add();
		if(result) {
			Toast.makeText(this, R.string.toast_added_account, Toast.LENGTH_LONG).show();
		}
		return result;
	}
	
	@Override
	protected boolean update() {
		boolean result = super.update();
		if(result) {
			Toast.makeText(this, R.string.toast_edited_account, Toast.LENGTH_LONG).show();
		}
		return result;
	}
	
	@Override
	protected void delete() {
		super.delete();
		Toast.makeText(this, R.string.toast_deleted_accounts, Toast.LENGTH_LONG).show();
	}
	
	@Override
	protected boolean validate(String text) {
		if(text.length() == 0) {
			Toast.makeText(this, R.string.toast_empty_input, Toast.LENGTH_LONG).show();
			return false;
		}
		
		return true;
	}
}