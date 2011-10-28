package com.am.hfinance.api;

import java.io.IOException;

import com.am.rest.RestException;

public class ApiException extends Exception {

	/**
	50 — сессия невалидна
	51 — у данного пользователя нет прав для совершения в данном бюджетном аккаунте запрашиваемого действия
	52 — попытка работать по https протоколу в бесплатном аккаунте
	53 — исчерпано количество сессионных дней для данного бесплатного аккаунта
	54 — попытка работать в аккаунте (не своем) у которого нулевой баланс
	55 — попытка сменить тип бюджетной сетки на бесплатном аккаунте
	56 — попытка смотреть историю бюджетного аккаунта, недоступную для бесплатного аккаунта
	57 — неверный E-mail или пароль при авторизации
	**/
	public static final int ERROR_CODE_SESSION_INVALID = 50;
	public static final int ERROR_CODE_ACCESS_DENIED_ACCOUNT = 51;
	public static final int ERROR_CODE_HTTPS_DENIED = 52;
	public static final int ERROR_CODE_OUT_OF_SERVICE = 53;
	public static final int ERROR_CODE_ZERO_BALANCE = 54;
	public static final int ERROR_CODE_ACCESS_DENIED_BUDGET_NET = 55;
	public static final int ERROR_CODE_ACCESS_DENIED_HISTORY = 56;
	public static final int ERROR_CODE_AUTHENTICATION_FAILED = 57;
	
	private static final long serialVersionUID = 719227397L;
	private int code;
	private int orig;
	
	public ApiException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}
	
	public ApiException(String detailMessage, int code, int orig) {
		super(detailMessage);
		this.setCode(code);
		this.setOrig(orig);
	}

	public ApiException(String detailMessage) {
		super(detailMessage);
	}

	public boolean isNetworkDown() {
		boolean result = false;

		if (getCause() != null && getCause() instanceof RestException
				&& getCause().getCause() != null
				&& getCause().getCause() instanceof IOException) {

			result = true;
		}

		return result;
	}
	
	public boolean isLogicalError() {
		return getCode() != 0;
	}

	private void setCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	private void setOrig(int orig) {
		this.orig = orig;
	}

	public int getOrig() {
		return orig;
	}
}