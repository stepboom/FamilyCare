package stepboom.familycare.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Stepboom on 11/18/2017.
 */

public class UserParcelable implements Parcelable {

    private String macAddress;
    private String information;

    public UserParcelable(String macAddress, String information){
        this.macAddress = macAddress;
        this.information = information;
    }

    public String getMacAddress(){
        return this.macAddress;
    }

    public String getInformation(){
        return this.information;
    }

    protected UserParcelable(Parcel in) {
        macAddress = in.readString();
        information = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(macAddress);
        dest.writeString(information);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserParcelable> CREATOR = new Creator<UserParcelable>() {
        @Override
        public UserParcelable createFromParcel(Parcel in) {
            return new UserParcelable(in);
        }

        @Override
        public UserParcelable[] newArray(int size) {
            return new UserParcelable[size];
        }
    };
}
