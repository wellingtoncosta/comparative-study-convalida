package br.com.wellingtoncosta.comparative.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import br.com.wellingtoncosta.comparative.R;
import br.com.wellingtoncosta.comparative.domain.User;
import br.com.wellingtoncosta.comparative.util.SharedPreferencesUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * @author Wellington Costa on 14/09/2017.
 */
public class  LoginActivity extends AppCompatActivity implements Validator.ValidationListener {

    private Validator validator;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Email(messageResId = R.string.invalid_email)
    @BindView(R.id.emailLayout)
    TextInputLayout emailLayout;

    @BindView(R.id.emailField)
    EditText emailField;

    @NotEmpty(messageResId = R.string.password_required)
    @BindView(R.id.passwordLayout)
    TextInputLayout passwordLayout;

    @BindView(R.id.passwordField)
    EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        validator = new Validator(this);
        validator.registerAdapter(TextInputLayout.class, new TextInputLayoutAdapter());
        validator.setValidationListener(this);
    }

    @OnClick(R.id.loginButton)
    public void login() {
        validator.validate();
    }

    @OnClick(R.id.registerNewUserButton)
    public void registerNewUser() {
        startActivity(new Intent(this, NewUserActivity.class));
        finish();
    }

    @Override
    public void onValidationSucceeded() {
        Realm realm = Realm.getDefaultInstance();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        User user = realm.where(User.class).equalTo("email", email).findFirst();

        if (user == null) {
            Snackbar.make(toolbar, "Usuário não encontrado.", Snackbar.LENGTH_LONG).show();
            return;
        }

        boolean emailIsNotEquals = !user.getEmail().equals(email);
        boolean passwordIsNotEquals = !user.getPassword().equals(password);

        if (emailIsNotEquals || passwordIsNotEquals) {
            Snackbar.make(toolbar, "E-mail ou senha inválido.", Snackbar.LENGTH_LONG).show();
            return;
        }

        SharedPreferencesUtils.setUserLogged(this, user.getId());
        startActivity(new Intent(this, ListContactsActivity.class));
        finish();
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