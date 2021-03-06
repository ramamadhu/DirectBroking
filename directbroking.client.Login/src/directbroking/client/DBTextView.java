package directbroking.client;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DBTextView extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dbtextview);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            if (extras.getString("htmlString") != null)
            {
                TextView textView = (TextView) findViewById(R.id.textView1);
                CharSequence htmlStringRequest = extras.getCharSequence("htmlString");
                textView.setText(htmlStringRequest);
            }
        }
    }
}