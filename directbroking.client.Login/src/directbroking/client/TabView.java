package directbroking.client;

import android.app.TabActivity;
import android.os.Bundle;
import android.view.Menu;

public class TabView extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabview);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tabview, menu);
        return true;
    }
}
