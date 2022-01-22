package pt.ua.tripfinder_android;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TripsDao {
    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Trips trip);

    @Query("DELETE FROM trip_table")
    void deleteAll();

    @Query("SELECT * FROM trip_table")
    LiveData<List<Trips>> getTrips();

    @Query("SELECT * FROM trip_table WHERE id = :trip_id")
    LiveData<Trips> getTrip(String trip_id);
}
