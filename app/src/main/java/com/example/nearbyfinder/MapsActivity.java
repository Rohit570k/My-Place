package com.example.nearbyfinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.nearbyfinder.Activity.LoginActivity;
import com.example.nearbyfinder.Activity.AccountsActivity;
import com.example.nearbyfinder.Adapters.TomPlacesDetailAdapter;
import com.example.nearbyfinder.Constants.AllConstants;
import com.example.nearbyfinder.Model.PlaceModel.TomResponseModel;
import com.example.nearbyfinder.Model.PoiGroupSet;
import com.example.nearbyfinder.WebServices.RetrofitApi;
import com.example.nearbyfinder.WebServices.RetrofitClient;
import com.example.nearbyfinder.Model.TomNearbyPlaceModel;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback ,GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MAPSACTIVTY";
    private GoogleMap mMap;
    private final int LOCATION_PERMISSION_REQUEST = 1;
    private boolean mLocationPermissionGranted,isTrafficEnable;
private FirebaseAuth mAuth;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private RetrofitApi retrofitApi;
    private int searchRadius=5000;
    private PoiGroupSet selectedPoiGroupSet;
//    private PlacesClient placesClient;
//    private List<AutocompletePrediction> predictionList;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private Location currentLocation;
    private MaterialSearchBar materialSearchBar;
    private RippleBackground rippleBg;

    private List<TomNearbyPlaceModel> tomNearbyPlaceModelList;
    private Marker currentMarker;
    RecyclerView placesRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Maps");
         FloatingActionButton btnMapType=(FloatingActionButton)findViewById(R.id.btnMapType);
        FloatingActionButton enableTraffic=(FloatingActionButton)findViewById(R.id.enableTraffic);
        FloatingActionButton currentLocaton=(FloatingActionButton)findViewById(R.id.currentLocation);
        ChipGroup chipGroup = (ChipGroup)findViewById(R.id.poiExampleGroup) ;
        rippleBg=(RippleBackground)findViewById(R.id.ripple_bg) ;
        mAuth=FirebaseAuth.getInstance();
        retrofitApi= RetrofitClient.getRetrofitClient().create(RetrofitApi.class);
        tomNearbyPlaceModelList=new ArrayList<>();
        placesRecyclerView=findViewById(R.id.placesRecyclerView);

//        materialSearchBar = findViewById(R.id.searchBar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        for (PoiGroupSet poiGroupSet : AllConstants.poiGroups) {

            Chip chip = new Chip(MapsActivity.this);
            chip.setText(poiGroupSet.getName());
            chip.setId(poiGroupSet.getId());
            chip.setPadding(8, 8, 8, 8);
            chip.setTextColor(getResources().getColor(R.color.white, null));
            chip.setChipBackgroundColor(getResources().getColorStateList(R.color.lightblack, null));
            chip.setChipIcon(ResourcesCompat.getDrawable(getResources(), poiGroupSet.getDrawableId(), null));
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);

            chipGroup.addView(chip);


        }

        btnMapType.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenuInflater().inflate(R.menu.map_type_menu, popupMenu.getMenu());


            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.btnNormal:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;

                    case R.id.btnSatellite:
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;

                    case R.id.btnTerrain:
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                }
                return true;
            });

            popupMenu.show();
        });

        enableTraffic.setOnClickListener(view -> {

            if (isTrafficEnable) {
                if (mMap != null) {
                    mMap.setTrafficEnabled(false);
                    isTrafficEnable = false;
                }
            } else {
                if (mMap != null) {
                    mMap.setTrafficEnabled(true);
                    isTrafficEnable = true;
                }
            }
        });

        currentLocaton.setOnClickListener(currentLocation -> getCurrentLocation());

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                if (checkedId != -1) {
                    PoiGroupSet poiGroupSet = AllConstants.poiGroups.get(checkedId - 1);
//                    binding.edtPlaceName.setText(placeModel.getName());
                    selectedPoiGroupSet = poiGroupSet;
                    getNearbyPlaces(poiGroupSet.getPoiCategoriesId());
                }
            }
        });

// Construct a FusedLocationProviderClient.
//        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this);
//        Places.initialize(MapsActivity.this, getString(R.string.google_maps_key));
//        placesClient = Places.createClient(this);
//        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        Log.i("PlaceaPI call", "onCreate: "+"https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                "-33.8670522,151.1957362&radius=500&types=food&name=harbour&key="+"AIzaSyA812zqb8ftSKFuyAJwMcsk0vM7zuspixw");
//
//        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
//            @Override
//            public void onSearchStateChanged(boolean enabled) {
//
//            }
//
//            @Override
//            public void onSearchConfirmed(CharSequence text) {
//                startSearch(text.toString(), true, null, true);
//            }
//
//            @Override
//            public void onButtonClicked(int buttonCode) {
//                if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
//                    materialSearchBar.disableSearch();
//                }
//            }
//        });
//
//        materialSearchBar.addTextChangeListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
//                        .setTypeFilter(TypeFilter.ADDRESS)
//                        .setSessionToken(token)
//                        .setQuery(s.toString())
//                        .build();
//                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
//                    @Override
//                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
//                        if (task.isSuccessful()) {
//                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
//                            if (predictionsResponse != null) {
//                                predictionList = predictionsResponse.getAutocompletePredictions();
//                                List<String> suggestionsList = new ArrayList<>();
//                                for (int i = 0; i < predictionList.size(); i++) {
//                                    AutocompletePrediction prediction = predictionList.get(i);
//                                    suggestionsList.add(prediction.getFullText(null).toString());
//                                }
//                                materialSearchBar.updateLastSuggestions(suggestionsList);
//                                if (!materialSearchBar.isSuggestionsVisible()) {
//                                    materialSearchBar.showSuggestionsList();
//                                }
//                            }
//                        } else {
//                            Log.i("mytag", "prediction fetching task unsuccessful");
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        materialSearchBar.setSuggstionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
//            @Override
//            public void OnItemClickListener(int position, View v) {
//                if (position >= predictionList.size()) {
//                    return;
//                }
//                AutocompletePrediction selectedPrediction = predictionList.get(position);
//                String suggestion = materialSearchBar.getLastSuggestions().get(position).toString();
//                materialSearchBar.setText(suggestion);
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        materialSearchBar.clearSuggestions();
//                    }
//                }, 1000);
//                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                if (imm != null)
//                    imm.hideSoftInputFromWindow(materialSearchBar.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
//                final String placeId = selectedPrediction.getPlaceId();
//                List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG);
//
//                FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build();
//                placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
//                    @Override
//                    public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
//                        Place place = fetchPlaceResponse.getPlace();
//                        Log.i("mytag", "Place found: " + place.getName());
//                        LatLng latLngOfPlace = place.getLatLng();
//                        if (latLngOfPlace != null) {
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOfPlace, 17));
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        if (e instanceof ApiException) {
//                            ApiException apiException = (ApiException) e;
//                            apiException.printStackTrace();
//                            int statusCode = apiException.getStatusCode();
//                            Log.i("mytag", "place not found: " + e.getMessage());
//                            Log.i("mytag", "status code: " + statusCode);
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void OnItemDeleteListener(int position, View v) {
//
//            }
//        });


    }



    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        mLocationPermissionGranted = false;
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;

                } else {
                    Toast.makeText(this, "User has not granted location access permission", Toast.LENGTH_LONG).show();
                    finish();
                }
                updateLocationUI();
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     * WhENEVER MAP IS ON SCREEN THIS FUNCTION STATEMNT RUNS
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoomLevel = 20f;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
//        // Add a marker in Sydney and move the camera
//        LatLng patna = new LatLng(25.611, 85.144);
//        mMap.addMarker(new MarkerOptions().position(patna).title("Patna,Bihar"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(patna,zoomLevel));

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            Log.d("MYACTIVITY", "updateLocationUI: " + mLocationPermissionGranted);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted=true;
                Log.d("MYACTIVITY", "updateLocationUI: " + "HASCAME");
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setTiltGesturesEnabled(true);
                mMap.setOnMarkerClickListener(this::onMarkerClick);
                //mMap.getUiSettings().setMyLocationButtonEnabled(true);
                getLocationUpdate();
            } else {
                mMap.setMyLocationEnabled(false);
               // mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.getUiSettings().setTiltGesturesEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationUpdate() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(20000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        Log.d("TAG", "onLocationResult: " + location.getLongitude() + " " + location.getLatitude());

                    }
                }
                super.onLocationResult(locationResult);
            }
        };
        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(MapsActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(MapsActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();
            }
        });

        task.addOnFailureListener(MapsActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(MapsActivity.this, 50);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 50) {
            if (resultCode == RESULT_OK) {
                startLocationUpdates();
            }
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MapsActivity.this, "Location updated started", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        getCurrentLocation();
    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = false;
            return;
        }
        /** This was used earlier to get current location but BCZ ,
         * onSuccess also passing when current location is null hence ,
         * used onComplete Listener so that to make a check for that
//        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                currentLocation = location;
////                infoWindowAdapter = null;
////                infoWindowAdapter = new GoogleMap.InfoWindowAdapter(currentLocation, requireContext());
////                mMap.setInfoWindowAdapter(infoWindowAdapter);
//                moveCameraToLocation(location);
//
//
//            }
//        });
         */
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(
                this, new OnCompleteListener<Location>() {

                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            currentLocation = task.getResult();
                            if (currentLocation != null) {
                                moveCameraToLocation(currentLocation);
                                //placesRecyclerView.setAlpha(0);
                                placesRecyclerView.setAdapter(null);
                            } else {
                                Log.d(TAG, "Current location is null. Using defaults.");
                                Log.e(TAG, "Exception: %s"+ task.getException());
                            }
                        }
                    }
                });
    }

    private void moveCameraToLocation(Location location) {
        Log.i(TAG, "moveCameraToLocation:"+location);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new
                LatLng(location.getLatitude(), location.getLongitude()), 17);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(location.getLatitude (), location.getLongitude()))
                .title("Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                 .snippet(mAuth.getCurrentUser().getDisplayName());
        if (currentMarker != null) {
            currentMarker.remove();
            mMap.clear();
        }
        currentMarker = mMap.addMarker(markerOptions);
        currentMarker.setTag(703);
        mMap.animateCamera(cameraUpdate);

    }

    private void stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        Log.d("TAG", "stopLocationUpdate: Location Update stop");
    }

    @Override
    public void onPause() {
        super.onPause();

        if (fusedLocationProviderClient != null)
            stopLocationUpdate();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (fusedLocationProviderClient != null) {
            startLocationUpdates();
            if (currentMarker != null) {
                currentMarker.remove();
            }
        }
    }

    private void getNearbyPlaces(String poiCatogeriesId){
        rippleBg.startRippleAnimation();
        String url ="https://api.tomtom.com/search/2/nearbySearch/.json?" +
                "lat="+ currentLocation.getLatitude() + "&lon=" + currentLocation.getLongitude()
                + "&limit=100" + "&countrySet=IN" + "&radius=" +searchRadius+
                "&categorySet="+ poiCatogeriesId + "&view=IN" + "&openingHours=nextSevenDays" +
                "&relatedPois=all" +
                "&key=" + "MtUGzlQ1xgeImI1xMbxwolLWJXxYjU1B";

        retrofitApi.getNearByPlaces(url).enqueue(new Callback<TomResponseModel>() {
            @Override
            public void onResponse(Call<TomResponseModel> call, Response<TomResponseModel> response) {
                Log.i(TAG, "onResponse:JSONRESPONSE"+response.body());
                Gson gson =new Gson();
                String res =gson.toJson(response.body());
                Log.i(TAG, "onResponse: JsonParsed"+res);
                if(response.errorBody()==null){
                    if(response.body()!= null) {
                        if(response.body().getTomNearbyPlaceModelList()!=null&&response.body().getTomNearbyPlaceModelList().size()>0) {
                            tomNearbyPlaceModelList.clear();
                            mMap.clear();
                            for (int i = 0; i < response.body().getTomNearbyPlaceModelList().size(); i++) {

                                tomNearbyPlaceModelList.add(response.body().getTomNearbyPlaceModelList().get(i));
                                addMarker(response.body().getTomNearbyPlaceModelList().get(i), i);
                            }

                            setUpRecyclerview();
                        }else{
                            mMap.clear();
                            tomNearbyPlaceModelList.clear();

                            setUpRecyclerview();
                            //If not found in 5km then we add 1km to request
                            //and again make a request aPi call
                            searchRadius+=1000;
                            getNearbyPlaces(poiCatogeriesId);
                        }
                    }
                }else {
                    Log.d("TAG", "onResponseERROR: " + response.errorBody());
                    Toast.makeText(MapsActivity.this, "Error : " + response.errorBody(), Toast.LENGTH_SHORT).show();
                }

                rippleBg.stopRippleAnimation();
            }

            @Override
            public void onFailure(Call<TomResponseModel> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t);
                rippleBg.stopRippleAnimation();
            }
        });
    }
     private void addMarker(TomNearbyPlaceModel tomNearbyPlaceModel,int position){
        MarkerOptions markerOptions =new MarkerOptions()
                .position(new LatLng(tomNearbyPlaceModel.getPosition().getLat(),
                        tomNearbyPlaceModel.getPosition().getLon()))
                .title(tomNearbyPlaceModel.getPoi().getName())
                .snippet(tomNearbyPlaceModel.getAddress().getFreeformAddress());
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(markerOptions).setTag(position);
         CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new
                 LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 14);
         mMap.animateCamera(cameraUpdate);
     }

    private BitmapDescriptor getCustomIcon() {

        Drawable background = ContextCompat.getDrawable(MapsActivity.this, R.drawable.mapiconshadow );
       // background.setTint(getResources().getColor(R.color.quantum_googred900, null));
        background.setBounds(0, 0, background.getMinimumWidth(), background.getMinimumHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getMinimumWidth(), background.getMinimumHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void setUpRecyclerview() {

//placesRecyclerView.setAlpha(1);
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(MapsActivity.this, LinearLayoutManager.HORIZONTAL,false));
        placesRecyclerView.setHasFixedSize(false);
        TomPlacesDetailAdapter tomPlacesDetailAdapter = new TomPlacesDetailAdapter(tomNearbyPlaceModelList);
        placesRecyclerView.setAdapter(tomPlacesDetailAdapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        placesRecyclerView.setOnFlingListener(null);
        snapHelper.attachToRecyclerView(placesRecyclerView);

       placesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (position > -1) {
                    TomNearbyPlaceModel tomNearbyPlaceModel = tomNearbyPlaceModelList.get(position);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(tomNearbyPlaceModel.getPosition().getLat(),
                            tomNearbyPlaceModel.getPosition().getLon()), 17));
                }
            }
        });


    }
    @Override
    public boolean onMarkerClick(Marker marker) {

        int markerTag = (int) marker.getTag();
        Log.d("TAG", "onMarkerClick: " + markerTag);

       placesRecyclerView.scrollToPosition(markerTag);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //adding a click listner for option selected on below line.
        int id = item.getItemId();
        //            case R.id.sign_out_menu:
        //                //displaying a toast message on user logged out inside on click.
        //                Toast.makeText(getApplicationContext(), "User Logged Out", Toast.LENGTH_LONG).show();
        //                //on below line we are signing out our user.
        //                mAuth.signOut();
        //                //on below line we are opening our login activity.
        //                Intent i = new Intent(MapsActivity.this, LoginActivity.class);
        //                startActivity(i);
        //                this.finish();
        //                return true;
        if (id == R.id.account) {
            Intent settingsIntent = new Intent(this, AccountsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //on below line we are inflating our menu file for displaying our menu options.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            //to visible the icon along with text
            m.setOptionalIconsVisible(true);
        }
        return true;
    }
}