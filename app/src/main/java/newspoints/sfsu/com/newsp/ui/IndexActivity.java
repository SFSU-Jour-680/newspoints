package newspoints.sfsu.com.newsp.ui;


import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.adapters.ViewPagerAdapter;
import newspoints.sfsu.com.newsp.ui.fragments.ContentMainFragment;
import newspoints.sfsu.com.newsp.util.ProjectConstants;
import newspoints.sfsu.com.newsp_data.dao.RecordingDetailsDB;

public class IndexActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, ContentMainFragment.IProjectSelectedListener, ViewPager.OnPageChangeListener, LocationListener {

    public static Context context;
    public static List<String> projectList, projectImage, projectDate;
    // sliding tabs Variables
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    BufferedReader reader;
    XmlPullParserFactory xmlFactoryObject;
    XmlPullParser myParser;
    Set<String> shot, source, question, category;
    SharedPreferences prefs;
    Editor editor;
    SearchView searchView;
    private ContentMainFragment mContentMainFragment;
    private TabHost tHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setTitle(R.string.app_name);

        context = getApplicationContext();
        // must be final to avoid NPE
        final Typeface ROBOTO_MEDIUM = Typeface.createFromAsset(this.getAssets(), "Roboto-Medium.ttf");
        final Typeface ROBOT_REGULAR = Typeface.createFromAsset(this.getAssets(), "Roboto-Regular.ttf");

        // decoupling all the code in order to speed up the activity
        ProjectConstants.ROBOT_MEDIUM = ROBOTO_MEDIUM;
        ProjectConstants.ROBOT_REGULAR = ROBOT_REGULAR;

        if (prefs == null) {
            prefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
            editor = prefs.edit();
        }

        ProjectConstants.dbClass = new RecordingDetailsDB(context);

        // will add tabs to the IndexActivity
        addTabs();

        try {
            tHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String arg0) {
                    viewPager.setCurrentItem(tHost.getCurrentTab());
                }
            });

        } catch (Exception e) {
            Log.d("=====>", e.getMessage());
        }

        // initialize the ViewPager and ViewPagerAdapter
        initializeActivityControls();

        mContentMainFragment = (ContentMainFragment) adapter.getItem(0);
        if (mContentMainFragment == null)  // could be null if not instantiated yet
        {
            mContentMainFragment = new ContentMainFragment();
        }

        shot = new HashSet<String>();
        source = new HashSet<String>();
        question = new HashSet<String>();
        category = new HashSet<String>();
        ParseXMLData();

        //check location
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(IndexActivity.this);
            dialog.setMessage("Location service is disabled. Please turn on location");
            dialog.setPositiveButton(IndexActivity.this.getResources().getString(R.string.enable_gps), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton(IndexActivity.this.getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                }
            });
            dialog.show();
        }
    }


    private void initializeActivityControls() {
        // initialize the Adapter and the ViewPager to generate the Views
        setTitle(ProjectConstants.selectedProjectName);
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
        tHost.setCurrentTab(0);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // initializeActivity method will initialize the ViewPager Adapters and set Tabs.
        initializeActivityControls();
        context = getApplicationContext();

        //check location
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(IndexActivity.this);
            dialog.setMessage("Location service is disabled. Please enable location.");
            dialog.setPositiveButton(IndexActivity.this.getResources().getString(R.string.enable_gps), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton(IndexActivity.this.getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                }
            });
            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.activity_main, menu);
        MenuItem searchItem = menu.findItem(R.id.search_index);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);


        // When using the support library, the setOnActionExpandListener() method is
        // static and accepts the MenuItem object as an argument
        // Must must implement
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    // All the methods for handling SearchView events
    private boolean isAlwaysExpanded() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    public boolean onQueryTextChange(String newText) {

        // get the ContentMainFragment from the ViewPager
        return mContentMainFragment.parseQuery(newText);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }

    // helper methods for the Activity
    private void setupSearchView(MenuItem searchItem) {

        if (isAlwaysExpanded()) {
            searchView.setIconifiedByDefault(false);
        } else {
            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        }

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();

            ComponentName cn = new ComponentName(this, IndexActivity.class);
            SearchableInfo info = searchManager.getSearchableInfo(cn);
            for (SearchableInfo inf : searchables) {
                if (inf.getSuggestAuthority() != null && inf.getSuggestAuthority().startsWith("applications")) {
                    info = inf;
                }
            }
            searchView.setSearchableInfo(info);
        }
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tHost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * This method will be called to initialize tabs and add tabs to ViewPagerAdapter
     */
    private void addTabs() {
        tHost = (TabHost) findViewById(android.R.id.tabhost);
        tHost.setup();
        TabHost.TabSpec list = tHost.newTabSpec("List").setIndicator("List");
        list.setContent(new TabFactory(this));
        tHost.addTab(list);

        // Map Tab
        TabHost.TabSpec map = tHost.newTabSpec("Map").setIndicator("Map");
        map.setContent(new TabFactory(this));
        tHost.addTab(map);

        // set Divider
        tHost.getTabWidget().setDividerDrawable(null);

        for (int j = 0; j < 2; j++) {
            tHost.getTabWidget().getChildAt(j).setBackgroundResource(R.drawable.tab_text_selector);
            TextView tabText = (TextView) tHost.getTabWidget().getChildAt(j)
                    .findViewById(android.R.id.title);
            tabText.setTextColor(getResources().getColor(android.R.color.white));
        }
    }

    /**
     * Method is used to parse the XML file selected by the User
     */
    public void ParseXMLData() {
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            myParser = xmlFactoryObject.newPullParser();
            myParser.setInput(reader);

        } catch (XmlPullParserException e) {
            Log.d("=====>", e.getMessage());
        }
        String tempCategoryName = null;

        int event;
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (name.equalsIgnoreCase("category")) {
                            String id = null;
                            String store = null;
                            String attName1 = myParser.getAttributeName(0);
                            String attName2 = myParser.getAttributeName(1);
                            if (attName1.equalsIgnoreCase("id")) {
                                id = myParser.getAttributeValue(0);
                            }
                            if (attName2.equalsIgnoreCase("name")) {
                                store = myParser.getAttributeValue(1);
                            }
                            editor.putString(store, id);
                            editor.commit();
                            category.add(store);
                            tempCategoryName = myParser.getAttributeValue(1);
                        } else if (name.equalsIgnoreCase("shot")) {
                            String id = null;
                            String store = null;
                            String attName1 = myParser.getAttributeName(0);
                            String attName2 = myParser.getAttributeName(1);
                            String attName3 = myParser.getAttributeName(2);
                            if (attName3.equalsIgnoreCase("label")) {

                                store = myParser.getAttributeValue(2);

                            } else if (attName2.equalsIgnoreCase("label")) {
                                store = myParser.getAttributeValue(1);
                            }

                            id = myParser.getAttributeValue(0);
                            shot.add(store);

                            editor.putString(store, id);
                            editor.commit();
                        } else if (name.equalsIgnoreCase("source")) {
                            String store = null;
                            String id = null;
                            String attName1 = myParser.getAttributeName(0);
                            String attName2 = myParser.getAttributeName(1);
                            if (attName1.equalsIgnoreCase("label")) {

                                store = myParser.getAttributeValue(2);

                            } else if (attName2.equalsIgnoreCase("label")) {

                                store = myParser.getAttributeValue(1).toString();

                            }
                            id = myParser.getAttributeValue(0).toString();

                            source.add(store);

                            editor.putString(store, id);
                            editor.commit();
                        } else if (name.equalsIgnoreCase("question")) {
                            String store = null;
                            String id = null;
                            String attName1 = myParser.getAttributeName(0);
                            String attName2 = myParser.getAttributeName(1);
                            if (attName1.equalsIgnoreCase("label")) {

                                store = myParser.getAttributeValue(0).toString();
                                id = myParser.getAttributeValue(1).toString();

                            } else if (attName2.equalsIgnoreCase("label")) {

                                store = myParser.getAttributeValue(1).toString();
                                id = myParser.getAttributeValue(0).toString();

                            }
                            // store = myParser.getAttributeValue(1).toString();
                            question.add(store);
                            editor.putString(store, id);
                            editor.commit();
                        }
                        // }
                        break;

                    case XmlPullParser.TEXT:

                        break;
                    case XmlPullParser.END_TAG:

                        if (name.equalsIgnoreCase("category")) {

                            editor.putStringSet(tempCategoryName
                                    + ProjectConstants.SOURCE, source);
                            editor.commit();

                            editor.putStringSet(tempCategoryName
                                    + ProjectConstants.SHOT, shot);
                            editor.commit();

                            editor.putStringSet(tempCategoryName
                                    + ProjectConstants.QUESTION, question);
                            editor.commit();

                            editor.putStringSet(ProjectConstants.CATEGORY, category);
                            editor.commit();

                            shot = null;
                            question = null;
                            source = null;
                            shot = new HashSet<String>();
                            source = new HashSet<String>();
                            question = new HashSet<String>();
                        }
                        break;
                }
                event = myParser.next();
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * this method initializes the List of data to be inflated in ListView
     * All the details are added to a List of List and returned
     */
    public List<List<String>> prepareListData() {

        List<List<String>> listProjectDetails = new ArrayList<>();

        projectList = new ArrayList<String>();
        projectImage = new ArrayList<String>();
        projectDate = new ArrayList<String>();

        ProjectConstants.dbClass.getProjectDetails();

        // add all the lists to the ListProjectDetails
        listProjectDetails.add(projectList);
        listProjectDetails.add(projectImage);
        listProjectDetails.add(projectDate);

        return listProjectDetails;
    }


    public ArrayList<String> handleGeoLocations(ArrayList<String> currentLocality) {
        ArrayList<String> myCurrentLocality = currentLocality;
        return myCurrentLocality;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onItemSelected(int position) {

    }

    // TabFactory helper class for creating the content of tabs
    class TabFactory implements TabHost.TabContentFactory {

        private final Context mContext;

        public TabFactory(Context context) {
            mContext = context;
        }

        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(50);
            v.setMinimumHeight(50);
            v.setBackgroundResource(R.drawable.tab_text_selector);

            return v;
        }

    }
}
