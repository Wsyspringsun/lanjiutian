package com.wyw.ljtds.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/16 0016.
 */

public class EvaluatePhotoModel extends BaseModel implements Parcelable{
    public String content;
    public ArrayList<String> photos;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeStringList(this.photos);
    }

    public EvaluatePhotoModel() {
    }

    public EvaluatePhotoModel(String content, ArrayList<String> photos) {
        this.content = content;
        this.photos = photos;
    }

    protected EvaluatePhotoModel(Parcel in) {
        this.content = in.readString();
        this.photos = in.createStringArrayList();
    }

    public static final Parcelable.Creator<EvaluatePhotoModel> CREATOR = new Parcelable.Creator<EvaluatePhotoModel>() {
        @Override
        public EvaluatePhotoModel createFromParcel(Parcel source) {
            return new EvaluatePhotoModel(source);
        }

        @Override
        public EvaluatePhotoModel[] newArray(int size) {
            return new EvaluatePhotoModel[size];
        }
    };
}
