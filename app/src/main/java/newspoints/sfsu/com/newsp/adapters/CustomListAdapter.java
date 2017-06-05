package newspoints.sfsu.com.newsp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.ui.CreateProjectActivity;
import newspoints.sfsu.com.newsp.async.GeoCodeAsync;
import newspoints.sfsu.com.newsp_data.util.ProjectConstants;

public class CustomListAdapter extends ArrayAdapter {

    Editor edit;
    SharedPreferences prefs;
    SimpleDateFormat sdf;
    TextView projectLocation;
    private Context _context;
    private List<String> _listDataHeader, projectImage, projectDate;


    public CustomListAdapter(Context context, List<String> listDataHeader, List<String> projectImage, List<String> projectDate) {
        super(context, 0, 0, listDataHeader);
        this._context = context;
        this._listDataHeader = listDataHeader;
        prefs = _context.getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
        edit = prefs.edit();
        this.projectImage = projectImage;
        this.projectDate = projectDate;
    }

    @Override
    public int getCount() {

        return this._listDataHeader.size();
    }

    @Override
    public Object getItem(int position) {

        int temp = this._listDataHeader.size();
        return this._listDataHeader.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item_project, null);
        }

        String headerTitle = (String) getItem(position);

        // video count
        int audioCount = ProjectConstants.dbClass.getRecordingCount(headerTitle, "audio");
        int videoCount = ProjectConstants.dbClass.getRecordingCount(headerTitle, "video");
        TextView video_count = (TextView) convertView.findViewById(R.id.textView_listItem_videoCount);
        video_count.setTypeface(ProjectConstants.ROBOT_REGULAR);

        // audio count
        TextView audio_count = (TextView) convertView.findViewById(R.id.textView_listItem_audioCount);
        audio_count.setTypeface(ProjectConstants.ROBOT_REGULAR);
        video_count.setText("" + videoCount);
        audio_count.setText("" + audioCount);

        String timeDurationString = ProjectConstants.dbClass.getTotalRecordingDuration(headerTitle);

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.textView_listItem_header);
        lblListHeader.setText(headerTitle);
        lblListHeader.setTypeface(ProjectConstants.ROBOT_MEDIUM);

        TextView projectDate = (TextView) convertView.findViewById(R.id.textView_listItem_date);
        if (this.projectDate.get(position).equals("")) {
            projectDate.setText("mm:dd:yy");
        } else {
            // apply formatting in date
            sdf = new SimpleDateFormat("MM-dd-yy HH:mm");
            Date eventDate = null;
            try {
                eventDate = sdf.parse(this.projectDate.get(position));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat sdfFinal = new SimpleDateFormat("MM/dd/yy; HH:mm");
            String finalDate = sdfFinal.format(eventDate);

            // once we get the final date, in proper format, just set it to the TextView
            projectDate.setText(finalDate);
        }
        ImageView projectImageView = (ImageView) convertView.findViewById(R.id.imageView_listItem_projectImage);

        try {
            Bitmap bitmap = BitmapFactory.decodeFile(projectImage.get(position));
            if (bitmap != null) {
                projectImageView.setImageBitmap(bitmap);
            } else {
                projectImageView.setImageResource(R.drawable.newspoints_placeholder_256x256);
            }
        } catch (OutOfMemoryError o) {
            projectImageView.setImageBitmap(null);
        }
        TextView timeDuration = (TextView) convertView.findViewById(R.id.textView_listItem_time);

        if (timeDurationString.equals("")) {
            timeDuration.setText("00:00");
        } else {
            timeDuration.setText(timeDurationString);
        }
        timeDuration.setTypeface(ProjectConstants.ROBOT_REGULAR);

        // set the city project location
        projectLocation = (TextView) convertView.findViewById(R.id.textView_listItem_location);
        List<CreateProjectActivity.LocationDetails> locationDetails = ProjectConstants.dbClass.getLocationPoints();

        GeoCodeAsync geoCodeCity = new GeoCodeAsync(_context);
        geoCodeCity.execute(locationDetails);

        String city = "demo";
        try {
            city = geoCodeCity.get().toString();
            projectLocation.setText(city);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}