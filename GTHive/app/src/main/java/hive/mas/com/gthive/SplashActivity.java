package hive.mas.com.gthive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Creates the opening splash screen for the application
 *
 */
public class SplashActivity extends AppCompatActivity {
    /**
     * Run a splash screen when the app is opened
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, BuildingListActivity.class);
        startActivity(intent);
        finish();
    }
}
