package com.gxq.tpm.tools;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by rjt on 16/9/26.
 */
public class BaiduLocationUtils {
    static  private BaiduLocationUtils instance;

    static public BDLocation mLocation;

    // 定位相关
    private LocationClient mLocClient;
    private BDLocationListener myListener;

    static public void createtLocationAndCity(Context context) {
        BaiduLocationUtils.getInstance().getLoactionOnce(context);
    }

    private BaiduLocationUtils(){
        super();
    }

    static public BaiduLocationUtils getInstance(){
        if (instance==null){
            instance = new BaiduLocationUtils();
        }
        return instance;
    }

    public void getLoactionOnce(Context context){
        myListener = new OnceLocationListenner();
        // 定位初始化
        mLocClient = new LocationClient(context);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setAddrType("all");
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }



    /**
     * 定位SDK监听函数
     */
    private class OnceLocationListenner implements BDLocationListener {


        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            mLocation = location;
            Print.i("ProductsActivity", "*******" + location.getLatitude() + "," + location.getLongitude()+","+ location.getCity()+","+location.getCityCode());
            mLocClient.stop();//仅仅获取一次
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
}