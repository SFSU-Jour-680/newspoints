package newspoints.sfsu.com.newsp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import newspoints.sfsu.com.newsp.R;
import newspoints.sfsu.com.newsp.util.ProjectConstants;

public class FilesListAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> projectId, recordingId, endTime, eventId, type, imageUri,
            createdDate;
    RelativeLayout listView;
    String projectName;
    SimpleDateFormat sdf;
    SharedPreferences prefs;
    //Set<String> shotsAll, sourcesAll, questionsAll;
    Set<String> shotsTemp, sourcesTemp, questionsTemp;


    public FilesListAdapter(Context context, String[] projectId,
                            String[] recordingId, String[] endTime, String[] eventId,
                            String[] type, String[] imageUri, String[] createdDate, String projectName) {
        this.context = context;
        this.projectName = projectName;
        //Toast.makeText(context, eventType, Toast.LENGTH_LONG).show();
        this.projectId = new ArrayList<String>();
        this.recordingId = new ArrayList<String>();
        this.endTime = new ArrayList<String>();
        this.eventId = new ArrayList<String>();
        this.type = new ArrayList<String>();
        this.imageUri = new ArrayList<String>();
        this.createdDate = new ArrayList<String>();
        for (int i = 0; i < projectId.length; i++) {

            try {
                if (eventId[i].contains("404")) {

                } else {
                    this.projectId.add(projectId[i]);
                    this.endTime.add(endTime[i]);
                    this.eventId.add(eventId[i]);
                    this.type.add(type[i]);
                    this.recordingId.add(recordingId[i]);
                    this.imageUri.add(imageUri[i]);
                    this.createdDate.add(createdDate[i]);
                }
            } catch (Exception e) {
                Log.d("=====>", "Null in parsing the Sets");
            }
        }


    }

    @Override
    public int getCount() {
        return this.projectId.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.files_list_layout,
                    null);
        }
        listView = (RelativeLayout) convertView.findViewById(R.id.list_item);
        TextView recordingDate = (TextView) convertView
                .findViewById(R.id.recording_date);

        sdf = new SimpleDateFormat("MM-dd-yy HH:mm");
        Date eventDate = null;
        try {
            eventDate = sdf.parse(createdDate.get(position));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdfFinal = new SimpleDateFormat("MM/dd/yy; HH:mm");
        String finalDate = sdfFinal.format(eventDate);
        //Toast.makeText(context, finalDate, Toast.LENGTH_LONG).show();

        // once we get the final date, in proper format, just set it to the TextView
        recordingDate.setText(finalDate);

        recordingDate.setTypeface(ProjectConstants.ROBOT_REGULAR);
        TextView recordingId = (TextView) convertView.findViewById(R.id.recording_id);
        TextView item_id = (TextView) convertView.findViewById(R.id.item_id);
        TextView eventCategory = (TextView) convertView.findViewById(R.id.eventType);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        ImageView recordingImage = (ImageView) convertView
                .findViewById(R.id.recording_image);


//        prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
//
//        // get Project ID and Category in order to build the absolute path
//        String projectDetails[] = ProjectConstants.dbClass.getProjectMainDetails(projectName);
//
//        shotsAll = prefs.getStringSet(projectDetails[1] + ProjectConstants.SHOT, new HashSet<String>());
//        sourcesAll = prefs.getStringSet(projectDetails[1] + ProjectConstants.SOURCE, new HashSet<String>());
//        questionsAll = prefs.getStringSet(projectDetails[1] + ProjectConstants.QUESTION, new HashSet<String>());

        shotsTemp = ProjectConstants.getShots();
        sourcesTemp = ProjectConstants.getSources();
        questionsTemp = ProjectConstants.getQuestions();

//        Toast.makeText(context, "" + shotsTemp.size(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "" + sourcesTemp.size(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(context, "" + questionsTemp.size(), Toast.LENGTH_SHORT).show();

        try {
            String eventType = this.eventId.get(position);
            item_id.setTypeface(ProjectConstants.ROBOT_MEDIUM);
            item_id.setText(eventType);
            try {
                if (shotsTemp.contains(eventType)) {
//                    item_id.setBackgroundColor(context.getResources().getColor(R.color.colorShot));
                    item_id.setTextColor((ContextCompat.getColor(context, R.color.colorShot)));
                    eventCategory.setText(ProjectConstants.SHOT);
                } else if (sourcesTemp.contains(eventType)) {
//                    item_id.setBackgroundColor(context.getResources().getColor(R.color.colorSource));
                    item_id.setTextColor((ContextCompat.getColor(context, R.color.colorSource)));
                    eventCategory.setText(ProjectConstants.SOURCE);
                } else if (questionsTemp.contains(eventType)) {
//                    item_id.setBackgroundColor(context.getResources().getColor(R.color.colorQuestion));
                    item_id.setTextColor(ContextCompat.getColor(context, R.color.colorQuestion));
                    eventCategory.setText(ProjectConstants.QUESTION);
                }
            } catch (NullPointerException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            // set time
            SimpleDateFormat sdfTime = new SimpleDateFormat("m:s");
            Date eventTime = null;
            try {
                eventDate = sdfTime.parse(this.endTime.get(position));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat sdfTimeFinal = new SimpleDateFormat("mm:ss");
            String finalTime = sdfTimeFinal.format(eventDate);
            //Toast.makeText(context, finalTime, Toast.LENGTH_LONG).show();

            time.setText(finalTime);
            time.setTypeface(ProjectConstants.ROBOT_MEDIUM);

            Bitmap bitmap = BitmapFactory.decodeFile(imageUri.get(position));

            // create an empty bitmap
            Bitmap emptyBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
            // if the bitmap is empty
            if (bitmap.sameAs(emptyBitmap)) {
                recordingImage.setImageResource(R.drawable.newspoints_placeholder_256x256);
            } else {
                recordingImage.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            Log.d("=====>", e.getMessage());
        }

        return convertView;

    }
}
