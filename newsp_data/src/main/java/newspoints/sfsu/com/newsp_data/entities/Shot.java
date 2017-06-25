package newspoints.sfsu.com.newsp_data.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A shot represents type of video being captured in case of any {@link NPEvent}
 * <p>
 * Defines four different attributes {@code id}, {@code context}, {@code label} and {@code value}
 * </p>
 * Directly maps to an XML file tag represented by Shot
 * Created by Pavitra on 4/3/2016.
 */
public class Shot implements Parcelable {
    public static final Creator<Shot> CREATOR = new Creator<Shot>() {
        @Override
        public Shot createFromParcel(Parcel in) {
            return new Shot(in);
        }

        @Override
        public Shot[] newArray(int size) {
            return new Shot[size];
        }
    };
    private String label, context, value, xmlFileName;
    private Long id;

    public Shot(Long id, String label, String context, String value, String xmlFileName) {
        this.id = id;
        this.label = label;
        this.context = context;
        this.value = value;
        this.xmlFileName = xmlFileName;
    }

    protected Shot(Parcel in) {
        id = in.readLong();
        label = in.readString();
        context = in.readString();
        value = in.readString();
        xmlFileName = in.readString();
    }

    public String getXmlFileName() {
        return xmlFileName;
    }

    public void setXmlFileName(String xmlFileName) {
        this.xmlFileName = xmlFileName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
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
        dest.writeLong(id);
        dest.writeString(label);
        dest.writeString(context);
        dest.writeString(value);
        dest.writeString(xmlFileName);
    }
}
