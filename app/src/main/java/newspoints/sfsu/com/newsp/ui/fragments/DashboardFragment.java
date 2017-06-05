package newspoints.sfsu.com.newsp.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.FilePickerFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newspoints.sfsu.com.newsp.R;


public class DashboardFragment extends Fragment {

    private static final int CODE_SD = 1;
    private static final int FILE_ACCESS_PERMISSION = 2;
    private static final String TAG = "~!@#$DashboardFrag";
    @Bind(R.id.fab_dashboard_captureMedia)
    FloatingActionButton fab_captureMedia;
    @Bind(R.id.relativeLayout_dashboard_importXML)
    RelativeLayout relativeLayout_importXML;
    private IDashboardCallbacks mInterface;
    private Context mContext;


    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        askForPermission();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IDashboardCallbacks) {
            mInterface = (IDashboardCallbacks) context;
            mContext = context;
        } else {
            throw new RuntimeException(context.toString() + " must implement IDashboardCallbacks");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, rootView);

        relativeLayout_importXML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filePickerIntent = new Intent(mContext, FilePickerActivity.class);
                filePickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                filePickerIntent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerFragment.MODE_FILE);
                startActivityForResult(filePickerIntent, CODE_SD);
            }
        });

        fab_captureMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.onClickFabCaptureMedia();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (CODE_SD == requestCode) {
            Log.i(TAG, "onActivityResult: success");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInterface = null;
    }

    /**
     * Helper method to clear runtime permissions for user to capture image
     */
    private void askForPermission() {
        int hasCameraPermission = 0;
        int hasWritePermission = 0;
        int hasReadPermission = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasCameraPermission = mContext.checkSelfPermission(Manifest.permission.CAMERA);
            hasWritePermission = mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            hasReadPermission = mContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            List<String> permissions = new ArrayList<>();
            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }

            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), FILE_ACCESS_PERMISSION);
            }
        } else {
            Log.i(TAG, "askForPermission: not working");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionsResult: reached");
        switch (requestCode) {
            case FILE_ACCESS_PERMISSION: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Log.d("Permissions", "Permission Denied: " + permissions[i]);
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    public interface IDashboardCallbacks {
        void onClickFabCaptureMedia();
    }
}
