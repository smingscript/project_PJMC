package com.sister.projectpjmc;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.geometry.Bounds;
import com.google.maps.android.kml.KmlContainer;
import com.google.maps.android.kml.KmlLayer;
import com.google.maps.android.kml.KmlPlacemark;
import com.google.maps.android.kml.KmlPoint;
import com.google.maps.android.kml.KmlPolygon;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private ClusterManager<TkykItem> tkykItemClusterManager;
    private GoogleMap mMap;
    private MapView mapView;
    private Geocoder geocoder;
    private List<Address> list = null;
    private ArrayList<List<Address>> addressArray;

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
            mMap.addMarker(new MarkerOptions().position(center));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(center));

            ArrayList<HashMap<String, String>> tkykPoints = threadMethod();

            for(int i = 0; i < tkykPoints.size(); i++){
                HashMap<String, String> pointsDetails = tkykPoints.get(i);
//                Log.e("coordinates", pointsDetails.get("name").toString());
                String[] coordinates = pointsDetails.get("coordinates").split(",");
//                Log.e("coordinates", coordinates.toString());

                try {
                    LatLng latLng = new LatLng(Double.parseDouble(coordinates[1]), Double.parseDouble(coordinates[0]));
                    mMap.addMarker(new MarkerOptions().position(latLng));
                    Log.e("coordinates", Double.parseDouble(coordinates[0]) + "//" + Double.parseDouble(coordinates[1]));
                } catch (NumberFormatException e) {
                    Log.e("coordinates_null", pointsDetails.get("name"));
                }
            }

            //Kml data 가져오기
//            KmlLayer layer = new KmlLayer(mMap, R.raw.takoyaki, getApplicationContext());
            //layer.addLayerToMap();

//            //Retrieve the first container in the KML layer
//            //서울1 콘테이너만 가져온다. 해결 해야함!
//            KmlContainer container = layer.getContainers().iterator().next();
//            //Retrieve a nested container within the first container
//            container = container.getContainers().iterator().next();
//            //Retrieve the first placemark in the nested container
//            KmlPlacemark placemark = container.getPlacemarks().iterator().next();
//            //Retrieve a polygon object in a placemark
//            KmlPoint polygon = (KmlPoint) placemark.getGeometry();
//            Create LatLngBounds of the outer coordinates of the polygon
//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//            for (LatLng latLng : polygon.getOuterBoundaryCoordinates()) {
//                builder.include(latLng);
//            }

////                      //역지오코딩으로 각 점의 주소 가져오기
////            for (KmlPlacemark placemark : container.getPlacemarks()) {
////                if(placemark.getGeometry().getGeometryType().equals("Point")) {
////                    KmlPoint point = (KmlPoint) placemark.getGeometry();
////                    list = geocoder.getFromLocation(point.getGeometryObject().latitude, point.getGeometryObject().longitude, 1);
////                    LatLng latLng = new LatLng(point.getGeometryObject().latitude, point.getGeometryObject().longitude);
////                    mMap.addMarker(new MarkerOptions().position(latLng));
//////                    Log.e(latLng.toString(), "return: " + latLng.toString());
//////                    TkykItem offsetItem = new TkykItem(new LatLng(lat, lng));
//////                    tkykItemClusterManager.addItem(offsetItem);
////                    if(list != null){
////                        if(list.size() == 0) Log.e("Error", "해당되는 주소 정보는 없습니다");
////                        else addressArray.add(list);
////                    }
////                }
////            }
//            Log.e("lists", Integer.toString(addressArray.size()));
////            Log.e("lists", list.toString());




//            // Set some lat/lng coordinates to start with.
//            double lat = 51.5145160;
//            double lng = -0.1270060;
//
//            // Add ten cluster items in close proximity, for purposes of this example.
//            for (int i = 0; i < 10; i++) {
//                double offset = i / 60d;
//                lat = lat + offset;
//                lng = lng + offset;
//                TkykItem offsetItem = new TkykItem(new LatLng(lat, lng));
//                tkykItemClusterManager.addItem(offsetItem);
//            }


//            LatLng sydney = new LatLng(-34, 151);
//            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {


        return false;
    }

    private ArrayList<HashMap<String,String>> threadMethod() throws XmlPullParserException, IOException {
        StringBuffer buffer = new StringBuffer();

        InputStream ins = getResources().openRawResource(getResources().getIdentifier("takoyaki", "raw", getPackageName()));
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(ins, "utf-8");

        String tag = "";
        int eventType = xpp.getEventType();

        boolean isStartTag = false;
        ArrayList<HashMap<String, String>> searchContents = new ArrayList<>();

        HashMap<String, String> placeMarkContent = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    tag = xpp.getName();
                    if (tag.equals("Placemark")) {
                        placeMarkContent = new HashMap<>();
                    } else if(tag.equals("name") || tag.equals("description") || tag.equals("coordinates")) {
                        isStartTag = true;
                    }

                    break;
                case XmlPullParser.TEXT:
                    String text = xpp.getText();

                    if (isStartTag && placeMarkContent != null && tag.equals("name")) {
                        placeMarkContent.put("name", text);
//                        Log.e("text name", text);
                    } else if (isStartTag && placeMarkContent != null && tag.equals("description")) {
                        placeMarkContent.put("description", text);
                    } else if (isStartTag && placeMarkContent != null && tag.equals("coordinates")){
                        placeMarkContent.put("coordinates", text);
//                        Log.e("text coordinates", text);
//                        Log.e("text coordinates map", placeMarkContent.toString());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tag = xpp.getName();

                    if(tag.equals("Placemark")){
                        searchContents.add(placeMarkContent);
//                        Log.e("text placeMarkContent", placeMarkContent.toString());
                        placeMarkContent = null;
                    } else if(tag.equals("name") || tag.equals("description") || tag.equals("coordinates")) {
                        isStartTag = false;
                    }
                    break;
            }
            eventType = xpp.next();
        }

        Log.e("XmlParser", searchContents.toString());
        return searchContents;

    }


}
