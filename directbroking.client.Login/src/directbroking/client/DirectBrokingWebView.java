package directbroking.client;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Toast;

public class DirectBrokingWebView extends Activity
{
    String htmlString;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dbwebview);
        WebView webview = (WebView)findViewById(R.id.dbWebView);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
             // Get endResult
        	String userRequest = extras.getString("userRequestItem");

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
                          htmlString = xpp.getText();
                          webview.loadData(htmlString, "text/html", "utf-8");
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
    }
}
