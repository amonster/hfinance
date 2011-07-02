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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.am.hfinance.HomeFinanceApplication;
import com.am.hfinance.R;
import com.am.hfinance.model.Account;
import com.am.hfinance.model.Transfer;

public class AddTransferActivity extends Activity implements OnClickListener {
	public enum Mode {
		add,
		edit
	}
	
	public static final String EXTRA_TRANSFER = "transfer";
	private Transfer transfer;
	private Mode mode = Mode.add;
	private HomeFinanceApplication app;
	private Button btnAdd;
	private Spinner listAccountsFrom;
	private Spinner listAccountsTo;
	private ArrayAdapter<Account> accountsAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transfer);

		initRefs();
		processIntent();
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_add:
			if(mode == Mode.add) {
				addTransfer();
			} else {
				updateTransfer(transfer);
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
			updateTransfer(transfer);
			break;
		default:
			deleteTransfer(transfer);
			break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	private void processIntent() {
		Intent intent = getIntent();
		String action = intent.getAction();
		
		List<Account> accounts = app.getDaoFactory().getAccountDao().read();
		accountsAdapter = new ArrayAdapter<Account>(this, android.R.layout.simple_spinner_item, accounts);
		accountsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		listAccountsFrom.setAdapter(accountsAdapter);
		listAccountsTo.setAdapter(accountsAdapter);		
		
		if(Intent.ACTION_EDIT.equalsIgnoreCase(action)) {
			mode = Mode.edit;
			transfer = (Transfer)intent.getParcelableExtra(EXTRA_TRANSFER);
			populateWithData(transfer);
			((TextView)findViewById(R.id.title)).setText(R.string.title_edit_transfer);
			this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		} else {
			((TextView)findViewById(R.id.title)).setText(R.string.title_add_transfer);
			this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);			
		}
	}
	
	private void initRefs() {
		app = (HomeFinanceApplication)getApplication();
		btnAdd = (Button)findViewById(R.id.btn_add);

		listAccountsFrom = (Spinner)findViewById(R.id.list_account_from);
		listAccountsTo = (Spinner)findViewById(R.id.list_account_to);

		btnAdd.setOnClickListener(this);		
	}	
	
	private void populateWithData(Transfer transfer) {
		UiHelper.setText(this, R.id.text_comment, transfer.getComment());
		
		UiHelper.setText(this, R.id.text_amount_from, transfer.getAmount().toString());
		UiHelper.setAdapterViewSelectionId(this, R.id.list_currency_from, (int)transfer.getCurrency() - 1);
		UiHelper.setAdapterViewSelectionId(this, R.id.list_account_from, (int)transfer.getAccount() - 1);

		UiHelper.setText(this, R.id.text_amount_to, transfer.getCreditAmount().toString());
		UiHelper.setAdapterViewSelectionId(this, R.id.list_currency_to, (int)transfer.getCreditCurrency() - 1);
		UiHelper.setAdapterViewSelectionId(this, R.id.list_account_to, (int)transfer.getCreditAccount() - 1);		
		
		UiHelper.setText(this, R.id.btn_add, R.string.save);
	}	
	
	private void addTransfer() {
		String comment = UiHelper.getText(this, R.id.text_comment);
		
		String amount = UiHelper.getText(this, R.id.text_amount_from);
		long currency = UiHelper.getAdapterViewSelectionId(this, R.id.list_currency_from) + 1;
		long account = UiHelper.getAdapterViewSelectionId(this, R.id.list_account_from) + 1;

		String creditAmount = UiHelper.getText(this, R.id.text_amount_to);
		long creditCurrency = UiHelper.getAdapterViewSelectionId(this, R.id.list_currency_to) + 1;
		long creditAccount = UiHelper.getAdapterViewSelectionId(this, R.id.list_account_to) + 1;
		
		if(comment.length() == 0) {
			Toast.makeText(this, R.string.toast_empty_comment, Toast.LENGTH_LONG).show();
			return;
		}
		
		if(amount.length() == 0) {
			Toast.makeText(this, R.string.toast_empty_amount, Toast.LENGTH_LONG).show();
			return;
		}
		
		if(creditAmount.length() == 0) {
			Toast.makeText(this, R.string.toast_empty_amount, Toast.LENGTH_LONG).show();
			return;
		}

		
		Transfer transfer = new Transfer(new Date(), comment, new BigDecimal(amount), currency, account, new BigDecimal(creditAmount), creditCurrency, creditAccount);
		((HomeFinanceApplication)getApplication()).getDaoFactory().getTransferDao().create(transfer);
		
		Toast.makeText(this, R.string.toast_added_transfer, Toast.LENGTH_LONG).show();
	}

	private void updateTransfer(Transfer transfer) {
		String comment = UiHelper.getText(this, R.id.text_comment);
		
		String amount = UiHelper.getText(this, R.id.text_amount_from);
		long currency = UiHelper.getAdapterViewSelectionId(this, R.id.list_currency_from) + 1;
		long account = UiHelper.getAdapterViewSelectionId(this, R.id.list_account_from) + 1;

		String creditAmount = UiHelper.getText(this, R.id.text_amount_to);
		long creditCurrency = UiHelper.getAdapterViewSelectionId(this, R.id.list_currency_to) + 1;
		long creditAccount = UiHelper.getAdapterViewSelectionId(this, R.id.list_account_to) + 1;

		if(comment.length() == 0) {
			Toast.makeText(this, R.string.toast_empty_comment, Toast.LENGTH_LONG).show();
			return;
		}
		
		if(amount.length() == 0) {
			Toast.makeText(this, R.string.toast_empty_amount, Toast.LENGTH_LONG).show();
			return;
		}
		
		if(creditAmount.length() == 0) {
			Toast.makeText(this, R.string.toast_empty_amount, Toast.LENGTH_LONG).show();
			return;
		}
		
		transfer.setComment(comment);
		
		transfer.setAmount(new BigDecimal(amount));
		transfer.setCurrency(currency);
		transfer.setAccount(account);

		transfer.setCreditAmount(new BigDecimal(creditAmount));
		transfer.setCreditCurrency(creditCurrency);
		transfer.setCreditAccount(creditAccount);
		
		((HomeFinanceApplication)getApplication()).getDaoFactory().getTransferDao().update(transfer);
		
		Toast.makeText(this, R.string.toast_edited_transfer, Toast.LENGTH_LONG).show();
	}
	
	private void deleteTransfer(Transfer transfer) {
		((HomeFinanceApplication)getApplication()).getDaoFactory().getTransferDao().delete(transfer);
		Toast.makeText(this, R.string.toast_deleted_transfer, Toast.LENGTH_LONG).show();
		finish();
	}
}
