package appewtc.masterung.snrurun;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //Explicit
    private GoogleMap mMap;
    private double snruLatADouble = 17.189813,
            snruLngADouble = 104.087387;
    private LocationManager locationManager;
    private Criteria criteria;
    private double myLatADouble, myLngADouble;
    private boolean gpsABoolean, networkABoolean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_design);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Setup Location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

    }   // Main Method

    public Location myFindLocation(String strProvider, String strError) {

        Location location = null;

        if (locationManager.isProviderEnabled(strProvider)) {

            locationManager.requestLocationUpdates(strProvider, 1000, 10, (android.location.LocationListener) locationListener);
            location = locationManager.getLastKnownLocation(strProvider);

        } else {
            Log.d("test", "my Error ==> " + strError);
        }

        return location;
    }


    //Create Inner Class
    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            myLatADouble = location.getLatitude();
            myLngADouble = location.getLongitude();

        }
    }; // Location



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

       //Setup for สกล
        LatLng snruLatLng = new LatLng(snruLatADouble, snruLngADouble);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(snruLatLng, 15));

    }   // onMapReady

}   // Main Class
