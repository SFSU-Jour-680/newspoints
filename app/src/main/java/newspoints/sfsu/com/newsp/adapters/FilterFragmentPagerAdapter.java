package newspoints.sfsu.com.newsp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import newspoints.sfsu.com.newsp.ui.FilesListActivity;
import newspoints.sfsu.com.newsp.ui.fragments.AllFilesFragment;
import newspoints.sfsu.com.newsp.ui.fragments.QuestionsFilesFragment;
import newspoints.sfsu.com.newsp.ui.fragments.ShotFilesFragment;
import newspoints.sfsu.com.newsp.ui.fragments.SourceFilesFragment;


public class FilterFragmentPagerAdapter extends FragmentPagerAdapter {

	Context context;

	public FilterFragmentPagerAdapter(FragmentManager fm, FilesListActivity context) {
		super(fm);
		this.context = context;
	}

	@Override
	public Fragment getItem(int pos) {
		Fragment fragment = null;

		switch (pos) {
		case 0:
			fragment = AllFilesFragment.newInstance("Hell", context);
			break;

		case 2:
			fragment = QuestionsFilesFragment.newInstance("Is", context);
			break;
		case 3:
			fragment = ShotFilesFragment.newInstance(("HAHAH"), context);
			break;
		case 1:
			fragment = SourceFilesFragment.newInstance("Here", context);
			break;

		}
		return fragment;
	}

	@Override
	public int getCount() {
		return 4;

	}
}
