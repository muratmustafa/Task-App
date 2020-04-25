package com.example.taskapp.models;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.GregorianCalendar;

@SuppressLint("ParcelCreator")
public class Task implements Parcelable {

    private static int MAX_ID = 0;

    private int mId;
    private String mShortName;
    private String mDescription;
    private Date mCreationDate;
    private boolean mDone;

    public Task(String shortName) {
        this.mId = MAX_ID++;
        this.mShortName = shortName;
        this.mCreationDate = GregorianCalendar.getInstance().getTime();
    }

    protected Task(Parcel in) {
        mId = in.readInt();
        mShortName = in.readString();
        mDescription = in.readString();
        mDone = in.readByte() != 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public int getId() {
        return this.mId;
    }

    public String getShortName() {
        return mShortName;
    }

    public void setShortName(String shortName) {
        this.mShortName = shortName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public Date getCreationDate() {
        return mCreationDate;
    }

    public boolean isDone() {
        return mDone;
    }

    public void setDone(boolean done) {
        this.mDone = done;
    }

    @Override
    public String toString() {
        return mShortName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Task) {
            return this.getId() == ((Task) obj).getId();
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mShortName);
        dest.writeString(mDescription);
        dest.writeByte((byte) (mDone ? 1 : 0));
    }
}
