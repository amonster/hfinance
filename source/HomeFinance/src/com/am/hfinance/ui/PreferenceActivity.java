package com.am.hfinance.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

import com.am.hfinance.R;

public class PreferenceActivity extends android.preference.PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.main);
		
		       
		findPreference("accounts").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference pref) {
				startActivity(new Intent(PreferenceActivity.this, ManageAccountsActivity.class));
				return true;
			}
		});
		
		findPreference("expense_cats").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference pref) {
				startActivity(new Intent(PreferenceActivity.this, ManageExpenseCatsActivity.class));
				return true;
			}
		});		

		findPreference("income_cats").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference pref) {
				startActivity(new Intent(PreferenceActivity.this, ManageIncomeCatsActivity.class));
				return true;
			}
		});		
	}
}
