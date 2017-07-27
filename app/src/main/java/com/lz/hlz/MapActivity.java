package com.lz.hlz;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.amap.api.col.f;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.lz.hlz.util.map.AMapUtil;
import com.lz.hlz.widget.TitleActivity;


public class MapActivity extends TitleActivity implements GeocodeSearch.OnGeocodeSearchListener {
    private com.amap.api.maps.AMap aMap;
    private MapView mapView;
    private GeocodeSearch geocoderSearch;
    private Marker geoMarker;
    private static LatLonPoint latLonPoint;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setTitle("牧羊犬");
        showBackwardView(R.string.go_back, true);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        double x = (double) getIntent().getSerializableExtra("x");
        double y = (double) getIntent().getSerializableExtra("y");
        latLonPoint = new LatLonPoint(x, y);
        initMap();
    }


    /**
     * 返回按钮点击后触发
     *
     * @param backwardView
     */
    protected void onBackward(View backwardView) {
        super.onBackward(backwardView);
        finish();
    }

    private void initMap() {

        if (aMap == null) {
            aMap = mapView.getMap();

            //用高德默认图标
            //geoMarker= aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            //自定义图标
            geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.punch_dw))));
        }
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                AMapUtil.convertToLatLng(latLonPoint), 15));
        aMap.setTrafficEnabled(false);// 显示实时交通状况
        UiSettings mUiSettings = aMap.getUiSettings();
        mUiSettings.setCompassEnabled(true);
        geoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
        getAddress(latLonPoint);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {

    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {

            } else {

            }
        } else {
        }
    }
}
