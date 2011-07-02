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
import com.am.hfinance.model.IActivity;
import com.am.hfinance.model.Transfer;

public class TransferItemViewBuilder implements IItemViewBuilder {
	
	private List<Account> accounts;

	public TransferItemViewBuilder(List<Account> accounts) {
		this.accounts = accounts;
	}	

	public View buildView(Context context, LayoutInflater inflater,
			IActivity activity, ViewGroup parent) {
		View result = inflater.inflate(R.layout.item_transfer, parent, false);
		Transfer transfer = (Transfer)activity;
		Resources resources = context.getResources();
		String debitCurrency = resources.getStringArray(R.array.entries_currency_sign)[(int)transfer.getCurrency() - 1];
		long index = transfer.getAccount();
		String debitAccount = index > 0 && index <= accounts.size() ? accounts.get((int)index - 1).getName() : "";

		TextView debitAmountView = (TextView) result.findViewById(R.id.item_amount_debit);
		debitAmountView.setText(String.format("%s%s %s", transfer.getAmount().equals(BigDecimal.ZERO)? "": "-", transfer.getAmount().toPlainString(), debitCurrency));
		debitAmountView.setTextColor(resources.getColor(R.color.red));

		String creditCurrency = resources.getStringArray(R.array.entries_currency_sign)[(int)transfer.getCreditCurrency() - 1];
		index = transfer.getCreditAccount();
		String creditAccount = index > 0 && index <= accounts.size() ? accounts.get((int)index - 1).getName() : "";
		
		TextView creditAmountView = (TextView) result.findViewById(R.id.item_amount_credit);
		creditAmountView.setText(String.format(" (%s%s %s)", transfer.getCreditAmount().equals(BigDecimal.ZERO)? "": "+", transfer.getCreditAmount().toPlainString(), creditCurrency));
		creditAmountView.setTextColor(resources.getColor(R.color.green));
		
		((TextView) result.findViewById(R.id.item_comment)).setText(transfer.getComment());
		((TextView) result.findViewById(R.id.item_date)).setText(new SimpleDateFormat().format(transfer.getDate()));
		((TextView) result.findViewById(R.id.item_account_debit)).setText(debitAccount);
		((TextView) result.findViewById(R.id.item_account_credit)).setText(String.format(" (%s)", creditAccount));
		
		return result;
	}

}
