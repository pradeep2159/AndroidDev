package com.shiva;

import java.io.InputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class ConnectionManager {

	private DefaultHttpClient httpclient;
	private HttpPost httppost;

	HttpGet getRequest;
	InputStream iStream;
	String response = null;

	private static final int TIME_OUT_CONNECTION = 30000;
	private static final int TIME_OUT_SOCKET = 50000;

	public String setUpHttpPost(String url, String string) {

		try {

			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is
			// established.
			// The default value is zero, that means the timeout is not used.
			int timeoutConnection = TIME_OUT_CONNECTION;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = TIME_OUT_SOCKET;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			// Log.v("TAG", "TIMe OUT   1");
			httpclient = new DefaultHttpClient(httpParameters);
			httppost = new HttpPost(url);

			httppost.setHeader("Content-type",
					"application/json");

			httppost.setEntity(new StringEntity(string, "UTF-8"));
			HttpResponse httpResponse = httpclient.execute(httppost);

			httpResponse.getStatusLine().getStatusCode();

			Log.v("TAG", "getStatusCode(): "
					+ httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				response = EntityUtils.toString(httpResponse.getEntity());
			} else {
				response = null;
			}

			Log.v("TAG", "URL: " + url);
			Log.v("TAG", "REQUEST: " + string);
			Log.v("TAG", "RESPONSE : " + response);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return response;
	}

	public String makeGetRequestgetJSONResponse(String uri) {

		try {

			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is
			// established.
			// The default value is zero, that means the timeout is not used.
			int timeoutConnection = TIME_OUT_CONNECTION;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = TIME_OUT_SOCKET;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			// Log.v("TAG", "TIMe OUT   1");
			// Log.v("POSTURL", uri);
			httpclient = new DefaultHttpClient(httpParameters);

			getRequest = new HttpGet(uri);

			HttpResponse httpResponse = httpclient.execute(getRequest);

			response = EntityUtils.toString(httpResponse.getEntity());
			Log.d("TAG", "REQUEST: " + uri);
			Log.d("TAG", "RESPONSE: " + response);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
