package com.am.hfinance.ui;

import android.widget.Toast;

import com.am.hfinance.R;
import com.am.hfinance.dal.IDao;
import com.am.hfinance.model.IncomeCategory;

public class ManageIncomeCatsActivity extends
		ListEditorActivity<IncomeCategory> {

	@Override
	protected IDao<IncomeCategory> getDao() {
		return getApp().getDaoFactory().getIncomeCategoryDao();
	}

	@Override
	protected IncomeCategory newEntityFromText(String text) {
		return new IncomeCategory(text);
	}

	@Override
	protected void updateEntityWithText(IncomeCategory entity, String text) {
		entity.setName(text);
	}

	@Override
	protected boolean add() {
		boolean result = super.add();
		if(result) {
			Toast.makeText(this, R.string.toast_added_category, Toast.LENGTH_LONG).show();
		}
		
		return result;
	}
	
	@Override
	protected boolean update() {
		boolean result = super.update();
		if(result) {
			Toast.makeText(this, R.string.toast_edited_category, Toast.LENGTH_LONG).show();
		}
		
		return result;
	}
	
	@Override
	protected void delete() {
		super.delete();
		Toast.makeText(this, R.string.toast_deleted_categories, Toast.LENGTH_LONG).show();
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
