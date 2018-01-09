package br.com.wellingtoncosta.comparative.ui;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import br.com.wellingtoncosta.comparative.R;
import br.com.wellingtoncosta.comparative.domain.Contact;
import br.com.wellingtoncosta.comparative.domain.User;
import br.com.wellingtoncosta.comparative.util.RealmUtils;
import br.com.wellingtoncosta.comparative.util.SharedPreferencesUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * @author Wellington Costa on 14/09/2017.
 */
public class NewContactActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nameLayout)
    TextInputLayout nameLayout;

    @BindView(R.id.nameField)
    EditText nameField;

    @BindView(R.id.emailLayout)
    TextInputLayout emailLayout;

    @BindView(R.id.emailField)
    EditText emailField;

    @BindView(R.id.phoneLayout)
    TextInputLayout phoneLayout;

    @BindView(R.id.phoneField)
    EditText phoneField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        ButterKnife.bind(this);
        setupToolbar();
        addNameFieldTextChangedListener();
        addEmailFieldTextChangedListener();
        addPhoneFieldTextChangedListener();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_register_new_contact) {
            registerNewContact();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void registerNewContact() {
        try {
            long start = System.currentTimeMillis();
            boolean fieldsAreValid = fieldsAreValid();
            long end = System.currentTimeMillis();

            Log.d("Profiler", "Tempo de execucao = " + (end - start) + "ms");

            if (fieldsAreValid) {
                Realm realm = Realm.getDefaultInstance();
                int userLoggedId = SharedPreferencesUtils.getUserLogged(this);
                User userLogged = realm.where(User.class).equalTo("id", userLoggedId).findFirst();

                if (userLogged != null) {
                    int nextContactId = RealmUtils.getNextValue(Contact.class);

                    realm.beginTransaction();
                    Contact contact = realm.createObject(Contact.class, nextContactId);
                    contact.setName(nameField.getText().toString());
                    contact.setEmail(emailField.getText().toString());
                    contact.setPhone(phoneField.getText().toString());
                    userLogged.getContacts().add(contact);
                    realm.commitTransaction();
                }

                finish();
            }
        } catch (Exception ex) {
            Log.e("NewContactActivity", ex.getMessage());
            Snackbar.make(toolbar, "Não foi possível salvar o contato.", Snackbar.LENGTH_LONG).show();
        }
    }

    public boolean fieldsAreValid() {
        validateNameField(nameField.getText().toString());
        validateEmailField(emailField.getText().toString());
        validatePhoneField(phoneField.getText().toString());
        return !nameLayout.isErrorEnabled() && !emailLayout.isErrorEnabled() && !phoneLayout.isErrorEnabled();
    }

    public void validateEmailField(String text) {
        boolean isValid = Patterns.EMAIL_ADDRESS.matcher(text).matches();
        String errorMessage = !isValid ? getString(R.string.invalid_email) : null;
        emailLayout.setErrorEnabled(!isValid);
        emailLayout.setError(errorMessage);
    }

    public void validateNameField(String text) {
        boolean isValid = text != null && !text.isEmpty();
        String errorMessage = !isValid ? getString(R.string.name_required) : null;
        nameLayout.setErrorEnabled(!isValid);
        nameLayout.setError(errorMessage);
    }

    public void validatePhoneField(String text) {
        boolean isValid = text != null && !text.isEmpty();
        String errorMessage = !isValid ? getString(R.string.phone_required) : null;
        phoneLayout.setErrorEnabled(!isValid);
        phoneLayout.setError(errorMessage);
    }

    public void addNameFieldTextChangedListener() {
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = String.valueOf(charSequence);
                validateNameField(text);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    public void addEmailFieldTextChangedListener() {
        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = String.valueOf(charSequence);
                validateEmailField(text);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    public void addPhoneFieldTextChangedListener() {
        phoneField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = String.valueOf(charSequence);
                validatePhoneField(text);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }
}