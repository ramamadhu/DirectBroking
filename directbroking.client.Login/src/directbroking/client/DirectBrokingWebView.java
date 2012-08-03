package directbroking.client;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Toast;

public class DirectBrokingWebView extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

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

    private void ProcessFaqRequests(Bundle extras)
    {
        // Get endResult
        String htmlStringRequest;
        String userRequest = extras.getString("userRequestItem");
        setContentView(R.layout.dbwebview);
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

    private void ProcessHtmlRequests(Bundle extras)
    {
        String htmlStringRequest = extras.getString("htmlString");
        setContentView(R.layout.dbwebview);
        WebView webview = (WebView)findViewById(R.id.dbWebView);
//        webview.loadData(htmlStringRequest, "text/html", "utf-8");
        webview.loadDataWithBaseURL(null, htmlStringRequest, "text/html", "utf-8", null);
    }
}
