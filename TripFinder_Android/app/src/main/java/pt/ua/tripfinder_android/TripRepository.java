package pt.ua.tripfinder_android;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TripRepository {
    private TripsDao mTripsDao;
    private LiveData<List<Trips>> mAllTrips;
    private LiveData<Trips> mTrip;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    TripRepository(Application application) {
        TripRoomDatabase db = TripRoomDatabase.getDatabase(application);
        mTripsDao = db.tripsDao();
        mAllTrips = mTripsDao.getTrips();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Trips>> getAllTrips() {
        return mAllTrips;
    }

    LiveData<Trips> getTrip(String id){
        return mTripsDao.getTrip(id);
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Trips trip) {
        TripRoomDatabase.databaseWriteExecutor.execute(() -> mTripsDao.insert(trip));
    }
}
