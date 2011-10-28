package com.am.hfinance.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;
import android.util.Log;

import com.am.hfinance.Const;
import com.am.rest.RestException;
import com.am.rest.RestServiceClient;

public class ApiClient {
	private static final String URL_API = "http://home.finance.ua/services/index.php";
	private static final String USER_AGENT = "Home Finances (Android � "
			+ Build.VERSION.SDK + ")";
	private static final String CONTENT_TYPE = "application/json";

	private static final RestServiceClient restClient = new RestServiceClient(
			USER_AGENT, CONTENT_TYPE);
	private static final ApiClient instance = new ApiClient();

	private ApiClient() {
	}

	public static final ApiClient instance() {
		return instance;
	}

	public AuthResponse authenticate(String email, String password)
			throws ApiException {
		JSONObject request = new JSONObject();
		try {
			request.put("id", 1);
			request.put("service", "sauth");
			request.put("method", "auth");

			JSONObject param = new JSONObject();
			param.put("email", email);
			param.put("password", password);

			JSONArray params = new JSONArray();
			params.put(param);

			request.put("params", params);
		} catch (JSONException e) {
			throw new ApiException("Failed to build authentication request", e);
		}

		try {
			JSONObject json = execute(request);
			AuthResponse response = AuthResponse.fromJson(json);
			return response;
		} catch (JSONException e) {
			throw new ApiException("Failed to parse authentication response", e);
		}
	}

	private JSONObject execute(JSONObject request) throws ApiException {
		try {
			String plainRequest = request.toString();
			Log.d(Const.TAG, "Executing request : " + plainRequest);
			String plainResponse = restClient.post(URL_API, plainRequest);
			Log.d(Const.TAG, "Received response : " + plainResponse);
			JSONObject response = new JSONObject(plainResponse);

			JSONObject errorObject = response.optJSONObject("error");
			if (errorObject != null) {
				throw new ApiException(errorObject.optString("message"),
						errorObject.optInt("code"), errorObject
								.optInt("origin"));
			}
			
			JSONObject result = response.getJSONObject("result");
			return result;
		} catch (RestException e) {
			throw new ApiException("Failed to preform request "
					+ request.optString("method"), e);
		} catch (JSONException e) {
			throw new ApiException("Failed to parse response"
					+ request.optString("method"), e);
		}
	}
}
