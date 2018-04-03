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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private ClusterManager<TkykItem> tkykItemClusterManager;
    private GoogleMap mMap;
    private MapView mapView;

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

//            LatLngBounds latLngBounds = new LatLngBounds(new LatLng(40.712216, -74.22655),     // South west corner
//                    new LatLng(40.773941, -74.12544));
//            mMap.setBounds(latLngBounds);
//
//            mMap.b

//            final LatLngBounds AUSTRALIA = new LatLngBounds(
//                    new LatLng(-44, 113), new LatLng(-10, 154));
//
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AUSTRALIA.getCenter(), 10));
//
//            LatLngBounds ADELAIDE = new LatLngBounds(
//                    new LatLng(-35.0, 138.58), new LatLng(-34.9, 138.61));
//// Constrain the camera target to the Adelaide bounds.
//            mMap.setMinZoomPreference(1.0f);
//            mMap.setMaxZoomPreference(16.0f);
//            mMap.setLatLngBoundsForCameraTarget(ADELAIDE);

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


            //threadMethod();



            KmlLayer layer = new KmlLayer(mMap, R.raw.takoyaki, getApplicationContext());
            //layer.addLayerToMap();

//            if(true){
//                throw new AssertionError("Object cannot be null");
//
//            }

            KmlContainer container = layer.getContainers().iterator().next();
            //Retrieve a nested container within the first container
            container = container.getContainers().iterator().next();


            for (KmlPlacemark placemark : container.getPlacemarks()) {

                if(placemark.getGeometry().getGeometryType().equals("Point")) {
//                    if(true){
//                        throw new AssertionError("Object cannot be null");
//                    }
                    KmlPoint point = (KmlPoint) placemark.getGeometry();
                    LatLng latLng = new LatLng(point.getGeometryObject().latitude, point.getGeometryObject().longitude);
                    mMap.addMarker(new MarkerOptions().position(latLng));
//                    Log.e(latLng.toString(), "return: " + latLng.toString());
//                    tkykItemClusterManager.addItem(new TkykItem(latLng));
                }
            }

            final Geocoder geocoder = new Geocoder(this, Locale.KOREA);
            List<Address> list = null;

            list = geocoder.getFromLocation(35.958438, 127.771991, 10);

            if(list != null){
                if(list.size() == 0){
                    Log.e("Error", "해당되는 주소 정보는 없습니다");
                } else {
                    Log.e("Geocoder", list.get(0).toString());
                }
            }

//            KmlPolygon kmlPolygon = (KmlPolygon)layer.getPlacemarks();


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

    private void threadMethod() throws XmlPullParserException, IOException {
        StringBuffer buffer = new StringBuffer();

        InputStream ins = getResources().openRawResource(getResources().getIdentifier("takoyaki", "raw", getPackageName()));
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(ins, "utf-8");

        String tag = "";
        int eventType = xpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    tag = xpp.getName();
                    buffer.append("\n\nstart tag => " + tag + "\n");
                    if (tag.equals("number")) {
                        buffer.append("번호 : ");
                    } else if (tag.equals("name")) {
                        buffer.append("이름 : ");
                    }
                    break;
                case XmlPullParser.TEXT:
                    String text = xpp.getText();
                    if (tag.equals("number")) {
                        buffer.append(text);
                    } else if (tag.equals("name")) {
                        buffer.append(text);
                    }
                    break;
                case XmlPullParser.END_TAG: // ⑤ ⑨ ⑪
                    tag = xpp.getName();
                    buffer.append("\nend tag => " + tag);
                    break;
            }
            eventType = xpp.next();
        }


//        return buffer.toString();
        Log.e("XmlParser", buffer.toString());
    }

}