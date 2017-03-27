package newspoints.sfsu.com.newsp.ui.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.adapters.ProjectListAdapter;
import newspoints.sfsu.com.newsp.listeners.ItemClickSupport;
import newspoints.sfsu.com.newsp.ui.MyRecyclerItemClickListener;
import newspoints.sfsu.com.newsp.ui.ProjectActivity;
import newspoints.sfsu.com.newsp_data.entities.Project;
import newspoints.sfsu.com.newsp_data.manager.DatabaseManager;

/**
 * Displays list of {@link Project} in a RecyclerView using CardView.
 */
public class ProjectListFragment extends Fragment
        implements View.OnClickListener,
        SearchView.OnQueryTextListener {

    private static final String TAG = "~!@#$ProjListFrag";
    private static final String KEY_PROJECT_TEMP = "project_details";
    @Bind(R.id.recyclerView_projList_main)
    RecyclerView recyclerView_project;
    @Bind(R.id.textViewStatic_projList_listInfo)
    TextView txtView_statusInfo;
    @Bind(R.id.fab_projList_addProj)
    FloatingActionButton fab_addProject;
    @Bind(R.id.relativeLayout_projList_main)
    RelativeLayout mRelativeLayout_projectList;
    private IProjectListCallbacks mInterface;
    private Context mContext;
    private RecyclerView mProjectRecyclerView;
    private List<Project> localProjectList, mAllProjectList;
    private LinearLayoutManager mLinearLayoutManager;
    private DatabaseManager mDatabaseManager;
    private ProjectListAdapter mProjectListAdapter;
    private Project demoProject;

    public ProjectListFragment() {
        // Required empty public constructor
    }

    public static ProjectListFragment newInstance(Project mProject) {
        ProjectListFragment fragment = new ProjectListFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_PROJECT_TEMP, mProject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabaseManager = new DatabaseManager(mContext);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_project_list, container, false);

        ButterKnife.bind(this, rootView);

        manageToolbar();
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        fab_addProject.setOnClickListener(this);

        if (getArguments() != null) {
            if (getArguments().getParcelable(KEY_PROJECT_TEMP) != null) {
                demoProject = getArguments().getParcelable(KEY_PROJECT_TEMP);
            }
        }
        localProjectList = new ArrayList<>();
        localProjectList.add(demoProject);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IProjectListCallbacks) {
            mInterface = (IProjectListCallbacks) context;
            mContext = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mAllProjectList = localProjectList;

        if (mAllProjectList.size() > 0 && mAllProjectList != null) {
            displayProjectList();
        } else if (mAllProjectList.size() == 0) {
            txtView_statusInfo.setVisibility(View.VISIBLE);
            recyclerView_project.setVisibility(View.GONE);
            mRelativeLayout_projectList.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorLight));
        } else {
            Log.i(TAG, "onResume: projectList not OK");
        }
    }

    private void manageToolbar() {
        final Toolbar toolbar = ((ProjectActivity) getActivity()).mainProjectActivityToolbar;
        toolbar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        ProjectActivity mProjectActivity = (ProjectActivity) getActivity();

        mProjectActivity.setTitle(R.string.title_fragment_project_list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
            mProjectActivity.getWindow().setStatusBarColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        }
        mProjectActivity.setSupportActionBar(toolbar);
        mProjectActivity.getSupportActionBar().show();
    }

    /**
     * Helper method to handle the View rendering of RecyclerView and Item.
     */
    private void displayProjectList() {

        int fabMargin = getResources().getDimensionPixelSize(R.dimen.fab_margin);

        recyclerView_project.setHasFixedSize(true);

        if (mContext != null) {
            mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView_project.setLayoutManager(mLinearLayoutManager);
        } else {
            Log.d(TAG, " No layout manager supplied");
        }

        mProjectListAdapter = new ProjectListAdapter(mAllProjectList, mContext);
        mProjectListAdapter.notifyDataSetChanged();

        // another method to setup onClick support to RecyclerView

        ItemClickSupport.addTo(recyclerView_project).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // do it
            }
        });

        if (recyclerView_project != null) {
            recyclerView_project.setAdapter(mProjectListAdapter);

            // custom onClick listener on recyclerView
            recyclerView_project.addOnItemTouchListener(new MyRecyclerItemClickListener(mContext, recyclerView_project,
                    new MyRecyclerItemClickListener.IRecyclerItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            mInterface.onProjectListItemClickListener(mAllProjectList.get(position));
                        }

                        @Override
                        public void onItemLongClick(View view, int position) {

                        }
                    }));
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mInterface = null;
    }

    @Override
    public void onClick(View v) {}

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<Project> filteredActivitiesList = filter(mAllProjectList, query);
        mProjectListAdapter.animateTo(filteredActivitiesList);
        recyclerView_project.scrollToPosition(0);
        return true;
    }

    private List<Project> filter(List<Project> mAllProjectList, String query) {
        query = query.toLowerCase();
        final List<Project> filteredActivitiesList = new ArrayList<>();

        if (mAllProjectList != null && mAllProjectList.size() > 0) {
            for (Project project : mAllProjectList) {
                // perform the search on Activity name
                final String text = project.getName().toLowerCase();
                if (text.contains(query)) {
                    filteredActivitiesList.add(project);
                }
            }
        }
        return filteredActivitiesList;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_project_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_upload:
                filterProjects();
                return true;
            case R.id.action_search:
                SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                if (searchView != null) {
                    searchView.setOnQueryTextListener(this);
                }
                break;
            case android.R.id.home:
                Log.i(TAG, "navigation drawer clicked");
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void filterProjects() {

    }

    /**
     * Callback interface to handle onclick events in the {@link ProjectListFragment}
     */
    public interface IProjectListCallbacks {
        /**
         * Handler to open {@link ProjectDetailsFragment} on item click od the list.
         *
         * @param mProject
         */
        void onProjectListItemClickListener(Project mProject);
    }
}
