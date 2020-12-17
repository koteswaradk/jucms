package com.nxgenminds.eduminds.ju.cms.proxy;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientSingalTon {

	private static Object jucmsLock = new Object();
	private static CookieStore jucmsCookie = null;

	public static HttpClient getHttpClienttest() {
		final DefaultHttpClient httpClient = new DefaultHttpClient();
		synchronized (jucmsLock) {
			if (jucmsCookie == null) {
				jucmsCookie = httpClient.getCookieStore();
			} else {
				httpClient.setCookieStore(jucmsCookie);
			}
		}
		return httpClient;
	}

}
