package newspoints.sfsu.com.newsp_data.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A Source represents a real world entity such as a police officer, friend,  neighbor who provides substantial information and
 * can provide validating information on {@link MyEvent}.
 * <p>
 * Defines three different properties {@code id}, {@code label} and {@code value}.
 * </p>
 * Directly maps to an XML file tag represented by Source.
 * Created by Pavitra on 4/3/2016.
 */
public class Source implements Parcelable {
    public static final Creator<Source> CREATOR = new Creator<Source>() {
        @Override
        public Source createFromParcel(Parcel in) {
            return new Source(in);
        }

        @Override
        public Source[] newArray(int size) {
            return new Source[size];
        }
    };
    private String id;
    private String label;
    private String value;
    private String xmlFileName;

    public Source(String id, String label, String value, String xmlFileName) {
        this.id = id;
        this.label = label;
        this.value = value;
        this.xmlFileName = xmlFileName;
    }

    protected Source(Parcel in) {
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
