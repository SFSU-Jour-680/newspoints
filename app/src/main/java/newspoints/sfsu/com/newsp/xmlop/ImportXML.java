package newspoints.sfsu.com.newsp.xmlop;

import android.content.Context;

import com.newspoints.journalist.entities.Category;
import com.newspoints.journalist.entities.Question;
import com.newspoints.journalist.entities.Shot;
import com.newspoints.journalist.entities.Source;

import java.util.List;

/**
 * <p>
 * Parses the input XML and converts a list of
 * {@link com.newspoints.journalist.entities.Category} containing list of
 * {@link com.newspoints.journalist.entities.Source}, {@link com.newspoints.journalist.entities.Shot} and
 * {@link com.newspoints.journalist.entities.Question}
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
