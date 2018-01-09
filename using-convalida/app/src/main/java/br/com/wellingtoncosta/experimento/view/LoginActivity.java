package br.com.wellingtoncosta.experimento.view;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;

import br.com.wellingtoncosta.experimento.R;
import br.com.wellingtoncosta.experimento.domain.User;
import br.com.wellingtoncosta.experimento.util.SharedPreferencesUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import convalida.annotations.EmailValidation;
import convalida.annotations.PasswordValidation;
import convalida.library.Convalida;
import convalida.library.ConvalidaValidator;
import convalida.library.validation.Patterns;
import io.realm.Realm;

/**
 * @author Wellington Costa on 14/09/2017.
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @EmailValidation(R.string.invalid_email)
    @BindView(R.id.emailLayout)
    TextInputLayout emailLayout;

    @BindView(R.id.emailField)
    EditText emailField;

    @PasswordValidation(
            min = 1,
            errorMessage = R.string.password_required
    )
    @BindView(R.id.passwordLayout)
    TextInputLayout passwordLayout;

    @BindView(R.id.passwordField)
    EditText passwordField;

    private ConvalidaValidator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        validator = Convalida.init(this);
    }

    @OnClick(R.id.loginButton)
    public void login() {
        long start = System.currentTimeMillis();
        boolean fieldsAreValid = validator.validateFields();
        long end = System.currentTimeMillis();

        Log.d("Profiler", "Tempo de execucao = " + (end - start) + "ms");

        if(fieldsAreValid) {
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
    }

    @OnClick(R.id.registerNewUserButton)
    public void registerNewUser() {
        startActivity(new Intent(this, NewUserActivity.class));
        finish();
    }
}