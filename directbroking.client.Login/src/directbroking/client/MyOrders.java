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
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MyOrders extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            if (extras.getString("htmlString") != null)
            {
                String htmlData = extras.getString("htmlString");
                processOrders(htmlData);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dbwebviewmenu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.signout:
            {
            	String url = "https://www.directbroking.co.nz/DirectTrade/dynamic/signoff.aspx";
            	HttpClient client = DBHttpClient.defaultInstance(getApplicationContext());
            	HttpPost httppost = new HttpPost(url);
            	try
            	{
            		client.execute(httppost);
            	}
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                Intent login = new Intent(getApplicationContext(), Login.class);
                login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(login);

                break;
            }
            case R.id.MyPortfolio:
            {
            	try 
            	{
                    MyPortfolioActionTask myPortfolioTask = new MyPortfolioActionTask(this);
                    myPortfolioTask.execute();
            	}
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            	break;
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
      super.onResume();
    }

    @Override
    protected void onPause() {
      super.onPause();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    private void processOrders(String htmlData) {
    	Document document = Jsoup.parse(htmlData);
    	Element title = document.select("TITLE").first();
    	
        if( title != null && title.text().equals("My Orders | Direct Broking"))
        {
            Elements elements = document.select("span[id~=SystemMsg1_lblText]");
            for (Element element : elements)
            {
                displayNotification(element.html());
                break;
            }
        }
        else
        {
            String table = document.select("table[id=PortfolioPositionsTable]").outerHtml();
        }
    }
    private void displayNotification(CharSequence text)
    {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private class MyPortfolioActionTask extends AsyncTask<String,Void, String>
    {
        Context AppContext;

        private MyPortfolioActionTask(Context context)
        {
            AppContext = context.getApplicationContext();
        }

        @Override
        protected String doInBackground(String...inputs)
        {
            String responseBody = "";
        	String url = "https://www.directbroking.co.nz/DirectTrade/secure/portfolios.aspx";
        	HttpClient client = DBHttpClient.defaultInstance(getApplicationContext());
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
            	finish();
                Intent myPortfolio = new Intent(AppContext, MyPortfolio.class);
                myPortfolio.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myPortfolio.putExtra("htmlString", result);
                AppContext.startActivity(myPortfolio);
            }
            else
            {
            	finish();
                Intent login = new Intent(getApplicationContext(), Login.class);
                login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(login);
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
    }
    
}
