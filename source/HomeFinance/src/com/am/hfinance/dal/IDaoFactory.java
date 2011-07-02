package com.am.hfinance.dal;

public interface IDaoFactory {
	public IExpenseDao getExpenseDao();
	public IIncomeDao getIncomeDao();
	public ITransferDao getTransferDao();
	public IAccountDao getAccountDao();
	public IExpenseCategoryDao getExpenseCategoryDao();
	public IIncomeCategoryDao getIncomeCategoryDao();
}
