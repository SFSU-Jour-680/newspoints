package newspoints.sfsu.com.newsp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.adapters.QuestionsAdapter;
import newspoints.sfsu.com.newsp.ui.DynamicTagsActivity;
import newspoints.sfsu.com.newsp.ui.MediaCaptureActivity;
import newspoints.sfsu.com.newsp_lib.bus.BusProvider;

/**
 * Allows user to record Audio and attach Questions, sources and Shots with it. Also enables user to view all the attached
 * media
 */
public class AudioRecorderFragment extends Fragment {

    private static final String KEY_PROJECT_ID = "current_project_id";
    @Bind(R.id.drawerLayout_audioCapture)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.listView_audioCapture_questionsData)
    ListView mListViewQuestions;
    @Bind(R.id.icon_audioCapture_questions)
    ImageView iconQuestions;
    ActionBarDrawerToggle mDrawerToggle;
    @Bind(R.id.action_source_addNew)
    FloatingActionButton fab_addNew;
    @Bind(R.id.slidingLayout_main)
    SlidingUpPanelLayout slidingUpPanelLayout_main;
    @Bind(R.id.flowLayout_dragView_selectedSources_container)
    FlowLayout flowLayout_userSelectedSources;
    @Bind(R.id.linearLayout_dragView_selectedQuestions_container)
    LinearLayout linearLayout_userSelectedQuestions;
    @Bind(R.id.textView_audioCapture_questionsCount)
    TextView textView_questionsCount;
    private IAudioRecorderCallbacks mInterface;
    private Context mContext;
    private ImageView imageView_check;
    private int questionsCount;
    private ArrayList userSelectedSourcesList, userSelectedQuestionsList;
    private TextView txtView_question;

    public AudioRecorderFragment() {
        // Required empty public constructor
    }

    /**
     * Static method to create VideoCaptureFragment instance
     */
    public static AudioRecorderFragment newInstance(String projectId) {
        AudioRecorderFragment fragment = new AudioRecorderFragment();
        Bundle args = new Bundle();
        args.putString(KEY_PROJECT_ID, projectId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Static method to add Sources or Questions to Audio
     *
     * @param key
     * @param selectedTagsList
     * @return
     */
    public static AudioRecorderFragment newInstance(String key, ArrayList selectedTagsList) {
        AudioRecorderFragment fragment = new AudioRecorderFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(key, selectedTagsList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.bus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.bus().unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_audio_recorder_main, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // retrieve the selected Sources or Questions
        if (getArguments() != null) {
            // retrieve user selected Sources
            if (getArguments().getStringArrayList(MediaCaptureActivity.KEY_VIDEO_SOURCES_SELECTED) != null) {
                userSelectedSourcesList = getArguments().getStringArrayList(MediaCaptureActivity.KEY_VIDEO_SOURCES_SELECTED);
                if (userSelectedSourcesList != null)
                    displayUserSelectedSources();
            }

            // retrieve user selected Questions
            if (getArguments().getStringArrayList(MediaCaptureActivity.KEY_VIDEO_QUESTIONS_SELECTED) != null) {
                userSelectedQuestionsList = getArguments().getStringArrayList(MediaCaptureActivity.KEY_VIDEO_QUESTIONS_SELECTED);
                if (userSelectedQuestionsList != null)
                    displayUserSelectedQuestions();
            }
        }

        // initialize the views
        if (textView_questionsCount != null) {
            textView_questionsCount.setVisibility(View.INVISIBLE);
        }
        mDrawerToggle = new ActionBarDrawerToggle((getActivity()), mDrawerLayout, R.string.openDrawer, R.string.closeDrawer);

        List<String> questionsArray = new ArrayList<>();

        // TODO: these questions come from XML
        questionsArray.add("Contact Info");
        questionsArray.add("How many arrests?");
        questionsArray.add("What is being protested");
        questionsArray.add("How");
        questionsArray.add("Where");
        questionsArray.add("Who");
        questionsArray.add("Why");
        questionsArray.add("What");

        final QuestionsAdapter questionsAdapter = new QuestionsAdapter(mContext, R.layout.list_item_questions, questionsArray);
        mListViewQuestions.setAdapter(questionsAdapter);

        // define new footerView to enable user to add new Questions
        View footerView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.listview_questions_footer, null, false);
        mListViewQuestions.addFooterView(footerView);

        // add drawer listener
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (questionsCount > 0) {
                    Log.i("~!@#$", "less than zero");
                    textView_questionsCount.setVisibility(View.VISIBLE);
                    textView_questionsCount.setText("" + questionsCount);
                } else {
                    Log.i("~!@#$", "not more");
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        iconQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                    mDrawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });

        mListViewQuestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtView_question = (TextView) view.findViewById(R.id.textView_questionItem_questions);
                imageView_check = (ImageView) view.findViewById(R.id.icon_questionItem_tick);
                if (imageView_check.getVisibility() == View.INVISIBLE) {
                    imageView_check.setVisibility(View.VISIBLE);
                    txtView_question.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
                    questionsCount++;
                    Log.i("~!@#$", "onItemClick: " + questionsCount);
                } else {
                    imageView_check.setVisibility(View.INVISIBLE);
                    txtView_question.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary_80_per));
                }
            }
        });

        fab_addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dynamicTagIntent = new Intent(mContext, DynamicTagsActivity.class);
                dynamicTagIntent.putExtra(MediaCaptureActivity.KEY_ADD_NEW_VIDEO_SOURCE, 1);
                startActivity(dynamicTagIntent);
            }
        });

        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dynamicTagIntent = new Intent(mContext, DynamicTagsActivity.class);
                dynamicTagIntent.putExtra(MediaCaptureActivity.KEY_ADD_NEW_VIDEO_QUESTION, 1);
                startActivity(dynamicTagIntent);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IAudioRecorderCallbacks) {
            mInterface = (IAudioRecorderCallbacks) context;
            mContext = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IAudioRecorderCallbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInterface = null;
    }

    /**
     * Displays User selected Sources in the drag view
     */
    private void displayUserSelectedSources() {
        for (int i = 0; i < userSelectedSourcesList.size(); i++) {
            TextView txtView_source = new TextView(mContext);
            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
                    FlowLayout.LayoutParams.WRAP_CONTENT);
            txtView_source.setLayoutParams(layoutParams);
            txtView_source.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorSource));
            txtView_source.setTextColor(ContextCompat.getColor(mContext, R.color.textWhite));
            txtView_source.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            txtView_source.setPadding(16, 8, 16, 8);
            layoutParams.setMargins(8, 8, 8, 8);
            // set the text
            txtView_source.setText(userSelectedSourcesList.get(i).toString());

            // add to FlowLayout
            flowLayout_userSelectedSources.addView(txtView_source);
        }
    }

    /**
     * Displays User selected Sources in the drag view
     */
    private void displayUserSelectedQuestions() {
        for (int i = 0; i < userSelectedQuestionsList.size(); i++) {
            TextView txtView_question = new TextView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            // set the text
            txtView_question.setText(userSelectedQuestionsList.get(i).toString());
            txtView_question.setLayoutParams(layoutParams);
            txtView_question.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorQuestion));
            txtView_question.setTextColor(ContextCompat.getColor(mContext, R.color.textWhite));
            txtView_question.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            txtView_question.setPadding(16, 8, 16, 8);
            layoutParams.setMargins(8, 8, 8, 8);

            // add to FlowLayout
            linearLayout_userSelectedQuestions.addView(txtView_question);
        }
    }


    /**
     * Callback interface to handle the onClicks in {@link AudioRecorderFragment}
     */
    public interface IAudioRecorderCallbacks {
        /**
         * Callback method to attach user selected Sources to Audio
         */
        void attachSourcesToAudioDynamically();

        /**
         * Callback method to attach user selected Questions to Audio
         */
        void attachQuestionsToAudioDynamically();
    }
}
