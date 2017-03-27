package newspoints.sfsu.com.newsp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import newspoints.sfsu.com.newsp.ui.IndexActivity;
import newspoints.sfsu.com.newsp.ui.fragments.ContentMainFragment;
import newspoints.sfsu.com.newsp.ui.fragments.NewsMapViewFragment;


public class ViewPagerAdapter extends FragmentPagerAdapter {

    Context context;

    public ViewPagerAdapter(FragmentManager fm, IndexActivity context) {
        super(fm);
        this.context = context;

    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            ContentMainFragment tab1 = new ContentMainFragment();
            return tab1;
        } else {
            NewsMapViewFragment tab2 = new NewsMapViewFragment();
            return tab2;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

}
