package co.id.dicoding.moviecatalogueapi.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FavMovie implements Parcelable {
    private int id;
    private String title;
    private String description;
    private String release;
    private String img;
    private String vote;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.release);
        dest.writeString(this.img);
        dest.writeString(this.vote);
    }

    public FavMovie() {
    }

    public FavMovie(int id, String title, String description, String release, String img, String vote) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.release = release;
        this.img = img;
        this.vote = vote;
    }

    protected FavMovie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.release = in.readString();
        this.img = in.readString();
        this.vote = in.readString();
    }

    public static final Creator<FavMovie> CREATOR = new Creator<FavMovie>() {
        @Override
        public FavMovie createFromParcel(Parcel source) {
            return new FavMovie(source);
        }

        @Override
        public FavMovie[] newArray(int size) {
            return new FavMovie[size];
        }
    };

    @Override
    public String toString() {
        return "FavMovie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", release='" + release + '\'' +
                ", img='" + img + '\'' +
                ", vote='" + vote + '\'' +
                '}';
    }
}
