package pt.ua.tripfinder_android;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TripsViewModel extends AndroidViewModel {

    private TripRepository mRepository;

    private final LiveData<List<Trips>> mAllTrips;

    public TripsViewModel (Application application) {
        super(application);
        mRepository = new TripRepository(application);
        mAllTrips = mRepository.getAllTrips();
    }

    LiveData<List<Trips>> getAllTrips() { return mAllTrips; }

    LiveData<Trips> getTrip(String id) { return mRepository.getTrip(id); }

    public void insert(Trips trip) { mRepository.insert(trip); }
}
