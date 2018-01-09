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
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

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
public class NewUserActivity extends AppCompatActivity implements Validator.ValidationListener{

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

    @Password(messageResId = R.string.password_required)
    @BindView(R.id.passwordLayout)
    TextInputLayout passwordLayout;

    @BindView(R.id.passwordField)
    EditText passwordField;

    @ConfirmPassword(messageResId = R.string.different_password)
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

        validator = new Validator(this);
        validator.registerAdapter(TextInputLayout.class, new TextInputLayoutAdapter());
        validator.setValidationListener(this);
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
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
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