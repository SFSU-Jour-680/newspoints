package newspoints.sfsu.com.newsp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.apmem.tools.layouts.FlowLayout;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.util.ProjectConstants;
import newspoints.sfsu.com.newsp_lib.event.SourcesEvent;

/**
 * Allows user to choose from many default or create new Source Tags that can be attached to the Video/Audio and finally can be
 * exported to the XML
 */
public class DynamicSourcesFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "~!@#$DynSrcFrag";
    private static final String KEY_CALLER_CODE = "caller_code";
    @Bind(R.id.icon_dynSource_createNew)
    ImageView icon_addNew;
    @Bind(R.id.linearLayout_dynSource_newSource)
    LinearLayout mLinearLayout_newTags;
    @Bind(R.id.flowLayout_dynSource_allSources)
    FlowLayout flowLayout_allSources;
    @Bind(R.id.editText_dynSource_createNewSource)
    TextInputEditText et_newTagValue;
    @Bind(R.id.toolbar_dynamicSources)
    Toolbar toolbar_main;
    private int _x;
    private int _y;
    private Context mContext;
    private ArrayList<String> selectedSourcesList;
    private List<String> sourceTags = new ArrayList<>();
    private int callerCode;


    public DynamicSourcesFragment() {
        // Required empty public constructor
    }

    public static DynamicSourcesFragment newInstance(int code) {
        DynamicSourcesFragment mDynamicSourcesFragment = new DynamicSourcesFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_CALLER_CODE, code);
        mDynamicSourcesFragment.setArguments(args);
        return mDynamicSourcesFragment;
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
        View rootView = inflater.inflate(R.layout.fragment_dynamic_sources, container, false);
        ButterKnife.bind(this, rootView);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar_main);

        Log.i(TAG, "onCreateView: ");

        icon_addNew.setOnClickListener(this);

        sourceTags.add("brother");
        sourceTags.add("Sister");
        sourceTags.add("Friend");
        sourceTags.add("First_disaster");
        sourceTags.add("head_coach");
        sourceTags.add("management");
        sourceTags.add("neighbor");

        populateSourceTags();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_dynSource_createNew:
                addNewSourceTag();
                break;
        }
    }

    /**
     * Helper method to populate all the Source Tags
     */
    private void populateSourceTags() {
        selectedSourcesList = new ArrayList<>();
        for (int i = 0; i < sourceTags.size(); i++) {
            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
                    FlowLayout.LayoutParams.WRAP_CONTENT);
            final TagTextView txtViewTemp = new TagTextView.TextViewBuilder(mContext, layoutParams)
                    .addText(sourceTags.get(i))
                    .backgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.view_roundborder_background_sources))
                    .textColor(ContextCompat.getColor(mContext, R.color.textSecondary))
                    .textSize(TypedValue.COMPLEX_UNIT_SP, 18)
                    .padding(_x, _y, _x, _y)
                    .build();
            layoutParams.setMargins(24, 24, 24, 24);

            // set onclick listener to change the view state of the selected Tag TextView.
            txtViewTemp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtViewTemp.setBackground(ContextCompat.getDrawable(mContext,
                            R.drawable.view_roundborder_background_sources_filled));
                    txtViewTemp.setTextColor(ContextCompat.getColor(mContext, R.color.textWhite));
                    selectedSourcesList.add(txtViewTemp.getText().toString());
                }
            });
            flowLayout_allSources.addView(txtViewTemp);
        }
    }

//    /**
//     * Factory method to create new TextView for Sources tags
//     *
//     * @return
//     */
//    private TextView createSourceTagTextView() {
//        TextView txtView_newTag = new TextView(mContext);
//        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
//                FlowLayout.LayoutParams.WRAP_CONTENT);
//        txtView_newTag.setLayoutParams(layoutParams);
//        txtView_newTag.setBackground(ContextCompat.getDrawable(mContext, R.drawable.view_roundborder_background_sources));
//        txtView_newTag.setTextColor(ContextCompat.getColor(mContext, R.color.textSecondary));
//        txtView_newTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//        txtView_newTag.setPadding(12, 6, 12, 6);
//        layoutParams.setMargins(24, 24, 24, 24);
//
//        return txtView_newTag;
//    }

    /**
     * Gets the input text value from the EditText and creates new Tag
     */
    private void addNewSourceTag() {
        String newSourceTag = et_newTagValue.getText().toString();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        TagTextView txtView_newTag = new TagTextView.TextViewBuilder(mContext, layoutParams)
                .addText(newSourceTag)
                .backgroundDrawable(ContextCompat.getDrawable(mContext,
                        R.drawable.view_roundborder_background_sources_filled))
                .textColor(ContextCompat.getColor(mContext, R.color.textWhite))
                .textSize(TypedValue.COMPLEX_UNIT_SP, 18)
                .padding(12, 6, 12, 6)
                .build();

        layoutParams.setMargins(24, 24, 24, 24);

        // finally add TextView to LinearLayout
        mLinearLayout_newTags.addView(txtView_newTag);

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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _x = (int) getResources().getDimension(R.dimen.view_dimen_small);
        _y = (int) getResources().getDimension(R.dimen.view_dimen_X_small);
        Log.i(TAG, "onActivityCreated: " + _x + " : " + _y);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dynamic_tags, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                if (callerCode == ProjectConstants.AUDIO_CAPTURE_CODE)
                    publishSelectedSourcesToAudio();
                else if (callerCode == ProjectConstants.VIDEO_RECORD_CODE)
                    publishSelectedSourcesToVideo();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Publishes the user selected Sources using EventBus for VideoCaptureFragment
     */
    private void publishSelectedSourcesToVideo() {
        EventBus.getDefault().post(new SourcesEvent.OnVideoSourcesLoaded(selectedSourcesList));
    }

    /**
     * Publishes the user selected Sources using EventBus for AudioRecorderFragment
     */
    private void publishSelectedSourcesToAudio() {
        EventBus.getDefault().post(new SourcesEvent.OnAudioSourcesLoaded(selectedSourcesList));
    }

}
