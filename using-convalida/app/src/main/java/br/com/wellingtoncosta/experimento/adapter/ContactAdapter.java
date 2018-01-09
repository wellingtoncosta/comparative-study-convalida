package br.com.wellingtoncosta.experimento.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import br.com.wellingtoncosta.experimento.R;
import br.com.wellingtoncosta.experimento.domain.Contact;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author  Wellington Costa on 19/09/2017.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> contacts;

    public ContactAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public int getItemCount() {
        return this.contacts.size();
    }

    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.list_contact_item, viewGroup, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder viewHolder, int position) {
        viewHolder.bind(this.contacts.get(position));
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.contactImage)
        ImageView contactImage;

        @BindView(R.id.contactName)
        TextView contactName;

        @BindView(R.id.contactEmail)
        TextView contactEmail;

        @BindView(R.id.contactPhone)
        TextView contactPhone;

        ContactViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(final Contact contact) {
            String firstChar = contact.getName().substring(0, 1).toUpperCase();
            ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
            TextDrawable drawable = TextDrawable.builder().buildRoundRect(firstChar, colorGenerator.getRandomColor(), 100);

            contactImage.setImageDrawable(drawable);
            contactName.setText(contact.getName());
            contactEmail.setText(contact.getEmail());
            contactPhone.setText(contact.getPhone());
        }
    }
}