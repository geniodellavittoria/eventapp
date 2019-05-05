package ch.mobpro.eventapp.activity;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import ch.mobpro.eventapp.R;
import ch.mobpro.eventapp.adapter.CardListAdapter;
import ch.mobpro.eventapp.base.BaseActivity;
import ch.mobpro.eventapp.databinding.ActivityEventListBinding;
import ch.mobpro.eventapp.model.Event;
import ch.mobpro.eventapp.repository.SessionTokenRepository;
import ch.mobpro.eventapp.service.AuthInterceptor;
import ch.mobpro.eventapp.viewmodel.EventListViewModel;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends BaseActivity<ActivityEventListBinding>
        implements NavigationView.OnNavigationItemSelectedListener, CardListAdapter.OnEventListener {

    private String TAG = this.getClass().getSimpleName();
    public RecyclerView mRecView;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private List<Event> events;
    private EventListViewModel viewModel;
    private View.OnClickListener onItemClickListener;

    @Inject
    public SessionTokenRepository sessionTokenRepository;

    @Override
    protected int layoutRes() {
        return R.layout.activity_event_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EventListViewModel.class);
        dataBinding.setViewModel(viewModel);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> showCreateActivity());


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        setSetUserInformationOnNavigationView(navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        mRecView = findViewById(R.id.event_recycler_view);
        mRecView.setLayoutManager(new LinearLayoutManager(this));
        viewModel.getEvents().observe(this, e -> {
            this.events = e;
            CardListAdapter cardListAdapter = new CardListAdapter(events, this);
            mRecView.setAdapter(cardListAdapter);
        });
        viewModel.loadEvents();
        handleIntent(getIntent());
    }

    private void showCreateActivity() {
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }

    private void setSetUserInformationOnNavigationView(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.navigation_username_text);
        navUsername.setText(sessionTokenRepository.getUsername());

        TextView navName = headerView.findViewById(R.id.navigation_name_text);
        navName.setText(String.format("%s %s", sessionTokenRepository.getName(), sessionTokenRepository.getSurname()));
    }

    public void setItemClickListener(View.OnClickListener clickListener) {
        onItemClickListener = clickListener;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            List<Event> result = new ArrayList<>();
            for (Event event : this.events) {
                if (event.getName() != null && event.getName().toLowerCase().contains(query.toLowerCase())) {
                    result.add(event);
                }
            }
            if (!result.isEmpty()) {
                this.events = result;
            }
            CardListAdapter cardListAdapter = new CardListAdapter(events, this);
            mRecView.setAdapter(cardListAdapter);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.searchEventView);
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                viewModel.loadEvents();
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setQuery("", false);
                searchView.clearFocus();
                return true;
            }
        });
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onEventClick(int position) {
        Log.d(TAG, "onEventClick: clicked on" + position);
        Intent intent = new Intent(this, DetailEventActivity.class);
        Event selectedEvent = events.get(position);
        intent.putExtra("event", selectedEvent);
        startActivity(intent);
    }
}
