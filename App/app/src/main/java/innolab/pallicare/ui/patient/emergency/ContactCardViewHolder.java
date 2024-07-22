package innolab.pallicare.ui.patient.emergency;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import innolab.pallicare.R;

/**
 * This class gets the layout and connects to the needed visual components
 * - extends the ViewHolder to inherit the necessary methods.
 * - implements the onClickListener, to be able to react on a click
 *
 * @author Klaus Schmidt
 */
public class ContactCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /**
     * the imageView for the contact image
     * https://github.com/hdodenhof/CircleImageView
     */
    CircleImageView contactPicture;

    /**
     * the textView for the contact name
     */
    TextView contactName;

    /**
     * gets an onContactClickListener to call on an onClick.
     */
    private OnContactClickListener onContactClickListener;

    ContactCardViewHolder(@NonNull View itemView, OnContactClickListener onContactClickListener) {
        super(itemView);
        contactPicture = itemView.findViewById(R.id.contact_image);
        contactName = itemView.findViewById(R.id.contact_name);
        this.onContactClickListener = onContactClickListener;

        //sets the onClickListener to this cardItem
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //If the card is clicked, call the onContactClickListener by telling the position of the clicked contact. The onConctactClickListener is the EmergencyActivity class
        onContactClickListener.onContactClick(getAdapterPosition());
    }
}
