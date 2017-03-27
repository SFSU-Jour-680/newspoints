package newspoints.sfsu.com.newsp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.adapters.FilterFragmentPagerAdapter;
import newspoints.sfsu.com.newsp.util.ProjectConstants;


public class FilesListActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    Context context;
    Intent addProjectFiles;
    FilterFragmentPagerAdapter fragmentPagerAdapter;
    ViewPager viewPager;
    private TabHost tHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files_list);
        addTabs();
        tHost.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String arg0) {
                viewPager.setCurrentItem(tHost.getCurrentTab());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(ProjectConstants.selectedProjectName);
        context = this;
        viewPager = (ViewPager) findViewById(R.id.pager);
        fragmentPagerAdapter = new FilterFragmentPagerAdapter(getSupportFragmentManager(), FilesListActivity.this);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOnPageChangeListener(this);

        tHost.setCurrentTab(0);
    }

    private void addTabs() {
        tHost = (TabHost) findViewById(android.R.id.tabhost);
        tHost.setup();
        TabHost.TabSpec all = tHost.newTabSpec("All").setIndicator("ALL");
        all.setContent(new TabFactory(this));
        tHost.addTab(all);
        TabHost.TabSpec source = tHost.newTabSpec("Source").setIndicator("Source");
        source.setContent(new TabFactory(this));
        tHost.addTab(source);
        TabHost.TabSpec question = tHost.newTabSpec("Question").setIndicator("Question");
        question.setContent(new TabFactory(this));
        tHost.addTab(question);
        TabHost.TabSpec shot = tHost.newTabSpec("Shot").setIndicator("Shot");
        shot.setContent(new TabFactory(this));
        tHost.addTab(shot);

        tHost.getTabWidget().setDividerDrawable(null);

        for (int j = 0; j < 4; j++) {
            tHost.getTabWidget().getChildAt(j).setBackgroundResource(R.drawable.tab_text_selector);
            TextView tabText = (TextView) tHost.getTabWidget().getChildAt(j).findViewById(android.R.id.title);
            tabText.setTextColor(getResources().getColor(android.R.color.white));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent indexIntent = new Intent(FilesListActivity.this, IndexActivity.class);
        indexIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        indexIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        indexIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(indexIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.add_video:
                addProjectFiles = new Intent(FilesListActivity.this,
                        VideoCaptureActivity.class);

                startActivity(addProjectFiles);
                finish();
                break;
            case R.id.add_audio:
                addProjectFiles = new Intent(FilesListActivity.this,
                        AudioRecorderActivity.class);
                addProjectFiles.putExtra("foldername",
                        ProjectConstants.selectedProjectName);
                startActivity(addProjectFiles);
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int arg0) {
        tHost.setCurrentTab(arg0);

    }

    @Override
    public void onClick(View v) {
    }

    class TabFactory implements TabContentFactory {

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