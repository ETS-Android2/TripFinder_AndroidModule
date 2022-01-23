package pt.ua.tripfinder_android;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository mRepository;

    public UserViewModel (Application application) {
        super(application);
        mRepository = new UserRepository(application);
    }

    LiveData<User> getUser(String id) { return mRepository.getUser(id); }

    public void insert(User user) { mRepository.insert(user); }
}
