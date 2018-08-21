package pe.gob.reneic.pki.idaas.sdk.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Miguel Pazo (http://miguelpazo.com)
 */
public class User implements Parcelable {

    @JsonProperty("doc")
    private String doc;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("phone_number_verified")
    private Boolean phoneNumberVerified;
    @JsonProperty("email")
    private String email;
    @JsonProperty("email_verified")
    private Boolean emailVerified;
    @JsonProperty("ruc")
    private String ruc;
    @JsonProperty("sub")
    private String sub;

    public User() {
    }

    public User(Parcel in) {
        doc = in.readString();
        firstName = in.readString();
        phoneNumber = in.readString();
        phoneNumberVerified = in.readByte() != 0;
        email = in.readString();
        emailVerified = in.readByte() != 0;
        ruc = in.readString();
        sub = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getPhoneNumberVerified() {
        return phoneNumberVerified;
    }

    public void setPhoneNumberVerified(Boolean phoneNumberVerified) {
        this.phoneNumberVerified = phoneNumberVerified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(doc);
        dest.writeString(firstName);
        dest.writeString(phoneNumber);
        dest.writeByte((byte) (phoneNumberVerified ? 1 : 0));
        dest.writeString(email);
        dest.writeByte((byte) (emailVerified ? 1 : 0));
        dest.writeString(ruc);
        dest.writeString(sub);
    }

    @Override
    public String toString() {
        return "User{" +
                "doc='" + doc + '\'' +
                ", firstName='" + firstName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", phoneNumberVerified=" + phoneNumberVerified +
                ", email='" + email + '\'' +
                ", emailVerified=" + emailVerified +
                ", ruc='" + ruc + '\'' +
                ", sub='" + sub + '\'' +
                '}';
    }
}
