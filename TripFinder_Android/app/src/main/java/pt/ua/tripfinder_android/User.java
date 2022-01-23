package pt.ua.tripfinder_android;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {

    @PrimaryKey
    @NonNull
    private final String id;

    @ColumnInfo(name = "name")
    private final String name;

    @ColumnInfo(name = "image_url")
    private final String image_url;

    public User(String id, String name, String image_url) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
    }

    public String getId() {
        return id;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getName() {
        return name;
    }
}
