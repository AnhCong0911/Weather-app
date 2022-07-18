package com.example.wa1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vn.thanguit.toastperfect.ToastPerfect;

public class MainActivity extends AppCompatActivity {

    private ImageButton mIBSearch;
    private ViewPager mViewPager;
    private List<CityName> cityList;

    private LinearLayout dotsLLayout;
    private TextView[] dots;
    private int[] layouts;

    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String cityName, cityNameFromAlert;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // chiếm toàn bộ màn hình điện thoại
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        // ????
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMISSION_CODE);
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        // init UI
        initUI();

        //cityName = getCityName(location.getLongitude(), location.getLatitude());
        cityName = getCityName(105.804817, 21.028511);
        Log.d("CITY_NAME: ", cityName);
        cityList = new ArrayList<>();
        cityList.add(new CityName(cityName));
        addBottomDots(0);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                cityList);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.addOnPageChangeListener(listener);
    }

    private void initUI() {
        dotsLLayout = (LinearLayout) findViewById(R.id.idLLBottom);
        mIBSearch = findViewById(R.id.idIBSearch);
        mIBSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mSearchDialog = new AlertDialog.Builder(MainActivity.this);
                mSearchDialog.setTitle("Search");

                final EditText inputCityName = new EditText(MainActivity.this);
                inputCityName.setInputType(InputType.TYPE_CLASS_TEXT);
                mSearchDialog.setView(inputCityName);
                String text = inputCityName.getText().toString();

                mSearchDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                mSearchDialog.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cityNameFromAlert = inputCityName.getText().toString();
                        FragmentManager fm = getSupportFragmentManager();
                        TempDialogFragment tempDialogFragment = TempDialogFragment.newInstance(cityNameFromAlert);
                        tempDialogFragment.show(fm, null);
                    }
                });
                mSearchDialog.show();
            }
        });
        mViewPager = findViewById(R.id.idVPWeatherData);
    }

    public void addBottomDots(int currentPage) {
        dots = new TextView[cityList.size()];
        dotsLLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.white_dark));
            dotsLLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[currentPage].setTextColor(getResources().getColor(R.color.white));
        }
    }

    private String getCityName(double longitude, double latitude) {
        String cityName = "Not Found!";
        // ???
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null) {
            cityName = addresses.get(0).getAdminArea();
        }
        return cityName;
    }

    public void addOnListAndUpdateAdapter(String mCityName) {
        cityList.add(new CityName(mCityName));
        addBottomDots(0);
        viewPagerAdapter.notifyDataSetChanged();
    }

    public void displayMessage(int messageType, String message) {
        ToastPerfect.makeText(getApplicationContext(), messageType, message,
                ToastPerfect.BOTTOM, Toast.LENGTH_SHORT).show();
    }

    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
}