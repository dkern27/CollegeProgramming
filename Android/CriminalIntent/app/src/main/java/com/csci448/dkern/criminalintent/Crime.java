package com.csci448.dkern.criminalintent;

import android.text.format.DateFormat;

import java.util.Date;
import java.util.UUID;

/**
 * Created by dkern on 2/1/17.
 */

public class Crime
{
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;

    public Crime()
    {
        this(UUID.randomUUID());
    }

    public Crime(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public UUID getId()
    {
        return mId;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate()
    {
        return mDate;
    }

    public String getDateString()
    {
        return DateFormat.format("EEEE, MMM d, yyyy", mDate).toString();
    }

    public void setDate(Date date)
    {
        mDate = date;
    }

    public boolean isSolved()
    {
        return mSolved;
    }

    public void setSolved(boolean solved)
    {
        mSolved = solved;
    }

    public String getSuspect()
    {
        return mSuspect;
    }

    public void setSuspect(String suspect)
    {
        mSuspect = suspect;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
