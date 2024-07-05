package com.example.product_sale.map;
import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.createDefault2DPuck;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.getLocationComponent;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.product_sale.R;
import com.example.product_sale.databinding.FragmentMapBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.ImageHolder;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.LocationPuck;
import com.mapbox.maps.plugin.LocationPuck2D;
import com.mapbox.maps.plugin.PuckBearing;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener;
import com.mapbox.maps.plugin.viewport.ViewportPlugin;
import com.mapbox.maps.plugin.viewport.data.FollowPuckViewportStateOptions;
import com.mapbox.maps.plugin.viewport.state.FollowPuckViewportState;
import com.mapbox.maps.plugin.viewport.state.ViewportState;
import com.mapbox.maps.plugin.viewport.transition.ViewportTransition;

import java.util.List;

public class MapFragment extends Fragment {
    private FragmentMapBinding binding;
    private MapView mapView;

    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;

    private FloatingActionButton floatingActionButton;

    private static final double CUSTOM_LATITUDE = 10.7775906;
    private static final double CUSTOM_LONGITUDE = 106.509582;
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result){
                Toast.makeText(getContext(), "Permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mapView = binding.mapView;
        floatingActionButton = binding.focusLocation;
        floatingActionButton.hide();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        mapView.getMapboxMap().loadStyleUri("mapbox://styles/sthaophan/cly8nycqc00gu01pf5n4qcaqv", new Style.OnStyleLoaded(){

            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mapView.getMapboxMap().setCamera(new CameraOptions.Builder().zoom(17.0).build());
                LocationComponentPlugin locationComponentPlugin = getLocationComponent(mapView);
                locationComponentPlugin.setEnabled(true);
                LocationPuck2D locationPuck2D = new LocationPuck2D();
                Drawable drawable = AppCompatResources.getDrawable(requireContext(), R.drawable.mapbox_baseline_location_on_24);
                if (drawable != null) {
                    Bitmap bitmap = drawableToBitmap(drawable);
                    ImageHolder imageHolder = ImageHolder.from(bitmap);
                    locationPuck2D.setBearingImage(imageHolder);
                }
                locationComponentPlugin.setLocationPuck(locationPuck2D);
                locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
                getGestures(mapView).addOnMoveListener(onMoveListener);

                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                        locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
                        getGestures(mapView).addOnMoveListener(onMoveListener);
                        floatingActionButton.hide();
                    }
                });
            }
        });
        return root;
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private  final OnIndicatorBearingChangedListener onIndicatorBearingChangedListener = new OnIndicatorBearingChangedListener() {
        @Override
        public void onIndicatorBearingChanged(double v) {
            mapView.getMapboxMap().setCamera(new CameraOptions.Builder().bearing(v).build());
        }
    };

    private  final OnIndicatorPositionChangedListener onIndicatorPositionChangedListener = new OnIndicatorPositionChangedListener() {
        @Override
        public void onIndicatorPositionChanged(@NonNull Point point) {
            mapView.getMapboxMap().setCamera(new CameraOptions.Builder().center(point).zoom(17.0).build());
            getGestures(mapView).setFocalPoint(mapView.getMapboxMap().pixelForCoordinate(point));
        }
    };

    private final OnMoveListener onMoveListener = new OnMoveListener() {
        @Override
        public void onMoveBegin(@NonNull MoveGestureDetector moveGestureDetector) {
            getLocationComponent(mapView).removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
            getLocationComponent(mapView).addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
            getGestures(mapView).removeOnMoveListener(onMoveListener);
            floatingActionButton.show();
        }

        @Override
        public boolean onMove(@NonNull MoveGestureDetector moveGestureDetector) {
            return false;
        }

        @Override
        public void onMoveEnd(@NonNull MoveGestureDetector moveGestureDetector) {

        }
    };
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
