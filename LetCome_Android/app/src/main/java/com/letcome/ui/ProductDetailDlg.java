package com.letcome.ui;

import android.app.Dialog;
import android.content.Context;
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
import com.gxq.tpm.tools.Print;
import com.letcome.R;
import com.letcome.mode.ProductsRes;

/**
 * Created by rjt on 16/9/12.
 */
public class ProductDetailDlg extends Dialog {
    Button mCloseBtn;
    TextView mProductName,mProductPrice;
    MapView mMapView;
    ProductsRes.Record mRecord;
    public ProductDetailDlg(Context context,ProductsRes.Record record) {
        this(context, R.style.Dialog_FS);
        mRecord = record;
    }

    public ProductDetailDlg(Context context, int themeResId) {
        super(context, themeResId);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_dialog);
        mCloseBtn = (Button)findViewById(R.id.close_btn);

        mProductName = (TextView)findViewById(R.id.product_name);
        mProductPrice = (TextView)findViewById(R.id.product_price);
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDetailDlg.this.hide();

            }
        });

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        BaiduMap mBaiduMap = mMapView.getMap();
        //定义Maker坐标点

        // 设置定位数据
        Print.i("ProductsActivity", "+++++++" + Double.valueOf(mRecord.getLatitude()) + "," + Double.valueOf(mRecord.getLongitude()));

        LatLng point = new LatLng(Double.valueOf(mRecord.getLatitude()), Double.valueOf(mRecord.getLongitude()));
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
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
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }
}
