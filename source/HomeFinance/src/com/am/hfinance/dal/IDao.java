package com.am.hfinance.dal;

import java.util.List;

public interface IDao<T> {
	public void create(T object);
	public List<T> read();
	public void update(T object);
	public void delete(T object);
	public void deleteAll();
}
