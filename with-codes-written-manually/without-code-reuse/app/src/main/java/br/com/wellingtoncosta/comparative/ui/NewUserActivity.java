package br.com.wellingtoncosta.comparative.ui;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import br.com.wellingtoncosta.comparative.R;
import br.com.wellingtoncosta.comparative.domain.User;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        ButterKnife.bind(this);
        setupToolbar();
        addEmailFieldTextChangedListener();
        addNameFieldTextChangedListener();
        addPasswordFieldTextChangedListener();
        addConfirmPasswordFieldTextChangedListener();
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
            long start = System.currentTimeMillis();
            boolean fieldsAreValid = fieldsAreValid();
            long end = System.currentTimeMillis();

            Log.d("Profiler", "Tempo de execucao = " + (end - start) + "ms");

            if (fieldsAreValid) {
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

    public boolean fieldsAreValid() {
        validateNameField(nameField.getText().toString());
        validateEmailField(emailField.getText().toString());
        validatePasswordField(passwordField.getText().toString());
        validateConfirmPasswordField(confirmPasswordField.getText().toString());
        return !nameLayout.isErrorEnabled()
                && !emailLayout.isErrorEnabled()
                && !passwordLayout.isErrorEnabled()
                && !confirmPasswordLayout.isErrorEnabled();
    }

    public void validateNameField(String text) {
        boolean isValid = text != null && !text.isEmpty();
        String errorMessage = !isValid ? getString(R.string.name_required) : null;
        nameLayout.setErrorEnabled(!isValid);
        nameLayout.setError(errorMessage);
    }

    public void validateEmailField(String text) {
        boolean isValid = Patterns.EMAIL_ADDRESS.matcher(text).matches();
        String errorMessage = !isValid ? getString(R.string.invalid_email) : null;
        emailLayout.setErrorEnabled(!isValid);
        emailLayout.setError(errorMessage);
    }

    public void validatePasswordField(String text) {
        boolean isValid = text != null && !text.isEmpty();
        String errorMessage = !isValid ? getString(R.string.password_required) : null;
        passwordLayout.setErrorEnabled(!isValid);
        passwordLayout.setError(errorMessage);
    }

    public void validateConfirmPasswordField(String text) {
        String passwordText = passwordField.getText().toString();
        boolean isNotEmpty = text != null && !text.isEmpty();
        boolean isValid = isNotEmpty && passwordText.equals(text);
        String errorMessage = !isValid ? getString(R.string.different_password) : null;
        confirmPasswordLayout.setErrorEnabled(!isValid);
        confirmPasswordLayout.setError(errorMessage);
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

    public void addPasswordFieldTextChangedListener() {
        passwordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = String.valueOf(charSequence);
                validatePasswordField(text);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    public void addConfirmPasswordFieldTextChangedListener() {
        confirmPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = String.valueOf(charSequence);
                validateConfirmPasswordField(text);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }
}