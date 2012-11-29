package directbroking.client;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MyOrders extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_my_orders, menu);
        return true;
    }
}
