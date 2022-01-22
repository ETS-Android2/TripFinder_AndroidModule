package pt.ua.tripfinder_android;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "trip_table")
public class Trips {

    @PrimaryKey
    private final int id;

    @ColumnInfo(name = "distance")
    private final int distance;

    @ColumnInfo(name = "title")
    private final String title;

    @ColumnInfo(name = "short")
    private final String contentShort;

    @ColumnInfo(name = "full")
    private final String contentFull;

    @ColumnInfo(name = "image")
    private final String imageurl;

    @ColumnInfo(name = "location")
    private final String location;

    @ColumnInfo(name = "lat")
    private final double lat;

    @ColumnInfo(name = "lng")
    private final double lng;

    public Trips(int id, int distance, String title, String contentShort,
                 String contentFull, String imageurl, String location,
                 double lat, double lng){
        this.id=id;
        this.distance=distance;
        this.title=title;
        this.contentShort=contentShort;
        this.contentFull=contentFull;
        this.imageurl=imageurl;
        this.location=location;
        this.lat=lat;
        this.lng=lng;
    }

    public int getId(){
        return id;
    }

    public int getDistance(){
        return distance;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getContentFull() {
        return contentFull;
    }

    public String getContentShort() {
        return contentShort;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getLocation() {
        return location;
    }

    public String getTitle() {
        return title;
    }
}
