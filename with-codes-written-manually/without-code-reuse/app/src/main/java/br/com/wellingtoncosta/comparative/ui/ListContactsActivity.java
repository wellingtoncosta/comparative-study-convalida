package br.com.wellingtoncosta.comparative.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import br.com.wellingtoncosta.comparative.R;
import br.com.wellingtoncosta.comparative.ui.adapter.ContactAdapter;
import br.com.wellingtoncosta.comparative.domain.User;
import br.com.wellingtoncosta.comparative.util.SharedPreferencesUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * @author Wellington Costa on 14/09/2017.
 */
public class ListContactsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contacts);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setupSwipeRefreshLayout();
        setupRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadContacts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                loadContacts();
            }
        });
    }

    public void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public void loadContacts() {
        int userId = SharedPreferencesUtils.getUserLogged(this);
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).equalTo("id", userId).findFirst();

        if (user != null) {
            recyclerView.setAdapter(new ContactAdapter(user.getContacts()));
        }

        swipeRefreshLayout.setRefreshing(false);
    }

    @OnClick(R.id.newContactButton)
    public void newContact() {
        startActivity(new Intent(this, NewContactActivity.class));
    }

    public void logout() {
        SharedPreferencesUtils.setUserLogged(this, 0);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}