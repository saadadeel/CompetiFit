//package com.project.saadadeel.CompetiFit.RunTracker;
//
//import com.google.android.gms.maps.*;
//import com.google.android.gms.maps.model.*;
//import com.project.saadadeel.CompetiFit.*;
//import com.project.saadadeel.CompetiFit.R;
//
//import android.app.Activity;
//import android.os.Bundle;
//
//public class mapRun extends Activity implements OnMapReadyCallback {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(com.project.saadadeel.CompetiFit.R.layout.map_activity);
//
//        MapFragment mapFragment = (MapFragment) getFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }
//
//    @Override
//    public void onMapReady(GoogleMap map) {
//        LatLng sydney = new LatLng(-33.867, 151.206);
//
//        map.setMyLocationEnabled(true);
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
//
//        map.addMarker(new MarkerOptions()
//                .title("Sydney")
//                .snippet("The most populous city in Australia.")
//                .position(sydney));
//    }
//}