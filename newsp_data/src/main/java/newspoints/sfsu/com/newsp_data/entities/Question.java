package newspoints.sfsu.com.newsp_data.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Questions pertaining to an event or source configured while capturing {@link NPAudio} or {@link NPVideo}. Defines three
 * different properties {@code id}, {@code label} and {@code value}.
 * <p>
 * Directly maps to an XML file tag represented by Question
 * </p>
 * Created by Pavitra on 4/3/2016.
 */
public class Question implements Parcelable {
    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
    private String id, label, value, xmlFileName;

    public Question(String id, String label, String value, String xmlFileName) {
        this.id = id;
        this.label = label;
        this.value = value;
        this.xmlFileName = xmlFileName;
    }

    protected Question(Parcel in) {
        id = in.readString();
        label = in.readString();
        value = in.readString();
    }

    public String getXmlFileName() {
        return xmlFileName;
    }

    public void setXmlFileName(String xmlFileName) {
        this.xmlFileName = xmlFileName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(label);
        dest.writeString(value);
    }
}
