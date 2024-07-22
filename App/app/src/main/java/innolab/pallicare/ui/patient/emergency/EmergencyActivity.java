package innolab.pallicare.ui.patient.emergency;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import innolab.pallicare.R;
import innolab.pallicare.db.entities.Contact;
import innolab.pallicare.ui.BaseActivity;

/**
 * This class will show the alarm screen and start the emergency process (like calling / notifying etc, tbd).
 * <p>
 * -extends {@link BaseActivity} with hidden bottom buttons
 * -implements the {@link OnContactClickListener} interface, to be notified if someone clicks on an emergency contact card
 * For tutorial connecting with DB see: https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#0
 *
 * @author Klaus Schmidt
 */
public class EmergencyActivity extends BaseActivity implements OnContactClickListener {

    private static final int PICK_CONTACT = 1603;
    ContactCardRecyclerViewAdapter adapter;

    private ContactViewModel mContactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hideAlarmButton();
        super.onCreate(savedInstanceState);
        setUpRecyclerView();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_emergency_contacts;
    }

    @Override
    protected int getLayoutTitleDescriptor() {
        return R.string.emergency_activity_title;
    }

    @Override
    protected int getHelpText() {
        return R.string.help_emergency;
    }

    /**
     * Sets up the recyclerView to show the cards
     */
    private void setUpRecyclerView() {
        // Set up the RecyclerView, which can show many entries from a list
        RecyclerView recyclerView = findViewById(R.id.emergency_recycler_view);
        recyclerView.setHasFixedSize(true);
        //Set the Layout window as a grid, spanCount is the number of rows or columns (depending on orientation)
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));

        /*This adapter is used to connect the information of the contact list and the recycler view.
         It creates a new ContactCardRecyclerViewAdapter and sets itself as onContactListener, as it implements this interface*/
        adapter = new ContactCardRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        //Set the spacing between cards
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.smallPadding);
        recyclerView.addItemDecoration(new ContactGridItemDecoration(smallPadding, smallPadding));

        mContactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        mContactViewModel.getAllContacts().observe(this, contacts -> {
            // Update the cached copy of the words in the adapter.
            adapter.setContacts(contacts);
        });
    }

    /**
     * This method will return to the previous screen. It is called by the abort button.
     *
     * @param view ignore
     */
    public void stopAlarmAndGoBack(View view) {
        finish();
        /*
         * TODO KLAUS
         * Instead of solely finish one could use:
         * Intent replyIntent = new Intent();
         * if ...  setResult(RESULT_CANCELED, replyIntent);
         * else ... {
         *          replyIntent.putExtra(EXTRA_REPLY, word);
         *          setResult(RESULT_OK, replyIntent);
         * }
         * finish();
         */
    }


    //Implemented from the OnContactClickListener.
    @Override
    public void onContactClick(int position) {
        if (position == (adapter.getItemCount() - 1)) {
            //Opens the contact app to choose a contact
            Intent intent = new Intent(Intent.ACTION_PICK,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        } else {
            ///start a call
            String telno = adapter.getPhoneByContactPosition(position);
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + telno));
            startActivity(intent);
        }
    }

    /**
     * Save the chosen contact to the DB
     */
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                Cursor c;
                Uri contactData = data.getData();
                assert contactData != null;
                c = getContentResolver().query(contactData, null, null, null, null);
                assert c != null;
                c.moveToFirst();

                String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNo = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String photoUri = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                System.out.println(photoUri);

                if ((name != null) && (phoneNo != null)) {
                    mContactViewModel.insertItem(new Contact(name, phoneNo, photoUri));
                }

                c.close();
            }
        }
    }
}
