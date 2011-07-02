package com.am.hfinance.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
	private List<T> objects;
	private Context context;
	private LayoutInflater layoutInflater;

	public BaseAdapter(Context context, List<T> objects) {
		this.context = context;
		this.layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.objects = objects;
	}

	public int getCount() {
		return this.objects.size();
	}

	public T getItem(int position) {
		return objects.get(position);
	}	

	public long getItemId(int position) {
		return position;
	}

	protected Context getContext() {
		return context;
	}

	protected LayoutInflater getLayoutInflater() {
		return this.layoutInflater;
	}

    public void add(T item) {
        if( item == null ) {
            return;
        }
        this.objects.add(item);
    }
    
    public void clear() {
    	this.objects.clear();
    }
}