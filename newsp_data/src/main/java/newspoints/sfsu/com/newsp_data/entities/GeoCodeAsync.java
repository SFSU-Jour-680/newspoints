package newspoints.sfsu.com.newsp_data.entities;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.newspoints.journalist.ui.CreateProjectActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// will call the activity_video_capture activity and returns the list of cities matching the location

public class GeoCodeAsync extends AsyncTask<List<CreateProjectActivity.LocationDetails>, Void, String> {

    ArrayList<String> geocodeCities = new ArrayList<>();
    private Context context;

    public GeoCodeAsync(Context context) {
        this.context = context;
    }


    @Override
    protected void onPostExecute(String geocodeCities) {
        super.onPostExecute(geocodeCities);
        //((IndexActivity) _context).locationParsed();
    }

    @Override
    protected String doInBackground(List<CreateProjectActivity.LocationDetails>... params) {
        List<Address> addresses = null;
        String cityName = "";
        List<CreateProjectActivity.LocationDetails> paramsList = params[0];
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        try {
            for (int i = 0; i < paramsList.size(); i++) {
                // boundary conditions
                if (paramsList.get(i).getLatitude() == 0.0 || paramsList.get(i).getLatitude() == 0.0) {
                    cityName = "undef";
                } else {
                    addresses = gcd.getFromLocation(paramsList.get(i).getLatitude(), paramsList.get(i).getLongitude(), 1);
                    cityName = addresses.get(0).getLocality();
                }
            }
        } catch (IOException e) {
            Log.d("=====>", e.getMessage());
        }
        return cityName;
    }
}