package com.gxdcnjq.sharedbikesmis.ui.track;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.gxdcnjq.sharedbikesmis.R;
import com.gxdcnjq.sharedbikesmis.entity.RideRecord;

public class TrackMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_map);

        // 获取传递过来的 RideRecord 对象
        RideRecord rideRecord = getIntent().getParcelableExtra("rideRecord");

        if (rideRecord != null) {
            // 在这里根据 rideRecord 中的位置信息显示轨迹，可以使用地图 SDK 或自定义视图来实现
            TrackMapFragment trackMapFragment = TrackMapFragment.newInstance(rideRecord.getLocationList());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.map_container, trackMapFragment)
                    .commit();
        }
    }

    // 处理悬浮的返回按钮点击事件
    public void onBackButtonClick(View view) {
        onBackPressed(); // 当返回按钮被点击时调用 onBackPressed() 方法
    }
}
