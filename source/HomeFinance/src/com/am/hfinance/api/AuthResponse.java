package com.am.hfinance.api;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthResponse {
	public String token;
	public String idUser;
	public String idAccount;
	public String role;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getIdAccount() {
		return idAccount;
	}

	public void setIdAccount(String idAccount) {
		this.idAccount = idAccount;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public static AuthResponse fromJson(JSONObject json) throws JSONException {
		AuthResponse result = new AuthResponse();
		result.token = json.getString("token");
		result.idUser = json.getString("idUser");
		result.idAccount = json.getString("idAccount");
		result.role = json.getString("role");
		return result;
	}
}
