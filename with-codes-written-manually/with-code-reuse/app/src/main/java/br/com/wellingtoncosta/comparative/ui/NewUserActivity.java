package br.com.wellingtoncosta.comparative.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import br.com.wellingtoncosta.comparative.R;
import br.com.wellingtoncosta.comparative.domain.User;
import br.com.wellingtoncosta.comparative.ui.validation.ConfirmPasswordValidator;
import br.com.wellingtoncosta.comparative.ui.validation.EmailValidator;
import br.com.wellingtoncosta.comparative.ui.validation.NotEmptyValidator;
import br.com.wellingtoncosta.comparative.ui.validation.ValidatorSet;
import br.com.wellingtoncosta.comparative.util.RealmUtils;
import br.com.wellingtoncosta.comparative.util.SharedPreferencesUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * @author Wellington Costa on 14/09/2017.
 */
public class NewUserActivity extends AppCompatActivity {

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

    @BindView(R.id.passwordLayout)
    TextInputLayout passwordLayout;

    @BindView(R.id.passwordField)
    EditText passwordField;

    @BindView(R.id.confirmPasswordLayout)
    TextInputLayout confirmPasswordLayout;

    @BindView(R.id.confirmPasswordField)
    EditText confirmPasswordField;

    private ValidatorSet validators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        ButterKnife.bind(this);
        setupToolbar();

        validators = new ValidatorSet();
        validators.addValidator(new NotEmptyValidator(nameLayout, getString(R.string.name_required)));
        validators.addValidator(new EmailValidator(emailLayout, getString(R.string.invalid_email)));
        validators.addValidator(new NotEmptyValidator(passwordLayout, getString(R.string.password_required)));
        validators.addValidator(new ConfirmPasswordValidator(passwordLayout, confirmPasswordLayout, getString(R.string.different_password)));
    }

    @Override
    public void onBackPressed() {
        goBackToLoginActivity();
    }

    public void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToLoginActivity();
            }
        });
    }

    public void goBackToLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void goToListContactsActivity() {
        startActivity(new Intent(this, ListContactsActivity.class));
        finish();
    }

    @OnClick(R.id.saveNewUserButton)
    public void saveNewUser() {
        try {
            if (validators.validate()) {
                Realm realm = Realm.getDefaultInstance();
                int nextUserId = RealmUtils.getNextValue(User.class);

                realm.beginTransaction();
                User user = realm.createObject(User.class, nextUserId);
                user.setName(nameField.getText().toString());
                user.setEmail(emailField.getText().toString());
                user.setPassword(passwordField.getText().toString());
                realm.commitTransaction();

                SharedPreferencesUtils.setUserLogged(this, nextUserId);
                goToListContactsActivity();
            }
        } catch (Exception ex) {
            Log.e("NewUserActivity", ex.getMessage());
            Snackbar.make(toolbar, "Não foi possível salvar o usuário.", Snackbar.LENGTH_LONG).show();
        }
    }
}