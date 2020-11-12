package com.example.climateawarenessapplication.Core;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.climateawarenessapplication.Blog.BlogFragment;
import com.example.climateawarenessapplication.Blog.BotProtectionFrag;
import com.example.climateawarenessapplication.Campaigns.CampaignsAndStatsFragment;
import com.example.climateawarenessapplication.HomeDashboard.HomeFragment;
import com.example.climateawarenessapplication.Login.LoginFragment;
import com.example.climateawarenessapplication.News.Adapter;
import com.example.climateawarenessapplication.News.NewsFragment;
import com.example.climateawarenessapplication.Profile.ProfileFragment;
import com.example.climateawarenessapplication.Models.Article;
import com.example.climateawarenessapplication.R;
import com.example.climateawarenessapplication.Register.RegisterFragment;
import com.example.climateawarenessapplication.Settings.SettingsFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LockDrawerInterface {

    //    public static final String API_KEY = "4989e46f65f9481da014e0e06c495687";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private String TAG = MainActivity.class.getSimpleName();
    //  private TextView topHeadline;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DrawerLayout drawer;
    //navigation bar code
    private ActionBarDrawerToggle myToggle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideItem();



        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
            navigationView.setCheckedItem(R.id.login);
        }


        myToggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(myToggle);
        myToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    private void hideItem() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.login).setVisible(false);
        nav_Menu.findItem(R.id.register).setVisible(false);
    }

    //Opens up the navgation bar by clicking on the burger
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (myToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.news:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewsFragment()).commit();
                break;
            case R.id.campaigns:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CampaignsAndStatsFragment()).commit();
                break;
            case R.id.blog:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BotProtectionFrag()).commit();
                break;
            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;
            case R.id.login:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
                break;
            case R.id.register:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RegisterFragment()).commit();
                break;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.logout:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //this method controls whether the drawer is locked or unlocked
    @Override
    public void setDrawerLocked(boolean enabled) {

            if (enabled) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        }


}
