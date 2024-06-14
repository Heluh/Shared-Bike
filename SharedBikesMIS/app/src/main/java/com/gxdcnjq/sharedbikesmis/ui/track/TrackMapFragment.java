package com.gxdcnjq.sharedbikesmis.ui.track;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.gxdcnjq.sharedbikesmis.R;
import com.gxdcnjq.sharedbikesmis.entity.Location;

import java.util.ArrayList;
import java.util.List;

public class TrackMapFragment extends Fragment {
    private MapView mapView;
    private AMap aMap;
    private List<Location> locationList;

    public static TrackMapFragment newInstance(List<Location> locationList) {
        TrackMapFragment fragment = new TrackMapFragment();
        fragment.locationList = locationList;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_track_map, container, false);

        mapView = rootView.findViewById(R.id.track_map_view);
        mapView.onCreate(savedInstanceState);

        // 获取 AMap 实例
        aMap = mapView.getMap();

        // 在地图上绘制折线
        drawPolylineOnMap();

        return rootView;
    }

    private BitmapDescriptor resizeBitmap(int drawableResId, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), drawableResId);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return BitmapDescriptorFactory.fromBitmap(resizedBitmap);
    }

    private void drawPolylineOnMap() {
        if (aMap == null || locationList == null || locationList.isEmpty()) {
            return;
        }

        List<LatLng> latLngList = new ArrayList<>();
        for (Location location : locationList) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            latLngList.add(latLng);
        }

        // 绘制折线
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(latLngList)
                .color(getResources().getColor(R.color.teal_700)) // 自定义颜色
                .width(10);

        aMap.addPolyline(polylineOptions);

        // 为起点和终点添加标记
        if (!latLngList.isEmpty()) {
            // 起点标记
            LatLng startPoint = latLngList.get(0);
            BitmapDescriptor startIcon = resizeBitmap(R.drawable.start, 60, 60); // 调整大小为 50x50
            aMap.addMarker(new MarkerOptions()
                    .position(startPoint)
                    .icon(startIcon)
                    .title("起点"));

            // 终点标记
            LatLng endPoint = latLngList.get(latLngList.size() - 1);
            BitmapDescriptor endIcon = resizeBitmap(R.drawable.end, 60, 60); // 调整大小为 50x50
            aMap.addMarker(new MarkerOptions()
                    .position(endPoint)
                    .icon(endIcon)
                    .title("终点"));
        }

        // 调整地图视图以显示所有折线点
        adjustMapViewport(latLngList);
    }

    private void adjustMapViewport(List<LatLng> latLngList) {
        if (latLngList.isEmpty()) {
            return;
        }

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLng : latLngList) {
            boundsBuilder.include(latLng);
        }
        LatLngBounds bounds = boundsBuilder.build();

        int padding = 100; // 调整视图内边距
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
