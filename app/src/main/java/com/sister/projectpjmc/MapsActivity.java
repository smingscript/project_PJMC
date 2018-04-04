package com.sister.projectpjmc;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private ClusterManager<TkykItem> tkykItemClusterManager;
    private GoogleMap mMap;
    private MapView mapView;
    private Geocoder geocoder;
    private List<Address> singleAddress = null;
    private ArrayList<List<Address>> addressArray;
    private HashMap<String, String> pointsDetails;
    private KmlParser kmlParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            geocoder = new Geocoder(this, Locale.KOREA);
            addressArray = new ArrayList<>();

            //35.958438, 127.771991
            final LatLngBounds SEOUL = new LatLngBounds(
                    new LatLng(15, 120), new LatLng(40, 150));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL.getCenter(), 7));
            mMap.setLatLngBoundsForCameraTarget(SEOUL);

            tkykItemClusterManager = new ClusterManager<TkykItem>(this, mMap);
            mMap.setOnCameraIdleListener(tkykItemClusterManager);
            mMap.setOnMarkerClickListener(tkykItemClusterManager);

            LatLng center = new LatLng(35.958438, 127.771991);
//            mMap.addMarker(new MarkerOptions().position(center));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(center));

            kmlParser = new KmlParser(this);
            ArrayList<HashMap<String, String>> tkykPoints = kmlParser.parseXml();

            for (int i = 0; i < tkykPoints.size(); i++) {
                pointsDetails = tkykPoints.get(i);
//                Log.e("coordinates", pointsDetails.get("name").toString());
                String[] coordinates = pointsDetails.get("coordinates").split(",");
//                Log.e("coordinates", coordinates.toString());

                try {
                    LatLng latLng = new LatLng(Double.parseDouble(coordinates[1]), Double.parseDouble(coordinates[0]));
                    //역지오코딩으로 각 점의 주소 가져오기
//                    singleAddress = geocoder.getFromLocation(Double.parseDouble(coordinates[1]), Double.parseDouble(coordinates[0]), 1);
//                    addressArray.add(singleAddress);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(pointsDetails.get("name")).icon(BitmapDescriptorFactory.fromResource(R.drawable.takoyaki)));
                    mMap.setOnInfoWindowClickListener(this);
                } catch (NumberFormatException e) {
                    Log.e("coordinates_null", pointsDetails.get("name"));
                }
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MapsActivity.this, InfoActivity.class);
        intent.putExtra("details", pointsDetails);
        startActivity(intent);
    }
}
