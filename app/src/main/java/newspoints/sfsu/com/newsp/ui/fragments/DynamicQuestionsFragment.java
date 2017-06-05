package newspoints.sfsu.com.newsp.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.util.ProjectConstants;
import newspoints.sfsu.com.newsp_lib.bus.BusProvider;
import newspoints.sfsu.com.newsp_lib.event.QuestionsEvent;

/**
 * Allows user to choose from many default or create new Questions that can be attached to the Video/NPAudio and finally can be
 * exported to the XML. When the user selects the appropriate {@link newspoints.sfsu.com.newsp_data.entities.Question}, it gets
 * attached to the video which can be further viewed before exporting.
 */
public class DynamicQuestionsFragment extends Fragment {

    private static final String TAG = "~!@#$DynQuesFrag";
    private static final String KEY_CALLER_CODE = "caller_code";
    @Bind(R.id.linearLayout_dynQues_questionsContainer)
    LinearLayout linearLayout_questionsContainer;
    @Bind(R.id.icon_dynQues_createNew)
    ImageView icon_addNewQuestion;
    @Bind(R.id.editText_dynQues_createNewQues)
    TextInputEditText et_newQues;
    @Bind(R.id.linearLayout_dynQues_newQues)
    LinearLayout linearLayout_newQuestionContainer;
    @Bind(R.id.toolbar_dynamicQuestions)
    Toolbar toolbar_main;
    private Context mContext;
    private List<String> questionsList = new ArrayList<>();
    private ArrayList userSelectedQuestions;
    private int callerCode;

    public DynamicQuestionsFragment() {
        // Required empty public constructor
    }

    public static DynamicQuestionsFragment newInstance(int code) {
        DynamicQuestionsFragment mDynamicQuestionsFragment = new DynamicQuestionsFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_CALLER_CODE, code);
        mDynamicQuestionsFragment.setArguments(args);
        return mDynamicQuestionsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            if (getArguments().getInt(KEY_CALLER_CODE) != 0 || getArguments().getInt(KEY_CALLER_CODE) != -1) {
                callerCode = getArguments().getInt(KEY_CALLER_CODE);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dynamic_questions, container, false);
        ButterKnife.bind(this, rootView);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar_main);

        questionsList.add("Contact Info");
        questionsList.add("How many arrests?");
        questionsList.add("What is being protested");
        questionsList.add("How did it happen");
        questionsList.add("Where are other people");
        questionsList.add("Who was responsible");
        questionsList.add("Why no body complained");

        populateQuestions();

        return rootView;

    }

    private void populateQuestions() {
        userSelectedQuestions = new ArrayList();

        // loop till the size of ArrayList and populate the Views
        for (int i = 0; i < questionsList.size(); i++) {
            LinearLayout mLinearLayoutQuestion = new LinearLayout(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mLinearLayoutQuestion.setOrientation(LinearLayout.HORIZONTAL);
            mLinearLayoutQuestion.setLayoutParams(layoutParams);
            mLinearLayoutQuestion.setPadding(24, 0, 24, 0);
            layoutParams.setMargins(0, 0, 0, 16);

            // define check box
            LinearLayout.LayoutParams checkBoxLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            final CheckBox mCheckBox = new CheckBox(mContext);
            mCheckBox.setLayoutParams(checkBoxLayoutParams);
            mCheckBox.setText(questionsList.get(i));

            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    userSelectedQuestions.add(mCheckBox.getText().toString());
                }
            });

            // finally add both to LinearLayout
            mLinearLayoutQuestion.addView(mCheckBox);

            // finally add it to FlowLayout
            linearLayout_questionsContainer.addView(mLinearLayoutQuestion);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_dynamic_tags, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                if (callerCode == ProjectConstants.AUDIO_CAPTURE_CODE) {
                    publishQuestionsToAudio();
                } else if (callerCode == ProjectConstants.VIDEO_RECORD_CODE) {
                    publishQuestionsToVideo();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Publishes user selected Questions for Video
     */
    private void publishQuestionsToVideo() {
        BusProvider.bus().post(new QuestionsEvent.OnVideoQuesLoaded(userSelectedQuestions));
    }

    /**
     * Publishes user selected Questions for NPAudio
     */
    private void publishQuestionsToAudio() {
        BusProvider.bus().post(new QuestionsEvent.OnAudioQuestionsLoaded(userSelectedQuestions));
    }
}
