package com.gxdcnjq.sharedbikesmis.ui.mine;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.math.MathUtils;
import androidx.fragment.app.Fragment;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.ArcOptions;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.gxdcnjq.sharedbikesmis.R;
import com.gxdcnjq.sharedbikesmis.constant.ApiConstants;
import com.gxdcnjq.sharedbikesmis.entity.Location;
import com.gxdcnjq.sharedbikesmis.entity.RideRecord;
import com.gxdcnjq.sharedbikesmis.entity.TrackRecord;
import com.gxdcnjq.sharedbikesmis.entity.TrackRecordResponse;
import com.gxdcnjq.sharedbikesmis.ui.track.TrackMapActivity;
import com.gxdcnjq.sharedbikesmis.utils.MapUtil;
import com.gxdcnjq.sharedbikesmis.utils.OKHttpUtil;
import com.gxdcnjq.sharedbikesmis.utils.TimeUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RideRecordFragment extends Fragment {

    private ListView rideRecordListView;
    private List<TrackRecord> trackRecordList;
    private List<RideRecord> rideRecordList;
    private ArrayAdapter<String> rideRecordAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_record, container, false);


        rideRecordListView = view.findViewById(R.id.rideRecordListView);
        trackRecordList = new ArrayList<>();


        trackRecordList = getData();
        rideRecordList = process(trackRecordList);


        CustomAdapter adapter = new CustomAdapter(getContext(), rideRecordList);
        rideRecordListView.setAdapter(adapter);


        rideRecordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取选中的 RideRecord 对象
                RideRecord rideRecord = rideRecordList.get(position);

                // 创建意图，并将 RideRecord 对象传递到 TrackMapActivity 中
                Intent intent = new Intent(getContext(), TrackMapActivity.class);
                intent.putExtra("rideRecord", rideRecord);

                // 使用 startActivity 启动 TrackMapActivity
                startActivity(intent);
            }
        });




        return view;
    }

    public class CustomAdapter extends ArrayAdapter<RideRecord> {

        public CustomAdapter(Context context, List<RideRecord> data) {
            super(context, R.layout.ride_list_item, data);
        }

        @NonNull
        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.ride_list_item, parent, false);
            }

            ImageView imageView = convertView.findViewById(R.id.ride_item_image);
            TextView ride_id_text = convertView.findViewById(R.id.ride_id_text);
            TextView ride_start_text = convertView.findViewById(R.id.ride_start_text);
            TextView ride_end_text = convertView.findViewById(R.id.ride_end_text);

            // 设置每个列表项的数据
            RideRecord itemData = getItem(position);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            assert itemData != null;
            String formattedDistance = decimalFormat.format(itemData.getDistance()) + " km";
            ride_id_text.setText("距离 ： "+formattedDistance);
            ride_start_text.setText("开始 ： "+TimeUtils.formatDateTime(itemData.getStartTime()));
            ride_end_text.setText("时长 ： " + TimeUtils.formatDuration(itemData.getDuration()));
            imageView.setImageResource(R.drawable.bike);

            return convertView;
        }
    }

    public List<TrackRecord> getData() {
        List<TrackRecord> trackRecordList = new ArrayList<>();

        String res = OKHttpUtil.getSyncRequest(ApiConstants.BASE_URL_HTTP, "record");
        if (res != null) {
            Log.d("Pan", res);
            Gson gson = new Gson();
            try {
                TrackRecordResponse response = gson.fromJson(res, TrackRecordResponse.class);
                if ("0".equals(response.getCode())) {
                    trackRecordList = response.getData();
                } else {
                    Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Pan", "Data parsing error");
            }
        } else {
            Toast.makeText(getActivity(), "Server connection timeout", Toast.LENGTH_SHORT).show();
        }

        return trackRecordList;
    }


    public List<RideRecord> process(List<TrackRecord> trackRecordList) {
        Map<String, RideRecord> rideMap = new HashMap<>();

        for (TrackRecord track : trackRecordList) {
            String recordId = String.valueOf(track.getId());
            String createTime = track.getTime();
            double trackDistance = track.getDistance();
            long duration = track.getDuration();
            String[] latitudes = track.getLatitude().split(",");
            String[] longitudes = track.getLongitude().split(",");
            RideRecord rideRecord = new RideRecord(recordId, createTime, duration);
            rideRecord.setStartTime(createTime);
            rideRecord.setDistance(trackDistance);
            rideMap.put(recordId, rideRecord);

            for (int i = 0; i < latitudes.length; i++) {
                double latitude = Double.parseDouble(latitudes[i].trim());
                double longitude = Double.parseDouble(longitudes[i].trim());
//                double bdLatitude = Double.parseDouble(latitudes[i].trim());
//                double bdLongitude = Double.parseDouble(longitudes[i].trim());
//                double[] wgs84 = MapUtil.bd09ToWgs84(bdLongitude, bdLatitude);
//                double latitude = wgs84[1];
//                double longitude = wgs84[0];
                Location location = new Location(latitude, longitude);
                rideRecord.getLocationList().add(location);
            }
        }

        List<RideRecord> rideList = new ArrayList<>(rideMap.values());
        rideList.sort(Comparator.comparing(RideRecord::getStartTime));
        return rideList;
    }


    public class MapDialog extends Dialog {

        private List<Location> locationList;

        public MapDialog(@NonNull Context context, List<Location> locationList) {
            super(context);
            this.locationList = locationList;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_map);

            // 初始化地图控件
            MapView mapView = findViewById(R.id.map_view2);
            mapView.onCreate(savedInstanceState);

            // 获取地图对象
            AMap aMap = mapView.getMap();

            // 在地图上绘制轨迹线
            drawPolylineOnMap(aMap);
        }

        private void drawPolylineOnMap(AMap aMap) {
            // 创建轨迹线的经纬度点列表
            List<LatLng> latLngList = new ArrayList<>();
            for (Location location : locationList) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                latLngList.add(latLng);
            }

            calBorder(aMap);



            // 绘制轨迹线
            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(latLngList)
                    .color(Color.argb(200,0, 150, 136))
                    .width(7f);
            aMap.addPolyline(polylineOptions);

        }

        private void calBorder(AMap aMap){
            double minLatitude = Double.MAX_VALUE;
            double maxLatitude = Double.MIN_VALUE;
            double minLongitude = Double.MAX_VALUE;
            double maxLongitude = Double.MIN_VALUE;

            // 遍历所有坐标点，更新边界值
            for (Location location : locationList) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                if (latitude < minLatitude) {
                    minLatitude = latitude;
                }
                if (latitude > maxLatitude) {
                    maxLatitude = latitude;
                }
                if (longitude < minLongitude) {
                    minLongitude = longitude;
                }
                if (longitude > maxLongitude) {
                    maxLongitude = longitude;
                }
            }

            // 创建边界框
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(new LatLng(minLatitude, minLongitude))
                    .include(new LatLng(maxLatitude, maxLongitude))
                    .build();

            int padding = 100; // 可以根据需求调整
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            aMap.moveCamera(cameraUpdate);
        }
    }


    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 地球半径（单位：千米）

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c; // 距离（单位：千米）
        return distance;
    }


}
