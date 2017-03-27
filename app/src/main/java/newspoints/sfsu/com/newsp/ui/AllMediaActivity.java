package newspoints.sfsu.com.newsp.ui;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.ui.fragments.FourFragment;
import newspoints.sfsu.com.newsp.ui.fragments.OneFragment;
import newspoints.sfsu.com.newsp.ui.fragments.ThreeFragment;
import newspoints.sfsu.com.newsp.ui.fragments.TwoFragment;

/**
 * Container Activity for all the media related Fragments such as {@link newspoints.sfsu.com.newsp.ui.fragments.AllFilesFragment},
 */
public class AllMediaActivity extends MainBaseActivity {

    public static final String KEY_ADD_NEW_SOURCE = "add_new_source_tag";
    public static final String KEY_ADD_NEW_QUESTION = "add_new_question_tag";
    private static final String TAG = "~!@#$MediaAct";
    public FragmentManager mFragmentManager;
    @Bind(R.id.toolbar_top_base)
    Toolbar toolbar_allMedia;
    @Bind(R.id.viewpager_allMedia)
    ViewPager viewPager;
    @Bind(R.id.tabs_allMedia)
    TabLayout tabLayout;
    private int[] tabIcons = {
            R.mipmap.ic_perm_media_white_24dp,
            R.mipmap.ic_shot_white_24dp,
            R.mipmap.ic_source_white_24dp,
            R.mipmap.ic_question_answer_white_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        ButterKnife.bind(this);

        try {
            mFragmentManager = getSupportFragmentManager();

            // set elevatio to 0dp
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar_allMedia.setElevation(0);
            }

            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.colorSecondaryTemp),
                    ContextCompat.getColor(this, R.color.colorWhite));
            // setting up Tab icons
            setupTabIcons();


            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    tab.getIcon().setColorFilter(ContextCompat.getColor(AllMediaActivity.this, R.color.colorWhite),
                            PorterDuff.Mode.SRC_IN);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    tab.getIcon().setColorFilter(ContextCompat.getColor(AllMediaActivity.this, R.color.colorSecondaryTemp),
                            PorterDuff.Mode.SRC_IN);
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        } catch (IllegalArgumentException iae) {
            Log.i(TAG, "onCreate: illegal - " + iae.getMessage());
        } catch (NullPointerException ne) {
            Log.i(TAG, "onCreate: npe -" + ne.getMessage());
        } catch (Exception e) {
            Log.i(TAG, "onCreate: " + e.getMessage());
        }
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(mFragmentManager);
        adapter.addFragment(new OneFragment(), "All");
        adapter.addFragment(new TwoFragment(), "SHOTS");
        adapter.addFragment(new ThreeFragment(), "SOURCES");
        adapter.addFragment(new FourFragment(), "QUESTIONS");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
