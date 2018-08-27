package com.example.shengxinheng.myweather.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ViewFlipper;

import com.example.shengxinheng.myweather.R;
import com.example.shengxinheng.myweather.activity.adapter.CityViewAdapter;
import com.example.shengxinheng.myweather.activity.adapter.WeatherViewAdapter;
import com.example.shengxinheng.myweather.datamodel.Location;
import com.example.shengxinheng.myweather.datamodel.Weather;
import com.example.shengxinheng.myweather.helper.WeatherReceiver;
import com.example.shengxinheng.myweather.modelview.LocationsModel;
import com.example.shengxinheng.myweather.modelview.WeatherModel;
import com.example.shengxinheng.myweather.weather.WeatherService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, WeatherReceiver, SearchDialogFragment.SearchDialogListener,
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
    private WeatherService weatherService;
    private ViewFlipper viewFlipper;
    private WeatherModel weatherModel;
    private RecyclerView weatherRecyclerView, cityRecylerView;
    private WeatherViewAdapter weatherAdapter = null;
    private LocationsModel locationsModel;
    private CityViewAdapter cityAdapter = null;
    private NavigationView navigationView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private MenuItem currentItem;

    private List<Location> locations = new ArrayList<>();
    private Map<Integer, Location> locationMap = new HashMap<>();
    private List<Integer> locationIndex = new ArrayList<>();
    private Location local = new Location("Local");

    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int ACCESS_FIN_LOCATION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchDialogFragment dialog = new SearchDialogFragment();
                dialog.show(getSupportFragmentManager(), "search dialog");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        weatherService = new WeatherService(this);
        locationsModel = ViewModelProviders.of(this).get(LocationsModel.class);
        weatherModel = ViewModelProviders.of(this).get(WeatherModel.class);
        weatherModel.getWeather().observe(this, new Observer<Weather>() {
            @Override
            public void onChanged(@Nullable final Weather weather) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weatherAdapter == null) {
                            weatherAdapter = new WeatherViewAdapter(weather);
                            weatherRecyclerView.setAdapter(weatherAdapter);
                        } else {
                            weatherAdapter = new WeatherViewAdapter(weather);
                            weatherRecyclerView.setAdapter(weatherAdapter);
                        }
                        if(currentItem.getItemId() == R.id.local) {
                            collapsingToolbarLayout.setTitle(weather.getLocation().getShortName());
                            currentItem.setTitle(weather.getLocation().getShortName());
                        }
                    }
                });
            }
        });
        viewFlipper = findViewById(R.id.vf);
        weatherRecyclerView = findViewById(R.id.weather_recycler_view);
        weatherRecyclerView.setHasFixedSize(true);
        LinearLayoutManager wLayoutManager = new LinearLayoutManager(this);
        weatherRecyclerView.setLayoutManager(wLayoutManager);
        cityRecylerView = findViewById(R.id.city_recycler_view);
        cityRecylerView.setHasFixedSize(true);
        LinearLayoutManager cLayoutManager = new LinearLayoutManager(this);
        cityRecylerView.setLayoutManager(cLayoutManager);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int index = viewHolder.getAdapterPosition();
                int id = locationIndex.remove(index);
                locationMap.remove(id);
                locations.remove(index);
                navigationView.getMenu().removeItem(id);
                cityAdapter.reload(locations);
                cityAdapter.notifyDataSetChanged();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(cityRecylerView);

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //item.setChecked(true);
        currentItem = item;
        if (id == R.id.manage) {
            if (cityAdapter == null) {
                cityAdapter = new CityViewAdapter(locations);
                cityRecylerView.setAdapter(cityAdapter);
            } else {
                cityAdapter.reload(locations);
                cityAdapter.notifyDataSetChanged();
            }
            viewFlipper.setDisplayedChild(1);
            collapsingToolbarLayout.setTitle("Management");
        } else if (id == R.id.local) {
            if (fusedLocationProviderClient == null) {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_FIN_LOCATION_CODE);
            } else {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<android.location.Location>() {
                    @Override
                    public void onSuccess(android.location.Location location) {
                        if(location == null){
                            InitializeLocation();
                            Log.i("onSuccess", "1");
                        }
                        else {
                            weatherService.fetchWeather(location);
                            Log.i("onSuccess", "2");
                        }
                    }
                });
                viewFlipper.setDisplayedChild(0);
            }
        } else {
            weatherService.fetchWeather(locationMap.get(id));
            viewFlipper.setDisplayedChild(0);
            collapsingToolbarLayout.setTitle(locationMap.get(id).getShortName());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void receive(Object object) {
        if (object instanceof Weather)
            weatherModel.getWeather().setValue((Weather) object);
        else if (object instanceof List) {
            locationsModel.getLocations().setValue((List) object);
        }
    }

    @Override
    public void onDialogSearchClick(DialogFragment dialog, String text) {
        weatherService.fetchLocations(text);
    }

    @Override
    public void onDialogSelectClick(DialogFragment dialog, final Location location) {
        locationsModel.getLocations().setValue(new ArrayList<Location>());
        MenuItem item = navigationView.getMenu().add(R.id.main_group, View.generateViewId(), 0, location.getShortName())
                .setIcon(R.drawable.ic_menu_send);
        locations.add(location);
        locationMap.put(item.getItemId(), location);
        locationIndex.add(item.getItemId());
        onNavigationItemSelected(item);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FIN_LOCATION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<android.location.Location>() {
                        @Override
                        public void onSuccess(android.location.Location location) {
                            if(location == null){
                                InitializeLocation();
                                Log.i("onRequestPermissionsResult", "1");
                            }
                            else {
                                weatherService.fetchWeather(location);
                                Log.i("onRequestPermissionsResult", "2");
                            }
                        }
                    });
                    viewFlipper.setDisplayedChild(0);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private void InitializeLocation() {
        GoogleApiClient mGoogleApiClient;

        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addConnectionCallbacks(MainActivity.this)
                .addOnConnectionFailedListener(MainActivity.this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        final LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (android.location.Location location : locationResult.getLocations()) {
                    if (location != null) {
                        weatherService.fetchWeather(location);
                        Log.i("onConnected", "1");
                    }
                }
                fusedLocationProviderClient.removeLocationUpdates(this);
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
