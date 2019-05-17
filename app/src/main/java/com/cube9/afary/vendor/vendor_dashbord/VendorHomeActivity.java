package com.cube9.afary.vendor.vendor_dashbord;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cube9.afary.R;
import com.cube9.afary.helperClass.CustomUtils;
import com.cube9.afary.helperClass.PrefManager;
import com.cube9.afary.search_address.SearchAddressActivity;
import com.cube9.afary.user.home.HomeActivity;
import com.cube9.afary.user.home.HomeFragment;
import com.cube9.afary.vendor.vendor_dashbord.view.VendorHomeFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.barcode.Barcode;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VendorHomeActivity extends AppCompatActivity implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, VendorHomeFragment.OnFragmentInteractionListener {
    @BindView(R.id.fl_main_vendor)
    FrameLayout fl_main_vendor;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    private LocationManager locationManager;
    String lat="",lan="";
    @BindView(R.id.tv_location)
    TextView tv_location;
    FragmentManager fragmentManager;
    private static final String TAG_HOME = "home";
    private static final String TAG_ACCOUNT = "account";
    private static final String TAG_NOTI = "noti";
    private static final String TAG_CART = "cart";
    public static final String TAG_HOME_SERVICE = "home_service";
    public static int navItemIndex = 0;
    public static String CURRENT_TAG = TAG_HOME;
    private boolean shouldLoadHomeFragOnBackPress = true;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;

    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_home);


        ButterKnife.bind(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }



        checkLocation();
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navigation.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(1);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(VendorHomeActivity.this)
                .inflate(R.layout.notification_badge, itemView, true);

        TextView tv_noti_itm = badge.findViewById(R.id.notifications);


        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
            View activeLabel = item.findViewById(R.id.largeLabel);
            if (activeLabel instanceof TextView) {
                activeLabel.setPadding(0, 0, 0, 0);
            }
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
           loadHomeFragment();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;


            fragmentManager = getSupportFragmentManager();

            switch (item.getItemId()) {
                case R.id.nav_home:


                    navItemIndex = 0;
                    CURRENT_TAG = TAG_HOME;
                    break;
                case R.id.nav_account:
                    //  toolbar.setTitle("My Gifts");
                    navItemIndex = 3;
                    CURRENT_TAG = TAG_ACCOUNT;
                    break;



                case R.id.nav_noti:
                    navItemIndex = 1;
                    CURRENT_TAG = TAG_CART;
                    break;
                case R.id.nav_cart:
                    navItemIndex = 2;
                    CURRENT_TAG = TAG_NOTI;
                    break;


            }
            loadHomeFragment();
            return true;
        }
    };

    public void loadHomeFragment() {
       /* Fragment fragment = getHomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.rv_main, fragment, CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();
*/


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Fragment curFrag = getSupportFragmentManager().getPrimaryNavigationFragment();
        if (curFrag != null) {
            fragmentTransaction.detach(curFrag);
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CURRENT_TAG);
        if (fragment == null) {
            fragment = getHomeFragment();
            fragmentTransaction.add(R.id.fl_main_vendor, fragment, CURRENT_TAG);
        } else {
            fragmentTransaction.attach(fragment);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();

    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                return new VendorHomeFragment();
           /* case 1:
                return null;//new FavouriteFragment();
            case 2:
                return null;
            case 3:
                return null;
*/
            default:
                return new VendorHomeFragment();
        }
    }
    private Boolean exit = false;
    @Override
    public void onBackPressed() {

        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
        if (CURRENT_TAG==TAG_HOME)
        {
            if (exit) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                startActivity(intent);
                finish();
                System.exit(0);
            } else {
                CustomUtils.showToast("Press Back again to Exit.",VendorHomeActivity.this,MDToast.TYPE_INFO);
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);

            }
            return;
        }


        super.onBackPressed();



    }
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("sdsds", "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("sdsds", "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        getAddress(location.getLatitude(),location.getLongitude());
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void getAddress(double latitude, double longitude)
    {
        Geocoder geocoder = new Geocoder(VendorHomeActivity.this, Locale.getDefault());

        List<Address> addresses  = null;
        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String zip = addresses.get(0).getPostalCode();
        String country = addresses.get(0).getCountryName();
        String subLocality=addresses.get(0).getSubLocality();
        tv_location.setText(subLocality+", "+city);
        PrefManager.getInstance(VendorHomeActivity.this).setLocation(subLocality+", "+city);
    }

    @OnClick(R.id.tv_location)
    public void selectAddress()
    {

        startActivityForResult(new Intent(VendorHomeActivity.this,SearchAddressActivity.class),1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {

            if (!data.getStringExtra("intent_from").equals(""))
            {
                getLocationFromAddress(data.getStringExtra("intent_from"));
                Toast.makeText(this, ""+data.getStringExtra("intent_from"), Toast.LENGTH_SHORT).show();
                tv_location.setText(data.getStringExtra("intent_from"));
                PrefManager.getInstance(VendorHomeActivity.this).setLocation(data.getStringExtra("intent_from"));
            }



        }

    }


    @SuppressLint("RestrictedApi")
    public Barcode.GeoPoint getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        Barcode.GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }


            Log.i("addresssssssss", "" + address.toString());
            if (address.size() > 0) {
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();
                lat = "" + location.getLatitude();
                lan = "" + location.getLongitude();
                //Toast.makeText(this, ""+location.getLatitude(), Toast.LENGTH_SHORT).show();
                p1 = new Barcode.GeoPoint((double) (location.getLatitude() * 1E6),
                        (double) (location.getLongitude() * 1E6));
            }
            /**/


        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
