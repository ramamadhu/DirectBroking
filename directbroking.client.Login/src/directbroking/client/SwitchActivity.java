package directbroking.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

public class SwitchActivity extends AsyncTask<String,Void, String>
{
	Context AppContext;
	String url;
	Class<?> activityClass;
	Activity appActivity;

	public SwitchActivity(Activity activity, String urlString, Class<?> cls)
	{
		AppContext = activity.getApplicationContext();
		url = urlString;
		activityClass = cls;
		appActivity = activity;
	}

	@Override
	protected String doInBackground(String...inputs)
	{
		String responseBody = "";
		HttpClient client = DBHttpClient.defaultInstance(AppContext);
		HttpPost httppost = new HttpPost(url);
		try
		{
			HttpResponse response = client.execute(httppost);
			responseBody = EntityUtils.toString(response.getEntity());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return responseBody;
	}

	@Override
	protected void onPreExecute()
	{
	}

	@Override
	protected void onPostExecute(String result)
	{
		if(onLoginSuccess(result))
		{
			appActivity.finish();
			Intent userActivity = new Intent(AppContext, activityClass);
			userActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			userActivity.putExtra("htmlString", result);
			AppContext.startActivity(userActivity);
		}
		else
		{
			appActivity.finish();
			Intent login = new Intent(AppContext, Login.class);
			login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			AppContext.startActivity(login);
		}
	}

	private boolean onLoginSuccess(String htmlString)
	{
		boolean result= true;
		Document document = Jsoup.parse(htmlString);
		Element title = document.select("TITLE").first();

		if( title != null && title.text().equals("Login | Direct Broking"))
		{
			Elements elements = document.select("span[id~=SystemMsg1_lblText]");
			for (Element element : elements)
			{
				displayNotification(element.html());
				break;
			}

			result = false;
		}
		else if (title == null)
		{
			result = false;
		}

		return result;
	}

	private void displayNotification(CharSequence text)
	{
		int duration = Toast.LENGTH_LONG;

		Toast toast = Toast.makeText(AppContext, text, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

}
