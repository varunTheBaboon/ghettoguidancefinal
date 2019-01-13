package com.example.ghettoguidance;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.CameraPosition;
import com.tomtom.online.sdk.map.MapFragment;
import com.tomtom.online.sdk.map.MarkerBuilder;
import com.tomtom.online.sdk.map.OnMapReadyCallback;
import com.tomtom.online.sdk.map.RouteBuilder;
import com.tomtom.online.sdk.map.SimpleMarkerBalloon;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.routing.OnlineRoutingApi;
import com.tomtom.online.sdk.routing.RoutingApi;
import com.tomtom.online.sdk.routing.data.FullRoute;
import com.tomtom.online.sdk.routing.data.RouteQuery;
import com.tomtom.online.sdk.routing.data.RouteQueryBuilder;
import com.tomtom.online.sdk.routing.data.RouteType;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
   private Button button;

    public void routeShow(View v){



    }
    private TomtomMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.btnRouteShow);
        button.setOnClickListener(mapOnClick);
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getAsyncMap(this);
    }

    public void onMapReady(@NonNull TomtomMap tomtomMap) {
        this.map = tomtomMap;

        LatLng amsterdam;
        amsterdam = new LatLng(27.40, 81.78);
        SimpleMarkerBalloon balloon = new SimpleMarkerBalloon("Amsterdam");
        tomtomMap.addMarker(new MarkerBuilder(amsterdam).markerBalloon(balloon));
        tomtomMap.centerOn(CameraPosition.builder(amsterdam).zoom(7).build());
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    private View.OnClickListener mapOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LatLng amsterdam = new LatLng(27.40, 81.78);
            LatLng hague = new LatLng(29.31, 81.42);
            RoutingApi routingApi = OnlineRoutingApi.create(getApplicationContext());
            RouteQuery routeQuery = new RouteQueryBuilder(amsterdam, hague).withRouteType(RouteType.FASTEST);
            routingApi.planRoute(routeQuery)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(routeResult -> {
                        for (FullRoute fullRoute : routeResult.getRoutes()) {
                            RouteBuilder routeBuilder = new RouteBuilder(
                                    fullRoute.getCoordinates()).isActive(true);
                            map.addRoute(routeBuilder);
                        }
                    });

        }
    };
}
