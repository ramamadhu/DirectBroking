package directbroking.client;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Toast;

public class DirectBrokingWebView extends Activity
{
    private StockDataSource stocksSource;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dbwebview);
        WebView dbWebview = (WebView) findViewById(R.id.dbWebView);
        dbWebview.setBackgroundColor(getResources().getColor(R.color.grey));

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            if (extras.getString("userRequestItem") != null)
            {
                ProcessFaqRequests(extras);
            }
            else if (extras.getString("htmlString") != null)
            {
                ProcessHtmlRequests(extras);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dbwebviewmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.signout:
            	String url = "https://www.directbroking.co.nz/DirectTrade/dynamic/signoff.aspx";
            	HttpClient client = Login.dbHttpClientInstance();
            	HttpPost httppost = new HttpPost(url);
            	try
            	{
            		client.execute(httppost);
            	}
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            	finish();
                break;
        }
        return true;
    }

    private void ProcessFaqRequests(Bundle extras)
    {
        // Get endResult
        String htmlStringRequest;
        String userRequest = extras.getString("userRequestItem");
        WebView webview = (WebView)findViewById(R.id.dbWebView);

        try
        {
            XmlPullParser xpp = getResources().getXml(R.xml.faq);
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT)
            {
                if (xpp.getEventType()==XmlPullParser.START_TAG)
                {
                  if (xpp.getName().equals(userRequest))
                  {
                      xpp.next();
                      htmlStringRequest = xpp.getText();
                      webview.loadData(htmlStringRequest, "text/html", "utf-8");
                      break;
                  }
                }
                xpp.next();
            }
        }
        catch(Throwable t)
        {
            Toast
              .makeText(this, "Request failed: "+t.toString(), Toast.LENGTH_LONG)
              .show();
        }
    }

    static String stock;
    static String stockQuantity;
    static String costPrice;
    static String marketPrice;

    private void ProcessHtmlRequests(Bundle extras)
    {
        String htmlData = extras.getString("htmlString");
        WebView webview = (WebView)findViewById(R.id.dbWebView);
        webview.setBackgroundColor(Color.parseColor("#e6e6e6"));
        webview.loadDataWithBaseURL("https://www.directbroking.co.nz/DirectTrade/dynamic/", htmlData, "text/html", "utf-8", null);

        processPortfolio(htmlData);
    }

	/**
	 * @param htmlData
	 */
	private void processPortfolio(String htmlData) {
		Document document = Jsoup.parse(htmlData);
//        String table = document.select("tr[class=dgitTR]").outerHtml();
        Elements tableRows = document.select("table[id=PortfolioPositionsTable] tr:gt(0):lt(18)");

        String s[] = new String[tableRows.size()];
        for(Element row : tableRows)
        {
            s[0] = row.child(0).text();
            s[1] = row.child(1).text();
            s[2] = row.child(2).text();

            stock = s[0];
            System.out.printf("Stock is %s\n", stock);
            if (stock.contentEquals("Code"))
            {
                System.out.println("skipping title row");
                continue;
            }

            if (s[2] !=""){
                stockQuantity = s[2];
            }

            if (s[3] !=""){
            	costPrice = s[3];
            }

            if (s[5] !=""){
            	marketPrice = s[5];
            }
            
            // sql insert
           stocksSource = new StockDataSource(this);
           stocksSource.open();
           System.out.printf("create stock %s, quantity %s\n", stock, stockQuantity);
           Stock newStock = stocksSource.createStock(stock, stockQuantity, costPrice, marketPrice);
           System.out.printf("RM stock name: %s\n", newStock.getTicker());
           System.out.printf("RM stock quantity: %s\n", newStock.getQuantity());
        }
	}
}
