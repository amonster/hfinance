package com.am.hfinance.ui;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class UiHelper {
	public static long getAdapterViewSelectionId(Activity activity, int id) {
		return ((AdapterView<?>)activity.findViewById(id)).getSelectedItemId();
	}
	
	public static void setAdapterViewSelectionId(Activity activity, int id, int selectionId) {
		AdapterView<?> view = (AdapterView<?>)activity.findViewById(id);
		if(selectionId >= 0 && selectionId < view.getCount()) { 
			view.setSelection(selectionId);
		}
	}

	public static ListAdapter getAdapter(Activity activity, int id) {
		return ((ListView)activity.findViewById(id)).getAdapter();
	}
	
	public static void setAdapter(Activity activity, int id, ListAdapter adapter) {
		((ListView)activity.findViewById(id)).setAdapter(adapter);
	}
	
	public static String getText(Activity activity, int id) {
		return ((TextView)activity.findViewById(id)).getText().toString();
	}
	
	public static void setText(View view, int id, String text) {
		((TextView)view.findViewById(id)).setText(text);
	}	
	
	public static void setText(Activity activity, int id, String text) {
		((TextView)activity.findViewById(id)).setText(text);
	}	

	public static void setText(Activity activity, int id, int resId) {
		((TextView)activity.findViewById(id)).setText(resId);
	}	
}
