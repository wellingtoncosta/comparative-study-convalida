package br.com.wellingtoncosta.experimento.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import br.com.wellingtoncosta.experimento.R;
import br.com.wellingtoncosta.experimento.util.SharedPreferencesUtils;
import io.realm.Realm;

/**
 * @author Wellington Costa on 14/09/2017.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Realm.init(this);

        Intent intent = new Intent();

        if(SharedPreferencesUtils.hasUserLogged(this)) {
            intent.setClass(this, ListContactsActivity.class);
        } else {
            intent.setClass(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
