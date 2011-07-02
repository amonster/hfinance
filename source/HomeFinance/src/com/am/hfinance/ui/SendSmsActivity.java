package com.am.hfinance.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.am.hfinance.HomeFinanceApplication;
import com.am.hfinance.R;
import com.am.hfinance.dal.IExpenseDao;
import com.am.hfinance.dal.IIncomeDao;
import com.am.hfinance.dal.ITransferDao;
import com.am.hfinance.model.Expense;
import com.am.hfinance.model.Income;
import com.am.hfinance.model.Transfer;
import com.am.hfinance.sms.SmsMessageSender;
import com.am.hfinance.sms.SmsMessageSplitter;

public class SendSmsActivity extends Activity implements OnClickListener {
	public static final String TAG = SendSmsActivity.class.getSimpleName();
	List<String> sms;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.send_sms);
		
		findViewById(R.id.btn_send_sms).setOnClickListener(this);
		findViewById(R.id.help_no_sms).setOnClickListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.sms, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_help:
			Intent intent = new Intent(this, HelpActivity.class);
			intent.putExtra(HelpActivity.EXTRA_HELP_TOPIC, getString(R.string.help_sms_topic));
			intent.putExtra(HelpActivity.EXTRA_HELP_CONTENT, getString(R.string.help_sms_content));
			startActivity(intent);
			break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onResume() {
		super.onResume();

		HomeFinanceApplication app = ((HomeFinanceApplication) getApplication());
		List<String> parts = new ArrayList<String>();
		
		IExpenseDao expenseDao = app.getDaoFactory().getExpenseDao();
		for (Expense expense : expenseDao.read()) {
			parts.add(expense.toString());
		}

		IIncomeDao incomeDao = app.getDaoFactory().getIncomeDao();
		for (Income income : incomeDao.read()) {
			parts.add(income.toString());
		}
		
		ITransferDao transferDao = app.getDaoFactory().getTransferDao();
		for (Transfer transfer : transferDao.read()) {
			parts.add(transfer.toString());
		}		
		
		sms = new SmsMessageSplitter().split(parts);

		ListAdapter adapter = new BaseAdapter<String>(this, sms) {
			public View getView(int position, View paramView,
					ViewGroup root) {
				View view = getLayoutInflater().inflate(R.layout.item_sms, root, false);
				String item = (String)getItem(position);
				
				UiHelper.setText(view, R.id.item_sms, item);
				UiHelper.setText(view, R.id.item_sms_length, Integer.toString(item.length()));
				
				return view;
			}
		};
		
		ListView listSms = (ListView)findViewById(R.id.list_sms);
		listSms.setAdapter(adapter);
		
		View helpView = findViewById(R.id.help_no_sms); 
		if(sms.size() > 0) {
			listSms.setVisibility(View.VISIBLE);
			helpView.setVisibility(View.GONE);
		} else {
			listSms.setVisibility(View.GONE);
			helpView.setVisibility(View.VISIBLE);
			findViewById(R.id.btn_send_sms).setEnabled(false);
		}		
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_send_sms:
			sendSms();
			break;
		case R.id.help_no_sms:
			startActivity(new Intent(this, AddExpenseActivity.class));
			break;
		}
	}
	
	private void sendSms() {
		new AsyncTask<Void, String, Void>() {
			@Override
			protected Void doInBackground(Void... paramArrayOfParams) {
				String phone = getString(R.string.sms_gate);
				ArrayList<String> smsCopy = new ArrayList<String>(sms);
				for (String sms : smsCopy) {
					new SmsMessageSender(SendSmsActivity.this, new String[] { phone }, sms).sendMessage();
					publishProgress(sms);
				}
				
				HomeFinanceApplication app = ((HomeFinanceApplication) getApplication());
				app.getDaoFactory().getIncomeDao().deleteAll();
				app.getDaoFactory().getTransferDao().deleteAll();
				app.getDaoFactory().getExpenseDao().deleteAll();
				
				return null;
			}
			
			@SuppressWarnings("unchecked")
			@Override
			protected void onProgressUpdate(String... values) {
				sms.remove(values[0]);
				BaseAdapter<String> adapter = (BaseAdapter<String>)UiHelper.getAdapter(SendSmsActivity.this, R.id.list_sms);
				adapter.notifyDataSetChanged();
			}
			
			@Override
			protected void onPreExecute() {
				setProgressBarIndeterminateVisibility(true);
			};
			
			@Override
			protected void onPostExecute(Void result) {
				setProgressBarIndeterminateVisibility(false);
				Toast.makeText(SendSmsActivity.this, R.string.toast_sent, Toast.LENGTH_LONG).show();
				finish();
			};
			
		}.execute(null, null);		
	}
}