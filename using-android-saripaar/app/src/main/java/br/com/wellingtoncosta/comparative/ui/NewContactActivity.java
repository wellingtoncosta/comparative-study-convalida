package br.com.wellingtoncosta.comparative.ui;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

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
public class NewContactActivity extends AppCompatActivity implements Validator.ValidationListener{

    private Validator validator;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @NotEmpty(messageResId = R.string.name_required)
    @BindView(R.id.nameLayout)
    TextInputLayout nameLayout;

    @BindView(R.id.nameField)
    EditText nameField;

    @Email(messageResId = R.string.invalid_email)
    @BindView(R.id.emailLayout)
    TextInputLayout emailLayout;

    @BindView(R.id.emailField)
    EditText emailField;

    @NotEmpty(messageResId = R.string.phone_required)
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

        validator = new Validator(this);
        validator.registerAdapter(TextInputLayout.class, new TextInputLayoutAdapter());
        validator.setValidationListener(this);
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
            validator.validate();
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

    @Override
    public void onValidationSucceeded() {
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

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else if (view instanceof TextInputLayout) {
                ((TextInputLayout) view).setError(message);
                ((TextInputLayout) view).setErrorEnabled(true);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}