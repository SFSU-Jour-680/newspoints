package newspoints.sfsu.com.newsp.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.ui.CreateProjectActivity;
import newspoints.sfsu.com.newsp_data.dao.RecordingDetailsDB;
import newspoints.sfsu.com.newsp_data.util.ProjectConstants;
import newspoints.sfsu.com.newsp_lib.util.BaseFragment;

public class NewsMapViewFragment extends BaseFragment implements OnMapReadyCallback {

    GoogleMap googleMap;
    SupportMapFragment fragment;
    Button btnTest;
    private Context mContext;
    private List<CreateProjectActivity.LocationDetails> locationDetails;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_map_view, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // pass the context to the RecordingDetailsDB class.
        ProjectConstants.dbClass = new RecordingDetailsDB(mContext);

        // identify the Fragment container in the Fragment
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_news);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_news, fragment).commit();
        }

        try {
            //get the LocationDetails from the Database.
            locationDetails = ProjectConstants.dbClass.getLocationPoints();
            if (locationDetails == null) {
                Toast.makeText(mContext, "No data to display for Map", Toast.LENGTH_LONG).show();
                // get the LocationDetails from the Database.
                locationDetails = ProjectConstants.dbClass.getLocationPoints();
            }
        } catch (Exception e) {
            Log.d("=====>", e.getMessage());
        }

        // call the onMapReady callback
        fragment.getMapAsync(this);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        // identify the Fragment container in the Fragment
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_news);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_news, fragment).commit();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;


        if (null != googleMap) {
            UiSettings mapSettings;
            mapSettings = googleMap.getUiSettings();
            mapSettings.setZoomControlsEnabled(true);

            if (locationDetails != null) {
                if (locationDetails.size() > 0) {
                    for (int i = 0; i < locationDetails.size(); i++) {
                        LatLng latLng = new LatLng(locationDetails.get(i).getLatitude(), locationDetails.get(i).getLongitude());

                        googleMap.addMarker(new MarkerOptions().position(latLng).title(locationDetails.get(i).getProjectName())
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_48)));

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLng) // Sets the center of the map to
                                .zoom(12)       // Sets the zoom
                                .bearing(0)     // Sets the orientation of the camera to east
                                .tilt(0)        // Sets the tilt of the camera to 30 degrees
                                .build();       // Creates a CameraPosition from the builder
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                }
            } else {
                Toast.makeText(mContext, "No project location found", Toast.LENGTH_LONG).show();
                LatLng SanFrancisco = new LatLng(37.773972, -122.431297);
                googleMap.addMarker(new MarkerOptions().position(SanFrancisco).title("").draggable(false)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_48)).anchor(0.0f, 1.0f));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(SanFrancisco) // Sets the center of the map to
                        .zoom(12)                   // Sets the zoom
                        .bearing(0) // Sets the orientation of the camera to east
                        .tilt(0)    // Sets the tilt of the camera to 30 degrees
                        .build();    // Creates a CameraPosition from the builder
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    }
}
