package com.am.hfinance.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.am.hfinance.model.IActivity;

public interface IItemViewBuilder {

	public abstract View buildView(Context context, LayoutInflater inflater,
			IActivity activity, ViewGroup parent);

}