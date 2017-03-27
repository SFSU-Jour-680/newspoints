package newspoints.sfsu.com.newsp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

import newspoints.sfsu.com.newsp.R;

/**
 * Created by Pavitra on 3/9/2016.
 */
public class QuestionsAdapter extends ArrayAdapter<String> {
    private final static String TAG = "~!@#$QstnAdptr";
    ViewHolder viewHolder;
    List<String> questionsList;
    int mResource;
    private Context mContext;

    public QuestionsAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.questionsList = objects;
        viewHolder = new ViewHolder();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
//            convertView = inflater.inflate(mResource, null);
        }
        viewHolder.textView_questions = (TextView) convertView.findViewById(R.id.textView_questionItem_questions);
        convertView.setTag(viewHolder);
        viewHolder.textView_questions.setText(questionsList.get(position));

        return convertView;
    }

    private class ViewHolder {
        TextView textView_questions;
    }
}