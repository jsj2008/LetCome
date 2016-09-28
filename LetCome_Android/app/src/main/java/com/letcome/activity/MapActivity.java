package com.letcome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.tools.Print;
import com.letcome.R;
import com.letcome.mode.ProductsRes;

public class MapActivity extends SuperActivity {
    Button mCloseBtn;
    TextView mProductName,mProductPrice,mContactInfo,mCity;
    MapView mMapView;
    ProductsRes.Record mRecord;

    public static void create(Activity activity,ProductsRes.Record record){
        Intent intent = new Intent(activity,MapActivity.class);
        intent.putExtra("record", record);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mRecord = (ProductsRes.Record)getIntent().getSerializableExtra("record");
        mCloseBtn = (Button)findViewById(R.id.close_btn);

        mProductName = (TextView)findViewById(R.id.product_name);
        mProductPrice = (TextView)findViewById(R.id.product_price);
        mContactInfo = (TextView)findViewById(R.id.contact_info);
        mCity = (TextView)findViewById(R.id.city_name);
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.this.finish();
            }
        });

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        initMap();

        mProductName.setText(mRecord.getDescription());
        mProductPrice.setText("¥" + mRecord.getPrice());
        mContactInfo.setText(mRecord.getContact_info());
        mCity.setText(mRecord.getCity());

    }

    void initMap(){
        BaiduMap mBaiduMap = mMapView.getMap();
        //定义Maker坐标点

        // 设置定位数据
        Print.i("ProductsActivity", "+++++++" + Double.valueOf(mRecord.getLatitude()) + "," + Double.valueOf(mRecord.getLongitude()));

        LatLng point = new LatLng(Double.valueOf(mRecord.getLatitude()), Double.valueOf(mRecord.getLongitude()));
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_marker);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);

        //设定中心点坐标

        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(point)
                .zoom(15)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
}
