package innolab.pallicare.ui.patient.emergency;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import innolab.pallicare.db.PallicareRepository;
import innolab.pallicare.db.entities.Contact;

/**
 * A ViewModel holds your app's UI data in a lifecycle way that survives configuration changes.
 * Adopted from https://www.codeproject.com/Articles/1277308/Work-with-Database-using-Room-and-recyclerview-in
 * More information: https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#8
 *
 * @author Klaus Schmidt
 */
public class ContactViewModel extends AndroidViewModel {

    private PallicareRepository mDataRepository;
    private LiveData<List<Contact>> mListLiveData;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        mDataRepository = PallicareRepository.getInstance(getApplication()); //TODO can this be replaced with = new PallicareRepository(application)?
        mListLiveData = mDataRepository.getAllContacts();
    }

    LiveData<List<Contact>> getAllContacts() {
        return mListLiveData;
    }

    void insertItem(Contact contactItem) {
        mDataRepository.insertContact(contactItem);
    }
}