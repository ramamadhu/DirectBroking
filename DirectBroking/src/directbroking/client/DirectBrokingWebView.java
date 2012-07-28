package directbroking.client;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class DirectBrokingWebView extends Activity
{
//    ArrayList<String> items=new ArrayList<String>();
    String htmlString;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dbwebview);
        WebView webview = (WebView)findViewById(R.id.dbWebView);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
             // Get endResult
//             String htmlString = extras.getString("htmlString");
//             webview.loadDataWithBaseURL(null, htmlString, "text/html", "utf-8", null);
//             webview.loadData(htmlString, "text/html", "utf-8");

            try
            {
                XmlPullParser xpp = getResources().getXml(R.xml.aboutus);
                while (xpp.getEventType()!=XmlPullParser.END_DOCUMENT)
                {
                    if (xpp.getEventType()==XmlPullParser.START_TAG)
                    {
                      if (xpp.getName().equals("About Us"))
                      {
//                        items.add(xpp.getAttributeValue(0));
                          htmlString = xpp.getAttributeValue(0);
//                          webview.loadData(htmlString, "text/html", "utf-8");
                          break;
                      }
                    }
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
