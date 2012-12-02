package directbroking.client;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;

public class DBHttpClient 
{
  static public HttpClient client;
	static public HttpClient defaultInstance()
	{
		if (client == null)
		{
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
			HttpParams params = new BasicHttpParams();
			ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(params, schemeRegistry);
			client = new DefaultHttpClient(mgr, params);
		}
		return client;
	}

	static public HttpClient sslSessionClientInstance(Context appContext) 
	{
		try {
			SSLSessionCache sslSessionCache = new SSLSessionCache(appContext);
			SSLSocketFactory sslfactory  = SSLCertificateSocketFactory.getHttpSocketFactory(10*60*1000, sslSessionCache);

			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("https", sslfactory, 443));
			
			HttpParams params = new BasicHttpParams();
//	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
//	        HttpConnectionParams.setStaleCheckingEnabled(params, false);
//
//	        HttpConnectionParams.setConnectionTimeout(params, 4 * 1000);
//	        HttpConnectionParams.setSoTimeout(params, 5 * 1000);
//	        HttpConnectionParams.setSocketBufferSize(params, 8192);
//
//	        HttpClientParams.setRedirecting(params, false);			
			ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(params, schemeRegistry);
			return new DefaultHttpClient(mgr, params);
	    } catch (Exception e) {
	        return defaultInstance();
	    }
	}
	
}
