package com.am.hfinance.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.am.hfinance.HomeFinanceApplication;
import com.am.hfinance.R;
import com.am.hfinance.dal.IDao;

public abstract class ListEditorActivity<T> extends Activity {

	protected enum Mode {
			view,
			add,
			edit,
			delete
		}

	private EditText editText;
	private BaseAdapter<CheckableItem<T>> adapter;
	private Button btnAddUpdateDelete;
	private ListView listAccounts;
	private HomeFinanceApplication app;
	private InputMethodManager imm;
	private Mode mode;
	private T entity;
	private Button btnCancel;
	private LinearLayout panEdit;

	public ListEditorActivity() {
		super();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_delete, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_add:
			
			// TODO: workaround to show software keyboard after
			// menu is closed.
			editText.postDelayed(new Runnable() {
				public void run() {
					setMode(Mode.add);
				}
			}, 100);
			
			break;
		case R.id.menu_item_delete:
			setMode(Mode.delete);
			break;
		case R.id.menu_item_cancel:
			setMode(Mode.view);
			break;
		case R.id.menu_item_help:
			Intent intent = new Intent(this, HelpActivity.class);
			intent.putExtra(HelpActivity.EXTRA_HELP_TOPIC, getString(R.string.help_addcats_topic));
			intent.putExtra(HelpActivity.EXTRA_HELP_CONTENT, getString(R.string.help_addcats_content));
			startActivity(intent);
		}
		
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_editor);
	
		app = (HomeFinanceApplication)getApplication();
		imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
		
		panEdit = (LinearLayout)findViewById(R.id.pan_edit);
		editText = (EditText) findViewById(R.id.edit_account);
	
		editText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_DPAD_CENTER:
					case KeyEvent.KEYCODE_ENTER:
						if(mode == Mode.add) add();
						else if(mode == Mode.edit) update();
						return true;
					default:
						break;
					}
				}
				return false;
			}
		});
		
		btnAddUpdateDelete = (Button)findViewById(R.id.btn_add_update_delete);
		btnAddUpdateDelete.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
				if(mode == Mode.add) add();
				else if(mode == Mode.edit) update();
				else if(mode == Mode.delete) delete();
			}
		});
		
		btnCancel = (Button)findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				cancel();
			}
		});
		
		listAccounts = ((ListView)findViewById(R.id.list_accounts));
		listAccounts.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				CheckableItem<T> item = adapter.getItem(position);
				if (mode == Mode.view || mode == Mode.edit) {
					entity = item.getData();
					editText.setText(entity.toString());
					setMode(Mode.edit);
				} else if(mode == Mode.delete) {
					item.toggle();
					adapter.notifyDataSetChanged();
				}
			}
		});
		
		setMode(Mode.view);
	}

	@Override
	protected void onResume() {
		super.onResume();
		load();
	}
	
	protected HomeFinanceApplication getApp() {
		return app;
	}
	
	protected abstract IDao<T> getDao();
	
	protected abstract T newEntityFromText(String text);
	
	protected abstract void updateEntityWithText(T entity, String text);

	protected abstract boolean validate(String text);
	
	protected void load() {
		List<T> entities = getDao().read();
		List<CheckableItem<T>> list = new ArrayList<CheckableItem<T>>();
		
		for (T entity : entities) {
			list.add(new CheckableItem<T>(entity));
		}
		
		adapter = new BaseAdapter<CheckableItem<T>>(this, list) {
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = getLayoutInflater().inflate(R.layout.item_checked, parent, false);
				CheckableItem<T> item = getItem(position);
				((TextView)view.findViewById(R.id.text1)).setText(item.getData().toString());
				CheckBox checkBox = (CheckBox)view.findViewById(R.id.check1);
				if(mode == Mode.delete) {
					checkBox.setVisibility(View.VISIBLE);
					checkBox.setChecked(item.getChecked());
				} else {
					checkBox.setVisibility(View.GONE);
				}
				
				return view;
			}
		};
		
		listAccounts.setAdapter(adapter);
	}

	protected boolean add() {
		if(!validate(editText.getText().toString())) return false;
		
		T entity = newEntityFromText(editText.getText().toString());
		getDao().create(entity);
		adapter.add(new CheckableItem<T>(entity));
		editText.setText("");
		adapter.notifyDataSetChanged();
		// Scroll to the last position as a workaround for the
		// listAccounts.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL)
		// not reseting after
		// listAccounts.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED)
		listAccounts.postDelayed(new Runnable() {
			public void run() {
				listAccounts.setSelection(adapter.getCount()-1);
			}
		}, 100);
		
		return true;
	}

	protected boolean update() {
		if(!validate(editText.getText().toString())) return false;
		
		updateEntityWithText(entity, editText.getText().toString());
		getDao().update(entity);
		setMode(Mode.view);
		
		return true;
	}

	protected void cancel() {
		setMode(Mode.view);
	}

	protected void delete() {
		new AsyncTask<Void, Void, Void> () {
			@Override
			protected Void doInBackground(Void... params) {
				for (int i = 0; i < adapter.getCount(); i++) {
					CheckableItem<T> item = adapter.getItem(i);
					if(item.getChecked()) {
						getDao().delete(item.getData());
					}
				}
	
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				load();
				setMode(Mode.view);
			}
		}.execute(null, null);
	}

	protected void setMode(Mode mode) {
		this.mode = mode;
		
		if(mode == Mode.view) {
			panEdit.setVisibility(View.GONE);
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
			if(adapter != null) adapter.notifyDataSetChanged();
		} else if(mode == Mode.add) {
			panEdit.setVisibility(View.VISIBLE);
			btnAddUpdateDelete.setText(R.string.add2);
			editText.setVisibility(View.VISIBLE);
			editText.setText("");
			editText.requestFocus();
			imm.showSoftInput(editText, 0);
		} else if(mode == Mode.edit) {
			panEdit.setVisibility(View.VISIBLE);
			btnAddUpdateDelete.setText(R.string.update);
			editText.setVisibility(View.VISIBLE);
			editText.requestFocus();
			editText.setSelection(editText.getText().length());
			imm.showSoftInput(editText, 0);
		} else if(mode == Mode.delete) {
			panEdit.setVisibility(View.VISIBLE);
			btnAddUpdateDelete.setText(R.string.delete);
			editText.setVisibility(View.GONE);
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
			if(adapter != null) adapter.notifyDataSetChanged();
		}
	}
	
	class CheckableItem<P> {
		private boolean checked;
		private P data;
		
		public CheckableItem (P data) {
			this.data = data;
			this.checked = false;
		}
		
		public boolean getChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}
		
		public boolean toggle() {
			checked = !checked;
			return checked;
		}
		
		public P getData() {
			return data;
		}
	}	
}