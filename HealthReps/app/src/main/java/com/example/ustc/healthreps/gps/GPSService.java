package com.example.ustc.healthreps.gps;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.citylist.CityList;
import com.example.ustc.healthreps.utils.AboutGPS;


/**
 * Created by CaoRuijuan on 1/4/16.
 */

public class GPSService extends Service implements LocationListener {
    int time = 0;

    // saving the context for later use
    private final Context mContext;

    // if GPS is enabled
    boolean isGPSEnabled = false;
    // if Network is enabled
    boolean isNetworkEnabled = false;
    // if Location co-ordinates are available using GPS or Network
    public boolean isLocationAvailable = false;

    // Location and co-ordinates coordinates
    Location mLocation;
    double mLatitude;
    double mLongitude;
    String mCity;

    // Minimum time fluctuation for next update (in milliseconds)
    private static final long TIME = 30000;
    // Minimum distance fluctuation for next update (in meters)
    private static final long DISTANCE = 20;

    // Declaring a Location Manager
    protected LocationManager mLocationManager;
    //百度地图定位
    private LocationClient locationClient = null;

    public GPSService(Context context) {
        this.mContext = context;
        mLocationManager = (LocationManager) mContext
                .getSystemService(LOCATION_SERVICE);
        getLocation();
        double latitude = getLatitude();
        if(latitude != 0){
            CurLocation.latitude = latitude;
        }

        double longitude = getLongitude();
        if(latitude != 0){
            CurLocation.lontitude = longitude;
        }

        String city = getLocationAddress();
        if(city == null){
            //loadLocation();
        }
        else
            CurLocation.city = city;
    }

    public void locationChooice(){
        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);
        mAlertDialog.setTitle("城市获取失败，检查网络?")
                .setMessage("当前网络未打开，是否打开网络?")
                .setPositiveButton("打开网络", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        mContext.startActivity(intent);
                    }
                })
                .setNeutralButton("手动选择城市", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(mContext, CityList.class);
                        mContext.startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    /**
     * Returs the Location
     *
     * @return Location or null if no location is found
     */
    public Location getLocation() {
        try {

            // Getting GPS status
            isGPSEnabled = mLocationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // If GPS enabled, get latitude/longitude using GPS Services
            if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, TIME, DISTANCE, this);
                if (mLocationManager != null) {
                    mLocation = mLocationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (mLocation != null) {
                        mLatitude = mLocation.getLatitude();
                        mLongitude = mLocation.getLongitude();
                        isLocationAvailable = true; // setting a flag that
                        // location is available
                        return mLocation;
                    }
                }
            }

            // If we are reaching this part, it means GPS was not able to fetch
            // any location
            // Getting network status
            isNetworkEnabled = AboutGPS.isNetworkAvailable(mContext);

            if (isNetworkEnabled) {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, TIME, DISTANCE, this);
                if (mLocationManager != null) {
                    mLocation = mLocationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (mLocation != null) {
                        mLatitude = mLocation.getLatitude();
                        mLongitude = mLocation.getLongitude();
                        isLocationAvailable = true; // setting a flag that
                        // location is available
                        return mLocation;
                    }
                }
            }
            // If reaching here means, we were not able to get location neither from GPS not Network,
            if (!isNetworkEnabled) {
                // so asking user to open GPS
               locationChooice();
            }
            else{
                if(!isGPSEnabled)
                    askUserToOpenGPS();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // if reaching here means, location was not available, so setting the
        // flag as false
        isLocationAvailable = false;
        return null;
    }

    /**
     * Gives you complete address of the location
     *
     * @return complete address in String
     */
    public String getLocationAddress() {

        if (isLocationAvailable) {

            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            // Get the current location from the input parameter list
            // Create a list to contain the result address
            List<Address> addresses = null;
            try {
				/*
				 * Return 1 address.
				 */
                addresses = geocoder.getFromLocation(mLatitude, mLongitude, 1);
            } catch (IOException e1) {
                e1.printStackTrace();
                return ("IO Exception trying to get address:" + e1);
            } catch (IllegalArgumentException e2) {
                // Error message to post in the log
                String errorString = "Illegal arguments "
                        + Double.toString(mLatitude) + " , "
                        + Double.toString(mLongitude)
                        + " passed to address service";
                e2.printStackTrace();
                return errorString;
            }
            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {
                // Get the first address
                Address address = addresses.get(0);
				/*
				 * Format the first line of address (if available), city, and
				 * country name.
				 */
                String addressText = address.getLocality();

                // Return the text
                return addressText;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * get latitude
     *
     * @return latitude in double
     */
    public double getLatitude() {
        if (mLocation != null) {
            mLatitude = mLocation.getLatitude();
        }
        return mLatitude;
    }

    /**
     * get longitude
     *
     * @return longitude in double
     */
    public double getLongitude() {
        if (mLocation != null) {
            mLongitude = mLocation.getLongitude();
        }
        return mLongitude;
    }

    /**
     * close GPS to save battery
     */
    public void closeGPS() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(GPSService.this);
        }
    }

    /**
     * show settings to open GPS
     */
    public void askUserToOpenGPS() {
        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        mAlertDialog.setTitle("GPS未打开，是否打开GPS?")
                .setMessage("当前GPS未打开，想要精确定位，请打开GPS?")
                .setPositiveButton("打开GPS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    /**
     * Updating the location when location changes
     */
    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}

