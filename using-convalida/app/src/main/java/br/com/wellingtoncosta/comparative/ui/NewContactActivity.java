package br.com.wellingtoncosta.comparative.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import br.com.wellingtoncosta.comparative.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Wellington Costa on 14/09/2017.
 */
public class NewContactActivity extends AppCompatActivity {

    private static final String PHONE_PATTERN = "^(?:(?:\\+|00)?(55)\\s?)?(?:\\(?([1-9][0-9])\\)?\\s?)?(?:((?:9\\d|[2-9])\\d{3})\\-?(\\d{4}))$";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //    @NotEmptyValidation(R.string.name_required)
    @BindView(R.id.nameLayout)
    TextInputLayout nameLayout;

    @BindView(R.id.nameField)
    EditText nameField;

    //    @EmailValidation(R.string.invalid_email)
    @BindView(R.id.emailLayout)
    TextInputLayout emailLayout;

    @BindView(R.id.emailField)
    EditText emailField;

    @BindView(R.id.phoneLayout)
    TextInputLayout phoneLayout;

    //    @PatternValidation(errorMessage = R.string.phone_required, pattern = PHONE_PATTERN)
    @BindView(R.id.phoneField)
    EditText phoneField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        ButterKnife.bind(this);
        setupToolbar();
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
        long start = System.currentTimeMillis();
//        boolean fieldsAreValid = validator.validateFields();
        long end = System.currentTimeMillis();

        Log.d("Profiler", "Tempo de execucao = " + (end - start) + "ms");

//        if(fieldsAreValid) {
//            try {
//                Realm realm = Realm.getDefaultInstance();
//                int userLoggedId = SharedPreferencesUtils.getUserLogged(this);
//                User userLogged = realm.where(User.class).equalTo("id", userLoggedId).findFirst();
//
//                if (userLogged != null) {
//                    int nextContactId = RealmUtils.getNextValue(Contact.class);
//
//                    realm.beginTransaction();
//                    Contact contact = realm.createObject(Contact.class, nextContactId);
//                    contact.setName(nameField.getText().toString());
//                    contact.setEmail(emailField.getText().toString());
//                    contact.setPhone(phoneField.getText().toString());
//                    userLogged.getContacts().add(contact);
//                    realm.commitTransaction();
//                }
//
//                finish();
//            } catch (Exception ex) {
//                Log.e("NewContactActivity", ex.getMessage());
//                Snackbar.make(toolbar, "Não foi possível salvar o contato.", Snackbar.LENGTH_LONG).show();
//            }
//        }
    }
}