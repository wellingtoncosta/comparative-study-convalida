package br.com.wellingtoncosta.comparative.ui;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import br.com.wellingtoncosta.comparative.R;
import br.com.wellingtoncosta.comparative.domain.User;
import br.com.wellingtoncosta.comparative.util.RealmUtils;
import br.com.wellingtoncosta.comparative.util.SharedPreferencesUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import convalida.annotations.ConfirmPasswordValidation;
import convalida.annotations.EmailValidation;
import convalida.annotations.NotEmptyValidation;
import convalida.annotations.PasswordValidation;
import convalida.library.Convalida;
import convalida.library.ConvalidaValidator;
import convalida.library.Patterns;
import io.realm.Realm;

/**
 * @author Wellington Costa on 14/09/2017.
 */
public class NewUserActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @NotEmptyValidation(R.string.name_required)
    @BindView(R.id.nameLayout)
    TextInputLayout nameLayout;

    @BindView(R.id.nameField)
    EditText nameField;

    @EmailValidation(R.string.invalid_email)
    @BindView(R.id.emailLayout)
    TextInputLayout emailLayout;

    @BindView(R.id.emailField)
    EditText emailField;

    @PasswordValidation(
            min = 1,
            pattern = Patterns.NUMERIC_ONLY,
            errorMessage = R.string.password_required
    )
    @BindView(R.id.passwordLayout)
    TextInputLayout passwordLayout;

    @BindView(R.id.passwordField)
    EditText passwordField;

    @ConfirmPasswordValidation(R.string.different_password)
    @BindView(R.id.confirmPasswordLayout)
    TextInputLayout confirmPasswordLayout;

    @BindView(R.id.confirmPasswordField)
    EditText confirmPasswordField;

    private ConvalidaValidator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        ButterKnife.bind(this);
        setupToolbar();

        validator = Convalida.init(this);
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
        long start = System.currentTimeMillis();
        boolean fieldsAreValid = validator.validateFields();
        long end = System.currentTimeMillis();

        Log.d("Profiler", "Tempo de execucao = " + (end - start) + "ms");

        if(fieldsAreValid) {
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
}