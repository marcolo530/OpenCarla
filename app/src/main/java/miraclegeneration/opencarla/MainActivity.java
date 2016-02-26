package miraclegeneration.opencarla;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import miraclegeneration.opencarla.fragment.MainFragment;
import miraclegeneration.opencarla.fragment.accountFragment;
//google map implement new interface OnMapReadyCallback
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    //fragment class for map  word support is because extends from appCompatActivity -> fragment
    SupportMapFragment sMapFragment;

    UserLocal userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //create new instance for the google map
        sMapFragment = SupportMapFragment.newInstance();
        setContentView(R.layout.activity_main);
        userLocalStore =new UserLocal(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //Change the value  of header in nav bar
        UserLocal userLocal =new UserLocal(this);
        User user ;
        user= userLocal.getLoggedUser();
        TextView showUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.showUserName);
        showUserName.setText("歡迎！"+ user.username);
        ///////////////////////////////////////////////////////////
        navigationView.setNavigationItemSelectedListener(this);

        //call fragment manager to show fragment
        FragmentManager fm = getFragmentManager();
        //first paramter is the content, second is the main frament created
        fm.beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();

        //call the method of get the map (require pass a callback function which is the  onMapReady
        sMapFragment.getMapAsync(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //get fragment manager
        FragmentManager fm = getFragmentManager();

        // create google map fragment manager which for extends from fragment
        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //if no this one, the map will be visible for every fragment
        if(sMapFragment.isAdded())
            sFm.beginTransaction().hide(sMapFragment).commit();//hide the google map

        if (id == R.id.account) {
            // jump to the account page
            fm.beginTransaction().replace(R.id.content_frame, new accountFragment()).commit();
        } else if (id == R.id.map) {
            if(!sMapFragment.isAdded()) //if the map is not added
                sFm.beginTransaction().add(R.id.map, sMapFragment).commit();//add fragment object to the content_main
            else//if the map is added
                sFm.beginTransaction().show(sMapFragment).commit();// show the  map

        } else if (id == R.id.nav_slideshow) {

            startActivity(new Intent(MainActivity.this , login.class));

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if (id == R.id.logout) {
        userLocalStore.clearUserData();
        userLocalStore.setUserLoggedIn(false);
            startActivity(new Intent(MainActivity.this, login.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //when the map loaded execute this method only call once when the map ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //do negation here, add location




        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {


        }

        googleMap.setMyLocationEnabled(true);

        ///Set current position and make it as a marker
        LocationManager locationManager = (LocationManager) getSystemService
                (Context.LOCATION_SERVICE);
        Location getLastLocation = locationManager.getLastKnownLocation
                (LocationManager.PASSIVE_PROVIDER);
        double currentLongitude = getLastLocation.getLongitude();
        double currentLatitude = getLastLocation.getLatitude();


        LatLng currentPosition = new LatLng(currentLatitude, currentLongitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 13));

        googleMap.addMarker(new MarkerOptions()
                .title("你的位置")

                .position(currentPosition));

    }




}
