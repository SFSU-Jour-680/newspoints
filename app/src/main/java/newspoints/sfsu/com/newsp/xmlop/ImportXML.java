package newspoints.sfsu.com.newsp.xmlop;

import android.content.Context;

import java.util.List;

import newspoints.sfsu.com.newsp_data.entities.Category;
import newspoints.sfsu.com.newsp_data.entities.Question;
import newspoints.sfsu.com.newsp_data.entities.Shot;
import newspoints.sfsu.com.newsp_data.entities.Source;


/**
 * <p>
 * Parses the input XML and converts a list of Entities
 * </p>
 * <p>
 * Returns a list of Categories after successfully parsing the input file.
 * </p>
 * Created by Pavitra on 6/14/2016.
 */
public class ImportXML {
    private Context mContext;
    private List<Category> categories;
    private List<Shot> shots;
    private List<Source> sources;
    private List<Question> questions;


    public ImportXML(Context mContext) {
        this.mContext = mContext;
    }


}
