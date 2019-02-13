package helo.niti.movies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import helo.niti.movies.fragments.FragmentFavorites;
import helo.niti.movies.fragments.FragmentGenreAction;
import helo.niti.movies.fragments.FragmentGenreAnimation;
import helo.niti.movies.fragments.FragmentGenreComedy;
import helo.niti.movies.fragments.FragmentGenreDramas;
import helo.niti.movies.fragments.FragmentGenreHorror;
import helo.niti.movies.fragments.FragmentGenreScienceFiction;
import helo.niti.movies.fragments.FragmentPopular;
import helo.niti.movies.fragments.FragmentTopRated;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Navigation Drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_popular:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new FragmentPopular()).commit();
                        break;
                    case R.id.nav_top_rated:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new FragmentTopRated()).commit();
                        break;
                    case R.id.nav_my_favorites:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new FragmentFavorites()).commit();
                        break;
                    case R.id.nav_comedy:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new FragmentGenreComedy()).commit();
                        break;
                    case R.id.nav_drama:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new FragmentGenreDramas()).commit();
                        break;
                    case R.id.nav_science_fiction:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new FragmentGenreScienceFiction()).commit();
                        break;
                    case R.id.nav_action:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new FragmentGenreAction()).commit();
                        break;
                    case R.id.nav_horror:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new FragmentGenreHorror()).commit();
                        break;
                    case R.id.nav_animation:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new FragmentGenreAnimation()).commit();
                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Main fragment if no item is clicked
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new FragmentPopular()).commit();
            navigationView.setCheckedItem(R.id.nav_popular);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
