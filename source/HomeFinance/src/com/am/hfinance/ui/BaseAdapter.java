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