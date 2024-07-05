package com.example.product_sale.map;
import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.example.product_sale.R;
import com.example.product_sale.databinding.FragmentMapBinding;
import com.example.product_sale.models.ShopLocation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.OnMoveListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapFragment extends Fragment {
    private static final int REQUEST_CODE_SHOP_LOCATION = 1001;
    private FragmentMapBinding binding;
    private MapView mapView;

    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;

    private FloatingActionButton floatingActionButton;
    private Point markedLocation;
    private static final double CUSTOM_LATITUDE = 10.7527936;
    private static final double CUSTOM_LONGITUDE = 106.6698121;
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
        markedLocation = Point.fromLngLat(CUSTOM_LONGITUDE, CUSTOM_LATITUDE);

        mapView.getMapboxMap().loadStyleUri("mapbox://styles/sthaophan/cly8nycqc00gu01pf5n4qcaqv", style -> {
            initializeMap(style);
        });
        return root;
    }

    private void initializeMap(@NonNull Style style) {
        mapboxMap = mapView.getMapboxMap();
        mapboxMap.setCamera(new CameraOptions.Builder()
                .center(markedLocation)
                .zoom(17.0)
                .build());

        addMarker(markedLocation);

        getGestures(mapView).addOnMoveListener(onMoveListener);

        floatingActionButton.setOnClickListener(v -> focusOnMarkedLocation());
    }

    private void addMarker(Point location) {
        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
        PointAnnotationManager pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, mapView);

        Drawable drawable = AppCompatResources.getDrawable(requireContext(), R.drawable.mapbox_baseline_location_on_24);
        if (drawable != null) {
            Bitmap bitmap = drawableToBitmap(drawable);
            if (bitmap != null) {
                PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                        .withPoint(location)
                        .withIconImage(bitmap);
                pointAnnotationManager.create(pointAnnotationOptions);
            } else {
                Toast.makeText(getContext(), "Icon image could not be loaded", Toast.LENGTH_SHORT).show();
            }
        }
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

    private final OnMoveListener onMoveListener = new OnMoveListener() {
        @Override
        public void onMoveBegin(@NonNull MoveGestureDetector moveGestureDetector) {
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

    private void focusOnMarkedLocation() {
        mapboxMap.setCamera(new CameraOptions.Builder()
                .center(markedLocation)
                .zoom(17.0)
                .build());
        floatingActionButton.hide();
    }
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_shop_location, menu); // Inflate the menu
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_select_shop_location) {
            List<ShopLocation> list = new ArrayList<>();
            list.add(new ShopLocation("1163 Đ. Trần Hưng Đạo, Phường 5, Quận 5, Hồ Chí Minh", 10.7527936, 106.6698121));
            list.add(new ShopLocation("847 Đ. La Thành, Láng Thượng, Ba Đình, Hà Nội", 21.025833, 105.7951791));
            list.add(new ShopLocation("177 Đ. Nguyễn Hoàng, Nam Dương, Hải Châu, Đà Nẵng", 16.0435949, 108.1709148));

            ShopLocationPopup.show(getContext(), list, new ShopLocationPopup.OnShopLocationSelectedListener() {
                @Override
                public void onShopLocationSelected(double latitude, double longitude) {
                    markedLocation = Point.fromLngLat(longitude, latitude);
                    mapboxMap.setCamera(new CameraOptions.Builder()
                            .center(markedLocation)
                            .zoom(17.0)
                            .build());
                    addMarker(markedLocation);
                }
            });

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
