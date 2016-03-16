package miraclegeneration.opencarla.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import android.widget.Filterable;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.logging.LogRecord;

import miraclegeneration.opencarla.MainActivity;
import miraclegeneration.opencarla.R;

/**
 * Created by kenex on 10/1/2016.
 */
public class importFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerDragListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, RoutingListener {

    //auto complete variables
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutoCompleteAdapter mAdapter;
    private static final LatLngBounds BOUNDS_JAMAICA= new LatLngBounds(new LatLng(-57.965341647205726, 144.9987719580531),
            new LatLng(72.77492067739843, -9.998857788741589));
    //google map direction variables
    private ProgressDialog progressDialog;
    private ArrayList<Polyline> polylines;
    private int[] colors = new int[]{R.color.colorPrimaryDark,R.color.colorPrimary,R.color.wallet_primary_text_holo_light,R.color.colorAccent,R.color.primary_dark_material_light};
    //layout variables
    AutoCompleteTextView start_point;
    AutoCompleteTextView dest_point;
    EditText date,time;
    Button search, returnmyposition,timeButton,dateButton;
    Context thiscontext;
    //google map variables
    public MapFragment gMapFragment;
    static GoogleMap gMap;
    static LatLng currentPosition;
    static LatLng destPosition;
    public static View rootView;  //need to be static ,otherwise crash
    static Address addressName;
    static Marker startPosMarker;
    static Marker destPosMarker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //get context from MainActivity
        thiscontext = container.getContext();
        //only load the map one time
        try {
            if (rootView == null) {
                //load the layout
                rootView = inflater.inflate(R.layout.fragment_import, container, false);
                //get the map form layout ,id is fragmentmap
                gMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragmentmap);
                //call back function On map Ready
                gMapFragment.getMapAsync(this);
            }
        } catch (Exception e) {e.printStackTrace();}
        //initial layout variables
        start_point = (AutoCompleteTextView) rootView.findViewById(R.id.start_point);
        dest_point = (AutoCompleteTextView) rootView.findViewById(R.id.dest_point);
        date = (EditText) rootView.findViewById(R.id.date);
        time = (EditText) rootView.findViewById(R.id.time);
        search = (Button) rootView.findViewById(R.id.search);
        returnmyposition = (Button) rootView.findViewById(R.id.returnmyposition);
        //set listener
        search.setOnClickListener(this);
        timeButton = (Button) rootView.findViewById(R.id.timepicker);
        dateButton = (Button)rootView.findViewById(R.id.datepicker);
        timeButton.setOnClickListener(this);
        dateButton.setOnClickListener(this);
        returnmyposition.setOnClickListener(this);
        //google map direction initialize
        polylines = new ArrayList<>();
        //auto complete initialize
        mGoogleApiClient = new GoogleApiClient.Builder(thiscontext)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        MapsInitializer.initialize(thiscontext);
        //connect to the Place api
        mGoogleApiClient.connect();
        //create the adapter
        mAdapter = new PlaceAutoCompleteAdapter(thiscontext, android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_JAMAICA, null);
        //set adapter
        start_point.setAdapter(mAdapter);
        dest_point.setAdapter(mAdapter);
        //auto complete item checked listener
        start_point.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final PlaceAutoCompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                /*Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.*/
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (!places.getStatus().isSuccess()) {
                            // Request did not complete successfully
                            places.release();
                            return;
                        }
                        // Get the Place object from the buffer.
                        final Place place = places.get(0);
                        currentPosition = place.getLatLng();
                    }
                });
                //change the marker
                if (!start_point.getText().toString().equals("")) {
                    onSearch(start_point, "start_point"); //create the new marker with the new address
                }
            }
        });
        //auto complete item checked listener
        dest_point.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final PlaceAutoCompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                /*Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.*/
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (!places.getStatus().isSuccess()) {
                            // Request did not complete successfully
                            places.release();
                            return;
                        }
                        // Get the Place object from the buffer.
                        final Place place = places.get(0);
                        destPosition = place.getLatLng();
                    }
                });
                //change the marker
                if (!dest_point.getText().toString().equals("")) {
                    onSearch(dest_point, "dest_point");//create the new marker with the new address
                }

                //create the route
                if (Util.Operations.isOnline(thiscontext)) {
                    route();
                } else {
                    Toast.makeText(thiscontext, "No internet connectivity", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        //check the permission to allow the api go on
        if (ContextCompat.checkSelfPermission(thiscontext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        gMap.setMyLocationEnabled(true);

        ///Set current position and make it as a marker
        LocationManager locationManager = (LocationManager) thiscontext.getSystemService(Context.LOCATION_SERVICE);
        Location getLastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        double currentLongitude = getLastLocation.getLongitude();
        double currentLatitude = getLastLocation.getLatitude();
        //create variables for the location
        currentPosition = new LatLng(currentLatitude, currentLongitude);
        addressName = null;
        //get the address object from the function and need to be try catch beacase of the function throw IO exception
        try {
            addressName = getAddressForLocation(thiscontext, getLastLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //initial the start point with the current loaction
        start_point.setText(addressName.getAddressLine(0) + addressName.getAddressLine(1));//Set the address name in the edittext named start_point
        onSearch(start_point, "start_point");//make the marker with the address
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 13));//mover the camera to the current poistion
        //set  OnMarkerDragListener
        gMap.setOnMarkerDragListener(this);
    }

    //Get name from location
    public Address getAddressForLocation(Context context, Location location) throws IOException {

        if (location == null) {
            return null;
        }
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        int maxResults = 1;

        Geocoder gc = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = gc.getFromLocation(latitude, longitude, maxResults);

        if (addresses.size() == 1) {
            return addresses.get(0);
        } else {
            return null;
        }
    }

    //input a text and get location and new maker
    public void onSearch(EditText editText, String marker) {
        String start_point_input = editText.getText().toString();
        String dest_point_input = editText.getText().toString();
        List<Address> addressesList = null;
        if (start_point_input != null || start_point_input.equals("")) {
            Geocoder geocoder = new Geocoder(thiscontext);
            try {
                addressesList = geocoder.getFromLocationName(start_point_input, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Address address = addressesList.get(0);
            LatLng position = new LatLng(address.getLatitude(), address.getLongitude());
            if (marker.equals("start_point")) {
                currentPosition = position;
                if (startPosMarker==null) {
                    startPosMarker = gMap.addMarker(new MarkerOptions().draggable(true)
                            .title("起點")
                            .position(position));
                    gMap.animateCamera(CameraUpdateFactory.newLatLng(position));
                }else{
                    startPosMarker.setPosition(position);
                    gMap.animateCamera(CameraUpdateFactory.newLatLng(position));
                }
            }

        }

        if (dest_point_input != null || dest_point_input.equals("")) {
            Geocoder geocoder = new Geocoder(thiscontext);
            try {
                addressesList = geocoder.getFromLocationName(dest_point_input, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Address address = addressesList.get(0);
            LatLng position = new LatLng(address.getLatitude(), address.getLongitude());
            if (marker.equals("dest_point")) {
                destPosition = position;
                if (destPosMarker==null) {
                    destPosMarker = gMap.addMarker(new MarkerOptions().draggable(true)
                            .title("終點")
                            .position(position));
                    gMap.animateCamera(CameraUpdateFactory.newLatLng(position));
                }
                else{
                    destPosMarker.setPosition(position);
                    gMap.animateCamera(CameraUpdateFactory.newLatLng(position));

                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //reload current position and add the marker
            case R.id.returnmyposition:
                start_point.setText(addressName.getAddressLine(0) + addressName.getAddressLine(1));
                break;
            case R.id.datepicker:
                showDatePicker();
                break;
            case R.id.timepicker:
                showTimePicker();
                break;
        }
    }
    public void route()
    {
        if(currentPosition==null || destPosition==null)
        {
            if(currentPosition==null)
            {
                if(start_point.getText().length()>0)
                {
                    start_point.setError("currnullChoose location from dropdown.");
                }
                else
                {
                    Toast.makeText(thiscontext,"Please choose a starting point.",Toast.LENGTH_SHORT).show();
                }
            }
            if(destPosition==null)
            {
                if(dest_point.getText().length()>0)
                {
                    dest_point.setError("destnullChoose location from dropdown.");
                }
                else
                {
                    Toast.makeText(thiscontext,"Please choose a destination.",Toast.LENGTH_SHORT).show();
                }
            }
        }
        else
        {
            progressDialog = ProgressDialog.show(thiscontext, "Please wait.",
                    "Fetching route information.", true);
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(currentPosition, destPosition)
                    .build();
            routing.execute();
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if (marker.getTitle().equals("起點")) {
            Geocoder gc = new Geocoder(thiscontext, Locale.getDefault());
            List<Address> list = null;
            LatLng ll = marker.getPosition();
            try {
                list = gc.getFromLocation(ll.latitude, ll.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Address add = list.get(0);
            start_point.setText(add.getAddressLine(0) + add.getAddressLine(1));//Set the address name in the edittext named start_point
        }

        if (marker.getTitle().equals("終點")) {
            Geocoder gc = new Geocoder(thiscontext, Locale.getDefault());
            List<Address> list = null;
            LatLng ll = marker.getPosition();
            try {
                list = gc.getFromLocation(ll.latitude, ll.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Address add = list.get(0);
            dest_point.setText(add.getAddressLine(0) + add.getAddressLine(1));//Set the address name in the edittext named start_point
        }
    }

    protected void showTimePicker() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    protected void showDatePicker() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onRoutingFailure(RouteException e) {
        // The Routing request failed
        progressDialog.dismiss();
        if(e != null) {
            Toast.makeText(thiscontext, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(thiscontext, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex){
        progressDialog.dismiss();
        CameraUpdate center = CameraUpdateFactory.newLatLng(currentPosition);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

        gMap.moveCamera(center);

        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % colors.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(colors[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = gMap.addPolyline(polyOptions);
            polyline.setClickable(true);
            polylines.add(polyline);

            Toast.makeText(thiscontext.getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }
        //set the polyline on click listener
        gMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline polyline) {
                //remove all the other lines
                if(polylines.size()>0) {
                    for (Polyline poly : polylines) {
                        if(!poly.equals(polyline)){
                            poly.remove();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onRoutingCancelled() {

    }

    public class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(thiscontext, this, hour, minute,
                    DateFormat.is24HourFormat(thiscontext));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            String AM_PM = "";

            Calendar datetime = Calendar.getInstance();
            datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            datetime.set(Calendar.MINUTE, minute);

            if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                AM_PM = "AM";
            else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                AM_PM = "PM";

            String hours = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime
                    .get(Calendar.HOUR) + "";

            time.setText(hours + " : " + minute + " : " + AM_PM);

        }
    }
    public class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

			/*
			 * MOnth returned from datepicker is always one less than actual
			 * value for example: December will be 11, January will be 0 and so
			 * on.
			 */

            month = month + 1;

            date.setText(day + " - " + month + " - " + year);

        }
    }
}
