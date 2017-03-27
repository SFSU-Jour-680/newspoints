package newspoints.sfsu.com.newsp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import newspoints.sfsu.com.newsp.R;

public class DemoActivity extends MainBaseActivity {


    public static final String KEY_OPEN_VIDEO = "open_video";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container_main, new PlaceholderFragment2()).commit();
        }

        EventBus.getDefault().register(this);

        findViewById(R.id.button_openVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DemoActivity.this, MediaCaptureActivity.class);
                intent.putExtra(KEY_OPEN_VIDEO, 1);
                startActivity(intent);
                finish();
            }
        });
    }

    @Subscribe
    public void getMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void getSources(Sources sources) {
        Log.i("~!@#$", "Activity: " + sources.type + " : " + sources.name);
        getSupportFragmentManager().beginTransaction().add(R.id.container_main, new PlaceholderFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            TestData t = new TestData();
            t.message = "Hello from the activity";
            EventBus.getDefault().post(t);
            return true;
        } else if (id == R.id.action_camera) {
            Intent intent = new Intent(DemoActivity.this, MediaCaptureActivity.class);
            intent.putExtra(KEY_OPEN_VIDEO, 1);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public String produceEvent() {
        return "Starting up";
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public static class PlaceholderFragment extends Fragment {

        TextView txtview_Data;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);
            View button = rootView.findViewById(R.id.fragmentbutton);
            View button2 = rootView.findViewById(R.id.button_openPlace2);
            txtview_Data = (TextView) rootView.findViewById(R.id.txtView_data);
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post("Hello from the Fragment");
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            EventBus.getDefault().register(this);
            return rootView;
        }

        @Subscribe
        public void getMessage(DemoActivity.TestData data) {
            Toast.makeText(getActivity(), data.message, Toast.LENGTH_LONG).show();
            txtview_Data.setText(data.message);
        }

        @Subscribe
        public void getSources(Sources sources) {
            Log.i("~!@#$", "Fragment: " + sources.type + " : " + sources.name);

        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public static class PlaceholderFragment2 extends Fragment {

        public PlaceholderFragment2() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_placeholder2, container, false);
            View button = rootView.findViewById(R.id.fragmentbutton2);
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new Sources(24, "Friend"));
                }
            });
            EventBus.getDefault().register(this);
            return rootView;
        }

        @Subscribe
        public void getMessage(DemoActivity.TestData data) {
            Toast.makeText(getActivity(), data.message, Toast.LENGTH_LONG).show();
        }
    }

    public static class Sources {
        public int type;
        public String name;

        public Sources(int type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    public class TestData {
        public String message;
    }


}