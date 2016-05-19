package appewtc.masterung.snrurun;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //Explicit
    private GoogleMap mMap;
    private double snruLatADouble = 17.189813,
            snruLngADouble = 104.087387;
    private LocationManager locationManager;
    private Criteria criteria;
    private double myLatADouble, myLngADouble;
    private boolean gpsABoolean, networkABoolean;
    private String[] userStrings;

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

        //Receive from Intent
        userStrings = getIntent().getStringArrayExtra("User");

    }   // Main Method

    //Inner Class
    public class SynLocation extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url("http://swiftcodingthai.com/snru/get_user.php").build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                return null;
            }

            //return null;
        }   // doInBack


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mMap.clear();

            try {

                JSONArray jsonArray = new JSONArray(s);

                String[] nameStrings = new String[jsonArray.length()];
                String[] latStrings = new String[jsonArray.length()];
                String[] lngStrings = new String[jsonArray.length()];
                String[] avataStrings = new String[jsonArray.length()];

                for (int i=0;i<jsonArray.length();i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    nameStrings[i] = jsonObject.getString("Name");
                    latStrings[i] = jsonObject.getString("Lat");
                    lngStrings[i] = jsonObject.getString("Lng");
                    avataStrings[i] = jsonObject.getString("Avata");

                    LatLng latLng = new LatLng(Double.parseDouble(latStrings[i]),
                            Double.parseDouble(lngStrings[i]));
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(findIcon(avataStrings[i])))
                    .title(nameStrings[i]));


                }   // for

            } catch (Exception e) {
                e.printStackTrace();
            }

        }   // onPost

    }   // SynLocation Class

    private int findIcon(String avataString) {

        int intIcon = R.drawable.bird48;

        switch (Integer.parseInt(avataString)) {
            case 0:
                intIcon = R.drawable.bird48;
                break;
            case 1:
                intIcon = R.drawable.doremon48;
                break;
            case 2:
                intIcon = R.drawable.kon48;
                break;
            case 3:
                intIcon = R.drawable.nobita48;
                break;
            case 4:
                intIcon = R.drawable.rat48;
                break;
        }

        return intIcon;
    }

    @Override
    protected void onResume() {
        super.onResume();

       // locationManager.removeUpdates((android.location.LocationListener) locationListener);
        myLatADouble = snruLatADouble;
        myLngADouble = snruLngADouble;

        Location networkLocation = myFindLocation(LocationManager.NETWORK_PROVIDER, "ไม่ได้ต่อเน็ต");
        if (networkLocation != null) {
            myLatADouble = networkLocation.getLatitude();
            myLngADouble = networkLocation.getLongitude();
        }

        Location gpsLocation = myFindLocation(LocationManager.GPS_PROVIDER, "ไม่มี GPS");
        if (gpsLocation != null) {
            myLatADouble = gpsLocation.getLatitude();
            myLngADouble = gpsLocation.getLongitude();
        }


    }

    @Override
    protected void onStop() {
        super.onStop();

        locationManager.removeUpdates(locationListener);

    }

    public Location myFindLocation(String strProvider, String strError) {

        Location location = null;

        if (locationManager.isProviderEnabled(strProvider)) {

            locationManager.requestLocationUpdates(strProvider, 1000, 10, locationListener);
            location = locationManager.getLastKnownLocation(strProvider);

        } else {
            Log.d("test", "my Error ==> " + strError);
        }

        return location;
    }


    //Create Inner Class
   public android.location.LocationListener locationListener = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            myLatADouble = location.getLatitude();
            myLngADouble = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

       //Setup for สกล
        LatLng snruLatLng = new LatLng(snruLatADouble, snruLngADouble);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(snruLatLng, 15));

        //My Loop
        myLoop();

    }   // onMapReady

    private void myLoop() {

        Log.d("18May16", "myLat = " + myLatADouble);
        Log.d("18May16", "myLng = " + myLngADouble);

        updateLocation();

        createAllMarker();




        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                myLoop();
            }
        }, 3000);

    }   // myLoop

    private void createAllMarker() {

        SynLocation synLocation = new SynLocation();
        synLocation.execute();

    }   // createAllMarker

    private void updateLocation() {

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormEncodingBuilder()
                .add("isAdd", "true")
                .add("id", userStrings[0])
                .add("Lat", Double.toString(myLatADouble))
                .add("Lng", Double.toString(myLngADouble))
                .build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url("http://swiftcodingthai.com/snru/edit_location.php").post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });



    }   // updateLocation

}   // Main Class
