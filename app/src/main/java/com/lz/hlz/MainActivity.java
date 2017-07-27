package com.lz.hlz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.lz.hlz.fragment.NavFragment;
import com.lz.hlz.fragment.NavigationButton;
import com.lz.hlz.interf.OnTabReselectListener;
import com.lz.hlz.util.map.Utils;
import com.lz.hlz.widget.TitleActivity;

import kr.co.namee.permissiongen.PermissionGen;


public class MainActivity extends TitleActivity implements NavFragment.OnNavigationReselectListener, AMapLocationListener {
    private Button mBackwardbButton;
    private Button mForwardButton;
    private NavFragment mNavBar;
    private String[] strMsg;
    private double x = 0.0;
    private double y = 0.0;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionGen.with(MainActivity.this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.RECORD_AUDIO)
                .request();
        mBackwardbButton = (Button) findViewById(R.id.button_backward);
        mForwardButton = (Button) findViewById(R.id.button_forward);
        setTitle("小肥羊");
        showBackwardView(R.string.gps_button, true);
        showForwardView(R.string.call_button, true);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.gps);
        Drawable drawable2 = ContextCompat.getDrawable(this, R.drawable.call);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        drawable2.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mBackwardbButton.setCompoundDrawables(null, drawable, null, null);  //设置按钮上下左右图片
        mForwardButton.setCompoundDrawables(null, drawable2, null, null);  //设置按钮上下左右图片
//        mBackwardbButton.setBackgroundResource(R.drawable.gps); //设置按钮图片
        FragmentManager manager = getSupportFragmentManager();
        mNavBar = ((NavFragment) manager.findFragmentById(R.id.fag_nav));
        mNavBar.setup(this, manager, R.id.main_container, this);
//        ScrollView.addHeaderView(banner);

        Location();
    }

    public void onReselect(NavigationButton navigationButton) {
        Fragment fragment = navigationButton.getFragment();
        if (fragment != null
                && fragment instanceof OnTabReselectListener) {
            OnTabReselectListener listener = (OnTabReselectListener) fragment;
            listener.onTabReselect();
        }
    }

    /**
     * 返回按钮点击后触发
     *
     * @param backwardView
     */
    protected void onBackward(View backwardView) {
        if(x!=0.0&&y!=0.0){
            Intent intent = new Intent();
            intent.setClass(this, MapActivity.class);
            intent.putExtra("x",x);
            intent.putExtra("y",y);
            this.startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "定位信号弱，暂时无法查看当前位置", Toast.LENGTH_SHORT).show();
        }
        super.onBackward(backwardView);

    }

    /**
     * 提交按钮点击后触发
     *
     * @param forwardView
     */
    protected void onForward(View forwardView) {
        getroot();
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void getroot() {
        // 检查是否获得了权限（Android6.0运行时权限）
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // 没有获得授权，申请授权
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.CALL_PHONE)) {
                // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                // 弹窗需要解释为何需要该权限，再次请求授权
                Toast.makeText(MainActivity.this, "请授权！", Toast.LENGTH_LONG).show();

                // 帮跳转到该应用的设置界面，让用户手动授权
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            } else {
                // 不需要解释为何需要该权限，直接请求授权
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        1);
            }
        } else {
            // 已经获得授权，可以打电话
            CallPhone();
        }

    }

    // 处理权限申请的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 授权成功，继续打电话
                    CallPhone();
                } else {
                    // 授权失败！
                    Toast.makeText(this, "授权失败！", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }

    }

    public void CallPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "18396879339".trim()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);//调用上面这个intent实现拨号
    }

    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
            Message msg = mHandler.obtainMessage();
            msg.obj = loc;
            msg.what = Utils.MSG_LOCATION_FINISH;
            mHandler.sendMessage(msg);
        }

    }

    Handler mHandler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
                //定位完成
                case Utils.MSG_LOCATION_FINISH:
                    String result = "";
                    try {
                        AMapLocation loc = (AMapLocation) msg.obj;
                        result = Utils.getLocationStr(loc, 1);
                        strMsg = result.split(",");
                        Toast.makeText(MainActivity.this, "定位成功", Toast.LENGTH_LONG).show();
                        setBackButtonTitle(strMsg[3]);
                        //    textView.setText("地址：" + strMsg[0] + "\n" + "经    度：" + strMsg[1] + "\n" + "纬    度：" + strMsg[2]);
                        x = Double.valueOf(strMsg[2]);
                        y = Double.valueOf(strMsg[1]);
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "定位失败", Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
        }

        ;

    };

    public void Location() {
        // TODO Auto-generated method stub
        try {
            locationClient = new AMapLocationClient(this);
            locationOption = new AMapLocationClientOption();
            // 设置定位模式为低功耗模式
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            // 设置定位监听
            locationClient.setLocationListener(this);
            locationOption.setOnceLocation(true);//设置为单次定位
            locationClient.setLocationOption(locationOption);// 设置定位参数
            // 启动定位
            locationClient.startLocation();
            mHandler.sendEmptyMessage(Utils.MSG_LOCATION_START);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "定位失败", Toast.LENGTH_LONG).show();
        }
    }
}
