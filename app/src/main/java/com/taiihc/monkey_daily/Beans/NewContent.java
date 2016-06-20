package com.taiihc.monkey_daily.Beans;


import android.os.Parcel;
import android.os.Parcelable;

public class NewContent implements Parcelable{
    private String body;

    protected NewContent(Parcel in) {
        body = in.readString();
        title = in.readString();
        image = in.readString();
    }

    public static final Creator<NewContent> CREATOR = new Creator<NewContent>() {
        @Override
        public NewContent createFromParcel(Parcel in) {
            return new NewContent(in);
        }

        @Override
        public NewContent[] newArray(int size) {
            return new NewContent[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String title;
    private String image;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(body);
        dest.writeString(title);
        dest.writeString(image);
    }
}
