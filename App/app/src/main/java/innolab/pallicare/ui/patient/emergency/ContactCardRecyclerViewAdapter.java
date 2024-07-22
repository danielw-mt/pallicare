package innolab.pallicare.ui.patient.emergency;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import innolab.pallicare.R;
import innolab.pallicare.db.entities.Contact;

/**
 * This adapter creates and holds cards for each element of a given contact list.
 *
 * @author Klaus Schmidt
 */
public class ContactCardRecyclerViewAdapter extends RecyclerView.Adapter<ContactCardViewHolder> {

    /**
     * The generic OnContaktClickListener that is called if a contact card is clicked.
     */
    private OnContactClickListener onContactListener;

    /**
     * This list contains all contact entries which shall be shown as a card
     */
    private List<Contact> contactList;

    //A OnContactListener is provided. This is the emergency class, since it implements this interface.
    ContactCardRecyclerViewAdapter(OnContactClickListener onContactListener) {
        this.onContactListener = onContactListener;
    }

    //Loads the view holder one time. The holder gets a layoutView (the element i.e. a card layout) and connects to all relevant components.
    @NonNull
    @Override
    public ContactCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_card_emergency, parent, false);
        //The ContactCardViewHolder gets the layout it should use and the onContactListener which is passed on. This listener is the emergency class by default.
        return new ContactCardViewHolder(layoutView, onContactListener);
    }

    //This method is called for each element of the contactList and stores the given information of the array into a card.
    @Override
    public void onBindViewHolder(@NonNull ContactCardViewHolder holder, int position) {
        if (contactList != null && position < contactList.size()) {
            Contact contact = contactList.get(position);
            holder.contactName.setText(contact.getName());
            String photo = contact.getPhoto();
            if (photo != null) {
                holder.contactPicture.setImageURI(Uri.parse(photo));
            } else {
                if (position == contactList.size() - 1) {
                    //TODO + symbol
                } else {
                    //TODO dummy photo?
                }
            }
        }
    }

    /**
     * Needed to add new contacts
     *
     * @param newContacts
     */
    void setContacts(List<Contact> newContacts) {
        this.contactList = newContacts;
        this.contactList.add(new Contact("Kontakt hinzufügen", "-1", null));
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // contactList has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (contactList != null)
            return contactList.size();
        else return 0;
    }

    /**
     * returns the phone as a string, by a given position in the list
     *
     * @param position of the touched item
     * @return the assigned telephone-number
     */
    String getPhoneByContactPosition(int position) {
        if (contactList != null) {
            return contactList.get(position).getPhoneNumber();
        } else {
            return null;
        }
    }

}
