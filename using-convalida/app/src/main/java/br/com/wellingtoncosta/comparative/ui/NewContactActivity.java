package br.com.wellingtoncosta.comparative.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.wellingtoncosta.comparative.R;
import br.com.wellingtoncosta.comparative.domain.Contact;
import br.com.wellingtoncosta.comparative.domain.User;
import br.com.wellingtoncosta.comparative.util.RealmUtils;
import br.com.wellingtoncosta.comparative.util.SharedPreferencesUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import convalida.annotations.EmailValidation;
import convalida.annotations.OnValidationSuccess;
import convalida.annotations.PatternValidation;
import convalida.annotations.RequiredValidation;
import convalida.annotations.ValidateOnClick;
import io.realm.Realm;

/**
 * @author Wellington Costa on 14/09/2017.
 */
public class NewContactActivity extends AppCompatActivity {

    private static final String PHONE_PATTERN = "^(?:(?:\\+|00)?(55)\\s?)?(?:\\(?([1-9][0-9])\\)?\\s?)?(?:((?:9\\d|[2-9])\\d{3})\\-?(\\d{4}))$";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nameLayout)
    TextInputLayout nameLayout;

    @RequiredValidation(errorMessage = R.string.name_required)
    @BindView(R.id.nameField)
    EditText nameField;

    @BindView(R.id.emailLayout)
    TextInputLayout emailLayout;

    @EmailValidation(errorMessage = R.string.invalid_email)
    @BindView(R.id.emailField)
    EditText emailField;

    @BindView(R.id.phoneLayout)
    TextInputLayout phoneLayout;

    @PatternValidation(errorMessage = R.string.phone_required, pattern = PHONE_PATTERN)
    @BindView(R.id.phoneField)
    EditText phoneField;

    @ValidateOnClick
    @BindView(R.id.saveContactButton)
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        ButterKnife.bind(this);
        NewContactActivityFieldsValidation.init(this);
        setupToolbar();
    }

    @Override
    public void onBackPressed() {
        finish();
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

    @OnValidationSuccess
    public void registerNewContact() {
        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();

        Log.d("Profiler", "Tempo de execucao = " + (end - start) + "ms");

        try {
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
        } catch (Exception ex) {
            Log.e("NewContactActivity", ex.getMessage());
            Snackbar.make(toolbar, "Não foi possível salvar o contato.", Snackbar.LENGTH_LONG).show();
        }
    }
}