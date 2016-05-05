package com.example.sargis_kh.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.example.sargis_kh.fragment.CameraFragment;
import com.example.sargis_kh.fragment.CompaniesFragment;
import com.example.sargis_kh.fragment.CompanyDialogFragment;
import com.example.sargis_kh.helper_class.CalculationHelper;
import com.example.sargis_kh.helper_class.JsonData;
import com.example.sargis_kh.model.Company;
import com.example.sargis_kh.model.Vector2;
import com.example.sargis_kh.studioone.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener, CompanyDialogFragment.OnCompanyPositionChangedListener {

    private static final String MAIN_ACTIVITY_LOG = "MAIN_ACTIVITY_LOG";

    private static final int COMPANIES_FRAGMENT_INDEX = 0;
    private static SensorManager sensorManager;
    private Sensor sensor;

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    private Vector2 currentLocation;
    private Vector2 screenSize;

    private ArrayList<Company> companies;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        companies = JsonData.readJSON();
        screenSize = getScreenSize();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        registerLocationService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //register the sensor listener
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "ORIENTATION Sensor not found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        //unregister the sensor listener
        sensorManager.unregisterListener(this);
        sensor = null;
        super.onStop();
    }

    private void registerLocationService () {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.e(MAIN_ACTIVITY_LOG, "currentLocation: " + location.getLongitude());
                currentLocation = new Vector2(location.getLongitude(),location.getLatitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
    }

    private Vector2 getScreenSize() {
        // get screen size
        Display display = this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return new Vector2(width, height);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //if current location is not found, return
        if (currentLocation == null) {
            Log.d(MAIN_ACTIVITY_LOG, "currentLocation == null");
            return;
        }

        //if sensor is unreliable, return
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }

        // else it will output the Roll, Pitch and Yawn values
        double OrientationX = (double)event.values[2]; // Roll
        double OrientationY = (double)event.values[1]; // Pitch

        // angle between the magnetic north direction
        // 0=North, 90=East, 180=South, 270=West
        double azimuth = (double)event.values[0]; // Yaw // OrientationZ

        // TODO must be considered the case when the phone is rotated
        if (OrientationY < -135 || OrientationY > -45) {
            ((CameraFragment)fragmentList.get(1)).hideAllLogos();
            return;
        } else {
//            Log.d(MAIN_ACTIVITY_LOG, "lat: " + companies.get(0).getLatitude());

            for (int i = 0; i <companies.size(); i++) {
                Vector2 logoPosition = CalculationHelper.getLogoPosition(screenSize, companies.get(i), currentLocation, azimuth);
                if (logoPosition != null) {
                    ((CameraFragment) fragmentList.get(1)).showLogoAtPosition(companies.get(i), logoPosition);
                } else {
                    ((CameraFragment) fragmentList.get(1)).hideLogo(companies.get(i));
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // Called when company position changed from Dialog Fragment
    @Override
    public void CompanyPositionChanged(long companyId, double newLatitude, double newLongitude) {
        for (int i = 0; i < companies.size(); i++) {
            if (companies.get(i).getID() == companyId) {
                companies.get(i).setLatitude(newLatitude);
                companies.get(i).setLongitude(newLongitude);
                ((CompaniesFragment) fragmentList.get(COMPANIES_FRAGMENT_INDEX)).setCompanies(companies);
                break;
            }
        }
    }

    // Setup view pager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CompaniesFragment(), "Companies");
        adapter.addFragment(new CameraFragment(), "Camera");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == COMPANIES_FRAGMENT_INDEX) {
                ((CompaniesFragment) fragmentList.get(position)).setCompanies(companies);
            } else {
                ((CameraFragment) fragmentList.get(position)).setCompanies(getApplicationContext(), companies);
            }
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}