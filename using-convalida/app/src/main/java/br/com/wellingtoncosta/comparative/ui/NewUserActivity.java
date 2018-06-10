package br.com.wellingtoncosta.comparative.ui;

import android.content.Intent;
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
import br.com.wellingtoncosta.comparative.domain.User;
import br.com.wellingtoncosta.comparative.util.RealmUtils;
import br.com.wellingtoncosta.comparative.util.SharedPreferencesUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import convalida.annotations.ConfirmPasswordValidation;
import convalida.annotations.EmailValidation;
import convalida.annotations.OnValidationSuccess;
import convalida.annotations.PasswordValidation;
import convalida.annotations.RequiredValidation;
import convalida.annotations.ValidateOnClick;
import convalida.library.util.Patterns;
import io.realm.Realm;

/**
 * @author Wellington Costa on 14/09/2017.
 */
public class NewUserActivity extends AppCompatActivity {

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

    @BindView(R.id.passwordLayout)
    TextInputLayout passwordLayout;

    @PasswordValidation(
            min = 1,
            pattern = Patterns.NUMERIC_ONLY,
            errorMessage = R.string.password_required
    )
    @BindView(R.id.passwordField)
    EditText passwordField;

    @BindView(R.id.confirmPasswordLayout)
    TextInputLayout confirmPasswordLayout;

    @ConfirmPasswordValidation(errorMessage = R.string.different_password)
    @BindView(R.id.confirmPasswordField)
    EditText confirmPasswordField;

    @ValidateOnClick
    @BindView(R.id.saveNewUserButton)
    Button buttonSaveNewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        ButterKnife.bind(this);
        NewUserActivityFieldsValidation.init(this);
        setupToolbar();
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

    @OnValidationSuccess
    public void saveNewUser() {
        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();

        Log.d("Profiler", "Tempo de execucao = " + (end - start) + "ms");

        try {
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
        } catch (Exception ex) {
            Log.e("NewUserActivity", ex.getMessage());
            Snackbar.make(toolbar, "Não foi possível salvar o usuário.", Snackbar.LENGTH_LONG).show();
        }
    }
}