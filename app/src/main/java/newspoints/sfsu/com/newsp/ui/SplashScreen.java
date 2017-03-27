package newspoints.sfsu.com.newsp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import newspoints.sfsu.com.newsp.R;


public class SplashScreen extends AppCompatActivity {

    TextView txtView_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        txtView_description = (TextView) findViewById(R.id.textView_description1);
        String description = "Welcome to Newspoints\n" +
                "Tag video and audio as you record, then export to the cloud and into your favorite desktop video editor!";
        txtView_description.setText(description);


        int SPLASH_TIME_OUT = 1000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app activity_video_capture activity
                Intent intent = new Intent(SplashScreen.this, IndexActivity.class);
                startActivity(intent);

            }
        }, SPLASH_TIME_OUT);
    }


    // required so when user will press back button, this screen will not show up.
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
