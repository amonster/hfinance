/***
	Copyright (c) 2011 Android Monsters (http://androidmonsters.com)
	
	Licensed under the Apache License, Version 2.0 (the "License"); you may
	not use this file except in compliance with the License. You may obtain
	a copy of the License at
		http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */

package com.am.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

public class RestServiceClient {
	private static final int BUFFER_SIZE = 1024;
	private String userAgent;
	private String contentType;

	public RestServiceClient(String userAgent) {
		this.userAgent = userAgent;
	}

	public RestServiceClient(String userAgent, String contentType) {
		this.userAgent = userAgent;
		this.contentType = contentType;
	}

	/**
	 * Get the raw text content of the given URL. This call blocks until the
	 * operation has completed.
	 * 
	 * @param url
	 *            The exact URL to request.
	 * @return The raw content returned by the server.
	 * @throws RestException
	 *             If any connection or server error occurs.
	 */
	public String get(String url) throws RestException {
		try {
			HttpResponse response = execute(new HttpGet(url));
			return getPlainContent(response);
		} catch (IOException e) {
			throw new RestException("GET failed with the REST web service", e);
		}
	}

	/**
	 * Get the raw binary content of the given URL. This call blocks until the
	 * operation has completed.
	 * 
	 * @param url
	 *            The exact URL to request.
	 * @return The raw content returned by the server.
	 * @throws RestException
	 *             If any connection or server error occurs.
	 */
	public InputStream getBinary(String url) throws RestException {
		try {
			HttpResponse response = execute(new HttpGet(url));

			// Check if server response is valid
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				throw new RestException("Error response from server: "
						+ status.toString(), status.getStatusCode());
			}

			return response.getEntity().getContent();
		} catch (IOException e) {
			throw new RestException("GET failed with the REST web service", e);
		}
	}

	/**
	 * Post empty request and return the raw text content of the given URL. This
	 * call blocks until the operation has completed.
	 * 
	 * @param url
	 *            The exact URL to request.
	 * @return The raw content returned by the server.
	 * @throws RestException
	 *             If any connection or server error occurs.
	 */
	public String post(String url) throws RestException {
		try {
			HttpResponse response = execute(new HttpPost(url));
			return getPlainContent(response);
		} catch (IOException e) {
			throw new RestException("POST failed with the REST web service", e);
		}
	}

	/**
	 * Posts string data to the given URL. This call blocks until the operation
	 * has completed.
	 * 
	 * @param url
	 *            The exact URL to request.
	 * @param body
	 *            String content.
	 * @return The raw content returned by the server.
	 * @throws RestException
	 *             If any connection or server error occurs.
	 */
	public String post(String url, String body) throws RestException {
		try {
			HttpPost request = new HttpPost(url);
			request.setEntity(new StringEntity(body, HTTP.UTF_8));
			HttpResponse response = execute(request);
			return getPlainContent(response);
		} catch (IOException e) {
			throw new RestException("POST failed with the REST web service", e);
		}
	}

	/**
	 * Post form to the given URL. This call blocks until the operation has
	 * completed.
	 * 
	 * @param url
	 *            The exact URL to request.
	 * @param formParams
	 *            List of form parameters represented as name-value pairs.
	 * @return The raw content returned by the server.
	 * @throws RestException
	 *             If any connection or server error occurs.
	 */
	public String post(String url, List<NameValuePair> formParams)
			throws RestException {
		try {
			HttpPost request = new HttpPost(url);
			setFormParams(request, formParams);

			HttpResponse response = execute(request);

			return getPlainContent(response);
		} catch (IOException e) {
			throw new RestException("POST failed with the REST web service", e);
		}
	}

	/**
	 * Post empty request and return the raw text content of the given URL. This
	 * call blocks until the operation has completed.
	 * 
	 * @param url
	 *            The exact URL to request.
	 * @param formParams
	 *            List of form parameters represented as name-value pairs.
	 * @param headers
	 *            List of additional http headers.
	 * @return The raw content returned by the server.
	 * @throws RestException
	 *             If any connection or server error occurs.
	 */
	public String post(String url, List<NameValuePair> formParams,
			List<NameValuePair> headers) throws RestException {
		try {
			HttpPost request = new HttpPost(url);
			setHeaders(request, headers);
			setFormParams(request, formParams);

			HttpResponse response = execute(request);

			return getPlainContent(response);
		} catch (IOException e) {
			throw new RestException("POST failed with the REST web service", e);
		}
	}

	protected void setHeaders(HttpEntityEnclosingRequestBase request,
			List<NameValuePair> headers) {
		if (request == null || headers == null)
			return;

		for (NameValuePair pair : headers) {
			request.addHeader(pair.getName(), pair.getValue());
		}
	}

	protected void setFormParams(HttpEntityEnclosingRequestBase request,
			List<NameValuePair> formParams) throws UnsupportedEncodingException {
		if (request == null || formParams == null)
			return;

		// Set HTTP request entity.
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formParams,
				HTTP.UTF_8);
		request.addHeader(formEntity.getContentType());
		request.addHeader(formEntity.getContentEncoding());
		request.setEntity(formEntity);
	}

	protected HttpResponse execute(HttpRequestBase request)
			throws ClientProtocolException, IOException {
		// Create client.
		HttpClient client = new DefaultHttpClient();

		if (userAgent != null && userAgent.length() > 0)
			request.setHeader("User-Agent", userAgent);

		if (contentType != null && contentType.length() > 0)
			request.setHeader("Content-Type", contentType);

		return client.execute(request);
	}

	protected String getPlainContent(HttpResponse response)
			throws RestException, IOException {
		// Check if server response is valid
		StatusLine status = response.getStatusLine();
		if (status.getStatusCode() != HttpStatus.SC_OK) {
			throw new RestException("Error response from server: "
					+ status.toString(), status.getStatusCode());
		}

		// Pull content stream from response
		HttpEntity entity = response.getEntity();
		InputStream inputStream = entity.getContent();

		ByteArrayOutputStream content = new ByteArrayOutputStream();

		// Read response into a buffered stream
		int readBytes = 0;
		byte[] buffer = new byte[BUFFER_SIZE];
		while ((readBytes = inputStream.read(buffer)) != -1) {
			content.write(buffer, 0, readBytes);
		}

		// Return result from buffered stream
		return new String(content.toByteArray());
	}
}